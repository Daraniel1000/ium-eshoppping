import pandas as pd

from server import loaders, predictions


class Server:
    def __init__(self, mode, data_base_path="data/", models_base_path="models/", verbose=True):
        if verbose:
            print("Loading server. It may take a while...")

        data_base_path = self.normalize_dir_path(data_base_path)
        models_base_path = self.normalize_dir_path(models_base_path)

        self.data_base_path = data_base_path

        self.users_file_name = "users.jsonl"
        self.products_file_name = "products.jsonl"
        self.sessions_file_name = "sessions.jsonl"
        self.abtest_file_name = "abtest.jsonl"

        self.org_users_df, self.users_df = loaders.load_users(data_base_path, self.users_file_name)
        self.org_products_df, self.products_df = loaders.load_products(data_base_path, self.products_file_name)
        self.org_sessions_df, self.sessions_df = loaders.load_sessions(data_base_path, self.products_df,
                                                                       self.sessions_file_name)

        self.users_api_df = self.users_df[["user_id", "name"]]
        self.categories_api_df = pd.DataFrame(self.products_df["category"].unique(), columns=["name"])
        self.products_api_df = self.products_df

        self.encoder = loaders.load_encoder()
        self.scaler = loaders.load_scaler()

        self.is_ab_mode = mode == 'AB'

        if self.is_ab_mode:
            model_a = loaders.load_model(models_base_path + "A.pkl")
            model_b = loaders.load_model(models_base_path + "B.pkl")
            self.model_assignment = {0: model_a, 1: model_b}
            self.ab_test_df = loaders.load_abtest(data_base_path, self.abtest_file_name)
        else:
            model_a = loaders.load_model(models_base_path + "A.pkl")
            self.model_assignment = {0: model_a, 1: model_a}

        self.session_id_free = self.sessions_df["session_id"].dropna().astype('int').max() + 1
        self.purchase_id_free = self.sessions_df["purchase_id"].dropna().astype('int').max() + 1

    @staticmethod
    def normalize_dir_path(path):
        return path + '/' if path[-1] != '/' else path

    @staticmethod
    def assigned_group(uid):
        return uid % 2

    def generate_session_id(self):
        self.session_id_free += 1
        return self.session_id_free - 1

    def generate_purchase_id(self):
        self.purchase_id_free += 1
        return self.purchase_id_free - 1

    def get_users_dict(self):
        return self.users_api_df.to_dict(orient="records")

    def get_categories_dict(self):
        return self.categories_api_df.to_dict(orient="records")

    def get_products_dict(self, category):
        return self.products_api_df[self.products_api_df["category"] == category].to_dict(orient="records")

    def get_prediction(self, user, product):
        if not user.isdigit():
            raise ValueError("Parameter \"user\" is not an integer")
        return predictions.predict_discount(user, product, self.users_df, self.products_df, self.sessions_df,
                                            self.model_assignment[self.assigned_group(int(user))], self.encoder,
                                            self.scaler)

    def register_view(self, session, user, product, offered_discount):
        if not ((isinstance(session, int) or session.isdigit()) and int(session) < self.session_id_free):
            raise ValueError("Parameter \"session\" is invalid")
        if not (isinstance(user, int) or user.isdigit()):
            raise ValueError("Parameter \"user\" is not an integer")
        if not (isinstance(product, int) or product.isdigit()):
            raise ValueError("Parameter \"product\" is not an integer")
        if not (isinstance(offered_discount, int) or offered_discount.isdigit()):
            raise ValueError("Parameter \"offered_discount\" is not an integer")
        self.sessions_df = self.sessions_df.append(
            {'session_id': session, 'user_id': user, 'product_id': product, 'event_type': "VIEW_PRODUCT",
             'offered_discount': int(offered_discount), 'timestamp': pd.to_datetime('now')}, ignore_index=True)
        self.org_sessions_df = self.org_sessions_df.append(
            {'session_id': int(session), 'user_id': int(user), 'product_id': int(product), 'event_type': "VIEW_PRODUCT",
             'offered_discount': int(offered_discount),
             'timestamp': pd.to_datetime('now').strftime("%Y-%m-%dT%H:%M:%S")}, ignore_index=True)
        if self.is_ab_mode:
            self.ab_test_df = self.ab_test_df.append(
                {'event_type': "VIEW_PRODUCT", 'user_id': int(user), 'product_id': int(product),
                 "offered_discount": int(offered_discount)}, ignore_index=True)

    def register_buy(self, session, user, product, offered_discount):
        if not ((isinstance(session, int) or session.isdigit()) and int(session) < self.session_id_free):
            raise ValueError("Parameter \"session\" is invalid")
        if not (isinstance(user, int) or user.isdigit()):
            raise ValueError("Parameter \"user\" is not an integer")
        if not (isinstance(product, int) or product.isdigit()):
            raise ValueError("Parameter \"product\" is not an integer")
        if not (isinstance(offered_discount, int) or offered_discount.isdigit()):
            raise ValueError("Parameter \"offered_discount\" is not an integer")
        self.sessions_df = self.sessions_df.append(
            {'session_id': session, 'user_id': user, 'product_id': product, 'event_type': "BUY_PRODUCT",
             'offered_discount': int(offered_discount), 'timestamp': pd.to_datetime('now'),
             'purchase_id': str(self.generate_purchase_id())}, ignore_index=True)
        self.org_sessions_df = self.org_sessions_df.append(
            {'session_id': int(session), 'user_id': int(user), 'product_id': int(product), 'event_type': "BUY_PRODUCT",
             'offered_discount': int(offered_discount),
             'timestamp': pd.to_datetime('now').strftime("%Y-%m-%dT%H:%M:%S"),
             'purchase_id': self.generate_purchase_id()}, ignore_index=True)
        if self.is_ab_mode:
            self.ab_test_df = self.ab_test_df.append(
                {'event_type': "BUY_PRODUCT", 'user_id': int(user), 'product_id': int(product),
                 "offered_discount": int(offered_discount)}, ignore_index=True)

    def dump_state(self):
        self.org_sessions_df.to_json(self.data_base_path + self.sessions_file_name, orient='records', lines=True)
        if self.is_ab_mode:
            self.ab_test_df.to_json(self.data_base_path + self.abtest_file_name, orient='records', lines=True)

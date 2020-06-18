import numpy as np
import pandas as pd


def load_dataset(base_path, file):
    if base_path[-1] != '/':
        base_path += '/'

    return pd.read_json(base_path + file, lines=True)


def aggregate(users_df, products_df, sessions_df):
    columns = ["session_id", "user_id", "product_id", "offered_discount", "event_type"]
    df = sessions_df.groupby(columns)["timestamp"].min().unstack()
    df = pd.DataFrame({
        "time": df.max(axis=1),
        "bought": ~df["BUY_PRODUCT"].isna()
    }, index=df.index).reset_index()
    df = df.merge(users_df.rename({"user_id": "id"}, axis=1).add_prefix("user_"), on="user_id", how="left")
    df = df.merge(products_df.rename({"product_id": "id", "product_name": "name"}, axis=1).add_prefix("product_"),
                  on="product_id", how="left")

    df["offered_price"] = (1 - 0.01 * df["offered_discount"]) * df["product_price"]
    df["year"] = df['time'].dt.year
    df["month"] = df['time'].dt.month
    df["day"] = df['time'].dt.day
    df["hour"] = df['time'].dt.hour

    # Sinusy/cosinusy żeby wartości były cykliczne

    df['month_sin'] = np.sin((df["month"] - 1) * (2. * np.pi / 12))
    df['month_cos'] = np.cos((df["month"] - 1) * (2. * np.pi / 12))

    df['day_sin'] = np.sin((df["day"] - 1) * (2. * np.pi / df["time"].dt.daysinmonth))
    df['day_cos'] = np.cos((df["day"] - 1) * (2. * np.pi / df["time"].dt.daysinmonth))

    def get_aggregations(df):
        a = df.sort_values(by="time")[["user_id", "product_category", "bought", "offered_price", "offered_discount"]]

        a = pd.concat([
            a,
            pd.get_dummies(a["offered_discount"]).add_prefix("discount_").mask(a["bought"] == False, 0)
        ], axis=1).drop("offered_discount", axis=1)
        a["offered_price"] = a["offered_price"].mask(a["bought"] == False)

        pcd = a.groupby(["user_id", "product_category"])[a.columns[4:]].transform(
            lambda x: x.expanding().sum().shift(fill_value=0)
        ).add_prefix("previous_category_").sort_index().astype(int)

        pad = a.groupby(["user_id"])[a.columns[4:]].transform(
            lambda x: x.expanding().sum().shift(fill_value=0)
        ).add_prefix("previous_all_").sort_index().astype(int)

        return [
            # ile razy wcześniej kupił z każdą zniżką w danej kategorii
            pcd,

            # ile razy wcześniej kupił ze zniżkami mniejszym i równymi/większymi niż każda zniżka w danej kategorii
            pd.concat([pcd.iloc[:, :(i + 1)].sum(axis=1).to_frame(name=pcd.columns[i] + "_less_equal") for i in
                       range(len(pcd.columns))] +
                      [pcd.iloc[:, (i + 1):].sum(axis=1).to_frame(name=pcd.columns[i] + "_greater") for i in
                       range(len(pcd.columns))], axis=1),

            # ile razy wcześniej kupił z każdą zniżką w sumie
            pad,

            # ile razy wcześniej kupił ze zniżkami mniejszym i równymi/większymi niż każda zniżka w sumie
            pd.concat([pad.iloc[:, :(i + 1)].sum(axis=1).to_frame(name=pad.columns[i] + "_less_equal") for i in
                       range(len(pad.columns))] +
                      [pad.iloc[:, (i + 1):].sum(axis=1).to_frame(name=pad.columns[i] + "_greater") for i in
                       range(len(pad.columns))], axis=1),

            # ile średnie wcześniej wydał w danej kategorii
            a.groupby(["user_id", "product_category"])[a.columns[3]].transform(
                lambda x: x.expanding().mean().shift()
            ).sort_index().fillna(0).to_frame(name="mean_previous_category_price")
        ]

    df = pd.concat([df] + get_aggregations(df), axis=1)

    # dla braku poprzednich wartości ustawimy stosunek równy 1, bo taki najmniej mówi o zakupie
    df["ratio"] = (df["offered_price"] / df['mean_previous_category_price']).replace(np.inf, 1)

    # dla wszystkich zniżek -> dla danej zniżki
    df["previous_all_discount_less_equal"] = df.lookup(df.index,
                                                       "previous_all_discount_" + df['offered_discount'].astype(
                                                           str) + "_less_equal")
    df["previous_all_discount_greater"] = df.lookup(df.index, "previous_all_discount_" + df['offered_discount'].astype(
        str) + "_greater")
    df["previous_category_discount_less_equal"] = df.lookup(df.index, "previous_category_discount_" + df[
        'offered_discount'].astype(str) + "_less_equal")
    df["previous_category_discount_greater"] = df.lookup(df.index,
                                                         "previous_category_discount_" + df['offered_discount'].astype(
                                                             str) + "_greater")

    return df


def load_users(base_path, name="users.jsonl"):
    users_df = load_dataset(base_path, name)
    users_df["user_id"] = users_df["user_id"].astype(int).astype(str)  # convert id to string
    return users_df[["user_id", "name", "city", "street"]]  # explicitly choose columns


def load_products(base_path, name="products.jsonl"):
    products_df = load_dataset(base_path, name)
    products_df.loc[products_df["price"] < 0, "price"] *= -1  # invert negative values
    products_df = products_df[products_df["price"] < 10 * products_df.groupby("category_path")["price"].transform(
        "median")]  # drop price outliers
    products_df["product_id"] = products_df["product_id"].astype(int).astype(str)  # convert id to string
    products_df["category"] = products_df["category_path"].str.rpartition(';')[2]  # convert category_path to category
    return products_df[["product_id", "product_name", "price", "category"]]  # explicitly choose columns


def load_sessions(base_path, products_df, name="sessions.jsonl"):
    sessions_df = load_dataset(base_path, name)
    sessions_df = sessions_df[sessions_df["product_id"].isin(
        products_df["product_id"])].copy()  # drop sessions not connected to existing products
    sessions_df["timestamp"] = pd.to_datetime(sessions_df["timestamp"],
                                              errors='coerce')  # convert timestamp to datetime
    sessions_df = sessions_df.fillna(
        {"user_id": sessions_df.groupby("session_id")["user_id"].transform('min')})  # fill ids inside session
    sessions_df = sessions_df.dropna(subset=["user_id", "product_id"])  # drop missing values
    sessions_df["session_id"] = sessions_df["session_id"].astype(int).astype(str)  # convert id to string
    sessions_df["user_id"] = sessions_df["user_id"].astype(int).astype(str)  # convert id to string
    sessions_df["product_id"] = sessions_df["product_id"].astype(int).astype(str)  # convert id to string
    return sessions_df[["session_id", "timestamp", "user_id", "product_id", "event_type", "offered_discount",
                        "purchase_id"]]  # explicitly choose columns


def get_api_users(base_path):
    return load_users(base_path)[["user_id", "name"]]


def get_api_categories(base_path):
    return pd.DataFrame(load_products(base_path)["category"].unique(), columns=["name"])


def get_api_products(base_path):
    return load_products(base_path)


def get_aggregated(base_path):
    users_df = load_users(base_path)
    products_df = load_products(base_path)
    sessions_df = load_sessions(base_path, products_df)
    return aggregate(users_df,
                     products_df,
                     sessions_df)

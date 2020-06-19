import pickle

import pandas as pd


def load_dataset(base_path, file):
    if base_path[-1] != '/':
        base_path += '/'

    try:
        return pd.read_json(base_path + file, lines=True)
    except Exception as e:
        raise IOError("Can't create dataframe from " + base_path+file + ". " + str(e))


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


def load_pickled(path):
    with open(path, 'rb') as f:
        return pickle.load(f)


def load_model(path):
    return load_pickled(path)


def load_encoder(path="tools/encoder.pkl"):
    return load_pickled(path)


def load_scaler(path="tools/scaler.pkl"):
    return load_pickled(path)

import numpy as np
import pandas as pd


def replace(value, bad, good):
    return value if value is not bad else good


def get_input_vector(user_id, product_id, products, sessions, time, offered_discount):
    # trigonometric functions of time (so it's cyclical)
    month_sin = np.sin((time.month - 1) * (2. * np.pi / 12))
    month_cos = np.cos((time.month - 1) * (2. * np.pi / 12))
    day_sin = np.sin((time.day - 1) * (2. * np.pi / time.daysinmonth))
    day_cos = np.cos((time.day - 1) * (2. * np.pi / time.daysinmonth))

    category = products[products["product_id"] == product_id]["category"].values[0]

    # get previous sessions of this user
    t = sessions[(sessions["user_id"] == user_id) & (sessions["timestamp"] < time)][
        ["session_id", "product_id", "event_type", "offered_discount", "timestamp"]]

    t = t.merge(products[["product_id", "price", "category"]], on="product_id").drop("product_id", axis=1)

    # get first time of event (nan if didn't happen)
    t = t.groupby(t.columns.drop(["event_type", "timestamp"]).tolist() + ["event_type"])[
        "timestamp"].min().unstack()

    # check if there were any bought events
    if "BUY_PRODUCT" in t.columns:
        # is bought time is nan, then the product was not bought in that session
        t = (~t["BUY_PRODUCT"].isna()).reset_index().drop("session_id", axis=1)
    else:
        t = pd.DataFrame(columns=["offered_discount", "price", "category", "BUY_PRODUCT"])

    # keep only sessions where the users bought something
    t = t[t["BUY_PRODUCT"] == True]

    # offered price of products
    t["offered_price"] = (1 - 0.01 * t["offered_discount"]) * t["price"]

    # get mean spending of user in current category
    mean_previous_category_price = replace(t[t["category"] == category]["offered_price"].mean(), np.nan, 0)
    product_price = products[products["product_id"] == product_id]["price"].values[0]
    offered_price = (1 - 0.01 * offered_discount) * product_price

    # ratio of current price to mean previous spending
    ratio = 1 if mean_previous_category_price == 0 else replace(offered_price / mean_previous_category_price,
                                                                np.inf, 1)
    # how many times the user bought anything at less or equal discount
    previous_all_discount_less_equal = replace(t[t["offered_discount"] <= offered_discount]["price"].count(), np.nan,
                                               0)
    # how many times the user bought anything at greater discount
    previous_all_discount_greater = replace(t[t["offered_discount"] > offered_discount]["price"].count(), np.nan, 0)
    # how many times the user bought products in current category at less or equal discount
    previous_category_discount_less_equal = replace(
        t[(t["offered_discount"] <= offered_discount) & (t["category"] == category)]["price"].count(), np.nan, 0)
    # how many times the user bought products in current category at greater discount
    previous_category_discount_greater = replace(
        t[(t["offered_discount"] > offered_discount) & (t["category"] == category)]["price"].count(), np.nan, 0)

    return [offered_discount,
            product_price,
            offered_price,
            month_sin,
            month_cos,
            day_sin,
            day_cos,
            mean_previous_category_price,
            ratio,
            previous_all_discount_less_equal,
            previous_all_discount_greater,
            previous_category_discount_less_equal,
            previous_category_discount_greater]


def predict_discount(user_id, product_id, products, sessions, model):
    if products[products["product_id"] == product_id].empty:
        raise IndexError("Product id " + product_id + " not found")

    time = pd.to_datetime('now')
    discounts = [0, 5, 10, 15, 20]

    for offered_discount in discounts:
        input_vector = get_input_vector(user_id, product_id, products, sessions, time, offered_discount)

        will_buy = model.predict([input_vector])[0]

        if will_buy:
            return offered_discount

    return discounts[-1]

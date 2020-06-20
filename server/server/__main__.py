import argparse

import pandas as pd
from flask import Flask, jsonify, request, abort, render_template

from server import loaders
from server import predictions

app = Flask(__name__)
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True


@app.route('/', methods=['GET', 'POST'])
def index():
    return render_template("index.html")


@app.route('/users', methods=['GET'])
def users():
    return jsonify({
        "users": users_api_df.to_dict(orient="records")
    })


@app.route('/categories', methods=['GET'])
def categories():
    return jsonify({
        'categories': categories_api_df.to_dict(orient="records")
    })


@app.route('/products', methods=['GET'])
def products():
    category = request.args.get("category")
    if not category:
        abort(400, "Parameter \"category\" not passed")
    return jsonify({
        'products': products_api_df[products_api_df["category"] == category].to_dict(orient="records")
    })


@app.route('/predict', methods=['GET'])
def predict():
    user_id = request.args.get("user")
    product_id = request.args.get("product")
    if not user_id:
        abort(400, "Parameter \"user\" not passed")
    if not product_id:
        abort(400, "Parameter \"product\" not passed")
    if not user_id.isdigit():
        abort(400, "Parameter \"user\" is not an integer")
    try:
        return jsonify({
            'predicted_discount': predictions.predict_discount(user_id, product_id, users_df, products_df, sessions_df,
                                                               model_assignment[assigned_group(int(user_id))], encoder,
                                                               scaler)
        })
    except IndexError as e:
        abort(400, str(e))


def assigned_group(uid):
    return uid % 2


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="eShoppping server")
    parser.add_argument('port', nargs='?', type=int, default=8080)

    args = parser.parse_args()

    print("Loading server. It may take a while...")
    data_base_path = "data/"
    models_base_path = "models/"

    try:
        users_df = loaders.load_users(data_base_path)
        products_df = loaders.load_products(data_base_path)
        sessions_df = loaders.load_sessions(data_base_path, products_df)

        users_api_df = users_df[["user_id", "name"]]
        categories_api_df = pd.DataFrame(products_df["category"].unique(), columns=["name"])
        products_api_df = products_df

        modelA = loaders.load_model(models_base_path + "A.pkl")
        modelB = loaders.load_model(models_base_path + "B.pkl")
        encoder = loaders.load_encoder()
        scaler = loaders.load_scaler()

        model_assignment = {0: modelA, 1: modelB}
    except IOError as e:
        print("Can't load server. Error occurred: ", e)
        exit(1)

    app.run(port=args.port)

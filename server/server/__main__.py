import pandas as pd
from flask import Flask, jsonify, request

from server import loaders

app = Flask(__name__)
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True

@app.route('/users', methods=['GET'])
def users():
    return jsonify({
        "users": users_df.to_dict(orient="records")
    })


@app.route('/categories', methods=['GET'])
def categories():
    return jsonify({
        'categories': categories_df.to_dict(orient="records")
    })


@app.route('/products', methods=['GET'])
def products():
    return jsonify({
        'products': products_df[products_df["category"] == request.args.get('category')].to_dict(orient="records")
    })


@app.route('/predict', methods=['GET'])
def predict():
    return jsonify({
        'prediction': 0.5
    })


def load_dataset(base_path, file):
    if base_path[-1] != '/':
        base_path += '/'

    return pd.read_json(base_path + file, lines=True)


if __name__ == '__main__':
    data_base_path = "data/"

    users_df = loaders.load_users(data_base_path)
    categories_df = loaders.load_categories(data_base_path)
    products_df = loaders.load_products(data_base_path)
    aggregated_df = loaders.load_aggregated(data_base_path)

    app.run(port=8080)

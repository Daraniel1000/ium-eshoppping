from flask import Flask, jsonify, request
import pandas as pd

app = Flask(__name__)


@app.route('/users', methods=['GET'])
def users():
    query_df = pd.DataFrame(request.json)
    return jsonify({'users': ["bruh1", "bruh2"]})


@app.route('/categories', methods=['GET'])
def categories():
    query_df = pd.DataFrame(request.json)
    return jsonify({'categories': ["bruh1", "bruh2"]})


@app.route('/products', methods=['GET'])
def products():
    query_df = pd.DataFrame(request.json)
    return jsonify({'products': ["bruh1", "bruh2"]})


@app.route('/predict', methods=['GET'])
def predict():
    query_df = pd.DataFrame(request.json)
    return jsonify({'prediction': 0.5})


if __name__ == '__main__':
    app.run(port=8080)

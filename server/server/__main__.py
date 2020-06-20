import argparse

from flask import Flask, jsonify, request, abort, render_template

from server import server

app = Flask(__name__)
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True


@app.route('/', methods=['GET', 'POST'])
def index():
    try:
        return render_template("index.html")
    except Exception as e:
        abort(400, str(e))


@app.route('/users', methods=['GET'])
def users():
    try:
        return jsonify({
            "users": server.get_users_dict()
        })
    except Exception as e:
        abort(400, str(e))


@app.route('/categories', methods=['GET'])
def categories():
    try:
        return jsonify({
            'session': server.generate_session_id(),
            'categories': server.get_categories_dict()
        })
    except Exception as e:
        abort(400, str(e))


@app.route('/products', methods=['GET'])
def products():
    try:
        category = request.args.get("category")
        if not category:
            abort(400, "Parameter \"category\" not passed")
        return jsonify({
            'products': server.get_products_dict(category)
        })
    except Exception as e:
        abort(400, str(e))


@app.route('/predict', methods=['GET'])
def predict():
    try:
        user_id = request.args.get("user")
        product_id = request.args.get("product")
        session_id = request.args.get("session")
        if not user_id:
            abort(400, "Parameter \"user\" not passed")
        if not product_id:
            abort(400, "Parameter \"product\" not passed")
        if not session_id:
            abort(400, "Parameter \"session\" not passed")
        pred = server.get_prediction(user_id, product_id)
        server.register_view(session_id, user_id, product_id, pred)
        return jsonify({
            'predicted_discount': pred
        })
    except Exception as e:
        abort(400, str(e))


@app.route('/buy', methods=['GET'])
def buy():
    try:
        user_id = request.args.get("user")
        product_id = request.args.get("product")
        session_id = request.args.get("session")
        discount = request.args.get("discount")
        if not user_id:
            abort(400, "Parameter \"user\" not passed")
        if not product_id:
            abort(400, "Parameter \"product\" not passed")
        if not session_id:
            abort(400, "Parameter \"session\" not passed")
        if not discount:
            abort(400, "Parameter \"discount\" not passed")
        server.register_buy(session_id, user_id, product_id, discount)
        return jsonify({'success': True})
    except Exception as e:
        abort(400, str(e))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="eShoppping server")
    parser.add_argument('port', nargs='?', type=int, default=8080)
    parser.add_argument('-mode', choices=['basic', 'AB'], default='basic')

    args = parser.parse_args()

    try:
        server = server.Server(args.mode)
    except IOError as e:
        print("Can't load server. Error occurred: ", e)
        exit(1)

    try:
        app.run(port=args.port)
    finally:
        server.dump_state()

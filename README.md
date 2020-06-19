# eShoppping

Test app for making predictions in some e-commerce site

# Modules

- #### learning

    Documentation of model building process

- #### client

    Test GUI client app

- #### server

    Test web server

# How it works

Server stores a model that predicts if given user would buy given product at given discount.

Client logs in as some user and chooses some product. Server polls the model with series of discounts and responds with the lowest one that got a positive response from the model.

# Usage

#### Client

To run the client:

```
./run.sh client [serverPort]
```

Arguments:

- ```serverPort``` (optional) - port at which the server runs (defaults to 8080)

#### Server

To run the server:

```
./run.sh server model [port]
```

Arguments:

- ```model``` - model to use (A or B)

- ```port``` (optional) - port at which to run the server (defaults to 8080)
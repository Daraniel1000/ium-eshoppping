# eShoppping server

Test server for eShoppping

## Requirements
- Python
- Flask
- Pandas
- Numpy

## Usage

To run the server:

```
./run.sh [port]
```

Arguments:

- ```port``` (optional) - port at which to run the server (defaults to 8080)


Server will run on given port, so you can access it locally at:

```
http://localhost:port/...
```

## Endpoints

- ```users```

    Parameters: ```None```

    Get list of users

- ```categories```

    Parameters: ```None```

    Get list of categories and id of session

- ```products```

    Parameters: ```category```

    Get list of products in given category

- ```predict```

    Parameters: ```user, product, session```

    Get predicted discount that should be offered to given user when looking at given product at current time
    
- ```buy```

    Parameters: ```user, product, session, discount```

    Register buy event
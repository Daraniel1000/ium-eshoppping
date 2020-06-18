# eShoppping server

Test server for eShoppping

## Requirements
- Python
- Flask
- Pandas
- Numpy

## Usage

Run from cmd from server directory (the one that contains server package):

```
python3 -m server
```

Server will run on port 8080, so you can access it locally at:

```
http://localhost:8080/...
```

## Endpoints

- ```users```

    Parameters: ```None```

    Get list of users

- ```categories```

    Parameters: ```None```

    Get list of categories

- ```products```

    Parameters: ```category```

    Get list of products in given category

- ```predict```

    Parameters: ```user, product```

    Get predicted discount that should be offered to given user when looking at given product at current time
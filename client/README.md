# eShoppping Client

Test eShoppping client app

## Requirements

- JDK 11 
- Gradle 4.10.3 (wrapper can be used without Gradle installation)

## Usage

Make sure you have `JAVA_HOME` set to correct JDK path

To run the client:

```
./run.sh [serverPort]
```

Arguments:

- ```serverPort``` (optional) - port at which the server runs (defaults to 8080)

If you have Gradle installed, it will be used. Otherwise the wrapper will be used.
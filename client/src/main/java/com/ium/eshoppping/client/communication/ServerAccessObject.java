package com.ium.eshoppping.client.communication;

import com.ium.eshoppping.client.communication.data.*;

import java.io.IOException;

public class ServerAccessObject
{
    private final ServerEndpointAPI service;

    public ServerAccessObject(ServerEndpointAPI service)
    {
        this.service = service;
    }

    public Users getUsers() throws IOException
    {
        return this.service.getUsers().execute().body();
    }

    public Categories getCategories() throws IOException
    {
        return this.service.getCategories().execute().body();
    }

    public Products getProducts(String categoryName) throws IOException
    {
        return this.service.getProducts(categoryName).execute().body();
    }

    public Prediction predict(String userId, String productID, String session) throws IOException
    {
        return this.service.predict(userId, productID, session).execute().body();
    }

    public Buy buy(String userID, String productID, String session, String discount) throws IOException
    {
        return this.service.buy(userID, productID, session, discount).execute().body();
    }
}

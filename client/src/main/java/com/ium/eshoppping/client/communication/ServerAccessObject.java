package com.ium.eshoppping.client.communication;

import com.ium.eshoppping.client.communication.responses.categories.GetCategoriesResponse;
import com.ium.eshoppping.client.communication.responses.products.GetProductsResponse;
import com.ium.eshoppping.client.communication.responses.users.GetUsersResponse;
import com.ium.eshoppping.client.communication.responses.predict.PredictResponse;

import java.io.IOException;

public class ServerAccessObject
{
    private final ServerEndpointAPI service;

    public ServerAccessObject(ServerEndpointAPI service)
    {
        this.service = service;
    }

    public GetUsersResponse getUsers() throws IOException
    {
        return this.service.getUsers().execute().body();
    }

    public GetCategoriesResponse getCategories() throws IOException
    {
        return this.service.getCategories().execute().body();
    }

    public GetProductsResponse getProducts(String categoryName) throws IOException
    {
        return this.service.getProducts(categoryName).execute().body();
    }

    public PredictResponse predict(String userId, String productName) throws IOException
    {
        return this.service.predict(userId, productName).execute().body();
    }
}

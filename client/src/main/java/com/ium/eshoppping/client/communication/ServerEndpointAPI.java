package com.ium.eshoppping.client.communication;

import com.ium.eshoppping.client.communication.data.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerEndpointAPI
{
    @GET("users")
    Call<Users> getUsers();

    @GET("categories")
    Call<Categories> getCategories();

    @GET("products")
    Call<Products> getProducts(@Query("category") String categoryName);

    @GET("predict")
    Call<Prediction> predict(@Query("user") String userId, @Query("product") String productID);

    @GET("buy")
    Call<Object> buy(@Query("user") String userID, @Query("product") String productID, @Query("discount") String discount);
}

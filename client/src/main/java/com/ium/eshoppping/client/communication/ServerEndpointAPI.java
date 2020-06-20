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
    Call<Prediction> predict(@Query("user") String userId, @Query("product") String productID, @Query("session") String session);

    @GET("buy")
    Call<Buy> buy(@Query("user") String userID, @Query("product") String productID, @Query("session") String session, @Query("discount") String discount);
}

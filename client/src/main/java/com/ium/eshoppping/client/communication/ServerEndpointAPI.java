package com.ium.eshoppping.client.communication;
//TODO wrzucić data do responses i wywalić poprzednie
import com.ium.eshoppping.client.data.*;
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
    Call<Prediction> predict(@Query("user") String userId, @Query("product") String productName);
}

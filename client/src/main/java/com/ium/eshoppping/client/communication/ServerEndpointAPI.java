package com.ium.eshoppping.client.communication;

import com.ium.eshoppping.client.communication.responses.categories.GetCategoriesResponse;
import com.ium.eshoppping.client.communication.responses.products.GetProductsResponse;
import com.ium.eshoppping.client.communication.responses.users.GetUsersResponse;
import com.ium.eshoppping.client.communication.responses.predict.PredictResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerEndpointAPI
{
    @GET("users")
    Call<GetUsersResponse> getUsers();

    @GET("categories")
    Call<GetCategoriesResponse> getCategories();

    @GET("products")
    Call<GetProductsResponse> getProducts(@Query("category") String categoryName);

    @GET("predict")
    Call<PredictResponse> predict(@Query("user") String userId, @Query("product") String productName);
}

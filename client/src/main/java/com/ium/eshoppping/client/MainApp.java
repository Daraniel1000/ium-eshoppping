package com.ium.eshoppping.client;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.ServerEndpointAPI;
import com.ium.eshoppping.client.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class MainApp extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        System.setProperty("http.agent", "Chrome");

        OkHttpClient httpClient = buildHttpClient();
        Retrofit r = buildRetrofit(httpClient);
        ServerAccessObject sao = new ServerAccessObject(r.create(ServerEndpointAPI.class));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        LoginController controller = new LoginController(primaryStage, sao);
        loader.setController(controller);
        Parent root = loader.load();

        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setOnHidden(e -> {
            httpClient.connectionPool().evictAll();
            httpClient.dispatcher().executorService().shutdownNow();
        });
        primaryStage.show();
    }

    private OkHttpClient buildHttpClient()
    {
        long timeout = 1;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            HttpUrl url = original.url().newBuilder().build();

            Request request = original.newBuilder()
                                      .url(url)
                                      .build();

            return chain.proceed(request);
        });

        return httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
                         .writeTimeout(timeout, TimeUnit.SECONDS)
                         .readTimeout(timeout, TimeUnit.SECONDS)
                         .build();
    }

    private Retrofit buildRetrofit(OkHttpClient httpClient)
    {
        String BASE_URL = "http://localhost:8080"; // TODO

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }
}
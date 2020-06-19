package com.ium.eshoppping.client;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.ServerEndpointAPI;
import com.ium.eshoppping.client.login.LoginController;
import com.ium.eshoppping.client.utils.FXHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class MainApp extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        System.setProperty("http.agent", "Chrome");

        String port = "8080";
        if(!getParameters().getUnnamed().isEmpty())
            port = getParameters().getUnnamed().get(0);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> MainApp.handleError(t, e)));
        Thread.currentThread().setUncaughtExceptionHandler(MainApp::handleError);

        try
        {
            OkHttpClient httpClient = buildHttpClient();
            Retrofit r = buildRetrofit(httpClient, port);
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
        catch(Throwable t)
        {
            MainApp.handleError(Thread.currentThread(), t);
        }
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

    private Retrofit buildRetrofit(OkHttpClient httpClient, String port)
    {
        String BASE_URL = "http://localhost:" + port;

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    private static void handleError(Thread t, Throwable e) {
        if(e instanceof LoadException)
            e = e.getCause().getCause();
        if(e instanceof ReflectiveOperationException)
            e = e.getCause();
        if (Platform.isFxApplicationThread()) {
            FXHelper.showErrorDialog("Eshoppping - krytyczny błąd.", e.getMessage());
        } else {
            System.err.println("Eshoppping - krytyczny błąd. " + e.getMessage());
        }
        Platform.exit();
        System.exit(1);
    }
}
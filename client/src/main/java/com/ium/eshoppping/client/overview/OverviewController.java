package com.ium.eshoppping.client.overview;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.details.DetailsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class OverviewController
{
    private final Stage stage;
    private final Parent previousParent;
    private final ServerAccessObject sao;
    private final String user;
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> categoriesList; // TODO ListView<Category>
    @FXML
    private ListView<String> productsList; // TODO ListView<Category>

    public OverviewController(Stage stage, Parent previousParent, ServerAccessObject sao, String user)
    {
        this.stage = stage;
        this.previousParent = previousParent;
        this.sao = sao;
        this.user = user;
    }

    @FXML
    public void initialize()
    {
        // do something on start (after fxml loaded)
        categoriesList.getItems().add("category1");
        categoriesList.getItems().add("category2");
        categoriesList.getItems().add("category3");
        categoriesList.getItems().add("category4");
        categoriesList.getItems().add("category5");

        categoriesList.getSelectionModel()
                      .selectedItemProperty()
                      .addListener(((observable, oldValue, newValue) -> showProducts(newValue)));
        productsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                showDetails(newValue);
        });
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }

    private void showProducts(String category)
    {
        productsList.getItems().clear();
        productsList.getItems().add("product1");
        productsList.getItems().add("product2");
        productsList.getItems().add("product3");
        productsList.getItems().add("product4");
        productsList.getItems().add("product5");
        productsList.setVisible(true);
    }

    private void showDetails(String product)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/details.fxml"));
        DetailsController controller = new DetailsController(stage, stage.getScene().getRoot(), sao, this.user,
                                                             product);
        loader.setController(controller);
        Parent root;
        try
        {
            root = loader.load(); //load details scene
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }

        stage.getScene().setRoot(root);
    }
}

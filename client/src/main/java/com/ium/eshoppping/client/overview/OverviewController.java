package com.ium.eshoppping.client.overview;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.data.Categories;
import com.ium.eshoppping.client.communication.data.Category;
import com.ium.eshoppping.client.communication.data.Product;
import com.ium.eshoppping.client.communication.data.Products;
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
    private final int userID;
    @FXML
    private Button backButton;
    @FXML
    private ListView<Category> categoriesList;
    @FXML
    private ListView<Product> productsList;

    public OverviewController(Stage stage, Parent previousParent, ServerAccessObject sao, int userID)
    {
        this.stage = stage;
        this.previousParent = previousParent;
        this.sao = sao;
        this.userID = userID;
    }

    @FXML
    public void initialize()
    {
        // do something on start (after fxml loaded)

        Categories categories = null;
        try {
            categories = sao.getCategories();
            for (Category i: categories.categories) {
                categoriesList.getItems().add(i);
            }

            categoriesList.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(((observable, oldValue, newValue) -> {
                        try {
                            showProducts(newValue.name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
            productsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null)
                    showDetails(newValue);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }

    private void showProducts(String category) throws IOException {
        productsList.getItems().clear();
        Products products = sao.getProducts(category);
        for(Product i: products.products) {
            productsList.getItems().add(i);
        }
        productsList.setVisible(true);
    }

    private void showDetails(Product product)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/details.fxml"));
        DetailsController controller = new DetailsController(stage, stage.getScene().getRoot(), sao, this.userID,
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

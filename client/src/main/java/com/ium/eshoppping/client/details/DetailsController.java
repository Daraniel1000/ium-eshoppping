package com.ium.eshoppping.client.details;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.data.Prediction;
import com.ium.eshoppping.client.data.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailsController
{
    private final Stage stage;
    private final Parent previousParent;
    private final ServerAccessObject sao;
    private final Integer user;
    private final Product product;
    @FXML
    private Button backButton;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label finalPriceLabel;

    public DetailsController(Stage stage, Parent previousParent, ServerAccessObject sao, int user, Product product)
    {
        this.stage = stage;
        this.previousParent = previousParent;
        this.sao = sao;
        this.user = user;
        this.product = product;
    }

    @FXML
    public void initialize() throws IOException {
        productNameLabel.setText(this.product.productName);
        Prediction prediction = sao.predict(user.toString(), product.productName);
        productPriceLabel.setText(((Double)product.price).toString());
        finalPriceLabel.setText(((Double)(prediction.prediction * product.price)).toString());
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }
}

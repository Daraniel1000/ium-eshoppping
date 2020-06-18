package com.ium.eshoppping.client.details;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.data.Prediction;
import com.ium.eshoppping.client.communication.data.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        productPriceLabel.setText("Oryginalna cena: " + product.price);
        BigDecimal newPrice = new BigDecimal(Double.toString(prediction.prediction * product.price));
        finalPriceLabel.setText("Cena po zniżce: " + newPrice.setScale(2, RoundingMode.HALF_UP));
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }
}

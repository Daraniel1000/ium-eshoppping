package com.ium.eshoppping.client.details;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.data.Buy;
import com.ium.eshoppping.client.communication.data.Prediction;
import com.ium.eshoppping.client.communication.data.Product;
import com.ium.eshoppping.client.utils.FXHelper;
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
    private Prediction prediction;
    private final Integer session;
    @FXML
    private Button backButton;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label finalPriceLabel;
    @FXML
    private Label productDiscountLabel;
    @FXML
    private Button buyButton;

    public DetailsController(Stage stage, Parent previousParent, ServerAccessObject sao, int user, Product product, int session)
    {
        this.stage = stage;
        this.previousParent = previousParent;
        this.sao = sao;
        this.user = user;
        this.product = product;
        this.session = session;
    }

    @FXML
    public void initialize() throws IOException {
        productNameLabel.setText(this.product.productName);
        prediction = sao.predict(user.toString(), product.productId.toString(), session.toString());
        BigDecimal price = new BigDecimal(Double.toString(product.price));
        productPriceLabel.setText("Oryginalna cena: " + price.setScale(2));
        if(prediction.predictedDiscount == 0) {
            productDiscountLabel.setText("");
            finalPriceLabel.setText("");
        }
        else {
            productDiscountLabel.setText("Zniżka: " + prediction.predictedDiscount + "%");
            price = new BigDecimal(Double.toString((100 - prediction.predictedDiscount) * product.price / 100));
            finalPriceLabel.setText("Cena po zniżce: " + price.setScale(2, RoundingMode.HALF_UP));
        }
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }


    @FXML
    void onBuyClicked(ActionEvent event)
    {
        try {
            Buy buy = sao.buy(user.toString(), product.productId.toString(), session.toString(), prediction.predictedDiscount.toString());
            if(buy.success)
                stage.getScene().setRoot(previousParent);
            else
                FXHelper.showErrorDialog("Eshoppping - błąd zakupu", "Nie udało się kupić produktu.");
        } catch (IOException e) {
            FXHelper.showErrorDialog("Eshoppping - błąd połączenia", "Nie udało się kupić produktu.");
            e.printStackTrace();
        }
    }
}

package com.ium.eshoppping.client.details;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailsController
{
    private final Stage stage;
    private final Parent previousParent;
    private final ServerAccessObject sao;
    private final String user;
    private final String product;
    @FXML
    private Button backButton;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;

    public DetailsController(Stage stage, Parent previousParent, ServerAccessObject sao, String user, String product)
    {
        this.stage = stage;
        this.previousParent = previousParent;
        this.sao = sao;
        this.user = user;
        this.product = product;
    }

    @FXML
    public void initialize()
    {
        productNameLabel.setText(this.product);
        productPriceLabel.setText("Miljon złotych");
    }

    @FXML
    void onBackClicked(ActionEvent event)
    {
        stage.getScene().setRoot(previousParent);
    }
}

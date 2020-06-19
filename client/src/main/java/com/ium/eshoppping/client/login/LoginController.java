package com.ium.eshoppping.client.login;

import com.ium.eshoppping.client.communication.ServerAccessObject;
import com.ium.eshoppping.client.communication.data.User;
import com.ium.eshoppping.client.communication.data.Users;
import com.ium.eshoppping.client.overview.OverviewController;
import com.ium.eshoppping.client.utils.FXHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController
{
    private final Stage stage;
    private final ServerAccessObject sao;
    @FXML
    private ChoiceBox<User> choiceBox;
    @FXML
    private Button loginButton;

    public LoginController(Stage stage, ServerAccessObject sao)
    {
        this.stage = stage;
        this.sao = sao;
    }

    @FXML
    public void initialize() throws IOException
    {
        // do something on start (after fxml loaded)
        try {
            Users users = sao.getUsers();
            for (User i: users.users) {
                choiceBox.getItems().add(i);
            }
        } catch (IOException e) {
            throw new IOException("Nie udało się pobrać listy użytkowników", e);
        }

        choiceBox.setValue(choiceBox.getItems().get(0));
    }

    @FXML
    void onLoginClicked(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/overview.fxml"));
        OverviewController controller = new OverviewController(stage, stage.getScene().getRoot(), sao,
                                                               choiceBox.getValue().userId);
        loader.setController(controller);
        Parent root;
        try
        {
            root = loader.load(); //load details scene
        }
        catch(IOException e)
        {
            FXHelper.showErrorDialog("Eshoppping - błąd połączenia", "Nie udało się pobrać listy kategorii");
            return;
        }

        stage.getScene().setRoot(root);
    }
}

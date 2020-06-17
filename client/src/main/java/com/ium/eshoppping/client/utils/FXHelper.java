package com.ium.eshoppping.client.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FXHelper
{
    public static boolean showYesNoDialog(String title, String message)
    {
        return showAlert(Alert.AlertType.CONFIRMATION, title, message).getResult() == ButtonType.YES;
    }

    public static void showInfoDialog(String title, String message)
    {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showErrorDialog(String title, String message)
    {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    private static Alert showAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert;

        if(type == Alert.AlertType.CONFIRMATION)
            alert = new Alert(type, "", ButtonType.YES, ButtonType.NO);
        else
            alert = new Alert(type, "", ButtonType.OK);

        alert.setTitle("");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();

        return alert;
    }

    public static void showInfoNotification(String title, String message, int duration)
    {
        createNotification(title, message, duration).showInformation();
    }

    public static void showErrorNotification(String title, String message, int duration)
    {
        createNotification(title, message, duration).showError();
    }

    private static Notifications createNotification(String title, String message, int duration)
    {
        Duration showDuration = new Duration(duration);

        return Notifications.create()
                            .title(title)
                            .text(message)
                            .hideAfter(showDuration);
    }

    /**
     * Runs given Callable on UI Thread and waits for it to finish
     * Note: it has to be called on background thread. If you call it on UI thread there will be a deadlock
     */
    public static <T> T runOnUIAndWait(Callable<T> r) throws ExecutionException, InterruptedException
    {
        final FutureTask<T> task = new FutureTask<>(r);
        Platform.runLater(task);
        return task.get();
    }
}

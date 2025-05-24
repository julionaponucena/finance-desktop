package com.github.julionaponucena.financedesktop.commons.executor;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ExceptionHandler {

    private final String errorMessage;

    public void execute(Throwable exception) {
        if(exception instanceof ApplicationException) {
            showAlert(exception.getMessage());
        }else {
            showAlert(Objects.requireNonNullElse(errorMessage, "Não foi possível realizar a ação"));
            exception.printStackTrace();
        }
    }

    private static void showAlert(String message) {
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR,message);
            alert.show();
        });
    }
}

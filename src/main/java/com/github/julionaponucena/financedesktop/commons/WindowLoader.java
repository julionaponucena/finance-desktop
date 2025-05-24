package com.github.julionaponucena.financedesktop.commons;

import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WindowLoader {

    private final JobExecutor executor;

    public void openWindow(String path, Window window,Factory factory) {
        this.executor.execute(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/" + path));

            loader.setControllerFactory(factory);

            Scene scene = new Scene(loader.load());

            Platform.runLater(() -> {
                Stage stage = new Stage();

                stage.setScene(scene);
                stage.initOwner(window);
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

                stage.show();
            });
        },new ExceptionHandler("Houve erro ao carregar a tela"));
    }
}

package com.github.julionaponucena.financedesktop;


import com.github.julionaponucena.financedesktop.modules.main.controllers.factories.MainControllerFactory;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * JavaFX App
 */
public class FinanceDesktop extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        Optional<String> optionalString = getParameters().getUnnamed().stream().findFirst();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/main.fxml"));


        MainControllerFactory controllerFactory = optionalString.map(MainControllerFactory::new).orElseGet(MainControllerFactory::new);

        loader.setControllerFactory(controllerFactory);

        Scene scene = new Scene(loader.load());

        stage.setScene(scene);

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));


        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
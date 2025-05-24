package com.github.julionaponucena.financedesktop.commons;

import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories.BlockRegisterControllerFactory;
import com.github.julionaponucena.financedesktop.modules.category.controllers.factories.CategoryControllerFactory;
import com.github.julionaponucena.financedesktop.modules.main.controllers.factories.SidebarControllerFactory;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.factories.RegisterControllerFactory;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;


public class RouterFX {
    private VBox centerContainer;
    private BorderPane rootContainer;

    public void goToBlockRegisters(){
        URL resource = this.buildResource("blocks-registers.fxml");

        FXMLLoader mainLoader = new FXMLLoader(resource);
        mainLoader.setControllerFactory(new BlockRegisterControllerFactory(this));

        FXMLLoader sidebarLoader = new FXMLLoader(this.buildResource("sidebar.fxml"));

        sidebarLoader.setControllerFactory(new SidebarControllerFactory(this));

        try {
            Parent sidebar =sidebarLoader.load();
            Platform.runLater(() -> {this.rootContainer.setLeft(sidebar);
                this.changeScreen(mainLoader);
            });
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void goToRegisters(int blockId){
        URL resource =this.buildResource("registers.fxml");

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(new RegisterControllerFactory(blockId));


        this.changeScreen(loader);
    }

    public void goToCategories(){
        URL resource = this.buildResource("categories.fxml");

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(new CategoryControllerFactory());

        this.changeScreen(loader);
    }

    private void changeScreen(FXMLLoader loader) {
        Thread.startVirtualThread(() -> {
            try {
                Parent parent = loader.load();

                VBox.setVgrow(parent, Priority.ALWAYS);

                Platform.runLater(() -> {
                    this.centerContainer.getChildren().clear();

                    this.centerContainer.getChildren().add(parent);
                });
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

    }

    private URL buildResource(String path) {
        return getClass().getResource("/fxmls/" +path);
    }

    public void setContainers(BorderPane rootContainer, VBox centerContainer) {
        if(this.rootContainer != null || this.centerContainer != null){
            throw new IllegalStateException("Container already set");
        }

        this.rootContainer = rootContainer;
        this.centerContainer = centerContainer;
    }
}

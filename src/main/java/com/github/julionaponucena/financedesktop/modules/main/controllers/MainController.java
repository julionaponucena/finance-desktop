package com.github.julionaponucena.financedesktop.modules.main.controllers;

import com.github.julionaponucena.financedesktop.commons.FileManager;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainController {


    @FXML
    private VBox centerContainer;

    @FXML
    private BorderPane rootContainer;

    private final RouterFX routerFX;
    private final FileManager fileManager;

    @FXML
    private void initialize() {
        this.routerFX.setContainers(rootContainer, centerContainer);
    }

    public void open() {
        this.fileManager.open(this.rootContainer.getScene().getWindow());
    }

    public void create(){
        this.fileManager.create(this.rootContainer.getScene().getWindow());
    }
}

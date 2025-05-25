package com.github.julionaponucena.financedesktop.modules.main.controllers;

import com.github.julionaponucena.financedesktop.commons.FileManager;
import com.github.julionaponucena.financedesktop.commons.FileManagerService;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
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
    private String path;
    private FileManagerService fileManagerService;
    private JobExecutor executor;

    public MainController(RouterFX routerFX, FileManager fileManager, String path, FileManagerService fileManagerService,JobExecutor executor) {
        this.routerFX = routerFX;
        this.fileManager = fileManager;
        this.path = path;
        this.fileManagerService = fileManagerService;
        this.executor=executor;
    }

    @FXML
    private void initialize() {
        this.routerFX.setContainers(rootContainer, centerContainer);

        if(this.path != null) {
            this.executor.execute(() -> {
                this.fileManagerService.openConnection(this.path);
                this.routerFX.goToBlockRegisters();
            });
        }
    }

    public void open() {
        this.fileManager.open(this.rootContainer.getScene().getWindow());
    }

    public void create(){
        this.fileManager.create(this.rootContainer.getScene().getWindow());
    }
}

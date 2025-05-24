package com.github.julionaponucena.financedesktop.commons;

import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class FileManager {
    private final FileManagerService service;
    private final RouterFX routerFX;
    private JobExecutor executor;

    public void open(Window window) {
        FileChooser fileChooser = new FileChooser();

        File file =fileChooser.showOpenDialog(window);

        if(file == null){
            return;
        }

        this.executor.execute(() -> {
            this.service.openConnection(file.getAbsolutePath());
            this.routerFX.goToBlockRegisters();
        });
    }

    public void create(Window window) {
        FileChooser fileChooser = new FileChooser();

        File file =fileChooser.showSaveDialog(window);

        if(file == null){
            return;
        }

        this.executor.execute(() -> {
            this.service.createConnection(file.getAbsolutePath());
            this.routerFX.goToBlockRegisters();
        });
    }
}

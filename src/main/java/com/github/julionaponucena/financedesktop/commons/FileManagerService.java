package com.github.julionaponucena.financedesktop.commons;

import com.github.julionaponucena.financedesktop.modules.main.ConnectionManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileManagerService {
    private final ConnectionManager connectionManager;

    public void createConnection(String path){
        this.connectionManager.createConnection(path + ".finance");
    }

    public void openConnection(String path){
        ConnectionManager.getInstance().createConnection(path);
    }
}

package com.github.julionaponucena.financedesktop.modules.main.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.FileManager;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.index.IndexController;
import com.github.julionaponucena.financedesktop.commons.FileManagerService;
import com.github.julionaponucena.financedesktop.modules.main.ConnectionManager;
import com.github.julionaponucena.financedesktop.modules.main.controllers.MainController;


public class MainControllerFactory implements Factory {
    
    private String path;
    private final RouterFX routerFX = new RouterFX();

    public MainControllerFactory(String path) {
        this.path = path;
    }

    public MainControllerFactory() {}
    
    @Override
    public Object call(Class<?> clazz) {
        FileManagerService fileManagerService = new FileManagerService(ConnectionManager.getInstance());
        FileManager fileManager = new FileManager(fileManagerService,this.routerFX,new VirtualThreadJobExecutor());

        if(clazz == MainController.class) {
            MainController controller =new MainController(this.routerFX,fileManager);
            if (path != null) {
                ConnectionManager.getInstance().createConnection(path);

                // implementar essa lógica

            }
            return controller;
        }
        else{
            return new IndexController(fileManager);
        }
    }
}

package com.github.julionaponucena.financedesktop.modules.main.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.modules.main.controllers.SidebarController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SidebarControllerFactory implements Factory {
    private final RouterFX routerFX;

    @Override
    public Object call(Class<?> aClass) {
        return new SidebarController(routerFX);
    }
}

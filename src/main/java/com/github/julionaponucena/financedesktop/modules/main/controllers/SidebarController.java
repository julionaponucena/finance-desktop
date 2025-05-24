package com.github.julionaponucena.financedesktop.modules.main.controllers;

import com.github.julionaponucena.financedesktop.commons.RouterFX;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SidebarController{
    private final RouterFX routerFX;

    public void goToCategories(){
        this.routerFX.goToCategories();
    }

    public void goToBlockRegisters(){
        this.routerFX.goToBlockRegisters();
    }
}

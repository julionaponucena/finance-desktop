package com.github.julionaponucena.financedesktop.modules.category.data.out;

import javafx.beans.property.SimpleStringProperty;

public record ListCategoryOUTFX(int id, SimpleStringProperty name, int total) {
    public void setName(String name){
        this.name.set(name);
    }
}

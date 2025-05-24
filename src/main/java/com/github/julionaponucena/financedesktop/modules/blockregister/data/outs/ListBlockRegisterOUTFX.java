package com.github.julionaponucena.financedesktop.modules.blockregister.data.outs;

import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public record ListBlockRegisterOUTFX(int id, SimpleStringProperty title,BigDecimal value,boolean containRegister) {
    public void setTitle(String title) {
        this.title.set(title);
    }
}

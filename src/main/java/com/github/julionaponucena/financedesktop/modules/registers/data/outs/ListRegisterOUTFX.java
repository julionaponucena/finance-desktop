package com.github.julionaponucena.financedesktop.modules.registers.data.outs;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ListRegisterOUTFX(int id, SimpleStringProperty title, SimpleObjectProperty<LocalDate> date,
                                ObservableList<ListCategoryOUTFX> categories,
                                SimpleObjectProperty<BigDecimal> value) {

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void changeCategories(ObservableList<ListCategoryOUTFX> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
    }

    public void setValue(BigDecimal value) {
        this.value.set(value);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}

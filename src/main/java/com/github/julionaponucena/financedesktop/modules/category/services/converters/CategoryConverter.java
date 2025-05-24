package com.github.julionaponucena.financedesktop.modules.category.services.converters;

import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import javafx.beans.property.SimpleStringProperty;

public class CategoryConverter {
    public ListCategoryOUTFX convert(ListCategoryOUT categoryOUT) {
        return new ListCategoryOUTFX(categoryOUT.id(),new SimpleStringProperty(categoryOUT.name()),categoryOUT.total());
    }

    public ListCategoryOUTFX convert(CreateCategoryOUT categoryOUT){
        return new ListCategoryOUTFX(categoryOUT.id(),new SimpleStringProperty(categoryOUT.name()),0);
    }
}

package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.ExportRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.*;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.math.BigDecimal;
import java.util.List;

public class RegisterConverter {

    private final MoneyFormatter moneyFormatter = new MoneyFormatter();

    public ListCategoryOUTFX convertToListCategoryOUTFX(CategoryPersistenceVO persistenceVO) {
        return new ListCategoryOUTFX(persistenceVO.id(), persistenceVO.name());
    }

    public ListRegisterOUTFX convertToListRegisterOUTFX(ListRegisterOUT registerOUT){
        List<ListCategoryOUTFX> categoryOUTFX =registerOUT.categories().stream().map(RegisterConverter::convertToListCategoryOUTFX).toList();

        return new ListRegisterOUTFX(registerOUT.id(),new SimpleStringProperty(registerOUT.title()),new SimpleObjectProperty<>(registerOUT.date()),
                FXCollections.observableArrayList(categoryOUTFX),
                new SimpleObjectProperty<>(registerOUT.value()));
    }

    public ListRegisterOUTFX convertToListRegisterOUTFX(CreateRegisterOUT registerOUT){
        List<ListCategoryOUTFX> categoryOUTFXES = registerOUT.categories()
                .stream()
                .map(RegisterConverter::convertToListCategoryOUTFX).toList();

        return new ListRegisterOUTFX(registerOUT.id(),new SimpleStringProperty(registerOUT.title()),
                new SimpleObjectProperty<>(registerOUT.date()),FXCollections.observableArrayList(categoryOUTFXES),
                new SimpleObjectProperty<>(registerOUT.value()));
    }

    public ExportRegisterInput convertToRegisterInput(int idBlock, ListRegisterOUTFX list){
        final String value = "R$ " + moneyFormatter.format(list.value().getValue());

        return  new ExportRegisterInput(idBlock,list.title().get(),list.date().getValue(),value);
    }

    private static ListCategoryOUTFX  convertToListCategoryOUTFX(ListCategoryRelOUT categoryOUT){
        return new ListCategoryOUTFX(categoryOUT.id(), categoryOUT.name());
    }
}

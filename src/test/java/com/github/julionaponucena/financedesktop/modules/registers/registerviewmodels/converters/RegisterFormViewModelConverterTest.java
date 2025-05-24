package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.converters;

import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterFormViewModelConverterTest {

    @Test
    void convert() {
        RegisterFormViewModelConverter converter = new RegisterFormViewModelConverter();

        ListCategoryOUTFX categoryOUTFX = new ListCategoryOUTFX(1,"teste");

        CategoryPersistenceInputFX input = converter.convert(categoryOUTFX);

        Assertions.assertEquals(categoryOUTFX.id(), input.getId());
        Assertions.assertEquals(categoryOUTFX.name(), input.getName());
        Assertions.assertEquals(CategoryPersistenceState.CREATED, input.getPersistenceState());
        Assertions.assertEquals(CategoryPersistenceState.CREATED, input.getViewState());
    }
}
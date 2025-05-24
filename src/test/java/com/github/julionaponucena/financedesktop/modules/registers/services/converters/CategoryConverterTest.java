package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryConverterTest {
    private final CategoryConverter converter = new CategoryConverter();

    @Test
    void convertToListCategoryOutFX() {
        ListCategoryRelOUT categoryOUT = getCategoryOUT();

        ListCategoryOUTFX listCategoryOUTFX = this.converter.convertToListCategoryOutFX(categoryOUT);

        Assertions.assertEquals(categoryOUT.id(),listCategoryOUTFX.id());
        Assertions.assertEquals(categoryOUT.name(),listCategoryOUTFX.name());
    }

    @Test
    void convertToInput() {
        CategoryPersistenceInputFX inputFX = new CategoryPersistenceInputFX(1,"teste",CategoryPersistenceState.CREATED,CategoryPersistenceState.CREATED);

        CategoryPersistenceInput input = converter.convertToInput(inputFX);

        Assertions.assertEquals(inputFX.getId(),input.id());
        Assertions.assertEquals(inputFX.getName(),input.name());
        Assertions.assertEquals(inputFX.getPersistenceState(),input.persistenceState());
        Assertions.assertEquals(inputFX.getViewState(),input.currentState());
    }

    private static ListCategoryRelOUT getCategoryOUT() {
        return new ListCategoryRelOUT(1, "teste");
    }
}
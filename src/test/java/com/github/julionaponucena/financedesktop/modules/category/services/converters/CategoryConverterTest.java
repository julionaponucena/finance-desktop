package com.github.julionaponucena.financedesktop.modules.category.services.converters;

import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryConverterTest {
    private final CategoryConverter converter = new CategoryConverter();

    @Test
    void convertToInputFXToInputFx() {
        ListCategoryOUT categoryOUT = new ListCategoryOUT(1,"teste",10);

        ListCategoryOUTFX categoryOUTFX = this.converter.convert(categoryOUT);

        Assertions.assertEquals(categoryOUT.id(),categoryOUTFX.id());
        Assertions.assertEquals(categoryOUT.name(),categoryOUTFX.name().getValue());
        Assertions.assertEquals(categoryOUT.total(),categoryOUTFX.total());
    }

    @Test
    void convertToInputFXToListCategoryOutFX() {
        CreateCategoryOUT categoryOUT = new CreateCategoryOUT(1,"teste");

        ListCategoryOUTFX categoryOUTFX = this.converter.convert(categoryOUT);

        Assertions.assertEquals(categoryOUT.id(),categoryOUTFX.id());
        Assertions.assertEquals(categoryOUT.name(),categoryOUTFX.name().getValue());
        Assertions.assertEquals(0,categoryOUTFX.total());
    }
}
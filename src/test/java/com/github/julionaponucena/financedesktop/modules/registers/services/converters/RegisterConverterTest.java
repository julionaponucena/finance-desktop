package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.modules.registers.data.outs.*;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

class RegisterConverterTest {

    private final RegisterConverter registerConverter = new RegisterConverter();

    @Test
    void convertToListCategoryOUTFXFromCategoryPersistenceVO() {
        CategoryPersistenceVO persistenceVO = new CategoryPersistenceVO(1,"teste");

        ListCategoryOUTFX listCategoryOUTFX = registerConverter.convertToListCategoryOUTFX(persistenceVO);

        Assertions.assertEquals(persistenceVO.id(),listCategoryOUTFX.id());
        Assertions.assertEquals(persistenceVO.name(),listCategoryOUTFX.name());
    }

    @Test
    void convertToListCategoryOUTFXFromListRegisterOUT() {
        ListRegisterOUT registerOUT = new ListRegisterOUT(1,"teste", LocalDate.now(),createListCategoryOUTFX(), BigDecimal.ZERO);

        ListRegisterOUTFX result =this.registerConverter.convertToListRegisterOUTFX(registerOUT);

        Assertions.assertEquals(registerOUT.id(),result.id());
        Assertions.assertEquals(registerOUT.title(),result.title().getValue());
        Assertions.assertEquals(registerOUT.date(),result.date().getValue());
        Assertions.assertEquals(registerOUT.value(),result.value().getValue());

        validateListCategoryOUTFX(registerOUT.categories(),result.categories());
    }

    @Test
    void convertToListCategoryOUTFXFromCreateRegisterOUT() {
        CreateRegisterOUT registerOUT = new CreateRegisterOUT(1,"teste", LocalDate.now(),createListCategoryOUTFX(), BigDecimal.ZERO);

        ListRegisterOUTFX result =this.registerConverter.convertToListRegisterOUTFX(registerOUT);

        Assertions.assertEquals(registerOUT.id(),result.id());
        Assertions.assertEquals(registerOUT.title(),result.title().getValue());
        Assertions.assertEquals(registerOUT.date(),result.date().getValue());
        Assertions.assertEquals(registerOUT.value(),result.value().getValue());
        validateListCategoryOUTFX(registerOUT.categories(),result.categories());
    }

    private static List<ListCategoryRelOUT> createListCategoryOUTFX() {
        return List.of(
                new ListCategoryRelOUT(1,"teste"),
                new ListCategoryRelOUT(2,"teste 2")
        );
    }

    private static void validateListCategoryOUTFX(List<ListCategoryRelOUT> listCategoryOUT, List<ListCategoryOUTFX> listCategoryOUTFX) {
        Assertions.assertEquals(listCategoryOUT.size(),listCategoryOUTFX.size());

        for (int i = 0; i < listCategoryOUT.size(); i++) {
            ListCategoryRelOUT categoryOut =listCategoryOUT.get(i);
            ListCategoryOUTFX listCategoryOUTFX1 = listCategoryOUTFX.get(i);

            Assertions.assertEquals(categoryOut.id(),listCategoryOUTFX1.id());
            Assertions.assertEquals(categoryOut.name(),listCategoryOUTFX1.name());

        }
    }
}
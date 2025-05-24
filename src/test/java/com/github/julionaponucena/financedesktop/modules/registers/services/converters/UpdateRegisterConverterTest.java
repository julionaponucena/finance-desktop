package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.UpdateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceWrapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateRegisterConverterTest {

    private UpdateRegisterConverter converter = new UpdateRegisterConverter();

    @BeforeEach
    public void config(){
        this.converter = new UpdateRegisterConverter();
    }

    @Test
    void mapCategoryPersistence() {
        List<CategoryPersistenceInput> categories = List.of(
                new CategoryPersistenceInput(1,"teste", CategoryPersistenceState.PERSISTED,CategoryPersistenceState.PERSISTED),
                new CategoryPersistenceInput(2,"teste",CategoryPersistenceState.CREATED,CategoryPersistenceState.CREATED),
                new CategoryPersistenceInput(3,"teste",CategoryPersistenceState.DELETED,CategoryPersistenceState.DELETED)
        );

        CategoryPersistenceWrapperDTO wrapperDTO =this.converter.mapCategoryPersistence(categories);

        assertEquals(1, wrapperDTO.insertIds().size());
        assertEquals(1, wrapperDTO.deleteIds().size());
        assertTrue(wrapperDTO.insertIds().contains(2));
        assertTrue(wrapperDTO.deleteIds().contains(3));
    }

    @Test
    void convertToOUT(){
        String categoryName="teste categoria criada";

        List<CategoryPersistenceInput> categories = List.of(
                new CategoryPersistenceInput(1,categoryName,CategoryPersistenceState.CREATED,CategoryPersistenceState.CREATED),
                new CategoryPersistenceInput(2,"teste",CategoryPersistenceState.DELETED,CategoryPersistenceState.DELETED)
        );

        Register register = new Register(1,"teste registro", LocalDate.now(),new ArrayList<>(),new BigDecimal(1));

        UpdateRegisterOUT registerOUT =this.converter.convertToOUT(register,categories);

        CategoryPersistenceVO categoryPersistenceVO = registerOUT.categories().getFirst();

        assertEquals(1,registerOUT.categories().size());
        assertEquals(1,categoryPersistenceVO.id());
        assertEquals(categoryName,categoryPersistenceVO.name());
        assertEquals(register.getId(),registerOUT.id());
        assertEquals(register.getTitle(),registerOUT.title());
        assertEquals(register.getDate(),registerOUT.date());
        assertEquals(register.getValue(),registerOUT.value());
    }

    @Test
    void convertInputToEntity(){
        UpdateRegisterInput registerInput = new UpdateRegisterInput(1,"teste",new ArrayList<>(),LocalDate.now(),new BigDecimal(1));

        Register register = this.converter.convertInputToEntity(registerInput);

        assertEquals(registerInput.id(),register.getId());
        assertEquals(registerInput.title(),register.getTitle());
        assertEquals(registerInput.date(),register.getDate());
        assertEquals(registerInput.value(),register.getValue());
    }
}
package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.UpdateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceWrapperDTO;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.UpdateRegisterConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UpdateRegisterServiceTest {

    private UpdateRegisterService service;
    private RegisterRepository repository;
    private UpdateRegisterConverter converter;

    @BeforeEach
    public void config(){
        this.converter= Mockito.mock(UpdateRegisterConverter.class);
        this.repository = Mockito.mock(RegisterRepository.class);
        this.service = new UpdateRegisterService(repository, converter);
    }

    @Test
    @DisplayName("Deve atualizar o Registro")
    void shouldUpdateRegister()throws InternalServerException {
        Mockito.when(this.repository.notExists(1)).thenReturn(false);

        Set<Integer> insertIds = new HashSet<>();
        Set<Integer> deleteIds = new HashSet<>();

        List<CategoryPersistenceInput> categoryPersistenceInputs = new ArrayList<>();

        UpdateRegisterInput input = new UpdateRegisterInput(1,"",categoryPersistenceInputs,LocalDate.now(),new BigDecimal(1));

        Mockito.when(this.converter.mapCategoryPersistence(categoryPersistenceInputs))
                .thenReturn(new CategoryPersistenceWrapperDTO(insertIds, deleteIds));

        Register register = new Register();

        Mockito.when(this.converter.convertInputToEntity(input))
                .thenReturn(register);

        Mockito.when(this.converter.convertToOUT(register,categoryPersistenceInputs))
                .thenReturn(new UpdateRegisterOUT(1,"", LocalDate.now(),new ArrayList<>(),new BigDecimal(1)));

        Assertions.assertDoesNotThrow(() -> this.service.execute(input));

        Mockito.verify(this.repository, Mockito.times(1)).notExists(1);
        Mockito.verify(this.repository,Mockito.times(1)).removeCategories(deleteIds, input.id());
        Mockito.verify(this.repository,Mockito.times(1)).addCategories(insertIds, input.id());
        Mockito.verify(this.repository,Mockito.times(1)).update(register);
        Mockito.verify(this.converter,Mockito.times(1)).mapCategoryPersistence(categoryPersistenceInputs);
    }

    @Test
    @DisplayName("Deve lançar excessão quando o registro não existir")
    void shouldNotUpdateRegisterWhenRegisterDoesNotExist()throws InternalServerException {
        Mockito.when(this.repository.notExists(1)).thenReturn(true);

        UpdateRegisterInput input = new UpdateRegisterInput(1,"",new ArrayList<>(),LocalDate.now(),new BigDecimal(1));

        Assertions.assertThrows(ApplicationException.class, () -> this.service.execute(input));
    }
}
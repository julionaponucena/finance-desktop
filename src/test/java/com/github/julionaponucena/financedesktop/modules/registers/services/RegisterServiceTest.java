package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RegisterServiceTest {
    private RegisterRepository repository;
    private RegisterService service;

    @BeforeEach
    public void init(){
        this.repository = Mockito.mock(RegisterRepository.class);
        this.service = new RegisterService(repository);
    }

    @Test
    @DisplayName("Deve deletar o Registro")
    void shouldDeleteRegister() throws InternalServerException {
        Mockito.when(repository.notExists(1)).thenReturn(false);

        Assertions.assertDoesNotThrow(() ->service.delete(1));

        Mockito.verify(repository, Mockito.times(1)).removeCategories(1);
        Mockito.verify(repository, Mockito.times(1)).delete(1);
    }

    @Test
    @DisplayName("Deve lançar uma excessão quando o registro não existir")
    void shouldThrowExceptionWhenRegisterDoesNotExists() throws InternalServerException {
        Mockito.when(repository.notExists(1)).thenReturn(true);

        Assertions.assertThrows(ApplicationException.class, () ->service.delete(1));
    }
}
package com.github.julionaponucena.financedesktop.modules.blockregister.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ListBlockRegisterServiceTest {

    private BlockRegisterRepository repository;
    private ListBlockRegisterService service;


    @BeforeEach
    public void config(){
        this.repository =Mockito.mock(BlockRegisterRepository.class);
        this.service = new ListBlockRegisterService(repository);
    }


    @Test
    @DisplayName("Deve apagar o bloco de registro")
    void shouldDeleteBlockRegister() throws InternalServerException {
        Mockito.when(this.repository.existsRegister(1)).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> this.service.delete(1));

        Mockito.verify(this.repository,Mockito.times(1)).delete(1);
    }

    @Test
    @DisplayName("Não deve apagar o bloco de registro caso contenha um registro")
    void shouldNotDeleteBlockRegisterWhenItContainsARegister() throws InternalServerException {
        Mockito.when(this.repository.notExists(1)).thenReturn(false);
        Mockito.when(this.repository.existsRegister(1)).thenReturn(true);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.delete(1));
    }

    @Test
    @DisplayName("Não deve apagar o bloco de registro caso ele não exista")
    void shouldNotDeleteBlockRegisrWhenItNotExist() throws InternalServerException {
        Mockito.when(this.repository.notExists(1)).thenReturn(true);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.delete(1));
    }
}
package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateCategoryServiceTest {

    private CreateCategoryService service;
    private CategoryRepository repository;

    @BeforeEach
    void config(){
        this.repository = Mockito.mock(CategoryRepository.class);
        this.service = new CreateCategoryService(repository);
    }

    @Test
    @DisplayName("Deve criar uma categoria")
    void shouldCreateCategory() throws InternalServerException {
        String name = "test";

        Mockito.when(this.repository.nameExists(name)).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> this.service.execute(name));

        Mockito.verify(this.repository, Mockito.times(1)).create(name);
    }

    @Test
    @DisplayName("Não deve criar uma categoria caso ela já exista")
    void shouldNotCreateCategoryWhenItAlredyExists() throws InternalServerException {
        String name = "test";

        Mockito.when(this.repository.nameExists(name)).thenReturn(true);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.execute(name));

        Mockito.verify(this.repository, Mockito.never()).create(name);
    }
}
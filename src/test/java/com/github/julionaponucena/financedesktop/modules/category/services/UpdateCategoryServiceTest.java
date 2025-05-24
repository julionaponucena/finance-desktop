package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.data.input.UpdateCategoryInput;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UpdateCategoryServiceTest {

    private CategoryRepository repository;
    private UpdateCategoryService service;

    @BeforeEach
    void config(){
        this.repository = Mockito.mock(CategoryRepository.class);
        this.service = new UpdateCategoryService(repository);
    }

    @Test
    @DisplayName("Deve atualizar a categoria")
    void shouldUpdateCategory() throws InternalServerException {
        int id = 1;
        String name = "test";

        Mockito.when(this.repository.nameExists(id,name)).thenReturn(false);

        UpdateCategoryInput input = new UpdateCategoryInput(id, name);

        Assertions.assertDoesNotThrow(() -> this.service.execute(input));

        Mockito.verify(this.repository, Mockito.times(1)).nameExists(id,name);
        Mockito.verify(this.repository, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    @DisplayName("Não deve atualizar a categoria caso o nome exista")
    void shouldNotUpdateCategoryWhenNameExists() throws InternalServerException {
        int id = 1;
        String name = "test";

        Mockito.when(this.repository.nameExists(id,name)).thenReturn(true);

        UpdateCategoryInput input = new UpdateCategoryInput(id, name);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.execute(input));

        Mockito.verify(this.repository, Mockito.times(1)).nameExists(id,name);
        Mockito.verify(this.repository, Mockito.never()).update(Mockito.any());
    }

    @Test
    @DisplayName("Não deve atualizar a categoria caso ela não seja encontrada")
    void shouldNotUpdateCategoryWhenNotFound() throws InternalServerException {
        int id = 1;
        String name = "test";

        Mockito.when(this.repository.nameExists(id,name)).thenReturn(false);
        Mockito.when(this.repository.notExists(id)).thenReturn(true);

        UpdateCategoryInput input = new UpdateCategoryInput(id, name);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.execute(input));

        Mockito.verify(this.repository, Mockito.times(1)).nameExists(id,name);
        Mockito.verify(this.repository, Mockito.times(1)).notExists(id);
        Mockito.verify(this.repository, Mockito.never()).update(Mockito.any());
    }
}
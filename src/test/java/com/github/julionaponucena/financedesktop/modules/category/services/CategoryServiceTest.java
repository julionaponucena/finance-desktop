package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CategoryServiceTest {

    private CategoryService service;
    private CategoryRepository repository;

    @BeforeEach
    void config() {
        this.repository = Mockito.mock(CategoryRepository.class);
        this.service = new CategoryService(repository);
    }

    @Test
    @DisplayName("Deve apagar a categoria")
    void shouldDeleteCategory() throws InternalServerException {
        Mockito.when(this.repository.containsRelation(1)).thenReturn(false);
        Mockito.when(this.repository.notExists(1)).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> this.service.delete(1));

        Mockito.verify(this.repository, Mockito.times(1)).delete(1);
    }

    @Test
    @DisplayName("Não deve apagar a categoria quando conter um registro vinculado")
    void shouldNotDeleteCategory() throws InternalServerException {
        Mockito.when(this.repository.containsRelation(1)).thenReturn(true);
        Mockito.when(this.repository.notExists(1)).thenReturn(false);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.delete(1));
    }

    @Test
    @DisplayName("Não deve apagar a categoria quando ela não existir")
    void shouldNotDeleteCategoryWhenNotFound() throws InternalServerException {
        Mockito.when(this.repository.containsRelation(1)).thenReturn(false);
        Mockito.when(this.repository.notExists(1)).thenReturn(true);

        Assertions.assertThrows(ApplicationException.class, () -> this.service.delete(1));
    }
}
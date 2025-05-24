package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Category;

import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListCategoryService {
    private final CategoryRepository repository;

    public List<ListCategoryRelOUT> findAll() throws InternalServerException {
        List<Category> categories = repository.findAll();

        return categories.stream().map(category -> new ListCategoryRelOUT(category.getId(),category.getName())).toList();
    }
}

package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Category;
import com.github.julionaponucena.financedesktop.modules.category.data.input.UpdateCategoryInput;
import com.github.julionaponucena.financedesktop.modules.category.data.out.UpdateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateCategoryService {
    private final CategoryRepository repository;

    public UpdateCategoryOUT execute(UpdateCategoryInput input)throws InternalServerException, ApplicationException {
        boolean exists = this.repository.nameExists(input.id(),input.name());

        if (exists) {
            throw new ApplicationException("Categoria já existe");
        }

        boolean notExists = this.repository.notExists(input.id());

        if (notExists) {
            throw new ApplicationException("Categoria não encontrada");
        }

        Category category = new Category(input.id(), input.name());

        this.repository.update(category);

        return new UpdateCategoryOUT(input.id(), input.name());
    }
}

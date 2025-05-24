package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateCategoryService {
    private final CategoryRepository repository;

    public CreateCategoryOUT execute(String name) throws InternalServerException, ApplicationException {

        boolean exists = this.repository.nameExists(name);

        if (exists) {
            throw new ApplicationException("Categoria já existe");
        }

        int id =this.repository.create(name);

        return new CreateCategoryOUT(id, name);
    }
}

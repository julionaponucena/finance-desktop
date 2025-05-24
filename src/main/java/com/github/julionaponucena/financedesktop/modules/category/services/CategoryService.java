package com.github.julionaponucena.financedesktop.modules.category.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public List<ListCategoryOUT> findAll() throws InternalServerException {
        return this.repository.findAllTotalProj();
    }

    public void delete(int id) throws InternalServerException,ApplicationException {
        boolean containsRelation = this.repository.containsRelation(id);

        if (containsRelation) {
            throw new ApplicationException("Não foi possível apagar a categoria: Registros estão utiliando essa categoria");
        }

        boolean notExists = this.repository.notExists(id);

        if (notExists) {
            throw new ApplicationException("Categoria não encontrada");
        }

        this.repository.delete(id);
    }
}

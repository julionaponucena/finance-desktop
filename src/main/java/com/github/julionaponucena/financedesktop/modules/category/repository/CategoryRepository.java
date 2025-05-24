package com.github.julionaponucena.financedesktop.modules.category.repository;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Category;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;

import java.util.List;
import java.util.Set;

public interface CategoryRepository {
    List<Category> findAll() throws InternalServerException;

    List<Category> findAll(Set<Integer> ids) throws InternalServerException;

    List<ListCategoryOUT> findAllTotalProj() throws InternalServerException;

    boolean nameExists(String name) throws InternalServerException;

    boolean nameExists(int id,String name) throws InternalServerException;

    boolean notExists(int id) throws InternalServerException;

    boolean containsRelation(int id) throws InternalServerException;

    int create(String name) throws InternalServerException;

    void update(Category category) throws InternalServerException;

    void delete(int id) throws InternalServerException;
}

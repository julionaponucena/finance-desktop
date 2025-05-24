package com.github.julionaponucena.financedesktop.modules.registers.repositories;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Register;

import java.util.List;
import java.util.Set;

public interface RegisterRepository {
    List<Register> findAll();
    List<Register> findAllByBlockId(int blockId) throws InternalServerException;

    Register create(Register register) throws InternalServerException;
    
    void addCategories(Set<Integer> categoriesId,int registerId) throws InternalServerException;
    
    void removeCategories(Set<Integer> categoriesId,int registerId) throws InternalServerException;

    void removeCategories(int registerId) throws InternalServerException;

    void delete(int id) throws InternalServerException;

    boolean notExists(int id) throws InternalServerException;

    void update(Register register) throws InternalServerException;
}

package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RegisterService {
    private final RegisterRepository repository;

    public List<Register> findAll() {
        return this.repository.findAll();
    }

    public List<ListRegisterOUT> findAllByBlockId(int categoryId)throws InternalServerException {
        return this.repository.findAllByBlockId(categoryId)
                .stream().map(register -> {
                    List<ListCategoryRelOUT> categories = register.getCategories().stream().map(category ->
                            new ListCategoryRelOUT(category.getId(),category.getName())).toList();


                    return new ListRegisterOUT(register.getId(),
                        register.getTitle(),
                        register.getDate(),
                        categories,
                        register.getValue());
                }).toList();
    }

    public void delete(int id)throws InternalServerException, ApplicationException {
        boolean notExists = this.repository.notExists(id);

        if(notExists) {
            throw new ApplicationException("Registro não encontrado");
        }

        this.repository.removeCategories(id);

        this.repository.delete(id);
    }
}

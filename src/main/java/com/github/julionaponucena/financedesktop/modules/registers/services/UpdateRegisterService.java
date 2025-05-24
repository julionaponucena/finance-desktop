package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.UpdateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceWrapperDTO;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.UpdateRegisterConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateRegisterService {
    private final RegisterRepository repository;
    private final UpdateRegisterConverter converter;


    public UpdateRegisterOUT execute(UpdateRegisterInput input) throws InternalServerException, ApplicationException {
        boolean notExists = this.repository.notExists(input.id());

        if (notExists) {
            throw new ApplicationException("Não foi possível atualizar o registro");
        }

        CategoryPersistenceWrapperDTO wrapperDTO =this.converter.mapCategoryPersistence(input.categoryPersistenceInputs());

        this.repository.removeCategories(wrapperDTO.deleteIds(),input.id());
        this.repository.addCategories(wrapperDTO.insertIds(),input.id());

        Register register = this.converter.convertInputToEntity(input);

        this.repository.update(register);

        return this.converter.convertToOUT(register,input.categoryPersistenceInputs());
    }
}

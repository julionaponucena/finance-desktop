package com.github.julionaponucena.financedesktop.modules.blockregister.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.BlockRegister;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.inputs.UpdateBlockRegisterInput;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.UpdateBlockRegisterOUT;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateBlockRegisterService {

    private final BlockRegisterRepository repository;

    public UpdateBlockRegisterOUT execute(UpdateBlockRegisterInput input)throws InternalServerException,ApplicationException {
        boolean notExists = this.repository.notExists(input.id());

        if (notExists) {
            throw new ApplicationException("O bloco não foi encontrado");
        }

        BlockRegister blockRegister = new BlockRegister(input.id(),input.title());

        this.repository.update(blockRegister);

        return new UpdateBlockRegisterOUT(input.id(),input.title());
    }
}

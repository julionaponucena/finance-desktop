package com.github.julionaponucena.financedesktop.modules.blockregister.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListBlockRegisterService {
    private final BlockRegisterRepository blockRegisterRepository;

    public List<BlockRegisterProj> list() throws InternalServerException {
        return this.blockRegisterRepository.list();
    }

    public void delete(int id) throws InternalServerException,ApplicationException {
        boolean notExist = this.blockRegisterRepository.notExists(id);

        if (notExist) {
            throw new ApplicationException("Bloco de Registro não encontrado");
        }

        boolean existsRegister = this.blockRegisterRepository.existsRegister(id);

        if (existsRegister) {
            throw new ApplicationException("Não é possível apagar um bloco que contenha um registro");
        }

        this.blockRegisterRepository.delete(id);
    }
}

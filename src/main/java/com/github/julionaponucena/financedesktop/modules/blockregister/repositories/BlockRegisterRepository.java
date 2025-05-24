package com.github.julionaponucena.financedesktop.modules.blockregister.repositories;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.BlockRegister;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;

import java.util.List;

public interface BlockRegisterRepository {
    List<BlockRegisterProj> list() throws InternalServerException;

    BlockRegister create(String title) throws InternalServerException;

    boolean notExists(int id) throws InternalServerException;

    void update(BlockRegister blockRegister) throws InternalServerException;

    void delete(int id) throws InternalServerException;

    boolean existsRegister(int id) throws InternalServerException;
}

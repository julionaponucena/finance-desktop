package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;

public interface BlockRegisterPersistenceStrategy {
    void persist(String title) throws InternalServerException;
}

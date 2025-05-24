package com.github.julionaponucena.financedesktop.modules.category.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;

public interface CategoryPersistenceStrategy {
    void persist(String name) throws ApplicationException, InternalServerException;
}

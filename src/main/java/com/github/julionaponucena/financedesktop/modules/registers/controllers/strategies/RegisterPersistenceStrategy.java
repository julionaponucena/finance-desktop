package com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RegisterPersistenceStrategy {
    void persist(String title, LocalDate date, List<CategoryPersistenceInput> categories, BigDecimal value) throws ApplicationException, InternalServerException;
}

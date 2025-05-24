package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateRegisterInput(int id, String title, List<CategoryPersistenceInput> categoryPersistenceInputs,
                                  LocalDate date, BigDecimal value) {
}

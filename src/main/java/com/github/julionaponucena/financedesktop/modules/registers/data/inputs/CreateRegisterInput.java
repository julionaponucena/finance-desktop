package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record CreateRegisterInput(String name, LocalDate date, Set<Integer> categoriesId, BigDecimal value,int idBlock) {
}

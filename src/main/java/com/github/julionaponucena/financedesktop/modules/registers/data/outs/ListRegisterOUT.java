package com.github.julionaponucena.financedesktop.modules.registers.data.outs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ListRegisterOUT(int id, String title, LocalDate date, List<ListCategoryRelOUT> categories, BigDecimal value) {
}

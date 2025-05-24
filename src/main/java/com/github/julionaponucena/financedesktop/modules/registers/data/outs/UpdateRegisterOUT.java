package com.github.julionaponucena.financedesktop.modules.registers.data.outs;

import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateRegisterOUT(int id, String title, LocalDate date, List<CategoryPersistenceVO> categories,
                                BigDecimal value) {
}

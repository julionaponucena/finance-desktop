package com.github.julionaponucena.financedesktop.modules.registers.data.formdata;

import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.enums.RegisterTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RegisterFormData(String title, LocalDate date, List<CategoryPersistenceInputFX> categories, BigDecimal value) {

    public RegisterTypeEnum type(){
        return value.signum() == 1 ? RegisterTypeEnum.CREDIT : RegisterTypeEnum.DEBIT;
    }
}

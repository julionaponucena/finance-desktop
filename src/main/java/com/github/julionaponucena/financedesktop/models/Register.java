package com.github.julionaponucena.financedesktop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    private int id;
    private String title;
    private LocalDate date;
    private List<Category> categories;
    private BigDecimal value;
    private BlockRegister blockRegister;

    public Register(int id, String title, LocalDate date, List<Category> categories, BigDecimal value) {
        this.id=id;
        this.title=title;
        this.date=date;
        this.categories=categories;
        this.value=value;
    }

    public Register(int id, String title, LocalDate date, BigDecimal value) {
        this.id=id;
        this.title=title;
        this.date=date;
        this.value=value;
    }

    public Register(String title, LocalDate date, List<Category> categories, BigDecimal value,BlockRegister blockRegister) {
        this.title=title;
        this.date=date;
        this.categories=categories;
        this.value=value;
        this.blockRegister=blockRegister;
    }
}

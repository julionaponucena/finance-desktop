package com.github.julionaponucena.financedesktop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BlockRegister {
    private int id;

    private String title;

    public BlockRegister(int id) {
        this.id = id;
    }
}

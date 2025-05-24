package com.github.julionaponucena.financedesktop.commons;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyFormatterTest {

    @Test
    void format() {
        MoneyFormatter formatter = new MoneyFormatter();

        String value =formatter.format(new BigDecimal("10.5"));

        assertEquals("10,50", value);

        value = formatter.format(new BigDecimal("10"));

        assertEquals("10,00", value);

        value = formatter.format(new BigDecimal("10.55"));

        assertEquals("10,55", value);
    }
}
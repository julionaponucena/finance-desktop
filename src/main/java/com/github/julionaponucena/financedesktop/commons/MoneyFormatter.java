package com.github.julionaponucena.financedesktop.commons;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MoneyFormatter {
    public String format(BigDecimal value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,##0.00",symbols);

        df.setGroupingUsed(true);

        return df.format(value);
    }
}

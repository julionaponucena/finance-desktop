package com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.function.Function;

@AllArgsConstructor
public class ReactiveTotalValue<T> implements Callable<String> {

    private final Function<T,BigDecimal> function;
    private final SimpleObjectProperty<ObservableList<T>> data;
    private final MoneyFormatter formatter;

    @Override
    public String call(){
        BigDecimal totalValue = this.data.getValue().stream().map(function).reduce(BigDecimal.ZERO, BigDecimal::add);

        return "Total: R$ " + formatter.format(totalValue);
    }
}

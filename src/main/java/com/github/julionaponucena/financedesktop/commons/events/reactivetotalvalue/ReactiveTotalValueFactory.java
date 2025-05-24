package com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.function.Function;

public class ReactiveTotalValueFactory {
    public static  <T> ReactiveTotalValue<T> call(Function<T, BigDecimal> function, SimpleObjectProperty<ObservableList<T>>data) {
        return new ReactiveTotalValue<>(function, data,new MoneyFormatter());
    }
}

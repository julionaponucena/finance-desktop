package com.github.julionaponucena.financedesktop.commons.events;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue.ReactiveTotalValue;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

class ReactiveTotalValueTest {

    private ReactiveTotalValue<ListRegisterOUTFX> reactiveTotalValue;
    private MoneyFormatter formatter;
    private SimpleObjectProperty<ObservableList<ListRegisterOUTFX>> data;

    @BeforeEach
    void config(){
        formatter = Mockito.mock(MoneyFormatter.class);
        Function<ListRegisterOUTFX, BigDecimal> function = register -> register.value().getValue();
        data = new SimpleObjectProperty<>();
        reactiveTotalValue = new ReactiveTotalValue<>(function,data,formatter);
    }

    @Test
    void call() {
        this.data.setValue(FXCollections.observableArrayList(
                createListRegisterOUTFX(new BigDecimal("10.25")),
                createListRegisterOUTFX(new BigDecimal("5.50")),
                createListRegisterOUTFX(new BigDecimal("5.50")),
                createListRegisterOUTFX(new BigDecimal("-3.30"))
                )
        );

        Mockito.when(this.formatter.format(Mockito.any())).thenReturn("17,95");

        String value =this.reactiveTotalValue.call();

        ArgumentCaptor<BigDecimal> captor = ArgumentCaptor.forClass(BigDecimal.class);

        Mockito.verify(formatter, Mockito.times(1)).format(captor.capture());

        Assertions.assertEquals(new BigDecimal("17.95"), captor.getValue());
        Assertions.assertEquals("Total: R$ 17,95",value);
    }

    private static ListRegisterOUTFX createListRegisterOUTFX(BigDecimal value) {
        return new ListRegisterOUTFX(1,new SimpleStringProperty("teste"), new SimpleObjectProperty<>(LocalDate.now()),FXCollections.observableArrayList(),
                new SimpleObjectProperty<>(value));
    }
}
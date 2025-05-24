package com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.implementations;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.observers.UpdateRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.UpdateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.services.UpdateRegisterService;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class UpdateRegisterPersistenceStrategy implements RegisterPersistenceStrategy {

    private final UpdateRegisterService service;
    private final ListRegisterOUTFX registerOUTFX;
    private final UpdateRegisterObserver[] observers;

    public UpdateRegisterPersistenceStrategy(UpdateRegisterService service, ListRegisterOUTFX registerOUTFX,
                                             UpdateRegisterObserver... observers) {
        this.service = service;
        this.registerOUTFX = registerOUTFX;
        this.observers = observers;
    }


    @Override
    public void persist(String title, LocalDate date, List<CategoryPersistenceInput> categories, BigDecimal value) throws ApplicationException,InternalServerException {
        UpdateRegisterOUT registerOUT =this.service.execute(new UpdateRegisterInput(registerOUTFX.id(), title, categories, date, value));

        for (UpdateRegisterObserver observer : this.observers) {
            Platform.runLater(() -> observer.onUpdateRegister(registerOUT));
        }
    }
}

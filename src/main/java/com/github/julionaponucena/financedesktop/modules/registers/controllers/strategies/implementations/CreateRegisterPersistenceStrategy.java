package com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.implementations;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.observers.CreateRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CreateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.CreateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.services.CreateRegisterService;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateRegisterPersistenceStrategy implements RegisterPersistenceStrategy {

    private final CreateRegisterService service;
    private final int idBlockRegister;
    private final CreateRegisterObserver[] observers;

    public CreateRegisterPersistenceStrategy(CreateRegisterService service, int idBlockRegister ,CreateRegisterObserver... observers) {
        this.service = service;
        this.idBlockRegister = idBlockRegister;
        this.observers = observers;
    }

    @Override
    public void persist(String title, LocalDate date, List<CategoryPersistenceInput> categories, BigDecimal value) throws InternalServerException {

        Set<Integer>categoriesId = categories.stream().map(CategoryPersistenceInput::id).collect(Collectors.toSet());

        CreateRegisterOUT createRegisterOUT =this.service
                .execute(new CreateRegisterInput(title, date,categoriesId, value,idBlockRegister));

        for(CreateRegisterObserver observer : this.observers) {
            Platform.runLater(() -> observer.notifyCreation(createRegisterOUT));
        }
    }
}

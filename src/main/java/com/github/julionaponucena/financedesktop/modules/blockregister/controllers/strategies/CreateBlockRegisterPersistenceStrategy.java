package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.CreateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.CreateBlockRegisterService;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateBlockRegisterPersistenceStrategy implements BlockRegisterPersistenceStrategy {

    private final CreateBlockRegisterService service;
    private final CreateBlockRegisterObserver[] observers;

    @Override
    public void persist(String title) throws InternalServerException {
        CreateBlockRegisterOUT createBlockRegisterOUT =this.service.execute(title);

        Platform.runLater(() -> {
            for (CreateBlockRegisterObserver observer : this.observers) {
                observer.notifyCreation(createBlockRegisterOUT);
            }
        });
    }
}

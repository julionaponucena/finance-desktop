package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.UpdateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.inputs.UpdateBlockRegisterInput;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.UpdateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.UpdateBlockRegisterService;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UpdateBlockRegisterPersistenceStrategy implements BlockRegisterPersistenceStrategy{

    private final UpdateBlockRegisterService service;
    private final int registerId;
    private final UpdateBlockRegisterObserver[] observers;

    public UpdateBlockRegisterPersistenceStrategy(UpdateBlockRegisterService service, int registerId,
                                                  UpdateBlockRegisterObserver... observers) {
        this.service = service;
        this.registerId=registerId;
        this.observers = observers;
    }

    @Override
    public void persist(String title) throws InternalServerException {
        try {
            UpdateBlockRegisterOUT blockRegisterOUT = this.service.execute(new UpdateBlockRegisterInput(this.registerId, title));

            Platform.runLater(() -> {
                for (UpdateBlockRegisterObserver observer : observers) {
                    observer.notifyUpdate(blockRegisterOUT);
                }
            });
        }catch (ApplicationException exception){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,exception.getMessage());

                alert.show();
            });
        }
    }
}

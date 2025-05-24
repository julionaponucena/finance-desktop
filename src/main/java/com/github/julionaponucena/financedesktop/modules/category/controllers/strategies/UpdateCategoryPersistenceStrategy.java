package com.github.julionaponucena.financedesktop.modules.category.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.controllers.observers.UpdateCategoryObserver;
import com.github.julionaponucena.financedesktop.modules.category.data.input.UpdateCategoryInput;
import com.github.julionaponucena.financedesktop.modules.category.data.out.UpdateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.services.UpdateCategoryService;
import javafx.application.Platform;

public class UpdateCategoryPersistenceStrategy implements CategoryPersistenceStrategy {
    private final int id;
    private final UpdateCategoryService service;
    private final UpdateCategoryObserver[] observers;

    public UpdateCategoryPersistenceStrategy(int id, UpdateCategoryService service ,UpdateCategoryObserver... observers) {
        this.id = id;
        this.service=service;
        this.observers = observers;
    }

    @Override
    public void persist(String categoryName)throws ApplicationException,InternalServerException {
        UpdateCategoryOUT categoryOUT = this.service.execute(new UpdateCategoryInput(id,categoryName));

        for (UpdateCategoryObserver observer : observers) {
            Platform.runLater(() -> observer.onUpdate(categoryOUT));
        }

    }
}

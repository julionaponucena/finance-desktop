package com.github.julionaponucena.financedesktop.modules.category.controllers.strategies;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.category.controllers.observers.CreateCategoryObserver;
import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.services.CreateCategoryService;
import javafx.application.Platform;


public class CreateCategoryPersistenceStrategy implements CategoryPersistenceStrategy{

    private final CreateCategoryService service;
    private final CreateCategoryObserver[] observers;

    public CreateCategoryPersistenceStrategy(CreateCategoryService service, CreateCategoryObserver... observers) {
        this.service = service;
        this.observers = observers;
    }

    @Override
    public void persist(String name) throws ApplicationException,InternalServerException {

        CreateCategoryOUT categoryOUT = this.service.execute(name);

        for (CreateCategoryObserver observer : observers) {
            Platform.runLater(() -> observer.onCreate(categoryOUT));
        }
    }
}

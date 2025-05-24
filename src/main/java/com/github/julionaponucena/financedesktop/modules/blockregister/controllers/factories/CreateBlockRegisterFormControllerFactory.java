package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.BlockRegisterFormController;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.CreateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies.BlockRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies.CreateBlockRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.implementations.JDBCBlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.CreateBlockRegisterService;


public class CreateBlockRegisterFormControllerFactory implements Factory {

    private final CreateBlockRegisterObserver[] observers;

    public CreateBlockRegisterFormControllerFactory(CreateBlockRegisterObserver... observers) {
        this.observers = observers;
    }

    @Override
    public Object call(Class<?> aClass) {
        BlockRegisterRepository repository = new JDBCBlockRegisterRepository();

        CreateBlockRegisterService service = new CreateBlockRegisterService(repository);

        BlockRegisterPersistenceStrategy strategy = new CreateBlockRegisterPersistenceStrategy(service,observers);

        return new BlockRegisterFormController(strategy,new VirtualThreadJobExecutor());
    }
}

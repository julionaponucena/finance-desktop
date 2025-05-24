package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.BlockRegisterFormController;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.UpdateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies.BlockRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies.UpdateBlockRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.implementations.JDBCBlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.UpdateBlockRegisterService;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateBlockRegisterFormControllerFactory implements Factory {

    private final ListBlockRegisterOUTFX listBlockRegisterOUTFX;
    private final UpdateBlockRegisterObserver observer;

    @Override
    public Object call(Class<?> aClass) {
        BlockRegisterRepository repository = new JDBCBlockRegisterRepository();

        UpdateBlockRegisterService service = new UpdateBlockRegisterService(repository);

        BlockRegisterPersistenceStrategy strategy = new UpdateBlockRegisterPersistenceStrategy(service,
                listBlockRegisterOUTFX.id()
                ,observer);

         return new BlockRegisterFormController(strategy,new VirtualThreadJobExecutor(),listBlockRegisterOUTFX.title().getValue());
    }
}

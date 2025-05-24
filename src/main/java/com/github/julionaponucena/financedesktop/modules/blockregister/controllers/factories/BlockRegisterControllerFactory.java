package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.ListBlockRegisterService;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.converters.BlockRegisterConverter;
import com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels.BlockRegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.BlockRegisterController;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.implementations.JDBCBlockRegisterRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockRegisterControllerFactory implements Factory {
    private final RouterFX routerFX;

    @Override
    public Object call(Class<?> aClass) {

        BlockRegisterRepository repository = new JDBCBlockRegisterRepository();

        ListBlockRegisterService service = new ListBlockRegisterService(repository);

        JobExecutor executor = new VirtualThreadJobExecutor();

        BlockRegisterViewModel viewModel = new BlockRegisterViewModel(service,executor,new BlockRegisterConverter());

        return new BlockRegisterController(viewModel,this.routerFX,new WindowLoader(executor));
    }
}

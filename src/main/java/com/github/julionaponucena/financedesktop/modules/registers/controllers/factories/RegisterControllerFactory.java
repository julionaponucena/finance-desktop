package com.github.julionaponucena.financedesktop.modules.registers.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.registers.services.RegisterService;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.RegisterController;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations.JDBCRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.RegisterConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterControllerFactory implements Factory {
    private final int blockId;


    @Override
    public Object call(Class<?> aClass) {
        RegisterRepository repository = new JDBCRegisterRepository();

        RegisterService registerService = new RegisterService(repository);

        JobExecutor jobExecutor = new VirtualThreadJobExecutor();

        RegisterViewModel registerViewModel = new RegisterViewModel(registerService,jobExecutor,new RegisterConverter());

        return new RegisterController(registerViewModel, blockId,new WindowLoader(jobExecutor));
    }
}

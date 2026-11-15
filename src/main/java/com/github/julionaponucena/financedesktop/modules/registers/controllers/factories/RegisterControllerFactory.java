package com.github.julionaponucena.financedesktop.modules.registers.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.implementations.JDBCBlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations.RegisterFileOutputGenerator;
import com.github.julionaponucena.financedesktop.modules.registers.services.ExportRegisterToPDFService;
import com.github.julionaponucena.financedesktop.modules.registers.services.RegisterService;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.RegisterController;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations.JDBCRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.RegisterConverter;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterControllerFactory implements Factory {
    private final ListBlockRegisterOUTFX blockRegister;
    private final Window window;


    @Override
    public Object call(Class<?> aClass) {
        RegisterRepository repository = new JDBCRegisterRepository();

        RegisterService registerService = new RegisterService(repository);

        JobExecutor jobExecutor = new VirtualThreadJobExecutor();

        final var exportService = new ExportRegisterToPDFService(new RegisterFileOutputGenerator(window),
                new JDBCBlockRegisterRepository());

        RegisterViewModel registerViewModel = new RegisterViewModel(registerService,jobExecutor,
                new RegisterConverter(),exportService);

        return new RegisterController(registerViewModel, this.blockRegister ,new WindowLoader(jobExecutor));
    }
}

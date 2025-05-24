package com.github.julionaponucena.financedesktop.modules.registers.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.commons.listmanager.PersistenceListManagerFX;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.repository.implementations.JDBCCategoryRepository;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.RegisterControllerForm;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.implementations.CreateRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterFormViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.converters.RegisterFormViewModelConverter;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations.JDBCRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.CreateRegisterService;
import com.github.julionaponucena.financedesktop.modules.registers.services.ListCategoryService;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.CategoryConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateRegisterControllerFactory implements Factory {

    private final int blockId;
    private final RegisterViewModel registerViewModel;

    @Override
    public Object call(Class<?> aClass) {
        RegisterRepository registerRepository = new JDBCRegisterRepository();

        CategoryRepository categoryRepository = new JDBCCategoryRepository();

        CreateRegisterService registerService = new CreateRegisterService(registerRepository,categoryRepository);

        RegisterPersistenceStrategy strategy = new CreateRegisterPersistenceStrategy(registerService,blockId,registerViewModel);

        ListCategoryService listCategoryService = new ListCategoryService(categoryRepository);

        PersistenceListManagerFX<CategoryPersistenceInputFX, ListCategoryOUTFX> listManagerFX = new PersistenceListManagerFX<>(
          new RegisterFormViewModelConverter()
        );

        RegisterFormViewModel registerFormViewModel = new RegisterFormViewModel(strategy,listCategoryService,
                new VirtualThreadJobExecutor(),new CategoryConverter(),listManagerFX);

        return new RegisterControllerForm(registerFormViewModel);
    }
}

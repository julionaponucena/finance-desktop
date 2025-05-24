package com.github.julionaponucena.financedesktop.modules.registers.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.commons.listmanager.PersistenceListManagerFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.converters.RegisterFormViewModelConverter;
import com.github.julionaponucena.financedesktop.modules.registers.services.ListCategoryService;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterFormViewModel;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.repository.implementations.JDBCCategoryRepository;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.RegisterControllerForm;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.implementations.UpdateRegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.formdata.RegisterFormData;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations.JDBCRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.CategoryConverter;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.UpdateRegisterConverter;
import com.github.julionaponucena.financedesktop.modules.registers.services.UpdateRegisterService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UpdateRegisterControllerFactory implements Factory {

    private final RegisterViewModel registerViewModel;
    private final ListRegisterOUTFX listRegisterOutFX;

    @Override
    public Object call(Class<?> aClass) {
        RegisterFormViewModel registerFormViewModel = getRegisterFormViewModel();

        List<CategoryPersistenceInputFX> categories = this.listRegisterOutFX.categories()
                .stream().
                map(listCategoryOUTFX ->
                        new CategoryPersistenceInputFX(listCategoryOUTFX.id(),
                                listCategoryOUTFX.name(),
                                CategoryPersistenceState.PERSISTED,
                                CategoryPersistenceState.CREATED ))
                .toList();

        RegisterFormData registerFormData = new RegisterFormData(listRegisterOutFX.title().getValue(),
                listRegisterOutFX.date().getValue(),categories,listRegisterOutFX.value().getValue());

        return new RegisterControllerForm(registerFormViewModel,registerFormData);
    }

    private RegisterFormViewModel getRegisterFormViewModel() {
        RegisterRepository repository = new JDBCRegisterRepository();

        CategoryRepository categoryRepository = new JDBCCategoryRepository();

        ListCategoryService listCategoryService = new ListCategoryService(categoryRepository);

        UpdateRegisterService updateRegisterService = new UpdateRegisterService(repository,new UpdateRegisterConverter());

        RegisterPersistenceStrategy persistenceStrategy = new UpdateRegisterPersistenceStrategy(
                updateRegisterService,
                listRegisterOutFX,
                registerViewModel
        );

        PersistenceListManagerFX<CategoryPersistenceInputFX, ListCategoryOUTFX> listManagerFX = new PersistenceListManagerFX<>(
          new RegisterFormViewModelConverter()
        );

        return new RegisterFormViewModel(persistenceStrategy,listCategoryService,new VirtualThreadJobExecutor(),new CategoryConverter(),listManagerFX);
    }
}

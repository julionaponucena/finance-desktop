package com.github.julionaponucena.financedesktop.modules.category.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.category.controllers.CategoryFormController;
import com.github.julionaponucena.financedesktop.modules.category.controllers.strategies.CategoryPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.category.controllers.strategies.CreateCategoryPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.repository.implementations.JDBCCategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.services.CreateCategoryService;
import com.github.julionaponucena.financedesktop.modules.category.viewmodels.CategoryViewModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateCategoryControllerFactory implements Factory {

    private final CategoryViewModel viewModel;

    @Override
    public Object call(Class<?> aClass) {
        CategoryRepository categoryRepository = new JDBCCategoryRepository();

        CreateCategoryService service = new CreateCategoryService(categoryRepository);

        CategoryPersistenceStrategy persistenceStrategy = new CreateCategoryPersistenceStrategy(service,this.viewModel);

        return new CategoryFormController(persistenceStrategy,new VirtualThreadJobExecutor());
    }
}

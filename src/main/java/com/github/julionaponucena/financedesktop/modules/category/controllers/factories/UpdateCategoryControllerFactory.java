package com.github.julionaponucena.financedesktop.modules.category.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.category.controllers.CategoryFormController;
import com.github.julionaponucena.financedesktop.modules.category.controllers.strategies.CategoryPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.category.controllers.strategies.UpdateCategoryPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.repository.implementations.JDBCCategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.services.UpdateCategoryService;
import com.github.julionaponucena.financedesktop.modules.category.viewmodels.CategoryViewModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateCategoryControllerFactory implements Factory {
    private final ListCategoryOUTFX listCategoryOutFX;
    private final CategoryViewModel categoryViewModel;

    @Override
    public Object call(Class<?> aClass) {
        CategoryRepository repository = new JDBCCategoryRepository();

        UpdateCategoryService service = new UpdateCategoryService(repository);

        CategoryPersistenceStrategy persistenceStrategy = new UpdateCategoryPersistenceStrategy(
                listCategoryOutFX.id(),
                service,categoryViewModel);

        return new CategoryFormController(persistenceStrategy,new VirtualThreadJobExecutor(),listCategoryOutFX.name().getValue());
    }
}

package com.github.julionaponucena.financedesktop.modules.category.controllers.factories;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.VirtualThreadJobExecutor;
import com.github.julionaponucena.financedesktop.modules.category.controllers.CategoryController;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.repository.implementations.JDBCCategoryRepository;
import com.github.julionaponucena.financedesktop.modules.category.services.CategoryService;
import com.github.julionaponucena.financedesktop.modules.category.services.converters.CategoryConverter;
import com.github.julionaponucena.financedesktop.modules.category.viewmodels.CategoryViewModel;

public class CategoryControllerFactory implements Factory {
    @Override
    public Object call(Class<?> aClass) {
        CategoryRepository repository = new JDBCCategoryRepository();

        CategoryService service = new CategoryService(repository);

        JobExecutor executor =new VirtualThreadJobExecutor();

        CategoryViewModel viewModel = new CategoryViewModel(service,executor,new CategoryConverter());

        return new CategoryController(viewModel,new WindowLoader(executor));
    }
}

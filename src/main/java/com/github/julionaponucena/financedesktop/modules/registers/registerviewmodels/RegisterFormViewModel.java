package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.listmanager.PersistenceListManagerFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.enums.RegisterTypeEnum;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.services.ListCategoryService;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.CategoryConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterFormViewModel {
    private final RegisterPersistenceStrategy persistenceStrategy;
    private final JobExecutor executor;
    private final CategoryConverter categoryConverter;
    private final ListCategoryService service;
    @Getter
    private final BooleanProperty isLoading;
    private final PersistenceListManagerFX<CategoryPersistenceInputFX, ListCategoryOUTFX> data;

    public RegisterFormViewModel(RegisterPersistenceStrategy persistenceStrategy, ListCategoryService service,JobExecutor executor,
                                 CategoryConverter categoryConverter,PersistenceListManagerFX<CategoryPersistenceInputFX,ListCategoryOUTFX> data) {
        this.persistenceStrategy = persistenceStrategy;
        this.service = service;
        this.data=data;
        this.executor=executor;
        this.isLoading = new SimpleBooleanProperty(false);
        this.categoryConverter = categoryConverter;
    }

    public RegisterFormViewModel(RegisterPersistenceStrategy persistenceStrategy,
                                 ListCategoryService service,
                                 JobExecutor executor,
                                 CategoryConverter categoryConverter,
                                 PersistenceListManagerFX<CategoryPersistenceInputFX,ListCategoryOUTFX> data,
                                 SimpleBooleanProperty isLoading
    ) {
        this.persistenceStrategy = persistenceStrategy;
        this.executor = executor;
        this.data = data;
        this.service = service;
        this.isLoading = isLoading;
        this.categoryConverter = categoryConverter;
    }

    public void searchCategories(){
        this.executor.execute(this::processCategories,new ExceptionHandler("Não foi possível buscar categorias")
        );
    }

    public void removeCategory(CategoryPersistenceInputFX category) {
        this.data.remove(category);
    }

    public void addCategory(ListCategoryOUTFX category) {
        this.data.add(category);
    }

    private void processCategories() throws InternalServerException {
        List<ListCategoryRelOUT> categories = this.service.findAll();

        this.data.addAllOutput(categories.stream().map(categoryConverter::convertToListCategoryOutFX)
                .collect(Collectors.toList()));

       this.isLoading.set(false);
    }

    public void addDefaultCategories(List<CategoryPersistenceInputFX> categories) {
        this.data.addDefault(categories);
    }

    public void save(String title, LocalDate date, BigDecimal value, RegisterTypeEnum type){
        if(type == RegisterTypeEnum.DEBIT){
            value =value.multiply(BigDecimal.valueOf(-1));
        }

        List<CategoryPersistenceInputFX> categoriesFX = this.data.getInputData();

        List<CategoryPersistenceInput> categories = categoriesFX.stream().map(categoryConverter::convertToInput).toList();

        BigDecimal finalValue = value;
        this.executor.execute(() -> this.persistenceStrategy.persist(title,date,categories, finalValue),
                new ExceptionHandler("Não foi possível salvar o registro"));
    }

    public SimpleObjectProperty<ObservableList<CategoryPersistenceInputFX>> getData(){
        return this.data.getInputProperty();
    }

    public SimpleObjectProperty<ObservableList<ListCategoryOUTFX>> getCategoryOUTS(){
        return this.data.getOutputProperty();
    }
}

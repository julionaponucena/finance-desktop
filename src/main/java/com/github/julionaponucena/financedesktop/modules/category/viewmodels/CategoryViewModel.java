package com.github.julionaponucena.financedesktop.modules.category.viewmodels;

import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.modules.category.controllers.observers.CreateCategoryObserver;
import com.github.julionaponucena.financedesktop.modules.category.controllers.observers.UpdateCategoryObserver;
import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.category.data.out.UpdateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.services.CategoryService;
import com.github.julionaponucena.financedesktop.modules.category.services.converters.CategoryConverter;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.List;

public class CategoryViewModel implements CreateCategoryObserver, UpdateCategoryObserver {

    private final CategoryService service;
    private final JobExecutor executor;
    private final CategoryConverter converter;

    private final ListManagerFX<ListCategoryOUTFX> data;

    @Getter
    private final SimpleBooleanProperty isLoading;

    public CategoryViewModel(CategoryService service, JobExecutor executor,CategoryConverter converter) {
        this.service = service;
        this.executor = executor;
        this.data = new ListManagerFX<>();
        this.isLoading = new SimpleBooleanProperty(true);
        this.converter=converter;
    }

    public CategoryViewModel(CategoryService service, JobExecutor executor,
                             CategoryConverter converter,
                             ListManagerFX<ListCategoryOUTFX> data, SimpleBooleanProperty isLoading) {
        this.service = service;
        this.executor = executor;
        this.data = data;
        this.isLoading = isLoading;
        this.converter=converter;
    }

    public void searchCategories(){
        this.executor.execute(() -> {
            List<ListCategoryOUT> listCategoryOUTS = this.service.findAll();

            List<ListCategoryOUTFX> listCategoryOUTFX = listCategoryOUTS.stream()
                    .map(converter::convert).toList();

            this.data.addAll(listCategoryOUTFX);
            this.isLoading.setValue(false);
        },new ExceptionHandler("Não foi possível listar categorias"));
    }

    @Override
    public void onCreate(CreateCategoryOUT categoryOUT) {
        ListCategoryOUTFX listCategoryOUTFX = this.converter.convert(categoryOUT);

        this.data.add(listCategoryOUTFX);
    }

    public void delete(int id){
        this.executor.execute(() -> {
            this.service.delete(id);

            Platform.runLater(()->
               this.data.remove(listCategoryOUTFX -> listCategoryOUTFX.id() == id)
            );
        },new ExceptionHandler("Não foi possível apagar categoria"));
    }

    @Override
    public void onUpdate(UpdateCategoryOUT categoryOUT) {
        ListCategoryOUTFX listCategoryOUTFX = this.data
                .findOne(listCategoryOUTFX1 -> listCategoryOUTFX1.id()==categoryOUT.id())
                .orElseThrow();

        listCategoryOUTFX.setName(categoryOUT.name());
    }

    public SimpleObjectProperty<ObservableList<ListCategoryOUTFX>> getData(){
        return this.data.get();
    }
}

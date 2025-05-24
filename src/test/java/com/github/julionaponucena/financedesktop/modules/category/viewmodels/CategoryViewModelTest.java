package com.github.julionaponucena.financedesktop.modules.category.viewmodels;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.RunnableHandler;
import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.category.data.out.UpdateCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.services.CategoryService;
import com.github.julionaponucena.financedesktop.modules.category.services.converters.CategoryConverter;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class CategoryViewModelTest {
    private CategoryService service;
    private JobExecutor executor;
    private ListManagerFX<ListCategoryOUTFX> data;
    private SimpleBooleanProperty isLoading;
    private CategoryConverter converter;
    private CategoryViewModel viewModel;

    @BeforeEach
    void config(){
        this.service= Mockito.mock(CategoryService.class);
        this.executor = Mockito.mock(JobExecutor.class);
        this.data = Mockito.mock(ListManagerFX.class);
        this.isLoading = Mockito.mock(SimpleBooleanProperty.class);
        this.converter = Mockito.mock(CategoryConverter.class);
        this.viewModel = new CategoryViewModel(service,executor,converter,data,isLoading);
    }

    @Test
    void searchCategories()throws InternalServerException {
        ListCategoryOUT categoryOUT = new ListCategoryOUT(1,"teste",1);
        ListCategoryOUTFX categoryOUTFX = new ListCategoryOUTFX(1,new SimpleStringProperty("teste"),1);

        Mockito.when(this.service.findAll()).thenReturn(List.of(categoryOUT));
        Mockito.when(this.converter.convert(categoryOUT)).thenReturn(categoryOUTFX);

        this.viewModel.searchCategories();

        this.runTask();

        Mockito.verify(this.converter,Mockito.times(1)).convert(categoryOUT);
        Mockito.verify(this.service, Mockito.times(1)).findAll();
        Mockito.verify(this.data,Mockito.times(1)).addAll(Mockito.anyList());
        Mockito.verify(this.isLoading,Mockito.times(1)).setValue(false);
    }

    @Test
    void onCreate(){
        CreateCategoryOUT categoryOUT = new CreateCategoryOUT(1,"teste");

        ListCategoryOUTFX categoryOUTFX = new ListCategoryOUTFX(1,new SimpleStringProperty("teste"),0);

        Mockito.when(this.converter.convert(categoryOUT)).thenReturn(categoryOUTFX);

        this.viewModel.onCreate(categoryOUT);

        Mockito.verify(this.data,Mockito.times(1)).add(categoryOUTFX);
        Mockito.verify(this.converter,Mockito.times(1)).convert(categoryOUT);
    }

    @Test
    void delete()throws InternalServerException, ApplicationException {
        try (MockedStatic<Platform> platformMockedStatic = Mockito.mockStatic(Platform.class)) {
            this.viewModel.delete(1);

            this.runTask();

            ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
            platformMockedStatic.verify(() -> Platform.runLater(captor.capture()));

            captor.getValue().run();
        }

        Mockito.verify(this.service,Mockito.times(1)).delete(1);
        Mockito.verify(this.data,Mockito.times(1)).remove(Mockito.any());
    }

    @Test
    void onUpdate() {
        UpdateCategoryOUT categoryOUT = new UpdateCategoryOUT(2,"teste updated");

        ListCategoryOUTFX findedCategory = Mockito.spy(new ListCategoryOUTFX(2,new SimpleStringProperty("teste"),10));

        Mockito.when(this.data.findOne(Mockito.any())).thenReturn(Optional.of(findedCategory));

        this.viewModel.onUpdate(categoryOUT);

        Mockito.verify(this.data,Mockito.times(1)).findOne(Mockito.any());
        Mockito.verify(findedCategory,Mockito.times(1)).setName(categoryOUT.name());
    }

    @Test
    void getData(){
        SimpleObjectProperty<ObservableList<ListCategoryOUTFX>> listCategoryOUTFX = new SimpleObjectProperty<>();
        Mockito.when(this.data.get()).thenReturn(listCategoryOUTFX);

        SimpleObjectProperty<ObservableList<ListCategoryOUTFX>> result = this.viewModel.getData();

        Assertions.assertSame(listCategoryOUTFX,result);
    }

    private void runTask(){
        ArgumentCaptor<RunnableHandler> captor = ArgumentCaptor.forClass(RunnableHandler.class);

        Mockito.verify(this.executor).execute(captor.capture(),Mockito.any());

        try{
            captor.getValue().run();
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}
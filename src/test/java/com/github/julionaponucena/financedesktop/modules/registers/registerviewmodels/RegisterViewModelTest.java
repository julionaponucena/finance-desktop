package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.RunnableHandler;
import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.*;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;
import com.github.julionaponucena.financedesktop.modules.registers.services.RegisterService;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.RegisterConverter;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RegisterViewModelTest {
    private JobExecutor executor;
    private RegisterService service;
    private RegisterViewModel viewModel;
    private ListManagerFX<ListRegisterOUTFX> data;
    private SimpleStringProperty total;
    private SimpleBooleanProperty isLoading;
    private ArgumentCaptor<RunnableHandler> runnableCaptor;
    private RegisterConverter converter;
    private StringBinding totalBinding;

    @BeforeEach
    void config() {
        this.executor=Mockito.mock(JobExecutor.class);
        this.service = Mockito.mock(RegisterService.class);
        this.data = Mockito.mock(ListManagerFX.class);
        this.total = Mockito.mock(SimpleStringProperty.class);
        this.isLoading = Mockito.mock(SimpleBooleanProperty.class);
        this.converter= Mockito.mock(RegisterConverter.class);
        this.totalBinding = Mockito.mock(StringBinding.class);
        this.viewModel = new RegisterViewModel(service, executor,converter,data,total,isLoading,totalBinding);
        this.runnableCaptor = ArgumentCaptor.forClass(RunnableHandler.class);
    }

    @Test
    void searchRegisters() throws InternalServerException {
        ListRegisterOUT listRegisterOUT =new ListRegisterOUT(1,"teste", LocalDate.now(),new ArrayList<>(),new BigDecimal(1));

        List<ListRegisterOUT> list = List.of(listRegisterOUT);

        Mockito.when(this.service.findAllByBlockId(1)).thenReturn(list);
        Mockito.when(this.converter.convertToListRegisterOUTFX(listRegisterOUT)).thenReturn(
                new ListRegisterOUTFX(1,
                        new SimpleStringProperty("teste"),
                        new SimpleObjectProperty<>(LocalDate.now()),FXCollections.observableArrayList(),
                        new SimpleObjectProperty<>(new BigDecimal(1))
                )
        );

        this.viewModel.searchRegisters(1);

        this.runTask();

        Mockito.verify(this.service,Mockito.times(1)).findAllByBlockId(1);
        Mockito.verify(this.total,Mockito.times(1)).bind(Mockito.any());
        Mockito.verify(this.data,Mockito.times(1)).addAll(Mockito.anyList());
        Mockito.verify(this.isLoading,Mockito.times(1)).setValue(false);
    }

    @Test
    void notifyCreation() {
        List<ListCategoryRelOUT> categories = List.of(
                new ListCategoryRelOUT(1,"Teste")
        );

        LocalDate localDate = LocalDate.now();

        CreateRegisterOUT registerOUT = new CreateRegisterOUT(1,"Teste",localDate,categories,new BigDecimal(1));

        ListRegisterOUTFX listRegisterOUTFX = new ListRegisterOUTFX(1,new SimpleStringProperty("Teste"),new SimpleObjectProperty<>(localDate),
                FXCollections.observableArrayList(
                        new ListCategoryOUTFX(1,"teste")
                )
                ,
                new SimpleObjectProperty<>(new BigDecimal(1)));

        Mockito.when(this.converter.convertToListRegisterOUTFX(registerOUT)).thenReturn(listRegisterOUTFX);

        this.viewModel.notifyCreation(registerOUT);

        Mockito.verify(this.data,Mockito.times(1)).add(listRegisterOUTFX);
        Mockito.verify(this.totalBinding,Mockito.times(1)).invalidate();
    }

    @Test
    void delete() throws InternalServerException, ApplicationException {
        this.viewModel.delete(1);

        try(MockedStatic<Platform> mockedStatic = Mockito.mockStatic(Platform.class)){
            this.runTask();

            ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);

            mockedStatic.verify(() -> Platform.runLater(argumentCaptor.capture()));

            argumentCaptor.getValue().run();
        }

        Mockito.verify(this.data,Mockito.times(1)).remove(Mockito.any());
        Mockito.verify(this.service,Mockito.times(1)).delete(1);
    }

    @Test
    void onUpdateRegister(){
        ListRegisterOUTFX findedRegisterOUT= Mockito.spy(createListRegisterOUTFX(1,"teste"));

        ListCategoryOUTFX listCategoryOUTFX = new ListCategoryOUTFX(1,"Categoria");

        CategoryPersistenceVO categoryPersistenceVO =new CategoryPersistenceVO(1,"Categoria");

        UpdateRegisterOUT registerOUT = new UpdateRegisterOUT(
                1,
                "teste",
                LocalDate.now(),
                List.of(categoryPersistenceVO),
                BigDecimal.ZERO
        );

        Mockito.when(this.converter.convertToListCategoryOUTFX(categoryPersistenceVO)).thenReturn(listCategoryOUTFX);
        Mockito.when(this.data.findOne(Mockito.any())).thenReturn(Optional.of(findedRegisterOUT));

        this.viewModel.onUpdateRegister(registerOUT);

        Mockito.verify(findedRegisterOUT,Mockito.times(1)).setDate(registerOUT.date());
        Mockito.verify(findedRegisterOUT,Mockito.times(1)).setTitle(registerOUT.title());
        Mockito.verify(findedRegisterOUT,Mockito.times(1)).setValue(registerOUT.value());
        Mockito.verify(this.converter,Mockito.times(1)).convertToListCategoryOUTFX(categoryPersistenceVO);
        Mockito.verify(this.data,Mockito.times(1)).findOne(Mockito.any());

        ArgumentCaptor<ObservableList<ListCategoryOUTFX>> argumentCaptor = ArgumentCaptor.forClass(ObservableList.class);

        Mockito.verify(findedRegisterOUT,Mockito.times(1)).changeCategories(argumentCaptor.capture());

        Assertions.assertSame(listCategoryOUTFX,argumentCaptor.getValue().getFirst());

        Mockito.verify(this.totalBinding,Mockito.times(1)).invalidate();
    }

    @Test
    void getData(){
        SimpleObjectProperty<ObservableList<ListRegisterOUTFX>> data = new SimpleObjectProperty<>();
        Mockito.when(this.data.get()).thenReturn(data);

        SimpleObjectProperty<ObservableList<ListRegisterOUTFX>> result = this.viewModel.getData();

        Assertions.assertSame(data,result);
    }

    @Test
    void getValue(){
        ObservableList<ListRegisterOUTFX> data = this.viewModel.getValue();

        Mockito.when(this.data.getValue()).thenReturn(data);

        ObservableList<ListRegisterOUTFX> result = this.viewModel.getValue();

        Assertions.assertSame(data,result);
    }

    private void runTask(){
        Mockito.verify(this.executor).execute(runnableCaptor.capture(), Mockito.any());
        try {
            runnableCaptor.getValue().run();
        }catch (Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }

    private static ListRegisterOUTFX createListRegisterOUTFX(int id,String title) {
        return new ListRegisterOUTFX(
                id,
                new SimpleStringProperty(title),
                new SimpleObjectProperty<>(LocalDate.now()),
                FXCollections.observableArrayList(),
                new SimpleObjectProperty<>(BigDecimal.ZERO));
    }
}
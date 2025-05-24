package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.RunnableHandler;
import com.github.julionaponucena.financedesktop.commons.listmanager.PersistenceListManagerFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.enums.RegisterTypeEnum;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.strategies.RegisterPersistenceStrategy;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.services.ListCategoryService;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.CategoryConverter;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class RegisterFormViewModelTest {
    private RegisterPersistenceStrategy persistenceStrategy;
    private JobExecutor executor;
    private PersistenceListManagerFX<CategoryPersistenceInputFX,ListCategoryOUTFX> data;
    private ListCategoryService service;
    private SimpleBooleanProperty isLoading;
    private RegisterFormViewModel viewModel;
    private ArgumentCaptor<RunnableHandler> runnableCaptor;
    private CategoryConverter categoryConverter;

    @BeforeEach
    public void config() {
        this.persistenceStrategy = Mockito.mock(RegisterPersistenceStrategy.class);
        this.executor = Mockito.mock(JobExecutor.class);
        this.data = Mockito.mock(PersistenceListManagerFX.class);
        this.service = Mockito.mock(ListCategoryService.class);
        this.isLoading= Mockito.mock(SimpleBooleanProperty.class);
        this.categoryConverter = Mockito.mock(CategoryConverter.class);
        this.viewModel = new RegisterFormViewModel(persistenceStrategy,service,executor,categoryConverter,
                data,isLoading);
        this.runnableCaptor = ArgumentCaptor.forClass(RunnableHandler.class);
    }

    @Test
    void searchCategories() throws InternalServerException {
        ListCategoryRelOUT categoryOUT = new ListCategoryRelOUT(1,"teste");

        List<ListCategoryRelOUT> listCategoryOUTS =List.of(categoryOUT);

        Mockito.when(this.service.findAll()).thenReturn(listCategoryOUTS);
        Mockito.when(this.categoryConverter.convertToListCategoryOutFX(categoryOUT)).thenReturn(
                new ListCategoryOUTFX(1,"teste")
        );

        this.viewModel.searchCategories();

        this.runHandleTask();

        Mockito.verify(this.service,Mockito.times(1)).findAll();
        Mockito.verify(this.data,Mockito.times(1)).addAllOutput(Mockito.anyList());
        Mockito.verify(this.isLoading,Mockito.times(1)).set(false);
    }

    @Test
    void removeCategory() {
        CategoryPersistenceInputFX deleteInput = createCategoryPersistenceInputFX(1,"delete");

        this.viewModel.removeCategory(deleteInput);

        Mockito.verify(this.data,Mockito.times(1)).remove(Mockito.any());
    }

    @Test
    void addCategory() {
        ListCategoryOUTFX categoryOUTFX = new ListCategoryOUTFX(1,"teste");

        this.viewModel.addCategory(categoryOUTFX);

        Mockito.verify(this.data,Mockito.times(1)).add(categoryOUTFX);
    }

    @Test
    void addDefaultCategories(){
        List<CategoryPersistenceInputFX> cagories = new ArrayList<>();

        this.viewModel.addDefaultCategories(cagories);

        Mockito.verify(this.data,Mockito.times(1)).addDefault(cagories);
    }

    @Test
    void saveCreditValue() throws ApplicationException, InternalServerException{
        CategoryPersistenceInput input = createCategoryPersistenceInput();

        ArgumentCaptor<List<CategoryPersistenceInput>> categoriesCaptor = ArgumentCaptor.forClass(List.class);

        String title="title";
        LocalDate date = LocalDate.now();
        BigDecimal value= new BigDecimal(1);

        CategoryPersistenceInputFX inputFX =this.mockConvertCategoryPersistenceInput(input);

        Mockito.when(this.data.getInputData()).thenReturn(List.of(inputFX));

        this.viewModel.save(title,date,value,RegisterTypeEnum.CREDIT);

        this.runTask();

        Mockito.verify(this.persistenceStrategy,Mockito.times(1)).persist(
                Mockito.eq(title),
                Mockito.eq(date),
                categoriesCaptor.capture(),
                Mockito.eq(value));

        this.validateInput(input, getCategoryPersistenceInput(categoriesCaptor));
    }

    @Test
    void saveDebitValue()throws ApplicationException,InternalServerException {
        String title="title";
        LocalDate date = LocalDate.now();
        BigDecimal value= new BigDecimal(1);

        CategoryPersistenceInput input = createCategoryPersistenceInput();

        CategoryPersistenceInputFX inputFX = this.mockConvertCategoryPersistenceInput(input);

        Mockito.when(this.data.getInputData()).thenReturn(List.of(inputFX));

        this.viewModel.save(title,date,value,RegisterTypeEnum.DEBIT);

        this.runTask();

        ArgumentCaptor<List<CategoryPersistenceInput>> categoriesCaptor = ArgumentCaptor.forClass(List.class);

        ArgumentCaptor<BigDecimal> valueCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        Mockito.verify(this.persistenceStrategy,Mockito.times(1)).persist(
                Mockito.eq(title),
                Mockito.eq(date),
                categoriesCaptor.capture(),
                valueCaptor.capture());

        this.validateInput(input, getCategoryPersistenceInput(categoriesCaptor));

        Assertions.assertEquals(new BigDecimal(-1),valueCaptor.getValue());
    }

    private static CategoryPersistenceInput getCategoryPersistenceInput(ArgumentCaptor<List<CategoryPersistenceInput>> categoriesCaptor) {
        return categoriesCaptor.getValue().getFirst();
    }

    private void validateInput(CategoryPersistenceInput input, CategoryPersistenceInput captureInput) {
        Assertions.assertSame(input,captureInput);
    }

    private static CategoryPersistenceInput createCategoryPersistenceInput() {
        return new CategoryPersistenceInput(1,"teste",CategoryPersistenceState.CREATED,CategoryPersistenceState.CREATED);
    }

    private CategoryPersistenceInputFX mockConvertCategoryPersistenceInput(CategoryPersistenceInput input) {
        CategoryPersistenceInputFX inputFX =createCategoryPersistenceInputFX(1,"teste");

        Mockito.when(this.categoryConverter.convertToInput(inputFX))
                .thenReturn(input);

        return inputFX;
    }

    private void runTask(){
        ArgumentCaptor<RunnableHandler> runnableArgumentCaptor = ArgumentCaptor.forClass(RunnableHandler.class);

        Mockito.verify(this.executor).execute(runnableArgumentCaptor.capture(),Mockito.any());

        try {
            runnableArgumentCaptor.getValue().run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void runHandleTask(){
        Mockito.verify(this.executor).execute(runnableCaptor.capture(),Mockito.any());
        try {
            runnableCaptor.getValue().run();
        }catch (Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }


    private static CategoryPersistenceInputFX createCategoryPersistenceInputFX(int id, String name) {
        return createCategoryPersistenceInputFX(id,name,CategoryPersistenceState.CREATED);
    }

    private static CategoryPersistenceInputFX createCategoryPersistenceInputFX(int id, String name,CategoryPersistenceState state) {
        return new CategoryPersistenceInputFX(id,
                name,state,state);
    }
}
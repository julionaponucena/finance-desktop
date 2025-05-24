package com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.executor.RunnableHandler;
import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.UpdateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.ListBlockRegisterService;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.converters.BlockRegisterConverter;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class BlockRegisterViewModelTest {
    private ListBlockRegisterService service;
    private JobExecutor executor;
    private BlockRegisterConverter converter;
    private SimpleBooleanProperty isLoading;
    private SimpleStringProperty total;
    private ListManagerFX<ListBlockRegisterOUTFX> data;
    private BlockRegisterViewModel viewModel;

    @BeforeEach
    void config(){
        this.service = Mockito.mock(ListBlockRegisterService.class);
        this.executor = Mockito.mock(JobExecutor.class);
        this.converter = Mockito.mock(BlockRegisterConverter.class);
        this.isLoading = Mockito.mock(SimpleBooleanProperty.class);
        this.total = Mockito.spy(new SimpleStringProperty());
        this.data = Mockito.mock(ListManagerFX.class);
        this.viewModel = new BlockRegisterViewModel(service,executor,converter,data,isLoading,total);
    }

    @Test
    void searchCategories() throws InternalServerException {
        BlockRegisterProj proj = new BlockRegisterProj(1,"teste",new BigDecimal(1),false);
        ListBlockRegisterOUTFX blockRegisterOUTFX = new ListBlockRegisterOUTFX(1,new SimpleStringProperty("teste"),new BigDecimal(1),
                false);

        Mockito.when(this.service.list()).thenReturn(List.of(proj));
        Mockito.when(this.converter.convert(proj)).thenReturn(blockRegisterOUTFX);
        Mockito.when(this.data.get()).thenReturn(new SimpleObjectProperty<>(FXCollections.observableArrayList()));

        this.viewModel.searchCategories();

        this.runTask();

        Mockito.verify(this.data,Mockito.times(1)).addAll(Mockito.anyList());
        Mockito.verify(this.total,Mockito.times(1)).set(Mockito.any());
        Mockito.verify(this.isLoading,Mockito.times(1)).set(false);
    }

    @Test
    void notifyCreation(){
        CreateBlockRegisterOUT blockRegisterOUT = new CreateBlockRegisterOUT(1,"teste");
        ListBlockRegisterOUTFX blockRegisterOUTFX = new ListBlockRegisterOUTFX(1,new SimpleStringProperty("teste"),new BigDecimal(1)
        ,false);

        Mockito.when(this.converter.convert(blockRegisterOUT)).thenReturn(blockRegisterOUTFX);

        this.viewModel.notifyCreation(blockRegisterOUT);

        Mockito.verify(this.converter,Mockito.times(1)).convert(blockRegisterOUT);

        Mockito.verify(this.data,Mockito.times(1)).add(blockRegisterOUTFX);
    }

    @Test
    void notifyUpdate(){
        ListBlockRegisterOUTFX updated = Mockito.spy(
                new ListBlockRegisterOUTFX(1,new SimpleStringProperty("Updated"),new BigDecimal(1),false)
        );

        Mockito.when(this.data.findOne(Mockito.any())).thenReturn(Optional.of(updated));

        UpdateBlockRegisterOUT blockRegisterOUT = new UpdateBlockRegisterOUT(1,"New Title");

        this.viewModel.notifyUpdate(blockRegisterOUT);

        Mockito.verify(updated,Mockito.times(1)).setTitle(blockRegisterOUT.title());
        Mockito.verify(data,Mockito.times(1)).findOne(Mockito.any());
    }

    @Test
    void delete() throws InternalServerException, ApplicationException {
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

    private void runTask(){
        ArgumentCaptor<RunnableHandler> captor = ArgumentCaptor.forClass(RunnableHandler.class);

        Mockito.verify(this.executor).execute(captor.capture(),Mockito.any());

        try {
            captor.getValue().run();
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}
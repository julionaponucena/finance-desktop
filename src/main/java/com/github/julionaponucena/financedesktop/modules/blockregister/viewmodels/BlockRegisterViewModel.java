package com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels;

import com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue.ReactiveTotalValue;
import com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue.ReactiveTotalValueFactory;
import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.CreateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers.UpdateBlockRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.UpdateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.ListBlockRegisterService;
import com.github.julionaponucena.financedesktop.modules.blockregister.services.converters.BlockRegisterConverter;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.List;

public class BlockRegisterViewModel implements CreateBlockRegisterObserver, UpdateBlockRegisterObserver {
    private final ListBlockRegisterService listBlockRegisterService;
    private final JobExecutor executor;
    private final BlockRegisterConverter converter;

    private final ListManagerFX<ListBlockRegisterOUTFX> data;

    @Getter
    private final SimpleBooleanProperty isLoading;

    @Getter
    private final SimpleStringProperty total;

    public BlockRegisterViewModel(ListBlockRegisterService listBlockRegisterService, JobExecutor executor, BlockRegisterConverter converter) {
        this.listBlockRegisterService = listBlockRegisterService;
        this.executor = executor;
        this.converter = converter;
        this.data = new ListManagerFX<>();
        this.isLoading = new SimpleBooleanProperty(true);
        this.total = new SimpleStringProperty();
    }

    public BlockRegisterViewModel(ListBlockRegisterService listBlockRegisterService, JobExecutor executor, BlockRegisterConverter converter,
                                  ListManagerFX<ListBlockRegisterOUTFX> data,
                                  SimpleBooleanProperty isLoading, SimpleStringProperty total) {
        this.listBlockRegisterService = listBlockRegisterService;
        this.executor = executor;
        this.converter = converter;
        this.data = data;
        this.isLoading = isLoading;
        this.total = total;
    }

    public void searchCategories() {
        this.executor.execute(() -> {
            List<ListBlockRegisterOUTFX> blockRegisterOUTFXES = this.listBlockRegisterService.list()
                    .stream().map(converter::convert).toList();

            this.data.addAll(blockRegisterOUTFXES);

            ReactiveTotalValue<ListBlockRegisterOUTFX> reactiveTotalValue = ReactiveTotalValueFactory
                    .call(ListBlockRegisterOUTFX::value,this.data.get());

            this.total.set(reactiveTotalValue.call());

            this.isLoading.set(false);

        },new ExceptionHandler("Houve um erro ao buscar os Blocos de registros"));
    }

    @Override
    public void notifyCreation(CreateBlockRegisterOUT createBlockRegisterOUT) {
        ListBlockRegisterOUTFX listBlockRegisterOUTFX = this.converter.convert(createBlockRegisterOUT);

        this.data.add(listBlockRegisterOUTFX);
    }

    @Override
    public void notifyUpdate(UpdateBlockRegisterOUT updateBlockRegisterOUT) {
        ListBlockRegisterOUTFX findedBlock = this.data
                .findOne(listBlockRegisterOUTFX -> listBlockRegisterOUTFX.id()== updateBlockRegisterOUT.id())
                .orElseThrow();

        findedBlock.setTitle(updateBlockRegisterOUT.title());
    }

    public void delete(int id) {
        this.executor.execute(() -> {
            this.listBlockRegisterService.delete(id);

            Platform.runLater(() ->
                    this.data.remove(listBlockRegisterOUTFX -> listBlockRegisterOUTFX.id() == id));

        },new ExceptionHandler("Houve um erro ao deletar o registro"));
    }

    public SimpleObjectProperty<ObservableList<ListBlockRegisterOUTFX>> getData() {
        return this.data.get();
    }
}

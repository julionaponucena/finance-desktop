package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels;

import com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue.ReactiveTotalValue;
import com.github.julionaponucena.financedesktop.commons.events.reactivetotalvalue.ReactiveTotalValueFactory;
import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.commons.listmanager.ListManagerFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.observers.CreateRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.observers.UpdateRegisterObserver;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.*;
import com.github.julionaponucena.financedesktop.modules.registers.services.RegisterService;
import com.github.julionaponucena.financedesktop.modules.registers.services.converters.RegisterConverter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.List;

public class RegisterViewModel implements CreateRegisterObserver, UpdateRegisterObserver {
    private final RegisterService service;
    private final JobExecutor executor;
    private final RegisterConverter converter;
    private final ListManagerFX<ListRegisterOUTFX> data;

    @Getter
    private final SimpleStringProperty total;

    @Getter
    private final SimpleBooleanProperty isLoading;

    private final StringBinding totalBinding;

    public RegisterViewModel(RegisterService service, JobExecutor executor,
                             RegisterConverter converter , ListManagerFX<ListRegisterOUTFX> data,
                             SimpleStringProperty total, SimpleBooleanProperty isLoading, StringBinding totalBinding) {
        this.service = service;
        this.executor = executor;
        this.converter=converter;
        this.data = data;
        this.total=total;
        this.isLoading = isLoading;
        this.totalBinding=totalBinding;
    }

    public RegisterViewModel(RegisterService service, JobExecutor executor,RegisterConverter converter) {
        this.service = service;
        this.executor = executor;
        this.converter=converter;
        this.data= new ListManagerFX<>();
        this.total= new SimpleStringProperty();
        this.isLoading= new SimpleBooleanProperty(true);

        ReactiveTotalValue<ListRegisterOUTFX> reactiveTotalValue = ReactiveTotalValueFactory
                .call(o -> o.value().getValue(),this.data.get());

        this.totalBinding=Bindings.createStringBinding(reactiveTotalValue,this.data.get(),this.data.getValue());
    }

    public void searchRegisters(int blockId) {
        this.executor.execute(() -> {
            List<ListRegisterOUT> registerList = this.service.findAllByBlockId(blockId);

            List<ListRegisterOUTFX> listRegisterOUTFX =registerList.stream().map(converter::convertToListRegisterOUTFX).toList();

           this.total.bind(this.totalBinding);

            this.data.addAll(listRegisterOUTFX);
            this.isLoading.setValue(false);
        },new ExceptionHandler("Não foi possível encontrar registros"));
    }

    @Override
    public void notifyCreation(CreateRegisterOUT registerOUT) {
        ListRegisterOUTFX listRegister = this.converter.convertToListRegisterOUTFX(registerOUT);

        this.data.add(listRegister);

        this.totalBinding.invalidate();
    }

    public void delete(int id){
        this.executor.execute(() -> {
            this.service.delete(id);

            Platform.runLater(() -> this.data.remove(listRegisterOUTFX -> listRegisterOUTFX.id() == id));
        },new ExceptionHandler("Não foi possível apagar o registro"));
    }

    @Override
    public void onUpdateRegister(UpdateRegisterOUT registerOUT) {
        ListRegisterOUTFX registerOUTFX =this.data.findOne(listRegisterOUTFX -> listRegisterOUTFX.id() == registerOUT.id())
                .orElseThrow();

        List<ListCategoryOUTFX> listCategoryOUTFXES = registerOUT.categories().stream().map(converter::convertToListCategoryOUTFX)
                        .toList();

        registerOUTFX.setDate(registerOUT.date());
        registerOUTFX.setTitle(registerOUT.title());
        registerOUTFX.setValue(registerOUT.value());
        registerOUTFX.changeCategories(FXCollections.observableArrayList(listCategoryOUTFXES));

        this.totalBinding.invalidate();
    }

    public SimpleObjectProperty<ObservableList<ListRegisterOUTFX>> getData() {
        return this.data.get();
    }

    public ObservableList<ListRegisterOUTFX> getValue() {
        return this.data.getValue();
    }
}

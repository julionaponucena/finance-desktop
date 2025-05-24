package com.github.julionaponucena.financedesktop.commons.listmanager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListManagerFX<T> {
    private final SimpleObjectProperty<ObservableList<T>> data = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    public void add(T item) {
        List<T> list = new ArrayList<>();
        list.add(item);
        this.safeAdd(list);
    }


    public void addAll(List<T> items) {
        this.safeAdd(items);
    }

    public void remove(Predicate<T> predicate) {
        if(this.data.getValue().size() >1){
            this.data.getValue().removeIf(predicate);
//            this.data.set(FXCollections.observableArrayList(this.data.getValue()));
        }else{
            this.data.setValue(FXCollections.observableArrayList());
        }
    }

    public ObservableList<T> getValue() {
        return this.data.getValue();
    }

    public SimpleObjectProperty<ObservableList<T>> get(){
        return this.data;
    }

    private void safeAdd(List<T> items) {
        if(this.data.get().isEmpty()){
            this.data.set(FXCollections.observableArrayList(items));
        }else{
            this.data.getValue().addAll(items);
//            this.data.set(FXCollections.observableArrayList(this.data.getValue()));
        }
    }

    public Optional<T> findOne(Predicate<T> predicate) {
        return this.data.getValue().stream().filter(predicate).findFirst();
    }
}

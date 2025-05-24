package com.github.julionaponucena.financedesktop.commons.listmanager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PersistenceListManagerFX<I extends Persistable,O extends Selectable> {
    private final ListManagerFX<I> input = new ListManagerFX<>();
    private final ListManagerFX<O> output = new ListManagerFX<>();
    private final ListManagerFX<I> persistenceInput = new ListManagerFX<>();
    private final ListManagerFX<O> persistenceOutput = new ListManagerFX<>();
    private final PersistenceConverter<I,O> converter;

    public void addAllOutput(List<O> outputs) {
        if(!this.output.getValue().isEmpty() || !persistenceOutput.getValue().isEmpty()){
            throw new IllegalCallerException("Cannot add all to output list");
        }

        this.output.addAll(outputs);
        this.persistenceOutput.addAll(outputs);
    }

    public void addDefault(List<I> inputs) {
        this.input.addAll(inputs);
        this.persistenceInput.addAll(inputs);

        Set<Integer> ids = inputs.stream().map(Persistable::getItemIdentifier).collect(Collectors.toSet());

        this.output.remove(o -> o.isSelected(ids));
    }

    public void add(O output) {
        Optional<I>  optionalI =this.persistenceInput.findOne(output::isSelected);

        if (optionalI.isPresent()) {
            optionalI.get().persistCreateState();
            this.input.add(optionalI.get());
        }else{
            I convertedInput = converter.convert(output);
            this.persistenceInput.add(convertedInput);
            this.input.add(convertedInput);
        }

        this.output.remove(o -> o.equals(output));
    }

    public void remove(I input) {
        input.persistDeleteState();

        this.input.remove(i -> i.equals(input));

        O findedOutput =this.persistenceOutput.findOne(o -> o.isSelected(input)).orElseThrow();

        this.output.add(findedOutput);
    }

    public List<I> getInputData() {
        return this.persistenceInput.getValue();
    }

    public SimpleObjectProperty<ObservableList<O>> getOutputProperty() {
        return this.output.get();
    }

    public SimpleObjectProperty<ObservableList<I>> getInputProperty() {
        return this.input.get();
    }
}

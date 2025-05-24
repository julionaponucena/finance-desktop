package com.github.julionaponucena.financedesktop.modules.category.controllers;

import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.modules.category.controllers.strategies.CategoryPersistenceStrategy;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoryFormController {
    private final CategoryPersistenceStrategy persistenceStrategy;
    private final JobExecutor executor;

    private String categoryName;

    @FXML
    private MFXTextField nameInput;
    @FXML
    private Label errorLabel;

    public CategoryFormController(CategoryPersistenceStrategy persistenceStrategy, JobExecutor executor, String categoryName) {
        this.persistenceStrategy = persistenceStrategy;
        this.executor = executor;
        this.categoryName = categoryName;
    }

    @FXML
    private void initialize(){
        if(categoryName != null){
            this.nameInput.setText(categoryName);
        }

        BooleanExpression condition = Bindings.createBooleanBinding(
                () -> !nameInput.getText().trim().isEmpty(),
                nameInput.textProperty()
        );

        Constraint constraint = Constraint.Builder.build().setSeverity(Severity.ERROR).setMessage("O Campo Não pode estar em branco")
                .setCondition(condition).get();

        this.nameInput.getValidator().constraint(constraint);
    }

    public void save(){
        List<Constraint> constraintList =nameInput.validate();

        StringBuilder message = new StringBuilder();

        for (Constraint constraint : constraintList) {
            if (!constraint.isValid()) {
                message.append(constraint.getMessage()).append("\n");
            }
        }

        if (!message.isEmpty()) {
            this.errorLabel.setText(message.toString());
            return;
        }

        this.executor.execute(() -> this.persistenceStrategy.persist(this.nameInput.getText()),
                new ExceptionHandler("Não foi possível salvar a categoria"));

        this.nameInput.getScene().getWindow().hide();
    }
}

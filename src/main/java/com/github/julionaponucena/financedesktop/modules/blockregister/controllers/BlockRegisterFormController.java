package com.github.julionaponucena.financedesktop.modules.blockregister.controllers;

import com.github.julionaponucena.financedesktop.commons.executor.ExceptionHandler;
import com.github.julionaponucena.financedesktop.commons.executor.JobExecutor;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.strategies.BlockRegisterPersistenceStrategy;
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
public class BlockRegisterFormController {

    private final BlockRegisterPersistenceStrategy persistenceStrategy;
    private final JobExecutor executor;
    private String defaultTitle;

    public BlockRegisterFormController(BlockRegisterPersistenceStrategy persistenceStrategy, JobExecutor executor, String defaultTitle) {
        this.persistenceStrategy = persistenceStrategy;
        this.executor = executor;
        this.defaultTitle = defaultTitle;
    }

    @FXML
    private MFXTextField titleInput;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        BooleanExpression condition = Bindings.createBooleanBinding(
                () -> !titleInput.getText().trim().isEmpty(),
                titleInput.textProperty()
        );

        Constraint constraint = Constraint.Builder.build().setSeverity(Severity.ERROR).setMessage("O Campo Não pode estar em branco")
                .setCondition(condition).get();


        titleInput.getValidator().constraint(constraint);

        if(defaultTitle != null){
            this.titleInput.setText(defaultTitle);
        }
    }

    public void save(){
        List<Constraint> constraintList =titleInput.validate();

        StringBuilder message = new StringBuilder();

        for (Constraint constraint : constraintList) {
            if (!constraint.isValid()){
                message.append(constraint.getMessage()).append("\n");
            }
        }

        if(!message.isEmpty()) {
            errorLabel.setText(message.toString());
            return;
        }

        this.executor.execute(() ->this.persistenceStrategy.persist(this.titleInput.getText()),
                new ExceptionHandler("Houve um erro ao criar um Bloco"));

        this.titleInput.getScene().getWindow().hide();
    }
}

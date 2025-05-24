package com.github.julionaponucena.financedesktop.modules.registers.controllers;

import com.github.julionaponucena.financedesktop.commons.events.ClickTableViewEventFilter;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.enums.RegisterTypeEnum;
import com.github.julionaponucena.financedesktop.modules.registers.data.formdata.RegisterFormData;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterFormViewModel;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class RegisterControllerForm {

    private final RegisterFormViewModel registerFormViewModel;
    private RegisterFormData formData;

    public RegisterControllerForm(RegisterFormViewModel registerFormViewModel, RegisterFormData formData) {
        this.registerFormViewModel = registerFormViewModel;
        this.formData = formData;
    }

    @FXML
    private MFXTextField titleInput;

    @FXML
    private MFXTableColumn<CategoryPersistenceInputFX> contentColumn;

    @FXML
    private MFXTableColumn<CategoryPersistenceInputFX> deleteColumn;

    @FXML
    private MFXTableView<CategoryPersistenceInputFX> tableView;

    @FXML
    private MFXComboBox<ListCategoryOUTFX> comboCategory;

    @FXML
    private MFXProgressSpinner progressSpinner;

    @FXML
    private HBox containerCombo;

    @FXML
    private TextField valueInput;

    @FXML
    private MFXComboBox<RegisterTypeEnum> typeCombo;

    @FXML
    private Label titleError;

    @FXML
    private Label valueError;

    @FXML
    private MFXDatePicker datePicker;

    @FXML
    private Label dateError;

    private final MFXValidator inputValidator = new MFXValidator();

    @FXML
    private void initialize() {
        this.progressSpinner.visibleProperty().bind(this.registerFormViewModel.getIsLoading());
        this.containerCombo.visibleProperty().bind(this.registerFormViewModel.getIsLoading().not());

        this.registerFormViewModel.searchCategories();

        contentColumn.setRowCellFactory(categoryPersistenceInput ->
                new MFXTableRowCell<>(CategoryPersistenceInputFX::getName));

        deleteColumn.setRowCellFactory(categoryPersistenceInput ->{
            MFXButton button = new MFXButton("Remover");

            button.getStyleClass().addAll("button","delete-button");

            MFXTableRowCell<CategoryPersistenceInputFX,String> tableRowCell = new MFXTableRowCell<>(categoryPersistenceInput1 ->{
                button.setOnAction(event -> this.registerFormViewModel.removeCategory(categoryPersistenceInput1));

                return "";
            });

            tableRowCell.setGraphic(button);

            return tableRowCell;
                });

        this.tableView.itemsProperty().bind(this.registerFormViewModel.getData());

        this.comboCategory.itemsProperty().bind(this.registerFormViewModel.getCategoryOUTS());

        this.comboCategory.setConverter(new StringConverter<>() {

            @Override
            public String toString(ListCategoryOUTFX listCategoryOUT) {
                return listCategoryOUT != null ? listCategoryOUT.name() : "";
            }

            @Override
            public ListCategoryOUTFX fromString(String s) {
                return null;
            }
        });

        this.registerFormViewModel.getData().addListener((observable,
                                                          oldValue,
                                                          newValue) ->
            this.tableView.update()
        );

        StringConverter<BigDecimal> converter = new StringConverter<>() {
            @Override
            public String toString(BigDecimal value) {
                if (value == null) return "";
                return NumberFormat.getCurrencyInstance(Locale.of("pt","BR")).format(value);
            }

            @Override
            public BigDecimal fromString(String text) {
                try {
                    String cleaned = text.replaceAll("[^\\d,]", "").replace(",", ".");
                    return cleaned.isEmpty() ? BigDecimal.ZERO : new BigDecimal(cleaned);
                } catch (NumberFormatException e) {
                    return BigDecimal.ZERO;
                }
            }
        };


        StringBuilder digitos = new StringBuilder();

        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        TextFormatter<BigDecimal> formatter = new TextFormatter<>(
                converter,
                BigDecimal.ZERO,
                change -> {
            if (change.isContentChange()) {
                String input = change.getText().replaceAll("[^\\d]", "");

                if (change.isAdded()) {
                    digitos.append(input);
                } else if (change.isDeleted() && !digitos.isEmpty()) {
                    digitos.deleteCharAt(digitos.length() - 1);
                }

                if (digitos.length() > 11) {
                    digitos.setLength(11);
                }

                String valorFormatado;
                if (digitos.isEmpty()) {
                    valorFormatado = formato.format(0.0);
                } else {
                    double valor = Long.parseLong(digitos.toString()) / 100.0;
                    valorFormatado = formato.format(valor);
                }
                change.setRange(0, change.getControlText().length());
                change.setText(valorFormatado);
                change.setCaretPosition(valorFormatado.length());
                change.setAnchor(valorFormatado.length());
            }
            return change;
        });

        this.valueInput.setTextFormatter(formatter);

        BooleanExpression numberCondition = Bindings.createBooleanBinding(
                () -> BigDecimal.ZERO.compareTo((BigDecimal) valueInput.getTextFormatter().getValue()) != 0,
                valueInput.textProperty()
        );

        Constraint constraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
                .setMessage("O valor não pode ser igual a 0").setCondition(numberCondition).get();

        this.inputValidator.constraint(constraint);

        this.typeCombo.setItems(FXCollections.observableArrayList(RegisterTypeEnum.CREDIT,RegisterTypeEnum.DEBIT));

        this.typeCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(RegisterTypeEnum registerTypeEnum) {
                if (registerTypeEnum == null) return "";
                return registerTypeEnum.getValue();
            }

            @Override
            public RegisterTypeEnum fromString(String s) {
                return RegisterTypeEnum.fromValue(s).orElse(RegisterTypeEnum.CREDIT);
            }
        });

        BooleanExpression titleCondition = Bindings.createBooleanBinding(
          () -> !this.titleInput.getText().trim().isEmpty(),
          this.titleInput.textProperty()
        );

        Constraint titleConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
                .setMessage("O título não pode estar em branco").setCondition(titleCondition).get();

        this.titleInput.getValidator().constraint(titleConstraint);

        this.typeCombo.selectItem(RegisterTypeEnum.CREDIT);

        BooleanExpression dateCondition = Bindings.createBooleanBinding(
                () -> this.datePicker.getValue()!=null
                , datePicker.textProperty()
        );

        Constraint dateConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
                .setMessage("Data não pode estar em branco").setCondition(dateCondition).get();

        this.datePicker.getValidator().constraint(dateConstraint);

        if(this.formData!= null){
            this.titleInput.setText(formData.title());
            this.registerFormViewModel.addDefaultCategories(formData.categories());
            formatter.setValue(formData.value());
            this.typeCombo.selectItem(formData.type());
            this.datePicker.setValue(formData.date());
        }

        this.tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, new ClickTableViewEventFilter());
    }

    public void addCategory() {
        ListCategoryOUTFX category =this.comboCategory.getValue();

        this.comboCategory.clearSelection();

        this.registerFormViewModel.addCategory(category);
    }

    public void save(){
        List<Constraint> constraints = this.inputValidator.validate();

        StringBuilder message = new StringBuilder();

        boolean hasError = false;

        for (Constraint constraint : constraints) {
            if(!constraint.isValid()){
                message.append(constraint.getMessage()).append("\n");
            }
        }

        if (!message.isEmpty()) {
            valueError.setText(message.toString());

            hasError = true;
        }

        message = new StringBuilder();

        constraints = this.titleInput.validate();

        for (Constraint constraint : constraints) {
            if(!constraint.isValid()){
                message.append(constraint.getMessage()).append("\n");
            }
        }

        if (!message.isEmpty()) {
            titleError.setText(message.toString());

            hasError = true;
        }

        constraints = this.datePicker.validate();

        message = new StringBuilder();

        for (Constraint constraint : constraints) {
            if(!constraint.isValid()){
                message.append(constraint.getMessage()).append("\n");
            }
        }

        if (!message.isEmpty()) {
            this.dateError.setText(message.toString());

            hasError = true;
        }

        if (hasError) {
            return;
        }

        BigDecimal value = (BigDecimal) this.valueInput.getTextFormatter().getValue();

        RegisterTypeEnum type = this.typeCombo.getValue();

       this.registerFormViewModel.save(titleInput.getText(),this.datePicker.getValue(),value,type);

       this.typeCombo.getScene().getWindow().hide();
    }
}

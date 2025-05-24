package com.github.julionaponucena.financedesktop.modules.registers.controllers;

import com.github.julionaponucena.financedesktop.commons.Factory;
import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.events.ClickTableViewEventFilter;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.rowcellfactory.RegisterActionsRowCellFactory;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.factories.CreateRegisterControllerFactory;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegisterController {
    private final RegisterViewModel viewModel;
    private final int blockId;
    private final WindowLoader windowLoader;

    @FXML
    private MFXTableView<ListRegisterOUTFX> tableView;

    @FXML
    private MFXTableColumn<ListRegisterOUTFX> titleColumn;

    @FXML
    private MFXTableColumn<ListRegisterOUTFX> dateColumn;

    @FXML
    private MFXTableColumn<ListRegisterOUTFX>  valueColumn;

    @FXML
    private MFXTableColumn<ListRegisterOUTFX> actionsColumn;

    @FXML
    private MFXTableColumn<ListRegisterOUTFX> categoryColumn;

    @FXML
    private MFXProgressSpinner progressSpinner;

    @FXML
    private Text totalText;

    @FXML
    private void initialize() {
        this.progressSpinner.visibleProperty().bind(this.viewModel.getIsLoading());
        this.tableView.visibleProperty().bind(this.viewModel.getIsLoading().not());

        this.tableView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            bindDataWithTable();
        });

        this.viewModel.getIsLoading().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                this.viewModel.getValue().addListener((ListChangeListener<? super ListRegisterOUTFX>) change ->
                        bindDataWithTable());
            }
        });

        this.viewModel.searchRegisters(blockId);

        this.tableView.itemsProperty().bind(this.viewModel.getData());



//        this.registerViewModel.getData().addListener((observable,
//                                                      oldValue,
//                                                      newValue) ->
//                this.tableView.autosizeColumnsOnInitialization());

        this.tableView.autosizeColumnsOnInitialization();

//        this.registerViewModel.getData().addListener((observableValue,
//                                                      listRegisterOUTFXES, t1) ->
//                );

        titleColumn.setRowCellFactory(listRegisterOUTFX -> new MFXTableRowCell<>(listRegisterOUTFX1 ->
                listRegisterOUTFX1.title().getValue()));

        dateColumn.setRowCellFactory(listRegisterOUTFX ->
                new MFXTableRowCell<>(listRegisterOUTFX1 ->{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    return listRegisterOUTFX1.date().getValue().format(formatter);
                }));

        valueColumn.setRowCellFactory(listRegisterOUTFX ->
                new MFXTableRowCell<>(listRegisterOUTFX1 ->"R$ " +
                        new MoneyFormatter().format(listRegisterOUTFX1.value().getValue())));

//        actionsColumn.setRowCellFactory(listRegisterOUTFX ->{
//            MFXTableRowCell<ListRegisterOUTFX,String> mfxTableRowCell = new MFXTableRowCell<>(listRegisterOUTFX1 ->"");
//
//            MFXButton saveButton = new MFXButton("Editar");
//
//            saveButton.getStyleClass().addAll("button","add-button");
//
//            saveButton.setOnAction(event -> this.openFormWindow(
//                new UpdateRegisterControllerFactory(this.registerViewModel,listRegisterOUTFX)
//            ));
//
//            MFXButton deleteButton = new MFXButton("Excluir");
//
//            deleteButton.getStyleClass().addAll("button","delete-button");
//
//            deleteButton.setOnAction(event -> {
//                ButtonType confirmDeleteButton = new ButtonType("Confirmar");
//
//
//                confirmDeleteButton.getStyleClass().addAll("button","add-button");
//
//                ButtonType cancelButton = new ButtonType("Cancelar");
//
//                cancelButton.getStyleClass().addAll("button","neutral-button");
//
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Tem certeza que deseja excluir o registro ?",
//                        confirmDeleteButton,cancelButton);
//
//                alert.getDialogPane().lookupButton(confirmDeleteButton).getStyleClass().addAll("button","add-button");
//                alert.getDialogPane().lookupButton(cancelButton).getStyleClass().addAll("button","neutral-button");
//
//                Optional<ButtonType> optionalButtonType = alert.showAndWait();
//
//                if(optionalButtonType.isEmpty()||optionalButtonType.get()==cancelButton){
//                    return;
//                }
//
//                this.registerViewModel.delete(listRegisterOUTFX.id());
//            });
//
//            HBox hBox = new HBox(20, saveButton, deleteButton);
//
//            mfxTableRowCell.setGraphic(hBox);
//
//            return mfxTableRowCell;
//        });

        actionsColumn.setRowCellFactory(new RegisterActionsRowCellFactory(this.viewModel,this.windowLoader,this.tableView));

        this.categoryColumn
                .setRowCellFactory(
                        listRegisterOUTFX ->
                                new MFXTableRowCell<>(listRegisterOUTFX1 ->
                                        listRegisterOUTFX1.categories()
                                                .stream().map(ListCategoryOUTFX::name).collect(Collectors.joining(", "))));

        this.totalText.textProperty().bind(this.viewModel.getTotal());

        this.tableView.addEventFilter(MouseEvent.MOUSE_CLICKED,new ClickTableViewEventFilter());
    }

    private void bindDataWithTable() {
        for(ListRegisterOUTFX registerOUTFX :this.viewModel.getData().getValue()){
            registerOUTFX.categories().addListener((ListChangeListener<? super ListCategoryOUTFX>) change -> {
                this.tableView.update();
                this.tableView.autosizeColumns();
            });

            registerOUTFX.title().addListener( change ->{
                this.tableView.update();
                this.tableView.autosizeColumns();
            });

            registerOUTFX.date().addListener((observableValue, date, t1) -> this.tableView.update());
            registerOUTFX.title().addListener((observableValue, string, t1) -> this.tableView.update());
            registerOUTFX.value().addListener((observableValue, string, t1) -> this.tableView.update());
        }
    }

    public void add(){
        this.windowLoader.openWindow("register-form.fxml",this.tableView.getScene().getWindow(),
                new CreateRegisterControllerFactory(this.blockId,this.viewModel));
    }
}

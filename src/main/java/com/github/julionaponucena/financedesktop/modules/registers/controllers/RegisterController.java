package com.github.julionaponucena.financedesktop.modules.registers.controllers;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.events.ClickTableViewEventFilter;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.factories.CreateRegisterControllerFactory;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.rowcellfactory.RegisterActionsRowCellFactory;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegisterController {
    private final RegisterViewModel viewModel;
    private final ListBlockRegisterOUTFX blockRegister;
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
    private Text titleLabel;

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

        this.viewModel.searchRegisters(this.blockRegister.id());

        this.tableView.itemsProperty().bind(this.viewModel.getData());



        this.tableView.autosizeColumnsOnInitialization();


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


        actionsColumn.setRowCellFactory(new RegisterActionsRowCellFactory(this.viewModel,this.windowLoader,this.tableView));

        this.categoryColumn
                .setRowCellFactory(
                        listRegisterOUTFX ->
                                new MFXTableRowCell<>(listRegisterOUTFX1 ->
                                        listRegisterOUTFX1.categories()
                                                .stream().map(ListCategoryOUTFX::name).collect(Collectors.joining(", "))));

        this.totalText.textProperty().bind(this.viewModel.getTotal());

        this.tableView.addEventFilter(MouseEvent.MOUSE_CLICKED,new ClickTableViewEventFilter());

        this.titleLabel.setText(blockRegister.title().getValue());
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
                new CreateRegisterControllerFactory(this.blockRegister.id(),this.viewModel));
    }

    public void export(){
        this.viewModel.export(this.blockRegister.id());
    }
}

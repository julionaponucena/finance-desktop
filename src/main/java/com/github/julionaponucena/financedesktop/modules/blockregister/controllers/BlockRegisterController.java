package com.github.julionaponucena.financedesktop.modules.blockregister.controllers;

import com.github.julionaponucena.financedesktop.commons.MoneyFormatter;
import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.events.ClickTableViewEventFilter;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories.CreateBlockRegisterFormControllerFactory;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.rowcellFactories.BlockRegisterActionRowCellFactory;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels.BlockRegisterViewModel;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class BlockRegisterController {
    private final BlockRegisterViewModel viewModel;

    private final RouterFX routerFX;
    private final WindowLoader windowLoader;

    @FXML
    private MFXTableView<ListBlockRegisterOUTFX> tableView;

    @FXML
    private MFXTableColumn<ListBlockRegisterOUTFX> titleColumn;

    @FXML
    private MFXTableColumn<ListBlockRegisterOUTFX> valueColumn;

    @FXML
    private MFXTableColumn<ListBlockRegisterOUTFX> actionsColumn;

    @FXML
    private MFXProgressSpinner progressSpinner;

    @FXML
    private Text totalText;

    @FXML
    private void initialize(){
        this.tableView.itemsProperty().addListener((observableValue,
                                                    listBlockRegisterOUTFXES,
                                                    t1) ->
                this.bindTitleWithTable());

        this.viewModel.getIsLoading().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                this.tableView.itemsProperty().getValue().addListener((ListChangeListener<? super ListBlockRegisterOUTFX>)
                        observable -> this.bindTitleWithTable());
            }
        });

        this.totalText.textProperty().bind(this.viewModel.getTotal());

        this.viewModel.searchCategories();

        titleColumn.setRowCellFactory(listBlockRegisterOUTFX ->
                new MFXTableRowCell<>(listBlockRegisterOUTFX1 -> listBlockRegisterOUTFX1.title().getValue()));

        valueColumn.setRowCellFactory(listBlockRegisterOUTFX ->
                new MFXTableRowCell<>(listBlockRegisterOUTFX1 -> "R$ " +
                        new MoneyFormatter().format(listBlockRegisterOUTFX1.value())));


        actionsColumn.setRowCellFactory(new BlockRegisterActionRowCellFactory(this.viewModel,this.windowLoader,this.tableView,this.routerFX));

        titleColumn.setComparator(Comparator.comparing(listBlockRegisterOUTFX -> listBlockRegisterOUTFX.title().getValue()));

        valueColumn.setComparator(Comparator.comparing(ListBlockRegisterOUTFX::value));

        this.tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, new ClickTableViewEventFilter());

       this.progressSpinner.visibleProperty().bind(this.viewModel.getIsLoading());

       this.tableView.visibleProperty().bind(this.viewModel.getIsLoading().not());

       this.tableView.itemsProperty().bind(this.viewModel.getData());
    }

    private void bindTitleWithTable() {
        for (ListBlockRegisterOUTFX listBlockRegisterOUTFX : this.viewModel.getData().getValue()) {
            listBlockRegisterOUTFX.title().addListener((observable, oldValue, newValue) ->
                    this.tableView.update());
        }
    }

    public void add(){
        this.windowLoader.openWindow("block-registers-form.fxml",
                this.tableView.getScene().getWindow(),
                new CreateBlockRegisterFormControllerFactory(this.viewModel));
    }
}

package com.github.julionaponucena.financedesktop.modules.category.controllers;

import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.commons.events.ClickTableViewEventFilter;
import com.github.julionaponucena.financedesktop.modules.category.controllers.factories.CreateCategoryControllerFactory;
import com.github.julionaponucena.financedesktop.modules.category.controllers.rowcellfactories.CategoryActionRollCellFactory;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.category.viewmodels.CategoryViewModel;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryController {
    private final CategoryViewModel viewModel;
    private final WindowLoader windowLoader;

    @FXML
    private MFXTableView<ListCategoryOUTFX> tableView;

    @FXML
    private MFXTableColumn<ListCategoryOUTFX> nameColumn;

    @FXML
    private MFXTableColumn<ListCategoryOUTFX> totalColumn;

    @FXML
    private MFXTableColumn<ListCategoryOUTFX> actionsColumn;

    @FXML
    private MFXProgressSpinner progressSpinner;

    @FXML
    private void initialize() {
        this.progressSpinner.visibleProperty().bind(this.viewModel.getIsLoading());
        this.tableView.visibleProperty().bind(this.viewModel.getIsLoading().not());
        this.tableView.itemsProperty().bind(this.viewModel.getData());

        this.viewModel.getIsLoading().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                bindDataWithTable();

                this.viewModel.getData().getValue()
                        .addListener((ListChangeListener<? super ListCategoryOUTFX>) change ->
                        bindDataWithTable());
            }
        });

        this.viewModel.searchCategories();

        this.nameColumn.setRowCellFactory(listCategoryOUTFX ->
                new MFXTableRowCell<>(listCategoryOUTFX1 -> listCategoryOUTFX1.name().getValue()));

        this.totalColumn.setRowCellFactory(listCategoryOUTFX ->
                new MFXTableRowCell<>(ListCategoryOUTFX::total));

        this.actionsColumn.setRowCellFactory(new CategoryActionRollCellFactory(this.windowLoader,this.viewModel,this.tableView));

        this.tableView.addEventFilter(MouseEvent.MOUSE_CLICKED,new ClickTableViewEventFilter());
    }

    private void bindDataWithTable() {
        for (ListCategoryOUTFX categoryOUTFX : this.viewModel.getData().getValue()){
            categoryOUTFX.name().addListener((observableValue, s, t1) ->
                    this.tableView.update());
        }
    }

    public void add(){
       this.windowLoader.openWindow("category-form.fxml",
               this.tableView.getScene().getWindow()
               ,new CreateCategoryControllerFactory(this.viewModel));
    }
}

package com.github.julionaponucena.financedesktop.modules.category.controllers.rowcellfactories;

import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.modules.category.controllers.CategoryController;
import com.github.julionaponucena.financedesktop.modules.category.controllers.factories.UpdateCategoryControllerFactory;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUTFX;
import com.github.julionaponucena.financedesktop.modules.category.viewmodels.CategoryViewModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class CategoryActionRollCellFactory implements Function<ListCategoryOUTFX, MFXTableRowCell<ListCategoryOUTFX,?>> {

    private final WindowLoader windowLoader;
    private CategoryViewModel viewModel;
    private final Node node;

    @Override
    public MFXTableRowCell<ListCategoryOUTFX, String> apply(ListCategoryOUTFX listCategoryOUTFX) {
        MFXButton saveButton = new MFXButton("Editar");

        saveButton.getStyleClass().addAll("button","add-button");

        HBox hBox = new HBox(20, saveButton);

        final MFXButton deleteButton = new MFXButton("Excluir");

        deleteButton.getStyleClass().addAll("button","delete-button");

        MFXTableRowCell<ListCategoryOUTFX,String> tableRowCell = new MFXTableRowCell<>(o -> {
            saveButton.setOnAction(event ->
                    windowLoader.openWindow("category-form.fxml",node.getScene().getWindow(), new UpdateCategoryControllerFactory(o,this.viewModel)));

            if (!shouldUseDeleteButton(o.total())) {
                hBox.getChildren().remove(deleteButton);

                return "";
            }

            deleteButton.setOnAction(event -> {
                ButtonType confirmButton = new ButtonType("Sim");

                ButtonType cancelButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir?", confirmButton, cancelButton);

                alert.getDialogPane().lookupButton(confirmButton).getStyleClass().addAll("button", "view-button");

                alert.getDialogPane().lookupButton(cancelButton).getStyleClass().addAll("button", "delete-button");

                alert.getDialogPane().getStylesheets().add(CategoryController.class.getResource("/css/buttons.css").toExternalForm());

                Optional<ButtonType> optionalButtonType = alert.showAndWait();

                if (optionalButtonType.isEmpty() || optionalButtonType.get() == cancelButton) {
                    return;
                }

                this.viewModel.delete(o.id());
            });

            if(!hBox.getChildren().contains(deleteButton)) {
                hBox.getChildren().add(deleteButton);
            }

            return "";
        });

        tableRowCell.setGraphic(hBox);

        return tableRowCell;
    }

    private  static boolean shouldUseDeleteButton(int total) {
        return total==0;
    }
}

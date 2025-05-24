package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.rowcellFactories;

import com.github.julionaponucena.financedesktop.commons.RouterFX;
import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories.UpdateBlockRegisterFormControllerFactory;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels.BlockRegisterViewModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class BlockRegisterActionRowCellFactory implements Function<ListBlockRegisterOUTFX, MFXTableRowCell<ListBlockRegisterOUTFX,?>> {
    private final BlockRegisterViewModel viewModel;
    private final WindowLoader windowLoader;
    private final Node node;
    private final RouterFX routerFX;

    @Override
    public MFXTableRowCell<ListBlockRegisterOUTFX, ?> apply(ListBlockRegisterOUTFX listBlockRegisterOUTFX) {
        MFXButton saveButton = new MFXButton("Editar");

        saveButton.getStyleClass().add("button");
        saveButton.getStyleClass().add("add-button");



        MFXButton viewButton = new MFXButton("Visualizar");

        viewButton.getStyleClass().add("button");
        viewButton.getStyleClass().add("view-button");

        MFXButton deleteButton = new MFXButton("Excluir");

        deleteButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("delete-button");

        HBox saveButtonHBox = new HBox(20,saveButton,viewButton);

        MFXTableRowCell<ListBlockRegisterOUTFX,String> tableRowCell = new MFXTableRowCell<>(listBlockRegisterOUTFX1 -> {
            saveButton.setOnAction(event -> this.windowLoader.openWindow(
                    "block-registers-form.fxml",
                    node.getScene().getWindow(),
                    new UpdateBlockRegisterFormControllerFactory(listBlockRegisterOUTFX1,this.viewModel)
                    ));

            viewButton.setOnAction(actionEvent ->
                    this.routerFX.goToRegisters(listBlockRegisterOUTFX1.id())
            );

            if(listBlockRegisterOUTFX1.containRegister()){
                saveButtonHBox.getChildren().remove(deleteButton);

                return "";
            }

            deleteButton.setOnAction(event -> {
                ButtonType confirmButtonType = new ButtonType("Sim");

                ButtonType cancelButtonType = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Tem certeza que deseja excluir?",
                        confirmButtonType,cancelButtonType);

                alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/buttons.css")).toExternalForm());

                Node confirmNode = alert.getDialogPane().lookupButton(confirmButtonType);

                confirmNode.getStyleClass().add("view-button");

                Node cancelNode = alert.getDialogPane().lookupButton(cancelButtonType);

                cancelNode.getStyleClass().add("delete-button");

                Optional<ButtonType> optionalButtonType = alert.showAndWait();

                if(optionalButtonType.isEmpty()) return;

                if(optionalButtonType.get() == confirmButtonType) {
                    this.viewModel.delete(listBlockRegisterOUTFX1.id());
                }
            });

            if(!saveButtonHBox.getChildren().contains(deleteButton)){
                saveButtonHBox.getChildren().add(deleteButton);
            }

            return "";
        });

        tableRowCell.setGraphic(saveButtonHBox);

        return tableRowCell;
    }
}

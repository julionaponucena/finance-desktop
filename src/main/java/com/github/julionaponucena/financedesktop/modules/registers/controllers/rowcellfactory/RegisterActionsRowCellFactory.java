package com.github.julionaponucena.financedesktop.modules.registers.controllers.rowcellfactory;

import com.github.julionaponucena.financedesktop.commons.WindowLoader;
import com.github.julionaponucena.financedesktop.modules.registers.controllers.factories.UpdateRegisterControllerFactory;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.RegisterViewModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class RegisterActionsRowCellFactory implements Function<ListRegisterOUTFX, MFXTableRowCell<ListRegisterOUTFX,?>> {
    private final RegisterViewModel viewModel;
    private final WindowLoader windowLoader;
    private final Node node;

    @Override
    public MFXTableRowCell<ListRegisterOUTFX, ?> apply(ListRegisterOUTFX listRegisterOUTFX) {
        MFXButton saveButton = new MFXButton("Editar");

        saveButton.getStyleClass().addAll("button","add-button");

        MFXButton deleteButton = new MFXButton("Excluir");

        deleteButton.getStyleClass().addAll("button","delete-button");

        MFXTableRowCell<ListRegisterOUTFX,String> mfxTableRowCell = new MFXTableRowCell<>(listRegisterOUTFX1 ->{
            saveButton.setOnAction(event -> this.windowLoader.openWindow("register-form.fxml",node.getScene().getWindow(),
                    new UpdateRegisterControllerFactory(this.viewModel,listRegisterOUTFX1)));

            deleteButton.setOnAction(event -> {
                ButtonType confirmDeleteButton = new ButtonType("Confirmar");

                ButtonType cancelButton = new ButtonType("Cancelar");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Tem certeza que deseja excluir o registro ?",
                        confirmDeleteButton,cancelButton);


                alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/buttons.css").toExternalForm());

                alert.getDialogPane().lookupButton(confirmDeleteButton).getStyleClass().addAll("button","add-button");
                alert.getDialogPane().lookupButton(cancelButton).getStyleClass().addAll("button","neutral-button");

                Optional<ButtonType> optionalButtonType = alert.showAndWait();

                if(optionalButtonType.isEmpty()||optionalButtonType.get()==cancelButton){
                    return;
                }

                this.viewModel.delete(listRegisterOUTFX1.id());
            });

            return "";
        });

        HBox hBox = new HBox(20, saveButton, deleteButton);

        mfxTableRowCell.setGraphic(hBox);

        return mfxTableRowCell;
    }
}

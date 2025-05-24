package com.github.julionaponucena.financedesktop.modules.index;

import com.github.julionaponucena.financedesktop.commons.FileManager;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexController {
    @FXML
    private MFXButton openButton;

    private final FileManager fileManager;

    public void create(){
        this.fileManager.create(this.openButton.getScene().getWindow());
    }

    public void open(){
      this.fileManager.open(this.openButton.getScene().getWindow());
    }
}

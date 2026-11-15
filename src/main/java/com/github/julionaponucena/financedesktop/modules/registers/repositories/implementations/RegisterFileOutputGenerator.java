package com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterOutputGenerator;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@RequiredArgsConstructor
public class RegisterFileOutputGenerator implements RegisterOutputGenerator {

    private final Window window;

    @Override
    public OutputStream generate() throws InternalServerException {
        final var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File file = fileChooser.showSaveDialog(window);

        if (file == null) {
            throw new InternalServerException(new FileNotFoundException("No file selected"));
        }

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getParentFile(), file.getName() + ".pdf");
        }

        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new InternalServerException(e);
        }
    }
}

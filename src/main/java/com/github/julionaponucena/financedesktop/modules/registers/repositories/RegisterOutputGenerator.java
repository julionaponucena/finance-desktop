package com.github.julionaponucena.financedesktop.modules.registers.repositories;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;

import java.io.OutputStream;

public interface RegisterOutputGenerator {
    OutputStream generate() throws InternalServerException;
}

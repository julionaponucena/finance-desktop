package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import java.time.LocalDate;

public record ExportRegisterInput(int idBlock,String name, LocalDate date, String value) {
}

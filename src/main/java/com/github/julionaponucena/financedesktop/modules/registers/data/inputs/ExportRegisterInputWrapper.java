package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import java.util.List;

public record ExportRegisterInputWrapper(List<ExportRegisterInput> inputs, int blockId ,String totalValue) {
}

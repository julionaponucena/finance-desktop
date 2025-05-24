package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;

public record CategoryPersistenceInput(int id, String name,
                                       CategoryPersistenceState persistenceState,CategoryPersistenceState currentState) {
}

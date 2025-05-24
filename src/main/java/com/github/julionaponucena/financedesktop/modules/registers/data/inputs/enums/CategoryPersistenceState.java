package com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums;

public enum CategoryPersistenceState {
    CREATED,
    DELETED,
    PERSISTED;

    public CategoryPersistenceState createDeleteState() {
        if (this == CategoryPersistenceState.CREATED) {
            return PERSISTED;
        }
        return DELETED;
    }

    public CategoryPersistenceState createCreateState() {
        if (this == CategoryPersistenceState.DELETED) {
            return PERSISTED;
        }
        return CREATED;
    }
}

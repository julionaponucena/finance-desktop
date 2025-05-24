package com.github.julionaponucena.financedesktop.modules.registers.data.inputs;

import com.github.julionaponucena.financedesktop.commons.listmanager.Persistable;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode(of = "id")
public class CategoryPersistenceInputFX implements Persistable {
    private final int id;
    private final String name;
    private CategoryPersistenceState persistenceState;
    private CategoryPersistenceState viewState;

    @Override
    public void persistDeleteState() {
        this.viewState = CategoryPersistenceState.DELETED;
        this.persistenceState = this.persistenceState.createDeleteState();
    }

    @Override
    public int getItemIdentifier() {
        return this.id;
    }

    @Override
    public void persistCreateState() {
        this.viewState= CategoryPersistenceState.CREATED;

        this.persistenceState= this.persistenceState.createCreateState();
    }
}

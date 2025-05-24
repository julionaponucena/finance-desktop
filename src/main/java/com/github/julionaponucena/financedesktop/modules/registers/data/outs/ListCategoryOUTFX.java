package com.github.julionaponucena.financedesktop.modules.registers.data.outs;

import com.github.julionaponucena.financedesktop.commons.listmanager.Persistable;
import com.github.julionaponucena.financedesktop.commons.listmanager.Selectable;

import java.util.Set;

public record ListCategoryOUTFX(int id, String name) implements Selectable {
    @Override
    public boolean isSelected(Persistable persistable) {
        return persistable.getItemIdentifier() == id;
    }

    @Override
    public boolean isSelected(Set<Integer> identifiers) {
        return identifiers.contains(id);
    }
}

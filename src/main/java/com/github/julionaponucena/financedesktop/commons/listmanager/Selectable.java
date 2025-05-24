package com.github.julionaponucena.financedesktop.commons.listmanager;

import java.util.Set;

public interface Selectable {
    boolean isSelected(Persistable persistable);
    boolean isSelected(Set<Integer> identifiers);
}

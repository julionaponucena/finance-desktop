package com.github.julionaponucena.financedesktop.commons.listmanager;

public interface PersistenceConverter<I,O> {
    I convert(O o);
}

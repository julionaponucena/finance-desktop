package com.github.julionaponucena.financedesktop.commons.listmanager;

public interface Persistable {
    void persistCreateState();
    void persistDeleteState();
    int getItemIdentifier();
}

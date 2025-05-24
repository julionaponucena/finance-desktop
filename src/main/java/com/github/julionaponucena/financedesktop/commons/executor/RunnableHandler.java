package com.github.julionaponucena.financedesktop.commons.executor;

import com.github.julionaponucena.financedesktop.commons.exceptions.ApplicationException;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;

import java.io.IOException;

@FunctionalInterface
public interface RunnableHandler {

    void run()throws ApplicationException, InternalServerException, IOException;
}

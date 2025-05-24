package com.github.julionaponucena.financedesktop.commons.executor;

public interface JobExecutor {
    void execute(RunnableHandler runnableHandler, ExceptionHandler handler);

    void execute(Runnable runnable);
}

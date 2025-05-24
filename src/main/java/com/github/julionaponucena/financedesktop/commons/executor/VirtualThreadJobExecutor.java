package com.github.julionaponucena.financedesktop.commons.executor;

public class VirtualThreadJobExecutor implements JobExecutor {

    @Override
    public void execute(RunnableHandler runnableHandler, ExceptionHandler handler) {
        Thread.startVirtualThread(() -> {
            try {
                runnableHandler.run();
            }catch (Throwable e) {
                handler.execute(e);
            }
        });
    }

    @Override
    public void execute(Runnable runnable) {
        Thread.startVirtualThread(runnable);
    }
}

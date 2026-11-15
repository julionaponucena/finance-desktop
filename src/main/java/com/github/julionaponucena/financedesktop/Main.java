package com.github.julionaponucena.financedesktop;

/**
 * Entry point que não estende {@link javafx.application.Application}.
 * Necessário porque o launcher do JDK bloqueia classes main que estendem
 * Application quando o JavaFX não está no module path (execução via IDE/classpath).
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        FinanceDesktop.main(args);
    }
}

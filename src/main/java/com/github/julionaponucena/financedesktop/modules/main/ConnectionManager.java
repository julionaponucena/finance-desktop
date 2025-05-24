package com.github.julionaponucena.financedesktop.modules.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionManager {
    private static ConnectionManager INSTANCE;

    @Getter
    private Connection connection;

    public void createConnection(String path){
        try {
            String url ="jdbc:sqlite:" + path;

            this.connection = DriverManager.getConnection(url);

            Flyway flyway = Flyway.configure()
                    .dataSource(url, null, null)
                    .locations("classpath:migrations")
                    .load();
            flyway.migrate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static ConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionManager();
        }

        return INSTANCE;
    }
}

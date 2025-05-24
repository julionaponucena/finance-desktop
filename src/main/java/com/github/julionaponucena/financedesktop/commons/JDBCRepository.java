package com.github.julionaponucena.financedesktop.commons;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.modules.main.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JDBCRepository {

    private final String sql;
    private final Object[] parameters;
    private ResultSet resultSet;
    private int key;
    private PreparedStatement statement;

    public JDBCRepository(String sql, Object... parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public JDBCRepository(StringBuilder sql,Object... params) {
        this.sql = sql.toString();
        this.parameters = params;
    }

    public void execute()throws InternalServerException {
        this.persist();
    }

    public int executeWithReturn() throws InternalServerException {
        this.persist();

        return this.key;
    }

    private void persist()throws InternalServerException {
        try (PreparedStatement statement = this.prepareStatement()){
            int index=1;

            for(Object parameter: parameters){
                statement.setObject(index,parameter);

                index++;
            }

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()){
                key=resultSet.getInt(1);
            }
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public void executeQuery() throws InternalServerException {


        this.statement = this.prepareStatement();
        try{
            statement = this.prepareStatement();

            int index=1;

            for(Object parameter: parameters){
                statement.setObject(index,parameter);
                index++;
            }

            this.resultSet = statement.executeQuery();
        }
        catch (SQLException exception){
            try {
                statement.close();
            }catch (SQLException exception1){
                throw new InternalServerException(exception1);
            }
            throw new InternalServerException(exception);
        }
    }

    private PreparedStatement prepareStatement()throws InternalServerException {
        Connection connection =ConnectionManager.getInstance().getConnection();

        try{
            return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public String getString(String label)throws InternalServerException {
        try {
            return this.resultSet.getString(label);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public int getInt(String label)throws InternalServerException {
        try{
            return this.resultSet.getInt(label);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public LocalDate getLocalDate(String label)throws InternalServerException {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return LocalDate.parse(this.resultSet.getString(label),formatter);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public BigDecimal getBigDecimal(String label)throws InternalServerException {
        try{
            return this.resultSet.getBigDecimal(label);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public boolean getBoolean(String label)throws InternalServerException {
        try{
            return this.resultSet.getBoolean(label);
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }

    public boolean next() throws InternalServerException {
        try {
            if(!this.resultSet.next()){
                this.statement.close();

                return false;
            }

            return true;
        }catch (SQLException exception){
            throw new InternalServerException(exception);
        }
    }
}

package com.github.julionaponucena.financedesktop.modules.registers.repositories.implementations;

import com.github.julionaponucena.financedesktop.commons.JDBCRepository;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Category;
import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JDBCRegisterRepository implements RegisterRepository {
    @Override
    public List<Register> findAll() {
        return List.of();
    }

    @Override
    public List<Register> findAllByBlockId(int blockId) throws InternalServerException {
        JDBCRepository jdbcRepository = getJdbcRepository(blockId);

        List<Register> registers = new ArrayList<>();
        Register register =null;

        List<Category> categories = null;

        BigDecimal dividor =new BigDecimal(100);

        while (jdbcRepository.next()) {
            int id = jdbcRepository.getInt("id_register");
            if(register==null || register.getId()!=id) {
                categories = new ArrayList<>();

                BigDecimal valueCents =jdbcRepository.getBigDecimal("value_cents");

                register = new Register(
                        id,
                        jdbcRepository.getString("title"),
                        jdbcRepository.getLocalDate("date"),
                        categories,
                        valueCents.divide(dividor)
                );

                registers.add(register);
            }

            int categoryId = jdbcRepository.getInt("id_category");

            if(categoryId != 0){
                Category category = new Category(
                        categoryId,
                        jdbcRepository.getString("name_category")
                );

                categories.add(category);
            }
        }

        return registers;
    }

    private static JDBCRepository getJdbcRepository(int blockId) throws InternalServerException {
        String sql = "SELECT registers.id_register AS id_register,title,date ,value_cents, " +
                "categories.id_category AS id_category, categories.name AS " +
                "name_category FROM registers " +
                "LEFT JOIN registers_categories rc ON rc.id_register = registers.id_register " +
                "LEFT JOIN categories ON categories.id_category = rc.id_category WHERE id_block_register = ? ";

        JDBCRepository jdbcRepository = new JDBCRepository(sql, blockId);

        jdbcRepository.executeQuery();
        return jdbcRepository;
    }

    @Override
    public Register create(Register register) throws InternalServerException {
        String sql="INSERT INTO registers (title,date,value_cents,id_block_register) VALUES (?,?,?,?)";

        int finalValue = getFinalValue(register);

        JDBCRepository jdbcRepository = new JDBCRepository(sql, register.getTitle(), register.getDate(),
                finalValue,register.getBlockRegister().getId() );

        int id=jdbcRepository.executeWithReturn();

        register.setId(id);

        return register;
    }

    @Override
    public void addCategories(Set<Integer> categoriesId,int registerId) throws InternalServerException {
        if(categoriesId.isEmpty()){
            return;
        }

        final int total = categoriesId.size()*2;

        Object[] params = new Object[total];

        StringBuilder sql = new StringBuilder("INSERT INTO registers_categories (id_register,id_category) VALUES ");

        int i = 0;

        for(int categorieId : categoriesId) {
            sql.append("(?,?)");

            params[i++] = registerId;
            params[i++] = categorieId;

            if(i < total){
                sql.append(",");
            }
        }

        JDBCRepository jdbcRepository = new JDBCRepository(sql, params);

        jdbcRepository.execute();
    }

    @Override
    public void removeCategories(Set<Integer> categoriesId,int registerId) throws InternalServerException {
        if(categoriesId.isEmpty()){
            return;
        }

        final int total = categoriesId.size()+1;

        Object[] params = new Object[total];

        StringBuilder sql = new StringBuilder("DELETE FROM registers_categories WHERE id_register = ? AND id_category IN (");

        int i = 0;

        params[i++] =registerId;

        for(int categorieId : categoriesId) {
            sql.append("?");

            params[i++] = categorieId;

            if(i < total){
                sql.append(",");
            }
        }

        sql.append(")");

        JDBCRepository jdbcRepository = new JDBCRepository(sql, params);

        jdbcRepository.execute();
    }

    @Override
    public void removeCategories(int registerId) throws InternalServerException {
        String sql = "DELETE FROM registers_categories WHERE id_register = ?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql, registerId);

        jdbcRepository.execute();
    }

    @Override
    public void delete(int id) throws InternalServerException {
        String sql = "DELETE FROM registers WHERE id_register = ?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql, id);

        jdbcRepository.execute();
    }

    @Override
    public boolean notExists(int id) throws InternalServerException {
        String sql = "SELECT NOT EXISTS(SELECT 1 FROM registers WHERE id_register = ?) AS not_exists";

        JDBCRepository jdbcRepository = new JDBCRepository(sql, id);

        jdbcRepository.executeQuery();

        return jdbcRepository.getBoolean("not_exists");
    }

    @Override
    public void update(Register register) throws InternalServerException {
        String sql ="UPDATE registers SET title = ?,date = ?,value_cents = ? WHERE id_register = ?";

        int finalValue = getFinalValue(register);

        JDBCRepository jdbcRepository = new JDBCRepository(sql, register.getTitle(), register.getDate(),finalValue,register.getId());

        jdbcRepository.execute();
    }

    private static int getFinalValue(Register register) {
        return register.getValue().multiply(new BigDecimal(100)).intValue();
    }
}

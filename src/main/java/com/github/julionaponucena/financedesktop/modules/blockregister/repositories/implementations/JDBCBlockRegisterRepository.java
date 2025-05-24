package com.github.julionaponucena.financedesktop.modules.blockregister.repositories.implementations;

import com.github.julionaponucena.financedesktop.commons.JDBCRepository;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.BlockRegister;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;

import java.util.ArrayList;
import java.util.List;

public class JDBCBlockRegisterRepository implements BlockRegisterRepository {
    @Override
    public List<BlockRegisterProj> list()throws InternalServerException {
        String sql = "SELECT br.id_block_register, br.title, " +
                "COALESCE(" +
                "(SELECT SUM(r.value_cents) " +
                "FROM registers r " +
                "WHERE r.id_block_register = br.id_block_register " +
                ")," +
                "0) /100 AS value, CASE WHEN EXISTS " +
                "(SELECT 1 FROM registers WHERE registers.id_block_register=br.id_block_register) " +
                " THEN 1 ELSE 0 END AS contain_registers " +
                "FROM block_registers br;";

        return getBlockRegisterProjs(sql);
    }

    private static List<BlockRegisterProj> getBlockRegisterProjs(String sql) throws InternalServerException {
        JDBCRepository jdbcRepository = new JDBCRepository(sql);

        jdbcRepository.executeQuery();

        List<BlockRegisterProj> blockRegisterProjs = new ArrayList<>();

        while(jdbcRepository.next()){
            BlockRegisterProj blockRegisterProj = new BlockRegisterProj(
                    jdbcRepository.getInt("id_block_register"),
                    jdbcRepository.getString("title"),
                    jdbcRepository.getBigDecimal("value"),
                    jdbcRepository.getBoolean("contain_registers")
            );

            blockRegisterProjs.add(blockRegisterProj);
        }
        return blockRegisterProjs;
    }

    @Override
    public BlockRegister create(String title) throws InternalServerException {
        String sql = "INSERT INTO block_registers (title) VALUES (?)";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,title);

        int id = jdbcRepository.executeWithReturn();

        return new BlockRegister(id,title);
    }

    @Override
    public boolean notExists(int id) throws InternalServerException {
        String sql="SELECT NOT EXISTS(SELECT 1 FROM block_registers WHERE id_block_register = ?) AS not_exists";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.executeQuery();

        jdbcRepository.next();

        return jdbcRepository.getBoolean("not_exists");
    }

    @Override
    public void update(BlockRegister blockRegister) throws InternalServerException {
        String sql = "UPDATE block_registers SET title = ? WHERE id_block_register = ?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,blockRegister.getTitle(),blockRegister.getId());

        jdbcRepository.execute();
    }

    @Override
    public void delete(int id) throws InternalServerException {
        String sql = "DELETE FROM block_registers WHERE id_block_register = ?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.execute();
    }

    @Override
    public boolean existsRegister(int id) throws InternalServerException {
        String sql ="SELECT EXISTS(SELECT 1 FROM registers WHERE id_block_register = ?) AS \"exists\"";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.executeQuery();

        jdbcRepository.next();

        return jdbcRepository.getBoolean("exists");
    }
}

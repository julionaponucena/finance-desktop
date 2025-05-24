package com.github.julionaponucena.financedesktop.modules.category.repository.implementations;

import com.github.julionaponucena.financedesktop.commons.JDBCRepository;
import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.Category;
import com.github.julionaponucena.financedesktop.modules.category.data.out.ListCategoryOUT;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JDBCCategoryRepository implements CategoryRepository {
    @Override
    public List<Category> findAll() throws InternalServerException {
        String sql = "SELECT id_category, name FROM categories";

        JDBCRepository jdbcRepository = new JDBCRepository(sql);

        return getCategories(jdbcRepository);
    }

    @Override
    public List<Category> findAll(Set<Integer> ids) throws InternalServerException {
        StringBuilder sql = new StringBuilder("SELECT id_category, name FROM categories WHERE id_category in (");

        Object[] params = new Object[ids.size()];

        int i = 0;

        for (int id : ids) {
            sql.append("?");

            if (i != params.length - 1) {
                sql.append(",");
            }

            params[i++] = id;
        }

        sql.append(")");

        JDBCRepository jdbcRepository = new JDBCRepository(sql, params);

        return getCategories(jdbcRepository);
    }

    private List<Category> getCategories(JDBCRepository jdbcRepository) throws InternalServerException {
        jdbcRepository.executeQuery();

        List<Category> categories = new ArrayList<>();

        while (jdbcRepository.next()) {
            Category category = new Category(
                    jdbcRepository.getInt("id_category"),
                    jdbcRepository.getString("name")
            );

            categories.add(category);
        }

        return categories;
    }

    @Override
    public List<ListCategoryOUT> findAllTotalProj() throws InternalServerException {
        String sql = "SELECT id_category, name, (SELECT COUNT(rc.id_category) FROM registers_categories rc WHERE rc.id_category=categories.id_category)" +
                " AS total FROM categories";

        JDBCRepository jdbcRepository = new JDBCRepository(sql);

        jdbcRepository.executeQuery();

        List<ListCategoryOUT> categories = new ArrayList<>();

        while (jdbcRepository.next()) {
            ListCategoryOUT categoryOUT = new ListCategoryOUT(
              jdbcRepository.getInt("id_category"),
              jdbcRepository.getString("name"),
              jdbcRepository.getInt("total")
            );

            categories.add(categoryOUT);
        }

        return categories;
    }

    @Override
    public boolean nameExists(String name) throws InternalServerException {
        String sql = "SELECT EXISTS (SELECT 1 FROM categories WHERE name = ?) AS name_exists";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,name);

        jdbcRepository.executeQuery();

        return jdbcRepository.getBoolean("name_exists");
    }

    @Override
    public boolean nameExists(int id, String name) throws InternalServerException {
        String sql = "SELECT EXISTS (SELECT 1 FROM categories WHERE id_category != ? AND name=?) AS name_exists";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id,name);

        jdbcRepository.executeQuery();

        return jdbcRepository.getBoolean("name_exists");
    }

    @Override
    public boolean notExists(int id) throws InternalServerException {
        String sql = "SELECT NOT EXISTS (SELECT 1 FROM categories WHERE id_category = ?) as not_exists";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.executeQuery();

        return jdbcRepository.getBoolean("not_exists");
    }

    @Override
    public boolean containsRelation(int id) throws InternalServerException {
        String sql = "SELECT EXISTS (SELECT 1 FROM registers_categories WHERE id_category = ?) as contains_relation";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.executeQuery();

        return jdbcRepository.getBoolean("contains_relation");
    }

    @Override
    public int create(String name) throws InternalServerException {
        String sql = "INSERT INTO categories (name) VALUES (?)";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,name);

        return jdbcRepository.executeWithReturn();
    }

    @Override
    public void update(Category category) throws InternalServerException {
        String sql = "UPDATE categories SET name=? WHERE id_category=?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,category.getName(),category.getId());

        jdbcRepository.execute();
    }

    @Override
    public void delete(int id) throws InternalServerException {
        String sql = "DELETE FROM categories WHERE id_category = ?";

        JDBCRepository jdbcRepository = new JDBCRepository(sql,id);

        jdbcRepository.execute();
    }
}

package org.yearup.data;

import org.yearup.models.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao
{
    List<Category> getAllCategories();
    Category getProductsByCatId(int categoryId);
    Category getById(int categoryId);
    Category create(Category category);
    void update(int categoryId, Category category) throws SQLException;
    void delete(int categoryId) throws  SQLException;
}

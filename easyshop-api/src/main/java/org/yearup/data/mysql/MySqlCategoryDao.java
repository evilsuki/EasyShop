package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    private final ProductDao productDao;

    public MySqlCategoryDao(DataSource dataSource, ProductDao productDao)
    {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List <Category> categories = new ArrayList<>();

        String sql = """
                SELECT *
                FROM categories;
                """;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        )
        {
            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                Category category = mapRow(row);
                categories.add(category);
            }
        }
        catch(SQLException e)
        {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
        return categories;
    }

    @Override
    public Category getProductsByCatId(int categoryId)
    {
        Category category;
        List<Product> products = productDao.listByCategoryId(categoryId);

        try
        {
            category = new Category();
            category.setProducts(products);

            return category;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = """
                SELECT *
                FROM categories
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql = """
                INSERT INTO categories (name, description)
                VALUES (?, ?);
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            try(ResultSet key = statement.getGeneratedKeys())
            {
                if(key.next())
                {
                    int categoryID = key.getInt(1);
                    return getById(categoryID);
                }
            }
            catch(Exception ignored) {}
        }
        catch (SQLException e)
        {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) throws SQLException
    {
        // update category
        String sql = """
                UPDATE categories
                SET name = ?
                  , description = ?
                WHERE category_id = ?;
                """;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        )
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

    @Override
    public void delete(int categoryId) throws SQLException
    {
        // delete category
        String sql = """
                DELETE FROM categories
                WHERE category_id = ?;
                """;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        )
        {
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
    }

}

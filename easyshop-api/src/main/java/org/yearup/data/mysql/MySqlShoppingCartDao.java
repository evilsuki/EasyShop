package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.*;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = """
                SELECT p.*
                     , s.quantity
                FROM products AS p
                JOIN shopping_cart AS s
                	ON s.product_id = p.product_id
                JOIN users AS u
                	ON s.user_id = u.user_id
                WHERE u.user_id = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();

            ShoppingCart cart = new ShoppingCart(); // Create a new instance of ShoppingCart

            while (row.next())
            {
                int productId = row.getInt("product_id");
                String name = row.getString("name");
                BigDecimal price = row.getBigDecimal("price");
                int categoryId = row.getInt("category_id");
                String description = row.getString("description");
                String color = row.getString("color");
                int stock = row.getInt("stock");
                String imageUrl = row.getString("image_url");
                boolean isFeatured = row.getBoolean("featured");
                int quantity = row.getInt("quantity");


                // Create a new Product object using the retrieved data
                Product product = new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
                ShoppingCartItem cartItem = new ShoppingCartItem(product, quantity);

                // Add the product and its quantity to the shopping cart
                cart.add(cartItem);
            }

            return cart;
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ShoppingCart addItem(int productId, int userId)
    {
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES  (?, ?, ?);
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, 1);

            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public void updateItem(int quantity, int userId, int productId)
    {
        String sql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ?
                   AND product_id = ?;
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void removeItem(int productId, int userId)
    {
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?
                   AND product_id = ?;
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?;
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);

            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}

package org.yearup.data.mysql;


import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao
{

    public MySqlOrderDao(DataSource dataSource)
    {
        super(dataSource);
    }


    @Override
    public Order create(Order order, int userId)
    {
        // add coding
        String sql = "INSERT INTO orders (order_id, user_id, date, address, city, state, zip)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";

        try(Connection connection = dataSource.getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getOrderId());
            ps.setInt(2, order.getUserId());
            ps.setDate(3, order.getDate());
            ps.setString(4, order.getAddress());
            ps.setString(5, order.getCity());
            ps.setString(6, order.getState());
            ps.setString(7, order.getZip());

            ps.executeUpdate();

            return order;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}

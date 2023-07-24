package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("orders")
@CrossOrigin
public class OrdersController
{
    private final ShoppingCartDao shoppingCartDao;
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public OrdersController(ShoppingCartDao shoppingCartDao, OrderDao orderDao, UserDao userDao, ProfileDao profileDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }


    @PostMapping
    public Order create(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Profile userProfile = profileDao.getProfile(userId);

            LocalDate localDate = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            // create order with current user information
            Order order = new Order();

            // default to today date
            order.setDate(sqlDate);
            order.setUserId(userId);
            order.setAddress(userProfile.getAddress());
            order.setCity(userProfile.getCity());
            order.setState(userProfile.getState());
            order.setZip(userProfile.getZip());

            // get the shopping cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            Map<Integer, ShoppingCartItem> cartItems = cart.getItems();

            // loop through each item - and create an orderLineItem
            List<OrderLineItem> lineItems = new ArrayList<>();

            for (ShoppingCartItem cartItem : cartItems.values())
            {
                OrderLineItem lineItem = new OrderLineItem();
                lineItem.setProduct(cartItem.getProduct());
                lineItem.setQuantity(cartItem.getQuantity());
                lineItems.add(lineItem);
            }
            // add the lineItem to the order
            order.setLineItems(lineItems);

            Order createOrder = orderDao.create(order, userId);

            shoppingCartDao.clearCart(userId);

            return createOrder;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }
}

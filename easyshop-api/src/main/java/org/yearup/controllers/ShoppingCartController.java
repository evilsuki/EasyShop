package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }



    // each method in this controller requires a Principal object as a parameter
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping(path = "/products/{productId}")
    public ShoppingCart addItemToCart(@PathVariable int productId, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            if(cart.contains(productId))
            {
                // get the current quantity and add 1 to it
                // then update the cart
                ShoppingCartItem item = cart.get(productId);
                int currentQuantity = item.getQuantity();
                int newQuantity = currentQuantity + 1;

                shoppingCartDao.updateItem(newQuantity, userId, productId);
            }
            else
            {
                shoppingCartDao.addItem(productId, userId);
            }

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping(path = "/products/{productId}")
    public ShoppingCart updateItemInCart(@PathVariable int productId,
                                 @RequestParam int quantity,
                                 Principal principal)
    {
        try
        {

            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.updateItem(productId, userId, quantity);

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping(path = "/products/{productId}")
    public ShoppingCart removeItemFromCart(@PathVariable int productId, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.removeItem(productId, userId);

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }

    @DeleteMapping()
    public ShoppingCart clearCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);

            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }
}

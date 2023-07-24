package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@CrossOrigin
@RequestMapping("categories")
@RestController
public class CategoriesController
{
    private final CategoryDao categoryDao;
    private final ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    // add the appropriate annotation for a get action
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        // find and return all categories
        try
        {
            return categoryDao.getAllCategories();
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add the appropriate annotation for a get action
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        Category category;

        // get the category by id
        try
        {
            category = categoryDao.getById(id);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }

        if (category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsByCatId(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        try
        {
            var category = categoryDao.getProductsByCatId(categoryId);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That category does not exist.");

            return productDao.search(categoryId, null, null, null);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        try
        {
            return categoryDao.create(category);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id0*01*8*
        try
        {
            var currentCategory = categoryDao.getById(id);

            if (currentCategory == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That category does not exist.");

            categoryDao.update(id, category);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
        try
        {
            var currentCategory = categoryDao.getById(id);

            if (currentCategory == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That category does not exist.");

            categoryDao.delete(id);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}

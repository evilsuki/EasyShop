package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Category
{
    private int categoryId;
    private String name;
    private String description;
    private List<Product> products;

    public Category()
    {
    }

    public Category(int categoryId, String name, String description, List<Product> products)
    {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.products = products;
    }


    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @JsonIgnore
    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }
}

package org.yearup.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    private int orderId;
    private int userId;
    private Date date;
    private String address;
    private String city;
    private String state;
    private String zip;
    private List<OrderLineItem> lineItems;

    public Order()
    {
        lineItems = new ArrayList<>();
    }

    public Order(int orderId, int userId, Date date, String address, String city, String state, String zip, List<OrderLineItem> lineItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.lineItems = lineItems != null ? lineItems : new ArrayList<>();
    }


    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public BigDecimal getShippingAmount()
    {
        int numberOfLineItems = lineItems.size();
        BigDecimal baseShippingFee = BigDecimal.valueOf(5.99);  // Base shipping fee
        BigDecimal perItemFee = BigDecimal.valueOf(0.50);  // Shipping fee per item

        // Calculate the shipping amount based on the number of line items
        BigDecimal numberOfItems = new BigDecimal(numberOfLineItems);
        BigDecimal itemFee = perItemFee.multiply(numberOfItems);

        return baseShippingFee.add(itemFee);
    }

    public List<OrderLineItem> getLineItems()
    {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems)
    {
        this.lineItems = lineItems;
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderLineItem lineItem : lineItems)
        {
            BigDecimal lineTotal = lineItem.getLineTotal();
            total = total.add(lineTotal);
        }

        return total.add(getShippingAmount());
    }
}

package org.yearup.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_line_items")
public class OrderLineItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;

    @Column(name = "order_id")//this links each line tem back to its parent order, this tells me which prod belongs to which order
    private int orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "sales_price")//this takes the price at the time of purchase, locks price in when user pays.
    private double salesPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")//this records when discount was used during purchase
    private double discount;

    public int getOrderLineItemId() { return orderLineItemId; }
    public void setOrderLineItemId(int orderLineItemId) { this.orderLineItemId = orderLineItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public double getSalesPrice() { return salesPrice; }
    public void setSalesPrice(double salesPrice) { this.salesPrice = salesPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;

import java.util.ArrayList;

/**
 *
 * @author jiaenxu; Editor: zihuawu
 */
public class Order {
    private String id;


    private Customer customer;
    private ArrayList<OrderItem> orderItems;
    private SalesProfile salesPerson;  // the salesperson who created this order

    public Order(String id, Customer c) {
        this.id = id;
        this.customer = c;
        this.orderItems = new ArrayList<>();
    }
    
    public String getId(){
        return id;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public SalesProfile getSalesProfile() {
        return salesPerson;
    }

    public void setSalesProfile(SalesProfile sp) {
        this.salesPerson = sp;

   
    }
}




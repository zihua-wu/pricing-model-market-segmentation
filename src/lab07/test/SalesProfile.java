/*
 * SalesProfile class — represents a salesperson (employee)
 * ---------------------------------------------------------
 * Inherits from Person (gets 'name' field and methods)
 * Adds:
 *   - id        → unique employee ID
 *   - orders    → list of orders handled by this salesperson
 *
 * When a new order is assigned to a salesperson,
 * it should be added through addOrder().
 *
 * Author: Jiaen Xu; Editor: zihuaWu
 */
package lab07.test;

import java.util.ArrayList;

public class SalesProfile {
    private String id;
    private String name;
    private ArrayList<Order> orders;

    public SalesProfile(String id, String name) {
        this.id = id;
        this.name = name;
        this.orders = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    
}


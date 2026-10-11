/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;

import java.util.ArrayList;

/**
 *
 * @author jiaenxu;  Editor: zihuawu
 */
public class Customer extends Person {
    private ArrayList<Order> orders;
    
    private int age;
    private String region;
    private String incomeLevel;
    
    private Market market;
    private Channel channel;
    
     public Customer(String name, String id) {
        super(name, id);
        this.orders = new ArrayList<>();
    }

    public Customer(String name,String id, int age, String region, String incomeLevel) {
        super(name,id);
        this.orders = new ArrayList<>();
        this.age = age;
        this.region = region;
        this.incomeLevel = incomeLevel;        
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    public int getAge() {
        return age;
    }

    public String getRegion() {
        return region;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    // Market / Channel getter/setter
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

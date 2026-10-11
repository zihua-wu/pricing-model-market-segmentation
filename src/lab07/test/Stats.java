/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;

/**
 *
 * @author wawawu
 */
public class Stats {
    private int orderCount;
    private double revenue;
    
    public void addOrder(double r){
        orderCount++;
        revenue += r;
    }
    
    public int getOrderCount(){
        return orderCount;
    }
    
    public double getRevenue(){
        return revenue;
    }
    
}

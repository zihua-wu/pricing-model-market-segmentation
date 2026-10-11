/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;
import java.util.ArrayList;
/**
 *
 * @author wawawu
 */
public class MarketDirectory {
    private ArrayList<Market> markets = new ArrayList<>();
    
    public Market addMarket(String name){
        Market m = new Market(name);
        markets.add(m);
        return m;
    }
    
    public ArrayList<Market> getMarkets(){
        return markets;
    }
}

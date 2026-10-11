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
public class Market {
    private String name;
    private ArrayList<Channel> channels;
    
    public Market(String name){
        this.name = name;
        this.channels = new ArrayList<>();
    }
    
    public void addChannel(Channel c){
        if (channels.size() < 2){
            channels.add(c);
        }
    }
    
    public String getName(){return name;}
    
    public ArrayList<Channel> getChannels(){
        return channels;
    }
}

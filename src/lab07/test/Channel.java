/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;

/**
 *
 * @author wawawu
 */
public class Channel {
    private String name;
    private String type;
    
    public Channel(String name, String type){
        this.name = name;
        this.type = type;
    }
    
    public String getName(){return name;}
    public String getType(){return type;}
}

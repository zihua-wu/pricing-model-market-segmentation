/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab07.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author jiaenxu; editor: zihuawu
 */
public class EmployeeDirectory {
    private List<SalesProfile> salesProfiles = new ArrayList<>();
    
    public void addSalesProfile(SalesProfile sp){
        salesProfiles.add(sp);
    }    
    public List<SalesProfile>getSalesProfiles(){
        return salesProfiles;
    }
    public SalesProfile getRandomSalesProfile(){
        Random r = new Random();
        return salesProfiles.get(r.nextInt(salesProfiles.size()));
    }
}

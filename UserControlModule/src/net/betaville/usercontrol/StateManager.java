/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol;

import edu.poly.bxmc.betaville.model.City;

/**
 *
 * @author Skye Book
 */
@org.openide.util.lookup.ServiceProvider(service=StateManager.class)
public class StateManager {
    
    private City city;
    
    public StateManager(){
    }
    
    public City getCity(){
	return city;
    }
    
    public void setCity(City city){
	this.city = city;
    }
}

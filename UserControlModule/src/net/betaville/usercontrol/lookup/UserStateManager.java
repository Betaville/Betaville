/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol.lookup;

import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.Design;

/**
 *
 * @author Skye Book
 */
public class UserStateManager {
    private Design selectedDesign = null;
    private ILocation userLocation = null;
    
    private String userSessionToken = null;

    /**
     * @return the selectedDesign
     */
    public Design getSelectedDesign() {
        return selectedDesign;
    }

    /**
     * @param selectedDesign the selectedDesign to set
     */
    public void setSelectedDesign(Design selectedDesign) {
        this.selectedDesign = selectedDesign;
    }

    /**
     * @return the userLocation
     */
    public ILocation getUserLocation() {
        return userLocation;
    }

    /**
     * @param userLocation the userLocation to set
     */
    public void setUserLocation(ILocation userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * @return the userSessionToken
     */
    public String getUserSessionToken() {
        return userSessionToken;
    }

    /**
     * @param userSessionToken the userSessionToken to set
     */
    public void setUserSessionToken(String userSessionToken) {
        this.userSessionToken = userSessionToken;
    }
}

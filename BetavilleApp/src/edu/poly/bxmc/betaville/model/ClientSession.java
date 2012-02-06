/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville.model;

/**
 * Contains information about a user session
 * @author Skye Book
 */
public class ClientSession {
    
    private String user = null;
    private String sessionToken = null;
    
    private String server = null;
    
    public ClientSession(){}
    
    public ClientSession(String user, String sessionToken, String server){
	this.user = user;
	this.sessionToken = sessionToken;
	this.server = server;
    }

    /**
     * @return the user
     */
    public String getUser() {
	return user;
    }

    /**
     * @return the sessionToken
     */
    public String getSessionToken() {
	return sessionToken;
    }

    /**
     * @return the server
     */
    public String getServer() {
	return server;
    }
}

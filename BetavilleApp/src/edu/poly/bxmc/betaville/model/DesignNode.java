/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville.model;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Skye Book
 */
public class DesignNode extends AbstractNode{
    
    public DesignNode(){
	super(Children.create(new DesignChildFactory(), true));
	setDisplayName("Root");
    }
    
    public DesignNode(Design design){
	super(Children.create(new DesignChildFactory(), true), Lookups.singleton(design));
    }
}

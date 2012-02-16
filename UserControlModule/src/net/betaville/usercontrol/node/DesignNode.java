/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol.node;

import edu.poly.bxmc.betaville.model.Design;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Skye Book
 */
public class DesignNode extends AbstractNode{
    private Design design;
    
    public DesignNode(Design design){
	super(Children.LEAF);
	this.design = design;
	setDisplayName(design.getName());
    }
    
    public Design getDesign(){
	return design;
    }
}
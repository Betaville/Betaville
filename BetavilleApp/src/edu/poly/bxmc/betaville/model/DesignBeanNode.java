/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville.model;

import java.beans.IntrospectionException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Skye Book
 */
public class DesignBeanNode extends BeanNode<Design>{
    public DesignBeanNode(Design design) throws IntrospectionException{
        super(design, Children.LEAF, Lookups.singleton(design));
    }
}

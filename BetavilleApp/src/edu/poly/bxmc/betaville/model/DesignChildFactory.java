/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville.model;

import java.util.List;
import org.openide.nodes.ChildFactory;

/**
 *
 * @author Skye Book
 */
public class DesignChildFactory extends ChildFactory<Design> {

    @Override
    protected boolean createKeys(List<Design> toPopulate) {
	toPopulate.add(new EmptyDesign());
	return true;
    }
}

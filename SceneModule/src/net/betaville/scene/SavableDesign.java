/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import edu.poly.bxmc.betaville.model.Design;
import java.io.IOException;

/**
 *
 * @author Skye Book
 */
public class SavableDesign implements Savable{
    
    private Design design;
    
    public SavableDesign(Design design){
	this.design = design;
    }
    
    public Design getDesign(){
	return design;
    }
    
    public void setDesign(Design design){
	this.design = design;
    }

    @Override
    public void write(JmeExporter je) throws IOException {
	// Do nothing - We are not interested in persisting design data during import/export
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
	// Do nothing - We are not interested in persisting design data during import/export
    }
    
}

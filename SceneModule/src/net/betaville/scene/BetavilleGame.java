/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication{

    @Override
    public void simpleInitApp(){
        
        Box box = new Box(1, 1, 1);
        Geometry geometry = new Geometry("box", box);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
        
        flyCam.setDragToRotate(true);
    }
    
    @Override
    public void simpleUpdate(float tpf){
    }
}

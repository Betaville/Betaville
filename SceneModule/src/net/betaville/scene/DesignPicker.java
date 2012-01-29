/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import edu.poly.bxmc.betaville.model.Design;

/**
 *
 * @author Skye Book
 */
public class DesignPicker implements AnalogListener {

    private InputManager inputManager;
    private Camera cam;
    private Node rootNode;
    private DesignSelectionCallback designSelectionCallback;

    public DesignPicker(InputManager inputManager, Camera cam, Node rootNode, DesignSelectionCallback designSelectionCallback) {
	this.inputManager = inputManager;
	this.cam = cam;
	this.rootNode = rootNode;
	this.designSelectionCallback = designSelectionCallback;
    }

    @Override
    public void onAnalog(String name, float intensity, float tpf) {
        
        long start = System.currentTimeMillis();
	
	// Reset results list.
	CollisionResults results = new CollisionResults();
	// Convert screen click to 3d position
	Vector2f click2d = inputManager.getCursorPosition();
	Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
	Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d);
	dir.normalizeLocal();
	// Aim the ray from the clicked spot forwards.
	Ray ray = new Ray(click3d, dir);
	// Collect intersections between ray and all nodes in results list.
	rootNode.collideWith(ray, results);
        
        System.out.println("Collision calculation took " + (System.currentTimeMillis()-start)+"ms");
        
	// Use the results -- we rotate the selected geometry.
	if (results.size() > 0) {
	    // The closest result is the target that the player picked:
	    Geometry target = results.getClosestCollision().getGeometry();
	   
	    // get the node that has this object's data
	    Node parent = target.getParent();
	    while(parent.getParent().getParent()!=rootNode){
		parent = parent.getParent();
	    }
	    
	    SavableDesign savable = parent.getUserData("design");
	    if(savable!=null){
		System.out.println("Selected Design " + savable.getDesign().getName());
		designSelectionCallback.designSelected(savable.getDesign());
	    }
	}
    }
    
    public interface DesignSelectionCallback{
	public void designSelected(Design selectedDesign);
    }
}

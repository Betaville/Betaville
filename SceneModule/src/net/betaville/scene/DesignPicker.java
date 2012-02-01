/**
 * Copyright (c) 2008-2012, Brooklyn eXperimental Media Center
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of Brooklyn eXperimental Media
 * Center nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Brooklyn eXperimental Media Center BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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

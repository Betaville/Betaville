/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import edu.poly.bxmc.betaville.SettingsPreferences;
import edu.poly.bxmc.betaville.model.Wormhole;
import net.betaville.usercontrol.lookup.CentralLookup;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication {
    
    private Wormhole wormhole;
    private CityAppState cityAppState;

    // Core Nodes
    @Override
    public void simpleInitApp() {
	wormhole = CentralLookup.getDefault().lookup(Wormhole.class);
        
        System.out.println("Wormhole Name: " + wormhole.getName());

	// create a CityAppState
	cityAppState = new CityAppState(SettingsPreferences.getCity(), wormhole.getLocation());
        cityAppState.provide(rootNode, assetManager);
        cityAppState.loadBase(wormhole.getLocation());
	cam.setLocation(cityAppState.getCoordinateTransformer().locationToBetaville(wormhole.getLocation()));
	
	stateManager.attach(cityAppState);
	cityAppState.setEnabled(true);
	
	
	// create lights
	ColorRGBA diffuseLightColor = new ColorRGBA(1f, 1f, 1f, 1f);
	ColorRGBA diffuseLightColor2 = new ColorRGBA(154f / 255f, 160f / 255f, 166f / 255f, 185f / 255f);

	DirectionalLight directionalLight = new DirectionalLight();
	directionalLight.setDirection(new Vector3f(.25f, -.85f, .75f));
	directionalLight.setColor(diffuseLightColor);

	DirectionalLight fillLight = new DirectionalLight();
	fillLight.setDirection(new Vector3f(-.25f, .85f, -.75f));
	fillLight.setColor(diffuseLightColor2);
	
	rootNode.addLight(directionalLight);
	rootNode.addLight(fillLight);

	
	/*
	Box box = new Box(1, 1, 1); Geometry geometry = new Geometry("box", box);
	Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	geometry.setMaterial(material);
	rootNode.attachChild(geometry);
	*/
	 

	flyCam.setDragToRotate(true);
    }

    @Override
    public void simpleUpdate(float tpf) {}
    
    private AnalogListener analogListener = new AnalogListener() {

	public void onAnalog(String name, float intensity, float tpf) {
	    if (name.equals("pick target")) {
		// Reset results list.
		CollisionResults results = new CollisionResults();
		// Convert screen click to 3d position
		Vector2f click2d = inputManager.getCursorPosition();
		Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d);
		// Aim the ray from the clicked spot forwards.
		Ray ray = new Ray(click3d, dir);
		// Collect intersections between ray and all nodes in results list.
		rootNode.collideWith(ray, results);
		// (Print the results so we see what is going on:)
		for (int i = 0; i < results.size(); i++) {
		    // (For each “hit”, we know distance, impact point, geometry.)
		    float dist = results.getCollision(i).getDistance();
		    Vector3f pt = results.getCollision(i).getContactPoint();
		    String target = results.getCollision(i).getGeometry().getName();
		    System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
		}
		// Use the results -- we rotate the selected geometry.
		if (results.size() > 0) {
		    // The closest result is the target that the player picked:
		    Geometry target = results.getClosestCollision().getGeometry();
		    // Here comes the action:
		    if (target.getName().equals("Red Box")) {
			target.rotate(0, -intensity, 0);
		    } else if (target.getName().equals("Blue Box")) {
			target.rotate(0, intensity, 0);
		    }
		}
	    } // else if ...
	}
    };
}

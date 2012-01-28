/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
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
import edu.poly.bxmc.betaville.model.Design;
import net.betaville.scene.DesignPicker.DesignSelectionCallback;
import net.betaville.usercontrol.lookup.CentralLookup;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication {
    
    private Wormhole wormhole;
    private CityAppState cityAppState;
    
    private DesignPicker designPicker;
    
    private DesignSelectionCallback designSelectionCallback = null;
    
    public BetavilleGame(DesignSelectionCallback designSelectionCallback){
	this.designSelectionCallback = designSelectionCallback;
    }

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
        /*
        flyCam = new CustomFlyByCamera(cam);
        flyCam.setMoveSpeed(1f);
        flyCam.registerWithInput(inputManager);
        */
	flyCam.setDragToRotate(true);
	
	// Create the pickers
	designPicker = new DesignPicker(inputManager, cam, rootNode, designSelectionCallback);
	
	// Add the listeners
	inputManager.addListener(designPicker, "PickDesign");
	
	// Add the design picker by default
	inputManager.addMapping("PickDesign", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
    }

    @Override
    public void simpleUpdate(float tpf) {}
}

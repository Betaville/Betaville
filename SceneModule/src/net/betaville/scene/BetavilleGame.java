/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import edu.poly.bxmc.betaville.SceneScape;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.Wormhole;
import org.openide.util.Utilities;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication {

    // Core Nodes
    @Override
    public void simpleInitApp() {
	Wormhole wormhole = Utilities.actionsGlobalContext().lookupResult(Wormhole.class).allInstances().iterator().next();

	// create a CityAppState
	CityAppState cityAppState = new CityAppState(SceneScape.getCity(), wormhole.getLocation());
	cityAppState.loadBase(wormhole.getLocation());
	stateManager.attach(cityAppState);
	
	
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
	 * Box box = new Box(1, 1, 1); Geometry geometry = new Geometry("box",
	 * box); Material material = new Material(assetManager,
	 * "Common/MatDefs/Misc/Unshaded.j3md"); geometry.setMaterial(material);
	 * rootNode.attachChild(geometry);
	 */

	flyCam.setDragToRotate(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
}

/**
 * Copyright (c) 2008-2012, Brooklyn eXperimental Media Center All rights
 * reserved.
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

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import edu.poly.bxmc.betaville.SettingsPreferences;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.Wormhole;
import java.util.Collection;
import java.util.Iterator;
import net.betaville.scene.DesignPicker.DesignSelectionCallback;
import net.betaville.usercontrol.lookup.CentralLookup;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication implements LookupListener {

    private Lookup.Result<Wormhole> result = null;
    private CityAppState currentCityAppState;
    private DesignPicker designPicker;
    private DesignSelectionCallback designSelectionCallback = null;

    public BetavilleGame(){
	
	// Create the design selection listener
	designSelectionCallback = new DesignSelectionCallback() {

	    @Override
	    public void designSelected(Design selectedDesign) {
		StatusDisplayer.getDefault().setStatusText(selectedDesign.getName());
		CentralLookup lookup = CentralLookup.getDefault();
		Collection designs = lookup.lookupAll(Design.class);
		if (!designs.isEmpty()) {
		    Iterator it = designs.iterator();
		    while (it.hasNext()) {
			lookup.remove(it.next());
		    }
		}

		lookup.add(selectedDesign);
	    }
	};
    }

    // Core Nodes
    @Override
    public void simpleInitApp() {
	
	// Create the wormhole lookup result and add the listener
	result = CentralLookup.getDefault().lookupResult(Wormhole.class);
	result.addLookupListener(this);
	
	cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), .1f, 500f);

	// add the camera's location to the lookup
	CentralLookup.getDefault().add(cam.getLocation());

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

	// remove the original fly cam
	inputManager.removeListener(flyCam);

	flyCam = new CustomFlyByCamera(cam);
	flyCam.setMoveSpeed(1f);
	flyCam.registerWithInput(inputManager);
	//flyCam.setDragToRotate(true);

	// Create the picker
	designPicker = new DesignPicker(inputManager, cam, rootNode, designSelectionCallback);

	// Add the listeners
	inputManager.addListener(designPicker, "PickDesign");

	// Add the design picker by default
	inputManager.addMapping("PickDesign", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));



	Wormhole wormhole = CentralLookup.getDefault().lookup(Wormhole.class);
	if (wormhole != null) {
	    System.out.println("Wormhole Name: " + wormhole.getName());

	    // create a CityAppState
	    currentCityAppState = loadCity(wormhole);
	    cam.setLocation(currentCityAppState.getCoordinateTransformer().locationToBetaville(wormhole.getLocation()));

	    stateManager.attach(currentCityAppState);
	    currentCityAppState.setEnabled(true);
	}
    }

    private CityAppState loadCity(Wormhole wormhole) {
	CityAppState appState = new CityAppState(SettingsPreferences.getCity(), wormhole.getLocation());
	appState.provide(rootNode, assetManager);
	appState.loadBase(wormhole.getLocation());
	return appState;
    }

    @Override
    public void simpleUpdate(float tpf) {
	// update the user's location
	if (currentCityAppState != null) {
	    CentralLookup lookup = CentralLookup.getDefault();
	    Collection locations = lookup.lookupAll(ILocation.class);
	    if (!locations.isEmpty()) {
		Iterator it = locations.iterator();
		while (it.hasNext()) {
		    lookup.remove(it.next());
		}
	    }

	    lookup.add(currentCityAppState.getCoordinateTransformer().betavilleToUTM(cam.getLocation()));
	}

    }

    @Override
    public void resultChanged(LookupEvent ev) {
	if(result.allInstances().size()>0){
	    Wormhole wormhole = result.allInstances().iterator().next();
	    System.out.println("Wormhole Name: " + wormhole.getName());

	    // create a CityAppState
	    CityAppState newCityAppState = loadCity(wormhole);
	    
	    if(currentCityAppState!=null){
		stateManager.detach(currentCityAppState);
		stateManager.attach(newCityAppState);
		cam.setLocation(currentCityAppState.getCoordinateTransformer().locationToBetaville(wormhole.getLocation()));
		newCityAppState.setEnabled(true);
	    }
	}
    }
}

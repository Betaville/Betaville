/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.Collections;
import java.util.Iterator;
import net.betaville.scene.DesignPicker.DesignSelectionCallback;
import net.betaville.usercontrol.lookup.CentralLookup;
import org.openide.awt.StatusDisplayer;

/**
 *
 * @author skyebook
 */
public class BetavilleGame extends SimpleApplication {

    private Wormhole wormhole;
    private CityAppState currentCityAppState;
    private DesignPicker designPicker;
    private DesignSelectionCallback designSelectionCallback = null;

    public BetavilleGame() {
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
        // add the camera's location to the lookup
        CentralLookup.getDefault().add(cam.getLocation());

        wormhole = CentralLookup.getDefault().lookup(Wormhole.class);

        System.out.println("Wormhole Name: " + wormhole.getName());

        // create a CityAppState
        currentCityAppState = new CityAppState(SettingsPreferences.getCity(), wormhole.getLocation());
        currentCityAppState.provide(rootNode, assetManager);
        currentCityAppState.loadBase(wormhole.getLocation());
        cam.setLocation(currentCityAppState.getCoordinateTransformer().locationToBetaville(wormhole.getLocation()));

        stateManager.attach(currentCityAppState);
        currentCityAppState.setEnabled(true);


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
    }

    @Override
    public void simpleUpdate(float tpf) {
        // update the user's location
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;

/**
 *
 * @author Skye Book
 */
public class CustomFlyByCamera extends FlyByCamera{
    public CustomFlyByCamera(Camera cam){
        super(cam);
    }
    
    @Override
    public void registerWithInput(InputManager inputManager){
        super.registerWithInput(inputManager);
	
	this.inputManager.addMapping("AltLeft", new KeyTrigger(KeyInput.KEY_LMENU));
	this.inputManager.addMapping("AltRight", new KeyTrigger(KeyInput.KEY_RMENU));
	
        this.inputManager.deleteMapping("FLYCAM_RotateDrag");
        this.inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        this.inputManager.addListener(this, "FLYCAM_RotateDrag", "AltLeft", "AltRight");
	
        setDragToRotate(true);
    }
    
    @Override
    protected void zoomCamera(float value){
        // Override the zoomCamera method to redirect to moving the camera forward
        moveCamera(value, false);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
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
        this.inputManager.deleteMapping("FLYCAM_RotateDrag");
        this.inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        this.inputManager.addListener(this, "FLYCAM_RotateDrag");
    }
    
    @Override
    protected void zoomCamera(float value){
        // Override the zoomCamera method to redirect to moving the camera forward
        moveCamera(value, false);
    }
}

package net.betaville.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.*;

/**
 * A self-contained scenegraph representation of a city.
 * @author Skye Book
 */
public class CityAppState implements AppState{
    
    private Node rootNode;
    
    private Node designNode;
    private Node terrainNode;
    
    private JME3MapManager mapManager;
    
    private AssetManager assetManager;
    
    public CityAppState(ILocation pointOfOrigin){
        mapManager = new JME3MapManager();
        mapManager.adjustOffsets(pointOfOrigin);
    }
    
    public void addDesignToScene(Design design){
       // TODO: Implement me!
    }
    
    public Node getDesignFromScene(Design design){
        return (Node)designNode.getChild(design.getFullIdentifier());
    }
    
    public void removeDesignFromScene(Design design){
         // TODO: Implement me!
    }
    
    public void addDesignToTerrain(Design design){
         // TODO: Implement me!
    }
    
    public Node getDesignFromTerrain(Design design){
         // TODO: Implement me!
        return null;
    }
    
    public void removeDesignFromTerrain(Design design){
         // TODO: Implement me!
    }
    
    /**
     * Loads a design already in the jME format
     * @param design
     * @return 
     */
    private Node loadDesign(Design design){
        Node node = (Node) assetManager.loadModel(design.getID()+".j30");
        node.setName(design.getFullIdentifier());
        Vector3f landingPoint = mapManager.locationToBetaville(design.getCoordinate());
        node.setLocalTranslation(landingPoint);
        
        if(design instanceof ModeledDesign){
            Quaternion q = new Quaternion();
            q.fromAngles(
                    ((ModeledDesign)design).getRotationX(),
                    ((ModeledDesign)design).getRotationY(),
                    ((ModeledDesign)design).getRotationZ());
            node.setLocalRotation(q);
        }
        else if(design instanceof SketchedDesign){
            // Still unsupported
        }
        else if(design instanceof AudibleDesign){
            // Still unsupported
        }
        else if(design instanceof VideoDesign){
            // Still unsupported
        }
        
        return node;
    }

    @Override
    public void initialize(AppStateManager asm, Application aplctn) {
        rootNode = ((SimpleApplication)aplctn).getRootNode();
        
        designNode = new Node("designNode");
        terrainNode = new Node("terrainNode");
        
        assetManager = aplctn.getAssetManager();
    }

    @Override
    public boolean isInitialized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEnabled(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateAttached(AppStateManager asm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateDetached(AppStateManager asm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(RenderManager rm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void postRender() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

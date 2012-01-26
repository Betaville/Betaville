package net.betaville.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import edu.poly.bxmc.betaville.CacheManager;
import edu.poly.bxmc.betaville.jme.loaders.util.DriveFinder;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.*;
import edu.poly.bxmc.betaville.net.InsecureClientManager;
import edu.poly.bxmc.betaville.net.NetPool;
import edu.poly.bxmc.betaville.net.ProgressInputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.skyebook.jme2loader.JME2Loader;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;

/**
 * A self-contained scenegraph representation of a city.
 *
 * @author Skye Book
 */
public class CityAppState implements AppState {

    private boolean enabled = true;
    private Node rootNode;
    private Node designNode;
    private Node terrainNode;
    private JME3MapManager mapManager;
    private AssetManager assetManager;
    private City thisCity;

    public CityAppState(City city, ILocation pointOfOrigin) {
        thisCity = city;
        mapManager = new JME3MapManager();
        mapManager.adjustOffsets(pointOfOrigin);

        designNode = new Node("designNode");
        terrainNode = new Node("terrainNode");
    }

    public void addDesignToScene(Design design) throws IOException {
        designNode.attachChild(loadDesign(design));
    }

    public Node getDesignFromScene(Design design) {
        return (Node) designNode.getChild(design.getFullIdentifier());
    }

    public void removeDesignFromScene(Design design) {
        designNode.detachChildNamed(design.getFullIdentifier());
    }

    public void addDesignToTerrain(Design design) throws IOException {
        terrainNode.attachChild(loadDesign(design));
    }

    public Node getDesignFromTerrain(Design design) {
        return (Node) terrainNode.getChild(design.getFullIdentifier());
    }

    public void removeDesignFromTerrain(Design design) {
        terrainNode.detachChildNamed(design.getFullIdentifier());
    }

    /**
     * Loads a design already in the jME format
     *
     * @param design
     * @return
     */
    private Node loadDesign(Design design) throws IOException {
        assetManager.registerLocator(DriveFinder.getBetavilleFolder().toString(), FileLocator.class);

        Node node = null;
	// strip the extension from the filename
	String fileWithoutExtension = design.getFilepath().substring(0, design.getFilepath().lastIndexOf("."));
        if (design.getFilepath().endsWith("jme")) {
            // check if we have the j30 first
            try {
                assetManager.registerLoader(BinaryImporter.class, "j30");
                node = (Node) assetManager.loadModel("cache/" +fileWithoutExtension + ".j30");
            } catch (AssetNotFoundException e) {
                // if we don't have the j30 yet, convert this to the jME3 format
                assetManager.registerLoader(JME2Loader.class, "jme");
                StatusDisplayer.getDefault().setStatusText("Converting model to jME3 format and caching locally");
                node = (Node) assetManager.loadModel("cache/" +fileWithoutExtension + ".jme");
                BinaryExporter.getInstance().save(node, new File(DriveFinder.getBetavilleFolder().toString() + "/cache/" + fileWithoutExtension + ".j30"));
            }
        } else {
            node = (Node) assetManager.loadModel("cache/"+fileWithoutExtension + ".j30");
        }

        node.setName(design.getFullIdentifier());
        Vector3f landingPoint = mapManager.locationToBetaville(design.getCoordinate());
        node.setLocalTranslation(landingPoint);

        if (design instanceof ModeledDesign) {
            Quaternion q = new Quaternion();
            q.fromAngles(
                    ((ModeledDesign) design).getRotationX(),
                    ((ModeledDesign) design).getRotationY(),
                    ((ModeledDesign) design).getRotationZ());
            node.setLocalRotation(q);
        } else if (design instanceof SketchedDesign) {
            // Still unsupported
        } else if (design instanceof AudibleDesign) {
            // Still unsupported
        } else if (design instanceof VideoDesign) {
            // Still unsupported
        }

        return node;
    }

    @Override
    public void initialize(AppStateManager asm, Application aplctn) {
    }

    public void provide(Node rootNode, AssetManager assetManager) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }

    @Override
    public boolean isInitialized() {
        return rootNode != null && designNode != null;
    }

    @Override
    public void setEnabled(boolean bln) {
        enabled = bln;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager asm) {
        rootNode.attachChild(designNode);
        rootNode.attachChild(terrainNode);
    }

    @Override
    public void stateDetached(AppStateManager asm) {
        rootNode.detachChild(designNode);
        rootNode.detachChild(terrainNode);
    }

    @Override
    public void update(float f) {
        // Nothing to see here
    }

    @Override
    public void render(RenderManager rm) {
        // Nothing to see here
    }

    @Override
    public void postRender() {
        // Nothing to see here
    }

    @Override
    public void cleanup() {
        // release all designs for garbage collection
        designNode.detachAllChildren();
        terrainNode.detachAllChildren();
    }

    private void updateStatusBar(int number, int total, String message) {
        StatusDisplayer.getDefault().setStatusText(number + " of " + total + " " + message);
    }

    public void loadBase(ILocation locale) {
        try {
            System.out.println("here goes");
            StatusDisplayer.getDefault().setStatusText("Retrieving Base Models");

            final AtomicInteger itemsLoaded = new AtomicInteger(0);
	    
	    InsecureClientManager icm = NetPool.getPool().getConnection();
	    icm.getProgressInputStream().setListener(new ProgressInputStream.ProgressInputListener() {

		@Override
		public void readProgressUpdate(int i) {
		    System.out.println(i + " bytes read");
		}
	    });

            List<Design> designs = NetPool.getPool().getConnection().findBaseDesignsByCity(thisCity.getCityID());

            // If the designs are null, we can't do anything
            if (designs == null) {
                DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message("Could not connect to server and get designs for city " + thisCity.getCityID()));
                return;
            }

            Iterator<Design> designIterator = designs.iterator();
            while (designIterator.hasNext()) {
                Design d = designIterator.next();
                if (d.getClassification().equals(Design.Classification.BASE) && d instanceof ModeledDesign) {
                    // do nothing
                } else {
                    designIterator.remove();
                }
            }

            StatusDisplayer.getDefault().setStatusText("Sorting models by distance");

            Collections.sort(designs, Design.distanceComparator(locale.getUTM()));

            updateStatusBar(0, designs.size(), "Models Loaded");

            for (int i = 0; i < designs.size(); i++) {
                Design design = designs.get(i);


                //logger.debug("adding: " + design.getName() + " | ID: " + design.getID());

                boolean fileResponse = CacheManager.getCacheManager().requestFile(design.getID(), design.getFilepath());

                // if the file couldn't be found or downloaded, there is a problem
                if (!fileResponse) {
                    // ERROR!
                    System.out.println("This is bad");
                    continue;
                } else {
                    addDesignToScene(design);
                    itemsLoaded.incrementAndGet();
                    updateStatusBar(itemsLoaded.get(), designs.size(), "Models Loaded");
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("NO!");
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public JME3MapManager getCoordinateTransformer(){
	return mapManager;
    }
}

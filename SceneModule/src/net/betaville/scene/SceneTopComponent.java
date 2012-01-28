/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.DesignNode;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author Skye Book
 */
@TopComponent.Description(preferredID="SceneTopComponent", iconBase="data/new_icon.png", persistenceType=TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.betaville.scene.SceneTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 0),
    @ActionReference(path = "Toolbars/File", position = 0)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_NewCanvasAction")
@Messages({"CTL_NewCanvasAction=New Canvas"})
public class SceneTopComponent extends TopComponent implements ActionListener, ChangeListener, ComponentListener{
    private BetavilleGame sceneGame;
    private JmeCanvasContext context;
    private Canvas canvas;
    
    public SceneTopComponent(){
        setName("Scene");
        setLayout(new BoxLayout(this, WIDTH));
        AppSettings settings = new AppSettings(true);
        settings.setWidth(640);
        settings.setHeight(480);
        
        sceneGame = new BetavilleGame(new DesignPicker.DesignSelectionCallback() {

	    @Override
	    public void designSelected(final Design selectedDesign) {
		try {
		    EventQueue.invokeAndWait(new Runnable() {

			@Override
			public void run() {
			    setActivatedNodes(new DesignNode[]{new DesignNode(selectedDesign)});
			}
		    });
		    
		    //associateLookup(Lookups.singleton(selectedDesign));
		} catch (InterruptedException ex) {
		    Exceptions.printStackTrace(ex);
		} catch (InvocationTargetException ex) {
		    Exceptions.printStackTrace(ex);
		}
	    }
	});
        sceneGame.setPauseOnLostFocus(false);
        sceneGame.setSettings(settings);
        sceneGame.createCanvas();
        sceneGame.startCanvas();
        
        context = (JmeCanvasContext)sceneGame.getContext();
        canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
        
        add(canvas);
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent ce) {
        System.out.println("top component resized");
    }

    @Override
    public void componentMoved(ComponentEvent ce) {
    }

    @Override
    public void componentShown(ComponentEvent ce) {
    }

    @Override
    public void componentHidden(ComponentEvent ce) {
    }
    
}

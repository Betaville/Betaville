/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Skye Book
 */
@TopComponent.Description(preferredID="SceneTopComponent", iconBase="data/new_icon.png", persistenceType=TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
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
        
        sceneGame = new BetavilleGame();
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

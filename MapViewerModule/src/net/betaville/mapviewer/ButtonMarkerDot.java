/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.mapviewer;

import edu.poly.bxmc.betaville.jme.map.GPSCoordinate;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 *
 * @author Skye Book
 */
public class ButtonMarkerDot extends JButton implements MapMarker{
    
    private double lat;
    private double lon;
    
    public ButtonMarkerDot(GPSCoordinate coordinate, ImageIcon icon){
	super(icon);
	setLayout(null);
	setMargin(new Insets(0, 0, 0, 0));
	lat = coordinate.getLatitude();
	lon = coordinate.getLongitude();
    }
    
    public ButtonMarkerDot(GPSCoordinate coordinate, String buttonText){
	super(buttonText);
	setLayout(null);
	setMargin(new Insets(0, 0, 0, 0));
	lat = coordinate.getLatitude();
	lon = coordinate.getLongitude();
    }
    
    public ButtonMarkerDot(GPSCoordinate coordinate){
	this(coordinate, "");
    }

    @Override
    public double getLat() {
	return lat;
    }

    @Override
    public double getLon() {
	return lon;
    }

    @Override
    public void paint(Graphics grphcs, Point point) {
	// Painting is taken care of by the component's own internals.
	
	// Set the location of the button
	setBounds(point.x, point.y, 75, 25);
    }
    
}

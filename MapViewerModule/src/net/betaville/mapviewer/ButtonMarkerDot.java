/* Copyright (c) 2008-2012, Brooklyn eXperimental Media Center
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Brooklyn eXperimental Media Center nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Brooklyn eXperimental Media Center BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

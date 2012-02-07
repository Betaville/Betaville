/**
 * Copyright (c) 2008-2012, Brooklyn eXperimental Media Center
 * All rights reserved.
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
package net.betaville.mapviewer;

import edu.poly.bxmc.betaville.jme.map.GPSCoordinate;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.model.ClientSession;
import edu.poly.bxmc.betaville.model.Wormhole;
import edu.poly.bxmc.betaville.net.InsecureClientManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import net.betaville.usercontrol.lookup.CentralLookup;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.betaville.mapviewer//MapViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "MapViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.betaville.mapviewer.MapViewerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MapViewerAction",
preferredID = "MapViewerTopComponent")
@Messages({
    "CTL_MapViewerAction=MapViewer",
    "CTL_MapViewerTopComponent=MapViewer Window",
    "HINT_MapViewerTopComponent=This is a MapViewer window"
})
public final class MapViewerTopComponent extends TopComponent implements LookupListener {

    Lookup.Result<ILocation> result = null;
    private JMapViewer map = new JMapViewer();
    final JLabel mperpLabelName = new JLabel("Meters/Pixels: ");
    final JLabel mperpLabelValue = new JLabel(String.format("%s", map.getMeterPerPixel()));
    final JLabel zoomLabel = new JLabel("Zoom: ");
    final JLabel zoomValue = new JLabel(String.format("%s", map.getZoom()));
    private MapMarkerDot cameraMapMarker = null;

    public MapViewerTopComponent() {
        initComponents();
        setName(Bundle.CTL_MapViewerTopComponent());
        setToolTipText(Bundle.HINT_MapViewerTopComponent());

        JPanel panel = new JPanel();
        JPanel helpPanel = new JPanel();


        add(panel, BorderLayout.NORTH);
        add(helpPanel, BorderLayout.SOUTH);
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpPanel.add(helpLabel);
        JButton button = new JButton("setDisplayToFitMapMarkers");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                map.setDisplayToFitMapMarkers();
            }
        });
        JComboBox tileSourceSelector = new JComboBox(new TileSource[]{new OsmTileSource.Mapnik(),
                    new OsmTileSource.TilesAtHome(), new OsmTileSource.CycleMap(), new BingAerialTileSource()});
        tileSourceSelector.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                map.setTileSource((TileSource) e.getItem());
            }
        });
        JComboBox tileLoaderSelector;
        try {
            tileLoaderSelector = new JComboBox(new TileLoader[]{new OsmFileCacheTileLoader(map),
                        new OsmTileLoader(map)});
        } catch (IOException e) {
            tileLoaderSelector = new JComboBox(new TileLoader[]{new OsmTileLoader(map)});
        }
        tileLoaderSelector.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                map.setTileLoader((TileLoader) e.getItem());
            }
        });
        map.setTileLoader((TileLoader) tileLoaderSelector.getSelectedItem());
        panel.add(tileSourceSelector);
        panel.add(tileLoaderSelector);
        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setSelected(map.getMapMarkersVisible());
        showMapMarker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                map.setMapMarkerVisible(showMapMarker.isSelected());
            }
        });
        panel.add(showMapMarker);
        final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
        showTileGrid.setSelected(map.isTileGridVisible());
        showTileGrid.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                map.setTileGridVisible(showTileGrid.isSelected());
            }
        });
        panel.add(showTileGrid);
        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setSelected(map.getZoomContolsVisible());
        showZoomControls.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                map.setZoomContolsVisible(showZoomControls.isSelected());
            }
        });
        panel.add(showZoomControls);
        panel.add(button);

        panel.add(zoomLabel);
        panel.add(zoomValue);
        panel.add(mperpLabelName);
        panel.add(mperpLabelValue);

        add(map, BorderLayout.CENTER);
	try {
	    // map.setDisplayPositionByLatLon(49.807, 8.6, 11);
	    // map.setTileGridVisible(true);
	    InsecureClientManager icm = new InsecureClientManager(null, CentralLookup.getDefault().lookup(ClientSession.class).getServer());
	    List<Wormhole> wormholes = icm.getAllWormholes();
            icm.close();
	    for(Wormhole wormhole : wormholes){
		addWormhole(wormhole);
	    }
	} catch (UnknownHostException ex) {
	    Exceptions.printStackTrace(ex);
	} catch (IOException ex) {
	    Exceptions.printStackTrace(ex);
	}

    }

    public void addWormhole(Wormhole wormhole) {
	GPSCoordinate gps = wormhole.getLocation().getGPS();

	WormholeMarkerDot dot = new WormholeMarkerDot(wormhole);
	dot.addWormholeMapMarkerSelectionListener(new WormholeMarkerDot.WormholeMarkerDotSelectionListener() {

	    @Override
	    public void wormholeMarkerDotSelected(Wormhole wormhole) {
		
		System.out.println("Selected " + wormhole.getName());
		
		CentralLookup lookup = CentralLookup.getDefault();
		Collection locations = lookup.lookupAll(Wormhole.class);
		if (!locations.isEmpty()) {
		    Iterator it = locations.iterator();
		    while (it.hasNext()) {
			lookup.remove(it.next());
		    }
		}

		lookup.add(wormhole);
	    }
	});
	map.addMapMarker(dot);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

	setLayout(new java.awt.BorderLayout());
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    // End of variables declaration                   
    @Override
    public void componentOpened() {
        result = CentralLookup.getDefault().lookupResult(ILocation.class);
        result.addLookupListener(this);

        map.addJMVListener(new JMapViewerEventListener() {

            @Override
            public void processCommand(JMVCommandEvent command) {
                if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM)
                        || command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
                    updateZoomParameters();
                }
            }

            private void updateZoomParameters() {
                if (mperpLabelValue != null) {
                    mperpLabelValue.setText(String.format("%s", map.getMeterPerPixel()));
                }
                if (zoomValue != null) {
                    zoomValue.setText(String.format("%s", map.getZoom()));
                }
            }
        });

        map.addMouseListener(new MouseListener() {
	    
	    int mouseDistanceTolerance = 5;

            @Override
            public void mouseReleased(MouseEvent arg0) {
                //System.out.println(map.getPosition(arg0.getPoint()));
		
		// find the closest map marker
		Point mousePoint = arg0.getPoint();
		Coordinate mouseCoordinate = map.getPosition(mousePoint);
		List<MapMarker> markers = map.getMapMarkerList();
		
		double closestDistance = -1;
		MapMarker closestMarker = null;
		
		for(MapMarker marker : markers){
		    
		    //System.out.println("Checking Marker at: " + marker.getLat()+", "+marker.getLon());
		    
		    // Get the position of the item on the map.  This will return null if it is outside the viewport
		    Point mapPos = map.getMapPosition(marker.getLat(), marker.getLon());
		    
		    if(mapPos==null){
			continue;
		    }
		    
		    //System.out.println("MARKER");
		    
		    double distance = mousePoint.distance(mapPos);
		    if(closestDistance==-1){
			closestDistance = distance;
		    }
		    else{
			if(distance < closestDistance){
			    closestDistance = distance;
			    closestMarker = marker;
			}
		    }
		}
		
		if(closestDistance < mouseDistanceTolerance){
		    if(closestMarker instanceof WormholeMarkerDot){
			((WormholeMarkerDot)closestMarker).fireSelectionEvent();
		    }
		    //System.out.println("Closest Distance: " + closestDistance);
		    //System.out.println("Marker selected!");
		}
		else{
		    //System.out.println("Closest Distance: " + closestDistance);
		}
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
		//System.out.println("Mouse Clicked: " + map.getPosition(arg0.getPoint()));
            }
        });
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        System.out.println("lookup update");
        Collection<? extends ILocation> locations = result.allInstances();
        if (!locations.isEmpty()) {
            ILocation location = locations.iterator().next();
            final GPSCoordinate gps = location.getGPS();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
		    if(cameraMapMarker!=null){
			map.removeMapMarker(cameraMapMarker);
		    }
                    cameraMapMarker = new MapMarkerDot(Color.RED, gps.getLatitude(), gps.getLongitude());
                    map.addMapMarker(cameraMapMarker);
                }
            });
        }
    }
}

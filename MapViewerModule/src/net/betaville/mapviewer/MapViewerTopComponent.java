/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.mapviewer;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.OsmTileLoader;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
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
public final class MapViewerTopComponent extends TopComponent {

    private JMapViewer map = new JMapViewer();
    final JLabel mperpLabelName = new JLabel("Meters/Pixels: ");
    final JLabel mperpLabelValue = new JLabel(String.format("%s", map.getMeterPerPixel()));
    final JLabel zoomLabel = new JLabel("Zoom: ");
    final JLabel zoomValue = new JLabel(String.format("%s", map.getZoom()));

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

            public void actionPerformed(ActionEvent e) {
                map.setDisplayToFitMapMarkers();
            }
        });
        JComboBox tileSourceSelector = new JComboBox(new TileSource[]{new OsmTileSource.Mapnik(),
                    new OsmTileSource.TilesAtHome(), new OsmTileSource.CycleMap(), new BingAerialTileSource()});
        tileSourceSelector.addItemListener(new ItemListener() {

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

            public void actionPerformed(ActionEvent e) {
                map.setMapMarkerVisible(showMapMarker.isSelected());
            }
        });
        panel.add(showMapMarker);
        final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
        showTileGrid.setSelected(map.isTileGridVisible());
        showTileGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setTileGridVisible(showTileGrid.isSelected());
            }
        });
        panel.add(showTileGrid);
        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setSelected(map.getZoomContolsVisible());
        showZoomControls.addActionListener(new ActionListener() {

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

        //
        map.addMapMarker(new MapMarkerDot(49.814284999, 8.642065999));
        map.addMapMarker(new MapMarkerDot(49.91, 8.24));
        map.addMapMarker(new MapMarkerDot(49.71, 8.64));
        map.addMapMarker(new MapMarkerDot(48.71, -1));
        map.addMapMarker(new MapMarkerDot(49.8588, 8.643));

        // map.setDisplayPositionByLatLon(49.807, 8.6, 11);
        // map.setTileGridVisible(true);

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

            @Override
            public void mouseReleased(MouseEvent arg0) {
                System.out.println(map.getPosition(arg0.getPoint()));
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
            }
        });
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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
}

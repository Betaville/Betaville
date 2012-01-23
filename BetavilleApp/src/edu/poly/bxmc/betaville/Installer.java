/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville;

import edu.poly.bxmc.betaville.jme.loaders.util.DriveFinder;
import edu.poly.bxmc.betaville.xml.PreferenceReader;
import edu.poly.bxmc.betaville.xml.UpdatedPreferenceWriter;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    private static final Logger logger = Logger.getLogger(Installer.class);

    @Override
    public void restored() {
	// Bootstrap the original Betaville preferences
	// Try to load the preferences
	try {
		PreferenceReader preferenceReader = new PreferenceReader(new File(
				DriveFinder.getHomeDir().toString()
				+ "/.betaville/preferences.xml"));
		if (preferenceReader.isXMLLoaded()) {
			preferenceReader.parse();
			UpdatedPreferenceWriter.writeDefaultPreferences();
		} else
			logger.info("Preferences file not found");
	} catch (JDOMException e) {
		logger.error("JDOM error", e);
	} catch (IOException e) {
		try {
			logger.info("Preferences file could not be found, writing one.");
			UpdatedPreferenceWriter.writeDefaultPreferences();
		} catch (IOException e1) {
			logger.error("Preferences file could not be written", e1);
		}
	}
    }
}

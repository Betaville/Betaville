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
	logger.info("Loading original preferences");
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

/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.pushingpixels.substance.internal.plugin;

import java.util.HashSet;
import java.util.Set;

import org.pushingpixels.substance.api.skin.*;

/**
 * Core plugin for skins. See
 * {@link org.pushingpixels.substance.internal.plugin.SubstanceSkinPlugin} interface. This class
 * is <b>for internal use only</b>.
 * 
 * @author Kirill Grouchnikov.
 */
public class BaseSkinPlugin implements SubstanceSkinPlugin {
	/**
	 * Creates info object on a single skin.
	 * 
	 * @param displayName
	 *            Skin display name.
	 * @param skinClass
	 *            Skin class.
	 * @param isDefault
	 *            Indication whether the specified skin is default.
	 * @return Info object on the specified skin.
	 */
	private static SkinInfo create(String displayName, Class<?> skinClass,
			boolean isDefault) {
		SkinInfo result = new SkinInfo(displayName, skinClass.getName());
		result.setDefault(isDefault);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pushingpixels.substance.plugin.SubstanceSkinPlugin#getSkins()
	 */
	public Set<SkinInfo> getSkins() {
		Set<SkinInfo> result = new HashSet<SkinInfo>();
		// result.add(create(AutumnSkin.NAME, AutumnSkin.class, false));
		result.add(create(BusinessSkin.NAME, BusinessSkin.class, false));
		result.add(create(BusinessBlackSteelSkin.NAME,
				BusinessBlackSteelSkin.class, false));
		result.add(create(BusinessBlueSteelSkin.NAME,
				BusinessBlueSteelSkin.class, false));
		result.add(create(CremeSkin.NAME, CremeSkin.class, false));
		result.add(create(ModerateSkin.NAME, ModerateSkin.class, false));
		result.add(create(SaharaSkin.NAME, SaharaSkin.class, false));
		result.add(create(OfficeBlue2007Skin.NAME, OfficeBlue2007Skin.class,
				false));
		result.add(create(OfficeSilver2007Skin.NAME,
				OfficeSilver2007Skin.class, false));
		result.add(create(RavenSkin.NAME, RavenSkin.class, false));
		result.add(create(GraphiteSkin.NAME, GraphiteSkin.class, false));
		result.add(create(GraphiteGlassSkin.NAME, GraphiteGlassSkin.class,
				false));
		result
				.add(create(GraphiteAquaSkin.NAME, GraphiteAquaSkin.class,
						false));
		result.add(create(ChallengerDeepSkin.NAME, ChallengerDeepSkin.class,
				false));
		result.add(create(EmeraldDuskSkin.NAME, EmeraldDuskSkin.class, false));
		result.add(create(NebulaSkin.NAME, NebulaSkin.class, false));
		result.add(create(NebulaBrickWallSkin.NAME, NebulaBrickWallSkin.class,
				false));
		result.add(create(MistSilverSkin.NAME, MistSilverSkin.class, false));
		result.add(create(MistAquaSkin.NAME, MistAquaSkin.class, false));
		result.add(create(AutumnSkin.NAME, AutumnSkin.class, false));
		result.add(create(CremeCoffeeSkin.NAME, CremeCoffeeSkin.class, false));
		result.add(create(DustSkin.NAME, DustSkin.class, false));
		result.add(create(DustCoffeeSkin.NAME, DustCoffeeSkin.class, false));
		result.add(create(TwilightSkin.NAME, TwilightSkin.class, false));
		result.add(create(MagellanSkin.NAME, MagellanSkin.class, false));
		result.add(create(GeminiSkin.NAME, GeminiSkin.class, false));

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.substance.plugin.SubstanceSkinPlugin#getDefaultSkinClassName()
	 */
	public String getDefaultSkinClassName() {
		return null;
	}
}

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
package org.pushingpixels.substance.api.watermark;

import java.awt.*;

import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

/**
 * Implementation of {@link SubstanceWatermark} that paints a translucent fill
 * (with no pattern). This class is part of officially supported API.
 * 
 * @author Kirill Grouchnikov
 * @author Chris Hall
 */
public class SubstanceNullWatermark implements SubstanceWatermark {
	/**
	 * Simple constructor.
	 */
	public SubstanceNullWatermark() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.substance.watermark.SubstanceWatermark#drawWatermarkImage
	 * (java .awt.Graphics, int, int, int, int)
	 */
	public void drawWatermarkImage(Graphics graphics, Component c, int x,
			int y, int width, int height) {
		SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c);
		Graphics2D g2d = (Graphics2D) graphics.create();
		g2d.setComposite(LafWidgetUtilities
				.getAlphaComposite(c, 0.2f, graphics));
		g2d.setColor(skin.getWatermarkColorScheme().getWatermarkLightColor());
		g2d.fillRect(x, y, width, height);
		g2d.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.substance.api.watermark.SubstanceWatermark#previewWatermark
	 * (java.awt.Graphics, org.pushingpixels.substance.api.SubstanceSkin, int,
	 * int, int, int)
	 */
	@Override
	public void previewWatermark(Graphics g, SubstanceSkin skin, int x, int y,
			int width, int height) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.substance.watermark.SubstanceWatermark#updateWatermarkImage
	 * (org.pushingpixels.substance.skin.SubstanceSkin)
	 */
	public boolean updateWatermarkImage(SubstanceSkin skin) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.substance.api.trait.SubstanceTrait#getDisplayName()
	 */
	public String getDisplayName() {
		return "Null";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pushingpixels.substance.watermark.SubstanceWatermark#dispose()
	 */
	public void dispose() {
	}
}
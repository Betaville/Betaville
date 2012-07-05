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
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.painter.FractionBasedPainter;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

/**
 * Decoration painter with fraction-based stops and a color query associated
 * with each stop. This class allows creating multi-gradient decorations with
 * exact control over which color is used at every gradient control point.
 * 
 * @author Kirill Grouchnikov
 */
public class FractionBasedDecorationPainter extends FractionBasedPainter
		implements SubstanceDecorationPainter {
	/**
	 * Creates a new fraction-based decoration painter.
	 * 
	 * @param displayName
	 *            The display name of this painter.
	 * @param fractions
	 *            The fractions of this painter. Must be strictly increasing,
	 *            starting from 0.0 and ending at 1.0.
	 * @param colorQueries
	 *            The color queries of this painter. Must have the same size as
	 *            the fractions array, and all entries must be non-
	 *            <code>null</code>.
	 */
	public FractionBasedDecorationPainter(String displayName,
			float[] fractions, ColorSchemeSingleColorQuery[] colorQueries) {
		super(displayName, fractions, colorQueries);
	}

	@Override
	public void paintDecorationArea(Graphics2D graphics, Component comp,
			DecorationAreaType decorationAreaType, int width, int height,
			SubstanceSkin skin) {
		if ((decorationAreaType == DecorationAreaType.PRIMARY_TITLE_PANE)
				|| (decorationAreaType == DecorationAreaType.SECONDARY_TITLE_PANE)) {
			this.paintTitleBackground(graphics, comp, width, height, skin
					.getBackgroundColorScheme(decorationAreaType));
		} else {
			this.paintExtraBackground(graphics, SubstanceCoreUtilities
					.getHeaderParent(comp), comp, width, height, skin
					.getBackgroundColorScheme(decorationAreaType));
		}
	}

	/**
	 * Paints the title background.
	 * 
	 * @param graphics
	 *            Graphics context.
	 * @param comp
	 *            Component.
	 * @param width
	 *            Width.
	 * @param height
	 *            Height.
	 * @param scheme
	 *            Color scheme for painting the title background.
	 */
	private void paintTitleBackground(Graphics2D graphics, Component comp,
			int width, int height, SubstanceColorScheme scheme) {
		Graphics2D g2d = (Graphics2D) graphics.create();
		Color[] fillColors = new Color[this.fractions.length];
		for (int i = 0; i < this.fractions.length; i++) {
			ColorSchemeSingleColorQuery colorQuery = this.colorQueries[i];
			fillColors[i] = colorQuery.query(scheme);
		}

		MultipleGradientPaint gradient = new LinearGradientPaint(0, 0, 0,
				height, this.fractions, fillColors, CycleMethod.REPEAT);
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, width, height);

		g2d.dispose();
	}

	/**
	 * Paints the background of non-title decoration areas.
	 * 
	 * @param graphics
	 *            Graphics context.
	 * @param parent
	 *            Component ancestor for computing the correct offset of the
	 *            background painting.
	 * @param comp
	 *            Component.
	 * @param width
	 *            Width.
	 * @param height
	 *            Height.
	 * @param scheme
	 *            Color scheme for painting the title background.
	 */
	private void paintExtraBackground(Graphics2D graphics, Container parent,
			Component comp, int width, int height, SubstanceColorScheme scheme) {
		graphics.setColor(scheme.getMidColor());
		graphics.fillRect(0, 0, width, height);
	}
}

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

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;

/**
 * Implementation of {@link SubstanceDecorationPainter} that uses brushed metal
 * painting on decoration areas.
 * 
 * @author Kirill Grouchnikov
 * @since version 4.3
 */
public abstract class ImageWrapperDecorationPainter implements
		SubstanceDecorationPainter {
	/**
	 * Contains the original (not colorized) image of this painter.
	 */
	protected Image originalTile = null;

	/**
	 * The base decoration painter - the colorized image tiles are painted over
	 * the painting of this painter. Can be <code>null</code>.
	 */
	protected SubstanceDecorationPainter baseDecorationPainter;

	/**
	 * Map of colorized tiles.
	 */
	protected LinkedHashMap<String, Image> colorizedTileMap;

	/**
	 * Alpha channel for the texture image (colorized tiles applied on top of
	 * the {@link #baseDecorationPainter} painting).
	 */
	protected float textureAlpha;

	/**
	 * Creates a new image wrapper decoration painter.
	 */
	public ImageWrapperDecorationPainter() {
		this.textureAlpha = 0.3f;

		this.colorizedTileMap = new LinkedHashMap<String, Image>() {
			@Override
			protected boolean removeEldestEntry(Entry<String, Image> eldest) {
				return this.size() > 10;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @seeorg.pushingpixels.substance.painter.decoration.SubstanceDecorationPainter
	 * # paintDecorationArea(java.awt.Graphics2D, java.awt.Component,
	 * org.pushingpixels.substance.painter.decoration.DecorationAreaType, int,
	 * int, org.pushingpixels.substance.api.SubstanceSkin)
	 */
	public void paintDecorationArea(Graphics2D graphics, Component comp,
			DecorationAreaType decorationAreaType, int width, int height,
			SubstanceSkin skin) {
		if ((decorationAreaType == DecorationAreaType.PRIMARY_TITLE_PANE)
				|| (decorationAreaType == DecorationAreaType.SECONDARY_TITLE_PANE)) {
			this.paintTitleBackground(graphics, comp, decorationAreaType,
					width, height, skin);
		} else {
			this.paintExtraBackground(graphics, SubstanceCoreUtilities
					.getHeaderParent(comp), comp, decorationAreaType, width,
					height, skin);
		}
	}

	/**
	 * Paints the title background.
	 * 
	 * @param graphics
	 *            Graphics context.
	 * @param comp
	 *            Component.
	 * @param decorationAreaType
	 *            Decoration area type. Must not be <code>null</code>.
	 * @param width
	 *            Width.
	 * @param height
	 *            Height.
	 * @param skin
	 *            Skin for painting the title background.
	 */
	private void paintTitleBackground(Graphics2D graphics, Component comp,
			DecorationAreaType decorationAreaType, int width, int height,
			SubstanceSkin skin) {

		SubstanceColorScheme tileScheme = skin
				.getBackgroundColorScheme(decorationAreaType);
		if (this.baseDecorationPainter == null) {
			graphics.setColor(tileScheme.getMidColor());
			graphics.fillRect(0, 0, width, height);
		} else {
			this.baseDecorationPainter.paintDecorationArea(graphics, comp,
					decorationAreaType, width, height, skin);
		}

		Graphics2D temp = (Graphics2D) graphics.create();
		this.tileArea(temp, comp, tileScheme, 0, 0, 0, 0, width, height);
		temp.dispose();
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
	 * @param decorationAreaType
	 *            Decoration area type. Must not be <code>null</code>.
	 * @param width
	 *            Width.
	 * @param height
	 *            Height.
	 * @param skin
	 *            Skin for painting the background of non-title decoration
	 *            areas.
	 */
	private void paintExtraBackground(Graphics2D graphics, Container parent,
			Component comp, DecorationAreaType decorationAreaType, int width,
			int height, SubstanceSkin skin) {

		JRootPane rootPane = SwingUtilities.getRootPane(parent);
		// fix for bug 234 - Window doesn't have a root pane.
		int dx = 0;
		int dy = 0;
		JComponent titlePane = null;
		if (rootPane != null) {
			titlePane = SubstanceCoreUtilities.getTitlePane(rootPane);

			if (titlePane != null) {
				if (comp.isShowing() && titlePane.isShowing()) {
					dx += (comp.getLocationOnScreen().x - titlePane
							.getLocationOnScreen().x);
					dy += (comp.getLocationOnScreen().y - titlePane
							.getLocationOnScreen().y);
				} else {
					// have to traverse the hierarchy
					Component c = comp;
					dx = 0;
					dy = 0;
					while (c != rootPane) {
						dx += c.getX();
						dy += c.getY();
						c = c.getParent();
					}
					c = titlePane;
					while (c != rootPane) {
						dx -= c.getX();
						dy -= c.getY();
						c = c.getParent();
					}
				}
			}
		}

		SubstanceColorScheme tileScheme = skin
				.getBackgroundColorScheme(decorationAreaType);
		if (this.baseDecorationPainter != null) {
			this.baseDecorationPainter.paintDecorationArea(graphics, comp,
					decorationAreaType, width, height, skin);
		} else {
			graphics.setColor(tileScheme.getMidColor());
			graphics.fillRect(0, 0, width, height);
		}
		Graphics2D temp = (Graphics2D) graphics.create();
		this.tileArea(temp, comp, tileScheme, dx, dy, 0, 0, width, height);
		temp.dispose();
	}

	/**
	 * Tiles the specified area with colorized version of the image tile. This
	 * is called after the {@link #baseDecorationPainter} has painted the area.
	 * This method should respect the current {@link #textureAlpha} value.
	 * 
	 * @param g
	 *            Graphic context.
	 * @param comp
	 *            Component.
	 * @param tileScheme
	 *            Scheme for the tile colorization.
	 * @param offsetTextureX
	 *            X offset for the tiling.
	 * @param offsetTextureY
	 *            Y offset for the tiling.
	 * @param x
	 *            X coordinate of the tiling region.
	 * @param y
	 *            Y coordinate of the tiling region.
	 * @param width
	 *            Width of the tiling region.
	 * @param height
	 *            Height of the tiling region.
	 */
	protected void tileArea(Graphics2D g, Component comp,
			SubstanceColorScheme tileScheme, int offsetTextureX,
			int offsetTextureY, int x, int y, int width, int height) {

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setComposite(LafWidgetUtilities.getAlphaComposite(comp,
				this.textureAlpha, g));

		Image colorizedTile = this.getColorizedTile(tileScheme);
		int tileWidth = colorizedTile.getWidth(null);
		int tileHeight = colorizedTile.getHeight(null);

		offsetTextureX = offsetTextureX % tileWidth;
		offsetTextureY = offsetTextureY % tileHeight;
		int currTileTop = -offsetTextureY;
		do {
			int currTileLeft = -offsetTextureX;
			do {
				graphics.drawImage(colorizedTile, currTileLeft, currTileTop,
						null);
				currTileLeft += tileWidth;
			} while (currTileLeft < width);
			currTileTop += tileHeight;
		} while (currTileTop < height);

		graphics.dispose();
	}

	/**
	 * Sets the base decoration painter.
	 * 
	 * @param baseDecorationPainter
	 *            Base decoration painter.
	 */
	public void setBaseDecorationPainter(
			SubstanceDecorationPainter baseDecorationPainter) {
		this.baseDecorationPainter = baseDecorationPainter;
	}

	/**
	 * Sets the alpha channel for the image texture.
	 * 
	 * @param textureAlpha
	 *            Alpha channel for the image texture.
	 */
	public void setTextureAlpha(float textureAlpha) {
		this.textureAlpha = textureAlpha;
	}

	/**
	 * Returns a colorized image tile.
	 * 
	 * @param scheme
	 *            Color scheme for the colorization.
	 * @return Colorized tile.
	 */
	protected Image getColorizedTile(SubstanceColorScheme scheme) {
		Image result = this.colorizedTileMap.get(scheme.getDisplayName());
		if (result == null) {
			BufferedImage tileBi = new BufferedImage(this.originalTile
					.getWidth(null), this.originalTile.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			tileBi.getGraphics().drawImage(this.originalTile, 0, 0, null);
			result = SubstanceImageCreator.getColorSchemeImage(tileBi, scheme,
					0.0f);
			this.colorizedTileMap.put(scheme.getDisplayName(), result);
		}
		return result;
	}
}

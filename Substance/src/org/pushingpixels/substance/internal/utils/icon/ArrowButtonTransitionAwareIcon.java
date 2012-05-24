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
package org.pushingpixels.substance.internal.utils.icon;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.SwingConstants;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

/**
 * Transition aware implementation of arrow button icons. Used for implementing
 * icons of scroll bar buttons, combobox buttons, menus and more.
 * 
 * @author Kirill Grouchnikov
 */
@TransitionAware
public class ArrowButtonTransitionAwareIcon implements Icon {
	/**
	 * Icon cache to speed up the subsequent icon painting. The basic assumption
	 * is that the {@link #delegate} returns an icon that paints the same for
	 * the same parameters.
	 */
	private static LazyResettableHashMap<Icon> iconMap = new LazyResettableHashMap<Icon>(
			"ButtonArrowTransitionAwareIcon");

	/**
	 * Arrow icon orientation. Must be one of {@link SwingConstants#NORTH},
	 * {@link SwingConstants#SOUTH}, {@link SwingConstants#EAST} or
	 * {@link SwingConstants#WEST}.
	 */
	private int orientation;

	/**
	 * Icon width.
	 */
	protected int iconWidth;

	/**
	 * Icon height.
	 */
	protected int iconHeight;

	/**
	 * Delegate to compute the actual icons.
	 */
	protected TransitionAwareIcon.Delegate delegate;

	protected JComponent component;

	private TransitionAwareIcon.TransitionAwareUIDelegate transitionAwareUIDelegate;

	public ArrowButtonTransitionAwareIcon(final AbstractButton button,
			int orientation) {
		this(button, new TransitionAwareIcon.TransitionAwareUIDelegate() {
			@Override
			public TransitionAwareUI getTransitionAwareUI() {
				return (TransitionAwareUI) button.getUI();
			}
		}, orientation);
	}

	/**
	 * Creates an arrow icon.
	 * 
	 * @param component
	 *            Arrow button.
	 * @param orientation
	 *            Arrow icon orientation.
	 */
	public ArrowButtonTransitionAwareIcon(
			final JComponent component,
			TransitionAwareIcon.TransitionAwareUIDelegate transitionAwareUIDelegate,
			final int orientation) {
		this.component = component;
		this.transitionAwareUIDelegate = transitionAwareUIDelegate;
		this.orientation = orientation;
		this.delegate = new TransitionAwareIcon.Delegate() {
			@Override
			public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
				int fontSize = SubstanceSizeUtils
						.getComponentFontSize(component);
				return SubstanceImageCreator.getArrowIcon(fontSize,
						orientation, scheme);
			}
		};

		this.iconWidth = this.delegate.getColorSchemeIcon(
				SubstanceColorSchemeUtilities.getColorScheme(component,
						ComponentState.ENABLED)).getIconWidth();
		this.iconHeight = this.delegate.getColorSchemeIcon(
				SubstanceColorSchemeUtilities.getColorScheme(component,
						ComponentState.ENABLED)).getIconHeight();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		this.getIconToPaint().paintIcon(c, g, x, y);
	}

	/**
	 * Returns the icon to be painted for the current state of the button.
	 * 
	 * @param button
	 *            Arrow button.
	 * @return Icon to be painted.
	 */
	private Icon getIconToPaint() {
		boolean isMenu = (this.component instanceof JMenu);
		StateTransitionTracker stateTransitionTracker = this.transitionAwareUIDelegate
				.getTransitionAwareUI().getTransitionTracker();
		StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker
				.getModelStateInfo();
		Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = isMenu ? modelStateInfo
				.getStateNoSelectionContributionMap()
				: modelStateInfo.getStateContributionMap();
		ComponentState currState = isMenu ? modelStateInfo
				.getCurrModelStateNoSelection() : modelStateInfo
				.getCurrModelState();

		// Use HIGHLIGHT for rollover menus (arrow icons)
		// and MARK for the rest
		ColorSchemeAssociationKind baseAssociationKind = isMenu
				&& currState.isFacetActive(ComponentStateFacet.ROLLOVER) ? ColorSchemeAssociationKind.HIGHLIGHT
				: ColorSchemeAssociationKind.MARK;
		SubstanceColorScheme baseScheme = SubstanceColorSchemeUtilities
				.getColorScheme(this.component, baseAssociationKind, currState);
		float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(
				this.component, currState);

		HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(this.component
				.getClass().getName(), this.orientation, SubstanceSizeUtils
				.getComponentFontSize(this.component), baseScheme
				.getDisplayName(), baseAlpha);
		Icon layerBase = iconMap.get(keyBase);
		if (layerBase == null) {
			Icon baseFullOpacity = this.delegate.getColorSchemeIcon(baseScheme);
			if (baseAlpha == 1.0f) {
				layerBase = baseFullOpacity;
				iconMap.put(keyBase, layerBase);
			} else {
				BufferedImage baseImage = SubstanceCoreUtilities.getBlankImage(
						baseFullOpacity.getIconWidth(), baseFullOpacity
								.getIconHeight());
				Graphics2D g2base = baseImage.createGraphics();
				g2base.setComposite(AlphaComposite.SrcOver.derive(baseAlpha));
				baseFullOpacity.paintIcon(this.component, g2base, 0, 0);
				g2base.dispose();
				layerBase = new ImageIcon(baseImage);
				iconMap.put(keyBase, layerBase);
			}
		}
		// System.out.println("Contribution map in painting");
		// for (Map.Entry<ComponentState,
		// StateTransitionTracker.StateContributionInfo> existing : activeStates
		// .entrySet()) {
		// System.out.println("\t" + existing.getKey() + " in ["
		// + existing.getValue().start + ":" + existing.getValue().end
		// + "] : " + existing.getValue().curr);
		// }

		if (currState.isDisabled() || (activeStates.size() == 1)) {
			return layerBase;
		}

		BufferedImage result = SubstanceCoreUtilities.getBlankImage(layerBase
				.getIconWidth(), layerBase.getIconHeight());
		Graphics2D g2d = result.createGraphics();
		// draw the base layer
		// System.out.println("Painting currState " + currState + ":" +
		// baseAlpha);
		layerBase.paintIcon(this.component, g2d, 0, 0);

		// draw the other active layers
		for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates
				.entrySet()) {
			ComponentState activeState = activeEntry.getKey();
			// System.out.println("Painting state " + activeState + "[curr is "
			// + currState + "] with " + activeEntry.getValue());
			if (activeState == currState)
				continue;

			float stateContribution = activeEntry.getValue().getContribution();
			// System.out.println("Painting activeState "
			// + activeState
			// + ":"
			// + stateContribution
			// + "*"
			// + SubstanceColorSchemeUtilities.getAlpha(this.component,
			// activeState));
			if (stateContribution > 0.0f) {
				g2d.setComposite(AlphaComposite.SrcOver
						.derive(stateContribution));

				ColorSchemeAssociationKind associationKind = isMenu
						&& activeState
								.isFacetActive(ComponentStateFacet.ROLLOVER) ? ColorSchemeAssociationKind.HIGHLIGHT
						: ColorSchemeAssociationKind.MARK;
				SubstanceColorScheme scheme = SubstanceColorSchemeUtilities
						.getColorScheme(this.component, associationKind,
								activeState);
				float alpha = SubstanceColorSchemeUtilities.getAlpha(
						this.component, activeState);

				HashMapKey key = SubstanceCoreUtilities
						.getHashKey(this.component.getClass().getName(),
								this.orientation, SubstanceSizeUtils
										.getComponentFontSize(this.component),
								scheme.getDisplayName(), alpha);
				Icon layer = iconMap.get(key);
				if (layer == null) {
					Icon fullOpacity = this.delegate.getColorSchemeIcon(scheme);
					if (alpha == 1.0f) {
						layer = fullOpacity;
						iconMap.put(key, layer);
					} else {
						BufferedImage image = SubstanceCoreUtilities
								.getBlankImage(fullOpacity.getIconWidth(),
										fullOpacity.getIconHeight());
						Graphics2D g2layer = image.createGraphics();
						g2layer.setComposite(AlphaComposite.SrcOver
								.derive(alpha));
						fullOpacity.paintIcon(this.component, g2layer, 0, 0);
						g2layer.dispose();
						layer = new ImageIcon(image);
						iconMap.put(key, layer);
					}
				}
				layer.paintIcon(this.component, g2d, 0, 0);
			}
		}
		g2d.dispose();
		return new ImageIcon(result);
		//
		//
		// SubstanceColorScheme currScheme = SubstanceColorSchemeUtilities
		// .getColorScheme(this.component, currAssociationKind, currState);
		//
		// SubstanceColorScheme prevScheme = currScheme;
		//
		// // Use HIGHLIGHT for rollover menus (arrow icons)
		// // and MARK for the rest
		// if (prevState != currState) {
		// ColorSchemeAssociationKind prevAssociationKind = (this.component
		// instanceof JMenu)
		// && prevState.isFacetActive(AnimationFacet.ROLLOVER) ?
		// ColorSchemeAssociationKind.HIGHLIGHT
		// : ColorSchemeAssociationKind.MARK;
		// prevScheme = SubstanceColorSchemeUtilities.getColorScheme(
		// this.component, prevAssociationKind, prevState);
		// }
		// cyclePos = stateTransitionTracker.getModelTransitionPosition();
		// float currAlpha = SubstanceColorSchemeUtilities.getAlpha(
		// this.component, currState);
		// float prevAlpha = SubstanceColorSchemeUtilities.getAlpha(
		// this.component, prevState);
		//
		// HashMapKey key = SubstanceCoreUtilities.getHashKey(this.component
		// .getClass().getName(), this.orientation, SubstanceSizeUtils
		// .getComponentFontSize(this.component), currScheme
		// .getDisplayName(), prevScheme.getDisplayName(), currAlpha,
		// prevAlpha, cyclePos);
		// Icon result = iconMap.get(key);
		// if (result == null) {
		// Icon icon = this.delegate.getColorSchemeIcon(currScheme);
		// Icon prevIcon = this.delegate.getColorSchemeIcon(prevScheme);
		//
		// BufferedImage temp = SubstanceCoreUtilities.getBlankImage(icon
		// .getIconWidth(), icon.getIconHeight());
		// Graphics2D g2d = temp.createGraphics();
		//
		// if (currScheme == prevScheme) {
		// // same scheme - can paint just the current icon, no matter
		// // what the cycle position is.
		// g2d.setComposite(AlphaComposite.getInstance(
		// AlphaComposite.SRC_OVER, currAlpha));
		// icon.paintIcon(this.component, g2d, 0, 0);
		// } else {
		// // make optimizations for limit values of the cycle position.
		// if (cyclePos < 1.0f) {
		// g2d.setComposite(AlphaComposite.SrcOver.derive(prevAlpha));
		// prevIcon.paintIcon(this.component, g2d, 0, 0);
		// }
		// if (cyclePos > 0.0f) {
		// g2d.setComposite(AlphaComposite.SrcOver.derive(currAlpha
		// * cyclePos));
		// icon.paintIcon(this.component, g2d, 0, 0);
		// }
		// }
		//
		// result = new ImageIcon(temp);
		// iconMap.put(key, result);
		// g2d.dispose();
		// }
		//
		// return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return this.iconHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return this.iconWidth;
	}
}

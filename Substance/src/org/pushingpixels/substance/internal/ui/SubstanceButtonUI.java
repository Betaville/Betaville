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
package org.pushingpixels.substance.internal.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.*;
import org.pushingpixels.substance.internal.utils.*;
import org.pushingpixels.substance.internal.utils.border.SubstanceButtonBorder;
import org.pushingpixels.substance.internal.utils.icon.GlowingIcon;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

/**
 * UI for buttons in <b>Substance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceButtonUI extends BasicButtonUI implements
		TransitionAwareUI, ModificationAwareUI {
	/**
	 * Property used during the button shaper switch.
	 */
	public static final String BORDER_COMPUTED = "substancelaf.buttonbordercomputed";

	/**
	 * Property used during the button shaper switch.
	 */
	public static final String BORDER_COMPUTING = "substancelaf.buttonbordercomputing";

	/**
	 * Property used to store the original (pre-<b>Substance</b>) button border.
	 */
	public static final String BORDER_ORIGINAL = "substancelaf.buttonborderoriginal";

	/**
	 * Property used to store the original button icon.
	 */
	public static final String ICON_ORIGINAL = "substancelaf.buttoniconoriginal";

	/**
	 * Property used to store the original (pre-<b>Substance</b>) button
	 * opacity.
	 */
	public static final String OPACITY_ORIGINAL = "substancelaf.buttonopacityoriginal";

	/**
	 * Property used to lock the original (pre-<b>Substance</b>) button opacity.
	 */
	public static final String LOCK_OPACITY = "substancelaf.lockopacity";

	/**
	 * Internal property used to mark close buttons on title panes.
	 */
	public static final String IS_TITLE_CLOSE_BUTTON = "substancelaf.internal.isTitleCloseButton";

	/**
	 * Painting delegate.
	 */
	private ButtonBackgroundDelegate delegate;

	/**
	 * The matching glowing icon. Is used only when
	 * {@link AnimationConfigurationManager#isAnimationAllowed(AnimationFacet, Component)}
	 * returns true on {@link AnimationFacet#ICON_GLOW}.
	 */
	protected GlowingIcon glowingIcon;

	/**
	 * Property change listener. Listens on changes to the
	 * {@link SubstanceLookAndFeel#BUTTON_SHAPER_PROPERTY} property and
	 * {@link AbstractButton#MODEL_CHANGED_PROPERTY} property.
	 */
	protected PropertyChangeListener substancePropertyListener;

	/**
	 * Tracker for visual state transitions.
	 */
	protected ButtonVisualStateTracker substanceVisualStateTracker;

	protected AbstractButton button;

	private Timeline modifiedTimeline;

	private Rectangle viewRect = new Rectangle();

	private Rectangle iconRect = new Rectangle();

	private Rectangle textRect = new Rectangle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstanceButtonUI((AbstractButton) comp);
	}

	/**
	 * Simple constructor.
	 */
	public SubstanceButtonUI(AbstractButton button) {
		this.button = button;
		this.delegate = new ButtonBackgroundDelegate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.plaf.basic.BasicButtonUI#installDefaults(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		if (b.getClientProperty(SubstanceButtonUI.BORDER_ORIGINAL) == null)
			b.putClientProperty(SubstanceButtonUI.BORDER_ORIGINAL, b
					.getBorder());

		SubstanceButtonShaper shaper = SubstanceCoreUtilities
				.getButtonShaper(b);

		if (b.getClientProperty(SubstanceButtonUI.BORDER_COMPUTED) == null) {
			b.setBorder(shaper.getButtonBorder(b));
		} else {
			Border currBorder = b.getBorder();
			if (!(currBorder instanceof SubstanceButtonBorder)) {
				b.setBorder(shaper.getButtonBorder(b));
			} else {
				SubstanceButtonBorder sbCurrBorder = (SubstanceButtonBorder) currBorder;
				if (shaper.getClass() != sbCurrBorder.getButtonShaperClass())
					b.setBorder(shaper.getButtonBorder(b));
			}
		}
		b.putClientProperty(SubstanceButtonUI.OPACITY_ORIGINAL, b.isOpaque());
		b.setOpaque(false);

		b.setRolloverEnabled(true);

		LookAndFeel.installProperty(b, "iconTextGap", SubstanceSizeUtils
				.getTextIconGap(SubstanceSizeUtils.getComponentFontSize(b)));

		if (Boolean.TRUE.equals(b
				.getClientProperty(SubstanceLookAndFeel.WINDOW_MODIFIED))) {
			trackModificationFlag();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicButtonUI#uninstallDefaults(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);

		b.setBorder((Border) b
				.getClientProperty(SubstanceButtonUI.BORDER_ORIGINAL));
		b.setOpaque((Boolean) b
				.getClientProperty(SubstanceButtonUI.OPACITY_ORIGINAL));
		Icon origIcon = (Icon) b
				.getClientProperty(SubstanceButtonUI.ICON_ORIGINAL);
		if (origIcon != null)
			b.setIcon(origIcon);
		b.putClientProperty(SubstanceButtonUI.OPACITY_ORIGINAL, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicButtonUI#createButtonListener(javax.swing
	 * .AbstractButton)
	 */
	@Override
	protected BasicButtonListener createButtonListener(AbstractButton b) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.plaf.basic.BasicButtonUI#installListeners(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void installListeners(final AbstractButton b) {
		super.installListeners(b);

		this.substanceVisualStateTracker = new ButtonVisualStateTracker();
		this.substanceVisualStateTracker.installListeners(b, true);

		this.trackGlowingIcon();

		// this.substanceButtonListener = new RolloverButtonListener(b);
		// b.addMouseListener(this.substanceButtonListener);
		// b.addMouseMotionListener(this.substanceButtonListener);
		// b.addFocusListener(this.substanceButtonListener);
		// b.addPropertyChangeListener(this.substanceButtonListener);
		// b.addChangeListener(this.substanceButtonListener);

		this.substancePropertyListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (AbstractButton.ICON_CHANGED_PROPERTY.equals(evt
						.getPropertyName())) {
					trackGlowingIcon();
				}

				if (SubstanceLookAndFeel.WINDOW_MODIFIED.equals(evt
						.getPropertyName())) {
					boolean newValue = (Boolean) evt.getNewValue();
					if (newValue) {
						trackModificationFlag();
					} else {
						if (modifiedTimeline != null) {
							modifiedTimeline.cancel();
						}
					}
				}
			}
		};
		b.addPropertyChangeListener(this.substancePropertyListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.plaf.basic.BasicButtonUI#uninstallListeners(javax.swing.
	 * AbstractButton)
	 */
	@Override
	protected void uninstallListeners(AbstractButton b) {
		this.substanceVisualStateTracker.uninstallListeners(b);
		this.substanceVisualStateTracker = null;

		b.removePropertyChangeListener(this.substancePropertyListener);
		this.substancePropertyListener = null;

		super.uninstallListeners(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		if (!SubstanceLookAndFeel.isCurrentLookAndFeel())
			return;

		final AbstractButton b = (AbstractButton) c;

		if (b instanceof JButton) {
			JButton jb = (JButton) b;
			if (RootPaneDefaultButtonTracker.isPulsating(jb)) {
				RootPaneDefaultButtonTracker.update(jb);
			}
		}

		FontMetrics fm = g.getFontMetrics();

		Insets i = c.getInsets();

		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = b.getWidth() - (i.right + viewRect.x);
		viewRect.height = b.getHeight() - (i.bottom + viewRect.y);

		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

		Font f = c.getFont();

		// layout the text and icon
		String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(), b
				.getIcon(), b.getVerticalAlignment(), b
				.getHorizontalAlignment(), b.getVerticalTextPosition(), b
				.getHorizontalTextPosition(), viewRect, iconRect, textRect, b
				.getText() == null ? 0 : b.getIconTextGap());

		Graphics2D g2d = (Graphics2D) g.create();

		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		g2d.setFont(f);

		this.delegate.updateBackground(g2d, b);

		if (v != null) {
			v.paint(g2d, textRect);
		} else {
			this.paintButtonText(g2d, b, textRect, text);
		}

		// Paint the Icon
		if (b.getIcon() != null) {
			paintIcon(g2d, c, iconRect);
		}

		if (b.isFocusPainted()) {
			SubstanceCoreUtilities.paintFocus(g, b, b, this, null, textRect,
					1.0f, SubstanceSizeUtils
							.getFocusRingPadding(SubstanceSizeUtils
									.getComponentFontSize(b)));
		}

		// g2d.setColor(Color.red);
		// g2d.draw(iconRect);
		// g2d.draw(viewRect);
		// g2d.draw(textRect);

		// if (isPartOfCompositeControl) {
		// g.drawImage(offscreen, 0, 0, null);
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.ComponentUI#getPreferredSize(javax.swing.JComponent)
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		AbstractButton button = (AbstractButton) c;
		SubstanceButtonShaper shaper = SubstanceCoreUtilities
				.getButtonShaper(button);

		// fix for defect 263
		Dimension superPref = super.getPreferredSize(button);
		if (superPref == null)
			return null;

		if (shaper == null)
			return superPref;

		Dimension result = shaper.getPreferredSize(button, superPref);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#contains(javax.swing.JComponent, int,
	 * int)
	 */
	@Override
	public boolean contains(JComponent c, int x, int y) {
		return ButtonBackgroundDelegate.contains((JButton) c, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintIcon(java.awt.Graphics,
	 * javax.swing.JComponent, java.awt.Rectangle)
	 */
	@Override
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		Graphics2D graphics = (Graphics2D) g.create();

		// graphics.setColor(Color.red);
		// graphics.fill(iconRect);

		// We have three types of icons:
		// 1. The original button icon
		// 2. The themed version of 1.
		// 3. The glowing version of 1.
		AbstractButton b = (AbstractButton) c;
		Icon originalIcon = SubstanceCoreUtilities.getOriginalIcon(b, b
				.getIcon());
		Icon themedIcon = (!(b instanceof JRadioButton)
				&& !(b instanceof JCheckBox) && SubstanceCoreUtilities
				.useThemedDefaultIcon(b)) ? SubstanceCoreUtilities
				.getThemedIcon(b, originalIcon) : originalIcon;

		graphics.setComposite(LafWidgetUtilities.getAlphaComposite(b, g));
		float activeAmount = this.substanceVisualStateTracker
				.getStateTransitionTracker().getActiveStrength();
		if (activeAmount >= 0.0f) {
			if (AnimationConfigurationManager.getInstance().isAnimationAllowed(
					AnimationFacet.ICON_GLOW, b)
					&& this.substanceVisualStateTracker
							.getStateTransitionTracker().getIconGlowTracker()
							.isPlaying()) {
				this.glowingIcon.paintIcon(b, graphics, iconRect.x, iconRect.y);
			} else {
				themedIcon.paintIcon(b, graphics, iconRect.x, iconRect.y);
				graphics.setComposite(LafWidgetUtilities.getAlphaComposite(b,
						activeAmount, g));
				originalIcon.paintIcon(b, graphics, iconRect.x, iconRect.y);
			}
		} else {
			originalIcon.paintIcon(b, graphics, iconRect.x, iconRect.y);
		}
		graphics.dispose();
	}

	/**
	 * Paints the text.
	 * 
	 * @param g
	 *            Graphic context
	 * @param button
	 *            Button
	 * @param textRect
	 *            Text rectangle
	 * @param text
	 *            Text to paint
	 */
	protected void paintButtonText(Graphics g, AbstractButton button,
			Rectangle textRect, String text) {
		SubstanceTextUtilities.paintText(g, button, textRect, text, button
				.getDisplayedMnemonicIndex());
	}

	/**
	 * Tracks possible usage of glowing icon.
	 * 
	 * @param b
	 *            Button.
	 */
	protected void trackGlowingIcon() {
		Icon currIcon = this.button.getIcon();
		if (currIcon instanceof GlowingIcon)
			return;
		if (currIcon == null)
			return;
		this.glowingIcon = new GlowingIcon(currIcon,
				this.substanceVisualStateTracker.getStateTransitionTracker()
						.getIconGlowTracker());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		this.paint(g, c);
	}

	@Override
	public boolean isInside(MouseEvent me) {
		return this.contains(this.button, me.getX(), me.getY());
	}

	@Override
	public StateTransitionTracker getTransitionTracker() {
		return this.substanceVisualStateTracker.getStateTransitionTracker();
	}

	private void trackModificationFlag() {
		this.modifiedTimeline = new Timeline(this.button);
		AnimationConfigurationManager.getInstance().configureModifiedTimeline(
				modifiedTimeline);
		this.modifiedTimeline
				.addCallback(new SwingRepaintCallback(this.button));
		this.modifiedTimeline.playLoop(RepeatBehavior.REVERSE);
	}

	@Override
	public Timeline getModificationTimeline() {
		return this.modifiedTimeline;
	}
}

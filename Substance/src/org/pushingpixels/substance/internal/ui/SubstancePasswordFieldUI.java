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
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.*;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.*;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;

/**
 * UI for password fields in <b>Substance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstancePasswordFieldUI extends BasicPasswordFieldUI implements
		TransitionAwareUI {
	protected StateTransitionTracker stateTransitionTracker;

	/**
	 * The associated password field.
	 */
	protected JPasswordField passwordField;

	/**
	 * Property change listener.
	 */
	protected PropertyChangeListener substancePropertyChangeListener;

	/**
	 * Listener for transition animations.
	 */
	private RolloverTextControlListener substanceRolloverListener;

	/**
	 * Surrogate button model for tracking the state transitions.
	 */
	private ButtonModel transitionModel;

	// private FocusListener substanceFocusListener;

	/**
	 * Custom password view.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class SubstancePasswordView extends FieldView {
		/**
		 * The associated password field.
		 */
		private JPasswordField field;

		/**
		 * Simple constructor.
		 * 
		 * @param field
		 *            The associated password field.
		 * @param element
		 *            The element
		 */
		public SubstancePasswordView(JPasswordField field, Element element) {
			super(element);
			this.field = field;
		}

		/**
		 * Draws the echo character(s) for a single password field character.
		 * The number of echo characters is defined by
		 * {@link SubstanceLookAndFeel#PASSWORD_ECHO_PER_CHAR} client property.
		 * 
		 * @param g
		 *            Graphics context
		 * @param x
		 *            X coordinate of the first echo character to draw.
		 * @param y
		 *            Y coordinate of the first echo character to draw.
		 * @param c
		 *            Password field.
		 * @param isSelected
		 *            Indicates whether the password field character is
		 *            selected.
		 * @return The X location of the next echo character.
		 * @see SubstanceLookAndFeel#PASSWORD_ECHO_PER_CHAR
		 */
		protected int drawEchoCharacter(Graphics g, int x, int y, char c,
				boolean isSelected) {
			Container container = this.getContainer();

			Graphics2D graphics = (Graphics2D) g;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			JPasswordField field = (JPasswordField) container;

			int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
			int dotDiameter = SubstanceSizeUtils
					.getPasswordDotDiameter(fontSize);
			int dotGap = SubstanceSizeUtils.getPasswordDotGap(fontSize);
			ComponentState state = // isSelected ? ComponentState.SELECTED
			(field.isEnabled() ? ComponentState.ENABLED
					: ComponentState.DISABLED_UNSELECTED);
			SubstanceColorScheme scheme = SubstanceColorSchemeUtilities
					.getColorScheme(field, state);
			Color topColor = isSelected ? scheme.getSelectionForegroundColor()
					: SubstanceColorUtilities.getForegroundColor(scheme);
			Color bottomColor = topColor.brighter();
			graphics.setPaint(new GradientPaint(x, y - dotDiameter, topColor,
					x, y, bottomColor));
			int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(field);
			for (int i = 0; i < echoPerChar; i++) {
				graphics.fillOval(x + dotGap / 2, y - dotDiameter, dotDiameter,
						dotDiameter);
				x += (dotDiameter + dotGap);
			}
			return x;
		}

		/**
		 * Returns the advance of a single password field character. The advance
		 * is the pixel distance between first echo characters of consecutive
		 * password field characters. The
		 * {@link SubstanceLookAndFeel#PASSWORD_ECHO_PER_CHAR} can be used to
		 * specify that more than one echo character is used for each password
		 * field character.
		 * 
		 * @return The advance of a single password field character
		 */
		protected int getEchoCharAdvance() {
			int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
			int dotDiameter = SubstanceSizeUtils
					.getPasswordDotDiameter(fontSize);
			int dotGap = SubstanceSizeUtils.getPasswordDotGap(fontSize);
			int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(field);
			return echoPerChar * (dotDiameter + dotGap);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.PlainView#drawSelectedText(java.awt.Graphics,
		 * int, int, int, int)
		 */
		@Override
		protected int drawSelectedText(Graphics g, final int x, final int y,
				int p0, int p1) throws BadLocationException {
			Container c = getContainer();
			if (c instanceof JPasswordField) {
				JPasswordField f = (JPasswordField) c;
				if (!f.echoCharIsSet()) {
					return super.drawSelectedText(g, x, y, p0, p1);
				}
				int n = p1 - p0;
				char echoChar = f.getEchoChar();
				int currPos = x;
				for (int i = 0; i < n; i++) {
					currPos = drawEchoCharacter(g, currPos, y, echoChar, true);
				}
				return x + n * getEchoCharAdvance();
			}
			return x;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.PlainView#drawUnselectedText(java.awt.Graphics,
		 * int, int, int, int)
		 */
		@Override
		protected int drawUnselectedText(Graphics g, final int x, final int y,
				int p0, int p1) throws BadLocationException {
			Container c = getContainer();
			if (c instanceof JPasswordField) {
				JPasswordField f = (JPasswordField) c;
				if (!f.echoCharIsSet()) {
					return super.drawUnselectedText(g, x, y, p0, p1);
				}
				int n = p1 - p0;
				char echoChar = f.getEchoChar();
				int currPos = x;
				for (int i = 0; i < n; i++) {
					currPos = drawEchoCharacter(g, currPos, y, echoChar, false);
				}
				return x + n * getEchoCharAdvance();
			}
			return x;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.View#modelToView(int, java.awt.Shape,
		 * javax.swing.text.Position.Bias)
		 */
		@Override
		public Shape modelToView(int pos, Shape a, Position.Bias b)
				throws BadLocationException {
			Container c = this.getContainer();
			if (c instanceof JPasswordField) {
				JPasswordField f = (JPasswordField) c;
				if (!f.echoCharIsSet()) {
					return super.modelToView(pos, a, b);
				}

				Rectangle alloc = this.adjustAllocation(a).getBounds();
				int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(f);
				int fontSize = SubstanceSizeUtils
						.getComponentFontSize(this.field);
				int dotWidth = SubstanceSizeUtils
						.getPasswordDotDiameter(fontSize)
						+ SubstanceSizeUtils.getPasswordDotGap(fontSize);

				int dx = (pos - this.getStartOffset()) * echoPerChar * dotWidth;
				alloc.x += dx;
				alloc.width = 1;
				return alloc;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.View#viewToModel(float, float, java.awt.Shape,
		 * javax.swing.text.Position.Bias[])
		 */
		@Override
		public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
			bias[0] = Position.Bias.Forward;
			int n = 0;
			Container c = this.getContainer();
			if (c instanceof JPasswordField) {
				JPasswordField f = (JPasswordField) c;
				if (!f.echoCharIsSet()) {
					return super.viewToModel(fx, fy, a, bias);
				}
				a = this.adjustAllocation(a);
				Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a
						.getBounds();
				int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(f);
				int fontSize = SubstanceSizeUtils
						.getComponentFontSize(this.field);
				int dotWidth = SubstanceSizeUtils
						.getPasswordDotDiameter(fontSize)
						+ SubstanceSizeUtils.getPasswordDotGap(fontSize);
				n = ((int) fx - alloc.x) / (echoPerChar * dotWidth);
				if (n < 0) {
					n = 0;
				} else {
					if (n > (this.getStartOffset() + this.getDocument()
							.getLength())) {
						n = this.getDocument().getLength()
								- this.getStartOffset();
					}
				}
			}
			return this.getStartOffset() + n;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.View#getPreferredSpan(int)
		 */
		@Override
		public float getPreferredSpan(int axis) {
			switch (axis) {
			case View.X_AXIS:
				Container c = this.getContainer();
				if (c instanceof JPasswordField) {
					JPasswordField f = (JPasswordField) c;
					if (f.echoCharIsSet()) {
						int echoPerChar = SubstanceCoreUtilities
								.getEchoPerChar(f);
						int fontSize = SubstanceSizeUtils
								.getComponentFontSize(this.field);
						int dotWidth = SubstanceSizeUtils
								.getPasswordDotDiameter(fontSize)
								+ SubstanceSizeUtils
										.getPasswordDotGap(fontSize);
						return echoPerChar * dotWidth
								* this.getDocument().getLength();
					}
				}
			}
			return super.getPreferredSpan(axis);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstancePasswordFieldUI(comp);
	}

	/**
	 * Creates the UI delegate for the specified component (password field).
	 * 
	 * @param c
	 *            Component.
	 */
	public SubstancePasswordFieldUI(JComponent c) {
		super();
		this.passwordField = (JPasswordField) c;

		this.transitionModel = new DefaultButtonModel();
		this.transitionModel.setArmed(false);
		this.transitionModel.setSelected(false);
		this.transitionModel.setPressed(false);
		this.transitionModel.setRollover(false);
		this.transitionModel.setEnabled(this.passwordField.isEnabled());

		this.stateTransitionTracker = new StateTransitionTracker(
				this.passwordField, this.transitionModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
	 */
	@Override
	public View create(Element elem) {
		return new SubstancePasswordView(this.passwordField, elem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicTextUI#installListeners()
	 */
	@Override
	protected void installListeners() {
		super.installListeners();

		this.substanceRolloverListener = new RolloverTextControlListener(
				this.passwordField, this, this.transitionModel);
		this.substanceRolloverListener.registerListeners();

		this.stateTransitionTracker.registerModelListeners();
		this.stateTransitionTracker.registerFocusListeners();

		this.substancePropertyChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("font".equals(evt.getPropertyName())) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// remember the caret location - issue 404
							int caretPos = passwordField.getCaretPosition();
							passwordField.updateUI();
							passwordField.setCaretPosition(caretPos);
							Container parent = passwordField.getParent();
							if (parent != null) {
								parent.invalidate();
								parent.validate();
							}
						}
					});
				}

				if ("enabled".equals(evt.getPropertyName())) {
					transitionModel.setEnabled(passwordField.isEnabled());
				}
			}
		};
		this.passwordField
				.addPropertyChangeListener(this.substancePropertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicTextUI#uninstallListeners()
	 */
	@Override
	protected void uninstallListeners() {
		this.stateTransitionTracker.unregisterModelListeners();
		this.stateTransitionTracker.unregisterFocusListeners();

		this.passwordField
				.removePropertyChangeListener(this.substancePropertyChangeListener);
		this.substancePropertyChangeListener = null;

		this.passwordField.removeMouseListener(this.substanceRolloverListener);
		this.passwordField
				.removeMouseMotionListener(this.substanceRolloverListener);
		this.substanceRolloverListener = null;

		// this.passwordField.removeFocusListener(this.substanceFocusListener);
		// this.substanceFocusListener = null;

		super.uninstallListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicTextUI#installDefaults()
	 */
	@Override
	protected void installDefaults() {
		super.installDefaults();
		Border b = this.passwordField.getBorder();
		if (b == null || b instanceof UIResource) {
			Border newB = new BorderUIResource.CompoundBorderUIResource(
					new SubstanceTextComponentBorder(SubstanceSizeUtils
							.getTextBorderInsets(SubstanceSizeUtils
									.getComponentFontSize(this.passwordField))),
					new BasicBorders.MarginBorder());
			this.passwordField.setBorder(newB);
		}

		// support for per-window skins
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (passwordField == null)
					return;
				Color foregr = passwordField.getForeground();
				if ((foregr == null) || (foregr instanceof UIResource)) {
					passwordField
							.setForeground(SubstanceColorUtilities
									.getForegroundColor(SubstanceLookAndFeel
											.getCurrentSkin(passwordField)
											.getEnabledColorScheme(
													SubstanceLookAndFeel
															.getDecorationType(passwordField))));
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicTextUI#paintBackground(java.awt.Graphics)
	 */
	@Override
	protected void paintBackground(Graphics g) {
		SubstanceTextUtilities.paintTextCompBackground(g, this.passwordField);
	}

	@Override
	public boolean isInside(MouseEvent me) {
		if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
			return false;
		}
		Shape contour = SubstanceOutlineUtilities.getBaseOutline(
				this.passwordField, 2.0f * SubstanceSizeUtils
						.getClassicButtonCornerRadius(SubstanceSizeUtils
								.getComponentFontSize(this.passwordField)),
				null);
		return contour.contains(me.getPoint());
	}

	@Override
	public StateTransitionTracker getTransitionTracker() {
		return this.stateTransitionTracker;
	}
}
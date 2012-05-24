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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicDesktopIconUI;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

/**
 * UI for desktop icons in <b>Substance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceDesktopIconUI extends BasicDesktopIconUI {
	/**
	 * Listener on the title label (for the dragging purposes).
	 */
	private MouseInputListener substanceLabelMouseInputListener;

	/**
	 * Width of minimized component (desktop icon).
	 */
	private int width;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstanceDesktopIconUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicDesktopIconUI#installDefaults()
	 */
	@Override
	protected void installDefaults() {
		super.installDefaults();
		Font f = this.desktopIcon.getFont();
		if ((f == null) || (f instanceof UIResource)) {
			this.desktopIcon.setFont(UIManager.getFont("DesktopIcon.font"));
		}
		this.width = UIManager.getInt("DesktopIcon.width");
		this.desktopIcon.setBackground(SubstanceCoreUtilities.getSkin(
				this.desktopIcon.getInternalFrame()).getBackgroundColorScheme(
				DecorationAreaType.SECONDARY_TITLE_PANE)
		// SubstanceColorSchemeUtilities
				// //.getColorScheme(this.desktopIcon.getInternalFrame(),
				// ComponentState.ACTIVE).
				.getBackgroundFillColor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicDesktopIconUI#installComponents()
	 */
	@Override
	protected void installComponents() {
		this.frame = this.desktopIcon.getInternalFrame();
		// this.frame.setOpaque(false);
		// Icon icon = this.frame.getFrameIcon();

		this.iconPane = new SubstanceInternalFrameTitlePane(this.frame);
		this.iconPane.setOpaque(false);
		this.desktopIcon.setLayout(new BorderLayout());
		this.desktopIcon.add(this.iconPane, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicDesktopIconUI#uninstallComponents()
	 */
	@Override
	protected void uninstallComponents() {
		((SubstanceInternalFrameTitlePane) this.iconPane).uninstall();

		this.desktopIcon.setLayout(null);
		this.desktopIcon.remove(this.iconPane);

		this.frame = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicDesktopIconUI#installListeners()
	 */
	@Override
	protected void installListeners() {
		super.installListeners();
		this.substanceLabelMouseInputListener = this.createMouseInputListener();
		this.iconPane
				.addMouseMotionListener(this.substanceLabelMouseInputListener);
		this.iconPane.addMouseListener(this.substanceLabelMouseInputListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicDesktopIconUI#uninstallListeners()
	 */
	@Override
	protected void uninstallListeners() {
		((SubstanceInternalFrameTitlePane) this.iconPane).uninstallListeners();

		this.iconPane
				.removeMouseMotionListener(this.substanceLabelMouseInputListener);
		this.iconPane
				.removeMouseListener(this.substanceLabelMouseInputListener);
		this.substanceLabelMouseInputListener = null;

		super.uninstallListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.ComponentUI#getPreferredSize(javax.swing.JComponent)
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		// Desktop icons can not be resized. Their dimensions should
		// always be the minimum size. See getMinimumSize(JComponent c).
		return this.getMinimumSize(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#getMinimumSize(javax.swing.JComponent)
	 */
	@Override
	public Dimension getMinimumSize(JComponent c) {
		// For the desktop icon we will use the layout maanger to
		// determine the correct height of the component, but we want to keep
		// the width consistent according to the jlf spec.
		return new Dimension(this.width, this.desktopIcon.getLayout()
				.minimumLayoutSize(this.desktopIcon).height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#getMaximumSize(javax.swing.JComponent)
	 */
	@Override
	public Dimension getMaximumSize(JComponent c) {
		// Desktop icons can not be resized. Their dimensions should
		// always be the minimum size. See getMinimumSize(JComponent c).
		return this.getMinimumSize(c);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics,
	// * javax.swing.JComponent)
	// */
	// @Override
	// public void paint(Graphics g, JComponent c) {
	// JInternalFrame.JDesktopIcon di = (JInternalFrame.JDesktopIcon) c;
	// di.setOpaque(false);
	//
	// int width = di.getWidth();
	// int height = di.getHeight();
	//
	// Graphics2D graphics = (Graphics2D) g.create();
	// // the background is translucent
	// // graphics.setComposite(AlphaComposite.getInstance(
	// // AlphaComposite.SRC_ATOP, 0.6f));
	// //
	// // SubstanceImageCreator.paintRectangularBackground(graphics, 0, 0,
	// // width,
	// // height, SubstanceCoreUtilities.getActiveScheme(this.desktopIcon
	// // .getInternalFrame()), false, false);
	//
	// di.paintComponents(graphics);
	//
	// graphics.dispose();
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
	// * javax.swing.JComponent)
	// */
	// @Override
	// public void update(Graphics g, JComponent c) {
	// this.paint(g, c);
	// }
	//
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicDesktopIconUI#installUI(javax.swing.JComponent
	 * )
	 */
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setOpaque(false);
	}

	@Override
	public void uninstallUI(JComponent c) {
		// desktopIcon.remove(this.titleLabel);
		// super.uninstallUI(c);

		SubstanceInternalFrameTitlePane thePane = (SubstanceInternalFrameTitlePane) this.iconPane;
		super.uninstallUI(c);
		thePane.uninstallListeners();
	}

	/**
	 * Returns the component for desktop icon hover (internal frame preview)
	 * functionality.
	 * 
	 * @return The component for desktop icon hover (internal frame preview)
	 *         functionality.
	 */
	public JComponent getComponentForHover() {
		return this.iconPane;
	}

	/**
	 * Installs the UI delegate on the desktop icon if necessary.
	 * 
	 * @param jdi
	 *            Desktop icon.
	 */
	public void installIfNecessary(JDesktopIcon jdi) {
		// fix for issue 344 - reopening an internal frame
		// that has been closed.
		if (this.desktopIcon == null) {
			this.installUI(jdi);
		}
	}

	/**
	 * Uninstalls the UI delegate from the desktop icon if necessary.
	 * 
	 * @param jdi
	 *            Desktop icon.
	 */
	public void uninstallIfNecessary(JDesktopIcon jdi) {
		// fix for issue 345 - an internal frame used in inner option pane
		// gets closed twice
		if (this.desktopIcon == jdi) {
			this.uninstallUI(jdi);
		}
	}

	void setWindowModified(boolean isWindowModified) {
		((SubstanceInternalFrameTitlePane) this.iconPane).getCloseButton()
				.putClientProperty(SubstanceLookAndFeel.WINDOW_MODIFIED,
						Boolean.valueOf(isWindowModified));
	}
}

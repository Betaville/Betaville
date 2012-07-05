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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.table.JTableHeader;

import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollPaneBorder;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;
import org.pushingpixels.trident.ease.TimelineEase;

/**
 * UI for scroll panes in <b>Substance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceScrollPaneUI extends BasicScrollPaneUI {
	/**
	 * Property change listener on
	 * {@link SubstanceLookAndFeel#SCROLL_PANE_BUTTONS_POLICY},
	 * {@link SubstanceLookAndFeel#WATERMARK_TO_BLEED} and
	 * <code>layoutManager</code> properties.
	 */
	protected PropertyChangeListener substancePropertyChangeListener;

	/**
	 * Listener on the vertical scroll bar. Installed for the smart tree scroll
	 * (see {@link SubstanceLookAndFeel#TREE_SMART_SCROLL_ANIMATION_KIND}.
	 */
	protected ChangeListener substanceVerticalScrollbarChangeListener;

	/**
	 * Timeline of the current horizontal scroll under smart tree scroll mode.
	 */
	protected Timeline horizontalScrollTimeline;

	/**
	 * Creates new UI delegate.
	 * 
	 * @param comp
	 *            Component.
	 * @return UI delegate for the component.
	 */
	public static ComponentUI createUI(JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstanceScrollPaneUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicScrollPaneUI#installDefaults(javax.swing.
	 * JScrollPane)
	 */
	@Override
	protected void installDefaults(final JScrollPane scrollpane) {
		super.installDefaults(scrollpane);
		if (SubstanceCoreUtilities.toDrawWatermark(scrollpane)
				&& (SubstanceLookAndFeel.getCurrentSkin(scrollpane)
						.getWatermark() != null)) {
			scrollpane.setOpaque(false);
			scrollpane.getViewport().setOpaque(false);
		}
		scrollpane.setLayout(new AdjustedLayout((ScrollPaneLayout) scrollpane
				.getLayout()));

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// System.out.println("Installing");
				installTableHeaderCornerFiller(scrollpane);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicScrollPaneUI#uninstallDefaults(javax.swing
	 * .JScrollPane)
	 */
	@Override
	protected void uninstallDefaults(JScrollPane c) {
		Component upperRight = c.getCorner(JScrollPane.UPPER_RIGHT_CORNER);
		if (upperRight instanceof UIResource) {
			c.setCorner(JScrollPane.UPPER_RIGHT_CORNER, null);
		}
		Component upperLeft = c.getCorner(JScrollPane.UPPER_LEFT_CORNER);
		if (upperLeft instanceof UIResource) {
			c.setCorner(JScrollPane.UPPER_LEFT_CORNER, null);
		}

		LayoutManager lm = scrollpane.getLayout();
		if (lm instanceof AdjustedLayout) {
			c.setLayout(((AdjustedLayout) lm).delegate);
		}
		super.uninstallDefaults(c);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// javax.swing.plaf.basic.BasicScrollPaneUI#uninstallDefaults(javax.swing.
	// JScrollPane)
	// */
	// @Override
	// protected void uninstallDefaults(JScrollPane c) {
	// super.uninstallDefaults(c);
	// ScrollPaneSelector.uninstallScrollPaneSelector(scrollpane);
	// }
	//
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicScrollPaneUI#installListeners(javax.swing
	 * .JScrollPane)
	 */
	@Override
	protected void installListeners(final JScrollPane c) {
		super.installListeners(c);
		this.substancePropertyChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (SubstanceLookAndFeel.SCROLL_PANE_BUTTONS_POLICY.equals(evt
						.getPropertyName())) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							c.getHorizontalScrollBar().doLayout();
							c.getVerticalScrollBar().doLayout();
						}
					});
				}
				if (SubstanceLookAndFeel.WATERMARK_VISIBLE.equals(evt
						.getPropertyName())) {
					boolean toBleed = SubstanceCoreUtilities.toDrawWatermark(c);
					c.setOpaque(!toBleed);
					c.getViewport().setOpaque(!toBleed);
					Component view = c.getViewport().getView();
					if (view instanceof JComponent)
						((JComponent) view).setOpaque(!toBleed);
				}
				if ("layoutManager".equals(evt.getPropertyName())) {
					if (((Boolean) evt.getNewValue()).booleanValue()) {
						ScrollPaneLayout currLayout = (ScrollPaneLayout) c
								.getLayout();
						if (!(currLayout instanceof AdjustedLayout)) {
							c.setLayout(new AdjustedLayout((ScrollPaneLayout) c
									.getLayout()));
						}
					}
					// else {
					// ScrollPaneLayout currLayout = (ScrollPaneLayout) c
					// .getLayout();
					// if (currLayout instanceof AdjustedLayout) {
					// c.setLayout(((AdjustedLayout) currLayout).delegate);
					// }
					// }
				}
				if ("background".equals(evt.getPropertyName())) {
					// propagate application-specific background color to the
					// scroll bars.
					Color newBackgr = (Color) evt.getNewValue();
					if (!(newBackgr instanceof UIResource)) {
						JScrollBar vertical = scrollpane.getVerticalScrollBar();
						if (vertical != null) {
							if (vertical.getBackground() instanceof UIResource) {
								vertical.setBackground(newBackgr);
							}
						}
						JScrollBar horizontal = scrollpane
								.getHorizontalScrollBar();
						if (horizontal != null) {
							if (horizontal.getBackground() instanceof UIResource) {
								horizontal.setBackground(newBackgr);
							}
						}
					}
				}
				if ("columnHeader".equals(evt.getPropertyName())
						|| "componentOrientation".equals(evt.getPropertyName())
						|| "ancestor".equals(evt.getPropertyName())) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// need to switch the corner filler based on the
							// current scroll pane state.
							if (scrollpane != null) {
								installTableHeaderCornerFiller(scrollpane);
							}
						}
					});
				}
			}
		};
		c.addPropertyChangeListener(this.substancePropertyChangeListener);

		this.substanceVerticalScrollbarChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// check if it's a horizontally scrollable tree inside
				if ((c.getHorizontalScrollBar() != null)
						&& c.getHorizontalScrollBar().isVisible()
						&& (c.getViewport().getView() instanceof JTree)) {
					JTree tree = (JTree) c.getViewport().getView();
					// check if the smart scroll is enabled
					if (AnimationConfigurationManager
							.getInstance()
							.isAnimationAllowed(
									SubstanceLookAndFeel.TREE_SMART_SCROLL_ANIMATION_KIND,
									tree)) {
						SubstanceTreeUI treeUI = (SubstanceTreeUI) tree.getUI();
						final Rectangle viewportRect = c.getViewport()
								.getViewRect();
						int pivotX = treeUI.getPivotRendererX(viewportRect);
						int currPivotX = viewportRect.x;// + viewportRect.width
						// / 2;
						int delta = pivotX - currPivotX;
						int finalX = viewportRect.x + delta;
						if (finalX < 0) {
							delta -= finalX;
						}
						final int finalDelta = delta;
						if (Math.abs(finalDelta) > viewportRect.width / 6) {
							if (horizontalScrollTimeline != null) {
								// abort previous horizontal scroll
								horizontalScrollTimeline.abort();
							}
							horizontalScrollTimeline = new Timeline(tree);
							horizontalScrollTimeline
									.addCallback(new UIThreadTimelineCallbackAdapter() {
										@Override
										public void onTimelinePulse(
												float durationFraction,
												float timelinePosition) {
											if (timelinePosition >= 0.5) {
												int nudge = (int) (finalDelta * (timelinePosition - 0.5));
												c
														.getViewport()
														.setViewPosition(
																new Point(
																		viewportRect.x
																				+ nudge,
																		viewportRect.y));
											}
										}
									});
							horizontalScrollTimeline
									.setEase(new TimelineEase() {
										@Override
										public float map(float durationFraction) {
											if (durationFraction < 0.5)
												return 0.5f * durationFraction;
											return 0.25f + (durationFraction - 0.5f) * 0.75f / 0.5f;
										}
									});
							AnimationConfigurationManager
									.getInstance()
									.configureTimeline(horizontalScrollTimeline);
							horizontalScrollTimeline
									.setDuration(2 * horizontalScrollTimeline
											.getDuration());
							horizontalScrollTimeline.play();
						}
					}
				}
			}
		};
		c.getVerticalScrollBar().getModel().addChangeListener(
				this.substanceVerticalScrollbarChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.plaf.basic.BasicScrollPaneUI#uninstallListeners(javax.swing
	 * .JComponent)
	 */
	@Override
	protected void uninstallListeners(JComponent c) {
		c.removePropertyChangeListener(this.substancePropertyChangeListener);
		this.substancePropertyChangeListener = null;

		JScrollPane jsp = (JScrollPane) c;
		jsp.getVerticalScrollBar().getModel().removeChangeListener(
				this.substanceVerticalScrollbarChangeListener);
		this.substanceVerticalScrollbarChangeListener = null;

		super.uninstallListeners(c);
	}

	/**
	 * Layout manager to adjust the bounds of scrollbars and the viewport when
	 * the default ({@link SubstanceScrollPaneBorder}) border is set on the
	 * relevant {@link JScrollPane}.
	 * 
	 * @author Kirill Grouchnikov
	 */
	protected static class AdjustedLayout extends ScrollPaneLayout implements
			UIResource {
		/**
		 * The delegate layout.
		 */
		protected ScrollPaneLayout delegate;

		/**
		 * Creates a new layout for adjusting the bounds of scrollbars and the
		 * viewport.
		 * 
		 * @param delegate
		 *            The original (delegate) layout.
		 */
		public AdjustedLayout(ScrollPaneLayout delegate) {
			this.delegate = delegate;
		}

		@Override
		public void addLayoutComponent(String s, Component c) {
			delegate.addLayoutComponent(s, c);
		}

		@Override
		public boolean equals(Object obj) {
			return delegate.equals(obj);
		}

		@Override
		public JViewport getColumnHeader() {
			return delegate.getColumnHeader();
		}

		@Override
		public Component getCorner(String key) {
			return delegate.getCorner(key);
		}

		@Override
		public JScrollBar getHorizontalScrollBar() {
			return delegate.getHorizontalScrollBar();
		}

		@Override
		public int getHorizontalScrollBarPolicy() {
			return delegate.getHorizontalScrollBarPolicy();
		}

		@Override
		public JViewport getRowHeader() {
			return delegate.getRowHeader();
		}

		@Override
		public JScrollBar getVerticalScrollBar() {
			return delegate.getVerticalScrollBar();
		}

		@Override
		public int getVerticalScrollBarPolicy() {
			return delegate.getVerticalScrollBarPolicy();
		}

		@Override
		public JViewport getViewport() {
			return delegate.getViewport();
		}

		@Override
		@SuppressWarnings("deprecation")
		public Rectangle getViewportBorderBounds(JScrollPane scrollpane) {
			return delegate.getViewportBorderBounds(scrollpane);
		}

		@Override
		public int hashCode() {
			return delegate.hashCode();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return delegate.minimumLayoutSize(parent);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return delegate.preferredLayoutSize(parent);
		}

		@Override
		public void removeLayoutComponent(Component c) {
			delegate.removeLayoutComponent(c);
		}

		@Override
		public void setHorizontalScrollBarPolicy(int x) {
			delegate.setHorizontalScrollBarPolicy(x);
		}

		@Override
		public void setVerticalScrollBarPolicy(int x) {
			delegate.setVerticalScrollBarPolicy(x);
		}

		@Override
		public void syncWithScrollPane(JScrollPane sp) {
			delegate.syncWithScrollPane(sp);
		}

		@Override
		public String toString() {
			return delegate.toString();
		}

		// ScrollPaneLayout.UIResource {
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ScrollPaneLayout#layoutContainer(java.awt.Container)
		 */
		@Override
		public void layoutContainer(Container parent) {
			delegate.layoutContainer(parent);

			JScrollPane scrollPane = (JScrollPane) parent;
			Border border = scrollPane.getBorder();
			boolean toAdjust = (border instanceof SubstanceScrollPaneBorder);
			if (toAdjust) {
				JScrollBar vertical = scrollPane.getVerticalScrollBar();
				JScrollBar horizontal = scrollPane.getHorizontalScrollBar();

				int borderDelta = (int) Math.floor(SubstanceSizeUtils
						.getBorderStrokeWidth(SubstanceSizeUtils
								.getComponentFontSize(scrollPane)) / 2.0);
				int borderWidth = (int) SubstanceSizeUtils
						.getBorderStrokeWidth(SubstanceSizeUtils
								.getComponentFontSize(scrollPane));
				int dx = 0, dy = 0, dw = 0, dh = 0;
				if (scrollPane.getComponentOrientation().isLeftToRight()) {
					if ((vertical != null) && vertical.isVisible()) {
						Rectangle vBounds = vertical.getBounds();
						dw += (1 + borderDelta);
						vertical.setBounds(vBounds.x + 1 + borderDelta,
								vBounds.y + 1 - 2 * borderWidth, vBounds.width,
								vBounds.height + 2 * borderWidth);
					}
					if ((horizontal != null) && horizontal.isVisible()) {
						dh += (1 + borderDelta);
						Rectangle hBounds = horizontal.getBounds();
						horizontal.setBounds(hBounds.x + 1 - 2 * borderWidth,
								hBounds.y + 1, hBounds.width + 2 * borderWidth,
								hBounds.height);
					}

					if (delegate.getCorner(ScrollPaneLayout.LOWER_RIGHT_CORNER) != null) {
						Rectangle lrBounds = delegate.getCorner(
								ScrollPaneLayout.LOWER_RIGHT_CORNER)
								.getBounds();
						delegate.getCorner(ScrollPaneLayout.LOWER_RIGHT_CORNER)
								.setBounds(lrBounds.x + 1 + borderDelta,
										lrBounds.y + 1 + borderDelta,
										lrBounds.width, lrBounds.height);
					}
					if (delegate.getCorner(ScrollPaneLayout.UPPER_RIGHT_CORNER) != null) {
						Rectangle urBounds = delegate.getCorner(
								ScrollPaneLayout.UPPER_RIGHT_CORNER)
								.getBounds();
						delegate.getCorner(ScrollPaneLayout.UPPER_RIGHT_CORNER)
								.setBounds(urBounds.x + 1 + borderDelta,
										urBounds.y + borderDelta,
										urBounds.width - 1, urBounds.height);
					}
				} else {
					if ((vertical != null) && vertical.isVisible()) {
						dx -= (1 + borderDelta);
						dw += (1 + borderDelta);
						Rectangle vBounds = vertical.getBounds();
						vertical.setBounds(vBounds.x - 1 - borderDelta,
								vBounds.y - 1 - borderDelta, vBounds.width,
								vBounds.height + 2 * borderWidth);
					}
					if ((horizontal != null) && horizontal.isVisible()) {
						dh += (1 + borderDelta);
						Rectangle hBounds = horizontal.getBounds();
						horizontal.setBounds(hBounds.x - 1 - borderDelta,
								hBounds.y + 1 + borderDelta, hBounds.width + 2
										* borderWidth, hBounds.height);
					}
					if (delegate.getCorner(ScrollPaneLayout.LOWER_LEFT_CORNER) != null) {
						Rectangle llBounds = delegate.getCorner(
								ScrollPaneLayout.LOWER_LEFT_CORNER).getBounds();
						delegate.getCorner(ScrollPaneLayout.LOWER_LEFT_CORNER)
								.setBounds(llBounds.x - 1 - borderDelta,
										llBounds.y - 1 - borderDelta,
										llBounds.width, llBounds.height);
					}
					if (delegate.getCorner(ScrollPaneLayout.UPPER_LEFT_CORNER) != null) {
						Rectangle ulBounds = delegate.getCorner(
								ScrollPaneLayout.UPPER_LEFT_CORNER).getBounds();
						delegate.getCorner(ScrollPaneLayout.UPPER_LEFT_CORNER)
								.setBounds(ulBounds.x - borderDelta,
										ulBounds.y - borderDelta,
										ulBounds.width - 1, ulBounds.height);
					}
				}

				if (delegate.getViewport() != null) {
					Rectangle vpBounds = delegate.getViewport().getBounds();
					delegate.getViewport().setBounds(
							new Rectangle(vpBounds.x + dx, vpBounds.y + dy,
									vpBounds.width + dw, vpBounds.height + dh));
				}
				if (delegate.getColumnHeader() != null) {
					Rectangle columnHeaderBounds = delegate.getColumnHeader()
							.getBounds();
					delegate.getColumnHeader().setBounds(
							new Rectangle(columnHeaderBounds.x + dx,
									columnHeaderBounds.y + dy,
									columnHeaderBounds.width + dw,
									columnHeaderBounds.height));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		BackgroundPaintingUtils.updateIfOpaque(g, c);
		JScrollPane jsp = (JScrollPane) c;
		// if (SubstanceCoreUtilities.hasOverlayProperty(jsp)) {
		// JViewport viewport = jsp.getViewport();
		// int dx = -viewport.getX() + viewport.getViewRect().x;
		// int dy = viewport.getY() - viewport.getViewRect().y;
		// Graphics2D graphics = (Graphics2D) g.create();
		//
		// Area clip = new Area();
		// if ((jsp.getVerticalScrollBar() != null)
		// && jsp.getVerticalScrollBar().isVisible())
		// clip.add(new Area(jsp.getVerticalScrollBar().getBounds()));
		// if ((jsp.getHorizontalScrollBar() != null)
		// && jsp.getHorizontalScrollBar().isVisible())
		// clip.add(new Area(jsp.getHorizontalScrollBar().getBounds()));
		// graphics.setClip(clip);
		//
		// graphics.translate(-dx, dy);
		// JComponent view = (JComponent) viewport.getView();
		// // fix for defect 201 - set non-double buffer for the viewport
		// // recursively. Takes care of desktop pane wrapped in scroll pane.
		// Map<Component, Boolean> dbSnapshot = new HashMap<Component,
		// Boolean>();
		// SubstanceCoreUtilities.makeNonDoubleBuffered(view, dbSnapshot);
		// view.paint(graphics);
		// // restore the double buffer.
		// SubstanceCoreUtilities.restoreDoubleBuffered(view, dbSnapshot);
		// graphics.translate(dx, -dy);
		// graphics.dispose();
		// }

		LayoutManager lm = jsp.getLayout();
		ScrollPaneLayout scrollLm = null;
		if (lm instanceof ScrollPaneLayout) {
			scrollLm = (ScrollPaneLayout) lm;
		}

		if (scrollLm != null) {
			Set<Component> corners = new HashSet<Component>();
			if (scrollLm.getCorner(ScrollPaneLayout.LOWER_LEFT_CORNER) != null) {
				corners.add(scrollLm
						.getCorner(ScrollPaneLayout.LOWER_LEFT_CORNER));
			}
			if (scrollLm.getCorner(ScrollPaneLayout.LOWER_RIGHT_CORNER) != null) {
				corners.add(scrollLm
						.getCorner(ScrollPaneLayout.LOWER_RIGHT_CORNER));
			}
			if (scrollLm.getCorner(ScrollPaneLayout.UPPER_LEFT_CORNER) != null) {
				corners.add(scrollLm
						.getCorner(ScrollPaneLayout.UPPER_LEFT_CORNER));
			}
			if (scrollLm.getCorner(ScrollPaneLayout.UPPER_RIGHT_CORNER) != null) {
				corners.add(scrollLm
						.getCorner(ScrollPaneLayout.UPPER_RIGHT_CORNER));
			}

			if (SubstanceCoreUtilities.isOpaque(c)) {
				for (Component corner : corners) {
					BackgroundPaintingUtils.fillAndWatermark(g, c, c
							.getBackground(), corner.getBounds());
				}
			}
		}

		super.paint(g, c);
	}

	/**
	 * Installs a corner filler that matches the table header. This is done to
	 * provide a continuous appearance for tables with table headers placed in
	 * scroll panes.
	 * 
	 * @param scrollpane
	 *            Scroll pane.
	 */
	protected static void installTableHeaderCornerFiller(JScrollPane scrollpane) {
		// install custom scroll pane corner filler
		// for continuous painting of table headers
		JViewport columnHeader = scrollpane.getColumnHeader();
		// System.out.println("Column header " + columnHeader);
		if (columnHeader == null)
			return;
		Component columnHeaderComp = columnHeader.getView();
		// System.out.println("Column header comp " + columnHeaderComp);
		if (!(columnHeaderComp instanceof JTableHeader))
			return;
		JTableHeader tableHeader = (JTableHeader) columnHeaderComp;
		TableHeaderUI tableHeaderUI = tableHeader.getUI();
		if (!(tableHeaderUI instanceof SubstanceTableHeaderUI))
			return;
		SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI) tableHeaderUI;
		JComponent scrollPaneCornerFiller = ui.getScrollPaneCornerFiller();
		String cornerKey = scrollpane.getComponentOrientation().isLeftToRight() ? JScrollPane.UPPER_RIGHT_CORNER
				: JScrollPane.UPPER_LEFT_CORNER;
		Component cornerComp = scrollpane.getCorner(cornerKey);
		// Corner component can be replaced when the current one is null or
		// UIResource
		boolean canReplace = (cornerComp == null)
				|| (cornerComp instanceof UIResource);
		// System.out.println(canReplace + ":" + cornerComp);
		if (canReplace) {
			scrollpane.setCorner(cornerKey, scrollPaneCornerFiller);
		}
	}
}

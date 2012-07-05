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
package org.pushingpixels.substance.api.renderers;

import java.awt.*;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.*;
import javax.swing.table.*;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker.StateContributionInfo;
import org.pushingpixels.substance.internal.ui.SubstanceTableHeaderUI;
import org.pushingpixels.substance.internal.ui.SubstanceTableUI;
import org.pushingpixels.substance.internal.utils.*;

/**
 * Default renderer for table header cells.
 * 
 * @author Kirill Grouchnikov
 */
@SubstanceRenderer
public class SubstanceDefaultTableHeaderCellRenderer extends
		DefaultTableCellRenderer implements UIResource {
	/**
	 * Creates a new cell renderer.
	 */
	public SubstanceDefaultTableHeaderCellRenderer() {
		setHorizontalAlignment(JLabel.CENTER);
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (table == null) {
			setBorder(DefaultTableCellRenderer.noFocusBorder);
			setValue(value);
			setOpaque(false);
			return this;
		}

		if (table.getTableHeader() == null) {
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}

		JTableHeader tableHeader = table.getTableHeader();
		TableHeaderUI tableHeaderUI = tableHeader.getUI();
		if (SubstanceLookAndFeel.isCurrentLookAndFeel()
				&& (tableHeaderUI instanceof SubstanceTableHeaderUI)) {
			SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI) tableHeaderUI;

			StateTransitionTracker.ModelStateInfo modelStateInfo = ui
					.getModelStateInfo(column);
			ComponentState currState = ui.getColumnState(column);

			if (modelStateInfo != null) {
				Map<ComponentState, StateContributionInfo> activeStates = modelStateInfo
						.getStateContributionMap();
				SubstanceColorScheme colorScheme = getColorSchemeForState(
						tableHeader, currState);
				if (currState.isDisabled() || (activeStates == null)
						|| (activeStates.size() == 1)) {
					super.setForeground(new ColorUIResource(colorScheme
							.getForegroundColor()));
				} else {
					float aggrRed = 0;
					float aggrGreen = 0;
					float aggrBlue = 0;

					for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo
							.getStateContributionMap().entrySet()) {
						ComponentState activeState = activeEntry.getKey();
						SubstanceColorScheme scheme = getColorSchemeForState(
								tableHeader, activeState);
						Color schemeFg = scheme.getForegroundColor();
						float contribution = activeEntry.getValue()
								.getContribution();
						aggrRed += schemeFg.getRed() * contribution;
						aggrGreen += schemeFg.getGreen() * contribution;
						aggrBlue += schemeFg.getBlue() * contribution;
					}
					super.setForeground(new ColorUIResource(new Color(
							(int) aggrRed, (int) aggrGreen, (int) aggrBlue)));
				}
			} else {
				SubstanceColorScheme scheme = getColorSchemeForState(
						tableHeader, currState);
				super.setForeground(new ColorUIResource(scheme
						.getForegroundColor()));
			}
		} else {
			super.setForeground(table.getForeground());
		}

		this.setBackground(tableHeader.getBackground());

		// fix for issue 319 - using font from the table header
		if (tableHeader.getFont() != null) {
			setFont(tableHeader.getFont());
		} else {
			setFont(table.getFont());
		}

		TableUI tableUI = table.getUI();
		if (SubstanceLookAndFeel.isCurrentLookAndFeel()
				&& (tableUI instanceof SubstanceTableUI)) {
			this.setBorder(new EmptyBorder(((SubstanceTableUI) tableUI)
					.getCellRendererInsets()));
		}

		this.setValue(value);
		this.setOpaque(false);

		this.setEnabled(tableHeader.isEnabled() && table.isEnabled());

		// fix for defect 242 - not showing sort icon
		if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
			this.setIcon(null);
			RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
			if (rowSorter != null) {
				setHorizontalTextPosition(JLabel.LEADING);
				java.util.List<? extends RowSorter.SortKey> sortKeys = rowSorter
						.getSortKeys();
				Icon sortIcon = null;
				SubstanceColorScheme scheme = null;
				if (tableHeaderUI instanceof SubstanceTableHeaderUI) {
					SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI) tableHeaderUI;
					ComponentState state = ui.getColumnState(column);
					ColorSchemeAssociationKind colorSchemeAssociationKind = (state == ComponentState.ENABLED) ? ColorSchemeAssociationKind.MARK
							: ColorSchemeAssociationKind.HIGHLIGHT_MARK;
					scheme = SubstanceColorSchemeUtilities.getColorScheme(
							tableHeader, colorSchemeAssociationKind, state);
				} else {
					scheme = SubstanceColorSchemeUtilities.getColorScheme(
							tableHeader, ComponentState.ENABLED);
				}

				if (sortKeys.size() > 0
						&& sortKeys.get(0).getColumn() == table
								.convertColumnIndexToModel(column)) {
					switch (sortKeys.get(0).getSortOrder()) {
					case ASCENDING:
						sortIcon = SubstanceImageCreator.getArrowIcon(
								SubstanceSizeUtils
										.getComponentFontSize(tableHeader),
								SwingConstants.NORTH, scheme);
						break;
					case DESCENDING:
						sortIcon = SubstanceImageCreator.getArrowIcon(
								SubstanceSizeUtils
										.getComponentFontSize(tableHeader),
								SwingConstants.SOUTH, scheme);
						break;
					case UNSORTED:
						sortIcon = null;
					}
					this.setIcon(sortIcon);
				}
			}
		}

		return this;
	}

	private SubstanceColorScheme getColorSchemeForState(
			JTableHeader tableHeader, ComponentState activeState) {
		SubstanceColorScheme scheme = (activeState == ComponentState.ENABLED) ? SubstanceColorSchemeUtilities
				.getColorScheme(tableHeader, activeState)
				: SubstanceColorSchemeUtilities.getColorScheme(tableHeader,
						ColorSchemeAssociationKind.HIGHLIGHT, activeState);
		return scheme;
	}

	// /**
	// * Returns <code>true</code> if the specified column is sorted.
	// *
	// * @param table
	// * Table.
	// * @param columnIndex
	// * Column index.
	// * @return <code>true</code> if the specified column is sorted,
	// * <code>false</code> otherwise.
	// */
	// public static boolean isColumnSorted(JTable table, int columnIndex) {
	// RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
	// if (rowSorter != null) {
	// java.util.List<? extends RowSorter.SortKey> sortKeys = rowSorter
	// .getSortKeys();
	// if (sortKeys.size() > 0
	// && sortKeys.get(0).getColumn() == table
	// .convertColumnIndexToModel(columnIndex)) {
	// switch (sortKeys.get(0).getSortOrder()) {
	// case ASCENDING:
	// case DESCENDING:
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public final void paint(Graphics g) {
		super.paint(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected final void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}

package org.pushingpixels.substance.internal.utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;
import javax.swing.text.DefaultEditorKit;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import org.pushingpixels.substance.internal.utils.border.*;
import org.pushingpixels.substance.internal.utils.icon.*;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollPaneBorder;

public class SkinUtilities {
	/**
	 * Adds skin-specific entries to the UI defaults table.
	 * 
	 * @param table
	 *            UI defaults table.
	 */
	public static void addCustomEntriesToTable(UIDefaults table,
			SubstanceSkin skin) {
		Object menuArrowIcon = new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				return new MenuArrowIcon(null);
			}
		};

		Object listCellRendererActiveValue = new UIDefaults.ActiveValue() {
			public Object createValue(UIDefaults table) {
				return new SubstanceDefaultListCellRenderer.SubstanceUIResource();
			}
		};

		SubstanceColorScheme mainActiveScheme = skin
				.getActiveColorScheme(DecorationAreaType.NONE);
		SubstanceColorScheme mainEnabledScheme = skin
				.getEnabledColorScheme(DecorationAreaType.NONE);
		SubstanceColorScheme mainDisabledScheme = skin
				.getDisabledColorScheme(DecorationAreaType.NONE);
		Color controlText = new ColorUIResource(mainActiveScheme
				.getLightColor());
		Color foregroundColor = SubstanceColorUtilities
				.getForegroundColor(mainEnabledScheme);
		Color backgroundActiveColor = new ColorUIResource(mainActiveScheme
				.getBackgroundFillColor());
		Color backgroundDefaultColor = new ColorUIResource(mainEnabledScheme
				.getBackgroundFillColor());
		Color textBackgroundColor = new ColorUIResource(mainActiveScheme
				.getTextBackgroundFillColor());

		Color disabledForegroundColor = SubstanceColorUtilities
				.getForegroundColor(mainDisabledScheme);
		Color disabledTextComponentForegroundColor = disabledForegroundColor;
		float alpha = skin.getAlpha(null, ComponentState.DISABLED_UNSELECTED);
		if (alpha < 1.0f) {
			ColorUIResource defaultTextBackgroundColor = SubstanceColorUtilities
					.getDefaultBackgroundColor(true, skin, false);
			disabledTextComponentForegroundColor = new ColorUIResource(
					SubstanceColorUtilities.getInterpolatedColor(
							disabledTextComponentForegroundColor,
							defaultTextBackgroundColor, alpha));
		}

		Color lineColor = new ColorUIResource(mainActiveScheme.getLineColor());

		Color lineColorDefault = new ColorUIResource(mainEnabledScheme
				.getLineColor());

		int lcb = SubstanceColorUtilities
				.getColorBrightness(lineColor.getRGB());
		Color lineBwColor = new ColorUIResource(new Color(lcb, lcb, lcb));

		SubstanceColorScheme textHighlightColorScheme = skin.getColorScheme(
				(Component) null, ColorSchemeAssociationKind.TEXT_HIGHLIGHT,
				ComponentState.SELECTED);
		if (textHighlightColorScheme == null) {
			textHighlightColorScheme = skin.getColorScheme(null,
					ComponentState.ROLLOVER_SELECTED);
		}
		Color selectionTextBackgroundColor = new ColorUIResource(
				textHighlightColorScheme.getSelectionBackgroundColor());
		Color selectionTextForegroundColor = new ColorUIResource(
				textHighlightColorScheme.getSelectionForegroundColor());

		Color selectionCellForegroundColor = new ColorUIResource(
				textHighlightColorScheme.getForegroundColor());
		Color selectionCellBackgroundColor = new ColorUIResource(
				textHighlightColorScheme.getBackgroundFillColor());

		Object regularMarginBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new BorderUIResource.CompoundBorderUIResource(
						new SubstanceBorder(), new BasicBorders.MarginBorder());
			}
		};

		Object textBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new BorderUIResource.CompoundBorderUIResource(
						new SubstanceTextComponentBorder(SubstanceSizeUtils
								.getTextBorderInsets(SubstanceSizeUtils
										.getControlFontSize())),
						new BasicBorders.MarginBorder());
			}
		};

		Object textMarginBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new BasicBorders.MarginBorder();
			}
		};

		Object tooltipBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new SubstanceBorder(SubstanceSizeUtils
						.getToolTipBorderInsets(SubstanceSizeUtils
								.getControlFontSize()));
			}
		};

		Object comboBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new SubstanceBorder(SubstanceSizeUtils
						.getComboBorderInsets(SubstanceSizeUtils
								.getControlFontSize()));
			}
		};

		Object spinnerBorder = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new BorderUIResource.CompoundBorderUIResource(
						new SubstanceTextComponentBorder(SubstanceSizeUtils
								.getSpinnerBorderInsets(SubstanceSizeUtils
										.getControlFontSize())),
						new BasicBorders.MarginBorder());
			}
		};

		// SubstanceColorSchemeBundle titlePaneBundle =
		// skin.colorSchemeBundleMap
		// .containsKey(DecorationAreaType.PRIMARY_TITLE_PANE) ?
		// skin.colorSchemeBundleMap
		// .get(DecorationAreaType.PRIMARY_TITLE_PANE)
		// : skin.colorSchemeBundleMap.get(DecorationAreaType.NONE);
		final SubstanceColorScheme titlePaneScheme = skin
				.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE);
		// /skin.getColorScheme(
		// DecorationAreaType.PRIMARY_TITLE_PANE,
		// ColorSchemeAssociationKind.FILL, ComponentState.ACTIVE);
		//
		// titlePaneBundle.getActiveColorScheme();

		Object menuItemInsets = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				int menuItemMargin = SubstanceSizeUtils
						.getMenuItemMargin(SubstanceSizeUtils
								.getComponentFontSize(null));
				return new InsetsUIResource(menuItemMargin, menuItemMargin,
						menuItemMargin, menuItemMargin);
			}
		};

		Object fieldInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,

				"ctrl V", DefaultEditorKit.pasteAction,

				"ctrl X", DefaultEditorKit.cutAction,

				"COPY", DefaultEditorKit.copyAction,

				"PASTE", DefaultEditorKit.pasteAction,

				"CUT", DefaultEditorKit.cutAction,

				"control INSERT", DefaultEditorKit.copyAction,

				"shift INSERT", DefaultEditorKit.pasteAction,

				"shift DELETE", DefaultEditorKit.cutAction,

				"shift LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift RIGHT", DefaultEditorKit.selectionForwardAction,

				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,

				"ctrl LEFT", DefaultEditorKit.previousWordAction,

				"ctrl KP_LEFT", DefaultEditorKit.previousWordAction,

				"ctrl RIGHT", DefaultEditorKit.nextWordAction,

				"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,

				"ctrl shift LEFT",
				DefaultEditorKit.selectionPreviousWordAction,

				"ctrl shift KP_LEFT",
				DefaultEditorKit.selectionPreviousWordAction,

				"ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,

				"ctrl shift KP_RIGHT",
				DefaultEditorKit.selectionNextWordAction,

				"ctrl A", DefaultEditorKit.selectAllAction,

				"HOME", DefaultEditorKit.beginLineAction,

				"END", DefaultEditorKit.endLineAction,

				"shift HOME", DefaultEditorKit.selectionBeginLineAction,

				"shift END", DefaultEditorKit.selectionEndLineAction,

				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"ctrl H", DefaultEditorKit.deletePrevCharAction,

				"DELETE", DefaultEditorKit.deleteNextCharAction,

				"ctrl DELETE", DefaultEditorKit.deleteNextWordAction,

				"ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,

				"RIGHT", DefaultEditorKit.forwardAction,

				"LEFT", DefaultEditorKit.backwardAction,

				"KP_RIGHT", DefaultEditorKit.forwardAction,

				"KP_LEFT", DefaultEditorKit.backwardAction,

				"ENTER", JTextField.notifyAction,

				"ctrl BACK_SLASH", "unselect",

				"control shift O", "toggle-componentOrientation" });

		Object passwordInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,

				"ctrl V", DefaultEditorKit.pasteAction,

				"ctrl X", DefaultEditorKit.cutAction,

				"COPY", DefaultEditorKit.copyAction,

				"PASTE", DefaultEditorKit.pasteAction,

				"CUT", DefaultEditorKit.cutAction,

				"control INSERT", DefaultEditorKit.copyAction,

				"shift INSERT", DefaultEditorKit.pasteAction,

				"shift DELETE", DefaultEditorKit.cutAction,

				"shift LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift RIGHT", DefaultEditorKit.selectionForwardAction,

				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,

				"ctrl LEFT", DefaultEditorKit.beginLineAction,

				"ctrl KP_LEFT", DefaultEditorKit.beginLineAction,

				"ctrl RIGHT", DefaultEditorKit.endLineAction,

				"ctrl KP_RIGHT", DefaultEditorKit.endLineAction,

				"ctrl shift LEFT", DefaultEditorKit.selectionBeginLineAction,

				"ctrl shift KP_LEFT",
				DefaultEditorKit.selectionBeginLineAction,

				"ctrl shift RIGHT", DefaultEditorKit.selectionEndLineAction,

				"ctrl shift KP_RIGHT", DefaultEditorKit.selectionEndLineAction,

				"ctrl A", DefaultEditorKit.selectAllAction,

				"HOME", DefaultEditorKit.beginLineAction,

				"END", DefaultEditorKit.endLineAction,

				"shift HOME", DefaultEditorKit.selectionBeginLineAction,

				"shift END", DefaultEditorKit.selectionEndLineAction,

				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"ctrl H", DefaultEditorKit.deletePrevCharAction,

				"DELETE", DefaultEditorKit.deleteNextCharAction,

				"RIGHT", DefaultEditorKit.forwardAction,

				"LEFT", DefaultEditorKit.backwardAction,

				"KP_RIGHT", DefaultEditorKit.forwardAction,

				"KP_LEFT", DefaultEditorKit.backwardAction,

				"ENTER", JTextField.notifyAction,

				"ctrl BACK_SLASH", "unselect", "control shift O",
				"toggle-componentOrientation"

		});

		Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,

				"ctrl V", DefaultEditorKit.pasteAction,

				"ctrl X", DefaultEditorKit.cutAction,

				"COPY", DefaultEditorKit.copyAction,

				"PASTE", DefaultEditorKit.pasteAction,

				"CUT", DefaultEditorKit.cutAction,

				"control INSERT", DefaultEditorKit.copyAction,

				"shift INSERT", DefaultEditorKit.pasteAction,

				"shift DELETE", DefaultEditorKit.cutAction,

				"shift LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,

				"shift RIGHT", DefaultEditorKit.selectionForwardAction,

				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,

				"ctrl LEFT", DefaultEditorKit.previousWordAction,

				"ctrl KP_LEFT", DefaultEditorKit.previousWordAction,

				"ctrl RIGHT", DefaultEditorKit.nextWordAction,

				"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,

				"ctrl shift LEFT",
				DefaultEditorKit.selectionPreviousWordAction,

				"ctrl shift KP_LEFT",
				DefaultEditorKit.selectionPreviousWordAction,

				"ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,

				"ctrl shift KP_RIGHT",
				DefaultEditorKit.selectionNextWordAction,

				"ctrl A", DefaultEditorKit.selectAllAction,

				"HOME", DefaultEditorKit.beginLineAction,

				"END", DefaultEditorKit.endLineAction,

				"shift HOME", DefaultEditorKit.selectionBeginLineAction,

				"shift END", DefaultEditorKit.selectionEndLineAction,

				"UP", DefaultEditorKit.upAction,

				"KP_UP", DefaultEditorKit.upAction,

				"DOWN", DefaultEditorKit.downAction,

				"KP_DOWN", DefaultEditorKit.downAction,

				"PAGE_UP", DefaultEditorKit.pageUpAction,

				"PAGE_DOWN", DefaultEditorKit.pageDownAction,

				"shift PAGE_UP", "selection-page-up",

				"shift PAGE_DOWN", "selection-page-down",

				"ctrl shift PAGE_UP", "selection-page-left",

				"ctrl shift PAGE_DOWN", "selection-page-right",

				"shift UP", DefaultEditorKit.selectionUpAction,

				"shift KP_UP", DefaultEditorKit.selectionUpAction,

				"shift DOWN", DefaultEditorKit.selectionDownAction,

				"shift KP_DOWN", DefaultEditorKit.selectionDownAction,

				"ENTER", DefaultEditorKit.insertBreakAction,

				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,

				"ctrl H", DefaultEditorKit.deletePrevCharAction,

				"DELETE", DefaultEditorKit.deleteNextCharAction,

				"ctrl DELETE", DefaultEditorKit.deleteNextWordAction,

				"ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,

				"RIGHT", DefaultEditorKit.forwardAction,

				"LEFT", DefaultEditorKit.backwardAction,

				"KP_RIGHT", DefaultEditorKit.forwardAction,

				"KP_LEFT", DefaultEditorKit.backwardAction,

				"TAB", DefaultEditorKit.insertTabAction,

				"ctrl BACK_SLASH", "unselect",

				"ctrl HOME", DefaultEditorKit.beginAction,

				"ctrl END", DefaultEditorKit.endAction,

				"ctrl shift HOME", DefaultEditorKit.selectionBeginAction,

				"ctrl shift END", DefaultEditorKit.selectionEndAction,

				"ctrl T", "next-link-action",

				"ctrl shift T", "previous-link-action",

				"ctrl SPACE", "activate-link-action",

				"control shift O", "toggle-componentOrientation" });

		Object emptyIcon = new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new IconUIResource(new Icon() {
					public int getIconHeight() {
						// return the value that matches the core height, so
						// that the DefaultTreeCellEditor.EditorContainer
						// returns the correct value in its getPreferredSize
						// when it consults the "editingIcon" height.
						return 16;
					}

					public int getIconWidth() {
						return 2;
					}

					public void paintIcon(Component c, Graphics g, int x, int y) {
					}
				});
			}
		};

		Object[] defaults = new Object[] {
				"control",
				controlText,

				"TextField.focusInputMap",
				fieldInputMap,

				"PasswordField.focusInputMap",
				passwordInputMap,

				"TextArea.focusInputMap",
				multilineInputMap,

				"TextPane.focusInputMap",
				multilineInputMap,

				"EditorPane.focusInputMap",
				multilineInputMap,

				"Button.defaultButtonFollowsFocus",
				Boolean.FALSE,

				"Button.disabledText",
				disabledForegroundColor,

				"Button.foreground",
				foregroundColor,

				"Button.margin",
				new InsetsUIResource(0, 0, 0, 0),

				"CheckBox.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"CheckBox.border",
				new BorderUIResource.CompoundBorderUIResource(
						SubstanceSizeUtils.getCheckBoxBorder(SubstanceSizeUtils
								.getControlFontSize(), ComponentOrientation
								.getOrientation(Locale.getDefault())
								.isLeftToRight()), new MarginBorder()),

				"CheckBox.disabledText",
				disabledForegroundColor,

				"CheckBox.foreground",
				foregroundColor,

				"CheckBoxMenuItem.acceleratorForeground",
				foregroundColor,

				"CheckBoxMenuItem.acceleratorSelectionForeground",
				foregroundColor,

				"CheckBoxMenuItem.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"CheckBoxMenuItem.borderPainted",
				Boolean.FALSE,

				"CheckBoxMenuItem.checkIcon",
				new CheckBoxMenuItemIcon(null, 1 + SubstanceSizeUtils
						.getMenuCheckMarkSize(SubstanceSizeUtils
								.getControlFontSize())),

				"CheckBoxMenuItem.disabledForeground",
				disabledForegroundColor,

				"CheckBoxMenuItem.foreground",
				foregroundColor,

				"CheckBoxMenuItem.margin",
				menuItemInsets,

				"CheckBoxMenuItem.selectionForeground",
				selectionCellForegroundColor,

				"ColorChooser.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ColorChooser.foreground",
				foregroundColor,

				"ComboBox.ancestorInputMap",
				new UIDefaults.LazyInputMap(new Object[] { "ESCAPE",
						"hidePopup",

						"PAGE_UP", "pageUpPassThrough",

						"PAGE_DOWN", "pageDownPassThrough",

						"HOME", "homePassThrough",

						"END", "endPassThrough",

						"DOWN", "selectNext",

						"KP_DOWN", "selectNext",

						"alt DOWN", "togglePopup",

						"alt KP_DOWN", "togglePopup",

						"alt UP", "togglePopup",

						"alt KP_UP", "togglePopup",

						"SPACE", "spacePopup",

						"ENTER", "enterPressed",

						"UP", "selectPrevious",

						"KP_UP", "selectPrevious" }),

				"ComboBox.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ComboBox.border",
				comboBorder,

				"ComboBox.disabledBackground",
				textBackgroundColor,

				"ComboBox.disabledForeground",
				disabledForegroundColor,

				"ComboBox.foreground",
				foregroundColor,

				"ComboBox.selectionBackground",
				selectionCellBackgroundColor,

				"ComboBox.selectionForeground",
				selectionCellForegroundColor,

				"DesktopIcon.border",
				regularMarginBorder,

				"DesktopIcon.width",
				new Integer(140),

				"Desktop.background",
				new ColorUIResource(new Color(0x0, true)),

				"Desktop.foreground",
				foregroundColor,

				"Dialog.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"EditorPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"EditorPane.border",
				textMarginBorder,

				"EditorPane.foreground",
				foregroundColor,

				"EditorPane.caretForeground",
				foregroundColor,

				"EditorPane.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"EditorPane.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"EditorPane.inactiveForeground",
				disabledTextComponentForegroundColor,

				"EditorPane.selectionBackground",
				selectionTextBackgroundColor,

				"EditorPane.selectionForeground",
				selectionTextForegroundColor,

				"FileChooser.upFolderIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/go-up.png");
					}
				},

				"FileChooser.newFolderIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/folder-new.png");
					}
				},

				"FileChooser.homeFolderIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/user-home.png");
					}
				},

				"FileChooser.listViewIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/application_view_list.png");
					}
				},

				"FileChooser.detailsViewIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/application_view_detail.png");
					}
				},

				"FileChooser.viewMenuLabelText",
				"View",

				"FileChooser.refreshActionLabelText",
				"Refresh",

				"FileChooser.newFolderActionLabelText",
				"New Folder",

				"FileChooser.listViewActionLabelText",
				"List",

				"FileChooser.detailsViewActionLabelText",
				"Details",

				"FileChooser.lookInLabelMnemonic",
				new Integer(KeyEvent.VK_I),

				"FileChooser.fileNameLabelMnemonic",
				new Integer(KeyEvent.VK_N),

				"FileChooser.filesOfTypeLabelMnemonic",
				new Integer(KeyEvent.VK_T),

				"FileChooser.usesSingleFilePane",
				Boolean.TRUE,

				"FileChooser.ancestorInputMap",
				new UIDefaults.LazyInputMap(new Object[] { "ESCAPE",
						"cancelSelection", "F2", "editFileName", "F5",
						"refresh", "BACK_SPACE", "Go Up", "ENTER",
						"approveSelection" }),

				"FileView.computerIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/computer.png");
					}
				},

				"FileView.directoryIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/folder.png");
					}
				},

				"FileView.fileIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/text-x-generic.png");
					}
				},

				"FileView.floppyDriveIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/media-floppy.png");
					}
				},

				"FileView.hardDriveIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/drive-harddisk.png");
					}
				},

				"FormattedTextField.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"FormattedTextField.border",
				textBorder,

				"FormattedTextField.caretForeground",
				foregroundColor,

				"FormattedTextField.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"FormattedTextField.foreground",
				foregroundColor,

				"FormattedTextField.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"FormattedTextField.inactiveForeground",
				disabledTextComponentForegroundColor,

				"FormattedTextField.selectionBackground",
				selectionTextBackgroundColor,

				"FormattedTextField.selectionForeground",
				selectionTextForegroundColor,

				"InternalFrame.activeTitleBackground",
				selectionTextForegroundColor,

				"InternalFrame.inactiveTitleBackground",
				foregroundColor,

				"InternalFrame.border",
				new BorderUIResource(new SubstancePaneBorder()),

				"InternalFrame.closeIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceImageCreator.getCloseIcon(
								titlePaneScheme, titlePaneScheme);
					}
				},

				"InternalFrame.iconifyIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceImageCreator.getMinimizeIcon(
								titlePaneScheme, titlePaneScheme);
					}
				},

				"InternalFrame.maximizeIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceImageCreator.getMaximizeIcon(
								titlePaneScheme, titlePaneScheme);
					}
				},

				"InternalFrame.minimizeIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceImageCreator.getRestoreIcon(
								titlePaneScheme, titlePaneScheme);
					}
				},

				"InternalFrame.paletteCloseIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceImageCreator.getCloseIcon(
								titlePaneScheme, titlePaneScheme);
					}
				},

				"Label.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Label.foreground",
				foregroundColor,

				"Label.disabledText",
				disabledForegroundColor,

				"Label.disabledForeground",
				disabledForegroundColor,

				"List.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"List.cellRenderer",
				listCellRendererActiveValue,

				"List.focusCellHighlightBorder",
				new SubstanceBorder(new Insets(1, 1, 1, 1)),

				"List.focusSelectedCellHighlightBorder",
				new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1),

				"List.foreground",
				foregroundColor,

				"List.selectionBackground",
				selectionCellBackgroundColor,

				"List.selectionForeground",
				selectionCellForegroundColor,

				"Menu.arrowIcon",
				menuArrowIcon,

				"Menu.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Menu.borderPainted",
				Boolean.FALSE,

				"Menu.checkIcon",
				null,

				"Menu.disabledForeground",
				disabledForegroundColor,

				"Menu.foreground",
				foregroundColor,

				"Menu.margin",
				menuItemInsets,

				"Menu.selectionForeground",
				selectionCellForegroundColor,

				"MenuBar.background",
				skin.isRegisteredAsDecorationArea(DecorationAreaType.HEADER) ? new ColorUIResource(
						skin.getActiveColorScheme(DecorationAreaType.HEADER)
								.getMidColor())
						: SubstanceColorUtilities.getDefaultBackgroundColor(
								false, skin, false),

				"MenuBar.foreground",
				new ColorUIResource(skin.getActiveColorScheme(
						DecorationAreaType.HEADER).getForegroundColor()),

				"MenuBar.border",
				null,

				"MenuItem.acceleratorForeground",
				foregroundColor,

				"MenuItem.acceleratorSelectionForeground",
				foregroundColor,

				"MenuItem.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"MenuItem.borderPainted",
				Boolean.FALSE,

				"MenuItem.checkIcon",
				null,

				"MenuItem.disabledForeground",
				disabledForegroundColor,

				"MenuItem.foreground",
				foregroundColor,

				"MenuItem.margin",
				menuItemInsets,

				"MenuItem.selectionForeground",
				selectionCellForegroundColor,

				"OptionPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"OptionPane.errorIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/32/dialog-error.png");
					}
				},

				"OptionPane.foreground",
				foregroundColor,

				"OptionPane.informationIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/32/dialog-information.png");
					}
				},

				"OptionPane.messageForeground",
				foregroundColor,

				"OptionPane.questionIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/32/help-browser.png");
					}
				},

				"OptionPane.warningIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return SubstanceCoreUtilities
								.getIcon("resource/32/dialog-warning.png");
					}
				},

				"Panel.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Panel.foreground",
				foregroundColor,

				"PasswordField.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"PasswordField.border",
				textBorder,

				"PasswordField.caretForeground",
				foregroundColor,

				"PasswordField.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"PasswordField.foreground",
				foregroundColor,

				"PasswordField.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"PasswordField.inactiveForeground",
				disabledTextComponentForegroundColor,

				"PasswordField.selectionBackground",
				selectionTextBackgroundColor,

				"PasswordField.selectionForeground",
				selectionTextForegroundColor,

				"PopupMenu.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"PopupMenu.border",
				regularMarginBorder,

				"ProgressBar.border",
				new BorderUIResource(new SubstanceBorder()),

				"ProgressBar.cycleTime",
				new Integer(1000),

				"ProgressBar.repaintInterval",
				new Integer(50),

				"ProgressBar.horizontalSize",
				new DimensionUIResource(146, SubstanceSizeUtils
						.getControlFontSize()),

				"ProgressBar.verticalSize",
				new DimensionUIResource(
						SubstanceSizeUtils.getControlFontSize(), 146),

				"ProgressBar.selectionBackground",
				foregroundColor,

				"ProgressBar.selectionForeground",
				foregroundColor,

				"RadioButton.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"RadioButton.border",
				new BorderUIResource.CompoundBorderUIResource(
						SubstanceSizeUtils.getRadioButtonBorder(
								SubstanceSizeUtils.getControlFontSize(),
								ComponentOrientation.getOrientation(
										Locale.getDefault()).isLeftToRight()),
						new MarginBorder()),

				"RadioButton.foreground",
				foregroundColor,

				"RadioButton.disabledText",
				disabledForegroundColor,

				"RadioButtonMenuItem.acceleratorForeground",
				foregroundColor,

				"RadioButtonMenuItem.acceleratorSelectionForeground",
				foregroundColor,

				"RadioButtonMenuItem.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"RadioButtonMenuItem.borderPainted",
				Boolean.FALSE,

				"RadioButtonMenuItem.checkIcon",
				new RadioButtonMenuItemIcon(null, SubstanceSizeUtils
						.getMenuCheckMarkSize(SubstanceSizeUtils
								.getControlFontSize())),

				"RadioButtonMenuItem.disabledForeground",
				disabledForegroundColor,

				"RadioButtonMenuItem.foreground",
				foregroundColor,

				"RadioButtonMenuItem.margin",
				menuItemInsets,

				"RadioButtonMenuItem.selectionForeground",
				selectionCellForegroundColor,

				"RootPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"RootPane.border",
				new SubstancePaneBorder(),

				"ScrollBar.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ScrollBar.width",
				new Integer(SubstanceSizeUtils
						.getScrollBarWidth(SubstanceSizeUtils
								.getControlFontSize())),

				"ScrollBar.minimumThumbSize",
				new DimensionUIResource(SubstanceSizeUtils
						.getScrollBarWidth(SubstanceSizeUtils
								.getControlFontSize()) - 2, SubstanceSizeUtils
						.getScrollBarWidth(SubstanceSizeUtils
								.getControlFontSize()) - 2),

				"ScrollPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ScrollPane.foreground",
				foregroundColor,

				"ScrollPane.border",
				new SubstanceScrollPaneBorder(),

				"Separator.background",
				backgroundDefaultColor,

				"Separator.foreground",
				lineBwColor,

				"Slider.altTrackColor",
				lineColor,

				"Slider.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Slider.darkShadow",
				lineColor,

				"Slider.focus",
				lineColor,

				"Slider.focusInsets",
				new InsetsUIResource(2, 2, 0, 2),

				"Slider.foreground",
				lineColor,

				"Slider.highlight",
				textBackgroundColor,

				"Slider.shadow",
				lineColor,

				"Slider.tickColor",
				foregroundColor,

				"Spinner.arrowButtonInsets",
				SubstanceSizeUtils
						.getSpinnerArrowButtonInsets(SubstanceSizeUtils
								.getControlFontSize()),

				"Spinner.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"Spinner.border",
				spinnerBorder,

				"Spinner.disableOnBoundaryValues",
				Boolean.TRUE,

				"Spinner.foreground",
				foregroundColor,

				"Spinner.editorBorderPainted",
				Boolean.TRUE,

				"SplitPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"SplitPane.foreground",
				foregroundColor,

				"SplitPane.dividerFocusColor",
				backgroundDefaultColor,

				"SplitPaneDivider.draggingColor",
				backgroundActiveColor,

				"SplitPane.border",
				new BorderUIResource(new EmptyBorder(0, 0, 0, 0)),

				"SplitPane.dividerSize",
				(int) (SubstanceSizeUtils.getArrowIconWidth(SubstanceSizeUtils
						.getControlFontSize()) + SubstanceSizeUtils
						.getAdjustedSize(SubstanceSizeUtils
								.getControlFontSize(), -1, 6, -1, true)),

				"SplitPaneDivider.border",
				new BorderUIResource(new EmptyBorder(1, 1, 1, 1)),

				"TabbedPane.tabAreaBackground",
				backgroundDefaultColor,

				"TabbedPane.unselectedBackground",
				backgroundDefaultColor,

				"TabbedPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"TabbedPane.borderHightlightColor",
				new ColorUIResource(mainActiveScheme.getMidColor()),

				"TabbedPane.contentAreaColor",
				null,

				"TabbedPane.contentBorderInsets",
				new InsetsUIResource(4, 4, 4, 4),

				"TabbedPane.contentOpaque",
				Boolean.FALSE,

				"TabbedPane.darkShadow",
				new ColorUIResource(skin.getColorScheme((Component) null,
						ColorSchemeAssociationKind.BORDER,
						ComponentState.SELECTED).getLineColor()),

				"TabbedPane.focus",
				foregroundColor,

				"TabbedPane.foreground",
				foregroundColor,

				"TabbedPane.highlight",
				new ColorUIResource(mainActiveScheme.getLightColor()),

				"TabbedPane.light",
				mainEnabledScheme.isDark() ? new ColorUIResource(
						SubstanceColorUtilities.getAlphaColor(mainEnabledScheme
								.getUltraDarkColor(), 100))
						: new ColorUIResource(mainEnabledScheme.getLightColor()),

				"TabbedPane.selected",
				new ColorUIResource(mainActiveScheme.getExtraLightColor()),

				"TabbedPane.selectedForeground",
				foregroundColor,

				"TabbedPane.selectHighlight",
				new ColorUIResource(mainActiveScheme.getMidColor()),

				"TabbedPane.shadow",
				new ColorUIResource(SubstanceColorUtilities
						.getInterpolatedColor(mainEnabledScheme
								.getExtraLightColor(), mainEnabledScheme
								.getLightColor(), 0.5)),

				"TabbedPane.tabRunOverlay",
				new Integer(0),

				"Table.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Table.focusCellBackground",
				backgroundActiveColor,

				"Table.focusCellForeground",
				foregroundColor,

				"Table.focusCellHighlightBorder",
				new SubstanceBorder(),

				"Table.foreground",
				foregroundColor,

				"Table.gridColor",
				lineColorDefault,

				"Table.scrollPaneBorder",
				new SubstanceScrollPaneBorder(),

				"Table.selectionBackground",
				selectionCellBackgroundColor,

				"Table.selectionForeground",
				selectionCellForegroundColor,

				"TableHeader.cellBorder",
				null,

				"TableHeader.foreground",
				foregroundColor,

				"TableHeader.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"TextArea.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"TextArea.border",
				textMarginBorder,

				"TextArea.caretForeground",
				foregroundColor,

				"TextArea.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextArea.foreground",
				foregroundColor,

				"TextArea.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextArea.inactiveForeground",
				disabledTextComponentForegroundColor,

				"TextArea.selectionBackground",
				selectionTextBackgroundColor,

				"TextArea.selectionForeground",
				selectionTextForegroundColor,

				"TextField.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"TextField.border",
				textBorder,

				"TextField.caretForeground",
				foregroundColor,

				"TextField.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextField.foreground",
				foregroundColor,

				"TextField.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextField.inactiveForeground",
				disabledTextComponentForegroundColor,

				"TextField.selectionBackground",
				selectionTextBackgroundColor,

				"TextField.selectionForeground",
				selectionTextForegroundColor,

				"TextPane.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						false),

				"TextPane.border",
				textMarginBorder,

				"TextPane.disabledBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextPane.foreground",
				foregroundColor,

				"TextPane.caretForeground",
				foregroundColor,

				"TextPane.inactiveBackground",
				SubstanceColorUtilities.getDefaultBackgroundColor(true, skin,
						true),

				"TextPane.inactiveForeground",
				disabledTextComponentForegroundColor,

				"TextPane.selectionBackground",
				selectionTextBackgroundColor,

				"TextPane.selectionForeground",
				selectionTextForegroundColor,

				"TitledBorder.titleColor",
				foregroundColor,

				"TitledBorder.border",
				new SubstanceEtchedBorder(),

				"ToggleButton.foreground",
				foregroundColor,

				"ToggleButton.disabledText",
				disabledForegroundColor,

				"ToggleButton.margin",
				new InsetsUIResource(0, 0, 0, 0),

				"ToolBar.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ToolBar.border",
				new BorderUIResource(new SubstanceToolBarBorder()),

				"ToolBar.isRollover",
				Boolean.TRUE,

				"ToolBar.foreground",
				foregroundColor,

				"ToolBarSeparator.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ToolBarSeparator.foreground",
				lineBwColor,

				"ToolBar.separatorSize",
				null,

				"ToolTip.border",
				tooltipBorder,

				"ToolTip.borderInactive",
				tooltipBorder,

				"ToolTip.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"ToolTip.backgroundInactive",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						true),

				"ToolTip.foreground",
				foregroundColor,

				"ToolTip.foregroundInactive",
				disabledForegroundColor,

				"Tree.closedIcon",
				emptyIcon,

				"Tree.collapsedIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IconUIResource(SubstanceIconFactory
								.getTreeIcon(null, true));
					}
				},

				"Tree.expandedIcon",
				new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IconUIResource(SubstanceIconFactory
								.getTreeIcon(null, false));
					}
				},

				"Tree.leafIcon",
				emptyIcon,

				"Tree.openIcon",
				emptyIcon,

				"Tree.background",
				SubstanceColorUtilities.getDefaultBackgroundColor(false, skin,
						false),

				"Tree.selectionBackground", selectionCellBackgroundColor,

				"Tree.foreground", foregroundColor,

				"Tree.hash", lineColorDefault,

				"Tree.rowHeight", new Integer(0),

				"Tree.selectionBorderColor", lineColor,

				"Tree.selectionForeground", selectionCellForegroundColor,

				"Tree.textBackground", backgroundDefaultColor,

				"Tree.textForeground", foregroundColor,

				"Viewport.background", backgroundDefaultColor,

				"Viewport.foreground", foregroundColor,

		};
		table.putDefaults(defaults);
	}
}

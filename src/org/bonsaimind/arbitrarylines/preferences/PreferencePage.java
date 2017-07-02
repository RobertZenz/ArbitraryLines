/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonsaimind.arbitrarylines.Activator;
import org.bonsaimind.arbitrarylines.Util;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LinePainter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {
	private Button btnEnableArbitrarylines = null;
	private Button chckCharacterOverrideActive;
	private Label lblCharacterOverrideActive;
	private Label lblCharacterOverrideInformation;
	private Label lblCharacterSizeOverride;
	private Label lblOverrideCharacterHeight;
	private Label lblOverrideCharacterWidth;
	private Link linkCharacterOverrideArbitraryLines;
	private Link linkCharacterOverrideEclipse;
	private Preferences preferences = null;
	private Table table = null;
	private Text txtOverrideCharacterHeight;
	private Text txtOverrideCharacterWidth;
	
	public PreferencePage() {
		super();
	}
	
	@Override
	public void init(IWorkbench workbench) {
		preferences = Activator.getDefault().getPreferences();
	}
	
	@Override
	public boolean performOk() {
		preferences.setEnabled(btnEnableArbitrarylines.getSelection());
		
		List<Line> lines = new ArrayList<Line>();
		
		for (TableItem item : table.getItems()) {
			lines.add((Line)item.getData());
		}
		
		preferences.setLines(lines);
		
		preferences.setCharSizeOverrideActive(chckCharacterOverrideActive.getSelection());
		
		try {
			preferences.setCharHeight(Float.parseFloat(txtOverrideCharacterHeight.getText()));
		} catch (NumberFormatException e) {
			// Ignore the exception.
		}
		
		try {
			preferences.setCharWidth(Float.parseFloat(txtOverrideCharacterWidth.getText()));
		} catch (NumberFormatException e) {
			// Ignore the exception.
		}
		
		updateValuesFromPreferences();
		
		LinePainter.updateFromPreferences();
		Util.redrawAllEditors();
		
		return super.performOk();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			toggleAdvancedConfigurationVisible(false);
		}
		
		super.setVisible(visible);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		btnEnableArbitrarylines = new Button(parent, SWT.CHECK);
		btnEnableArbitrarylines.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnEnableArbitrarylines.setText("Enable ArbitraryLines");
		
		Group grpLines = new Group(parent, SWT.NONE);
		grpLines.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		grpLines.setText("Lines");
		grpLines.setLayout(new GridLayout(2, false));
		
		table = new Table(grpLines, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editCurrentSelection();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void mouseUp(MouseEvent e) {
				// Nothing to do.
			}
		});
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_table.heightHint = 128;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnDirection = new TableColumn(table, SWT.NONE);
		tblclmnDirection.setWidth(80);
		tblclmnDirection.setText("Direction");
		
		TableColumn tblclmnLocationType = new TableColumn(table, SWT.NONE);
		tblclmnLocationType.setWidth(90);
		tblclmnLocationType.setText("Location Type");
		
		TableColumn tblclmnLocation = new TableColumn(table, SWT.NONE);
		tblclmnLocation.setWidth(60);
		tblclmnLocation.setText("Location");
		
		TableColumn tblclmnThickness = new TableColumn(table, SWT.NONE);
		tblclmnThickness.setWidth(50);
		tblclmnThickness.setText("Thickness");
		
		TableColumn tblclmnOffset = new TableColumn(table, SWT.NONE);
		tblclmnOffset.setWidth(50);
		tblclmnOffset.setText("Offset");
		
		TableColumn tblclmnColor = new TableColumn(table, SWT.NONE);
		tblclmnColor.setWidth(60);
		tblclmnColor.setText("Color (RGBA)");
		
		Composite composite = new Composite(grpLines, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Button btnAdd = new Button(composite, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 96;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add...");
		btnAdd.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineEditingDialog dialog = new LineEditingDialog(getShell(), Preferences.DEFAULT_LINE);
				
				if (dialog.open() == Dialog.OK) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(dialog.getLine());
					updateTableItem(item);
				}
			}
		});
		
		Button btnEdit = new Button(composite, SWT.NONE);
		GridData gd_btnEdit = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnEdit.widthHint = 96;
		btnEdit.setLayoutData(gd_btnEdit);
		btnEdit.setText("Edit...");
		btnEdit.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				editCurrentSelection();
			}
		});
		
		Button btnRemove = new Button(composite, SWT.NONE);
		GridData gd_btnRemove = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnRemove.widthHint = 96;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.setText("Remove");
		btnRemove.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() >= 0) {
					table.remove(table.getSelectionIndex());
				}
			}
		});
		
		Button btnMoveUp = new Button(composite, SWT.NONE);
		GridData gd_btnMoveUp = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnMoveUp.widthHint = 96;
		btnMoveUp.setLayoutData(gd_btnMoveUp);
		btnMoveUp.setText("Move up");
		btnMoveUp.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() > 0) {
					TableItem currentItem = table.getItem(table.getSelectionIndex());
					TableItem aboveItem = table.getItem(table.getSelectionIndex() - 1);
					
					Object temp = currentItem.getData();
					currentItem.setData(aboveItem.getData());
					aboveItem.setData(temp);
					
					updateTableItem(currentItem);
					updateTableItem(aboveItem);
					
					table.setSelection(table.getSelectionIndex() - 1);
				}
			}
		});
		
		Button btnMoveDown = new Button(composite, SWT.NONE);
		GridData gd_btnMoveDown = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnMoveDown.widthHint = 96;
		btnMoveDown.setLayoutData(gd_btnMoveDown);
		btnMoveDown.setText("Move down");
		btnMoveDown.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do.
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() >= 0
						&& table.getSelectionIndex() < table.getItemCount() - 1) {
					TableItem currentItem = table.getItem(table.getSelectionIndex());
					TableItem belowItem = table.getItem(table.getSelectionIndex() + 1);
					
					Object temp = currentItem.getData();
					currentItem.setData(belowItem.getData());
					belowItem.setData(temp);
					
					updateTableItem(currentItem);
					updateTableItem(belowItem);
					
					table.setSelection(table.getSelectionIndex() + 1);
				}
			}
		});
		
		Group grpAdvancedConfiguration = new Group(grpLines, SWT.NONE);
		grpAdvancedConfiguration.setLayout(new GridLayout(2, false));
		grpAdvancedConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpAdvancedConfiguration.setText("Advanced Configuration");
		
		lblCharacterOverrideActive = new Label(grpAdvancedConfiguration, SWT.NONE);
		lblCharacterOverrideActive.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		lblCharacterOverrideActive.setText("Attention: The character size override is active!");
		
		Button btnShowAdvancedConfiguration = new Button(grpAdvancedConfiguration, SWT.TOGGLE);
		btnShowAdvancedConfiguration.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnShowAdvancedConfiguration.setText("Show advanced configuration");
		btnShowAdvancedConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = ((Button)e.widget).getSelection();
				
				toggleAdvancedConfigurationVisible(selected);
			}
		});
		
		lblCharacterOverrideInformation = new Label(grpAdvancedConfiguration, SWT.NONE);
		lblCharacterOverrideInformation.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		lblCharacterOverrideInformation.setText(
				"The \"character size override\" allows you to define a custom\ncharacter size which will be used for calculating the position\nof the lines. This can be used if the font measuring of SWT\nis off. For more information, please see the following links.");
		
		linkCharacterOverrideArbitraryLines = new Link(grpAdvancedConfiguration, SWT.NONE);
		linkCharacterOverrideArbitraryLines.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		linkCharacterOverrideArbitraryLines.setText("<a href=\"https://gitlab.com/RobertZenz/ArbitraryLines/issues/8\">ArbitraryLines#8, Location of painted lines is off</a>");
		
		linkCharacterOverrideEclipse = new Link(grpAdvancedConfiguration, SWT.NONE);
		linkCharacterOverrideEclipse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		linkCharacterOverrideEclipse
				.setText("<a href=\"https://bugs.eclipse.org/bugs/show_bug.cgi?id=508600\">Eclipse#508600, The average character width of a font is not integer</a>");
		
		lblCharacterSizeOverride = new Label(grpAdvancedConfiguration, SWT.NONE);
		lblCharacterSizeOverride.setText("Activate character size override");
		
		chckCharacterOverrideActive = new Button(grpAdvancedConfiguration, SWT.CHECK);
		chckCharacterOverrideActive.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		lblOverrideCharacterWidth = new Label(grpAdvancedConfiguration, SWT.NONE);
		lblOverrideCharacterWidth.setText("Override character width");
		
		txtOverrideCharacterWidth = new Text(grpAdvancedConfiguration, SWT.BORDER);
		txtOverrideCharacterWidth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblOverrideCharacterHeight = new Label(grpAdvancedConfiguration, SWT.NONE);
		lblOverrideCharacterHeight.setText("Override character height");
		
		txtOverrideCharacterHeight = new Text(grpAdvancedConfiguration, SWT.BORDER);
		txtOverrideCharacterHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		updateValuesFromPreferences();
		
		return null;
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		
		btnEnableArbitrarylines.setSelection(true);
		updateTable(Arrays.asList(Preferences.DEFAULT_LINE));
		
		lblCharacterOverrideActive.setVisible(false);
		chckCharacterOverrideActive.setSelection(true);
		txtOverrideCharacterHeight.setText(Float.toString(0.0f));
		txtOverrideCharacterWidth.setText(Float.toString(0.0f));
	}
	
	private void editCurrentSelection() {
		if (table.getSelectionIndex() >= 0) {
			TableItem selectedItem = table.getItem(table.getSelectionIndex());
			LineEditingDialog dialog = new LineEditingDialog(getShell(), (Line)selectedItem.getData());
			
			if (dialog.open() == Dialog.OK) {
				selectedItem.setData(dialog.getLine());
				updateTableItem(selectedItem);
			}
		}
	}
	
	private void toggleAdvancedConfigurationVisible(boolean visible) {
		lblCharacterOverrideInformation.setVisible(visible);
		((GridData)lblCharacterOverrideInformation.getLayoutData()).exclude = !visible;
		linkCharacterOverrideArbitraryLines.setVisible(visible);
		((GridData)linkCharacterOverrideArbitraryLines.getLayoutData()).exclude = !visible;
		linkCharacterOverrideEclipse.setVisible(visible);
		((GridData)linkCharacterOverrideEclipse.getLayoutData()).exclude = !visible;
		lblCharacterSizeOverride.setVisible(visible);
		((GridData)lblCharacterSizeOverride.getLayoutData()).exclude = !visible;
		chckCharacterOverrideActive.setVisible(visible);
		((GridData)chckCharacterOverrideActive.getLayoutData()).exclude = !visible;
		lblOverrideCharacterWidth.setVisible(visible);
		((GridData)lblOverrideCharacterWidth.getLayoutData()).exclude = !visible;
		txtOverrideCharacterWidth.setVisible(visible);
		((GridData)txtOverrideCharacterWidth.getLayoutData()).exclude = !visible;
		lblOverrideCharacterHeight.setVisible(visible);
		((GridData)lblOverrideCharacterHeight.getLayoutData()).exclude = !visible;
		txtOverrideCharacterHeight.setVisible(visible);
		((GridData)txtOverrideCharacterHeight.getLayoutData()).exclude = !visible;
		
		lblCharacterOverrideInformation.getParent().pack();
		lblCharacterOverrideInformation.getParent().getParent().pack();
		lblCharacterOverrideInformation.getParent().getParent().getParent().pack();
	}
	
	private void updateTable(Iterable<Line> lines) {
		table.removeAll();
		
		for (Line line : lines) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(line);
			updateTableItem(item);
		}
	}
	
	private void updateTableItem(TableItem item) {
		Line line = (Line)item.getData();
		
		item.setText(0, line.getDirection().toString());
		item.setText(1, line.getLocationType().toString());
		item.setText(2, Integer.toString(line.getLocation()));
		item.setText(3, Integer.toString(line.getThickness()));
		item.setText(4, Integer.toString(line.getOffset()));
		item.setText(5, Util.colorToString(line.getColorAsInt()));
	}
	
	private void updateValuesFromPreferences() {
		btnEnableArbitrarylines.setSelection(preferences.isEnabled());
		
		updateTable(preferences.getLines());
		
		lblCharacterOverrideActive.setVisible(preferences.isCharSizeOverrideActive());
		chckCharacterOverrideActive.setSelection(preferences.isCharSizeOverrideActive());
		txtOverrideCharacterHeight.setText(Float.toString(preferences.getCharHeight()));
		txtOverrideCharacterWidth.setText(Float.toString(preferences.getCharWidth()));
	}
}
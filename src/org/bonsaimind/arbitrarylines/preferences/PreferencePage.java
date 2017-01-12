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
import org.bonsaimind.arbitrarylines.listeners.LinePaintingPaintListener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {
	private Button btnEnableArbitrarylines = null;
	private Preferences preferences = null;
	private Table table = null;
	
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
		
		updateValuesFromPreferences();
		
		LinePaintingPaintListener.INSTANCE.updateFromPreferences();
		Util.redrawAllEditors();
		
		return super.performOk();
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
		
		updateValuesFromPreferences();
		
		return null;
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		
		btnEnableArbitrarylines.setSelection(true);
		updateTable(Arrays.asList(Preferences.DEFAULT_LINE));
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
	}
}
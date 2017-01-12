/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.preferences;

import org.bonsaimind.arbitrarylines.Util;
import org.bonsaimind.arbitrarylines.lines.Direction;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LocationType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LineEditingDialog extends Dialog {
	private Text colorText = null;
	private Combo directionCombo = null;
	private Line line = null;
	private Text locationText = null;
	private Combo locationTypeCombo = null;
	private Text offsetText = null;
	private Text thicknessText = null;;
	
	public LineEditingDialog(Shell parentShell, Line line) {
		super(parentShell);
		
		this.line = line;
	}
	
	public Line getLine() {
		return line;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control content = super.createContents(parent);
		
		Composite container = (Composite)getDialogArea();
		
		container.setLayout(new GridLayout(2, false));
		
		addCaptionLabel(container, "Direction");
		directionCombo = new Combo(container, SWT.BORDER);
		directionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		directionCombo.setItems(new String[] { Direction.HORIZONTAL.toString(), Direction.VERTICAL.toString() });
		directionCombo.setText(line.getDirection().toString());
		
		addCaptionLabel(container, "Location Type");
		locationTypeCombo = new Combo(container, SWT.BORDER);
		locationTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		locationTypeCombo.setItems(new String[] { LocationType.CHARACTER.toString(), LocationType.PIXEL.toString() });
		locationTypeCombo.setText(line.getLocationType().toString());
		
		addCaptionLabel(container, "Location");
		locationText = new Text(container, SWT.BORDER);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		locationText.setText(Integer.toString(line.getLocation()));
		
		addCaptionLabel(container, "Thickness");
		thicknessText = new Text(container, SWT.BORDER);
		thicknessText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		thicknessText.setText(Integer.toString(line.getThickness()));
		
		addCaptionLabel(container, "Offset");
		offsetText = new Text(container, SWT.BORDER);
		offsetText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		offsetText.setText(Integer.toString(line.getOffset()));
		
		addCaptionLabel(container, "Color (RGBA)");
		colorText = new Text(container, SWT.BORDER);
		colorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		colorText.setText(Util.colorToString(line.getColorAsInt()));
		
		return content;
	}
	
	@Override
	protected void okPressed() {
		Direction direction = Direction.valueOf(directionCombo.getText());
		if (direction == null) {
			direction = line.getDirection();
		}
		
		LocationType locationType = LocationType.valueOf(locationTypeCombo.getText());
		if (locationType == null) {
			locationType = line.getLocationType();
		}
		
		int location = line.getLocation();
		try {
			location = Integer.parseInt(locationText.getText());
		} catch (NumberFormatException e) {
			// Ignore the exception.
		}
		
		int thickness = line.getThickness();
		try {
			thickness = Integer.parseInt(thicknessText.getText());
		} catch (NumberFormatException e) {
			// Ignore the exception.
		}
		
		int offset = line.getOffset();
		try {
			offset = Integer.parseInt(offsetText.getText());
		} catch (NumberFormatException e) {
			// Ignore the exception.
		}
		
		int color = Util.colorFromString(colorText.getText());
		
		line = new Line(direction, locationType, location, thickness, offset, color);
		
		super.okPressed();
	}
	
	private final void addCaptionLabel(Composite composite, String caption) {
		Label captionLabel = new Label(composite, SWT.NONE);
		captionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		captionLabel.setText(caption);
	}
}

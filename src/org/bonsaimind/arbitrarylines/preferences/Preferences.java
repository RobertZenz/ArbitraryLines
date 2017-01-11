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
import java.util.Collections;
import java.util.List;

import org.bonsaimind.arbitrarylines.lines.Direction;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LocationType;
import org.eclipse.jface.preference.IPreferenceStore;

public class Preferences {
	public static final Line DEFAULT_LINE = new Line(Direction.VERTICAL, LocationType.CHARACTER, 80, 1, 0, 0x3465a4cc);
	private static final String LINES_SEPARATOR = ";";
	private static final String PREFERENCE_NAME_ENABLED = "enabled";
	private static final String PREFERENCE_NAME_LINES = "lines";
	private static final String VALUES_SEPARATOR = ",";
	private IPreferenceStore preferenceStore = null;
	
	public Preferences(IPreferenceStore preferenceStore) {
		super();
		
		this.preferenceStore = preferenceStore;
		
		preferenceStore.setDefault(PREFERENCE_NAME_ENABLED, true);
		preferenceStore.setDefault(PREFERENCE_NAME_LINES, lineToString(DEFAULT_LINE));
	}
	
	private static final Line lineFromString(String lineAsString) {
		if (lineAsString == null || lineAsString.length() == 0) {
			return null;
		}
		
		String[] splitted = lineAsString.split(VALUES_SEPARATOR);
		
		return new Line(
				Direction.valueOf(splitted[0]),
				LocationType.valueOf(splitted[1]),
				Integer.parseInt(splitted[2]),
				Integer.parseInt(splitted[3]),
				Integer.parseInt(splitted[4]),
				Integer.parseInt(splitted[5], 16));
	}
	
	private static final String lineToString(Line line) {
		if (line == null) {
			return "";
		}
		
		StringBuilder lineAsString = new StringBuilder();
		lineAsString.append(line.getDirection().toString()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getLocationType().toString()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getLocation()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getThickness()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getOffset()).append(VALUES_SEPARATOR);
		lineAsString.append(Integer.toString(line.getColorAsInt(), 16)).append(VALUES_SEPARATOR);
		
		return lineAsString.toString();
	}
	
	public List<Line> getLines() {
		String linesAsString = preferenceStore.getString(PREFERENCE_NAME_LINES);
		
		if (linesAsString == null || linesAsString.length() == 0) {
			return Collections.emptyList();
		}
		
		List<Line> lines = new ArrayList<Line>();
		
		for (String lineAsString : linesAsString.split(LINES_SEPARATOR)) {
			Line line = lineFromString(lineAsString);
			
			if (line != null) {
				lines.add(line);
			}
		}
		
		return lines;
	}
	
	public boolean isEnabled() {
		return preferenceStore.getBoolean(PREFERENCE_NAME_ENABLED);
	}
	
	public void restoreDefaults() {
		preferenceStore.setToDefault(PREFERENCE_NAME_ENABLED);
		preferenceStore.setToDefault(PREFERENCE_NAME_LINES);
	}
	
	public void setEnabled(boolean enabled) {
		preferenceStore.setValue(PREFERENCE_NAME_ENABLED, enabled);
	}
	
	public void setLines(Iterable<Line> lines) {
		if (lines == null) {
			preferenceStore.setValue(PREFERENCE_NAME_LINES, "");
			return;
		}
		
		StringBuilder linesAsString = new StringBuilder();
		
		for (Line line : lines) {
			linesAsString.append(lineToString(line)).append(LINES_SEPARATOR);
		}
		
		preferenceStore.setValue(PREFERENCE_NAME_LINES, linesAsString.toString());
	}
}

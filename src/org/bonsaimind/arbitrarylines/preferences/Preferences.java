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

import org.bonsaimind.arbitrarylines.Activator;
import org.bonsaimind.arbitrarylines.Util;
import org.bonsaimind.arbitrarylines.lines.Direction;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LineStyle;
import org.bonsaimind.arbitrarylines.lines.LocationType;
import org.eclipse.jface.preference.IPreferenceStore;

public class Preferences {
	public static final Line DEFAULT_LINE = new Line(Direction.VERTICAL, LocationType.CHARACTER, 80, 1, 0, 0x3465a4cc, LineStyle.SOLID, true);
	private static final String LINES_SEPARATOR = ";";
	private static final String PREFERENCE_NAME_CHAR_HEIGHT = Activator.PLUGIN_ID + ".char.height";
	private static final String PREFERENCE_NAME_CHAR_SIZE_OVERRIDE = Activator.PLUGIN_ID + ".char.size_override";
	private static final String PREFERENCE_NAME_CHAR_WIDTH = Activator.PLUGIN_ID + ".char.width";
	private static final String PREFERENCE_NAME_ENABLED = Activator.PLUGIN_ID + ".enabled";
	private static final String PREFERENCE_NAME_LINES = Activator.PLUGIN_ID + ".lines";
	private static final String VALUES_SEPARATOR = ",";
	private IPreferenceStore preferenceStore = null;
	
	public Preferences(IPreferenceStore preferenceStore) {
		super();
		
		this.preferenceStore = preferenceStore;
		
		preferenceStore.setDefault(PREFERENCE_NAME_CHAR_HEIGHT, 0.0f);
		preferenceStore.setDefault(PREFERENCE_NAME_CHAR_SIZE_OVERRIDE, false);
		preferenceStore.setDefault(PREFERENCE_NAME_CHAR_WIDTH, 0.0f);
		preferenceStore.setDefault(PREFERENCE_NAME_ENABLED, true);
		preferenceStore.setDefault(PREFERENCE_NAME_LINES, lineToString(DEFAULT_LINE));
	}
	
	protected Preferences() {
		super();
	}
	
	protected static final Line lineFromString(String lineAsString) {
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
				Util.colorFromString(splitted[5]),
				LineStyle.valueOf(splitted[6]),
				splitted.length >= 8 ? Boolean.parseBoolean(splitted[7]) : true);
	}
	
	protected static final String lineToString(Line line) {
		if (line == null) {
			return "";
		}
		
		StringBuilder lineAsString = new StringBuilder();
		lineAsString.append(line.getDirection().toString()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getLocationType().toString()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getLocation()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getThickness()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getOffset()).append(VALUES_SEPARATOR);
		lineAsString.append(Util.colorToString(line.getColorAsInt())).append(VALUES_SEPARATOR);
		lineAsString.append(line.getStyle().toString()).append(VALUES_SEPARATOR);
		lineAsString.append(line.getVisible()).append(VALUES_SEPARATOR);
		
		return lineAsString.toString();
	}
	
	public double getCharHeight() {
		return preferenceStore.getDouble(PREFERENCE_NAME_CHAR_HEIGHT);
	}
	
	public double getCharWidth() {
		return preferenceStore.getDouble(PREFERENCE_NAME_CHAR_WIDTH);
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
	
	public boolean isCharSizeOverrideActive() {
		return preferenceStore.getBoolean(PREFERENCE_NAME_CHAR_SIZE_OVERRIDE);
	}
	
	public boolean isEnabled() {
		return preferenceStore.getBoolean(PREFERENCE_NAME_ENABLED);
	}
	
	public void restoreDefaults() {
		preferenceStore.setToDefault(PREFERENCE_NAME_ENABLED);
		preferenceStore.setToDefault(PREFERENCE_NAME_LINES);
	}
	
	public void setCharHeight(double charHeight) {
		preferenceStore.setValue(PREFERENCE_NAME_CHAR_HEIGHT, charHeight);
	}
	
	public void setCharSizeOverrideActive(boolean active) {
		preferenceStore.setValue(PREFERENCE_NAME_CHAR_SIZE_OVERRIDE, active);
	}
	
	public void setCharWidth(double charWidth) {
		preferenceStore.setValue(PREFERENCE_NAME_CHAR_WIDTH, charWidth);
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

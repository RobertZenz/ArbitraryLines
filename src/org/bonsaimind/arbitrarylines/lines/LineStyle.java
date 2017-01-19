/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.lines;

import org.eclipse.swt.SWT;

public enum LineStyle {
	SOLID(SWT.LINE_SOLID), DASH(SWT.LINE_DASH), DOT(SWT.LINE_DOT), DASHDOT(SWT.LINE_DASHDOT), DASHDOTDOT(SWT.LINE_DASHDOTDOT);
	
	private static final String[] styleStrings = new String[DASHDOTDOT.ordinal() + 1];
	private final int swtStyle;
	
	static {
		for (LineStyle type : LineStyle.values()) {
			styleStrings[type.ordinal()] = type.toString();
		}
	}
	
	private LineStyle(int swtEquivalent) {
		swtStyle = swtEquivalent;
	}
	
	public static final String[] getStyleStrings() {
		return styleStrings;
	}
	
	public int getSwtStyle() {
		return swtStyle;
	}
}

/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

/**
 * The {@link LinePainter} is the main class which does paint the lines.
 */
public class LinePainter implements PaintListener {
	/** The shared instance which should be used whenever possible. */
	public static final LinePainter INSTANCE = new LinePainter();
	
	/**
	 * Creates a new instance of {@link LinePainter}.
	 */
	public LinePainter() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		if (event.getSource() instanceof StyledText) {
			StyledText styledText = (StyledText)event.getSource();
			
			// TODO Draw the lines here.
		}
	}
}

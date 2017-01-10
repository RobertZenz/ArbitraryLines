/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The {@link LinePaintingPaintListener} is the main class which does paint the lines.
 */
public class LinePaintingPaintListener implements PaintListener {
	/** The shared instance which should be used whenever possible. */
	public static final LinePaintingPaintListener INSTANCE = new LinePaintingPaintListener();
	
	/**
	 * Creates a new instance of {@link LinePaintingPaintListener}.
	 */
	public LinePaintingPaintListener() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		if (event.getSource() instanceof StyledText) {
			StyledText styledText = (StyledText)event.getSource();
			
			int charWidth = event.gc.getAdvanceWidth('B');
			int linePosition = charWidth * 80;
			
			Rectangle clipping = event.gc.getClipping();
			
			if ((styledText.getHorizontalPixel() + clipping.x) <= linePosition
					&& (styledText.getHorizontalPixel() + clipping.x + clipping.width) >= linePosition) {
				
				event.gc.drawLine(
						linePosition - styledText.getHorizontalPixel(),
						clipping.y,
						linePosition - styledText.getHorizontalPixel(),
						clipping.y + clipping.height);
			}
			
			// TODO Draw the lines here.
		}
	}
}

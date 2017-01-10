/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bonsaimind.arbitrarylines.lines.Direction;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LocationType;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The {@link LinePaintingPaintListener} is the main class which does paint the
 * lines.
 */
public class LinePaintingPaintListener implements PaintListener {
	/** The shared instance which should be used whenever possible. */
	public static final LinePaintingPaintListener INSTANCE = new LinePaintingPaintListener();
	
	// TODO Remove this, as it is for debuggin and testing only.
	private List<Line> lines = new ArrayList<Line>();
	
	/**
	 * Creates a new instance of {@link LinePaintingPaintListener}.
	 */
	public LinePaintingPaintListener() {
		super();
		
		lines.add(new Line(Direction.HORIZONTAL, LocationType.CHARACTER, 25, 1, 0, 0xff0000ff));
		lines.add(new Line(Direction.HORIZONTAL, LocationType.PIXEL, 32, 3, 0, 0x00ff00ff));
		
		lines.add(new Line(Direction.VERTICAL, LocationType.CHARACTER, 80, 6, 3, 0xffff0050));
		lines.add(new Line(Direction.VERTICAL, LocationType.PIXEL, 32, 12, 0, 0xff00ffff));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		if (event.getSource() instanceof StyledText) {
			// Store these values so that we can restore them later.
			Color previousBackgroundColor = event.gc.getBackground();
			Color previousForegroundColor = event.gc.getForeground();
			int previousAlphaValue = event.gc.getAlpha();
			
			StyledText styledText = (StyledText)event.getSource();
			
			Rectangle clipping = event.gc.getClipping();
			
			int lineHeight = styledText.getLineHeight();
			int charWidth = event.gc.getAdvanceWidth('B');
			
			for (Line line : lines) {
				paintLine(line, event.gc, clipping, styledText, charWidth, lineHeight);
			}
			
			// Restore the previously stored values.
			event.gc.setBackground(previousBackgroundColor);
			event.gc.setForeground(previousForegroundColor);
			event.gc.setAlpha(previousAlphaValue);
		}
	}
	
	/**
	 * Paints the given {@link Line} into the given {@link GC}.
	 * 
	 * @param line The {@link Line} to paint.
	 * @param gc The {@link GC} into which to paint.
	 * @param drawnRegion The {@link Rectangle} of the region that is drawn (aka
	 *        clipping region).
	 * @param styledText The parent {@link StyledText}.
	 * @param charWidth The width of one character.
	 * @param charHeight The height of one character.
	 */
	private void paintLine(
			Line line,
			GC gc,
			Rectangle drawnRegion,
			StyledText styledText,
			int charWidth,
			int charHeight) {
		
		// The -1 and +1 further down are fixing graphical artifacts when
		// scrolling.
		
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		
		switch (line.getDirection()) {
			case HORIZONTAL:
				switch (line.getLocationType()) {
					case CHARACTER:
						y = charHeight * line.getLocation();
						break;
					
					case PIXEL:
						y = line.getLocation();
						break;
				}
				
				y = y + line.getOffset();
				height = line.getThickness();
				
				// Check if we are inside the drawn bounds.
				if ((styledText.getTopPixel() + drawnRegion.y) > (y + height)
						|| (styledText.getTopPixel() + drawnRegion.y + drawnRegion.height) < y) {
					return;
				}
				
				y = y - styledText.getTopPixel();
				x = drawnRegion.x - 1;
				width = drawnRegion.width + 1;
				break;
			
			case VERTICAL:
				switch (line.getLocationType()) {
					case CHARACTER:
						x = charWidth * line.getLocation();
						break;
					
					case PIXEL:
						x = line.getLocation();
						break;
				}
				
				x = x + line.getOffset();
				width = line.getThickness();
				
				// Check if we are inside the drawn bounds.
				if ((styledText.getHorizontalPixel() + drawnRegion.x) > (x + width)
						|| (styledText.getHorizontalPixel() + drawnRegion.x + drawnRegion.width) < x) {
					return;
				}
				
				x = x - styledText.getHorizontalPixel();
				y = drawnRegion.y - 1;
				height = drawnRegion.height + 1;
				break;
		}
		
		gc.setAlpha(line.getColor().getAlpha());
		gc.setBackground(line.getColor());
		gc.setForeground(line.getColor());
		
		gc.fillRectangle(x, y, width, height);
	}
}

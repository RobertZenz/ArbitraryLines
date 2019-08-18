/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.lines;

import java.util.List;

import org.bonsaimind.arbitrarylines.Activator;
import org.bonsaimind.arbitrarylines.listeners.LinePaintingPaintListener;
import org.bonsaimind.arbitrarylines.preferences.Preferences;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Rectangle;

/**
 * {@link LinePainter} is the main drawing class, which handles drawing the
 * lines.
 */
public final class LinePainter {
	/** The override for the height of one character. */
	private static float charHeightOverride = 0.0f;
	
	/** If the override of the character size is active. */
	private static boolean charSizeOverrideActive = false;
	
	/** The override for the width of one character. */
	private static float charWidthOverride = 0.0f;
	
	/** If the drawing is enabled. */
	private static boolean enabled = true;
	
	/** The locally cached {@link List} of {@link Line}s to draw. */
	private static List<Line> lines = null;
	
	static {
		updateFromPreferences();
	}
	
	/**
	 * No instances, static utility.
	 */
	private LinePainter() {
		// Nothing to do, no instancing.
	}
	
	/**
	 * Paints the lines into the given {@link GC} based on the given context.
	 * 
	 * @param foldingTextViewer The {@link ITextViewerExtension5 folding
	 *        TextViewer} which is the parent.
	 * @param styledText The {@link StyledText} which to draw to.
	 * @param gc The {@link GC} to use.
	 */
	public static final void paintLines(
			ITextViewerExtension5 foldingTextViewer,
			StyledText styledText,
			GC gc) {
		if (!enabled) {
			return;
		}
		
		// Store these values so that we can restore them later.
		LineAttributes previousLineAttributes = gc.getLineAttributes();
		Color previousForegroundColor = gc.getForeground();
		int previousAlphaValue = gc.getAlpha();
		
		// Now let us draw all lines.
		Rectangle clipping = gc.getClipping();
		
		float charHeight = 0.0f;
		float charWidth = 0.0f;
		
		if (charSizeOverrideActive) {
			charHeight = charHeightOverride;
			charWidth = charWidthOverride;
		} else {
			charHeight = styledText.getLineHeight();
			charWidth = gc.getFontMetrics().getAverageCharWidth();
		}
		
		for (Line line : lines) {
			paintLine(foldingTextViewer, styledText, gc, line, clipping, charWidth, charHeight);
		}
		
		// Restore the previously stored values.
		gc.setAlpha(previousAlphaValue);
		gc.setForeground(previousForegroundColor);
		gc.setLineAttributes(previousLineAttributes);
	}
	
	/**
	 * Updates all {@link LinePaintingPaintListener} to the new preferences.
	 */
	public static void updateFromPreferences() {
		Preferences preferences = Activator.getDefault().getPreferences();
		
		enabled = preferences.isEnabled();
		lines = preferences.getLines();
		
		charSizeOverrideActive = preferences.isCharSizeOverrideActive();
		charHeightOverride = preferences.getCharHeight();
		charWidthOverride = preferences.getCharWidth();
	}
	
	/**
	 * Paints the given {@link Line} into the given {@link GC}.
	 * 
	 * @param foldingTextViewer The {@link ITextViewerExtension5 folding
	 *        TextViewer} which is the parent.
	 * @param styledText The {@link StyledText} which to draw to.
	 * @param gc The {@link GC} into which to paint.
	 * @param line The {@link Line} to paint.
	 * @param drawnRegion The {@link Rectangle} of the region that is drawn (aka
	 *        clipping region).
	 * @param charWidth The width of one character.
	 * @param charHeight The height of one character.
	 */
	private static final void paintLine(
			ITextViewerExtension5 foldingTextViewer,
			StyledText styledText,
			GC gc,
			Line line,
			Rectangle drawnRegion,
			float charWidth,
			float charHeight) {
		
		// The -1 and +1 further down are fixing graphical artifacts when
		// scrolling.
		
		int fromX = 0;
		int fromY = 0;
		int toX = 0;
		int toY = 0;
		
		// The +1 is a cheap and stupid way of rounding up.
		int thicknessOffset = (line.getThickness() + 1) / 2;
		
		switch (line.getDirection()) {
			case HORIZONTAL:
				switch (line.getLocationType()) {
					case CHARACTER:
						if (foldingTextViewer != null) {
							int widgetLine = foldingTextViewer.modelLine2WidgetLine(line.getLocation());
							
							if (widgetLine == -1) {
								// The line is currently not being displayed
								// because it has been folded.
								return;
							}
							
							fromY = Math.round(charHeight * widgetLine);
						} else {
							fromY = Math.round(charHeight * line.getLocation());
						}
						break;
					
					case PIXEL:
						fromY = line.getLocation();
						break;
				}
				
				fromY = fromY + line.getOffset() + styledText.getTopMargin();
				
				// Check if we are inside the drawn bounds.
				if ((styledText.getTopPixel() + drawnRegion.y) > (fromY + thicknessOffset)
						|| (styledText.getTopPixel() + drawnRegion.y + drawnRegion.height) < (fromY - thicknessOffset)) {
					return;
				}
				
				fromY = fromY - styledText.getTopPixel();
				toY = fromY;
				fromX = drawnRegion.x - 1;
				toX = drawnRegion.x + drawnRegion.width + 1;
				break;
			
			case VERTICAL:
				switch (line.getLocationType()) {
					case CHARACTER:
						fromX = Math.round(charWidth * line.getLocation());
						break;
					
					case PIXEL:
						fromX = line.getLocation();
						break;
				}
				
				fromX = fromX + line.getOffset() + styledText.getLeftMargin();
				
				// Check if we are inside the drawn bounds.
				if ((styledText.getHorizontalPixel() + drawnRegion.x) > (fromX + thicknessOffset)
						|| (styledText.getHorizontalPixel() + drawnRegion.x + drawnRegion.width) < (fromX - thicknessOffset)) {
					return;
				}
				
				fromX = fromX - styledText.getHorizontalPixel();
				toX = fromX;
				fromY = drawnRegion.y - 1;
				toY = drawnRegion.y + drawnRegion.height + 1;
				break;
		}
		
		gc.setAlpha(line.getColor().getAlpha());
		gc.setForeground(line.getColor());
		gc.setLineWidth(line.getThickness());
		gc.setLineStyle(line.getStyle().getSwtStyle());
		
		gc.drawLine(fromX, fromY, toX, toY);
	}
}

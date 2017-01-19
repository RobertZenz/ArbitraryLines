/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import java.util.List;

import org.bonsaimind.arbitrarylines.Activator;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The {@link LinePaintingPaintListener} is the main class which does paint the
 * lines.
 */
public class LinePaintingPaintListener implements PaintListener {
	/** If the drawing is enabled. */
	private static boolean enabled = true;
	
	/** The locally cached {@link List} of {@link Line}s to draw. */
	private static List<Line> lines = null;
	
	/** The {@link ITextViewerExtension5 folding TextViewer}, if any. */
	private ITextViewerExtension5 foldingTextViewer = null;
	
	/**
	 * Creates a new instance of {@link LinePaintingPaintListener}.
	 * 
	 * @param textViewer The "parent" {@link ITextViewer}.
	 */
	public LinePaintingPaintListener(ITextViewer textViewer) {
		super();
		
		if (textViewer instanceof ITextViewerExtension5) {
			foldingTextViewer = (ITextViewerExtension5)textViewer;
		}
		
		updateFromPreferences();
	}
	
	/**
	 * Updates all {@link LinePaintingPaintListener} to the new preferences.
	 */
	public static void updateFromPreferences() {
		enabled = Activator.getDefault().getPreferences().isEnabled();
		lines = Activator.getDefault().getPreferences().getLines();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		if (!enabled) {
			return;
		}
		
		if (event.getSource() instanceof StyledText) {
			// Store these values so that we can restore them later.
			LineAttributes previousLineAttributes = event.gc.getLineAttributes();
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
			event.gc.setAlpha(previousAlphaValue);
			event.gc.setForeground(previousForegroundColor);
			event.gc.setLineAttributes(previousLineAttributes);
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
	private final void paintLine(
			Line line,
			GC gc,
			Rectangle drawnRegion,
			StyledText styledText,
			int charWidth,
			int charHeight) {
		
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
							
							fromY = charHeight * widgetLine;
						} else {
							fromY = charHeight * line.getLocation();
						}
						break;
					
					case PIXEL:
						fromY = line.getLocation();
						break;
				}
				
				fromY = fromY + line.getOffset();
				
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
						fromX = charWidth * line.getLocation();
						break;
					
					case PIXEL:
						fromX = line.getLocation();
						break;
				}
				
				fromX = fromX + line.getOffset();
				
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

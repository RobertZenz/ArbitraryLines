/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import org.bonsaimind.arbitrarylines.lines.LinePainter;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

/**
 * The {@link LinePaintingPaintListener} is the main class which does paint the
 * lines.
 */
public class LinePaintingPaintListener implements PaintListener {
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
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		if (event.getSource() instanceof StyledText) {
			LinePainter.paintLines(foldingTextViewer, (StyledText)event.getSource(), event.gc);
		}
	}
}

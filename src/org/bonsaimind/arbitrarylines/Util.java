/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * Provides static utility functions.
 */
public final class Util {
	/**
	 * The cached {@link Method} {@code getSourceViewer} of
	 * {@link AbstractTextEditor}.
	 */
	private static Method getSourceViewerMethod = null;
	
	/**
	 * No instancing.
	 */
	private Util() {
		// No instancing.
	}
	
	/**
	 * Gets the color as int value from the given {@link String}.
	 * 
	 * @param string The {@link String} value. Should be in the format
	 *        {@code RRGGBB} or {@code RRGGBBAA}.
	 * @return The color as int value.
	 */
	public final static int colorFromString(String string) {
		if (string == null || string.length() == 0) {
			return 0x000000ff;
		}
		
		String padded = "";
		
		if (string.length() == 6) {
			padded = string + "ff";
		} else if (string.length() > 8) {
			padded = string.substring(0, 8);
		} else {
			padded = "00000000".substring(0, 8 - string.length()) + string;
		}
		
		try {
			return Integer.parseInt(padded.substring(0, 2), 16) << 24
					| Integer.parseInt(padded.substring(2, 4), 16) << 16
					| Integer.parseInt(padded.substring(4, 6), 16) << 8
					| Integer.parseInt(padded.substring(6, 8), 16);
		} catch (NumberFormatException e) {
			// Ignore the exception, we do not need to log it or know that it
			// happened, really.
			return 0x000000ff;
		}
	}
	
	/**
	 * Turns the color into a hex string.
	 * 
	 * @param color The color as int value.
	 * @return The well formatted hex string.
	 */
	public final static String colorToString(int color) {
		return String.format("%08x", Integer.valueOf(color));
	}
	
	/**
	 * Turns the color into a hex string.
	 * 
	 * @param rgb The color as an {@link RGB} object.
	 * @param alpha The alpha setting.
	 * @return The well formatted hex string.
	 */
	public final static String colorToString(RGB rgb, int alpha) {
		if (rgb == null) {
			return "000000" + String.format("%02x", Integer.valueOf(alpha & 0xff));
		}
		
		return String.format("%02x%02x%02x%02x",
				Integer.valueOf(rgb.red),
				Integer.valueOf(rgb.green),
				Integer.valueOf(rgb.blue),
				Integer.valueOf(alpha & 0xff));
	}
	
	/**
	 * Gets the {@link ITextViewer} from the given {@link AbstractTextEditor}.
	 * 
	 * @param textEditor The {@link AbstractTextEditor} from which to get the
	 *        {@link ITextViewer}.
	 * @return The {@link ITextViewer}. {@code null} if it could not be
	 *         returned.
	 */
	public static final ITextViewer getTextViewer(AbstractTextEditor textEditor) {
		if (textEditor == null) {
			return null;
		}
		
		if (getSourceViewerMethod == null) {
			if (!initGetSourceViewerMethod()) {
				return null;
			}
		}
		
		try {
			Object returnedValue = getSourceViewerMethod.invoke(textEditor);
			
			if (returnedValue instanceof ITextViewerExtension2) {
				return (ITextViewer)returnedValue;
			}
		} catch (Throwable th) {
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, th.getMessage()));
		}
		
		return null;
	}
	
	/**
	 * Redraws all open editors.
	 */
	public static final void redrawAllEditors() {
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				for (IEditorReference reference : page.getEditorReferences()) {
					IEditorPart part = reference.getEditor(false);
					
					if (part instanceof AbstractTextEditor) {
						AbstractTextEditor textEditor = (AbstractTextEditor)part;
						ITextViewer textViewer = Util.getTextViewer(textEditor);
						
						if (textViewer != null) {
							textViewer.getTextWidget().redraw();
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Initializes the {@link #getSourceViewerMethod}.
	 * 
	 * @return {@code true} if it was successfully initialized.
	 */
	private static final boolean initGetSourceViewerMethod() {
		try {
			Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
			method.setAccessible(true);
			
			getSourceViewerMethod = method;
			
			return true;
		} catch (Throwable th) {
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, th.getMessage()));
		}
		
		return false;
	}
}

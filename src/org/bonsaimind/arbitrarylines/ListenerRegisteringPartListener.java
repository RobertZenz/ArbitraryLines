
package org.bonsaimind.arbitrarylines;
/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class ListenerRegisteringPartListener implements IPartListener2 {
	public static final ListenerRegisteringPartListener INSTANCE = new ListenerRegisteringPartListener();
	private static Method getSourceViewerMethod = null;
	
	public ListenerRegisteringPartListener() {
		super();
	}
	
	public static void registerListener(IWorkbenchPartReference partRef) {
		if (partRef != null) {
			IWorkbenchPart part = partRef.getPart(false);
			
			if (part instanceof AbstractTextEditor) {
				AbstractTextEditor textEditor = (AbstractTextEditor)part;
				ITextViewer textViewer = getTextViewer(textEditor);
				
				if (textViewer != null) {
					textViewer.getTextWidget().addPaintListener(LinePainter.INSTANCE);
				}
			}
		}
	}
	
	private static final ITextViewer getTextViewer(AbstractTextEditor textEditor) {
		if (getSourceViewerMethod == null) {
			if (!initAccess()) {
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
	
	private static final boolean initAccess() {
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
	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		registerListener(partRef);
	}
	
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
}

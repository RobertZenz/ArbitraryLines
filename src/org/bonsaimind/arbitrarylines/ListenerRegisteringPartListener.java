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
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * The {@link ListenerRegisteringPartListener} is adding listeners to all parts.
 */
public class ListenerRegisteringPartListener implements IPartListener2 {
	/** The shared instance which should be used whenever possible. */
	public static final ListenerRegisteringPartListener INSTANCE = new ListenerRegisteringPartListener();
	/**
	 * The cached {@link Method} {@code getSourceViewer} of
	 * {@link AbstractTextEditor}.
	 */
	private static Method getSourceViewerMethod = null;
	
	/**
	 * Creates a new instance of {@link ListenerRegisteringPartListener}.
	 */
	public ListenerRegisteringPartListener() {
		super();
	}
	
	/**
	 * Registers the listeners on the given {@link IWorkbenchPartReference}.
	 * 
	 * @param partRef The {@link IWorkbenchPartReference} on which to register
	 *        the listeners.
	 */
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
	
	/**
	 * Gets the {@link ITextViewer} from the given {@link AbstractTextEditor}.
	 * 
	 * @param textEditor The {@link AbstractTextEditor} from which to get the
	 *        {@link ITextViewer}.
	 * @return The {@link ITextViewer}. {@code null} if it could not be
	 *         returned.
	 */
	private static final ITextViewer getTextViewer(AbstractTextEditor textEditor) {
		if (textEditor != null) {
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		registerListener(partRef);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Nothing to do.
	}
}

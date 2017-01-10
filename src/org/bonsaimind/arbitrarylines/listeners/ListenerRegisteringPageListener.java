/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The {@link ListenerRegisteringPageListener} is adding listeners to all pages.
 */
public class ListenerRegisteringPageListener implements IPageListener {
	/** The shared instance which should be used whenever possible. */
	public static final ListenerRegisteringPageListener INSTANCE = new ListenerRegisteringPageListener();
	
	/**
	 * Creates a new instance of {@link ListenerRegisteringPageListener}.
	 */
	public ListenerRegisteringPageListener() {
		super();
	}
	
	/**
	 * Registers the listeners on the given {@link IWorkbenchPage}.
	 * 
	 * @param workbenchPage The {@link IWorkbenchPage} on which to register the
	 *        listeners.
	 */
	public static void registerListener(IWorkbenchPage workbenchPage) {
		if (workbenchPage != null) {
			for (IEditorReference editorReference : workbenchPage.getEditorReferences()) {
				ListenerRegisteringPartListener.registerListener(editorReference);
			}
			
			workbenchPage.addPartListener(ListenerRegisteringPartListener.INSTANCE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pageActivated(IWorkbenchPage page) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pageClosed(IWorkbenchPage page) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pageOpened(IWorkbenchPage page) {
		registerListener(page);
	}
}

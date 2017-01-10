/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * The {@link ListenerRegisteringWindowListener} is adding listeners to all
 * parts.
 */
public class ListenerRegisteringWindowListener implements IWindowListener {
	/** The shared instance which should be used whenever possible. */
	public static final ListenerRegisteringWindowListener INSTANCE = new ListenerRegisteringWindowListener();
	
	/**
	 * Creates a new instance of {@link ListenerRegisteringWindowListener}.
	 */
	public ListenerRegisteringWindowListener() {
		super();
	}
	
	/**
	 * Registers the listeners on the given {@link IWorkbenchWindow}.
	 * 
	 * @param workbenchWindow The {@link IWorkbenchWindow} on which to register
	 *        the listeners.
	 */
	public static final void registerListener(IWorkbenchWindow workbenchWindow) {
		if (workbenchWindow != null) {
			for (IWorkbenchPage workbenchPage : workbenchWindow.getPages()) {
				ListenerRegisteringPageListener.registerListener(workbenchPage);
			}
			
			workbenchWindow.addPageListener(ListenerRegisteringPageListener.INSTANCE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowActivated(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowClosed(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowOpened(IWorkbenchWindow window) {
		registerListener(window);
	}
}

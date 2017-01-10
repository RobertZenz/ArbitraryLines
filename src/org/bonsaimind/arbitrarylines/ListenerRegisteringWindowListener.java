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

public class ListenerRegisteringWindowListener implements IWindowListener {
	public static final ListenerRegisteringWindowListener INSTANCE = new ListenerRegisteringWindowListener();
	
	public ListenerRegisteringWindowListener() {
		super();
	}
	
	public static final void registerListener(IWorkbenchWindow workbenchWindow) {
		if (workbenchWindow != null) {
			for (IWorkbenchPage workbenchPage : workbenchWindow.getPages()) {
				ListenerRegisteringPageListener.registerListener(workbenchPage);
			}
			
			workbenchWindow.addPageListener(ListenerRegisteringPageListener.INSTANCE);
		}
	}
	
	@Override
	public void windowActivated(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	@Override
	public void windowClosed(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		// Nothing to do.
	}
	
	@Override
	public void windowOpened(IWorkbenchWindow window) {
		registerListener(window);
	}
}

/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ListenerRegisteringRunnable implements Runnable {
	
	public ListenerRegisteringRunnable() {
		super();
	}
	
	@Override
	public void run() {
		for (IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			ListenerRegisteringWindowListener.registerListener(workbenchWindow);
		}
		
		PlatformUI.getWorkbench().addWindowListener(ListenerRegisteringWindowListener.INSTANCE);
	}
}

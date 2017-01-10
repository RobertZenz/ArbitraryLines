/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;

public class ListenerRegisteringPageListener implements IPageListener {
	public static final ListenerRegisteringPageListener INSTANCE = new ListenerRegisteringPageListener();
	
	public ListenerRegisteringPageListener() {
		super();
	}
	
	public static void registerListener(IWorkbenchPage workbenchPage) {
		if (workbenchPage != null) {
			for (IEditorReference editorReference : workbenchPage.getEditorReferences()) {
				ListenerRegisteringPartListener.registerListener(editorReference);
			}
			
			workbenchPage.addPartListener(ListenerRegisteringPartListener.INSTANCE);
		}
	}
	
	@Override
	public void pageActivated(IWorkbenchPage page) {
		// Nothing to do.
	}
	
	@Override
	public void pageClosed(IWorkbenchPage page) {
		// Nothing to do.
	}
	
	@Override
	public void pageOpened(IWorkbenchPage page) {
		registerListener(page);
	}
	
}

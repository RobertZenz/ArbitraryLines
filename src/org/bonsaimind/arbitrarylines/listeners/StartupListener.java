/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.listeners;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

/**
 * The {@link StartupListener} is called when Eclipse starts and does kickoff
 * the registering process if all the needed listeners.
 */
public class StartupListener implements IStartup {
	/**
	 * Creates a new instance of {@link StartupListener}.
	 */
	public StartupListener() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new ListenerRegisteringRunnable());
	}
}

/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.bonsaimind.arbitrarylines.preferences.Preferences;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator.
 */
public class Activator extends AbstractUIPlugin {
	/** The Plugin ID. */
	public static final String PLUGIN_ID = "ArbitraryLines";
	
	/** The instance of the Plugin. */
	private static Activator plugin;
	
	private Preferences preferences = null;
	
	/**
	 * Creates a new instance of {@link Activator}.
	 */
	public Activator() {
		super();
	}
	
	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance.
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public Preferences getPreferences() {
		if (preferences == null) {
			preferences = new Preferences(getPreferenceStore());
		}
		
		return preferences;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}

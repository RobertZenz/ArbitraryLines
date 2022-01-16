/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.tests;

import org.bonsaimind.arbitrarylines.lines.Direction;
import org.bonsaimind.arbitrarylines.lines.Line;
import org.bonsaimind.arbitrarylines.lines.LineStyle;
import org.bonsaimind.arbitrarylines.lines.LocationType;
import org.bonsaimind.arbitrarylines.preferences.Preferences;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link Preferences} class.
 * <p>
 * We must extend {@link Preferences} to receive access to the conversion
 * methods.
 */
public class TestPreferences extends Preferences {
	public TestPreferences() {
		super();
	}
	
	/**
	 * Performs a test if the {@link Line} could be read back from a
	 * {@link String} as it was saved in version 1.0.
	 * <p>
	 * This is a test to make sure that old preferences can always be read.
	 */
	@Test
	public void testLineFromString_v_1_0() {
		String oldLine = "VERTICAL,PIXEL,80,10,10,ffeeddcc,DASH,";
		Line line = new Line(
				Direction.VERTICAL,
				LocationType.PIXEL,
				80,
				10,
				10,
				0xffeeddcc,
				LineStyle.DASH,
				true);
		
		Assert.assertEquals(line, lineFromString(oldLine));
	}
	
	/**
	 * Performs a test if the {@link Line} could be read back from a
	 * {@link String} as it was saved in version 1.1.
	 * <p>
	 * This is a test to make sure that old preferences can always be read.
	 */
	@Test
	public void testLineFromString_v_1_1() {
		String oldLine = "VERTICAL,PIXEL,80,10,10,ffeeddcc,DASH,false";
		Line line = new Line(
				Direction.VERTICAL,
				LocationType.PIXEL,
				80,
				10,
				10,
				0xffeeddcc,
				LineStyle.DASH,
				false);
		
		Assert.assertEquals(line, lineFromString(oldLine));
	}
	
	/**
	 * Performs a sanity test on {@link Preferences#lineFromString(String)} and
	 * {@link Preferences#lineToString(Line)}.
	 */
	@Test
	public void testLineFromToString() {
		String line = "VERTICAL,PIXEL,80,10,10,ffeeddcc,DASH,true,";
		
		Assert.assertEquals(line, lineToString(lineFromString(line)));
	}
	
	/**
	 * Performs a sanity test on {@link Preferences#lineFromString(String)} and
	 * {@link Preferences#lineToString(Line)}.
	 */
	@Test
	public void testLineToFromString() {
		Line line = new Line(
				Direction.VERTICAL,
				LocationType.PIXEL,
				80,
				10,
				10,
				0xffeeddcc,
				LineStyle.DASH,
				true);
		
		Assert.assertEquals(line, lineFromString(lineToString(line)));
	}
}

/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines;

import org.bonsaimind.arbitrarylines.Util;
import org.eclipse.swt.graphics.RGB;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link Util} class.
 */
public class UtilTests {
	/**
	 * Tests the {@link Util#colorFromString(String)} method.
	 */
	@Test
	public void testColorFromString() {
		int defaultReturnValue = 0x000000ff;
		
		// Basic input tests.
		Assert.assertEquals(defaultReturnValue, Util.colorFromString(null));
		Assert.assertEquals(defaultReturnValue, Util.colorFromString(""));
		
		// Valid input.
		Assert.assertEquals(0xffccaaff, Util.colorFromString("ffccaa"));
		Assert.assertEquals(0xffccaa99, Util.colorFromString("ffccaa99"));
		Assert.assertEquals(0xffccaaff, Util.colorFromString("FFCCAA"));
		Assert.assertEquals(0xffccaa99, Util.colorFromString("FFCCAA99"));
		Assert.assertEquals(0x0f0c0a09, Util.colorFromString("0f0c0a09"));
		
		// Too long, but valid input.
		Assert.assertEquals(0x12345678, Util.colorFromString("12345678901234556"));
		
		// Invalid input.
		Assert.assertEquals(defaultReturnValue, Util.colorFromString("0xfe34"));
		Assert.assertEquals(defaultReturnValue, Util.colorFromString("Not a number"));
		Assert.assertEquals(defaultReturnValue, Util.colorFromString("3r5y8s9x"));
	}
	
	/**
	 * Tests the {@link Util#colorToString(RGB, int)} method.
	 */
	@Test
	public void testColorToStringWithRGB() {
		Assert.assertEquals("00000000", Util.colorToString(null, 0x00));
		Assert.assertEquals("000000cc", Util.colorToString(null, 0xcc));
		Assert.assertEquals("00000000", Util.colorToString(new RGB(0, 0, 0), 0x00));
		Assert.assertEquals("ccddeeaa", Util.colorToString(new RGB(0xcc, 0xdd, 0xee), 0xaa));
		Assert.assertEquals("0c0d0e0a", Util.colorToString(new RGB(0x0c, 0x0d, 0x0e), 0x0a));
		Assert.assertEquals("12345678", Util.colorToString(0x12345678));
	}
	
	/**
	 * Tests the {@link Util#colorToString(int)} method.
	 */
	@Test
	public void testColorToStringWithString() {
		Assert.assertEquals("00000000", Util.colorToString(0x00000000));
		Assert.assertEquals("000000ff", Util.colorToString(0x000000ff));
		Assert.assertEquals("12345678", Util.colorToString(0x12345678));
		Assert.assertEquals("0a0b0c0d", Util.colorToString(0x0a0b0c0d));
	}
}

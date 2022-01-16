/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.lines;

import org.bonsaimind.arbitrarylines.lines.LineStyle;
import org.eclipse.swt.SWT;
import org.junit.Assert;
import org.junit.Test;

/**
 * NOTE: The point of these tests is to make sure that the order of style
 * names don't get messed up. They're in the order checked here for a reason.
 */

/**
 * Tests the {@link LineStyle} enum.
 */
public class LineStyleTests {
	
	/**
	 * Tests the {@link LineStyle#getStyleStrings()} method.
	 */
	@Test
	public void testGetStyleStrings() {
		String[] expected = new String[] { LineStyle.SOLID.toString(),
				LineStyle.DASH.toString(),
				LineStyle.DOT.toString(),
				LineStyle.DASHDOT.toString(),
				LineStyle.DASHDOTDOT.toString() };
		
		// Basic input tests.
		Assert.assertArrayEquals(expected, LineStyle.getStyleStrings());
	}
	
	/**
	 * Tests the {@link LineStyle#getSwtStyle()} method.
	 */
	@Test
	public void testGetSwtStyle() {
		LineStyle[] inputs = new LineStyle[] { LineStyle.SOLID,
				LineStyle.DASH,
				LineStyle.DOT,
				LineStyle.DASHDOT,
				LineStyle.DASHDOTDOT };
		int[] expected = new int[] { SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DOT, SWT.LINE_DASHDOT, SWT.LINE_DASHDOTDOT };
		
		for (int index = 0; index < inputs.length; index++) {
			Assert.assertEquals(expected[index], inputs[index].getSwtStyle());
		}
	}
}

/*
 * Copyright (c) 2017 Robert 'Bobby' Zenz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.bonsaimind.arbitrarylines.lines;

import org.eclipse.swt.graphics.Color;

/**
 * Represents one line.
 */
public class Line {
	private Color color = null;
	private int colorAsInt = -1;
	private Direction direction = null;
	private int location = -1;
	private LocationType locationType = null;
	private int offset = 0;
	private LineStyle style = null;
	private int thickness = 1;
	
	/**
	 * Creates a new instance of {@link Line}.
	 *
	 * @param direction The {@link Direction}.
	 * @param locationType The {@link LocationType}.
	 * @param location The location.
	 * @param thickness The thickness.
	 * @param offset The additional offset, always in pixel.
	 * @param color The color, in {@code RGBA} format.
	 * @param style The line style, from {@code LineStyle}
	 */
	public Line(Direction direction, LocationType locationType, int location, int thickness, int offset, int color, LineStyle style) {
		super();
		
		this.direction = direction;
		this.locationType = locationType;
		this.location = location;
		this.thickness = thickness;
		this.offset = offset;
		this.colorAsInt = color;
		this.color = new Color(
				null,
				(color >> 24) & 0xff,
				(color >> 16) & 0xff,
				(color >> 8) & 0xff,
				color & 0xff);
		this.style = style;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Line other = (Line)obj;
		if (colorAsInt != other.colorAsInt) {
			return false;
		}
		if (direction != other.direction) {
			return false;
		}
		if (location != other.location) {
			return false;
		}
		if (locationType != other.locationType) {
			return false;
		}
		if (offset != other.offset) {
			return false;
		}
		if (style != other.style) {
			return false;
		}
		if (thickness != other.thickness) {
			return false;
		}
		return true;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getColorAsInt() {
		return colorAsInt;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getLocation() {
		return location;
	}
	
	public LocationType getLocationType() {
		return locationType;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public LineStyle getStyle() {
		return style;
	}
	
	public int getThickness() {
		return thickness;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colorAsInt;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + location;
		result = prime * result + ((locationType == null) ? 0 : locationType.hashCode());
		result = prime * result + offset;
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + thickness;
		return result;
	}
}

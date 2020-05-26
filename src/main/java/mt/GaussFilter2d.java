/*
 * GaussFilter2d.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

public class GaussFilter2d extends LinearImageFilter {
	public GaussFilter2d(int filterSize, float sigma) {
		super(filterSize, filterSize, "Gauss2d (" + filterSize + ", " + sigma + ")");
		fill((x, y) -> (float) (1.f / (2 * Math.PI * sigma * sigma) * Math.exp(-(x * x + y * y) / (2.f * sigma * sigma))));

		normalize();
	}
}

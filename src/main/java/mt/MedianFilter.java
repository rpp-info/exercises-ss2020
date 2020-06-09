/*
 * MedianFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

import java.util.Arrays;

public class MedianFilter extends NonLinearFilter {
	public MedianFilter(int filterSize) {
		super( "MedianFilter " + filterSize, filterSize);
		reductionFunction = (values, weights) -> {
			Arrays.sort(values);
			return values[values.length / 2];
		};
	}
}

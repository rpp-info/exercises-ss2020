/*
 * FancyFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

import lme.WeightingFunction2d;

public class NonLinearFilter implements ImageFilter {
	protected WeightingFunction2d weightingFunction = (centerValue,neighborValue,x,y) -> 1.f;
	protected lme.NeighborhoodReductionFunction reductionFunction;
	protected int filterSize;
	protected String name;

	public NonLinearFilter(String name, int filterSize) {
		this.filterSize = filterSize;
		this.name = name;
	}

	// [NonLinearFilter: 3/3]
	@Override
	public void apply(Image image, Image result) {
		/// 0.5 P
		float[] values = new float[filterSize * filterSize];
		float[] weights = new float[filterSize * filterSize];
		int halfSize = filterSize/2;

		/// 0.5 P  Loops
		for (int y = 0; y < result.height(); ++y) {
			for (int x = 0; x < result.width(); ++x) {

				for (int j = -halfSize; j <= halfSize; ++j) {
					for (int i = -halfSize; i <= halfSize; ++i) {

						/// 1 P  Filling the arrays
					    int arrayIdx = (j+halfSize) * filterSize + (i+halfSize);
						weights[arrayIdx] = weightingFunction.getWeight(
								image.atIndex(x, y), image.atIndex(x - i, y - j), i, j);
						values[arrayIdx] = image.atIndex(x - i, y - j);
					}
				}
				/// 1 P  Saving/calculating the result
				result.buffer()[y * image.width() + x] = reductionFunction.reduce(values, weights);

			}
		}
	}

	@Override
	public String name() {
		return name;
	}
}

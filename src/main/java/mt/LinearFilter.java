/*
 * filter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

public class LinearFilter extends Signal{

	public LinearFilter(float[] coefficients, String name) {
		super(coefficients, name);
		if (1 != (coefficients.length % 2)) {
			throw new RuntimeException("Filter not even");
		}
		minIndex = -buffer.length / 2;
	}

	public Signal apply(Signal input) {
		Signal result = new Signal(input.size(), input.name() + " (filtered with " + name() + ")");

		for (int k = 0; k < result.size(); ++k) {
			float sum = 0.f;
			for (int kappa = this.minIndex(); kappa <= this.maxIndex(); kappa++) {
				sum += input.atIndex(k - kappa) * this.atIndex(kappa);
			}
			result.buffer()[k] = sum;
		}

		return result;
	}
}

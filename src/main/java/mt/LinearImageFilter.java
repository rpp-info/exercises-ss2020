/*
 * LinearImageFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

public class LinearImageFilter extends Image implements ImageFilter {

	public LinearImageFilter(int width, int height, String name) {
		super(width, height, name);
		minIndexX -= width / 2;
		minIndexY -= height / 2;
	}

	public void apply(Image image, Image result) {
		for (int y = 0; y < result.height(); ++y) {
			for (int x = 0; x < result.width(); ++x) {

				float sum = 0.f;
				for (int yPrime = minIndexY(); yPrime <= maxIndexY(); ++yPrime) {
					for (int xPrime = minIndexX(); xPrime <= maxIndexX(); ++xPrime) {
						sum += image.atIndex(x - xPrime, y - yPrime) * this.atIndex(xPrime, yPrime);
					}
				}
	    		result.setAtIndex(x, y, sum);
			}
		}
	}
	public void normalize() {
		double sum = sum();
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] /= sum;
		}
	}

}

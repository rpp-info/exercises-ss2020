/*
 * Exercise05.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */

package exercises;

import mt.BilateralFilter;
import mt.GaussFilter2d;
import mt.Image;

// [Measuring the error somewhere: 0/1]
public class Exercise05 {
	public static void main(String[] args) {
		(new ij.ImageJ()).exitWhenQuitting(true);
        // ALterantive image
		//https://upload.wikimedia.org/wikipedia/en/7/7d/Lenna_%28test_image%29.png
		Image image = lme.DisplayUtils.openImageFromInternet("https://mt2-erlangen.github.io/shepp_logan.png", ".png");
		image.show();

		GaussFilter2d filter = new mt.GaussFilter2d(7, 1.0f);

		// with noise
		Image noise = new Image(image.width(), image.height(), "Noise");
		noise.addNoise(0.f, 10.f);

		Image noisyImage = image.plus(noise);
		noisyImage.setName("Noisy Image");
		noisyImage.show();

		Image restored = filter.apply(noisyImage);
		restored.show();

		Image restoredError = image.minus(restored);
		restoredError.setName("Error Restored");
		restoredError.show();
		System.out.println(restoredError.variance());

		Image error = image.minus(noisyImage);
		error.setName("Error Before");
		error.show();
		System.out.println("noisy");
		System.out.println(error.variance());

		// with bilinear filter
		//
		BilateralFilter bilinearFilter = new BilateralFilter(7, 1f, 10f);
		Image fancyFiltered = bilinearFilter.apply(noisyImage);
		fancyFiltered.show();

		Image restoredFancyError = image.minus(fancyFiltered);
		restoredFancyError.setName("Error Restored Fancy");
		restoredFancyError.show();
		System.out.println("Fancy!");
		System.out.println(restoredFancyError.variance());
	}
}

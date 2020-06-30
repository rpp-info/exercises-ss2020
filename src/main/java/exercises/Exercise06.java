/*
 * Exercise06.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package exercises;

import ij.IJ;
import lme.DisplayUtils;
import mt.Image;
import mt.LinearImageFilter;

public class Exercise06 {
	public static void main(String[] args) {
		(new ij.ImageJ()).exitWhenQuitting(true);

		mt.Image cells = lme.DisplayUtils.openImageFromInternet("https://upload.wikimedia.org/wikipedia/commons/8/86/Emphysema_H_and_E.jpg", ".jpg");
		cells.show();

		LinearImageFilter sobelX = new LinearImageFilter(3, 3, "Sobel X");
		sobelX.setBuffer(new float[] { -1, 0, 1,
				                       -2, 0, 2,
				                       -1, 0, 1 });

		LinearImageFilter sobelY = new LinearImageFilter(3, 3, "Sobel Y");
		sobelY.setBuffer(new float[] { -1, -2, -1,
										0, 0, 0,
										1, 2, 1 });

		var edgesX = sobelX.apply(cells);
		edgesX.setName("Edges X");
		var edgesY = sobelY.apply(cells);
		edgesY.setName("Edges Y");

		edgesX.show();
		edgesY.show();

		var gradientMag = edgesX.binaryOperation(edgesY, (x, y) -> (float) Math.sqrt(x * x + y * y), "Gradient Magnitude");
		gradientMag.show();

		var threshold = 0.15 * gradientMag.max();
		System.out.println(threshold);
		var segmentedBorders = gradientMag.unaryOperation(x -> x > threshold ? 1.f : 0.f, "Gradient Segmented");
		segmentedBorders.show();

		// Segmentation
		double thresholdCells = 0.5 * cells.max();
		System.out.println(thresholdCells);
		Image segmentedCells = cells.unaryOperation(x -> x > thresholdCells ? 0.f : 1.f, "Cells Segmented");
		segmentedCells.show();


		// This is not necessary and requested in the Excersie, but it will serve to separate the cells if you use
		// This image https://www.drugtargetreview.com/wp-content/uploads/breast-cancer-tissue-750x500.jpg", ".jpg"
		IJ.run("Make Binary");
		IJ.run("8-bit");
		IJ.run("Watershed");
		Image watershed = DisplayUtils.plusToImage(ij.WindowManager.getImage("Cells Segmented"), "Watershed");
		DisplayUtils.showSegmentedCells(cells, watershed, true);

		DisplayUtils.showSegmentedCells(cells, segmentedCells, true);

	}
}


/*
 * MainClass.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package exercises;

import ij.gui.Plot;
import lme.HeartSignalPeaks;
import mt.Signal;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.types.Matrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Exercise02 {

	public static lme.HeartSignalPeaks getPeakPositions(mt.Signal signal, float threshold) {
		lme.HeartSignalPeaks peaks = new lme.HeartSignalPeaks();
		float argMax = -1.0f;
		float currentMax = Float.NEGATIVE_INFINITY;
		boolean inPeakRegion = false;

		for (int i = 0; i < signal.buffer().length; ++i) {
			float currentValue = signal.buffer()[i];
			if (currentValue > threshold) {
				inPeakRegion = true;
				if (currentValue > currentMax) {
					currentMax = currentValue;
					argMax = i * signal.spacing();
				}
			} else {
				if (inPeakRegion) {
					peaks.xValues.add((double) argMax);
					peaks.yValues.add((double) currentMax);
					inPeakRegion = false;
					currentMax = Float.NEGATIVE_INFINITY;
					argMax = -1;
				}
			}
		}

		return peaks;
	}

	public static mt.Signal calcPeakIntervals(lme.HeartSignalPeaks peaks) {
		ArrayList<Double> peakPositions = peaks.xValues;
		if  (peakPositions.size() > 1) {
			Signal intervals = new mt.Signal(peaks.xValues.size() - 1, "Peak Intervals");

			for (int i = 0; i < peakPositions.size() - 1; ++i) {
				intervals.buffer()[i] = (float) (peakPositions.get(i + 1) - peakPositions.get(i));
			}
			return intervals;
		} else {
			return new mt.Signal(1, "No Intervals found");
		}

	}

	public static void main(String[] args) throws IOException {
		(new ij.ImageJ()).exitWhenQuitting(true);

		System.out.println("Started with the following arguments");
		for (String arg : args) {
			System.out.println(arg);
		}

		if (args.length == 1) {
			File file = new File(args[0]);
			if (file.isFile()) {
				Matrix mat = Mat5.readFromFile(file).getMatrix(0);
				Signal heartSignal = new mt.Signal(mat.getNumElements(), "Heart Signal");
				for (int i = 0; i < heartSignal.size(); ++i) {
					heartSignal.buffer()[i] = mat.getFloat(i);
				}

				heartSignal.setSpacing(1 / 360.f);

				heartSignal.show();

				System.out.println("Max value: " + heartSignal.max());
				System.out.println("Min value: " + heartSignal.min());
				System.out.println("Variance: " + heartSignal.variance());
				System.out.println("Mean: " + heartSignal.mean());

				float THRESHOLD = 0.50f;
				float max = heartSignal.max();
				float min = heartSignal.min();
				float mean = heartSignal.mean();
				float threshold = THRESHOLD * (max - min) + mean;

				HeartSignalPeaks peaks = getPeakPositions(heartSignal, threshold);

				Plot plot = new Plot("Heart Signal", "time (in sec)", "mV");
				heartSignal.show(plot);
				plot.setColor("red");
				plot.addPoints(peaks.xValues, peaks.yValues, 0);
				plot.setColor("blue");
				plot.add("lines", new double[] { 0, /*a large value*/ 10000 }, new double[] { threshold, threshold });
				plot.show();

				Signal intervals = calcPeakIntervals(peaks);
				intervals.show();
				System.out.println("Max value: " + intervals.max());
				System.out.println("Min value: " + intervals.min());
				System.out.println("Variance: " + intervals.variance());
				System.out.println("StdDev: " + intervals.stdDev());
				System.out.println("Mean Cycle duration: " + intervals.mean() + " s");
				System.out.println("Mean frequency: " + (1. / intervals.mean()) + " Hz");
				System.out.println("Mean frequency: " + 60 * (1. / intervals.mean()) + " bpm");
			} else {
				System.err.println("Could not find " + file);
			}

		} else {
			System.out.println("Wrong argcount: " + args.length);
			System.exit(-1);
		}
	}
}

/*
 * DisplayUtils.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */

package lme;

import ij.gui.Plot;

public class DisplayUtils {

    public static void showArray(float[] yValues, String title, double origin, double spacing) {
        showArray(yValues, new Plot(title, "X", "Y"), origin, spacing);
    }

    public static void showArray(float[] yValues, Plot plot, double origin, double spacing) {
        var yValuesDouble = new double[yValues.length];
        var xValues = new double[yValues.length];

        for (int i = 0; i < xValues.length; i++) {
            xValues[i] = origin + i * spacing;
            yValuesDouble[i] = (double) yValues[i];
        }

        plot.add("lines", xValues, yValuesDouble);
        plot.show();
    }

    public static void showArraysBars(int[] yValues, Plot plot, double minValue, double binSize) {
        var yValuesDouble = new double[yValues.length];
        var xValues = new double[yValues.length];

        for (int i = 0; i < xValues.length; i++) {
            xValues[i] = minValue + i * binSize + 0.5 * binSize;
            yValuesDouble[i] = (double) yValues[i];
        }

        plot.add("bars", xValues, yValuesDouble);
        plot.show();
    }

    public static void showArrayBars(float[] yValues, Plot plot, double minValue, double binSize) {
        var yValuesDouble = new double[yValues.length];
        var xValues = new double[yValues.length];

        for (int i = 0; i < xValues.length; i++) {
            xValues[i] = minValue + i * binSize + 0.5 * binSize;
            yValuesDouble[i] = yValues[i];
        }

        plot.add("bars", xValues, yValuesDouble);
        plot.show();
    }
}

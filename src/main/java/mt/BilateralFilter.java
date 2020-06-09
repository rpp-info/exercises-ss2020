/*
 * BilinearFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

// [BilateralFilter: 4/4]
public class BilateralFilter extends NonLinearFilter {
    GaussFilter2d gaussFilter;

    /// 1 P value weight
    private static float gauss(float x, float var) {
        // Pre-factor not necessary: Normalization!
//		return 1.f / (float) Math.sqrt(2 * (float) Math.PI * sigma) * (float) Math.exp(-(x * x) / (sigma * sigma));
        return (float) Math.exp(-x * x / (2*var));
    }

    public BilateralFilter(int filterSize, float spatialSigma, float valueSigma) {
        super("BilinearFilter spatialSigma " + spatialSigma + " valueSigma " + valueSigma, filterSize);
        gaussFilter = new GaussFilter2d(filterSize, spatialSigma);
        /// 1P weighting Function
        weightingFunction = (centerValue, value, shiftX, shiftY) -> gaussFilter.atIndex(shiftX, shiftY) * gauss(centerValue - value, valueSigma * valueSigma);

        /// 1 P reductionFunction
        reductionFunction = (values, weights) -> {
                    float sum = 0.f;
                    float weightSum = 0.f;
                    for (int i = 0; i < values.length; i++) {
                        sum += values[i] * weights[i];
                        /// 1 P weightSum
                        weightSum += weights[i];
                    }
                    return sum / weightSum;

                };
    }
}

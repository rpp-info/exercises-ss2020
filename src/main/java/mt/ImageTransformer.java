/*
 * ImageTransformer.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

// [ImageTransformer: 5/5]
public class ImageTransformer implements ImageFilter {
    public float shiftX;
    public float shiftY;
    public float rotation;
    public float scale;

    public void apply(Image input, Image output) {
        // This method should do an image transformation in the following order
        // - translation
        // - rotation
        // - scaling

        // For each index of the output image
        for (int yPrime = 0; yPrime < output.height(); ++yPrime) {
            for (int xPrime = 0; xPrime < output.width(); ++xPrime) {
                /// 1 P
                // Transform output index to physical coordinates
                float inputX = (float) (xPrime * output.spacing() + output.origin()[0]);
                float inputY = (float) (yPrime * output.spacing() + output.origin()[1]);

                // Remember that for the inverse translation we have to perform the individual transform in inverse order

                /// 1 P
                // Apply inverse scaling to physical output coordinates
                inputX *= 1.f / scale;
                inputY *= 1.f / scale;

                /// 1 P
                // Apply inverse rotation to your intermediate result
                float tmpX = (float) (Math.cos(rotation) * inputX + Math.sin(rotation) * inputY);
                float tmpY = (float) (-Math.sin(rotation) * inputX + Math.cos(rotation) * inputY);
                inputX = tmpX;
                inputY = tmpY;

                /// 1 P
                // Apply inverse the inverse shift to your intermediate result
                inputX -= shiftX;
                inputY -= shiftY;

                /// 1 P
                // Use input.interpolatedAt to get the pixel value at the calculated physical position
                float value = input.interpolatedAt(inputX, inputY);
                // Set your result at the current output pixel
                output.setAtIndex(xPrime, yPrime, value);
            }
        }
    }

    @Override
    public String name() {
        return "Image Transformer";
    }
}

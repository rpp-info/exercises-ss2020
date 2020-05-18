// Your name here <your idm here>
// Partner'sj name here <partner idm here>

package mt;

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
                // Transform output index (xPrime, yPrime) to physical coordinates


                // Remember that for the inverse translation we have to perform the individual transform in inverse order
                // Apply inverse scaling to physical output coordinates




                // Apply inverse rotation to your intermediate result





                // Apply inverse the inverse shift to your intermediate result




                // Use input.interpolatedAt to get the pixel value at the calculated physical position

                // Set your result at the current output pixel (xPrime, yPrime)

            }
        }
    }

    @Override
    public String name() {
        return "Image Transformer";
    }
}

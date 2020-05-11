package exercises;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import mt.GaussFilter2d;
import mt.Image;
import mt.LinearImageFilter;
import net.imagej.ImageJ;
import org.scijava.ItemVisibility;
import org.scijava.command.Command;
import org.scijava.command.Previewable;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.widget.NumberWidget;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * An ImageJ2 command with preview capabilities.
 */
@Plugin(type = Command.class, menuPath = "Tutorials>Command with Preview")
public class Exercise03Demo implements Command, Previewable {
    // -- Parameters --

    @Parameter(initializer = "initImagePlus")
    public ImagePlus imp;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0.01", max = "10", stepSize = "0.01")
    private float sigma;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0", max = "50", stepSize = "0.01")
    private float noiseSigma;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "1", max = "15", stepSize = "2")
    private int filterSize;

    @Parameter()
    private boolean doGaussFiltering;

    @Parameter(label = "High pass")
    private boolean highPass;

    @Parameter(visibility = ItemVisibility.MESSAGE, label = "Mean Squared Error")
    private float meanSquareError;

    // -- Other fields --

    /**
     * The original title of the image.
     */
    private ij.process.FloatProcessor originalProcessor;

    // -- Command methods --

    private static float calcError(Image image) {
        float rtn = 0.f;
        for (float f : image.buffer()) {
            rtn += f * f;
        }
        rtn /= image.size();
        return rtn;
    }

    @Override
    public void run() {

        Image input = lme.DisplayUtils.floatProcessorToImage(originalProcessor, "?");
        Image filterInput = new Image(input.width(), input.height(), "Noisy", Arrays.copyOf(input.buffer(), input.size()));
        filterInput.addNoise(0, noiseSigma);

        Image output = lme.DisplayUtils.plusToImage(imp, "?");

        if (doGaussFiltering) {
            LinearImageFilter filter = new GaussFilter2d(filterSize, sigma);
            filter.apply(filterInput, output);
            if (highPass) {
                IntStream.range(0, output.height()).forEach(y ->
                        IntStream.range(0, output.width()).forEach(x ->
                                output.setAtIndex(x, y, filterInput.atIndex(x, y) - output.atIndex(x, y))
                        ));
            }
        } else {
            LinearImageFilter filter = new LinearImageFilter(1, 1, "nothing");
            filter.setBuffer(new float[]{1});
            filter.apply(filterInput, output);
        }

        Image error = input.minus(output);
        meanSquareError = calcError(error);
        error.setName("Error");
        error.show();
//        meanSquareError = error.variance();

        imp.updateAndDraw();
    }

    // -- Previewable methods --

    @Override
    public void preview() {
        run();
    }

    @Override
    public void cancel() {
        // Set the image's title back to the original value.
        imp.setProcessor(originalProcessor);
    }

    protected void initImagePlus() {
        ImageConverter converter = new ij.process.ImageConverter(imp);
        converter.convertToGray32();
        originalProcessor = (FloatProcessor) imp.getProcessor();
        imp.setProcessor(new FloatProcessor(originalProcessor.getWidth(), originalProcessor.getHeight()));
        imp.setDisplayRange(0, 255);
    }

    // -- Main method --

    /**
     * Tests our command.
     */
    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        PluginInfo<Command> info = new PluginInfo<Command>(Exercise03Demo.class, Command.class);
        ij.plugin().addPlugin(info);
        ij.launch(args);

        Image image = lme.DisplayUtils.openImageFromInternet("https://mt2-erlangen.github.io/pacemaker.png", ".png");
        image.show();

        // Launch the "CommandWithPreview" command.
        ij.command().run(Exercise03Demo.class, true);
    }

}


package exercises;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import mt.*;
import net.imagej.ImageJ;
import org.scijava.ItemVisibility;
import org.scijava.command.Command;
import org.scijava.command.Previewable;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.widget.NumberWidget;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * An ImageJ2 command with preview capabilities.
 */
@Plugin(type = Command.class, menuPath = "Tutorials>Command with Preview")
public class Exercise05Demo implements Command, Previewable {
    // -- Parameters --

    @Parameter(initializer = "initImagePlus")
    public ImagePlus imp;

    @Parameter(label = "Filter Type", choices = { "Gauss", "Bilinear", "Median", "No Filter" })
    private String filterType;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0.01", max = "1000", stepSize = "0.01")
    private float gaussSigma;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0.01", max = "1000", stepSize = "0.01")
    private float valueSigma;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0", max = "1", stepSize = "0.01")
    private float noiseSigma;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "1", max = "15", stepSize = "2")
    private int filterSize;

    @Parameter()
    private boolean salt;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0", max = "0.2", stepSize = "0.01")
    private float saltProbability;

    @Parameter()
    private boolean pepper;

    @Parameter(style = NumberWidget.SLIDER_STYLE,
            min = "0", max = "0.2", stepSize = "0.01")
    private float pepperProbability;

    @Parameter(visibility = ItemVisibility.MESSAGE, label = "Mean Squared Error")
    private float meanSquareError;

    @Parameter(visibility = ItemVisibility.MESSAGE, label = "Mean Squared Error without Filtering")
    private float meanSquareErrorWithoutFiltering;

    // -- Other fields --

    /**
     * The original title of the image.
     */
    private FloatProcessor originalProcessor;

    // -- Command methods --

    private static float calcError(Image image) {
        float rtn = 0.f;
        for (float f : image.buffer()) {
            rtn += f * f;
        }
        rtn /= image.size();
        return rtn;
    }

    private static void addSalt(Image image, float saltProbability) {
        Random rand = new Random(42);
        IntStream.range(0, image.size()).forEach(i -> {
            if (rand.nextFloat() < saltProbability) {
                image.buffer()[i] = 1.f;
            }
        });
    }
    
    private static void addPepper(Image image, float pepperProbability) {
        Random rand = new Random(42);
        IntStream.range(0, image.size()).forEach(i -> {
            if (rand.nextFloat() < pepperProbability) {
                image.buffer()[i] = 0.f;
            }
        });
    }

    @Override
    public void run() {

        Image input = lme.DisplayUtils.floatProcessorToImage(originalProcessor, "Original");
        input.show();
        Image filterInput = new Image(input.width(), input.height(), "Noisy", Arrays.copyOf(input.buffer(), input.size()));
        filterInput.addNoise(0, noiseSigma);

        Image output = lme.DisplayUtils.plusToImage(imp, "?");

        if(salt) {
            addSalt(filterInput, saltProbability);
        }

        if(pepper) {
            addPepper(filterInput, pepperProbability);
        }
        filterInput.show();

        ImageFilter filter;
        if (filterType.equals("Gauss")) {
            System.out.println(filterType);
            filter = new GaussFilter2d(filterSize, gaussSigma);
        } else if (filterType.equals("Bilinear")) {
            filter = new BilateralFilter(filterSize, gaussSigma, valueSigma);
        } else if (filterType.equals("Median")) {
            filter = new MedianFilter(filterSize);
        } else {
            filter = new LinearImageFilter(1,1, "Copy of original");
            ((LinearImageFilter)filter).setBuffer(new float[]{1});
        }
        filter.apply(filterInput, output);

        Image error = input.minus(output);
        meanSquareError = calcError(error);
        error.setName("Error");
        error.show();

        Image errorWithoutFiltering = input.minus(filterInput);
        meanSquareErrorWithoutFiltering = calcError(errorWithoutFiltering);

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
        ImageConverter converter = new ImageConverter(imp);
        converter.convertToGray32();
        originalProcessor = (FloatProcessor) imp.getProcessor();
        Image input = lme.DisplayUtils.floatProcessorToImage(originalProcessor, "Original");
        float max = input.max();
        IntStream.range(0, input.size()).forEach(i -> input.buffer()[i] /= max);
        imp.setProcessor(new FloatProcessor(originalProcessor.getWidth(), originalProcessor.getHeight()));
        imp.setDisplayRange(0, 1);
    }

    // -- Main method --

    /**
     * Tests our command.
     */
    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        PluginInfo<Command> info = new PluginInfo<Command>(Exercise05Demo.class, Command.class);
        ij.plugin().addPlugin(info);
        ij.launch(args);

        Image image = lme.DisplayUtils.openImageFromInternet("https://mt2-erlangen.github.io/pacemaker.png", ".png");
//        Image image = lme.DisplayUtils.openImageFromInternet("http://www.cs.tau.ac.il/~turkel/lenna.jpg", ".jpg");

        image.setName("Filtered");
        image.show();

        // Launch the "CommandWithPreview" command.
        ij.command().run(Exercise05Demo.class, true);
    }

}


package mt;/*
 * LinearFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */

import mt.LinearFilter;
import mt.Signal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class LinearFilterTests {

    @Test
    void testConvolution() {
        (new ij.ImageJ()).exitWhenQuitting(true);

        var kernelArray = new float[]{0, 1, 2, 0.5f, 0.2f};
//        var kernelArray = new float[]{0, 0, 2, 0, 1};


        var input = new float[10];

        var random = new Random();
        IntStream.range(0, input.length).forEach(i -> input[i] = random.nextFloat());


        var inputSignal = new Signal(input, "Input");
        var filter = new LinearFilter(kernelArray, "Filter");
        var outputSignal = filter.apply(inputSignal);

        var referenceSignal = lme.Algorithms.convolution1d(inputSignal, filter);

        inputSignal.show();
        referenceSignal.show();
        outputSignal.show();

//        for(;;);
        Assertions.assertArrayEquals(outputSignal.buffer(), referenceSignal.buffer(), 0.01f);
    }

    @Test
    void testExampleFromExercise() {
        (new ij.ImageJ()).exitWhenQuitting(true);

        var kernelArray = new float[]{1, 2, 3};

        var input = new float[]{1, 0, 0, 2, 3, 0, 0, 0, 4};

        var inputSignal = new Signal(input, "Input");
        var filter = new LinearFilter(kernelArray, "Filter");
        var outputSignal = filter.apply(inputSignal);

        var referenceSignal = lme.Algorithms.convolution1d(inputSignal, filter);

        for(int i = 0; i < input.length; i++) {
            System.out.print("Your result " + outputSignal.atIndex(i));
            System.out.println(", Reference result " + outputSignal.atIndex(i));
        }
        Assertions.assertArrayEquals(outputSignal.buffer(), referenceSignal.buffer(), 0.01f);
    }

    @Test
    void testAtIndexFilter() {
        for (int i = 1; i < 10; ++i) {
            var filterArray = new float[i * 2 + 1];
            IntStream.range(0, filterArray.length).forEach(j -> filterArray[j] = j);
            var testFilter = new LinearFilter(filterArray, "Input");
            Assertions.assertEquals(testFilter.minIndex(), -i);
            Assertions.assertEquals(testFilter.size(), i * 2 + 1);
            Assertions.assertEquals(testFilter.maxIndex(), i);
            Assertions.assertEquals(testFilter.size(), i * 2 + 1);

            IntStream.range(-i, i).forEach(j -> testFilter.setAtIndex(j, j + 1));

            IntStream.range(-i, i).forEach(j -> Assertions.assertEquals(testFilter.atIndex(j) , j + 1));
        }

    }
}


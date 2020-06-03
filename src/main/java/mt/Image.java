/*
 * Image.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

import lme.DisplayUtils;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Image extends Signal {

    protected int width;
    protected int height;

    protected int minIndexX;
    protected int minIndexY;

    protected float[] origin = new float[]{0, 0};

    public Image(int width, int height, String name) {
        super(height * width, name);
        this.width = width;
        this.height = height;
        this.minIndexX = 0;
        this.minIndexY = 0;
    }

    public Image(int width, int height, String name, float[] pixels) {
        super(pixels, name);
        this.width = width;
        this.height = height;
    }

    public void show() {
        DisplayUtils.showImage(buffer, name, width(), origin, spacing(), true);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public long[] shape() {
        return new long[]{ width, height};
    }

    public float atIndex(int x, int y) {
        int xIdx = x - minIndexX;
        int yIdx = y - minIndexY;

        if (xIdx < 0 || xIdx >= width() || yIdx < 0 || yIdx >= height()) {
            return 0.f;
        } else {
            return buffer[yIdx * width() + xIdx];
        }
    }

    public void setAtIndex(int x, int y, float value) {
        int xIdx = x - minIndexX;
        int yIdx = y - minIndexY;
        
        if (xIdx < 0 || xIdx >= width() || yIdx < 0 || yIdx >= height()) {
            throw new RuntimeException("Index out of bounds");
        } else {
            buffer[yIdx * width() + xIdx] = value;
        }
    }

    public void fill(InitFunction2d function) {
        for (int y = minIndexY; y < minIndexY + height(); ++y) {
            for (int x = minIndexY; x < minIndexX + width(); ++x) {
                setAtIndex(x, y, function.init(x, y));
            }
        }
    }

    public int minIndexX() {
        return minIndexX;
    }

    public int minIndexY() {
        return minIndexY;
    }

    public int maxIndexX() {
        return minIndexX + width() - 1;
    }

    public int maxIndexY() {
        return minIndexY + height() - 1;
    }

    public Image minus(Signal other) {
        Signal signal = super.minus(other);
        return new Image(width(), height(), signal.name(), signal.buffer());
    }

    public Image plus(Signal other) {
        Signal signal = super.plus(other);
        return new Image(width(), height(), signal.name(), signal.buffer());
    }

    public void setOrigin(float x, float y) {
        origin[0] = x;
        origin[1] = y;
    }

    public float[] origin() {
        return origin;
    }

    public void centerOrigin() {
        origin[0] = -width() * spacing * 0.5f;
        origin[1] = -height() * spacing * 0.5f;
    }

    public float physicalX(int x) {
        return x * spacing() + origin()[0];
    }

    public float physicalY(int y) {
        return y * spacing() + origin()[1];
    }

    // [interpolation: 4/4]
    private static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public float interpolatedAt(float x, float y) {
        /// 1 P
        x -= origin[0];
        y -= origin[1];
        x /= spacing;
        y /= spacing;

        /// 1 P
        int xFloor = (int) Math.floor(x);
        int yFloor = (int) Math.floor(y);
        int xCeil = (int) Math.ceil(x);
        int yCeil = (int) Math.ceil(y);

        /// 1 P
        float x1 = lerp(atIndex(xFloor, yFloor), atIndex(xCeil, yFloor), x - xFloor);
        float x2 = lerp(atIndex(xFloor, yCeil), atIndex(xCeil, yCeil), x - xFloor);

        /// 1 P
        return lerp(x1, x2, y - yFloor);
    }

    public Image binaryOperation(Image other, BinaryOperator<Float> operation, String resultName) {
        Image output = new Image(width(), height(), resultName);
        IntStream.range(0, size())
                .forEach(i -> output.buffer[i] = operation.apply(this.buffer()[i], other.buffer()[i]));
        return output;
    }

    public Image unaryOperation(Function<Float, Float> operation, String resultName) {
        Image output = new Image(width(), height(), resultName);
        IntStream.range(0, size()).forEach(i -> output.buffer[i] = operation.apply(this.buffer()[i]));
        return output;
    }

}

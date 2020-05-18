
/*
 * Signal.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */

package mt;

import ij.gui.Plot;
import lme.DisplayUtils;

import java.util.Random;

public class Signal {

	protected float[] buffer;
	protected String name;
	protected int minIndex = 0;

	public Signal(int length) {
		buffer = new float[length];
		this.name = "unnamed";
	}

	public Signal(int length, String name) {
		buffer = new float[length];
		this.name = name;
	}

	public Signal(float[] buffer, String name) {
		this.buffer = buffer;
		this.name = name;
	}

	public void addNoise(float mean, float standardDeviation) {
		Random rand = new Random();
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] += mean + rand.nextGaussian() * standardDeviation;
		}
	}

	public void show(Plot plot) {
		DisplayUtils.showArray(buffer, plot, minIndex, 1.0f);
	}

	public void show() {
		DisplayUtils.showArray(buffer, name, minIndex, 1.0f);
	}

	public int size() {
		return buffer.length;
	}

	public float[] buffer() {
		return buffer;
	}

	public void setBuffer(float[] buffer) {
		this.buffer = buffer;
	}

	public int minIndex() {
		return minIndex;
	}

	public int maxIndex() {
		return minIndex + size() - 1;
	}

	public float atIndex(int i) {
		int arrayIdx = i - minIndex;
		if (arrayIdx < 0 || arrayIdx >= buffer.length) {
			return 0.f;
		} else {
			return buffer[arrayIdx];
		}
	}

	public void setAtIndex(int i, float value) {
		int arrayIdx = i - minIndex;
		buffer[arrayIdx] = value;
	}

	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//public void fill(Function<Integer, Float> fillFunction) {
		//IntStream.range(minIndex(), maxIndex()).forEach(i -> setAtIndex(i, fillFunction.apply(i)));
	//}
}


/*
 * MainClass.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package exercises;


import mt.LinearFilter;
import mt.Signal;

public class Exercise01 {
	public static void main(String[] args) {
		(new ij.ImageJ()).exitWhenQuitting(true);

		Signal signal = new mt.Signal(new float[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, "f(x)");
		signal.show();

		LinearFilter filter1 = new mt.LinearFilter(new float[]{1.0f/3 ,1/3.f ,1/3.f}, "Filter 1");
		Signal result1 = filter1.apply(signal);
		result1.show();

		LinearFilter filter2 = new mt.LinearFilter(new float[]{1/5.f, 1/5.f , 1/5.f, 1/5.f, 1/5.f}, "Filter 2");
		Signal result2 = filter2.apply(signal);
		result2.show();

		LinearFilter filter3 = new mt.LinearFilter(new float[]{0.5f, 0.0f, -0.5f}, "Filter 3");
		Signal result3 = filter3.apply(signal);
		result3.show();


	}
}

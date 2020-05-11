/*
 * ImageFilter.java
 * Copyright (C) 2020 Stephan Seitz <stephan.seitz@fau.de>
 *
 * Distributed under terms of the GPLv3 license.
 */
package mt;

public interface ImageFilter {
	default mt.Image apply(mt.Image image) {
		Image output = new Image(image.width(), image.height(), image.name() + " processed with " + this.name());
		apply(image, output);
		return output;
	}

	default void apply(mt.Image input, mt.Image output) {
		throw new RuntimeException("Implement this method!");
	}

	String name();
}

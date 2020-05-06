// <Your name> <your idm>
// <your partners name> <partner idm>

package exercises;

//// import this once you need it
// import lme.HeartSignalPeaks;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.types.Matrix;

import java.io.IOException;
import ij.gui.Plot;
import java.io.File;

public class Exercise02 {


	public static void main(String[] args) throws IOException {
		(new ij.ImageJ()).exitWhenQuitting(true);

		System.out.println("Started with the following arguments");
		for (String arg : args) {
			System.out.println(arg);
		}

		if (args.length == 1) {
			File file = new File(args[0]);
			if (file.isFile()) {
				// Your code here


			} else {
				System.err.println("Could not find " + file);
			}

		} else {
			System.out.println("Wrong argcount: " + args.length);
			System.exit(-1);
		}
	}
}

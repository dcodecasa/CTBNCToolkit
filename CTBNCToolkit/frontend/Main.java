/**
 * Copyright (c) 2012-2013, Daniele Codecasa <codecasa.job@gmail.com>,
 * Models and Algorithms for Data & Text Mining (MAD) laboratory of
 * Milano-Bicocca University, and all the CTBNCToolkit contributors
 * that will follow.
 * All rights reserved.
 *
 * @author Daniele Codecasa and all the CTBNCToolkit contributors that will follow.
 * @copyright 2012-2013 Daniele Codecasa, MAD laboratory, and all the CTBNCToolkit contributors that will follow
 */
package CTBNCToolkit.frontend;

import java.util.Vector;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Main class that lunch the command line interface.
 */
public class Main {

	/**
	 * Main: starts the command line interface.
	 * 
	 * @param args arguments of the function
	 */
	public static void main(String[] args) {
		
		// Modifiers and filenames are divided and given
		// in input to the command line.
		Vector<String> filenames = new Vector<String>();
		Vector<String> modifiers = new Vector<String>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				modifiers.add(args[i]);
			} else {
				filenames.add(args[i]);
			}
		}
		
		try {
			new CommandLine( modifiers.toArray( new String[0]), filenames.toArray( new String[0]));
		} catch (Exception e) {
			System.err.println("Error: " + e);
			System.err.println("StackTrace:\n");
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

}

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
package CTBNCToolkit;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define a decider that take
 * in input the probability distribution and
 * decide which class chose.
 */
public interface IClassifyDecider {

	/**
	 * Choose one class give the probability
	 * distribution.
	 * 
	 * @param results results from which read the probability distribution for each class
	 * @return index of the chosen class
	 * @throws RuntimeException in case of errors
	 */
	public int decide(IClassificationResult<Double> results) throws RuntimeException;
}

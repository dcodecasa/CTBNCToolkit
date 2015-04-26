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
package CTBNCToolkit.performances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface for classification performances in
 * one single run.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public interface IClassificationSingleRunPerformances<TimeType extends Number & Comparable<TimeType>> extends ISingleRunPerformances<TimeType>,IClassificationPerformances<TimeType> {

	/**
	 * Return the contingency matrix in the form
	 * true class - classification.
	 * 
	 * @return contingency matrix.
	 */
	public double[][]  getContingencyMatrix();
}

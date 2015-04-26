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

import java.util.Map;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that defines a factory for single
 * run performances.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <SingPType> type of single run performances returned
 */
public interface ISingleRunPerformancesFactory<TimeType extends Number & Comparable<TimeType>, SingPType extends ISingleRunPerformances<TimeType>> {

	/**
	 * Generate a new instance of single run
	 * performances.
	 * 
	 * @param classIndexToValue map index to class value
	 * @return new single run performances instance.
	 */
	public SingPType newInstance(Map<Integer,String> classIndexToValue);
}

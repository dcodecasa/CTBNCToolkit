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
 * Factory for ClassificationStandardPerformances
 * single run performances.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public class ClassificationStandardPerformancesFactory<TimeType extends Number & Comparable<TimeType>> implements
		ISingleRunPerformancesFactory<TimeType, ClassificationStandardPerformances<TimeType>> {

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformancesFactory#newInstance(java.util.Map)
	 */
	@Override
	public ClassificationStandardPerformances<TimeType> newInstance(
			Map<Integer, String> classIndexToValue) {

		return new ClassificationStandardPerformances<TimeType>(classIndexToValue);
	}

}

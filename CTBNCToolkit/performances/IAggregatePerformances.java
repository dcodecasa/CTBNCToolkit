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

import java.util.Collection;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface for aggregate classification
 * performances (performances for multiple
 * runs).
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)s
 * @param <PType> performance type
 */
public interface IAggregatePerformances<TimeType extends Number,PType> extends IPerformances {
	
	
	/**
	 * Return performances used to
	 * calculate aggregate results.
	 * 
	 * @return the trajectories used to calculate performances
	 */
	public Collection<PType> getPerformances();
	
	/**
	 * Multiple inserting of runs
	 * performances.
	 * 
	 * @param runsPerformances collection of all the run performances
	 */
	public void addPerformances(Collection<PType> runsPerformances);
	
	/**
	 * Insert a single run performances.
	 * 
	 * @param performances a performances of a single run
	 */
	public void addPerformances(PType performance);
	
	/**
	 * Return the variance of the
	 * learning time.
	 * Return NaN if times are not
	 * set.
	 * 
	 * @return variance of the learning time
	 */
	public double getVarianceLearningTime(); 

}

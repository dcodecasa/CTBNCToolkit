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

import java.util.*;

/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define the transitions for
 * a classified trajectory.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public interface IClassificationTransition<TimeType extends Number> extends ITransition<TimeType> {

	/**
	 * Set the probability distribution for each
	 * state of the class node in the given transition
	 * time.
	 *
	 * @param p the probability distribution
	 * @param stateToIndex the mapping between the state name and the index for the probability
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setProbability(double[] p, Map<String,Integer> stateToIndex) throws IllegalArgumentException;
	
	/**
	 * Return the classes probability distribution.
	 * Null if the probability distribution is not
	 * set.
	 * 
	 * @return probability distribution of the classes
	 */
	public double[] getPDistribution();
	
	/**
	 * Return the probability value for the class
	 * state in input in the current time.
	 * Null if it is not set.
	 * 
	 * @param classStateName class state who get the probability
	 * @return probability of the state
	 * @throws RuntimeException in case of error
	 */
	public Double getProbability(String classStateName) throws RuntimeException;
	
}

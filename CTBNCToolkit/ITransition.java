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
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Interface that define trajectory transitions.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public interface ITransition<TimeType extends Number> {
	
	/**
	 * Get the time instant.
	 * 
	 * @return time instant
	 */
	TimeType getTime();

	/**
	 * Get the state of a node.
	 * 
	 * @param nodeIndex index of the node
	 * @return state of the node or null if the node is not present in the transition
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	String getNodeValue(int nodeIndex) throws IllegalArgumentException;
	
}

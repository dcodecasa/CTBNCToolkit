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

import java.util.Map;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * This interface define the results of a classification model.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public interface IClassificationResult<TimeType extends Number> extends ITrajectory<TimeType>{

	/**
	 * Set the class name of the trajectory.
	 * 
	 * @param className the name of the class (a state of the class variable)
	 */
	public void setClassification(String className);
	
	/**
	 * Return the class of the trajectory.
	 * 
	 * @return the classification of the trajectory
	 */
	public String getClassification();
	
	/**
	 * Set the probability distribution for each
	 * state of the class node in the given transition
	 * time.
	 * 
	 * @param iTransition index of the transition
	 * @param p the probability distribution
	 * @param stateToIndex the mapping between the state name and the index for the probability
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setProbability(int iTransition, double[] p, Map<String,Integer> stateToIndex) throws IllegalArgumentException;
	
	/**
	 * Return the probability distribution at
	 * a specific transition.
	 *  
	 * @param iTransition index of the transition
	 * @return probability distribution for each class
	 * @throws IllegalArgumentException in case of error
	 */
	public double[] getPDistribution(int iTransition) throws IllegalArgumentException;
	
	/**
	 * Return the probability distribution of
	 * the trajectory.
	 *  
	 * @return probability distribution for each class given the complete trajectory
	 */
	public double[] getPDistribution();
	
	/**
	 * Return the probability for the required
	 * couple of transition and class state.
	 * Null if it is not set.
	 * 
	 * @param iTransition index of the transition
	 * @param classStateName class state who get the probability
	 * @return probability of the state at that transition time
	 * @throws IllegalArgumentException in case of error
	 */
	public Double getProbability(int iTransition, String classStateName) throws IllegalArgumentException;
	
	/**
	 * Return the probability for the required
	 * class state.
	 * Null if it is not set.
	 * 
	 * @param classStateName class state who get the probability
	 * @return probability of the state
	 * @throws IllegalArgumentException in case of error
	 */
	public Double getProbability(String classStateName) throws IllegalArgumentException;
	
	/**
	 * Print the result.
	 * 
	 * @return string that summarize the result
	 */
	public String resultToString();
}

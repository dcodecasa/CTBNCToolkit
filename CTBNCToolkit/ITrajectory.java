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
 * Interface that defines trajectories.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public interface ITrajectory<TimeType extends Number> {

	/**
	 * Set an optional name for the trajectory
	 * 
	 * @param name the name
	 */
	public void setName(String name);
	
	/**
	 * Get name of the trajectory. Null if it is
	 * not set.
	 * 
	 * @return name of the trajectory
	 */
	public String getName();
	
	/**
	 * Return the number of transitions.
	 * 
	 * @return number of transition in the trajectory.
	 */
	 public int getTransitionsNumber();

	 /**
	  * Return the time related to the selected
	  * transition.
	  * In case of discrete trajectory it will be
	  * an integer. In case of continuous trajectory
	  * it will be a double.
	  * 
	  * @param iTransition index of the transition
	  * @return the time of the selected transition
	  * @throws IllegalArgumentException in case of illegal argument
	  */
	 public TimeType getTransitionTime(int iTransition) throws IllegalArgumentException;
	 
	 /**
	  * Return the node value for the specific node at the
	  * specific transition.
	  * 
	  * @param iTransition index of the transition
	  * @param nodeIndex node index
	  * @return value of the node at the specified transition
	  * @throws IllegalArgumentException if the is an illegal argument
	  */
	 public String getNodeValue(int iTransition, int nodeIndex) throws IllegalArgumentException;
	 
	 /**
	  * Return the selected transition
	  * 
	  * @param iTransition transition index
	  * @return the selected transition
	  * @throws IllegalArgumentException if the is an illegal argument
	  */
	 public ITransition<TimeType> getTransition(int iTransition) throws IllegalArgumentException;
	 
	 /**
	  * Return the global node indexing
	  * associate with the trajectory.
	  *  
	  * @return global node indexing
	  */
	 public NodeIndexing getNodeIndexing();
	 
}

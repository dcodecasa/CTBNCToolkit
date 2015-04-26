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
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * This interface define the characteristics that a learning
 * algorithm has to follow.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 */
public interface ILearningAlgorithm<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> {
	
	/**
	 * Set the default values for the algorithm
	 * parameters.
	 */
	public void setDefaultParameters();
	
	/**
	 * Set the algorithm parameters.
	 * 
	 * @param params maps of parameters represented as map of "parameter name" and "value"
	 * @throws IllegalArgumentException if there is some error with the parameters
	 */
	public void setParameters(Map<String,Object> params) throws IllegalArgumentException;

	/**
	 * Return a parameter value.
	 * 
	 * @param name name of the parameter
	 * @return the parameter value
	 * @throws IllegalArgumentException in case of some error
	 */
	public Object getParameter(String name) throws IllegalArgumentException;
	
	/**
	 * Set the structure of the model.
	 * Parameter learning algorithm must use the input structure
	 * to learn the parameters over it.
	 * Structural learning algorithm must use the input structure
	 * to start the learning from it or (where it doesn't make
	 * sense) they can ignore the loaded network.
	 * Set to null if you want to remove a previous inserted structure.
	 * 
	 * @param adjMatrix adjacency matrix that represent a graph structure (NxN where N is the number of node)
	 * @throws IllegalArgumentException if there is some error with the defined structure
	 */
	public void setStructure(boolean[][] adjMatrix) throws IllegalArgumentException;
	
	/**
	 * Return the loaded structure
	 * (null if it is not loaded).
	 * 
	 * @return the adjacency matrix that represent the structure (null if it is not loaded).
	 */
	public boolean[][] getStructure();

	/**
	 * Return the help description for the algorithm parameters.
	 * 
	 * @return parameters help
	 */
	public String helpParameters();
	
	/**
	 * Learn the model in input given the training set.
	 * 
	 * @param model model to learn
	 * @param trainingSet training set from which learn the model
	 * @return the learning result (a container for the learned sufficient statistics)
	 * @throws RuntimeException if there is some problem with the learning
	 */
	public ILearningResults learn(ICTClassifier<TimeType,NodeType> model, Collection<ITrajectory<TimeType>> trainingSet)  throws RuntimeException;

}

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
package CTBNCToolkit.clustering;

import CTBNCToolkit.*;


/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * This interface define the structure that a clustering algorithm
 * have to follow.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 */
public interface IClusteringAlgorithm<TimeType extends Number & Comparable<TimeType>, NodeType extends INode>  extends ILearningAlgorithm<TimeType, NodeType> {	
	
	/**
	 * Set the algorithm to use in the classification.
	 * Note: for soft clustering the classification
	 * algorithm must return the probability distribution
	 * of the classes.
	 * 
	 * @param clAlg algorithm to use in the classification
	 * @throws IllegalArgumentException in case illegal arguments
	 */
	public void setClassificationAlgorithm( IClassifyAlgorithm<Double, CTDiscreteNode> clAlg) throws IllegalArgumentException;
	
	/**
	 * Set the stopping criterion to terminate the learning.
	 * 
	 * @param stopCriterion stop criterion to use to stop the clustering algorithm
	 * @throws IllegalArgumentException in case illegal arguments
	 */
	public void setStopCriterion( IStopCriterion stopCriterion) throws IllegalArgumentException;
	
}

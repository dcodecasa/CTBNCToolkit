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
 * This interface define the classifier in Continuous Time.
 * It extends the Continuous Time Models interface.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> node type
 */
public interface ICTClassifier<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> extends IModel<TimeType,NodeType>{

	/**
	 * Return the classification node.
	 * Null if it is not defined.
	 * 
	 * @return classification node
	 */
	public DiscreteNode getClassNode();
	
	/**
	 * Classify a trajectory.
	 * 
	 * @param algorithm classification algorithm
	 * @param trajectory input trajectory
	 * @return classification results
	 * @throws Exception in case of errors
	 */
	public IClassificationResult<TimeType> classify(IClassifyAlgorithm<TimeType,NodeType> algorithm, ITrajectory<TimeType> trajectory) throws Exception;
	
	/**
	 * Classify a trajectory.
	 * 
	 * @param algorithm classification algorithm
	 * @param trajectory input trajectory
	 * @param samplingInterval sampling interval to define a more granularity output trajectory
	 * @return classification results
	 * @throws Exception in case of errors
	 */
	public IClassificationResult<TimeType> classify(IClassifyAlgorithm<TimeType,NodeType> algorithm, ITrajectory<TimeType> trajectory, TimeType samplingInterval) throws Exception;
	
	/**
	 * Classify a trajectory.
	 * 
	 * @param algorithm classification algorithm
	 * @param trajectory input trajectory
	 * @param timeStream sequence of time interval in which calculate the probability distribution of the class
	 * @return classification results
	 * @throws Exception in case of errors
	 */
	public IClassificationResult<TimeType> classify(IClassifyAlgorithm<TimeType,NodeType> algorithm, ITrajectory<TimeType> trajectory, Vector<TimeType> timeStream) throws Exception;
}

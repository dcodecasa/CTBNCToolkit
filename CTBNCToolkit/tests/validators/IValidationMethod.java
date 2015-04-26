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
package CTBNCToolkit.tests.validators;

import java.util.List;

import CTBNCToolkit.*;
import CTBNCToolkit.performances.IPerformances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Interface that models the validation
 * method used (i.e. hold out, k-fold
 * cross validation, ...).
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public interface IValidationMethod<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends IModel<TimeType, NodeType>,PType extends IPerformances> {
	
	/**
	 * Flag to activate or disable
	 * the verbose comments option.
	 * 
	 * @param verbose true to get a full comment, false to get just the essential.
	 */
	public void setVerbose(boolean verbose);
	
	/**
	 * Return the state of the
	 * verbose flag.
	 * 
	 * @return verbose flag state.
	 */
	public boolean getVerbose();
	
	/**
	 * Validate the algorithms.
	 * Learn the model in input using the
	 * specified learning algorithm. Then
	 * make inferences and returns the
	 * performances.
	 * Performances depends on the used
	 * validation method.  
	 * 
	 * @param mdl model to learn
	 * @param learnAlgo learning algorithm
	 * @param infAlgo inference algorithm
	 * @param dataset dataset to make the experimental campaign
	 * @return performances of the experimentation
	 */
	public PType validate(TM mdl,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			IClassifyAlgorithm<TimeType, NodeType> infAlgo,
			List<ITrajectory<TimeType>> dataset);

}

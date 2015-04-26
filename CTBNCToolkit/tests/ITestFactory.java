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
package CTBNCToolkit.tests;

import java.util.List;

import CTBNCToolkit.IModel;
import CTBNCToolkit.INode;
import CTBNCToolkit.ITrajectory;
import CTBNCToolkit.performances.IPerformances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface for the test factory.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public interface ITestFactory<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends IModel<TimeType, NodeType>,PType extends IPerformances> {	
	/**
	 * Generate a new test, with an auto-generated dataset
	 * and return the results.
	 * 
	 * @param testName name of the test
	 * @param datasetDim dimension of the dataset
	 * @return result of the test
	 */
	public ITestResults<TimeType,NodeType,TM,PType> newTest(String testName, int datasetDim);
	
	/**
	 * Generate a new test and return the results.
	 * In the result the real model is unknown.
	 * 
	 * @param testName name of the test
	 * @param model to clone to allow the test learning
	 * @param dataset the dataset for the test
	 * @return result of the test
	 */
	public ITestResults<TimeType,NodeType,TM,PType> newTest(String testName, TM model, List<ITrajectory<TimeType>> dataset);
}

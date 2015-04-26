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

import CTBNCToolkit.*;
import CTBNCToolkit.performances.IPerformances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define a container
 * for the results of a test.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public interface ITestResults<TimeType extends Number & Comparable<TimeType>,NodeType extends INode, TM extends IModel<TimeType, NodeType>,PType extends IPerformances> {

	
	/**
	 * Set the true model.
	 * 
	 * @param trueModel true model to set
	 */
	public void setTrueModel(TM trueModel);
	
	/**
	 * Return the model used to generate the dataset.
	 * 
	 * @return model used to generate the dataset
	 */
	public TM getTrueModel();
	
	/**
	 * Get the name of the model
	 * used in the test.
	 * 
	 * @return test name
	 */
	public String getModelName();
	
	/**
	 * Get the dataset used for the test.
	 * 
	 * @return the used dataset
	 */
	public List<ITrajectory<TimeType>> getDataset();
	
	/**
	 * Get the results of the test.
	 * 
	 * @return result of the test
	 */
	public PType getPerformances();
}

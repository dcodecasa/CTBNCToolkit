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
package CTBNCToolkit.performances;

import java.util.Collection;
import java.util.List;

import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IClassificationResult;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface for the classification
 * performances of a single run.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public interface ISingleRunPerformances<TimeType extends Number & Comparable<TimeType>> extends IPerformances {
	
	/**
	 * Set the learned model.
	 * 
	 * @param learnedModel learned model
	 */
	public void setLearnedModel(ICTClassifier<TimeType, ?> learnedModel);
	
	/**
	 * Return the learned model.
	 * 
	 * @return learned model
	 */
	public ICTClassifier<TimeType, ?> getLearnedModel();
	
	/**
	 * Return the trajectories loaded
	 * in the performances.
	 * 
	 * @return the trajectories used to calculate performances
	 */
	public List<IClassificationResult<TimeType>> getResultsTrajectories();
	
	/**
	 * Sort the results trajectories
	 * by name. In this way they can
	 * be returned sorted.
	 * 
	 * @return return the sorted trajectories
	 */
	public List<IClassificationResult<TimeType>> sortResultsTrajectoriesByNames();
	
	/**
	 * Set the model learning time.
	 * 
	 * @param time time to set
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setLearningTime(double time) throws IllegalArgumentException;
	
	/**
	 * Insert the results in the contingency matrix
	 * and in the dataset.
	 * 
	 * @param trjResults all the trajectories
	 */
	public void addResults(Collection<IClassificationResult<TimeType>> trjResults);
	
	/**
	 * Insert the results in the contingency matrix
	 * and in the dataset.
	 * The indexes of results and times must correspond.
	 * 
	 * @param trjResults all the trajectories
	 * @param inferenceTimes inference time for each trajectory
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void addResults(List<IClassificationResult<TimeType>> trjResults, List<Double> inferenceTimes) throws IllegalArgumentException;
	
	/**
	 * Insert a new result in the contingency matrix
	 * and in the dataset.
	 * 
	 * @param trj the trajectory to add to the results
	 */
	public void addResult(IClassificationResult<TimeType> trj);
	
	/**
	 * Insert a new result in the contingency matrix
	 * and in the dataset.
	 * 
	 * @param trj the trajectory to add to the results
	 * @param inferenceTime inference time for the associated trajectory
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void addResult(IClassificationResult<TimeType> trj, double inferenceTime) throws IllegalArgumentException;
	
	/**
	 * Return the inference times for
	 * each instance.
	 * Eventually return null if
	 * no time is loaded.
	 * 
	 * @return the list of inference time for each instance
	 */
	public List<Double> getInferenceTimes();
	
	/**
	 * Calculate all the performances and
	 * remove all the results trajectories
	 * if specified in the parameters.
	 * This is done to reduce the memory
	 * allocation. If the flag it is set
	 * to true the functions that work with
	 * trajectories will not work anymore.
	 *  
	 * @param deleteTrajectories true to delete the results trajectories, false to keep them.
	 */
	public void calculateFinalResults(boolean deleteTrajectories);
}

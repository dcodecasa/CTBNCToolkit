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

import java.util.*;

import CTBNCToolkit.GenericLearningResults;
import CTBNCToolkit.IClassificationResult;
import CTBNCToolkit.SufficientStatistics;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class used to hide the result of clustering algorithms.
 * It contain sufficient statistics and the classified
 * training set. 
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public class ClusteringResults<TimeType extends Number> extends GenericLearningResults {
	
	private List<IClassificationResult<TimeType>> clusterizedTrajectories;
	
	/**
	 * Constructor.
	 * 
	 * @param sStats calculated sufficient statistics
	 * @param clusterizedTrajectories clusterized trajectories
	 */
	ClusteringResults(SufficientStatistics[] sStats, List<IClassificationResult<TimeType>> clusterizedTrajectories) {
		
		super(sStats);
		this.clusterizedTrajectories = clusterizedTrajectories;
	}
	
	
	/**
	 * Return the clusterized trajectories.
	 * 
	 * @return clusterized trajectories
	 */
	public List<IClassificationResult<TimeType>> getClusterizedTrajectories() {
		
		return this.clusterizedTrajectories;
	}
	
}

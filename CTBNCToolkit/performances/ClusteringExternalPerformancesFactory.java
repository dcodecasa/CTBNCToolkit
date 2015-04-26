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

import java.util.Map;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Factory for ClusteringExternalPerformances
 * single run performances.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public class ClusteringExternalPerformancesFactory<TimeType extends Number & Comparable<TimeType>> implements
		ISingleRunPerformancesFactory<TimeType, ClusteringExternalPerformances<TimeType>> {

	private Map<Integer,String> indexToCluster;
	
	/**
	 * Empty constructor.
	 * 
	 * In the new instance generation use
	 * the map indexes to states also as map
	 * indexes to clusters.
	 * The number of clusters MUST BE the same
	 * as the number of states!!!
	 */
	public ClusteringExternalPerformancesFactory() {
		
		this(null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param indexToCluster index to clustering association
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public ClusteringExternalPerformancesFactory(Map<Integer,String> indexToCluster) throws IllegalArgumentException {
	
		if( indexToCluster != null && indexToCluster.size() < 1)
			throw new IllegalArgumentException("Error: indexes to clusters map must contains at least a cluster");
		
		this.indexToCluster = indexToCluster;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformancesFactory#newInstance(java.util.Map)
	 */
	@Override
	public ClusteringExternalPerformances<TimeType> newInstance(
			Map<Integer, String> classIndexToValue) {

		if( this.indexToCluster == null)
			return new ClusteringExternalPerformances<TimeType>(classIndexToValue, classIndexToValue);
		else
			return new ClusteringExternalPerformances<TimeType>(classIndexToValue, this.indexToCluster);
	}

}

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
 * Factory of MicroMacroClusteringPerformances
 * aggregate performances class.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)s
 * @param <SingPType> type of single run performances returned
 */
public class MicroMacroClusteringPerformancesFactory<TimeType extends Number & Comparable<TimeType>, SingPType extends IExternalClusteringSingleRunPerformances<TimeType>> implements
	IAggregatePerformancesFactory<TimeType,MicroMacroClusteringPerformances<TimeType,SingPType>,SingPType>  {

	public ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory;
	private boolean microAvgFlag;
	private Map<Integer,String> indexToCluster;
	

	/**
	 * In the new instance generation use
	 * the map indexes to states also as map
	 * indexes to clusters.
	 * 
	 * @param singleRunFactory factory to use to generate the single run performance to add to the aggregate performances instances.
	 */
	public MicroMacroClusteringPerformancesFactory(ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory) {
		
		this( null, singleRunFactory);
	}
	
	/**
	 * Constructor.
	 * Default microAvgFlag value set to true.
	 * 
	 * @param indexToCluster index to clustering association
	 * @param singleRunFactory factory to use to generate the single run performance to add to the aggregate performances instances.
	 */
	public MicroMacroClusteringPerformancesFactory(Map<Integer,String> indexToCluster, ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory) {
		
		this( indexToCluster, singleRunFactory, true);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param indexToCluster index to clustering association
	 * @param singleRunFactory factory to use to generate the single run performance to add to the aggregate performances instances.
	 * @param microAvgFlag microAvgFlag to set in the new instances.
	 */
	public MicroMacroClusteringPerformancesFactory(Map<Integer,String> indexToCluster, ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory, boolean microAvgFlag) {

		if( indexToCluster != null && indexToCluster.size() < 1)
			throw new IllegalArgumentException("Error: indexes to clusters map must contains at least a cluster");
		if( singleRunFactory == null)
			throw new IllegalArgumentException("Error: null value of the single run fuctory parameter");
		
		this.indexToCluster = indexToCluster;
		this.singleRunFactory = singleRunFactory;
		this.microAvgFlag = microAvgFlag;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformancesFactory#newAggregateInstance(java.util.Map)
	 */
	@Override
	public MicroMacroClusteringPerformances<TimeType, SingPType> newAggregateInstance(
			Map<Integer, String> classIndexToValue) {
		
		MicroMacroClusteringPerformances<TimeType,SingPType> microMacroPerformances;
		
		if( this.indexToCluster == null)
			microMacroPerformances = new MicroMacroClusteringPerformances<TimeType,SingPType>(classIndexToValue, classIndexToValue);
		else
			microMacroPerformances = new MicroMacroClusteringPerformances<TimeType,SingPType>(classIndexToValue, this.indexToCluster);
		
		microMacroPerformances.setMicroFlag( this.microAvgFlag);		
		return microMacroPerformances;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformancesFactory#newSingleRunInstance(java.util.Map)
	 */
	@Override
	public SingPType newSingleRunInstance(Map<Integer, String> classIndexToValue) {
		
		return this.singleRunFactory.newInstance(classIndexToValue);
	}

}

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
 * Factory of MicroMacroClassificationPerformances
 * aggregate performances class.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)s
 * @param <SingPType> type of single run performances returned
 */
public class MicroMacroClassificationPerformancesFactory<TimeType extends Number & Comparable<TimeType>, SingPType extends IClassificationSingleRunPerformances<TimeType>> implements
		IAggregatePerformancesFactory<TimeType,MicroMacroClassificationPerformances<TimeType,SingPType>,SingPType> {

	public ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory;
	private boolean microAvgFlag;

	
	/**
	 * Constructor.
	 * Default microAvgFlag value set to true.
	 * 
	 * @param singleRunFactory factory to use to generate the single run performance to add to the aggregate performances instances.
	 */
	public MicroMacroClassificationPerformancesFactory(ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory) {
		
		this( singleRunFactory, true);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param singleRunFactory factory to use to generate the single run performance to add to the aggregate performances instances.
	 * @param microAvgFlag microAvgFlag to set in the new instances.
	 */
	public MicroMacroClassificationPerformancesFactory(ISingleRunPerformancesFactory<TimeType,SingPType> singleRunFactory, boolean microAvgFlag) {
		
		if( singleRunFactory == null)
			throw new IllegalArgumentException("Error: null value of the single run fuctory parameter");
		
		this.singleRunFactory = singleRunFactory;
		this.microAvgFlag = microAvgFlag;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformancesFactory#newAggregateInstance(java.util.Map)
	 */
	@Override
	public MicroMacroClassificationPerformances<TimeType,SingPType> newAggregateInstance(
			Map<Integer, String> classIndexToValue) {
		
		MicroMacroClassificationPerformances<TimeType,SingPType> microMacroPerformances = new MicroMacroClassificationPerformances<TimeType,SingPType>(classIndexToValue);
		microMacroPerformances.setMicroFlag( this.microAvgFlag);
		
		return microMacroPerformances;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformancesFactory#newSingleRunInstance(java.util.Map)
	 */
	@Override
	public SingPType newSingleRunInstance(
			Map<Integer, String> classIndexToValue) {

		return this.singleRunFactory.newInstance(classIndexToValue);
	}


}

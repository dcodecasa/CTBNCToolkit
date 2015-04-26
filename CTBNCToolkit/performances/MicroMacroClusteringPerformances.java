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
import java.util.Map;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * A performances class that work as
 * a container to calculate both micro
 * and macro averaging performances for
 * clustering.
 * 
 * WARNING:
 * ClustersPartitionsMatrix, Precision, Recall and
 * F-Measure are performances that are strictly
 * associated with the class name. Because the
 * naming of the clusters in all the runs can be
 * different, aggregate values of these performances
 * CAN NOT BE USED as an index of clustering 
 * performances.
 * Associate clusters together (renaming in the
 * best way) it is a NP problem. For this reason,
 * at now, these performances, in case of aggregate
 * clustering, measure must be ignored.
 * In the future we will propose an heuristic to
 * associate clusters in different runs.
 * In single run clustering performances the cited
 * measure can be taken into account.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <PType> performance type
 */
public class MicroMacroClusteringPerformances<TimeType extends Number & Comparable<TimeType>,PType extends IExternalClusteringSingleRunPerformances<TimeType>> implements
	IExternalClusteringAggregatePerformances<TimeType,PType> {
	
	private MicroAvgExternalClusteringAggregatePerformances<TimeType,PType> microAvg;
	private MacroAvgExternalClusteringAggregatePerformances<TimeType,PType> macroAvg;
	
	boolean microFlag;
	
	
	/**
	 * Constructor.
	 * 
	 * @param indexToTrueState index to true state association
	 * @param indexToCluster index to clustering association
	 */
	public MicroMacroClusteringPerformances(Map<Integer,String> indexToTrueState, Map<Integer,String> indexToCluster) {

		this.microAvg = new MicroAvgExternalClusteringAggregatePerformances<TimeType,PType>( indexToTrueState, indexToCluster);
		this.macroAvg = new MacroAvgExternalClusteringAggregatePerformances<TimeType,PType>();
		this.microFlag = true;
	}
	
	
	/**
	 * Return micro averaging performances.
	 * 
	 * @return micro averaging performances.
	 */
	public 	MicroAvgExternalClusteringAggregatePerformances<TimeType,PType> getMicroAveraging() {
		
		return this.microAvg;
	}
	
	/**
	 * Return macro averaging performances.
	 * 
	 * @return macro averaging performances
	 */
	public MacroAvgExternalClusteringAggregatePerformances<TimeType,PType> getMacroAveraging() {
		
		return this.macroAvg;
	}
	
	/**
	 * True to map all the methods on the
	 * micro averaging performances. False
	 * to map the methods on macro averaging
	 * performances.
	 * 
	 * @param microAvgFlag flag used to set the two modalities.
	 */
	public void setMicroFlag(boolean microAvgFlag) {
		
		this.microFlag = microAvgFlag;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#getPerformances()
	 */
	@Override
	public Collection<PType> getPerformances() {
		
		if( this.microFlag)
			return this.microAvg.getPerformances();
		else
			return this.macroAvg.getPerformances();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#addPerformances(java.util.Collection)
	 */
	@Override
	public void addPerformances(Collection<PType> runsPerformances) {
		
		this.microAvg.addPerformances(runsPerformances);
		this.macroAvg.addPerformances(runsPerformances);		}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#addPerformances(java.lang.Object)
	 */
	@Override
	public void addPerformances(PType performance) {
		
		this.microAvg.addPerformances(performance);
		this.macroAvg.addPerformances(performance);		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#getVarianceLearningTime()
	 */
	@Override
	public double getVarianceLearningTime() {
		
		if( this.microFlag)
			return this.microAvg.getVarianceLearningTime();
		else
			return this.macroAvg.getVarianceLearningTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {
		
		if( this.microFlag)
			return this.microAvg.datasetDimension();
		else
			return this.macroAvg.datasetDimension();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {
		
		if( this.microFlag)
			return this.microAvg.classesNumber();
		else
			return this.macroAvg.classesNumber();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {
		
		if( this.microFlag)
			return this.microAvg.indexToValue(index);
		else
			return this.macroAvg.indexToValue(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#valueToIndex(java.lang.String)
	 */
	@Override
	public Integer valueToIndex(String value) {
		
		if( this.microFlag)
			return this.microAvg.valueToIndex(value);
		else
			return this.macroAvg.valueToIndex(value);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getLearningTime()
	 */
	@Override
	public double getLearningTime() {
		
		if( this.microFlag)
			return this.microAvg.getLearningTime();
		else
			return this.macroAvg.getLearningTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getAvgInferenceTime()
	 */
	@Override
	public double getAvgInferenceTime() {
		
		if( this.microFlag)
			return this.microAvg.getAvgInferenceTime();
		else
			return this.macroAvg.getAvgInferenceTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getVarianceInferenceTime()
	 */
	@Override
	public double getVarianceInferenceTime() {
		
		if( this.microFlag)
			return this.microAvg.getVarianceInferenceTime();
		else
			return this.macroAvg.getVarianceInferenceTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterNumber()
	 */
	@Override
	public int clusterNumber() {
		
		if( this.microFlag)
			return this.microAvg.clusterNumber();
		else
			return this.macroAvg.clusterNumber();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#indexToCluster(int)
	 */
	@Override
	public String indexToCluster(int index) {
		
		if( this.microFlag)
			return this.microAvg.indexToCluster(index);
		else
			return this.macroAvg.indexToCluster(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterToIndex(java.lang.String)
	 */
	@Override
	public Integer clusterToIndex(String value) {
		
		if( this.microFlag)
			return this.microAvg.clusterToIndex(value);
		else
			return this.macroAvg.clusterToIndex(value);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getClustersPartitionsMatrix()
	 */
	@Override
	public double[][] getClustersPartitionsMatrix() {
		
		if( this.microFlag)
			return this.microAvg.getClustersPartitionsMatrix();
		else
			return this.macroAvg.getClustersPartitionsMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getPrecisionMatrix()
	 */
	@Override
	public double[][] getPrecisionMatrix() {
		
		if( this.microFlag)
			return this.microAvg.getPrecisionMatrix();
		else
			return this.macroAvg.getPrecisionMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRecallMatrix()
	 */
	@Override
	public double[][] getRecallMatrix() {
		
		if( this.microFlag)
			return this.microAvg.getRecallMatrix();
		else
			return this.macroAvg.getRecallMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFMeasureMatrix()
	 */
	@Override
	public double[][] getFMeasureMatrix() {
		
		if( this.microFlag)
			return this.microAvg.getFMeasureMatrix();
		else
			return this.macroAvg.getFMeasureMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getAssociationMatrix()
	 */
	@Override
	public double[][] getAssociationMatrix() {
		
		if( this.microFlag)
			return this.microAvg.getAssociationMatrix();
		else
			return this.macroAvg.getAssociationMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRandStatistic()
	 */
	@Override
	public double getRandStatistic() {
		
		if( this.microFlag)
			return this.microAvg.getRandStatistic();
		else
			return this.macroAvg.getRandStatistic();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getJaccardCoefficient()
	 */
	@Override
	public double getJaccardCoefficient() {
		
		if( this.microFlag)
			return this.microAvg.getJaccardCoefficient();
		else
			return this.macroAvg.getJaccardCoefficient();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFolkesMallowsIndex()
	 */
	@Override
	public double getFolkesMallowsIndex() {
		
		if( this.microFlag)
			return this.microAvg.getFolkesMallowsIndex();
		else
			return this.macroAvg.getFolkesMallowsIndex();
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#setInSamplePerformances(CTBNToolkit.performances.IExternalClusteringAggregatePerformances)
	 */
	@Override
	public void setInSamplePerformances(
			IExternalClusteringPerformances inSamplePerformances)
			throws IllegalArgumentException {
	
		this.microAvg.setInSamplePerformances( inSamplePerformances);
		this.macroAvg.setInSamplePerformances( inSamplePerformances);
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getInSamplePerformances()
	 */
	@Override
	public IExternalClusteringPerformances getInSamplePerformances() {
		
		if( this.microFlag)
			return this.microAvg.getInSamplePerformances();
		else
			return this.macroAvg.getInSamplePerformances();
	}
	
	
	/**
	 * Print the performances.
	 * 
	 * @return performances in string
	 */
	@Override
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append( "--- MICRO AVERAGING PERFORMANCES ---\n");
		strBuilder.append( this.microAvg.toString());
		
		strBuilder.append( "\n\n--- MACRO AVERAGING PERFORMANCES ---\n");
		strBuilder.append( this.macroAvg.toString());
		
		return strBuilder.toString();
	}

}

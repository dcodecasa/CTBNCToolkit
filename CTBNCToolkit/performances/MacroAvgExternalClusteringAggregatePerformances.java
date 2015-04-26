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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Macro averaging aggregate performance of
 * external clustering performances. It calculates
 * the average of the standard external indexes.
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
 * best way) it is an hard problem. For this reason,
 * at now, these performances, in case of aggregate
 * clustering, measure must be ignored.
 * In the future we will propose an heuristic to
 * associate clusters in different runs.
 * In single run clustering performances the cited
 * measure can be taken into account.
 *  
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)s
 * @param <PType> performance type
 */
public class MacroAvgExternalClusteringAggregatePerformances<TimeType extends Number,PType extends IExternalClusteringPerformances> implements
		IExternalClusteringAggregatePerformances<TimeType, PType> {

	private IExternalClusteringPerformances inSamplePerformances;
	
	private List<PType> performances;
	private int datasetDimension;
	
	private double avgLearningTime;			// average learning time
	private double varLearningTime;			// variance learning time
	private double avgInferenceTime;			// average inference time
	private double varInferenceTime;			// variance of inference time
	
	private double[][] clustersPartitionsMatrix;
	private double[][] precisionM;
	private double[][] recallM;
	private double[][] fMeasureM;
	private double[][] associationMatrix;
	
	private double randStatistic;
	private double jaccardCoefficient;
	private double folkesMallowsIndex;
	
	/**
	 * Constructor.
	 */
	public MacroAvgExternalClusteringAggregatePerformances() {
		
		this.initializeStructures();
	}
	
	/**
	 * Initialize the structures
	 */
	protected void initializeStructures() {

		this.performances = new LinkedList<PType>();
		this.datasetDimension = 0;
		this.inSamplePerformances = null;
		
		this.resetPerformances();
	}

	/**
	 * Reset all the performances.
	 */
	protected void resetPerformances() {

		this.clustersPartitionsMatrix = null;
		this.precisionM = null;
		this.recallM = null;
		this.fMeasureM = null;
		this.associationMatrix = null;
		
		this.randStatistic = -1.0;
		this.jaccardCoefficient = -1.0;
		this.folkesMallowsIndex = -1.0;
		
		this.avgLearningTime = -1.0;
		this.varLearningTime = -1.0;
		this.avgInferenceTime = -1.0;
		this.varInferenceTime = -1.0;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#getPerformances()
	 */
	@Override
	public Collection<PType> getPerformances() {
		
		return this.performances;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#addPerformances(java.util.Collection)
	 */
	@Override
	public void addPerformances(Collection<PType> runsPerformances) {
		
		if(runsPerformances == null)
			throw new IllegalArgumentException("Error: null argument");

		Iterator<PType> iter = runsPerformances.iterator();
		while( iter.hasNext())
			this.addPerformances( iter.next());			
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#addPerformances(java.lang.Object)
	 */
	@Override
	public void addPerformances(PType performance) {
		
		if(performance == null)
			throw new IllegalArgumentException("Error: null argument");
		
		if( performances.size() > 0) {
			for( int i = 0; i < this.performances.get(0).classesNumber(); ++i) {
				if( performance.valueToIndex(this.performances.get(0).indexToValue(i)) != i)
					throw new IllegalArgumentException("Error: the performance classes are not coherent with the aggregate performance classes");
			}
			
			for( int i = 0; i < this.performances.get(0).clusterNumber(); ++i) {
				if( performance.clusterToIndex(this.performances.get(0).indexToCluster(i)) != i)
					throw new IllegalArgumentException("Error: the performance clusters are not coherent with the aggregate performance clusters");
			}
		}
		
		this.datasetDimension += performance.datasetDimension();
		this.performances.add(performance);	
		this.resetPerformances();
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {
		
		return this.datasetDimension;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getLearningTime()
	 */
	@Override
	public double getLearningTime() {
		
		if( this.performances.size() == 0)
			return Double.NaN;

		if( this.avgLearningTime < 0.0) {
			
			double time;
			this.avgLearningTime = 0.0;
			for( int i = 0; i < this.performances.size(); ++i)
				if(! Double.isNaN( time = this.performances.get(i).getLearningTime())) {
					this.avgLearningTime += time;
				} else {
					this.avgLearningTime = Double.NaN;
					return this.avgLearningTime;
				}
			this.avgLearningTime /= (double)this.performances.size();
		}
		
		return this.avgLearningTime;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#getVarianceLearningTime()
	 */
	@Override
	public double getVarianceLearningTime() {
		
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.varLearningTime < 0) {
			if( Double.isNaN( this.getLearningTime())) {
			
				this.varLearningTime = Double.NaN;
			} else {
	
				double time;
				this.varLearningTime = 0.0;
				for(int i = 0; i < this.performances.size(); ++i)
					if(! Double.isNaN( time = this.performances.get(i).getLearningTime())) {
						this.varLearningTime += Math.pow( time - this.avgLearningTime, 2);
					} else {
						this.varLearningTime = Double.NaN;
						return this.varLearningTime;
					}

				this.varLearningTime /= (double)this.performances.size();
			}
		}
		
		return this.varLearningTime;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getAvgInferenceTime()
	 */
	@Override
	public double getAvgInferenceTime() {
		
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.avgInferenceTime < 0.0) {
			
			if( this.performances.size() == 0) {
				this.avgInferenceTime = Double.NaN;
				return this.avgInferenceTime;
			}
			
			double time;
			this.avgInferenceTime = 0.0;
			for( int i = 0; i < this.performances.size(); ++i) {
				if(! Double.isNaN( time = this.performances.get(i).getAvgInferenceTime())) {
					this.avgInferenceTime += time;
				} else {
					this.avgInferenceTime = Double.NaN;
					return this.avgInferenceTime;
				}
			}
			this.avgInferenceTime /= (double)this.performances.size();
		}
		
		return this.avgInferenceTime;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getVarianceInferenceTime()
	 */
	@Override
	public double getVarianceInferenceTime() {

		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.varInferenceTime < 0.0) {
			if( Double.isNaN( this.getAvgInferenceTime())) {
				this.varInferenceTime = Double.NaN;
			} else {
			
				double time;
				this.varInferenceTime = 0.0;
				for( int i = 0; i < this.performances.size(); ++i) {
					if(! Double.isNaN( time = this.performances.get(i).getVarianceInferenceTime())) {
						this.varInferenceTime += time;
					} else {
						this.varInferenceTime = Double.NaN;
						return this.varInferenceTime;
					}
						
				}
				this.varInferenceTime /= (double)this.performances.size();
			}
		}
		
		return this.varInferenceTime;
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterNumber()
	 */
	@Override
	public int clusterNumber() {
		
		if( this.performances.isEmpty())
			return 0;
		
		return this.performances.get(0).clusterNumber();
	}	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {
		
		if( this.performances.isEmpty())
			return 0;
		
		return this.performances.get(0).classesNumber();
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {
		
		if( this.performances.isEmpty())
			return null;
		
		return this.performances.get(0).indexToValue(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#valueToIndex(java.lang.String)
	 */
	@Override
	public Integer valueToIndex(String value) {
		
		if( this.performances.isEmpty())
			return null;
		
		return this.performances.get(0).valueToIndex(value);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#indexToCluster(int)
	 */
	@Override
	public String indexToCluster(int index) {

		if( this.performances.isEmpty())
			return null;
		
		return this.performances.get(0).indexToCluster(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterToIndex(java.lang.String)
	 */
	@Override
	public Integer clusterToIndex(String value) {
		
		if( this.performances.isEmpty())
			return null;
		
		return this.performances.get(0).clusterToIndex(value);
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getClustersPartitionsMatrix()
	 */
	@Override
	public double[][] getClustersPartitionsMatrix() {
		
		if( this.performances.size() == 0)
			return null;
		
		double[][] tmpMx;
		if( this.clustersPartitionsMatrix == null) {
			
			tmpMx = this.performances.get(0).getClustersPartitionsMatrix();
			// Initialization
			this.clustersPartitionsMatrix = new double[tmpMx.length][tmpMx[0].length];
			for(int i = 0; i < this.clustersPartitionsMatrix.length; ++i)
				for(int j = 0; j < this.clustersPartitionsMatrix[0].length; ++j)
					this.clustersPartitionsMatrix[i][j] = 0;
			
			// Average calculation
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				// Checking
				tmpMx = iterPerf.next().getClustersPartitionsMatrix();
				if( tmpMx.length != this.clustersPartitionsMatrix.length || tmpMx[0].length != this.clustersPartitionsMatrix[0].length)
					throw new RuntimeException("Error: performances' clusters partitions matrix do not correspond");
				
				// Sum
				for(int i = 0; i < this.clustersPartitionsMatrix.length; ++i)
					for(int j = 0; j < this.clustersPartitionsMatrix[0].length; ++j)
						this.clustersPartitionsMatrix[i][j] += tmpMx[i][j];
			}
			
			// Normalization
			for(int i = 0; i < this.clustersPartitionsMatrix.length; ++i)
				for(int j = 0; j < this.clustersPartitionsMatrix[0].length; ++j)
					this.clustersPartitionsMatrix[i][j] /= (double)this.performances.size();
		}
		
		return this.clustersPartitionsMatrix;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getPrecisionMatrix()
	 */
	@Override
	public double[][] getPrecisionMatrix() {

		if( this.performances.size() == 0)
			return null;
		
		double[][] tmpMx;
		if( this.precisionM == null) {
			
			tmpMx = this.performances.get(0).getPrecisionMatrix();
			// Initialization
			this.precisionM = new double[tmpMx.length][tmpMx[0].length];
			for(int i = 0; i < this.precisionM.length; ++i)
				for(int j = 0; j < this.precisionM[0].length; ++j)
					this.precisionM[i][j] = 0.0;
			
			// Average calculation
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				// Checking
				tmpMx = iterPerf.next().getPrecisionMatrix();
				if( tmpMx.length != this.precisionM.length || tmpMx[0].length != this.precisionM[0].length)
					throw new RuntimeException("Error: performances' clusters partitions matrix do not correspond");
				
				// Sum
				for(int i = 0; i < this.precisionM.length; ++i)
					for(int j = 0; j < this.precisionM[0].length; ++j)
						this.precisionM[i][j] += tmpMx[i][j];
			}
			
			// Normalization
			for(int i = 0; i < this.precisionM.length; ++i)
				for(int j = 0; j < this.precisionM[0].length; ++j)
					this.precisionM[i][j] /= (double)this.performances.size();
		}
		
		return this.precisionM;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRecallMatrix()
	 */
	@Override
	public double[][] getRecallMatrix() {

		if( this.performances.size() == 0)
			return null;
		
		double[][] tmpMx;
		if( this.recallM == null) {
			
			tmpMx = this.performances.get(0).getRecallMatrix();
			// Initialization
			this.recallM = new double[tmpMx.length][tmpMx[0].length];
			for(int i = 0; i < this.recallM.length; ++i)
				for(int j = 0; j < this.recallM[0].length; ++j)
					this.recallM[i][j] = 0.0;
			
			// Average calculation
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				// Checking
				tmpMx = iterPerf.next().getRecallMatrix();
				if( tmpMx.length != this.recallM.length || tmpMx[0].length != this.recallM[0].length)
					throw new RuntimeException("Error: performances' clusters partitions matrix do not correspond");
				
				// Sum
				for(int i = 0; i < this.recallM.length; ++i)
					for(int j = 0; j < this.recallM[0].length; ++j)
						this.recallM[i][j] += tmpMx[i][j];
			}
			
			// Normalization
			for(int i = 0; i < this.recallM.length; ++i)
				for(int j = 0; j < this.recallM[0].length; ++j)
					this.recallM[i][j] /= (double)this.performances.size();
		}
		
		return this.recallM;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFMeasureMatrix()
	 */
	@Override
	public double[][] getFMeasureMatrix() {

		if( this.performances.size() == 0)
			return null;
		
		double[][] tmpMx;
		if( this.fMeasureM == null) {
			
			tmpMx = this.performances.get(0).getFMeasureMatrix();
			// Initialization
			this.fMeasureM = new double[tmpMx.length][tmpMx[0].length];
			for(int i = 0; i < this.fMeasureM.length; ++i)
				for(int j = 0; j < this.fMeasureM[0].length; ++j)
					this.fMeasureM[i][j] = 0.0;
			
			// Average calculation
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				// Checking
				tmpMx = iterPerf.next().getFMeasureMatrix();
				if( tmpMx.length != this.fMeasureM.length || tmpMx[0].length != this.fMeasureM[0].length)
					throw new RuntimeException("Error: performances' clusters partitions matrix do not correspond");
				
				// Sum
				for(int i = 0; i < this.fMeasureM.length; ++i)
					for(int j = 0; j < this.fMeasureM[0].length; ++j)
						this.fMeasureM[i][j] += tmpMx[i][j];
			}
			
			// Normalization
			for(int i = 0; i < this.fMeasureM.length; ++i)
				for(int j = 0; j < this.fMeasureM[0].length; ++j)
					this.fMeasureM[i][j] /= (double)this.performances.size();
		}
		
		return this.fMeasureM;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getAssociationMatrix()
	 */
	@Override
	public double[][] getAssociationMatrix() {
		
		if( this.performances.size() == 0)
			return null;
		
		double[][] tmpMx;
		if( this.associationMatrix == null) {
			
			tmpMx = this.performances.get(0).getAssociationMatrix();
			// Initialization
			this.associationMatrix = new double[tmpMx.length][tmpMx[0].length];
			for(int i = 0; i < this.associationMatrix.length; ++i)
				for(int j = 0; j < this.associationMatrix[0].length; ++j)
					this.associationMatrix[i][j] = 0;
			
			// Average calculation
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				// Checking
				tmpMx = iterPerf.next().getAssociationMatrix();
				if( tmpMx.length != this.associationMatrix.length || tmpMx[0].length != this.associationMatrix[0].length)
					throw new RuntimeException("Error: performances' clusters partitions matrix do not correspond");
				
				// Sum
				for(int i = 0; i < this.associationMatrix.length; ++i)
					for(int j = 0; j < this.associationMatrix[0].length; ++j)
						this.associationMatrix[i][j] += tmpMx[i][j];
			}
			
			// Normalization
			for(int i = 0; i < this.associationMatrix.length; ++i)
				for(int j = 0; j < this.associationMatrix[0].length; ++j)
					this.associationMatrix[i][j] /= (double)this.performances.size();
		}
		
		return this.associationMatrix;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRandStatistic()
	 */
	@Override
	public double getRandStatistic() {
		
		if( this.performances.size() == 0)
			return Double.NaN;
	
		if( this.randStatistic < 0) {
		
			this.randStatistic = 0;
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				double perfValue = iterPerf.next().getRandStatistic();
				if( Double.isNaN( perfValue)) {
					this.randStatistic = Double.NaN;
					return this.randStatistic;
				}
				
				this.randStatistic += perfValue;
			}
			
			this.randStatistic /= (double)this.performances.size();
		}
		
		return this.randStatistic;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getJaccardCoefficient()
	 */
	@Override
	public double getJaccardCoefficient() {
		
		if( this.performances.size() == 0)
			return Double.NaN;
	
		if( this.jaccardCoefficient < 0) {
		
			this.jaccardCoefficient = 0;
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				double perfValue = iterPerf.next().getJaccardCoefficient();
				if( Double.isNaN( perfValue)) {
					this.jaccardCoefficient = Double.NaN;
					return this.jaccardCoefficient;
				}
				
				this.jaccardCoefficient += perfValue;
			}
			
			this.jaccardCoefficient /= (double)this.performances.size();
		}
		
		return this.jaccardCoefficient;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFolkesMallowsIndex()
	 */
	@Override
	public double getFolkesMallowsIndex() {
		
		if( this.performances.size() == 0)
			return Double.NaN;
	
		if( this.folkesMallowsIndex < 0) {
		
			this.folkesMallowsIndex = 0;
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				double perfValue = iterPerf.next().getFolkesMallowsIndex();
				if( Double.isNaN( perfValue)) {
					this.folkesMallowsIndex = Double.NaN;
					return this.folkesMallowsIndex;
				}
				
				this.folkesMallowsIndex += perfValue;
			}
			
			this.folkesMallowsIndex /= (double)this.performances.size();
		}
		
		return this.folkesMallowsIndex;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#setInSamplePerformances(CTBNToolkit.performances.IExternalClusteringAggregatePerformances)
	 */
	@Override
	public void setInSamplePerformances(
			IExternalClusteringPerformances inSamplePerformances)
			throws IllegalArgumentException {
		
		this.inSamplePerformances = inSamplePerformances;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getInSamplePerformances()
	 */
	@Override
	public IExternalClusteringPerformances getInSamplePerformances() {

		return this.inSamplePerformances;
	}

	
	/**
	 * Print the performances.
	 * 
	 * @param performances performances to print
	 * @return performances in string
	 */
	@Override
	public String toString() {
		
		return ClusteringExternalPerformances.toString(this);
	}
}

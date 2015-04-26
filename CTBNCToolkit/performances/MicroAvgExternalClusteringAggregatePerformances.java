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
import java.util.Map;

import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IClassificationResult;

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
public class MicroAvgExternalClusteringAggregatePerformances<TimeType extends Number & Comparable<TimeType>,PType extends IExternalClusteringSingleRunPerformances<TimeType>> extends
	ClusteringExternalPerformances<TimeType> implements
	IExternalClusteringAggregatePerformances<TimeType, PType> {
	
	private IExternalClusteringPerformances inSamplePerformances;
	
	private List<PType> performances;
	private boolean associationMatrixSet;
	private boolean clustersPartitionsMatrixSet;
	
	private double avgLearningTime;			// average learning time
	private double varLearningTime;			// variance learning time
	
	
	/**
	 * Constructor.
	 * 
	 * @param indexToTrueState index to true state association
	 * @param indexToCluster index to clustering association
	 */
	public MicroAvgExternalClusteringAggregatePerformances(Map<Integer,String> indexToTrueState, Map<Integer,String> indexToCluster) {
		
		super( indexToTrueState, indexToCluster);
	}

	@Override
	protected void initializeStructures() {

		super.initializeStructures();
		
		this.performances = new LinkedList<PType>();
	}

	@Override
	protected void resetPerformances() {

		super.resetPerformances();
		
		this.associationMatrixSet = false;
		this.clustersPartitionsMatrixSet = false;
		this.avgLearningTime = -1.0;
		this.varLearningTime = -1.0;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#setLearnedModel(CTBNToolkit.ICTClassifier)
	 */
	@Override
	public void setLearnedModel(
			ICTClassifier<TimeType, ?> learnedModel) throws RuntimeException {

		throw new RuntimeException("Method do no implemented in MicroAvgExternalClusteringAggregatePerformances class");	
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getLearnedModel()
	 */
	@Override
	public ICTClassifier<TimeType, ?> getLearnedModel() throws RuntimeException {

		throw new RuntimeException("Method do no implemented in MicroAvgExternalClusteringAggregatePerformances class");	
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#setLearningTime(double)
	 */
	@Override
	public void setLearningTime(double time) throws RuntimeException {

		throw new RuntimeException("Method do no implemented in MicroAvgExternalClusteringAggregatePerformances class");	
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getInferenceTimes()
	 */
	@Override
	public List<Double> getInferenceTimes() {
		
		List<Double> infTimes = new LinkedList<Double>();
		for( int i = 0; i < this.performances.size(); ++i)
			infTimes.addAll( this.performances.get(i).getInferenceTimes());
		
		return infTimes;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getResultsTrajectories()
	 */
	@Override
	public List<IClassificationResult<TimeType>> getResultsTrajectories() {
		
		List<IClassificationResult<TimeType>> resultsTrj = new LinkedList<IClassificationResult<TimeType>>();
		for( int i = 0; i < this.performances.size(); ++i)
			resultsTrj.addAll( this.performances.get(i).getResultsTrajectories());
		
		return resultsTrj;
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

		// Add the results trajectories of the performance
		// in the "this" performance dataset
		super.addResults( performance.getResultsTrajectories(), performance.getInferenceTimes());
		
		this.performances.add( performance);	
		this.resetPerformances();
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IAggregatePerformances#getPerformances()
	 */
	@Override
	public Collection<PType> getPerformances() {
		
		return this.performances;
	}

	/**
	 * Calculate the matrix of the relations
	 * between clusters and real partitions.
	 * It is calculated as the average of the
	 * matrix of each performance.
	 * 
	 * @return matrix of counts (cluster, true class)
	 * @throws RuntimeException if some error occurs (i.e. the class node is not find in the dataset)
	 */
	protected double[][] calculateClustersPartitionsMatrix() throws RuntimeException {
		if( this.clustersPartitionsMatrixSet)
			return this.getClustersPartitionsMatrix();
				
		// Table initialization
		double[][] newMatrix = new double[this.clusterNumber()][this.classesNumber()];
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] = 0;
		
		// Sum all the performances matrixes
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext()) {
			PType perf = iter.next();
			
			double[][] matrix = perf.getClustersPartitionsMatrix();
			for(int i = 0; i < newMatrix.length; ++i) {
				String valI = this.indexToValue(i);
				for(int j = 0; j < newMatrix[0].length; ++j)
					newMatrix[i][j] += matrix[perf.valueToIndex(valI)][perf.valueToIndex(this.indexToValue(j))];
			}
		}
		// Make the average
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] /= (double)this.performances.size();
		
		super.setClustersPartitionsMatrix(newMatrix);
		this.clustersPartitionsMatrixSet = true;	
		
		return this.getClustersPartitionsMatrix();	
	}	
	
	/**
	 * Calculate the 2x2 association matrix:
	 * SS SD
	 * DS DD
	 * where:
	 * - SS: is the number of data pairs that
	 *    are in the same cluster and in the
	 *    same original partition (true state)
	 * - SD: is the number of data pairs that
	 *    are in the same cluster but in
	 *    different partitions (true state)
	 * - DS: is the number of data pairs that
	 *    are in different clusters but in
	 *    the same partition (true state)
	 * - DD: is the number of data pairs that
	 *    are in different clusters and in
	 *    different partitions
	 * The matrix is calculated as the average
	 * of the matrix of each performance.
	 * 
	 * @return return 2x2 association matrix
	 */
	protected double[][] calculateAssociationMatrix() {
		
		if( this.associationMatrixSet)
			return this.getAssociationMatrix();

		// Table initialization
		double[][] newMatrix = new double[2][2];
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] = 0;
		
		// Sum all the performances matrixes
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext()) {
			PType perf = iter.next();
			
			double[][] matrix = perf.getAssociationMatrix();
			for(int i = 0; i < newMatrix.length; ++i) {
				String valI = this.indexToValue(i);
				for(int j = 0; j < newMatrix[0].length; ++j)
					newMatrix[i][j] += matrix[perf.valueToIndex(valI)][perf.valueToIndex(this.indexToValue(j))];
			}
		}
		// Make the average
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] /= (double)this.performances.size();
		
		super.setAssociationMatrix(newMatrix);
		this.associationMatrixSet = true;	
		
		return this.getAssociationMatrix();
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

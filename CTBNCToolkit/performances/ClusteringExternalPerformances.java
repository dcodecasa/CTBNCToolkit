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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IClassificationResult;
import CTBNCToolkit.NodeIndexing;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class of external performances for the
 * clustering.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public class ClusteringExternalPerformances<TimeType extends Number & Comparable<TimeType>> implements IExternalClusteringSingleRunPerformances<TimeType> {

	private IExternalClusteringPerformances inSamplePerformances;
	
	private Map<Integer,String> indexToTrueState;
	private Map<String,Integer> trueStateToIndex;
	private Map<Integer,String> indexToCluster;
	private Map<String,Integer> clusterToIndex;
	
	private ICTClassifier<TimeType, ?> learnedModel;
	
	private List<IClassificationResult<TimeType>> clusterizedTrajectories;
	private int nTrajectories;
	private List<Double> inferenceTimes;
	
	private double learningTime;
	private double sumInferenceTimes;			// sum of inferences times
	private double avgInferenceTime;			// average inference time
	private double varInferenceTime;			// variance of inference time
	
	private double[][] clustersPartitionsMatrix;
	private double[][] precisionM;
	private double[][] recallM;
	private double[][] fMeasureM;
	private double[][] associationMatrix;
	
	
	/**
	 * Constructor.
	 * 
	 * @param indexToTrueState index to true state association
	 * @param indexToCluster index to clustering association
	 */
	public ClusteringExternalPerformances(Map<Integer,String> indexToTrueState, Map<Integer,String> indexToCluster) {
		
		if( indexToTrueState == null)
			throw new IllegalArgumentException("Error: null indexes to true states map");
		if( indexToTrueState.size() < 1)
			throw new IllegalArgumentException("Error: indexes to true states map must contains at least a state");
		if( indexToCluster == null)
			throw new IllegalArgumentException("Error: null indexes to clusters map");
		if( indexToCluster.size() < 1)
			throw new IllegalArgumentException("Error: indexes to clusters map must contains at least a cluster");
		
		// True states maps
		this.trueStateToIndex = new TreeMap<String,Integer>();
		this.indexToTrueState = indexToTrueState;
		
		for( int i = 0; i < this.indexToTrueState.size(); ++i) {
			String val = this.indexToTrueState.get(i);
			if( val == null)
				throw new IllegalArgumentException("Error: key " + i + "doesn't included in the map index to value");
			
			this.trueStateToIndex.put(val, i);
		}
		
		// Clustering maps
		this.clusterToIndex = new TreeMap<String,Integer>();
		this.indexToCluster = indexToCluster;
		
		for( int i = 0; i < this.indexToCluster.size(); ++i) {
			String val = this.indexToCluster.get(i);
			if( val == null)
				throw new IllegalArgumentException("Error: key " + i + "doesn't included in the map index to value");
			
			this.clusterToIndex.put(val, i);
		}
		
		this.initializeStructures();
	}
	
	
	/**
	 * Initialize the structures
	 */
	protected void initializeStructures() {

		this.learnedModel = null;
		this.clusterizedTrajectories = new LinkedList<IClassificationResult<TimeType>>();
		this.nTrajectories = 0;
		
		this.inferenceTimes = new LinkedList<Double>();		
		this.learningTime = -1.0;
		this.sumInferenceTimes = 0.0;
		
		this.resetPerformances();
	}


	/**
	 * Reset all the performances.
	 */
	protected void resetPerformances() {

		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: it is impossible reset performances after the final calculation call");
		
		this.clustersPartitionsMatrix = null;
		this.precisionM = null;
		this.recallM = null;
		this.fMeasureM = null;
		this.associationMatrix = null;
		
		this.avgInferenceTime = -1.0;
		this.varInferenceTime = -1.0;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {
		
		return this.nTrajectories;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {
		
		return this.trueStateToIndex.size();
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterNumber()
	 */
	@Override
	public int clusterNumber() {

		return this.clusterToIndex.size();
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {

		return this.indexToTrueState.get(index);
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#valueToIndex(java.lang.String)
	 */
	@Override
	public Integer valueToIndex(String value) {

		return this.trueStateToIndex.get( value);
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#indexToCluster(int)
	 */
	@Override
	public String indexToCluster(int index) {

		return this.indexToCluster.get(index);
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#clusterToIndex(java.lang.String)
	 */
	@Override
	public Integer clusterToIndex(String value) {

		return this.clusterToIndex.get( value);
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#setLearningTime(double)
	 */
	@Override
	public void setLearningTime(double time) throws IllegalArgumentException {

		if( time < 0)
			throw new IllegalArgumentException("Error: time can not be negative");
		
		this.learningTime = time;			
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getLearningTime()
	 */
	@Override
	public double getLearningTime() {

		if( this.learningTime < 0)
			this.learningTime = Double.NaN;
		
		return this.learningTime;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getAvgInferenceTime()
	 */
	@Override
	public double getAvgInferenceTime() {
		
		if( this.avgInferenceTime < 0) {
			if( this.inferenceTimes == null || this.inferenceTimes.size() == 0)
				this.avgInferenceTime = Double.NaN;
			else
				this.avgInferenceTime = this.sumInferenceTimes / (double)this.inferenceTimes.size();
		}
		
		return this.avgInferenceTime;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getVarianceInferenceTime()
	 */
	@Override
	public double getVarianceInferenceTime() {
		
		if( this.varInferenceTime < 0) {
			if( this.inferenceTimes == null || this.inferenceTimes.size() == 0) {
				
				this.varInferenceTime = Double.NaN;
			} else {
				this.getAvgInferenceTime();
		
				this.varInferenceTime = 0.0;
				for(int i = 0; i < this.inferenceTimes.size(); ++i)
					this.varInferenceTime += Math.pow(this.inferenceTimes.get(i) - this.avgInferenceTime, 2);
			
				this.varInferenceTime /= (double)this.inferenceTimes.size();
			}
		}
		
		return this.varInferenceTime;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getInferenceTimes()
	 */
	@Override
	public List<Double> getInferenceTimes() {
		
		return this.inferenceTimes;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#setLearnedModel(CTBNToolkit.ICTClassifier)
	 */
	@Override
	public void setLearnedModel(
			ICTClassifier<TimeType, ?> learnedModel) {

		this.learnedModel = learnedModel;
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getLearnedModel()
	 */
	@Override
	public ICTClassifier<TimeType, ?> getLearnedModel() {
		
		return this.learnedModel;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#addResults(java.util.Collection)
	 */
	@Override
	public void addResults(
			Collection<IClassificationResult<TimeType>> trjResults) {

		Iterator<IClassificationResult<TimeType>> iterTrj = trjResults.iterator();
		while( iterTrj.hasNext())
			this.addResult( iterTrj.next());
		
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#addResults(java.util.List, java.util.List)
	 */
	@Override
	public void addResults(List<IClassificationResult<TimeType>> trjResults,
			List<Double> inferenceTimes) throws IllegalArgumentException {
		
		if( inferenceTimes == null) {
			this.addResults(trjResults);
			return;
		}
		
		if( trjResults.size() != inferenceTimes.size())
			throw new IllegalArgumentException("Error: the list have not the same length");
		
		for( int i = 0; i < trjResults.size(); ++i) {
			if( inferenceTimes.get(i) == null)
				throw new IllegalArgumentException("Error: null inference time");
			
			this.addResult( trjResults.get(i), inferenceTimes.get(i));
		}
		
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#addResult(CTBNToolkit.IClassificationResult)
	 */
	@Override
	public void addResult(IClassificationResult<TimeType> trj) {
		
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible add results after the final calculation call");
		
		this.clusterizedTrajectories.add( trj);
		++this.nTrajectories;
		
		this.inferenceTimes = null;		
		this.resetPerformances();
		
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#addResult(CTBNToolkit.IClassificationResult, double)
	 */
	@Override
	public void addResult(IClassificationResult<TimeType> trj,
			double inferenceTime) throws IllegalArgumentException {
		
		if( inferenceTime < 0)
			throw new IllegalArgumentException("Error: negative inference time");
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible add results after the final calculation call");
		
		this.clusterizedTrajectories.add( trj);
		++this.nTrajectories;
		
		if( this.inferenceTimes != null) {
			this.sumInferenceTimes += inferenceTime;
			this.inferenceTimes.add( inferenceTime);
		}
		
		this.resetPerformances();
		
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IPerformances#getResultsTrajectories()
	 */
	@Override
	public List<IClassificationResult<TimeType>> getResultsTrajectories() {
		
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible gets the trajectories after the final calculation call");
		
		return this.clusterizedTrajectories;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#sortResultsTrajectoriesByNames()
	 */
	@Override
	public List<IClassificationResult<TimeType>> sortResultsTrajectoriesByNames() {
		
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible sorts trajectories after the final calculation call");
		
		// Sort the dataset
		Collections.sort( this.clusterizedTrajectories, new Comparator<IClassificationResult<TimeType>>() {
			public int compare(IClassificationResult<TimeType> trj1, IClassificationResult<TimeType> trj2) {
				return trj1.getName().compareTo( trj2.getName());
			}
		});

		return this.clusterizedTrajectories;
	}
	
	
	/**
	 * Calculate the matrix of the relations
	 * between clusters and real partitions.
	 * It contains the count for each cluster
	 * and each true class in the original
	 * partitioning.
	 * 
	 * @return matrix of counts (cluster, true class)
	 * @throws RuntimeException if some error occurs (i.e. the class node is not find in the dataset)
	 */
	protected double[][] calculateClustersPartitionsMatrix() throws RuntimeException {
		
		if( this.clustersPartitionsMatrix != null)
			return this.clustersPartitionsMatrix;
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible calculates the cluster partition matrix after the final calculation call");
		
		// Get the global node indexing
		NodeIndexing nodeIndexing = this.clusterizedTrajectories.get(0).getNodeIndexing();
		
		// Table initialization
		this.clustersPartitionsMatrix = new double[clusterToIndex.size()][trueStateToIndex.size()];
		for(int i = 0; i < this.clustersPartitionsMatrix.length; ++i)
			for(int j = 0; j < this.clustersPartitionsMatrix[0].length; ++j)
				this.clustersPartitionsMatrix[i][j] = 0;
		
		// Table calculation
		for(int iTrj = 0; iTrj < this.clusterizedTrajectories.size(); ++iTrj) {
			IClassificationResult<TimeType> trj = this.clusterizedTrajectories.get( iTrj);
			++this.clustersPartitionsMatrix[this.clusterToIndex.get(trj.getClassification())][this.trueStateToIndex.get(trj.getNodeValue(0, nodeIndexing.getClassIndex()))];
		}
		
		return this.clustersPartitionsMatrix;			
	}	
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getClustersPartitionsMatrix()
	 */
	@Override
	public double[][] getClustersPartitionsMatrix() {
		
		if( this.clustersPartitionsMatrix != null)
			return this.clustersPartitionsMatrix;
		
		return this.calculateClustersPartitionsMatrix();
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getPrecisionMatrix()
	 */
	@Override
	public double[][] getPrecisionMatrix() {
		
		if( this.precisionM != null)
			return this.precisionM;
		
		this.getClustersPartitionsMatrix();		
		this.precisionM = new double[this.clusterNumber()][this.classesNumber()];
		
		for( int iCl = 0; iCl < this.precisionM.length; ++iCl) {
			double den = 0;
			for( int iTS2 = 0; iTS2 < this.precisionM[0].length; ++iTS2)
				den += this.clustersPartitionsMatrix[iCl][iTS2];
			
			for( int iTS = 0; iTS < this.precisionM[0].length; ++iTS)
				this.precisionM[iCl][iTS] = this.clustersPartitionsMatrix[iCl][iTS] / den;
		}
		
		return this.precisionM;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRecallMatrix()
	 */
	@Override
	public double[][] getRecallMatrix() {
		
		if( this.recallM != null)
			return this.recallM;
		
		this.getClustersPartitionsMatrix();
		this.recallM = new double[this.clusterNumber()][this.classesNumber()]; 
		
		for( int iTS = 0; iTS < this.recallM[0].length; ++iTS) {
			double den = 0;
			for( int iCl2 = 0; iCl2 < this.recallM.length; ++iCl2)
				den += this.clustersPartitionsMatrix[iCl2][iTS];
			
			for( int iCl = 0; iCl < this.recallM.length; ++iCl)
				this.recallM[iCl][iTS] = this.clustersPartitionsMatrix[iCl][iTS] / den;
		}
		
		return this.recallM;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFMeasureMatrix()
	 */
	@Override
	public double[][] getFMeasureMatrix() {
		
		if( this.fMeasureM != null)
			return this.fMeasureM;
		
		this.getPrecisionMatrix();
		this.getRecallMatrix();
		
		this.fMeasureM = new double[this.clusterNumber()][this.classesNumber()];
		for( int iCl = 0; iCl < this.fMeasureM.length; ++iCl)
			for( int iTS = 0; iTS < this.fMeasureM[0].length; ++iTS) {
				
				double den = this.precisionM[iCl][iTS] + this.recallM[iCl][iTS];
				if( den == 0)
					this.fMeasureM[iCl][iTS] = 0.0;
				else
					this.fMeasureM[iCl][iTS] = 2 * this.precisionM[iCl][iTS] * this.recallM[iCl][iTS] / den;
			}
		
		return this.fMeasureM;
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
	 * 
	 * @return return 2x2 association matrix
	 */
	protected double[][] calculateAssociationMatrix() {
		
		if( this.associationMatrix != null)
			return this.associationMatrix;
		if( this.clusterizedTrajectories == null)
			throw new RuntimeException("Error: impossible calculates the association matrix after the final calculation call");
		
		NodeIndexing nodeIndexing = this.clusterizedTrajectories.get(0).getNodeIndexing();
		
		// Association matrix initialization
		this.associationMatrix = new double[2][2];
		this.associationMatrix[0][0] = 0; this.associationMatrix[0][1] = 0; this.associationMatrix[1][0] = 0; this.associationMatrix[1][1] = 0;
		
		// Matrix calculation
		for(int iTrj1 = 0; iTrj1 < this.clusterizedTrajectories.size(); ++iTrj1) {
			IClassificationResult<TimeType> trj1 = this.clusterizedTrajectories.get( iTrj1);
			String cluster1 = trj1.getClassification();
			String tState1 = trj1.getNodeValue(0, nodeIndexing.getClassIndex());
			
			for(int iTrj2 = iTrj1 + 1; iTrj2 < this.clusterizedTrajectories.size(); ++iTrj2) {
				IClassificationResult<TimeType> trj2 = this.clusterizedTrajectories.get( iTrj2);
				boolean clEqual = cluster1.equals( trj2.getClassification());
				boolean tStateEqual = tState1.equals( trj2.getNodeValue(0, nodeIndexing.getClassIndex()));
				
				if( clEqual) {
					if( tStateEqual)
						++this.associationMatrix[0][0];	// SS
					else
						++this.associationMatrix[0][1];	// SD
				}else {
					if( tStateEqual)
						++this.associationMatrix[1][0];	// DS
					else
						++this.associationMatrix[1][1];	// DD
				}
			}
		}
		
		return this.associationMatrix;
	}	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getAssociationMatrix()
	 */
	@Override
	public double[][] getAssociationMatrix() {
		
		if( this.associationMatrix != null)
			return this.associationMatrix;
		
		return this.calculateAssociationMatrix();
	}
		
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getRandStatistic()
	 */
	@Override
	public double getRandStatistic() {
		
		double m;
		double[][] mx = this.getAssociationMatrix();
		
		m = mx[0][0] + mx[0][1] + mx[1][0] + mx[1][1];
		
		return (mx[0][0] + mx[1][1]) / m;
	}
		
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getJaccardCoefficient()
	 */
	@Override
	public double getJaccardCoefficient() {
		
		double dn;
		double[][] mx = this.getAssociationMatrix();
		
		dn = mx[0][0] + mx[0][1] + mx[1][0];
		
		return mx[0][0] / dn;
	}
		
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IExternalClusteringPerformances#getFolkesMallowsIndex()
	 */
	@Override
	public double getFolkesMallowsIndex() {
		
		double m1,m2;
		double[][] mx = this.getAssociationMatrix();
		
		m1 = mx[0][0] + mx[0][1];
		m2 = mx[0][0] + mx[1][0];
		
		return mx[0][0] / Math.sqrt(m1*m2);
	}	

	
	/**
	 * Set directly the cluster partition matrix.
	 * 
	 * @param matrix matrix to set
	 * @throws IllegalArgumentException in case of errors in the argument
	 */
	protected void setClustersPartitionsMatrix(double[][] matrix) throws IllegalArgumentException {
		
		if(matrix == null)
			throw new IllegalArgumentException("Error: illegal argument");
		if(matrix.length != this.clusterNumber() || matrix[0].length != this.classesNumber())
			throw new IllegalArgumentException("Error: illegal matrix dimension (right cardinality = |Cluster|x|TrueStates|)");
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[0].length; ++j)
				if( matrix[i][j] < 0)
					throw new IllegalArgumentException("Error: contingency matrix can not have values smaller then 0");
	
		this.resetPerformances();
		this.clustersPartitionsMatrix = matrix;
	}
	
	/**
	 * Set directly the association matrix.
	 * 
	 * @param matrix matrix to set
	 * @throws IllegalArgumentException in case of errors in the argument
	 */
	protected void setAssociationMatrix(double[][] matrix) throws IllegalArgumentException {
		
		if(matrix == null)
			throw new IllegalArgumentException("Error: illegal argument");
		if(matrix.length != 2 || matrix[0].length != 2)
			throw new IllegalArgumentException("Error: illegal matrix dimension (it must be a 2x2 matrix)");
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[0].length; ++j)
				if( matrix[i][j] < 0)
					throw new IllegalArgumentException("Error: contingency matrix can not have values smaller then 0");
		
		this.resetPerformances();
		this.associationMatrix = matrix;
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
	
	
	/**
	 * Print the performances in input.
	 * 
	 * @param performances performances to print
	 * @return performances in string
	 */
	static public String toString(IExternalClusteringPerformances performances) {
		
		double[][] mx;
		int fileColumns = performances.clusterNumber() + 1;
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("\nGENERAL INFORMATION;" + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("#clusters;" + performances.clusterNumber() + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("#classes;" + performances.classesNumber() + printSemicolons(fileColumns - 1) + "\n");
		
		strBuilder.append("\nTIME;" + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Average inference time;" + performances.getAvgInferenceTime() + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Variance inference time;" + performances.getVarianceInferenceTime() + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Average learning time;" + performances.getLearningTime() + printSemicolons(fileColumns - 1) + "\n");
		if( IAggregatePerformances.class.isAssignableFrom(performances.getClass()))
			strBuilder.append("Variance leargning time;" + ((IAggregatePerformances<?,?>) performances).getVarianceLearningTime() + printSemicolons(fileColumns - 1) + "\n");
		
		strBuilder.append("\nSUMMARY STATISTICS;" + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Rand statistic;" + performances.getRandStatistic() + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Jaccard coefficient;" + performances.getJaccardCoefficient() + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append("Folkes & Mallows index;" + performances.getFolkesMallowsIndex() + printSemicolons(fileColumns - 1) + "\n");
		
		strBuilder.append("\nASSOCIATION MATRIX;" + printSemicolons(fileColumns - 1) + "\n");
		strBuilder.append(";SS;SD;" + printSemicolons(fileColumns - 3) + "\n");
		strBuilder.append("DS;" + printVector( performances.getAssociationMatrix()[0]) + printSemicolons(fileColumns - 3) + "\n");
		strBuilder.append("DD;" + printVector( performances.getAssociationMatrix()[1]) + printSemicolons(fileColumns - 3) + "\n");
		
		strBuilder.append("\nCLUSTERS PARTITION MATRIX;" + printSemicolons(fileColumns - 1) + "\n");
		mx =  performances.getClustersPartitionsMatrix();
		strBuilder.append(";" + printClusters(performances) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		for(int i = 0; i < performances.classesNumber(); ++i) {
			strBuilder.append(performances.indexToValue(i) + ";" + printVector( mx[i]) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		}
		
		strBuilder.append("\nPRECISION MATRIX;" + printSemicolons(fileColumns - 1) + "\n");
		mx =  performances.getPrecisionMatrix();
		strBuilder.append(";" + printClusters(performances) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		for(int i = 0; i < performances.classesNumber(); ++i) {
			strBuilder.append(performances.indexToValue(i) + ";" + printVector( mx[i]) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		}
		
		strBuilder.append("\nRECALL MATRIX;" + printSemicolons(fileColumns - 1) + "\n");
		mx =  performances.getRecallMatrix();
		strBuilder.append(";" + printClusters(performances) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		for(int i = 0; i < performances.classesNumber(); ++i) {
			strBuilder.append(performances.indexToValue(i) + ";" + printVector( mx[i]) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		}

		strBuilder.append("\nF-MEASURE MATRIX;" + printSemicolons(fileColumns - 1) + "\n");
		mx =  performances.getFMeasureMatrix();
		strBuilder.append(";" + printClusters(performances) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		for(int i = 0; i < performances.classesNumber(); ++i) {
			strBuilder.append(performances.indexToValue(i) + ";" + printVector( mx[i]) + printSemicolons(fileColumns - (performances.clusterNumber() + 1)) + "\n");
		}
		
		return strBuilder.toString();
	}
	
	
	/**
	 * Print all the clusters in the
	 * order separating them by ';'.
	 * 
	 * @param performances performances to print
	 * @return clusters
	 */
	static private String printClusters(IExternalClusteringPerformances performances) {

		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < performances.clusterNumber(); ++i)
			strBuilder.append(performances.indexToCluster(i) + ";");
			
		return strBuilder.toString();
	}

	/**
	 * Print the vector calling for each object
	 * the toString method and adding a ';' after
	 * the call.
	 * 
	 * @param vect vector to print
	 * @return string printed
	 */
	static private String printVector(double[] vect) {
		
		StringBuilder strBuilder = new StringBuilder();
		
		for( int i = 0; i < vect.length; ++i)
			strBuilder.append(vect[i] + ";");
			
		return strBuilder.toString();
	}
	
	/**
	 * Print n semicolons.	 * 
	 * 
	 * @param n number of semicolons to print.
	 * @return sequence of n semicolons
	 */
	static private String printSemicolons(int n) {
		
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < n; ++i)
			strBuilder.append(";");
			
		return strBuilder.toString();
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#calculateFinalResults(boolean)
	 */
	@Override
	public void calculateFinalResults(boolean deleteTrajectories) {

		// Calculate the structures used to calculate performances
		this.calculateClustersPartitionsMatrix();
		this.calculateAssociationMatrix();
		
		// Remove the results trajectories
		if( deleteTrajectories)
			this.clusterizedTrajectories = null;
	}
}

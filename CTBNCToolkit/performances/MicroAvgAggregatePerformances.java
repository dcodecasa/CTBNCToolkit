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

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class used to calculate performance
 * in case of multiple runs (i.e. k-fold
 * cross validation) with micro averaging
 * methods.
 * 
 * Note: the accuracy confidence interval
 * is calculated with the supposition that
 * the performance are calculated over
 * disjointed part of the dataset (i.e. as
 * for the k-fold cross validation)
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <PType> performance type
 */
public class MicroAvgAggregatePerformances<TimeType extends Number & Comparable<TimeType>,PType extends IClassificationSingleRunPerformances<TimeType>> extends
	ClassificationStandardPerformances<TimeType> implements
	IClassificationAggregatePerformances<TimeType,PType> {
	
	private List<PType> performances;
	private boolean contincencyMatrixSet;
	
	private double avgLearningTime;			// average learning time
	private double varLearningTime;			// variance learning time
	
	
	/**
	 * Constructor.
	 * 
	 * @param classIndexToValue map index to class value
	 */
	public MicroAvgAggregatePerformances(Map<Integer,String> classIndexToValue) {
		
		super(classIndexToValue);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param classIndexToValue map index to class value
	 * @param classValueToIndex map class value to index
	 */
	public MicroAvgAggregatePerformances(Map<Integer,String> classIndexToValue,
			Map<String, Integer> classValueToIndex) {
		
		super(classIndexToValue, classValueToIndex);
	}
	
	@Override
	protected void initializeStructures() {

		super.initializeStructures();
		
		this.performances = new LinkedList<PType>();
	}

	@Override
	protected void resetPerformances() {

		super.resetPerformances();
		
		this.contincencyMatrixSet = false;
		this.avgLearningTime = -1.0;
		this.varLearningTime = -1.0;
	}

	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getLearningTime()
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
			this.avgLearningTime /= this.performances.size();
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
	 * @see CTBNToolkit.validation.IAggregatePerformances#addPerformances(java.util.Collection)
	 */
	@Override
	public void addPerformances(
			Collection<PType> runsPerformances) {
		
		if(runsPerformances == null)
			throw new IllegalArgumentException("Error: null argument");

		Iterator<PType> iter = runsPerformances.iterator();
		while( iter.hasNext())
			this.addPerformances( iter.next());		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IAggregatePerformances#addPerformances(CTBNToolkit.validation.IClassificationPerformances)
	 */
	@Override
	public void addPerformances(
			PType performance) {
		
		if(performance == null)
			throw new IllegalArgumentException("Error: null argument");

		for( int i = 0; i < super.classesNumber(); ++i) {
			if( performance.valueToIndex(super.indexToValue(i)) == null)
				throw new IllegalArgumentException("Error: the performance classes are not coherent with the aggregate performance classes");
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

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.ISingleRunPerformances#getContingencyMatrix()
	 */
	@Override
	public double[][] getContingencyMatrix() {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getContingencyMatrix();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAccuracy()
	 */
	@Override
	public double getAccuracy() {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getAccuracy();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAccuracy(java.lang.String)
	 */
	@Override
	public double[] getAccuracy(String confidenceLevel)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getAccuracy(confidenceLevel);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getError()
	 */
	@Override
	public double getError() {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getError();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getPrecision(java.lang.Object)
	 */
	@Override
	public double getPrecision(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getPrecision(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getRecall(java.lang.Object)
	 */
	@Override
	public double getRecall(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getRecall(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFMeasure(java.lang.Object)
	 */
	@Override
	public double getFMeasure(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getFMeasure(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSensitivity(java.lang.Object)
	 */
	@Override
	public double getSensitivity(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getSensitivity(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSpecificity(java.lang.Object)
	 */
	@Override
	public double getSpecificity(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getSpecificity(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getTruePositiveRate(java.lang.Object)
	 */
	@Override
	public double getTruePositiveRate(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getTruePositiveRate(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFalsePositiveRate(java.lang.Object)
	 */
	@Override
	public double getFalsePositiveRate(String classValue)
			throws IllegalArgumentException {

		if( !this.contincencyMatrixSet)
			this.calculateAverageContingencyMatrix();
		
		return super.getFalsePositiveRate(classValue);
	}
	
	
	/**
	 * Calculate the contingency matrix as
	 * average of the contingency matrixes
	 * of all the performances.
	 */
	protected void calculateAverageContingencyMatrix() {
		
		// Initialize the matrix
		double[][] newMatrix = new double[super.classesNumber()][super.classesNumber()];
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] = 0.0;
		
		// Sum all the performances matrixes
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext()) {
			PType perf = iter.next();
			
			double[][] matrix = perf.getContingencyMatrix();
			for(int i = 0; i < newMatrix.length; ++i) {
				String valI = super.indexToValue(i);
				for(int j = 0; j < newMatrix[0].length; ++j)
					newMatrix[i][j] += matrix[perf.valueToIndex(valI)][perf.valueToIndex(super.indexToValue(j))];
			}
		}
		// Make the average
		for(int i = 0; i < newMatrix.length; ++i)
			for(int j = 0; j < newMatrix[0].length; ++j)
				newMatrix[i][j] /= this.performances.size();
		
		super.setContingencyMatrix(newMatrix);
		this.contincencyMatrixSet = true;
	}

}

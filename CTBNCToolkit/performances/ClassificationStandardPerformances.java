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
import java.util.Vector;

import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IClassificationResult;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Standard class for single run performances.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public class ClassificationStandardPerformances<TimeType extends Number & Comparable<TimeType>> implements IClassificationSingleRunPerformances<TimeType> {
	
	private Map<String,Integer> valueToIndex;
	private Map<Integer,String> indexToValue;
	private ICTClassifier<TimeType, ?> learnedModel;
	
	private List<IClassificationResult<TimeType>> dataset;
	private int nTrajectories;
	private List<Double> inferenceTimes;
	private double[][] contingencyMatrix;
	
	private double learningTime;
	private double sumInferenceTimes;			// sum of inferences times
	private double avgInferenceTime;			// average inference time
	private double varInferenceTime;			// variance of inference time
	
	private double accuracy;					// accuracy
	private double[] precision;					// precision
	private double[] recall;					// recall
	private double[] fmeasure;					// f-measure
	private List<List<double[]>> precRec;		// Precision-recall curve
	private double[] AUCPrecRec;				// AUC Precision-recall curve
	private List<List<double[]>> ROC;			// ROC curve
	private double[] AUCROC;					// AUCROC
	private List<List<double[]>> cumulativeResp;// cumulative response
	private List<List<double[]>> liftChart;		// lift chart
	private double[] FPRate;					// false positive rate
	private double brier;						// brier value
	
	private double[] classPositiveNumber;		// number of positive in all the dataset (used only in ROC function, cumulative response and lift chart). Necessary to separate the contingency matrix from the ROC, Bier, cumulative responce, lift chart, and Precision-Recall curves calculation
	
	protected Map<String,Double> normalTable;
	
	/**
	 * Constructor.
	 * 
	 * @param classIndexToValue map index to class value
	 */
	public ClassificationStandardPerformances(Map<Integer,String> classIndexToValue) {
		
		this.valueToIndex = new TreeMap<String,Integer>();
		this.indexToValue = classIndexToValue;
		
		for( int i = 0; i < this.indexToValue.size(); ++i) {
			String val = this.indexToValue.get(i);
			if( val == null)
				throw new IllegalArgumentException("Error: key " + i + "doesn't included in the map index to value");
			
			this.valueToIndex.put(val, i);
		}
		
		this.initializeStructures();
	}	
	
	/**
	 * Constructor.
	 * 
	 * @param classIndexToValue map index to class value
	 * @param classValueToIndex map class value to index
	 */
	public ClassificationStandardPerformances(Map<Integer,String> classIndexToValue, Map<String,Integer> classValueToIndex) {
		
		if(classIndexToValue.size() != classValueToIndex.size())
			throw new IllegalArgumentException("Error: maps index to value and value to index have different dimension");
		for(int i = 0; i < classIndexToValue.size(); ++i)
			if( classIndexToValue.get(i) == null || classValueToIndex.get(classIndexToValue.get(i)) != i)
				throw new IllegalArgumentException("Error: maps index to value and value to index don't correpsond");

		this.valueToIndex = classValueToIndex;
		this.indexToValue = classIndexToValue;
		
		this.initializeStructures();
	}
	
	
	/**
	 * Initialize the structures
	 */
	protected void initializeStructures() {

		this.learnedModel = null;
		this.dataset = new LinkedList<IClassificationResult<TimeType>>();
		this.nTrajectories = 0;
		this.inferenceTimes = new LinkedList<Double>();
		
		this.classPositiveNumber = new double[this.indexToValue.size()];
		this.contingencyMatrix = new double[this.indexToValue.size()][this.indexToValue.size()];
		for( int i = 0; i < this.contingencyMatrix.length; ++i) {
			this.classPositiveNumber[i] = 0.0;
			for( int j = 0; j < this.contingencyMatrix[0].length; ++j)
				this.contingencyMatrix[i][j] = 0.0;
		}
		
		this.learningTime = -1.0;
		this.sumInferenceTimes = 0.0;
		
		this.resetPerformances();
		
		// Value of normal table for confidence interval
		// i.e.: 95% = 1.65 in normal table => this.normalTable.put("%90", 1.65); 
		this.normalTable = new TreeMap<String,Double>();
		this.normalTable.put("99.9%", 3.291);
		this.normalTable.put("99.8%", 3.09);
		this.normalTable.put("99%", 2.576);
		this.normalTable.put("98%", 2.326);
		this.normalTable.put("95%", 1.960);
		this.normalTable.put("90%", 1.645);
		this.normalTable.put("80%", 1.28);
	}


	/**
	 * Reset all the performances.
	 */
	protected void resetPerformances() {

		if( this.dataset == null)
			throw new RuntimeException("Error: it is impossible reset performances after the final calculation call");
		
		this.precision = null;
		this.recall = null;
		this.fmeasure = null;
		this.precRec = null;
		this.AUCPrecRec = null;
		this.ROC = null;
		this.AUCROC = null;
		this.cumulativeResp = null;
		this.liftChart = null;
		this.FPRate = null;
		this.brier = -1;
		
		this.avgInferenceTime = -1.0;
		this.varInferenceTime = -1.0;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#setLearnedModel(CTBNToolkit.ICTClassifier)
	 */
	@Override
	public void setLearnedModel(
			ICTClassifier<TimeType, ?> learnedModel) {

		this.learnedModel = learnedModel;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#getLearnedModel()
	 */
	@Override
	public ICTClassifier<TimeType, ?> getLearnedModel() {
		
		return this.learnedModel;
	}
	

	/**
	 * Set directly the contingency matrix.
	 * 
	 * @param matrix matrix to set
	 * @throws IllegalArgumentException in case of errors in the argument
	 */
	protected void setContingencyMatrix(double[][] matrix) throws IllegalArgumentException {
		
		if(matrix == null)
			throw new IllegalArgumentException("Error: illegal argument");
		if(matrix.length != this.indexToValue.size() || matrix[0].length != this.indexToValue.size())
			throw new IllegalArgumentException("Error: illegal matrix dimension");
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[0].length; ++j)
				if( matrix[i][j] < 0)
					throw new IllegalArgumentException("Error: contingency matrix can not have values smaller then 0");
		
		this.contingencyMatrix = matrix;
		this.resetPerformances();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#setLearningTime(double)
	 */
	@Override
	public void setLearningTime(double time) throws IllegalArgumentException {

		if( time < 0)
			throw new IllegalArgumentException("Error: time can not be negative");
		
		this.learningTime = time;		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#getLearningTime()
	 */
	@Override
	public double getLearningTime() throws RuntimeException {

		if( this.learningTime < 0)
			this.learningTime = Double.NaN;
		
		return this.learningTime;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#getInferenceTimes()
	 */
	@Override
	public List<Double> getInferenceTimes() {

		return this.inferenceTimes;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#getAvgInferenceTime()
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
	 * @see CTBNToolkit.performances.ISingleRunPerformances#getVarianceInferenceTime()
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
	 * @see CTBNToolkit.validation.ISingleRunPerformances#getResultsTrajectories()
	 */
	@Override
	public List<IClassificationResult<TimeType>> getResultsTrajectories() {
		
		if( this.dataset == null)
			throw new RuntimeException("Error: impossible gets the trajectories after the final calculation call");
		
		return this.dataset;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#sortResultsTrajectoriesByNames()
	 */
	@Override
	public List<IClassificationResult<TimeType>> sortResultsTrajectoriesByNames() {
		
		if( this.dataset == null)
			throw new RuntimeException("Error: impossible sorts trajectories after the final calculation call");
		
		// Sort the dataset
		Collections.sort( this.dataset, new Comparator<IClassificationResult<TimeType>>() {
												public int compare(IClassificationResult<TimeType> trj1, IClassificationResult<TimeType> trj2) {
													return trj1.getName().compareTo( trj2.getName());
												}
											});
		
		return this.dataset;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {

		return this.nTrajectories;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {

		return this.indexToValue.size();
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {
		
		return this.indexToValue.get(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#valueToIndex(java.lang.Object)
	 */
	@Override
	public Integer valueToIndex(String value) {

		return this.valueToIndex.get( value);
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#addResults(java.util.Collection)
	 */
	@Override
	public void addResults(Collection<IClassificationResult<TimeType>> trjResults) {
		
		Iterator<IClassificationResult<TimeType>> iterTrj = trjResults.iterator();
		while( iterTrj.hasNext())
			this.addResult( iterTrj.next());
	}
	
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#addResults(java.util.List, java.util.List)
	 */
	@Override
	public void addResults(List<IClassificationResult<TimeType>> trjResults, List<Double> inferenceTimes) throws IllegalArgumentException {

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
	 * @see CTBNToolkit.performances.ISingleRunPerformances#addResult(CTBNToolkit.IClassificationResult, double)
	 */
	@Override
	public void addResult(IClassificationResult<TimeType> trj,
			double inferenceTime) throws IllegalArgumentException {
		
		if( inferenceTime < 0)
			throw new IllegalArgumentException("Error: negative inference time");

		String trueClass = trj.getNodeValue(0, trj.getNodeIndexing().getClassIndex());
		String classification = trj.getClassification();
		
		++this.contingencyMatrix[this.valueToIndex.get(trueClass)][this.valueToIndex.get(classification)];
		++this.classPositiveNumber[this.valueToIndex.get(trueClass)];
		this.dataset.add( trj);
		++this.nTrajectories;
		
		if( this.inferenceTimes != null) {
			this.sumInferenceTimes += inferenceTime;
			this.inferenceTimes.add( inferenceTime);
		}
		
		this.resetPerformances();
		
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#addResult(CTBNToolkit.IClassificationResult)
	 */
	@Override
	public void addResult(IClassificationResult<TimeType> trj) {
		
		String trueClass = trj.getNodeValue(0, trj.getNodeIndexing().getClassIndex());
		String classification = trj.getClassification();
		
		++this.contingencyMatrix[this.valueToIndex.get(trueClass)][this.valueToIndex.get(classification)];
		++this.classPositiveNumber[this.valueToIndex.get(trueClass)];
		this.dataset.add( trj);	
		++this.nTrajectories;
		
		this.inferenceTimes = null;
		
		this.resetPerformances();
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.ISingleRunPerformances#getContingencyMatrix()
	 */
	@Override
	public double[][] getContingencyMatrix() {

		return this.contingencyMatrix;
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAccuracy()
	 */
	@Override
	public double getAccuracy() {

		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.precision == null)
			calculateContingencyPerformances();
		
		return this.accuracy;
	}
	

	/**
	 * Return the accuracy and in confidence
	 * interval in the following form:
	 * lower bound <= accuracy <= upper bound
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"}
	 * @return accuracy confidence interval (lower bound, accuracy, upper bound)
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	@Override
	public double[] getAccuracy(String confidenceLevel) throws IllegalArgumentException {
		
		return  getAccuracy( confidenceLevel, this.datasetDimension());
	}
	
	/**
	 * Return the accuracy and in confidence
	 * interval in the following form:
	 * lower bound <= accuracy <= upper bound
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"}
	 * @param N size of the sample
	 * @return accuracy confidence interval (lower bound, accuracy, upper bound)
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	protected double[] getAccuracy(String confidenceLevel, int N) throws IllegalArgumentException {
		
		if( confidenceLevel == null)
			throw new IllegalArgumentException("Error: null confidence level");
		
		if( this.datasetDimension() == 0) {
			double[] v = new double[3];
			v[0] = v[1] = v[2] = Double.NaN;
			return v;
		}
		
		if( this.precision == null)
			calculateContingencyPerformances();
		
		Double z = this.normalTable.get(confidenceLevel);
		if( z == null)
			throw new IllegalArgumentException("Error: confidence level didn't recognized");
		
		return ClassificationStandardPerformances.binomialInterval(this.accuracy, N, z);
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getError()
	 */
	@Override
	public double getError() {
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.precision == null)
			calculateContingencyPerformances();
		
		return 1.0 - this.accuracy;
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getPrecision(int)
	 */
	@Override
	public double getPrecision(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.precision == null)
			calculateContingencyPerformances();
		
		return this.precision[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getRecall(int)
	 */
	@Override
	public double getRecall(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");

		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.recall == null)
			calculateContingencyPerformances();
		
		return this.recall[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFMeasure(int)
	 */
	@Override
	public double getFMeasure(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");	
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.fmeasure == null)
			calculateContingencyPerformances();
		
		return this.fmeasure[this.valueToIndex.get(classValue)];
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getPrecisionRecallAUC(java.lang.Object)
	 */
	@Override
	public double getPrecisionRecallAUC(String classValue)
			throws IllegalArgumentException {

		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.precRec == null)
			this.calculatePrecisionRecallCurves();
		
		return this.AUCPrecRec[this.valueToIndex.get(classValue)];
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getPrecisionRecallCurve(java.lang.Object)
	 */
	@Override
	public List<double[]> getPrecisionRecallCurve(String classValue)
			throws IllegalArgumentException {

		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.precRec == null)
			this.calculatePrecisionRecallCurves();
		
		return this.precRec.get(this.valueToIndex.get(classValue));
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSensitivity(java.lang.Object)
	 */
	@Override
	public double getSensitivity(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.recall == null)
			calculateContingencyPerformances();
		
		return this.recall[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSpecificity(java.lang.Object)
	 */
	@Override
	public double getSpecificity(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.FPRate == null)
			calculateContingencyPerformances();
		
		return 1.0 - this.FPRate[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getTruePositiveRate(java.lang.Object)
	 */
	@Override
	public double getTruePositiveRate(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.recall == null)
			calculateContingencyPerformances();
		
		return this.recall[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFalsePositiveRate(java.lang.Object)
	 */
	@Override
	public double getFalsePositiveRate(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.datasetDimension() == 0)
			return Double.NaN;
		
		if( this.FPRate == null)
			calculateContingencyPerformances();
		
		return this.FPRate[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getBrier(int)
	 */
	@Override
	public double getBrier() throws IllegalArgumentException {
		
		if( this.datasetDimension() == 0 || this.dataset.get(0).getProbability(this.dataset.get(0).getClassification()) == null)
			this.brier = Double.NaN;
		
		if( this.brier == -1)
			this.calculateBrier();
		
		return this.brier;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAUC(int)
	 */
	@Override
	public double getROCAUC(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.ROC == null)
			this.calculateROCAndAUC();
		
		return this.AUCROC[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getROC(int)
	 */
	@Override
	public List<double[]> getROC(String classValue) throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.ROC == null)
			this.calculateROCAndAUC();
		
		return this.ROC.get(this.valueToIndex.get(classValue));
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getCumulativeResponse(java.lang.String)
	 */
	@Override
	public List<double[]> getCumulativeResponse(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.cumulativeResp == null)
			this.calculateCumulativeRespAndLiftChart();
		
		return this.cumulativeResp.get(this.valueToIndex.get(classValue));
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getLiftChart(java.lang.String)
	 */
	@Override
	public List<double[]> getLiftChart(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
		
		if( this.liftChart == null)
			this.calculateCumulativeRespAndLiftChart();
		
		return this.liftChart.get(this.valueToIndex.get(classValue));
	}

	
	/**
	 * Calculate all the performance from the 
	 * contingency matrix.
	 */
	protected void calculateContingencyPerformances() {
		
		if( this.dataset == null)
			throw new RuntimeException("Error: impossible sorts trajectories after the final calculation call");
		
		calculateAccuracy();		// accuracy calculation
		calculatePrecision(); 		// precision calculation
		calculateRecall();			// recall calculation
		calculateFMeasure();		// f-measure calculation
		calculateFPRate();			// false positive rate calculation
	}

	/**
	 * Calculate false positive rate
	 */
	protected void calculateFPRate() {
		
		this.FPRate = new double[this.indexToValue.size()];
		for( int i = 0; i < this.contingencyMatrix.length; ++i) {
			this.calculateFPRate(i);
		}
	}
	
	/**
	 * Calculate the false positive rate
	 * for the class in input.
	 * 
	 * @param iClass index of the class for which calculate the false positive rate
	 */
	protected void calculateFPRate(int iClass) {
		
		double n = 0;
		double fp = 0;
		for(int i = 0; i < this.contingencyMatrix.length; ++i)
			for( int j = 0; iClass != i && j < this.contingencyMatrix[0].length; ++j) {
				n += this.contingencyMatrix[i][j];
			}
		for( int i = 0; i < this.contingencyMatrix.length; ++i) {
				if( i != iClass)
					fp += this.contingencyMatrix[i][iClass];
			}
		
		this.FPRate[iClass] = fp/n;		// FP / N
		
	}

	/**
	 * Calculate f-measure
	 */
	protected void calculateFMeasure() {

		this.fmeasure = new double[this.indexToValue.size()];
		for( int i = 0; i < this.indexToValue.size(); ++i)
			this.fmeasure[i] = 2.0 / (1.0/this.precision[i] + 1.0/this.recall[i]);
	}
	
	/**
	 * Calculate recall
	 */
	protected void calculateRecall() {

		this.recall = new double[this.indexToValue.size()];
		for( int i = 0; i < this.contingencyMatrix.length; ++i) {
			double tot = 0;			
			for( int j = 0; j < this.contingencyMatrix[0].length; ++j)
				tot += this.contingencyMatrix[i][j];
			
			this.recall[i] = this.contingencyMatrix[i][i] / tot;
		}
	}

	/**
	 *  Calculate precision
	 */
	protected void calculatePrecision() {

		this.precision = new double[this.indexToValue.size()];
		for( int j = 0; j < this.contingencyMatrix[0].length; ++j) {
			double tot = 0;			
			for( int i = 0; i < this.contingencyMatrix.length; ++i)
				tot += this.contingencyMatrix[i][j];
			
			this.precision[j] = this.contingencyMatrix[j][j] / tot;
		}
	}
	
	/**
	 * Calculate accuracy
	 */
	protected void calculateAccuracy() {
		
		this.accuracy = 0;
		
		// Accuracy calculation
		double tot = 0.0;
		for( int i = 0; i < this.contingencyMatrix.length; ++i) {
			this.accuracy += this.contingencyMatrix[i][i];
			for( int j = 0; j < this.contingencyMatrix.length; ++j)
				tot += this.contingencyMatrix[i][j];
			}

		this.accuracy /= tot;
	}
	

	/**
	 * Calculate brier score for each class.
	 */
	protected void calculateBrier() {

		if( this.dataset == null)
			throw new RuntimeException("Error: impossible calculate Brier after the final calculation call");
		
		this.brier = 0;
		for( int i = 0; i < this.datasetDimension(); ++i) {
			IClassificationResult<TimeType> trj = this.dataset.get(i);
			
			String trueState = trj.getNodeValue(0, trj.getNodeIndexing().getClassIndex());
			String predictedState = trj.getClassification();
			double p = trj.getProbability(predictedState);
			
			double o;
			if( trueState.equals(predictedState))
				o = 1.0;
			else
				o = 0.0;
			
			
			this.brier += Math.pow(p - o, 2);
			
		}
		
		this.brier /= this.datasetDimension();
	}

	/**
	 * Calculate ROC curve, AUC and Brier score.
	 */
	protected void calculateROCAndAUC() {

		this.ROC = new Vector<List<double[]>>(this.indexToValue.size());
		for(int i = 0; i < this.indexToValue.size(); ++i)
			this.ROC.add(null);
		
		this.AUCROC = new double[this.indexToValue.size()];
		for( int i = 0; i < this.indexToValue.size(); ++i)
			calculateROCAndAUC(i);
		
	}
	
	/**
	 * Calculate ROC and AUC for the
	 * class in input.
	 * 
	 * @param index of the class
	 */
	protected void calculateROCAndAUC(int index) {

		if( this.dataset == null)
			throw new RuntimeException("Error: impossible calculate ROC and AUC after the final calculation call");
		
		if( this.datasetDimension() == 0) {
			this.AUCROC[index] = 0.0;
			return;
		}
		
		// Sort the dataset
		final String state = this.indexToValue.get(index);
		Collections.sort( this.dataset, new Comparator<IClassificationResult<TimeType>>() {
												public int compare(IClassificationResult<TimeType> trj1, IClassificationResult<TimeType> trj2) {
													Double p1 = trj1.getProbability(state);
													Double p2 = trj2.getProbability(state);
													if( Double.isNaN(p1))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj1.getName());
													if( Double.isNaN(p2))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj2.getName());
													
													return (int)Math.signum(p2 - p1);
												}
											});
		
		
		// Initialization
		double P = this.classPositiveNumber[index];
		double N = this.datasetDimension() - P;
		
		// Calculate ROC and AUC
		double FP = 0, FPPrev = 0;
		double TP = 0, TPPrev = 0;
		double pPrev = Double.NEGATIVE_INFINITY;
		this.AUCROC[index] = 0.0;
		List<double[]> roc = new LinkedList<double[]>();
		for( int i = 0; i < this.datasetDimension(); ++i) {
			IClassificationResult<TimeType> trj = this.dataset.get(i);
			
			double pCurr = trj.getProbability(state);
			if( pCurr != pPrev) {
				double[] data = new double[2];
				data[0] = FP/N; data[1] = TP/P;
				roc.add(data);
				
				this.AUCROC[index] += trapezoidArea(FP,FPPrev,TP,TPPrev);
				
				pPrev = pCurr;
				FPPrev = FP;
				TPPrev = TP;
			}
			
			if( trj.getNodeValue(0, trj.getNodeIndexing().getClassIndex()).equals(state))
				++TP;
			else
				++FP;
		}
		
		// Last ROC element
		double[] data = new double[2];
		data[0] = FP/N; data[1] = TP/P;
		roc.add(data);
		this.ROC.set(index, roc);
		
		// Last AUC trapezoid area
		this.AUCROC[index] += trapezoidArea(N,FPPrev,P,TPPrev);
		this.AUCROC[index] /= (P*N);			// normalization
		
	}

	
	/**
	 * Calculate the precision-recall
	 * curves for each class.
	 */
	protected void calculatePrecisionRecallCurves() {

		this.precRec = new Vector<List<double[]>>(this.indexToValue.size());
		for(int i = 0; i < this.indexToValue.size(); ++i)
			this.precRec.add(null);
		
		this.AUCPrecRec = new double[this.indexToValue.size()];
		for( int i = 0; i < this.indexToValue.size(); ++i)
			calculatePrecisionRecallCurve(i);
		
	}
	
	/**
	 * Calculate precision recall curve
	 * for a specific class.
	 * 
	 * @param index of the class which calculate precision-recall curve
	 */
	protected void calculatePrecisionRecallCurve(int index) {
		
		if( this.dataset == null)
			throw new RuntimeException("Error: impossible calculate precision-recall curves after the final calculation call");
		
		// Sort the dataset
		final String state = this.indexToValue.get(index);
		Collections.sort( this.dataset, new Comparator<IClassificationResult<TimeType>>() {
												public int compare(IClassificationResult<TimeType> trj1, IClassificationResult<TimeType> trj2) {
													Double p1 = trj1.getProbability(state);
													Double p2 = trj2.getProbability(state);
													if( Double.isNaN(p1))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj1.getName());
													if( Double.isNaN(p2))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj2.getName());
													
													return (int)Math.signum(p2 - p1);
												}
											});
		
		
		// Initialization
		double P = this.classPositiveNumber[index];
		
		// Calculate Precision-recall curve and AUC
		double p1, p2 = -1;
		double FP = 0.0, TP = 0.0;
		double prec = 1.0, precPrev = 1.0;
		double rec = 0.0, recPrev = 0.0;
		this.AUCPrecRec[index] = 0.0;
		List<double[]> pRCurve = new LinkedList<double[]>();
		IClassificationResult<TimeType> trj1 = null, trj2 = null;
		
		// Initialization
		double[] data = new double[2];
		data[0] = rec;	data[1] = prec;
		pRCurve.add(data);
		trj1 = this.dataset.get(0);
		for( int i = 1; i <= this.datasetDimension(); ++i) {
			
			p1 = trj1.getProbability(state);
			// Get trajectory
			if( i < this.datasetDimension()) {
				trj2 = this.dataset.get(i);
				p2 = trj2.getProbability(state);
			} else
				trj2 = null;
			
			// Update counts
			if( trj1.getNodeValue(0, trj1.getNodeIndexing().getClassIndex()).equals(state))
				++TP;
			else
				++FP;
			if( TP+FP == 0)				// precision
				prec = 1.0;
			else
				prec = TP/(TP+FP);
			rec = TP/P;					// recall
			
			if(  trj2 == null || p1 != p2) {
				data = new double[2];
				data[0] = rec;	data[1] = prec;
				pRCurve.add(data);
				this.AUCPrecRec[index] += trapezoidArea(rec,recPrev,prec,precPrev);
				precPrev = prec;
				recPrev = rec;
			}
			trj1 = trj2;
		}
		this.precRec.set(index, pRCurve);
	}

	
	
	/**
	 * Calculate cumulative response and lift chart curves.
	 */
	protected void calculateCumulativeRespAndLiftChart() {

		this.cumulativeResp = new Vector<List<double[]>>(this.indexToValue.size());
		for(int i = 0; i < this.indexToValue.size(); ++i)
			this.cumulativeResp.add(null);
		this.liftChart = new Vector<List<double[]>>(this.indexToValue.size());
		for(int i = 0; i < this.indexToValue.size(); ++i)
			this.liftChart.add(null);
		
		for( int i = 0; i < this.indexToValue.size(); ++i)
			calculateCumulativeRespAndLiftChart(i);
		
	}
	 
	/**
	 * Calculate cumulative response and lift chart curves
	 * for the class in input.
	 * 
	 * @param index of the class
	 */
	protected void calculateCumulativeRespAndLiftChart(int index) {

		if( this.dataset == null)
			throw new RuntimeException("Error: impossible calculate cumulative response and lift chart after the final calculation call");
		
		if( this.datasetDimension() == 0) {
			return;
		}
		
		// Sort the dataset
		final String state = this.indexToValue.get(index);
		Collections.sort( this.dataset, new Comparator<IClassificationResult<TimeType>>() {
												public int compare(IClassificationResult<TimeType> trj1, IClassificationResult<TimeType> trj2) {
													Double p1 = trj1.getProbability(state);
													Double p2 = trj2.getProbability(state);
													if( Double.isNaN(p1))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj1.getName());
													if( Double.isNaN(p2))
														throw new IllegalArgumentException("Error: NaN probability value in trajectory " + trj2.getName());
													
													return (int)Math.signum(p2 - p1);
												}
											});
		
		
		// Initialization
		double P = this.classPositiveNumber[index];
		double positiveRatio = P/this.datasetDimension();
		
		// Calculate ROC and AUC
		double selectedP = 0;
		double pPrev = Double.NEGATIVE_INFINITY;
		List<double[]> cumulativeR = new LinkedList<double[]>();
		List<double[]> liftChart = new LinkedList<double[]>();
		for( int i = 0; i < this.datasetDimension(); ++i) {
			IClassificationResult<TimeType> trj = this.dataset.get(i);
			
			double pCurr = trj.getProbability(state);
			if( pCurr != pPrev) {
				double[] cumulativeData = new double[2];
				cumulativeData[0] = ((double)i)/this.datasetDimension(); cumulativeData[1] = selectedP/P;
				cumulativeR.add(cumulativeData);
				
				double[] liftData = new double[2];
				liftData[0] = ((double)i)/this.datasetDimension(); liftData[1] = (selectedP/i)/positiveRatio;
				liftChart.add(liftData);
				
				pPrev = pCurr;
			}
			
			if( trj.getNodeValue(0, trj.getNodeIndexing().getClassIndex()).equals(state))
				++selectedP;
		}
		
		// Last cumulative response element
		double[] cumulativeData = new double[2];
		cumulativeData[0] = 1.0; cumulativeData[1] = selectedP/P;
		cumulativeR.add(cumulativeData);
		this.cumulativeResp.set(index, cumulativeR);
		// Last Lift chart element
		double[] liftData = new double[2];
		liftData[0] = 1.0; liftData[1] = (selectedP/this.datasetDimension())/positiveRatio;
		liftChart.add(liftData);
		this.liftChart.set(index, liftChart);
		
	}
	
	
	/**
	 * Trapezoid area calculation used
	 * in AUC calculation.
	 * 
	 * @param X1 fist high of the trapezoid
	 * @param X2 second high of the trapezoid
	 * @param Y1 first base of the trapezoid
	 * @param Y2 second base of the trapezoid
	 * @return area of the trapezoid
	 */
	public static double trapezoidArea(double X1, double X2, double Y1, double Y2) {

		return ((Y1+Y2)/2) * Math.abs(X1-X2);
	}


	/**
	 * Calculate the values interval assuming
	 * a binomial distribution.
	 * 
	 * @param val value around which calculate the interval
	 * @param N number of samples
	 * @param z z value used for the confidence interval (i.e. Pr [ −z ≤ X ≤ z ] = c, Pr [ −1.65 ≤ X ≤ 1.65] = 90%)
	 * @return confidence interval in the form (val lower bound, val, val upper bound)
	 */
	public static double[] binomialInterval(double val, int N, double z) {
		
		double[] interval = new double[3];
		double z2 = Math.pow(z, 2);
		double f = val;
		double f2 = Math.pow(val, 2);
		double N2 = Math.pow(N, 2);
		double sqrtValue = z * Math.sqrt(f/N - f2/N + z2/(4*N2));
		
		interval[0] = (f + z2/(2*N) - sqrtValue) / (1 + z2/N);
		interval[1] = f;
		interval[2] = (f + z2/(2*N) + sqrtValue) / (1 + z2/N);
		
		return interval;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.ISingleRunPerformances#calculateFinalResults(boolean)
	 */
	@Override
	public void calculateFinalResults(boolean deleteTrajectories) {

		// Calculates all the performances that requires
		// the trajectories
		this.calculateROCAndAUC();
		this.calculateBrier();
		this.calculatePrecisionRecallCurves();
		this.calculateCumulativeRespAndLiftChart();
		
		if( deleteTrajectories)
			this.dataset = null;
	}

	
}

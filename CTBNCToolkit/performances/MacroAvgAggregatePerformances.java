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
import java.util.TreeMap;
import java.util.Vector;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class used to calculate performance
 * in case of multiple runs (i.e. k-fold
 * cross validation) with macro averaging
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
public class MacroAvgAggregatePerformances<TimeType extends Number & Comparable<TimeType>,PType extends IClassificationSingleRunPerformances<TimeType>> implements
		IClassificationAggregatePerformances<TimeType,PType> {

	private Map<String,Integer> valueToIndex;
	private Map<Integer,String> indexToValue;

	private List<PType> performances;

	private double avgLearningTime;			// average learning time
	private double varLearningTime;			// variance learning time
	private double avgInferenceTime;			// average inference time
	private double varInferenceTime;			// variance of inference time
	
	private double accuracy;					// accuracy
	private double[] precision;				// precision
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
	
	private int datasetDimension;
	private int rocSamples = 100;
	private String rocConfidenceInterval = "90%";
	private int prSamples = 100;
	private String prConfidenceInterval = "90%";
	private int liftSamples = 100;
	private String liftConfidenceInterval = "90%";
	
	protected Map<String,Double> normalTable;
	
	
	/**
	 * Constructor.
	 * 
	 * @param classIndexToValue map index to class value
	 */
	public MacroAvgAggregatePerformances(Map<Integer, String> classIndexToValue) {
		
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
	public MacroAvgAggregatePerformances(Map<Integer, String> classIndexToValue,
			Map<String, Integer> classValueToIndex) {

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
		
		this.performances = new LinkedList<PType>();
		this.datasetDimension = 0;
	}

	/**
	 * Reset all the performances.
	 */
	protected void resetPerformances() {

		this.accuracy = -1.0;
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
		
		this.avgLearningTime = -1.0;
		this.varLearningTime = -1.0;
		this.avgInferenceTime = -1.0;
		this.varInferenceTime = -1.0;
		
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
	 * @see CTBNToolkit.performances.IClassificationPerformances#getAvgInferenceTime()
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
			this.avgInferenceTime /= this.performances.size();
		}
		
		return this.avgInferenceTime;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getVarianceInferenceTime()
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
				this.varInferenceTime /= this.performances.size();
			}
		}
		
		return this.varInferenceTime;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {

		return this.indexToValue.size();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {
		
		return this.indexToValue.get(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#valueToIndex(java.lang.Object)
	 */
	@Override
	public Integer valueToIndex(String value) {

		return this.valueToIndex.get( value);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IAggregatePerformances#getPerformances()
	 */
	@Override
	public Collection<PType> getPerformances() {
		
		return this.performances;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IAggregatePerformances#addPerformances(java.util.Collection)
	 */
	@Override
	public void addPerformances(
			Collection<PType> runsPerformances) {

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

		for( int i = 0; i < this.indexToValue.size(); ++i) {
			if( performance.valueToIndex(this.indexToValue(i)) == null)
				throw new IllegalArgumentException("Error: the performance classes are not coherent with the aggregate performance classes");
		}
		
		this.datasetDimension += performance.datasetDimension();
		this.performances.add(performance);	
		this.resetPerformances();
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {

		return this.datasetDimension;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAccuracy()
	 */
	@Override
	public double getAccuracy() {

		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.accuracy == -1.0) {
			
			this.accuracy = 0.0;
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext())
				this.accuracy += iterPerf.next().getAccuracy();
			
			this.accuracy /= this.performances.size();
		}
		
		return this.accuracy;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getAccuracy(java.lang.String)
	 */
	@Override
	public double[] getAccuracy(String confidenceLevel)
			throws IllegalArgumentException {
		
		return this.getAccuracy(confidenceLevel, this.datasetDimension);
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
		
		Double z = this.normalTable.get(confidenceLevel);
		if( z == null)
			throw new IllegalArgumentException("Error: confidence level didn't found in the table");
		
		if(N == 0) {
			double[] v = new double[3];
			v[0] = v[1] = v[2] = Double.NaN;
			return v;
		}
		
		this.accuracy = this.getAccuracy();
		
		return ClassificationStandardPerformances.binomialInterval(this.accuracy, N, z);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getError()
	 */
	@Override
	public double getError() {

		if( this.performances.size() == 0)
			return Double.NaN;
		
		return 1.0 - this.getAccuracy();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getPrecision(java.lang.Object)
	 */
	@Override
	public double getPrecision(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
	
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.precision == null) {
			
			this.precision = new double[this.indexToValue.size()];
			for(int i = 0; i < this.precision.length; ++i)
				this.precision[i] = 0.0;
			
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				PType perf = iterPerf.next();
				for(int i = 0; i < this.precision.length; ++i)
					this.precision[i] += perf.getPrecision(this.indexToValue(i));
			}
			
			for(int i = 0; i < this.precision.length; ++i)
				this.precision[i] /= this.performances.size();
		}
		
		return this.precision[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getRecall(java.lang.Object)
	 */
	@Override
	public double getRecall(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
	
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.recall == null) {
			
			this.recall = new double[this.indexToValue.size()];
			for(int i = 0; i < this.recall.length; ++i)
				this.recall[i] = 0.0;
			
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				PType perf = iterPerf.next();
				for(int i = 0; i < this.recall.length; ++i)
					this.recall[i] += perf.getRecall(this.indexToValue(i));
			}
			
			for(int i = 0; i < this.recall.length; ++i)
				this.recall[i] /= this.performances.size();
		}
		
		return this.recall[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFMeasure(java.lang.Object)
	 */
	@Override
	public double getFMeasure(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
	
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.fmeasure == null) {
			
			this.fmeasure = new double[this.indexToValue.size()];
			for(int i = 0; i < this.fmeasure.length; ++i)
				this.fmeasure[i] = 0.0;
			
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				PType perf = iterPerf.next();
				for(int i = 0; i < this.fmeasure.length; ++i)
					this.fmeasure[i] += perf.getFMeasure(this.indexToValue(i));
			}
			
			for(int i = 0; i < this.fmeasure.length; ++i)
				this.fmeasure[i] /= this.performances.size();
		}
		
		return this.fmeasure[this.valueToIndex.get(classValue)];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSensitivity(java.lang.Object)
	 */
	@Override
	public double getSensitivity(String classValue)
			throws IllegalArgumentException {
		
		return this.getRecall(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getSpecificity(java.lang.Object)
	 */
	@Override
	public double getSpecificity(String classValue)
			throws IllegalArgumentException {

		if( this.performances.size() == 0)
			return Double.NaN;
		
		return 1.0 - this.getFalsePositiveRate(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getTruePositiveRate(java.lang.Object)
	 */
	@Override
	public double getTruePositiveRate(String classValue)
			throws IllegalArgumentException {

		return this.getRecall(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getFalsePositiveRate(java.lang.Object)
	 */
	@Override
	public double getFalsePositiveRate(String classValue)
			throws IllegalArgumentException {
		
		if( classValue == null)
			throw new IllegalArgumentException("Error: null class value");
		if( this.valueToIndex.get(classValue) == null)
			throw new IllegalArgumentException("Error: state = " + classValue + " didn't found");
	
		if( this.performances.size() == 0)
			return Double.NaN;
		
		if( this.FPRate == null) {
			
			this.FPRate = new double[this.indexToValue.size()];
			for(int i = 0; i < this.FPRate.length; ++i)
				this.FPRate[i] = 0.0;
			
			Iterator<PType> iterPerf = this.performances.iterator();
			while(iterPerf.hasNext()) {
				
				PType perf = iterPerf.next();
				for(int i = 0; i < this.FPRate.length; ++i)
					this.FPRate[i] += perf.getFalsePositiveRate(this.indexToValue(i));
			}
			
			for(int i = 0; i < this.FPRate.length; ++i)
				this.FPRate[i] /= this.performances.size();
		}
		
		return this.FPRate[this.valueToIndex.get(classValue)];
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.validation.IClassificationPerformance#getBrier(int)
	 */
	@Override
	public double getBrier() throws IllegalArgumentException {
		
		if( this.performances.size() == 0 || Double.isNaN( this.performances.get(0).getBrier()))
			return Double.NaN;
		
		if( this.brier == -1)
			this.calculateBrier();
		
		return this.brier;
	}
	
	/**
	 * Calculate brier score for each class.
	 */
	protected void calculateBrier() {

		this.brier = 0.0;
		
		// Sum all the brier values
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext())
			this.brier += iter.next().getBrier();
		
		// Make the average
		this.brier /= this.performances.size();
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
		
		if( this.performances.size() == 0) {
			this.AUCROC[index] = 0.0;
			return;
		}
		
		// Get ROC curves for the current class
		String state = this.indexToValue(index);
		List<List<double[]>> rocs = new Vector<List<double[]>>(this.performances.size() + 1);
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext())
			rocs.add( iter.next().getROC(state));
		
		// Calculate the average curve adding it
		// at the end of the curves list and return
		// the AUC of the average curve
		Double z = this.normalTable.get(this.rocConfidenceInterval);
		if( z == null)
			throw new RuntimeException("Error: confidence level didn't found");
		double auc = calculateVerticalAverageAndAUC(rocs, this.rocSamples, 0.0, 1.0, z);
		
		this.ROC.set(index, rocs.get(rocs.size() - 1));
		this.AUCROC[index] = auc;
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
	
		if( this.performances.size() == 0) {
			this.AUCPrecRec[index] = 0.0;
			return;
		}
		
		// Get precision-recall curves for the current class
		String state = this.indexToValue(index);
		List<List<double[]>> prCurves = new Vector<List<double[]>>(this.performances.size() + 1);
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext())
			prCurves.add( iter.next().getPrecisionRecallCurve(state));
		
		// Calculate the average curve adding it
		// at the end of the curves list and return
		// the AUC of the average curve
		Double z = this.normalTable.get(this.prConfidenceInterval);
		if( z == null)
			throw new RuntimeException("Error: confidence level didn't found");
		double auc = calculateVerticalAverageAndAUC(prCurves, this.prSamples, 0.0, 1.0, z);
		
		this.precRec.set(index, prCurves.get(prCurves.size() - 1));
		this.AUCPrecRec[index] = auc;
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
	 * Calculate cumulative response and lift chart curvers
	 * for the class in input.
	 * 
	 * @param index of the class
	 */
	protected void calculateCumulativeRespAndLiftChart(int index) {
		
		if( this.performances.size() == 0) {
			return;
		}
		
		// Get the curves for the current class
		String state = this.indexToValue(index);
		List<List<double[]>> cumulativeResp = new Vector<List<double[]>>(this.performances.size() + 1);
		List<List<double[]>> liftChart = new Vector<List<double[]>>(this.performances.size() + 1);
		Iterator<PType> iter = this.performances.iterator();
		while( iter.hasNext()) {
			PType perf = iter.next();
			cumulativeResp.add( perf.getCumulativeResponse(state));
			liftChart.add( perf.getLiftChart(state));
		}
		
		// Calculate the average curve adding it
		// at the end
		Double z = this.normalTable.get(this.liftConfidenceInterval);
		if( z == null)
			throw new RuntimeException("Error: confidence level didn't found");
		calculateVerticalAverageAndAUC(cumulativeResp, this.liftSamples, 0.0, 1.0, z);
		calculateVerticalAverageAndAUC(liftChart, this.liftSamples, 0.0, 1.0, z);
		
		this.cumulativeResp.set(index, cumulativeResp.get(cumulativeResp.size() - 1));
		this.liftChart.set(index, liftChart.get(liftChart.size() - 1));
	}
	
	
	/**
	 * Calculate the vertical average curve
	 * from a list of curves defined in [0,1].
	 * Add the calculated curve at the end
	 * of the list and return the AUC of the
	 * averaged curve.
	 * 
	 * Note: each point of the average curve
	 * has the form (x, y, y lower bound, y upper bound) .
	 * The bound are calculated supposing
	 * binomial distribution.
	 * 
	 * Note: it is supposed that the curve
	 * and the confidence interval must be
	 * in [0,1].
	 * 
	 * @param curves list of curves to average (each curve must have at least 2 points) 
	 * @param nSamples number of samples to take in [0,1]
	 * @param minX minimum value of X
	 * @param maxX maximum value of X
	 * @param minY min value of Y (admissible lower bound)
	 * @param maxY max value of Y (admissible upper bound)
	 * @param z z value used for the confidence interval (i.e. Pr [ −z ≤ X ≤ z ] = c, Pr [ −1.65 ≤ X ≤ 1.65] = 90%)
	 * @return auc of the average curve
	 * @throws in case of illegal arguments
	 */
	private double calculateVerticalAverageAndAUC(List<List<double[]>> curves, int nSamples, double minX, double maxX, double z) throws IllegalArgumentException {

		double auc = 0.0;
		List<double[]> avgCurve = new LinkedList<double[]>();
		
		for( int iC = 0; iC < curves.size(); ++iC)
			if( curves.get( iC).size() < 2)
				throw new IllegalArgumentException("Error: the curves must contain at least 2 elements");
		
		// Indexes initialization (one index value for each curve)
		int[] firstIndex = new int[curves.size()];
		int[] secondIndex = new int[curves.size()];
		for(int i = 0; i < firstIndex.length; ++i) {
			firstIndex[i] = 0;						// index for the first value to interpolate
			secondIndex[i] = 0;						// index for the second value to interpolate
		}
		
		// Curve calculation
		double[] oldData = null;
		double deltaX = (maxX - minX) / nSamples; 
		double x = minX;
		for(int i = 0; i <= nSamples; ++i) {
			
			// Data generation
			double[] data = new double[4];
			data[0] = x;
			data[1] = 0.0;
			// For each curve calculate average Y
			for( int iC = 0; iC < curves.size(); ++iC) {
				List<double[]> cv = curves.get(iC);
				
				// Find the first element to interpolate
				for(; firstIndex[iC] < cv.size() && cv.get( firstIndex[iC])[0] <= x; ++firstIndex[iC]);--firstIndex[iC];
				// Find the second element to interpolate
				for( secondIndex[iC] = firstIndex[iC] + 1; secondIndex[iC] < cv.size() && (cv.get( secondIndex[iC])[0] == cv.get( firstIndex[iC] + 1)[0]); ++secondIndex[iC]);--secondIndex[iC];
				
				if( firstIndex[iC] < 0)	{						// if there is no point on the left use the 2 points on the right (usually in leave one out with curves with 2 values)
					firstIndex[iC] = 0; int nextIndex = 1;
					data[1] += interpolate( cv, x, firstIndex[iC], nextIndex, 0.0, 1.0);
				}else if( cv.get( firstIndex[iC])[0] == x)
					data[1] += cv.get( firstIndex[iC])[1];		//! findMaxY(firstIndex[iC], cv);
				else if(firstIndex[iC] != secondIndex[iC] )
					data[1] += interpolate( cv, x, firstIndex[iC], secondIndex[iC], 0.0, 1.0);
				else {	// if there is not a point on the right of X use the 2 points on the left of X to interpolate the Y value at X point
					int previousIndex = firstIndex[iC] - 1;
					for( ; previousIndex >= 0 && cv.get(previousIndex)[0] == cv.get(firstIndex[iC])[0]; --previousIndex);++previousIndex;
					data[1] += interpolate( cv, x, previousIndex, firstIndex[iC], 0.0, 1.0);
				}
			}
			data[1] /= curves.size();

			// Calculate intervals
			double[] interval = ClassificationStandardPerformances.binomialInterval(data[1], this.datasetDimension, z);
			data[2] = interval[0];
			data[3] = interval[2];
			// Add data
			avgCurve.add(data);
			
			// Calculate AUC
			if( i != 0)
				auc += ClassificationStandardPerformances.trapezoidArea(data[0],oldData[0],data[1],oldData[1]);
			
			// Next sample
			x += deltaX;
			if( x > maxX)							// in case of approximation error to be sure that the last sample is maxX
				x = maxX;
			oldData = data; 
		}
		
		curves.add(avgCurve);
		return auc;
	}
	

	/**
	 * Interpolate the value of Y at
	 * point X given the value of Y1
	 * and Y2 in point X1 and X2 using
	 * a straight line.
	 * 
	 * Note: if the result exceed minY
	 * it will be cut with the bound minY,
	 * if the result exceed maxY it will
	 * be cut with the bound maxY.
	 * 
	 * @param curve to take the points
	 * @param x X value where get the Y value
	 * @param index1 index of the first point to use in the interpolation
	 * @param index2 index of the second point to use in the interpolation
	 * @param minY min value of Y (admissible lower bound)
	 * @param maxY max value of Y (admissible upper bound)
	 * @return return the interpolation of the Y value in X
	 */
	private double interpolate(List<double[]> curve, double x, int index1, int index2, double minY, double maxY) {
		
		double y1 = curve.get(index1)[1];	//!findMaxY(index1, curve);
		double y2 = curve.get(index2)[1];	//!findMaxY(index2, curve);
		double slope = (y2 - y1) / (curve.get(index2)[0] - curve.get(index1)[0]);
		double y = y1 + slope*(x - curve.get(index1)[0]);
		
		if( y < minY)
			return minY;
		if( y > maxY)
			return maxY;
		return y;
	}

	//! Function not more used!!!
	/*
	 * With the actual version for each X
	 * it is taken the last value. In this
	 * case it is show a pessimistic
	 * behavior.
	 * With the findMax version for each X
	 * it is taken the maximum Y. In this
	 * case it is show a possibly very
	 * optimistic behavior especially
	 * in case of small datasets.
	 */
	/**
	 * Find the max Y value for the X
	 * value indicated by index1.
	 * 
	 * @param index1 index of the X value where localize the research
	 * @param curve curve where serach
	 * @return max Y value in the curve for a given X
	 *//*
	private double findMaxY(int index, List<double[]> curve) {

		double x = curve.get(index)[0];
		double max = curve.get(index)[1];
		
		for(int i = index - 1; i >= 0 && curve.get(i)[0] == x; --i)
			if( curve.get(i)[1] > max)
				max = curve.get(i)[1];
		
		for(int i = index + 1; i < curve.size() && curve.get(i)[0] == x; ++i)
			if( curve.get(i)[1] > max)
				max = curve.get(i)[1];
		
		return max;
	}*/
	
	
	/**
	 * Set the number of sampling to use
	 * in the vertical sampling of the
	 * ROC curve generation (zero sample
	 * is added by default). 
	 * 
	 * @param samplesNumber number of sample to generate the ROC curve.
	 */
	public void setROCSamplesNumber(int samplesNumber) throws IllegalArgumentException {
		
		if( samplesNumber < 1)
			throw new IllegalArgumentException("Error: it is required at least one sample");
		
		this.rocSamples = samplesNumber;
		
	}
	
	/**
	 * Set the confidence interval
	 * for the ROC aggregate curve.
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"}
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setROCConfidenceInterval(String confidenceInterval) throws IllegalArgumentException {
	
		if( confidenceInterval == null)
			throw new IllegalArgumentException("Error: null argument");
		if( this.normalTable.get(confidenceInterval) == null)
			throw new IllegalArgumentException("Error: confidence level didn't recognized");
		
		this.rocConfidenceInterval = confidenceInterval;
	}
	
	/**
	 * Set the number of sampling to use
	 * in the vertical sampling of the
	 * cumulative response and lift chart
	 * curves generation (zero sample
	 * is added by default). 
	 * 
	 * @param samplesNumber number of sample to generate the ROC curve.
	 */
	public void setLiftSamplesNumber(int samplesNumber) throws IllegalArgumentException {
		
		if( samplesNumber < 1)
			throw new IllegalArgumentException("Error: it is required at least one sample");
		
		this.liftSamples = samplesNumber;
		
	}
	
	/**
	 * Set the confidence interval
	 * for the cumulative response
	 * and lift chart aggregate curves.
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"}
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setLiftConfidenceInterval(String confidenceInterval) throws IllegalArgumentException {
	
		if( confidenceInterval == null)
			throw new IllegalArgumentException("Error: null argument");
		if( this.normalTable.get(confidenceInterval) == null)
			throw new IllegalArgumentException("Error: confidence level didn't recognized");
		
		this.liftConfidenceInterval = confidenceInterval;
	}
	
	/**
	 * Set the number of sampling to use
	 * in the vertical sampling of the
	 * precision-recall curve generation
	 * (zero sample is added by default). 
	 * 
	 * @param samplesNumber number of sample to generate the precision-recall curve.
	 */
	public void setPrecisionRecallSamplesNumber(int samplesNumber) throws IllegalArgumentException {
		
		if( samplesNumber < 1)
			throw new IllegalArgumentException("Error: it is required at least one sample");
		
		this.prSamples = samplesNumber;
		
	}
	
	/**
	 * Set the confidence interval for the
	 * precision-recall aggregate curve.
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"} [default "90%"]
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setPrecisionRecallConfidenceInterval(String confidenceInterval) throws IllegalArgumentException {
	
		if( confidenceInterval == null)
			throw new IllegalArgumentException("Error: null argument");
		if( this.normalTable.get(confidenceInterval) == null)
			throw new IllegalArgumentException("Error: confidence level didn't recognized");
		
		this.prConfidenceInterval = confidenceInterval;
	}
}

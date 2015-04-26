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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * A performances class that work as
 * a container to calculate both micro
 * and macro averaging performances.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <PType> performance type
 */
public class MicroMacroClassificationPerformances<TimeType extends Number & Comparable<TimeType>,PType extends IClassificationSingleRunPerformances<TimeType>> implements
	IClassificationAggregatePerformances<TimeType,PType> {
	
	private MicroAvgAggregatePerformances<TimeType,PType> microAvg;
	private MacroAvgAggregatePerformances<TimeType,PType> macroAvg;
	
	boolean microFlag;
	
	/**
	 * Constructor.
	 * MicroFlag is set to true. All methods
	 * call will be turned on micro averaging
	 * performances.
	 * 
	 * @param classIndexToValue map index to class value
	 */
	public MicroMacroClassificationPerformances(Map<Integer,String> classIndexToValue) {
		
		Map<String,Integer> valueToIndex = new TreeMap<String,Integer>();
		
		for( int i = 0; i < classIndexToValue.size(); ++i) {
			String val = classIndexToValue.get(i);
			if( val == null)
				throw new IllegalArgumentException("Error: key " + i + "doesn't included in the map index to value");
			
			valueToIndex.put(val, i);
		}
		
		this.microAvg = new MicroAvgAggregatePerformances<TimeType,PType>(classIndexToValue, valueToIndex);
		this.macroAvg = new MacroAvgAggregatePerformances<TimeType,PType>(classIndexToValue, valueToIndex);
		this.microFlag = true;
	}
			
	/**
	 * Constructor.
	 * MicroFlag is set to true. All methods
	 * call will be turned on micro averaging
	 * performances.
	 * 
	 * @param classIndexToValue map index to class value
	 * @param classValueToIndex map class value to index
	 */
	public MicroMacroClassificationPerformances(Map<Integer, String> classIndexToValue, 
				Map<String, Integer> classValueToIndex) {
		
		this.microAvg = new MicroAvgAggregatePerformances<TimeType,PType>(classIndexToValue, classValueToIndex);
		this.macroAvg = new MacroAvgAggregatePerformances<TimeType,PType>(classIndexToValue, classValueToIndex);
		this.microFlag = true;
	}
	
	
	/**
	 * Return micro averaging performances.
	 * 
	 * @return micro averaging performances.
	 */
	public 	MicroAvgAggregatePerformances<TimeType,PType> getMicroAveraging() {
		
		return this.microAvg;
	}
	
	/**
	 * Return macro averaging performances.
	 * 
	 * @return macro averaging performances
	 */
	public MacroAvgAggregatePerformances<TimeType,PType> getMacroAveraging() {
		
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
	 * @see CTBNToolkit.performances.IClassificationPerformances#datasetDimension()
	 */
	@Override
	public int datasetDimension() {

		if( this.microFlag)
			return this.microAvg.datasetDimension();
		else
			return this.macroAvg.datasetDimension();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#classesNumber()
	 */
	@Override
	public int classesNumber() {
		
		if( this.microFlag)
			return this.microAvg.classesNumber();
		else
			return this.macroAvg.classesNumber();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#indexToValue(int)
	 */
	@Override
	public String indexToValue(int index) {
		
		if( this.microFlag)
			return this.microAvg.indexToValue(index);
		else
			return this.macroAvg.indexToValue(index);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#valueToIndex(java.lang.Object)
	 */
	@Override
	public Integer valueToIndex(String value) {
		
		if( this.microFlag)
			return this.microAvg.valueToIndex(value);
		else
			return this.macroAvg.valueToIndex(value);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getLearningTime()
	 */
	@Override
	public double getLearningTime() {
		
		if( this.microFlag)
			return this.microAvg.getLearningTime();
		else
			return this.macroAvg.getLearningTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getAvgInferenceTime()
	 */
	@Override
	public double getAvgInferenceTime() {
		
		if( this.microFlag)
			return this.microAvg.getAvgInferenceTime();
		else
			return this.macroAvg.getAvgInferenceTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getVarianceInferenceTime()
	 */
	@Override
	public double getVarianceInferenceTime() {
		
		if( this.microFlag)
			return this.microAvg.getVarianceInferenceTime();
		else
			return this.macroAvg.getVarianceInferenceTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getAccuracy()
	 */
	@Override
	public double getAccuracy() {
		
		if( this.microFlag)
			return this.microAvg.getAccuracy();
		else
			return this.macroAvg.getAccuracy();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getAccuracy(java.lang.String)
	 */
	@Override
	public double[] getAccuracy(String confidenceLevel)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getAccuracy(confidenceLevel);
		else
			return this.macroAvg.getAccuracy(confidenceLevel);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getError()
	 */
	@Override
	public double getError() {
		
		if( this.microFlag)
			return this.microAvg.getError();
		else
			return this.macroAvg.getError();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getPrecision(java.lang.Object)
	 */
	@Override
	public double getPrecision(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getPrecision(classValue);
		else
			return this.macroAvg.getPrecision(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getRecall(java.lang.Object)
	 */
	@Override
	public double getRecall(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getRecall(classValue);
		else
			return this.macroAvg.getRecall(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getFMeasure(java.lang.Object)
	 */
	@Override
	public double getFMeasure(String classValue)
			throws IllegalArgumentException {
		

		if( this.microFlag)
			return this.microAvg.getFMeasure(classValue);
		else
			return this.macroAvg.getFMeasure(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getPrecisionRecallAUC(java.lang.Object)
	 */
	@Override
	public double getPrecisionRecallAUC(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getPrecisionRecallAUC(classValue);
		else
			return this.macroAvg.getPrecisionRecallAUC(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getPrecisionRecallCurve(java.lang.Object)
	 */
	@Override
	public List<double[]> getPrecisionRecallCurve(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getPrecisionRecallCurve(classValue);
		else
			return this.macroAvg.getPrecisionRecallCurve(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getSensitivity(java.lang.Object)
	 */
	@Override
	public double getSensitivity(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getSensitivity(classValue);
		else
			return this.macroAvg.getSensitivity(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getSpecificity(java.lang.Object)
	 */
	@Override
	public double getSpecificity(String classValue)
			throws IllegalArgumentException {

		if( this.microFlag)
			return this.microAvg.getSpecificity(classValue);
		else
			return this.macroAvg.getSpecificity(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getTruePositiveRate(java.lang.Object)
	 */
	@Override
	public double getTruePositiveRate(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getTruePositiveRate(classValue);
		else
			return this.macroAvg.getTruePositiveRate(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getFalsePositiveRate(java.lang.Object)
	 */
	@Override
	public double getFalsePositiveRate(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getFalsePositiveRate(classValue);
		else
			return this.macroAvg.getFalsePositiveRate(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getBrier()
	 */
	@Override
	public double getBrier() {
		
		if( this.microFlag)
			return this.microAvg.getBrier();
		else
			return this.macroAvg.getBrier();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getROCAUC(java.lang.Object)
	 */
	@Override
	public double getROCAUC(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getROCAUC(classValue);
		else
			return this.macroAvg.getROCAUC(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getROC(java.lang.Object)
	 */
	@Override
	public List<double[]> getROC(String classValue)
			throws IllegalArgumentException {
		
		if( this.microFlag)
			return this.microAvg.getROC(classValue);
		else
			return this.macroAvg.getROC(classValue);
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getCumulativeResponse(java.lang.String)
	 */
	@Override
	public List<double[]> getCumulativeResponse(String classValue)
			throws IllegalArgumentException {

		if( this.microFlag)
			return this.microAvg.getCumulativeResponse(classValue);
		else
			return this.macroAvg.getCumulativeResponse(classValue);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IClassificationPerformances#getLiftChart(java.lang.String)
	 */
	@Override
	public List<double[]> getLiftChart(String classValue)
			throws IllegalArgumentException {

		if( this.microFlag)
			return this.microAvg.getLiftChart(classValue);
		else
			return this.macroAvg.getLiftChart(classValue);
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
	public void addPerformances(
			Collection<PType> runsPerformances) {

		this.microAvg.addPerformances(runsPerformances);
		this.macroAvg.addPerformances(runsPerformances);		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.performances.IAggregatePerformances#addPerformances(CTBNToolkit.performances.ISingleRunPerformances)
	 */
	@Override
	public void addPerformances(
			PType performance) {

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

	/**
	 * Set the number of sampling to use
	 * in the vertical sampling of the
	 * ROC curve generation (zero sample
	 * is added by default). 
	 * 
	 * @param samplesNumber number of sample to generate the ROC curve.
	 */
	public void setMacroROCSamplesNumber(int samplesNumber) throws IllegalArgumentException {
		
		this.macroAvg.setROCSamplesNumber(samplesNumber);
		
	}
	
	/**
	 * Set the confidence interval
	 * for the ROC aggregate curve.
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"}
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setMacroROCConfidenceInterval(String confidenceInterval) throws IllegalArgumentException {
	
		this.macroAvg.setROCConfidenceInterval(confidenceInterval);
	}
	
	/**
	 * Set the number of sampling to use
	 * in the vertical sampling of the
	 * precision-recall curve generation
	 * (zero sample is added by default). 
	 * 
	 * @param samplesNumber number of sample to generate the precision-recall curve.
	 */
	public void setMacroPrecisionRecallSamplesNumber(int samplesNumber) throws IllegalArgumentException {
		
		this.macroAvg.setPrecisionRecallSamplesNumber(samplesNumber);
	}
	
	/**
	 * Set the confidence interval for the
	 * precision-recall aggregate curve.
	 * 
	 * @param confidenceLevel level of confidence (range of values {"99.9%", "99.8%", "99%", "98%", "95%", "90%", "80%"} [default "90%"]
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public void setMacroPrecisionRecallConfidenceInterval(String confidenceInterval) throws IllegalArgumentException {
	
		this.macroAvg.setPrecisionRecallConfidenceInterval(confidenceInterval);
	}
	
}

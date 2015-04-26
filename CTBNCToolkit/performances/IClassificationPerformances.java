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

import java.util.List;



/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Base interface for classification
 * performances.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 */
public interface IClassificationPerformances<TimeType extends Number> extends IPerformances {
	
	/**
	 * Return the accuracy of the test.
	 * 
	 * @return accuracy of the test
	 */
	public double getAccuracy();
	
	/**
	 * Return the accuracy and in confidence
	 * interval in the following form:
	 * lower bound <= accuracy <= upper bound
	 * 
	 * @param confidenceLevel level of confidence
	 * @return accuracy confidence interval (lower bound, accuracy, upper bound)
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double[] getAccuracy(String confidenceLevel) throws IllegalArgumentException;
	
	/**
	 * Return the accuracy of the test.
	 * 
	 * @return accuracy of the test
	 */
	public double getError();
	
	/**
	 * Return the precision for a specific
	 * class.
	 * 
	 * @param classValue class for which return the precision
	 * @return precision of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getPrecision(String classValue) throws IllegalArgumentException;

	/**
	 * Return the recall (sensitivity or true
	 * positive rate) for a specific class.
	 * 
	 * @param classValue class for which return the recall
	 * @return recall of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getRecall(String classValue) throws IllegalArgumentException;

	/**
	 * Return the f-measure for a specific
	 * class.
	 * 
	 * @param classValue class for which return the f-measure
	 * @return f-measure of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getFMeasure(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the AUC of the precision recall
	 * curve for a specific class.
	 * 
	 * @param classValue class for which return the AUC
	 * @return precision-recall AUC of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getPrecisionRecallAUC(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the points sampled from the
	 * precision-recall curve for a specific
	 * class.
	 * Each element is a couple of (R,P)
	 * values.
	 * 
	 * @param classValue class for which return the ROC
	 * @return precision-recall curve of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public List<double[]> getPrecisionRecallCurve(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the sensitivity (recall or true
	 * positive rate) for a specific class.
	 * 
	 * @param classValue class for which return the sensitivity
	 * @return sensitivity of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getSensitivity(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the specificity for a specific
	 * class.
	 * 
	 * @param classValue class for which return the specificity
	 * @return specificity of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getSpecificity(String classValue) throws IllegalArgumentException; 
	
	/**
	 * Return the true positive rate (recall
	 * or sensitivity) for a specific class.
	 * 
	 * @param classValue class for which return the true positive rate
	 * @return true positive rate of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getTruePositiveRate(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the false positive rate for
	 * a specific class.
	 * 
	 * @param classValue class for which return the false positive rate
	 * @return false positive rate of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getFalsePositiveRate(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the brier scoring.
	 * 
	 * @return brier score
	 */
	public double getBrier();

	/**
	 * Return the AUC for a specific
	 * class.
	 * 
	 * @param classValue class for which return the AUC
	 * @return AUC of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public double getROCAUC(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the points sampled from the ROC function
	 * for a specific class.
	 * Each element is a couple (X,Y) values.
	 * 
	 * @param classValue class for which return the ROC
	 * @return ROC of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public List<double[]> getROC(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the points of the cumulative response curve.
	 * Each element is a couple (X,Y) values.
	 * 
	 * @param classValue class for which return the curve
	 * @return cumulative response curve for the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public List<double[]> getCumulativeResponse(String classValue) throws IllegalArgumentException;
	
	/**
	 * Return the points of the lift chart.
	 * Each element is a couple (X,Y) values.
	 * 
	 * @param classValue class for which return the curve
	 * @return lift chart of the selected class
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public List<double[]> getLiftChart(String classValue) throws IllegalArgumentException;
}

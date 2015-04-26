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


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface to use in case of generic performances.
 */
public interface IPerformances {

	/**
	 * Return the dimension of the used
	 * dataset.
	 * 
	 * @return dimension of the dataset
	 */
	public int datasetDimension();
	
	/**
	 * Return the number of classes.
	 * 
	 * @return number of classes
	 */
	public int classesNumber();
	
	/**
	 * Return the value type from the index.
	 * This index is the same used in the
	 * contingency matrix.
	 * Null if the index is not found.
	 * 
	 * @param index index which the value is given back
	 * @return value related to the index in input
	 */
	public String indexToValue(int index);
	
	/**
	 * Return the index from the value type.
	 * This index is the same used in the
	 * contingency matrix.
	 * Null if the value is not found.
	 * 
	 * @param value value which the index is given back
	 * @return index related to the value in input
	 */
	public Integer valueToIndex(String value);
	
	/**
	 * Return the model learning
	 * time.
	 * Return NaN if times are not
	 * set.
	 * 
	 * @return model leraning time
	 */
	public double getLearningTime();
	
	/**
	 * Return the average inference
	 * time.
	 * Return NaN if times are not
	 * set.
	 * 
	 * @return average inference time
	 */
	public double getAvgInferenceTime();
	
	/**
	 * Return the variance of the
	 * inference time.
	 * Return NaN if times are not
	 * set.
	 * 
	 * @return variance of the inference time
	 */
	public double getVarianceInferenceTime();
	
}

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
package CTBNCToolkit.optimization;

import java.util.Map;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define an optimization
 * algorithm.
 *
 * @param <T> type of the elements managed by the optimization algorithm
 */
public interface IOptimizationAlgorithm<T extends IOptimizationElement> {

	/**
	 * Set the algorithm parameters.
	 * 
	 * @param params maps of parameters represented as map of "parameter name" and "value"
	 * @throws IllegalArgumentException if there is some error with the parameters
	 */
	public void setParameters(Map<String,Object> params) throws IllegalArgumentException;
	
	/**
	 * Return the help description for the algorithm parameters.
	 * 
	 * @return parameters help
	 */
	public String helpParameters();
	
	/**
	 * Search the optimal solution.
	 * 
	 * @return return the best solution found
	 * @throws RuntimeException in case of some error during the execution
	 */
	public T optimize() throws RuntimeException;
	
}

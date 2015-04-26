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

import java.util.Collection;

import CTBNCToolkit.CTDiscreteNode;
import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.ILearningAlgorithm;
import CTBNCToolkit.ITrajectory;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define a factory to generate individual.
 *
 * @param <T> type of optimization element generated
 */
public interface IElementFactory <T extends IOptimizationElement>{

	/**
	 * Generate a new instance of an optimization
	 * element.
	 * 
	 * @param model model used as starting individual
	 * @param nodeIndex index of the node who learn locally the structure
	 * @return new instance of an optimization element
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public T newInstance(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws IllegalArgumentException;
	
	/**
	 * Calculate the scoring for the model and
	 * the current node indicated by name.
	 * 
	 * @param model model for which calculate the performance
	 * @param nodeIndex index of the node that we are evaluating
	 * @return scoring value
	 * @throws RuntimeException in case of errors
	 */
	public double evaluate(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws RuntimeException;
	
	/**
	 * Set the dataset to use in the evaluation.
	 * 
	 * @param dataset dataset to use to calculate the scoring function
	 * @throws IllegalArgumentException if the argument is not correct
	 */
	public void setDataset(Collection<ITrajectory<Double>> dataset) throws IllegalArgumentException;

	/**
	 * Return the set dataset.
	 * 
	 * @return the dataset if set null otherwise
	 */
	public Collection<ITrajectory<Double>> getDataset();
	
	/**
	 * Return the parameter learning algorithm.
	 * 
	 * @return parameter learning algorithm to use
	 */
	public 	ILearningAlgorithm<Double, CTDiscreteNode> getParamsLearningAlg();
}

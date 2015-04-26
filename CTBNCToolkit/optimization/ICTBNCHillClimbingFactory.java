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

import CTBNCToolkit.*;


/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Interface used to define the structure of the
 * factory for the CTNCHillClimbingIndividual.
 * 
 */
public interface ICTBNCHillClimbingFactory extends IElementFactory<CTBNCHillClimbingIndividual> {

	/**
	 * Return the maximum number of parents
	 * allowed.
	 * 
	 * @return maximum number of allowed parents
	 */
	public int getMaxParents();

	/**
	 * Return the flag used to decide the execution
	 * mode.
	 * False to leave the class childhood relations
	 * as defined in the model.
	 * True to study the class childhood relations
	 * in order to select the best attributes
	 * for the class.
	 * 
	 * @return false to leave the class childhood relations as defined in the model. True to study the class childhood relations in order to select the best attributes	 for the class.
	 */
	public boolean getFeatureSelectionMode();
	
	/**
	 * Generate a new instance of an optimization
	 * element.
	 * 
	 * @param model model used as starting individual
	 * @param nodeIndex index of the node who learn locally the structure
	 * @param score pre-calculated score to assign to the new element
	 * @return new instance of an optimization element
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public CTBNCHillClimbingIndividual newInstance(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex, double score);
	
}

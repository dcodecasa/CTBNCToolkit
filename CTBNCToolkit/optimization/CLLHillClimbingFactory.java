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
import CTBNCToolkit.StructuralLearningScoringFormulae;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Factory to generate CTBNCHillClimbingIndividual and
 * evaluate them using Conditional Log-Likelihood (CLL).
 */
public class CLLHillClimbingFactory implements ICTBNCHillClimbingFactory {

	private ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg;
	private Collection<ITrajectory<Double>> dataset;
	private int maxParents;
	private boolean dimensionPenalty;
	private boolean featureSelectionMode;
	private int expApprox;
	 
	/**
	 * Base constructor.
	 * 
	 * @param paramsLearningAlg learning algorithm used to learn the parameters during the structural optimization process
	 * @param maxParents maximum number of parents allowed class included
	 * @param dimensionPenalty true if the scoring of the individual has to give a penalty related to the dimension of the structure, false otherwise
	 * @param featureSelectionMode false to leave the class childhood relations as defined in the model. True to study the class childhood relations in order to select the best attributes	 for the class
	 */
	public CLLHillClimbingFactory(ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg, int maxParents, boolean dimensionPenalty, boolean featureSelectionMode) {
	
		this.paramsLearningAlg = paramsLearningAlg;
		this.maxParents = maxParents;
		this.dimensionPenalty = dimensionPenalty;
		this.featureSelectionMode = featureSelectionMode;
		this.dataset = null;
		this.expApprox = 10;
	}
	
	
	@Override
	public CTBNCHillClimbingIndividual newInstance(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws IllegalArgumentException {

		return new CTBNCHillClimbingIndividual(this, model, nodeIndex);
	}

	@Override
	public CTBNCHillClimbingIndividual newInstance(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex, double score) throws IllegalArgumentException {
		
		return new CTBNCHillClimbingIndividual(this, model, nodeIndex, score);
	}
	
	/**
	 * Evaluate the model using the Conditional
	 * Log-Likelihood.
	 * The model must be a CTBNClassifier.
	 * 
	 * @param model the model to evaluate (CTBNClassifier instance)
	 * @param nodeIndex name of the node in evaluation
	 * @return scoring
	 */
	@Override
	public double evaluate(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws RuntimeException {
		
		return StructuralLearningScoringFormulae.conditionalLogLikelihoodScore(model, nodeIndex, this.dataset, this.paramsLearningAlg, this.dimensionPenalty, this.expApprox);
	}
	
	
	@Override
	public void setDataset(Collection<ITrajectory<Double>> dataset) throws IllegalArgumentException {

		if( dataset == null)
			throw new IllegalArgumentException("Error: null argument. A dataset is not optional");
		
		this.dataset = dataset;		
	}

	@Override
	public Collection<ITrajectory<Double>> getDataset() {
		
		return this.dataset;
	}
	
	@Override
	public int getMaxParents() {
		
		return this.maxParents;
	}
	
	@Override
	public boolean getFeatureSelectionMode() {
		
		return this.featureSelectionMode;
	}
	
	/**
	 * Return the flag used to decide if gives
	 * a penalty to the dimension of the structure
	 * during the learning process.
	 * 
	 * @return  true if the scoring of the individual has to give a penalty related to the dimension of the structure, false otherwise
	 */
	public boolean getDimensionPenalty() {
		
		return this.dimensionPenalty;
	}

	/**
	 * Set the number of iteration to approximate
	 * the exponential in the scoring calculation.
	 * e^x = sum_n(x^n/n!)
	 * [Default value = 10]
	 * 
	 * @param N number of iteration used to approximate the exponential
	 * @throws IllegalArgumentException if N is less then 3
	 */
	public void setExpApproximation(int N) throws IllegalArgumentException {
		
		if( N < 3)
			throw new IllegalArgumentException("Error: the exponential approximation value must be at least 3");
		
		this.expApprox = N;
	}
	
	/**
	 * Get the number of iteration used to approximate
	 * the exponential in the scoring calculation.
	 * e^x = sum_n(x^n/n!)
	 * 
	 * @return number of iteration used to approximate the exponential
	 */
	public int getExpApproximation() {
		
		return this.expApprox;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.IElementFactory#getParamsLearningAlg()
	 */
	@Override
	public ILearningAlgorithm<Double, CTDiscreteNode> getParamsLearningAlg() {

		return this.paramsLearningAlg;
	}

}

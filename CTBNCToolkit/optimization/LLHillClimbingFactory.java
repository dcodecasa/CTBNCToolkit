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
 * evaluate them using Log-Likelihood (LL).
 */
public class LLHillClimbingFactory implements ICTBNCHillClimbingFactory{

	private ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg;
	private Collection<ITrajectory<Double>> dataset;
	private int maxParents;
	private boolean dimensionPenalty;
	private boolean featureSelectionMode;
	 
	/**
	 * Base constructor.
	 * 
	 * @param paramsLearningAlg learning algorithm used to learn the parameters during the structural optimization process
	 * @param maxParents maximum number of parents allowed
	 * @param dimensionPenalty true if the scoring of the individual has to give a penalty related to the dimension of the structure, false otherwise
	 * @param featureSelectionMode false to leave the class childhood relations as defined in the model. True to study the class childhood relations in order to select the best attributes	 for the class
	 */
	public LLHillClimbingFactory(ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg, int maxParents, boolean dimensionPenalty, boolean featureSelectionMode) {
	
		this.paramsLearningAlg = paramsLearningAlg;
		this.maxParents = maxParents;
		this.dimensionPenalty = dimensionPenalty;
		this.featureSelectionMode = featureSelectionMode;
		this.dataset = null;
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
	 * Evaluate the model using the Nodelman
	 * formulae for the current node over the
	 * dataset inserted in the constructor.
	 * 
	 * @param model the model to evaluate
	 * @param nodeIndex index of the node in evaluation
	 * @return scoring
	 */
	@Override
	public double evaluate(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws RuntimeException {
		
		return StructuralLearningScoringFormulae.logLikelihoodScore(model, nodeIndex, this.dataset, this.paramsLearningAlg, this.dimensionPenalty);
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


	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.IElementFactory#getParamsLearningAlg()
	 */
	@Override
	public ILearningAlgorithm<Double, CTDiscreteNode> getParamsLearningAlg() {

		return this.paramsLearningAlg;
	}

}

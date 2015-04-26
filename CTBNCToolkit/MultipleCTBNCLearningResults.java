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
package CTBNCToolkit;

import java.util.List;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that implements the learning results over
 * MultipleCTBNC models.
 * The sufficient statistics depend on the model
 * expert set with a set method.
 */
public class MultipleCTBNCLearningResults implements ILearningResults {

	private int currentModel;
	private List<ILearningResults> learningResults;
	
	
	/**
	 * Base constructor.
	 * 
	 * @param learningResults list of result for each model in the MultipleCTBNC
	 */
	public MultipleCTBNCLearningResults(List<ILearningResults> learningResults) {
		
		this.currentModel = 0;
		this.learningResults = learningResults;
	}
	
	
	/**
	 * Return the number of model (it is equal
	 * to the number of classes).
	 * 
	 * @return number of models
	 */
	public int getNumberOfModel() {
		
		return this.learningResults.size();
	}
	
	
	/**
	 * Set the current model.
	 * 
	 * @param classIndex class index for which the model is an expert
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public void setCurrentModel(int classIndex) throws IllegalArgumentException {
		
		if( classIndex < 0 || classIndex >= this.learningResults.size())
			throw new IllegalArgumentException("Error: a model is defined for each class, so the range of value is {0,..., #classes}");
		
		this.currentModel = classIndex;
	}
	
	
	/**
	 * Get the index of the current model.
	 * 
	 * @return index of the current model
	 */
	public int getCurrentModelIndex() {
		
		return this.currentModel;
	}
	
	
	/**
	 * Return the result of a model given its
	 * index.
	 * 
	 * @param classIndex class index for which the model is an expert
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public ILearningResults getResult(int classIndex) throws IllegalArgumentException {
		
		if( classIndex < 0 || classIndex >= this.learningResults.size())
			throw new IllegalArgumentException("Error: a model is defined for each class, so the range of value is {0,..., #classes}");
		
		return this.learningResults.get( classIndex);
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningResults#getSufficientStatistics()
	 */
	@Override
	public SufficientStatistics[] getSufficientStatistics() {
		
		return this.learningResults.get( this.currentModel).getSufficientStatistics();
	}

}

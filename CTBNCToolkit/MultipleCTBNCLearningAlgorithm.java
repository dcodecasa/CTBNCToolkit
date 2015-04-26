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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Learning algorithm for MultipleCTBNC models.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> node type
 */
public class MultipleCTBNCLearningAlgorithm<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> extends
		LearningAlgorithm<TimeType, NodeType> implements
		ILearningAlgorithm<TimeType, NodeType> {
	
	private boolean[][] adjMStructure;
	private ILearningAlgorithm<TimeType, NodeType> learningAlgorithm;
	
	
	/**
	 * Base constructor.
	 * 
	 * @param learningAlgorithm algorithm to use to learn all the model of the MultipleCTBNC
	 */
	public MultipleCTBNCLearningAlgorithm(ILearningAlgorithm<TimeType, NodeType> learningAlgorithm) {
		
		if( learningAlgorithm == null)
			throw new IllegalArgumentException("Error: null argument. A learning algorithm is required to learn the CTBNC models");
		
		this.learningAlgorithm = learningAlgorithm;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#setDefaultParameters()
	 */
	@Override
	public void setDefaultParameters() { }

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#setStructure(boolean[][])
	 */
	@Override
	public void setStructure(boolean[][] adjMatrix)
			throws IllegalArgumentException {

		this.adjMStructure = adjMatrix;		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#getStructure()
	 */
	@Override
	public boolean[][] getStructure() {
		
		return this.adjMStructure;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#helpParameters()
	 */
	@Override
	public String helpParameters() {
		
		String helpStr = "";
		helpStr += "Class MultipleCTBNCLearningAlgorithm does not require parameters\n";
		
		return helpStr;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#learn(CTBNToolkit.ICTClassifier, java.util.Collection)
	 */
	@Override
	public ILearningResults learn(ICTClassifier<TimeType, NodeType> model,
			Collection<ITrajectory<TimeType>> trainingSet)
			throws RuntimeException {

		if( !MultipleCTBNC.class.isAssignableFrom( model.getClass()))
			throw new RuntimeException("Error: MultipleCTBNCLearningAlgorithm can be used only over MultipleCTBNC models");
		
		MultipleCTBNC<TimeType, NodeType> mCTBNC = (MultipleCTBNC<TimeType, NodeType>) model; 
		List<ILearningResults> learningResults = new Vector<ILearningResults>( mCTBNC.getNumberOfModel());
		
		// Learn each model expert
		for(int i = 0; i < mCTBNC.getNumberOfModel(); ++i) {
			
			// Generate the transformed training set
			Collection<ITrajectory<TimeType>> newTrainingSet = new Vector<ITrajectory<TimeType>>(trainingSet.size());
			Iterator<ITrajectory<TimeType>> trjIter = trainingSet.iterator();
			while( trjIter.hasNext())
				newTrainingSet.add( mCTBNC.generateClassTrajectory( trjIter.next(), i));
			
			// Learn the model expert
			learningResults.add( this.learningAlgorithm.learn( mCTBNC.getModel(i), newTrainingSet));
		}		
		
		return new MultipleCTBNCLearningResults( learningResults);
	}

}

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
package CTBNCToolkit.tests.validators;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import CTBNCToolkit.*;
import CTBNCToolkit.clustering.ClusteringResults;
import CTBNCToolkit.performances.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Validator designed to test in sample
 * clustering algorithms.
 * The algorithm learn over the dataset
 * and the performance are calculated
 * over the learned dataset.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <SingPType> type of single run performances returned
 */
public class ClusteringInSample<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,SingPType extends IExternalClusteringSingleRunPerformances<TimeType>>
		extends
		BaseValidationMethod<TimeType, NodeType, TM, SingPType>
		implements
		IValidationMethod<TimeType, NodeType, TM, SingPType> {

	private ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory;
	
	
	/**
	 * Base constructor.
	 * 
	 * @param singleRunerformancesFactory factory to generate the single run performances
	 */
	public ClusteringInSample(ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory) {
		
		if( singleRunerformancesFactory == null)
			throw new IllegalArgumentException("Error: null value for the performances factory parameter");
		
		this.singleRunerformancesFactory = singleRunerformancesFactory;
		
		super.setVerbose( false);
		super.setPrintSSFlag( false);
		super.setClusterInSampleFlag( true);
	}
	
	@Override
	public void setClusterInSampleFlag( boolean flag) { }
	
	/**
	 * Set the factory to generate the single
	 * run performances.
	 * 
	 * @param singleRunerformancesFactory factory to generate the single run performances
	 */
	public void setPerformancesFactory(ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory) {
		
		if( singleRunerformancesFactory == null)
			throw new IllegalArgumentException("Error: null value for the performances factory parameter");
		
		this.singleRunerformancesFactory = singleRunerformancesFactory;
	}
	
	
	@SuppressWarnings("unchecked")
	public SingPType validate(
			TM mdl,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			List<ITrajectory<TimeType>> dataset) {

		// Initialization
		DiscreteNode classNode = mdl.getClassNode();
		Map<Integer,String> classIndexToValue = new TreeMap<Integer,String>();
		for(int i = 0; i < classNode.getStatesNumber(); ++i)
			classIndexToValue.put(i, classNode.getStateName(i));
		
		super.verbosePrint("\n\nIn sample test over all the " + dataset.size() + " trajectories\n");
		super.verbosePrint("\nDataset = Training set = Test set\n");
		
		long startTime = System.currentTimeMillis();
		ILearningResults classificationResult = learnAlgo.learn(mdl, dataset);
		double learningTime = (System.currentTimeMillis() - startTime) / 1000.0;
		
		if( !ClusteringResults.class.isAssignableFrom(classificationResult.getClass()))
			throw new RuntimeException("Error: learning algorithm must be a clustering algorithm that return an istance of \"ClusteringResults\" class");
		
		// Generate in sample performances
		SingPType inSamplePerformances =  this.singleRunerformancesFactory.newInstance(classIndexToValue);
		inSamplePerformances.addResults((( ClusteringResults<TimeType>)classificationResult).getClusterizedTrajectories() );
		
		// Sufficient statistics
		printSufficientStatistics(classificationResult, mdl);
		inSamplePerformances.setLearnedModel( mdl);
		inSamplePerformances.setLearningTime( learningTime);
		
		super.verbosePrint("Model learnt in " + learningTime + " seconds\n\n");
		
		return inSamplePerformances;
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.tests.validators.IValidationMethod#validate(CTBNToolkit.IModel, CTBNToolkit.ILearningAlgorithm, CTBNToolkit.IClassifyAlgorithm, java.util.List)
	 */
	@Override
	public SingPType validate(
			TM mdl,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			IClassifyAlgorithm<TimeType, NodeType> infAlgo,
			List<ITrajectory<TimeType>> dataset) {

		return this.validate(mdl, learnAlgo, dataset);
	}

}

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
 * Hold out validation method.
 * No random sorting of the
 * dataset is done.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <SingPType> type of single run performances returned
 */
public class HoldOut<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,SingPType extends ISingleRunPerformances<TimeType>>
		extends
		BaseValidationMethod<TimeType, NodeType, TM, SingPType>
		implements
		IValidationMethod<TimeType, NodeType, TM, SingPType> {

	private ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory;
	private double cut;
	private List<ITrajectory<TimeType>> trainingSet, testSet;
	
	
	/**
	 * Base constructor.
	 * Fixed cut = 0.7: 70% training set,
	 * 30% test set.
	 * 
	 * @param singleRunerformancesFactory factory to generate the single run performances
	 */
	public HoldOut(ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory) {
		
		this(singleRunerformancesFactory, 0.7);
	}
	
	/**
	 * Constructor.
	 * Allow to set test set and training set.
	 * If training set and test set are set, the
	 * dataset in the validation method call is
	 * ignored.
	 * 
	 * @param singleRunerformancesFactory factory to generate the single run performances
	 * @param trainingSet training set to use
	 * @param testSet test set to use
	 */
	public HoldOut(ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory, List<ITrajectory<TimeType>> trainingSet, List<ITrajectory<TimeType>> testSet) {
		
		this(singleRunerformancesFactory, 0.7);
		
		this.setTrainingSet(trainingSet);
		this.setTestSet(testSet);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param singleRunerformancesFactory factory to generate the single run performances
	 * @param cut cut value that fix the division between training set and test set (i.e. 0.7 => training set 70%, test set 30%)
	 */
	public HoldOut(ISingleRunPerformancesFactory<TimeType, SingPType> singleRunerformancesFactory, double cut) {
		
		if( singleRunerformancesFactory == null)
			throw new IllegalArgumentException("Error: null value for the performances factory parameter");
		
		this.trainingSet = null;
		this.testSet = null;
		this.singleRunerformancesFactory = singleRunerformancesFactory;
		this.cut = cut;
		
		super.setVerbose( false);
		super.setPrintSSFlag( false);
		super.setClusterInSampleFlag( false);
	}
	
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
	
	/**
	 * Reset training set and test set.
	 */
	public void resetDatasets() {
		
		this.trainingSet = null;
		this.testSet = null;
	}
	
	/**
	 * Set the training set.
	 * 
	 * @param dataset dataset to set
	 */
	public void setTrainingSet( List<ITrajectory<TimeType>> dataset) {
		
		if( dataset == null)
			throw new IllegalArgumentException("Error: null dataset argument");
		if( dataset.size() < 1)
			throw new IllegalArgumentException("Error: too small dataset argument");
		
		this.trainingSet = dataset;
	}
	
	/**
	 * Set the test set.
	 * 
	 * @param dataset dataset to set
	 */
	public void setTestSet( List<ITrajectory<TimeType>> dataset) {
		
		if( dataset == null)
			throw new IllegalArgumentException("Error: null dataset argument");
		if( dataset.size() < 1)
			throw new IllegalArgumentException("Error: too small dataset argument");
		
		this.testSet = dataset;
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

		SingPType results = null;
		
		if( this.trainingSet != null && this.testSet != null) {			// if predefined dataset were set
			results = this.validate(mdl, learnAlgo, infAlgo);
		} else {
			// Dataset cut												// if predefined dataset were not set
			int cutIndex = (int) Math.round(this.cut * dataset.size());
			
			super.verbosePrint("\n\nHold out schema (cut = " + this.cut + ")\n");
			super.verbosePrint("Training set uses the first " + cutIndex + " trajectories\n");
			super.verbosePrint("Test set uses the last " + (dataset.size()-cutIndex-1) + " trajectories\n\n");
			
			this.trainingSet = dataset.subList(0, cutIndex);
			this.testSet = dataset.subList(cutIndex, dataset.size());
			
			// Validation
			results = this.validate(mdl, learnAlgo, infAlgo);
			
			// Resetting of the state of the datasets
			this.resetDatasets();
		}
		
		return results;
	}

	
	/**
	 * Validate the algorithms using the training
	 * set and the test set previously defined.
	 * Learn the model in input using the specified
	 * learning algorithm. Then make inferences and
	 * returns the performances.
	 * 
	 * @param mdl model to learn
	 * @param learnAlgo learning algorithm
	 * @param infAlgo inference algorithm
	 * @return performances of the experimentation
	 */
	@SuppressWarnings("unchecked")
	public SingPType validate(
			TM mdl,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			IClassifyAlgorithm<TimeType, NodeType> infAlgo) {
		
		if( this.trainingSet == null)
			throw new RuntimeException("Error: training set not setted");
		if( this.testSet == null)
			throw new RuntimeException("Error: test set not setted");

		// Initialization
		NodeIndexing nodeIndexing = mdl.getNodeIndexing();
		DiscreteNode classNode = mdl.getClassNode();
		Map<Integer,String> classIndexToValue = new TreeMap<Integer,String>();
		for(int i = 0; i < classNode.getStatesNumber(); ++i)
			classIndexToValue.put(i, classNode.getStateName(i));
		// Performance declaration
		SingPType outOfSamplePerformances = this.singleRunerformancesFactory.newInstance(classIndexToValue);
		SingPType inSamplePerformances = null;		
		
		// Learning
		super.verbosePrint("Learning (training set of " + this.trainingSet.size() + " trajectories):\n");
		
		long startTime = System.currentTimeMillis();
		ILearningResults classificationResult = learnAlgo.learn(mdl, this.trainingSet);
		double learningTime = (System.currentTimeMillis() - startTime) / 1000.0;
		// In sample performances
		if( super.getClusterInSampleFlag() && ClusteringResults.class.isAssignableFrom(classificationResult.getClass())) {
		
			// Generate in sample performances
			inSamplePerformances = this.singleRunerformancesFactory.newInstance(classIndexToValue);
			inSamplePerformances.addResults((( ClusteringResults<TimeType>)classificationResult).getClusterizedTrajectories() );
			inSamplePerformances.calculateFinalResults(true);
		}
		// Sufficient statistics
		printSufficientStatistics(classificationResult, mdl);
		outOfSamplePerformances.setLearnedModel( mdl);
		outOfSamplePerformances.setLearningTime( learningTime);
		
		super.verbosePrint("Model learnt in " + learningTime + " seconds\n\n");
		
		
		// Testing
		double prob;
		String classPrediction;
		String trueClass;
		super.verbosePrint("Testing (test set of " + this.testSet.size() + "trajectories):\n");
		for (int iTrj = 0; iTrj < this.testSet.size(); ++iTrj)
		{
			try
			{
				super.verbosePrint("Inference on " + this.testSet.get(iTrj).getName() + " test set trajectory\n");
				startTime = System.currentTimeMillis();
				IClassificationResult<TimeType> res = mdl.classify( infAlgo, this.testSet.get( iTrj));
				double inferenceTime =  (System.currentTimeMillis() - startTime) / 1000.0;
				
				classPrediction = res.getClassification();
				trueClass = res.getNodeValue(0, nodeIndexing.getClassIndex());
				if( infAlgo.probabilityFlag()) {
					prob = res.getProbability(classPrediction);
					super.verbosePrint( this.testSet.get( iTrj).getName() + ": True Class: " + trueClass + ", Predicted: " + classPrediction + ", Probability: " + prob + ", Inference time: " + inferenceTime + "\n");
				} else
					super.verbosePrint( this.testSet.get( iTrj).getName() + ": True Class: " + trueClass + ", Predicted: " + classPrediction + ", Inference time: " + inferenceTime + "\n");
				
				outOfSamplePerformances.addResult( res, inferenceTime);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
		if( super.getClusterInSampleFlag() && IExternalClusteringPerformances.class.isAssignableFrom( outOfSamplePerformances.getClass()))
			(( IExternalClusteringPerformances) outOfSamplePerformances).setInSamplePerformances(
					(IExternalClusteringPerformances) inSamplePerformances);
		
		return outOfSamplePerformances;
	}
}

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
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Class that define a classifiers container where a model
 * is learned for each class and the classification is made
 * using the experts votes.
 * 
 * Example: in a 3 class problem 3 binary CTNBClassifier are
 * generated one to classify class 1 against the others, 2
 * for class 2 against the others, 3 for class 3 against the
 * others.
 * The classification relies on the model voting for their
 * specialization class.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> node type
 */
public class MultipleCTBNC<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> implements ICTClassifier<TimeType,NodeType> {
	
	static public String otherStateName = "#";
	
	private String name;
	
	private ICTClassifier<TimeType,NodeType> baseModel;
	private List<ICTClassifier<TimeType,NodeType>> models;
	
	private boolean[][] adjMatrix;
	
	
	/**
	 * Base constructor.
	 * 
	 * @param name model name
	 * @param baseModel original model to use to generate the models experts
	 */
	public MultipleCTBNC(String name, ICTClassifier<TimeType,NodeType> baseModel) {
	
		this.name = name;
		this.baseModel = baseModel;
		
		DiscreteNode classNode = baseModel.getClassNode();
		this.models = new Vector<ICTClassifier<TimeType,NodeType>>( classNode.getStatesNumber());
		for( int i = 0; i < classNode.getStatesNumber(); ++i) {
			ICTClassifier<TimeType,NodeType> newModel = (ICTClassifier<TimeType,NodeType>) baseModel.clone();
			
			// Change the class of the new model in a binary class
			newModel.getClassNode().forceBinaryStates( newModel.getClassNode().getStateName( i));
			
			this.models.add( newModel);
		}
	}
	
	
	/**
	 * Constructor to insert directly the elements.
	 * 
	 * @param name model name
	 * @param baseModel original model used to generate the models experts
	 * @param models list of models experts
	 */
	private MultipleCTBNC(String name, ICTClassifier<TimeType,NodeType> baseModel, List<ICTClassifier<TimeType,NodeType>> models) {
		
		if( name == null || name.isEmpty())
			throw new IllegalArgumentException("Error: null or empty model name");
		
		if( baseModel == null)
			throw new IllegalArgumentException("Error: null base models arguments");
		
		if( models == null || models.isEmpty())
			throw new IllegalArgumentException("Error: null or empty list of experts models");
		
		this.name = name;
		this.baseModel = baseModel;
		this.models = models;
	}
	
	
	/**
	 * Get the name of the class with that index.
	 * 
	 * @param classIndex index of the class
	 * @return name of the class with index idx
	 */
	public String getClassName(int classIndex) {
		
		if( classIndex < 0 || classIndex >= models.size())
			throw new IllegalArgumentException("Error: class index out of range");
		
		return this.baseModel.getClassNode().getStateName( classIndex);
	}
	
	
	/**
	 * Return the number of model (it is equal
	 * to the number of classes).
	 * 
	 * @return number of models
	 */
	public int getNumberOfModel() {
		
		return this.models.size();
	}
	
	
	/**
	 * Get the model by index.
	 * 
	 * @param classIndex class index for which the model is an expert
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public ICTClassifier<TimeType,NodeType> getModel(int classIndex) throws IllegalArgumentException {
		
		if( classIndex < 0 || classIndex >= models.size())
			throw new IllegalArgumentException("Error: a model is defined for each class, so the range of value is {0,..., #classes}");
		
		return this.models.get( classIndex);
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#getName()
	 */
	@Override
	public String getName() {
		
		return this.name;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#getNode(int)
	 */
	@Override
	public NodeType getNode(int iNode) throws IllegalArgumentException {

		return this.baseModel.getNode(iNode);
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#generateTrajectory(double)
	 */
	@Override
	public ITrajectory<TimeType> generateTrajectory(double T)
			throws RuntimeException {
		
		throw new RuntimeException("Error: MultipleCTBNC class does not support trajectories generation");
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#getAdjMatrix()
	 */
	@Override
	public boolean[][] getAdjMatrix() {
		
		return this.adjMatrix;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#setStructure(boolean[][])
	 */
	@Override
	public void setStructure(boolean[][] adjMatrix) throws RuntimeException {
		
		for(int i = 0; i < this.models.size(); ++i)
			this.models.get(i).setStructure(adjMatrix);
		
		this.adjMatrix = adjMatrix;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#getNodeIndexing()
	 */
	@Override
	public NodeIndexing getNodeIndexing() {

		return this.baseModel.getNodeIndexing();
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.ICTClassifier#getClassNode()
	 */
	@Override
	public DiscreteNode getClassNode() {

		return this.baseModel.getClassNode();
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.ICTClassifier#classify(CTBNToolkit.IClassifyAlgorithm, CTBNToolkit.ITrajectory)
	 */
	@Override
	public IClassificationResult<TimeType> classify(
			IClassifyAlgorithm<TimeType, NodeType> algorithm,
			ITrajectory<TimeType> trajectory) throws Exception {
		
		// Classify with all the models
		List<IClassificationResult<TimeType>> results = new Vector<IClassificationResult<TimeType>>(this.models.size());
		for( int i = 0; i < this.models.size(); ++i) {
			// Transform the trajectory
			ITrajectory<TimeType> trj = generateClassTrajectory(trajectory, i);
			
			// Classify the trajectory
			results.add( algorithm.classify( this.getModel(i), trj));
		}
		
		
		// Generate the result
		// Initialization
		DiscreteNode classNode = this.baseModel.getClassNode();
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		for(int i = 0; i < classNode.getStatesNumber(); ++i)
			stateToIndex.put(classNode.getStateName(i), i);
		
		// Result initialization
		ClassificationResults<TimeType> finalResult = new ClassificationResults<TimeType>( trajectory);
		int iJmp;
		if( algorithm.probabilityFlag())
			iJmp = 0;
		else
			iJmp = finalResult.getTransitionsNumber() - 1;
		
		// Calculate the probabilities
		for(; iJmp < finalResult.getTransitionsNumber(); ++iJmp) {
			double[] p = new double[results.size()];
			
			// Get the probability for each class
			for(int iC = 0; iC < p.length; ++iC)
				p[iC] = results.get( iC).getProbability( classNode.getStateName( iC));
			
			// Normalize
			p = MultipleCTBNC.normalizeDistribution(p);
			
			// Set result
			finalResult.setProbability(iJmp, p, stateToIndex);
			if( iJmp == finalResult.getTransitionsNumber() - 1)
				finalResult.setClassification( classNode.getStateName( MultipleCTBNC.indexMax(p)));
		}
		
		return finalResult;
	}


	@Override
	public IClassificationResult<TimeType> classify(
			IClassifyAlgorithm<TimeType, NodeType> algorithm,
			ITrajectory<TimeType> trajectory, TimeType samplingInterval)
			throws Exception {
		
		// Classify with all the models
		List<IClassificationResult<TimeType>> results = new Vector<IClassificationResult<TimeType>>(this.models.size());
		for( int i = 0; i < this.models.size(); ++i) {
			// Transform the trajectory
			ITrajectory<TimeType> trj = generateClassTrajectory(trajectory, i);
			
			// Classify the trajectory
			results.add( algorithm.classify( this.getModel(i), trj, samplingInterval));
		}
		
		
		// Generate the result
		// Initialization
		DiscreteNode classNode = this.baseModel.getClassNode();
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		for(int i = 0; i < classNode.getStatesNumber(); ++i)
			stateToIndex.put(classNode.getStateName(i), i);
		Vector<TimeType> timeStream = new Vector<TimeType>( results.get(0).getTransitionsNumber());
		for(int i = 0; i < results.get(0).getTransitionsNumber(); ++i)
			timeStream.add( results.get(0).getTransitionTime(i));
		
		// Result initialization
		ClassificationResults<TimeType> finalResult = new ClassificationResults<TimeType>( trajectory, timeStream);
		int iJmp;
		if( algorithm.probabilityFlag())
			iJmp = 0;
		else
			iJmp = finalResult.getTransitionsNumber() - 1;
		
		// Calculate the probabilities
		for(; iJmp < finalResult.getTransitionsNumber(); ++iJmp) {
			double[] p = new double[results.size()];
			
			// Get the probability for each class
			for(int iC = 0; iC < p.length; ++iC)
				p[iC] = results.get( iC).getProbability( classNode.getStateName( iC));
			
			// Normalize
			p = MultipleCTBNC.normalizeDistribution(p);
			
			// Set result
			finalResult.setProbability(iJmp, p, stateToIndex);
			if( iJmp == finalResult.getTransitionsNumber() - 1)
				finalResult.setClassification( classNode.getStateName( MultipleCTBNC.indexMax(p)));
		}
		
		return finalResult;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.ICTClassifier#classify(CTBNToolkit.IClassifyAlgorithm, CTBNToolkit.ITrajectory, java.util.Vector)
	 */
	@Override
	public IClassificationResult<TimeType> classify(
			IClassifyAlgorithm<TimeType, NodeType> algorithm,
			ITrajectory<TimeType> trajectory, Vector<TimeType> timeStream)
			throws Exception {
		
		// Classify with all the models
		List<IClassificationResult<TimeType>> results = new Vector<IClassificationResult<TimeType>>(this.models.size());
		for( int i = 0; i < this.models.size(); ++i) {
			// Transform the trajectory
			ITrajectory<TimeType> trj = generateClassTrajectory(trajectory, i);
			
			// Classify the trajectory
			results.add( algorithm.classify( this.getModel(i), trj, timeStream));
		}
		
		
		// Generate the result
		// Initialization
		DiscreteNode classNode = this.baseModel.getClassNode();
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		for(int i = 0; i < classNode.getStatesNumber(); ++i)
			stateToIndex.put(classNode.getStateName(i), i);
		
		// Result initialization
		ClassificationResults<TimeType> finalResult = new ClassificationResults<TimeType>( trajectory, timeStream);
		int iJmp;
		if( algorithm.probabilityFlag())
			iJmp = 0;
		else
			iJmp = finalResult.getTransitionsNumber() - 1;
		
		// Calculate the probabilities
		for(; iJmp < finalResult.getTransitionsNumber(); ++iJmp) {
			double[] p = new double[results.size()];
			
			// Get the probability for each class
			for(int iC = 0; iC < p.length; ++iC)
				p[iC] = results.get( iC).getProbability( classNode.getStateName( iC));
			
			// Normalize
			p = MultipleCTBNC.normalizeDistribution(p);
			
			// Set result
			finalResult.setProbability(iJmp, p, stateToIndex);
			if( iJmp == finalResult.getTransitionsNumber() - 1)
				finalResult.setClassification( classNode.getStateName( MultipleCTBNC.indexMax(p)));
		}
		
		return finalResult;
	}

	@Override
	public IModel<TimeType,NodeType> clone() {
		
		List<ICTClassifier<TimeType,NodeType>> newModels = new Vector<ICTClassifier<TimeType,NodeType>>(this.models.size());
		for(int i = 0; i < this.models.size(); ++i)
			newModels.add(( ICTClassifier<TimeType,NodeType>) this.models.get( i).clone());
		
		MultipleCTBNC<TimeType,NodeType> clonedModel = new MultipleCTBNC<TimeType, NodeType>(this.name, this.baseModel, newModels);
		
		return clonedModel;
	}
	
	
	/**
	 * Print the model in string.
	 * 
	 * @return the representation of the model
	 */
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder();
		for( int i = 0; i < this.models.size(); ++i) {
			strBuilder.append("Model " + i + " Class " + this.baseModel.getClassNode().getStateName(i) + "\n");
			strBuilder.append(this.models.get(i).toString() + "\n\n");
		}
		
		return strBuilder.toString();
	}
	
	
	/**
	 * Transform the input trajectories in an
	 * equal trajectory where the class variable
	 * has only 2 states: the one associated to
	 * the class index and all the others.
	 * 
	 * @param trajectory trajectory to transform
	 * @param classIdx class index
	 * @return binary trajectory
	 */
	public ITrajectory<TimeType> generateClassTrajectory(ITrajectory<TimeType> trajectory, int classIdx) {
		
		List<TimeType> times = new Vector<TimeType>( trajectory.getTransitionsNumber());
		List<String[]> values = new Vector<String[]>( trajectory.getTransitionsNumber());
		
		int nNodes = trajectory.getNodeIndexing().getNodesNumber();
		int classNodeIndex = trajectory.getNodeIndexing().getClassIndex();
		for( int i = 0; i < trajectory.getTransitionsNumber(); ++i) {
			
			// Copy the time
			times.add( trajectory.getTransitionTime(i));
			
			// Copy the states
			String[] v = new String[nNodes];
			for(int j = 0; j < nNodes; ++j)
				v[j] = trajectory.getNodeValue(i, j);
			
			// Change the class state
			if( !v[classNodeIndex].equals( this.baseModel.getClassNode().getStateName( classIdx)))
				v[classNodeIndex] = MultipleCTBNC.otherStateName;
			
			// Update the values
			values.add(v);
		}
		
		return (ITrajectory<TimeType>) new CTTrajectory<TimeType>( trajectory.getNodeIndexing(), times, values);
	}
	

	/**
	 * Normalize a vector generating a distribution.
	 * 
	 * @param p vector to normalize
	 * @return a probability distribution
	 */
	private static double[] normalizeDistribution(double[] p) throws IllegalArgumentException {

		if( p == null || p.length < 1)
			throw new IllegalArgumentException("Error: empty vector");
		
		double s = 0;
		double[] newP = new double[p.length]; 
		for( int i = 0; i < p.length; ++i)
			s += p[i];
		for( int i = 0; i < p.length; ++i)
			newP[i] = p[i]/s;
		
		return newP;
	}


	/**
	 * Find the index of the maximum value.
	 * 
	 * @param p vector of values
	 * @return index of the maximum value
	 */
	private static int indexMax(double[] p) throws IllegalArgumentException {

		if( p == null || p.length < 1)
			throw new IllegalArgumentException("Error: empty vector");
		
		double max = p[0];
		int iMax = 0;
		for(int i = 1; i < p.length; ++i)
			if( p[i] > max) {
				max = p[i];
				iMax = i;
			}
		
		return iMax;
	}
}

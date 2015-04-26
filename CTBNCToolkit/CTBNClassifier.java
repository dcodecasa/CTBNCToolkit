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

import java.util.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that implement a Continuous Time Bayesian Network Classifier.
 * 
 * Note: the trajectory generated are sampled starting from an uniform
 * distribution. Only the class variable follow its prior all the others
 * nodes have not prior and so the uniform distribution is used (i.e. the
 * Bayesian Network that represent the prior is not implemented).
 * 
 * Note: the nodes embedded the graphical structure. It is not the class
 * responsibility to check that all the node in the structure are inserted
 * in the model. This must checked outside!
 */
public class CTBNClassifier extends DiscreteModel<Double,CTDiscreteNode> implements ICTClassifier<Double, CTDiscreteNode> {
	
	private int nGeneratedTrajectories = 0;
	private String ext = ".csv";

	/**
	 * Base constructor
	 *  
	 * @param nodeIndexing global node indexing
	 * @param modelName model name
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public CTBNClassifier(NodeIndexing nodeIndexing, String modelName) throws IllegalArgumentException {
		super(nodeIndexing, modelName);
	}

	/**
	 * Constructor that allow to set the nodes.
	 * 
	 * @param nodeIndexing global node indexing
	 * @param modelName model name
	 * @param nodesSet node set
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public CTBNClassifier(NodeIndexing nodeIndexing, String modelName, Set<CTDiscreteNode> nodesSet) throws IllegalArgumentException {
		super(nodeIndexing, modelName, nodesSet);
	}

	@Override
	public ITrajectory<Double> generateTrajectory(double T) throws RuntimeException {

		if( T <= 0)
			throw new IllegalArgumentException("Error: illegal time value");
		if( this.getNodeIndexing().getClassIndex() == -1)
			throw new IllegalStateException("Error: class node doesn't set");
	
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		// Initial state sampling
		generateInitialState(times, values);
		// Trajectory sampling
		generateTrajectory(times, values, T);
		
		CTTrajectory<Double> trj = new CTTrajectory<Double>(this.getNodeIndexing(), times, values);
		++nGeneratedTrajectories;
		trj.setName(this.getName() + "-trj" + nGeneratedTrajectories + this.ext);
		return trj;
	}

	/**
	 * Generate the initial state and update
	 * the trajectory information.
	 * The class is sampled using its prior,
	 * the other nodes are sampled using the
	 * uniform distribution.
	 * 
	 * @param times time stamp for the trajectory
	 * @param values list of values for the trajectory
	 */
	private void generateInitialState(List<Double> times, List<String[]> values) {
		
		NodeIndexing nodeIndexing = this.getNodeIndexing(); 
		String[] s0 = new String[nodeIndexing.getNodesNumber()];
		times.add(0.0);													// generate the time stamp
		
		// Class sampling
		int classIndex = nodeIndexing.getClassIndex();
		CTDiscreteNode clNode = this.getClassNode();
		clNode.setEvidence(clNode.sampleState());						// sample the class
		s0[classIndex] = clNode.getCurrentState();						// add the state to the trajectory
		
		// Uniform sampling for the not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				CTDiscreteNode node = this.getNode(i);
				if( node == null)
					throw new RuntimeException("Error: not all the nodes defined in the general indexing are set");
				
				double[] pDistr = new double[node.getStatesNumber()];	// sample the initial state
				for(int j = 0; j < pDistr.length; ++j)
					pDistr[j] = 1.0 / pDistr.length;
				
				node.setEvidence(DiscreteModel.sample(pDistr));			// set the initial state
				s0[i] = node.getCurrentState();							// add the state to the trajectory
			}
			
		values.add(s0);													// add the first set of values
	}
	
	/**
	 * Generate a trajectory till the time T given
	 * the initial state and the class.
	 * 
	 * @param times list of time jumps for the generated trajectory
	 * @param values list of value map for the generated trajectory
	 * @param T the maximum length of the trajectory
	 */
	private void generateTrajectory(List<Double> times, List<String[]> values, double T) {
		
		NodeIndexing nodeIndexing = this.getNodeIndexing();
		int classIndex = nodeIndexing.getClassIndex();
		LinkedList<Integer> sortedNodes = new LinkedList<Integer>();
		LinkedList<Double> sortedTimes = new LinkedList<Double>();
		LinkedList<Integer> noTimeNodes = new LinkedList<Integer>();
		
		// Initialize the nodes without time
		for(int i = 0; i < nodeIndexing.getNodesNumber(); ++i) {
			if( i != classIndex)
				noTimeNodes.add( i);
		}
		
		CTDiscreteNode tmpNode;
		double currentTime = 0.0;
		while( currentTime < T) {
			// Sample the time for the nodes without time
			while( !noTimeNodes.isEmpty()) {
				// Calculate transition time
				int tmpNodeIndex = noTimeNodes.getFirst();
				tmpNode = this.getNode( tmpNodeIndex);
				double newTime = tmpNode.sampleTransitionTime() + currentTime;
				// Sort the node using the transition time
				int index;									// find the index
				for( index = sortedTimes.size() - 1; index >= 0 && newTime < sortedTimes.get(index); --index); ++index;
				
				// Lists update
				sortedNodes.add(index, tmpNodeIndex);
				sortedTimes.add(index, newTime);
				noTimeNodes.removeFirst();
			}

			currentTime = sortedTimes.getFirst();
			if( currentTime > T)
				break;
			
			// Sample the new transition
			int tmpNodeIndex = sortedNodes.getFirst();
			tmpNode = this.getNode(tmpNodeIndex);
			int nextState = tmpNode.sampleState();
			tmpNode.setEvidence(nextState);
			
			// Update the trajectory
			times.add(currentTime);
			String[] newValues = new String[nodeIndexing.getNodesNumber()];
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
				newValues[iNode] = this.getNode(iNode).getCurrentState();
			values.add(newValues);
			
			// Update the lists
			sortedTimes.removeFirst();							// node update
			sortedNodes.removeFirst();
			noTimeNodes.add(tmpNodeIndex);
			for(int i = 0; i < sortedNodes.size(); ++i) 		// child update
				if( tmpNode.getChildIndex( this.getNode(sortedNodes.get(i)).getName()) != null) {
					noTimeNodes.add(sortedNodes.get(i));
					sortedNodes.remove(i);
					sortedTimes.remove(i);
					--i;
				}
		}
		
		// Add the ending time
		String[] newValues = new String[nodeIndexing.getNodesNumber()];
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
			newValues[iNode] = this.getNode(iNode).getCurrentState();
			tmpNode = this.getNode( iNode);
			tmpNode.retractEvidence();
		}
		times.add(T);
		values.add(newValues);
		
	}

	@Override
	public CTDiscreteNode getClassNode() {

		return this.getNode(this.getNodeIndexing().getClassIndex());
	}

	@Override
	public IClassificationResult<Double> classify(IClassifyAlgorithm<Double, CTDiscreteNode> algorithm, ITrajectory<Double> trajectory) throws Exception {

		return algorithm.classify(this, trajectory);
	}

	@Override
	public IClassificationResult<Double> classify(IClassifyAlgorithm<Double, CTDiscreteNode> algorithm, ITrajectory<Double> trajectory, Double samplingInterval) throws Exception {

		return algorithm.classify(this, trajectory, samplingInterval);
	}

	@Override
	public IClassificationResult<Double> classify(IClassifyAlgorithm<Double, CTDiscreteNode> algorithm, ITrajectory<Double> trajectory, Vector<Double> timeStream) throws Exception {

		return algorithm.classify(this, trajectory, timeStream);
	}
	
	@Override
	public IModel<Double, CTDiscreteNode> clone() {
		
		NodeIndexing nodeIndexing = this.getNodeIndexing();
		
		CTBNClassifier clonedModel = new CTBNClassifier(nodeIndexing, this.getName());
		for(int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			clonedModel.addNode((CTDiscreteNode) this.getNode(i).clone());
		
		clonedModel.setStructure(this.getAdjMatrix());
		
		for(int i = 0; i < nodeIndexing.getNodesNumber(); ++i) {
			CTDiscreteNode node = this.getNode(i);
			CTDiscreteNode clonedNode = clonedModel.getNode(i);
			// CIM update
			for(int pE = 0; pE < clonedNode.getNumberParentsEntries(); ++pE)
				clonedNode.setCIM(pE, node.getCIM(pE).clone());
			if( node.validatedCIMs())
				clonedNode.checkCIMs();
		}
		
		return clonedModel;
	}
	
	/**
	 * Print the model in string.
	 * 
	 * @return the representation of the model
	 */
	public String toString() {
		
		double[][] cim;
		CTDiscreteNode node;
		StringBuilder strBuilder = new StringBuilder(100000);
		NodeIndexing nodeIndexing = this.getNodeIndexing();
		
		// Introdution
		strBuilder.append( "-----------------------\n"); 
		strBuilder.append( "BAYESIAN NETWORK\n"); 
		strBuilder.append( "-----------------------\n"); 
		strBuilder.append( "BBNodes " + nodeIndexing.getNodesNumber() + "\n");
		
		// States
		strBuilder.append( "-----------------------\n");
		// class node
		int classIndex = nodeIndexing.getClassIndex();
		node = this.getNode(classIndex);
		strBuilder.append( node.getName() + "\t" + node.getStatesNumber() + "\n");
		// not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				node = this.getNode(i);
				strBuilder.append( node.getName() + "\t" + node.getStatesNumber() + "\n");
			}
		
		// T=0 structure (disconnected)
		strBuilder.append( "-----------------------\n");
		// class node
		node = this.getNode(classIndex);
		strBuilder.append( node.getName() + " 0\n");
		// not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				node = this.getNode(i);
				strBuilder.append( node.getName() + " 0\n");
			}
		
		// T=0 distribution (uniform distributed)
		strBuilder.append( "-----------------------\n");
		// class node
		node = this.getNode(classIndex);
		strBuilder.append( node.getName() + "\n");
		for(int s = 0; s < node.getStatesNumber(); ++s)
			strBuilder.append( 1/node.getStatesNumber() + " ");
		strBuilder.append( "\n");
		strBuilder.append( "-----------------------\n");
		// not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				node = this.getNode(i);
				strBuilder.append( node.getName() + "\n");
				for(int s = 0; s < node.getStatesNumber(); ++s)
					strBuilder.append( 1.0/node.getStatesNumber() + " ");
				strBuilder.append( "\n");
				strBuilder.append( "-----------------------\n");
			}
		
		// Structure (parental relation)
		strBuilder.append( "-----------------------\n");
		strBuilder.append( "DIRECTED GRAPH \n");
		strBuilder.append( "-----------------------\n");
		// class node
		node = this.getNode(classIndex);
		strBuilder.append( node.getName() + "\t");
		for( int pi = 0; pi < node.getParentsNumber(); ++pi)
			strBuilder.append( node.getParent(pi).getName() + "\t");
		strBuilder.append( "0\n");
		// not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				node = this.getNode(i);
				strBuilder.append( node.getName() + "\t");
				for( int pi = 0; pi < node.getParentsNumber(); ++pi)
					strBuilder.append( node.getParent(pi).getName() + "\t");
					strBuilder.append( "0\n");
			}
		strBuilder.append( "-----------------------\n");
		
		// CIMs
		strBuilder.append( "-----------------------\n");
		strBuilder.append( "CIMS\n");
		strBuilder.append( "-----------------------\n");
		// class node
		node = this.getNode(classIndex);
		strBuilder.append( node.getName() + "\n");
		for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
			cim = node.getCIM(pE);
			strBuilder.append( toStringCIM(cim) + "\n");
		}
		strBuilder.append( "-----------------------\n");
		// not class nodes
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			if( i != classIndex) {
				node = this.getNode(i);
				strBuilder.append( node.getName() + "\n");
				for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					cim = node.getCIM(pE);
					strBuilder.append( toStringCIM(cim) + "\n");
				}
				strBuilder.append( "-----------------------\n");
			}	
		
		return strBuilder.toString();
	}
	 
	/**
	 * Print in a row the CIM.
	 * 
	 * @param cim the CIM to print
	 * @return string of the result
	 */
	private static String toStringCIM(double[][] cim) {
		
		StringBuilder strBuilder = new StringBuilder(500);
		
		for(int i = 0; i < cim.length; ++i) {
			for(int j = 0; j < cim[0].length; ++j)
				strBuilder.append( cim[i][j] + " ");
		}
		
		return strBuilder.toString();
	}

	
}

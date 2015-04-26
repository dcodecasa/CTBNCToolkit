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
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Abstract class that implements a generic model with discrete
 * variables.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> node type
 */
public abstract class DiscreteModel<TimeType extends Number & Comparable<TimeType>, NodeType extends IDiscreteNode> implements IModel<TimeType,NodeType> {
	
	private String name;
	private NodeIndexing nodeIndexing;
	private Vector<NodeType> nodes;
	
	
	/**
	 * Base constructor
	 *  
	 * @param nodeIndexing global node indexing
	 * @param modelName model name
	 * @throws IllegalArgumentException in case of illegal arguments
	 */ 
	public DiscreteModel(NodeIndexing nodeIndexing, String modelName) throws IllegalArgumentException {
			
		if( nodeIndexing == null)
			throw new IllegalArgumentException("Error: null global node indexing");
		
		this.nodeIndexing = nodeIndexing;
		this.nodes = new Vector<NodeType>();
		for( int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			this.nodes.add(null);
			
		this.name = modelName;
	}

	/**
	 * Constructor that allow to set the nodes.
	 * 
	 * @param nodeIndexing global node indexing
	 * @param modelName model name
	 * @param nodesSet nodes set
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public DiscreteModel(NodeIndexing nodeIndexing, String modelName, Set<NodeType> nodesSet) throws IllegalArgumentException {
		
		this(nodeIndexing,modelName);
		
		Iterator<NodeType> iter = nodesSet.iterator();
		while(iter.hasNext())
		{
			NodeType node = iter.next();
			this.nodes.set(this.nodeIndexing.getIndex(node.getName()), node);
		}
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.IModel#getNodeIndexing()
	 */
	@Override
	public NodeIndexing getNodeIndexing() {

		return this.nodeIndexing;
	}
	
	@Override
	public String getName() {
		
		return this.name;
	}
	
	@Override
	public NodeType getNode(int iNode) throws IllegalArgumentException {

		if( iNode < 0 || iNode >= this.nodes.size())
			throw new IllegalArgumentException("Error: Node index " + iNode + " out of bound");
		if( this.nodes.get(iNode) == null)
			throw new IllegalArgumentException("Error: Node " + this.nodeIndexing.getName(iNode) + " (index = " + iNode + " ) didn't find!");
		
		return this.nodes.get(iNode);
	}

	
	/**
	 * Add a node in the model.
	 * 
	 * @param newNode node to add
	 * @return false if there is a node in the model with the same name, true if the node is inserted
	 */
	protected boolean addNode(NodeType newNode) {
		
		int nodeIndex = this.nodeIndexing.getIndex( newNode.getName());
		if( this.nodes.get( nodeIndex) != null)
			return false;
		
		this.nodes.set(nodeIndex, newNode);
		
		return true;
	}
	

	@Override
	public boolean[][] getAdjMatrix() {

		boolean[][] adjM = new boolean[this.nodes.size()][this.nodes.size()];
		
		for( int i = 0; i < this.nodes.size(); ++i)
			for( int j = 0; j < this.nodes.size(); ++j)
				adjM[i][j] = false;
		
		for( int i = 0; i < this.nodes.size(); ++i) {
			NodeType currentNode = this.nodes.get(i);
			if( currentNode == null)
				throw new RuntimeException("Error: not all the nodes defined in the general indexing are set (node = " + this.nodeIndexing.getName(i) + " index = " + i +" )");
			
			for( int c = 0; c < currentNode.getChildrenNumber(); ++c) {
				int cIndex = this.nodeIndexing.getIndex( currentNode.getChild(c).getName());
				adjM[i][cIndex] = true;
			}
		}
			
		return adjM;
	}
	
	@Override
	public void setStructure(boolean[][] adjMatrix) throws RuntimeException {
		
		if( adjMatrix.length != nodeIndexing.getNodesNumber() || adjMatrix[0].length != nodeIndexing.getNodesNumber())
			throw new IllegalArgumentException("Error: the adjacency matrix must be an NxN matrix where N is the number of nodes in the general indexing");
		
		for(int i = 0; i < this.nodeIndexing.getNodesNumber(); ++i) {
			if( this.getNode(i) == null)
				throw new RuntimeException("Error: not all the nodes defined in the general indexing are set");
			this.getNode(i).removeAllParents();
		}
		
		for(int i = 0; i < adjMatrix.length; ++i) {
			NodeType parentNode = this.getNode(i);
			for(int j = 0; j < adjMatrix[0].length; ++j)
				if( adjMatrix[i][j])
					parentNode.addChild(this.getNode(j));
		}
	}
	
	/**
	 * Sample a value given a probability distribution.
	 * 
	 * @param pDistr the probability distribution
	 * @return sampled value
	 * @throws IllegalArgumentException in case of illegal distribution
	 */
	protected static int sample(double[] pDistr) throws IllegalArgumentException {
		
		if(pDistr == null || pDistr.length < 1)
			throw new IllegalArgumentException("Error: empty probability distribution");
		
		int selectedValue = -1;
		double cumulated = 0, v = Math.random();
		for( int i = 0; i < pDistr.length; ++i) {
			cumulated += pDistr[i];
			if( selectedValue == -1 && v < cumulated)
				selectedValue = i;
		}
		if( !Node.equalsDoubles(cumulated, 1.0, 0.0000001))
			throw new IllegalArgumentException("Error: probability disitrubion doesn't sum to 1. Sum = " + cumulated);
	
		return selectedValue;
	}
	
	/**
	 * Clone the model.
	 * 
	 * @return cloned model
	 */
	public abstract IModel<TimeType, NodeType> clone();
}

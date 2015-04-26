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

import CTBNCToolkit.CTBNClassifier;
import CTBNCToolkit.CTDiscreteNode;
import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IModel;
import CTBNCToolkit.NodeIndexing;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Individual that implement the local search for
 * learning the structure in CTBNClassifiers.
 */
public class CTBNCHillClimbingIndividual implements ILocalSearchIndividual<String,CTBNCHillClimbingIndividual> {

	private ICTBNCHillClimbingFactory elementsFactory;
	private ICTClassifier<Double, CTDiscreteNode> model;
	private int nodeIndex;
	private Double value;
	private String id;
	
	/**
	 * Base constructor.
	 * 
	 * @param elementsFactory factory used to generate a new element
	 * @param model model used as starting individual
	 * @param nodeIndex index of the node who learn locally the structure
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public CTBNCHillClimbingIndividual(ICTBNCHillClimbingFactory elementsFactory, ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex) throws IllegalArgumentException {
		
		if( model.getNode( nodeIndex).isStaticNode())
			throw new IllegalArgumentException("Error: the scoring algorithm is defined only on continuous nodes");
		
		this.elementsFactory = elementsFactory;
		this.model = model;
		this.nodeIndex = nodeIndex;
		this.value = null;
		this.id = null;
	}
	
	/**
	 * Base constructor.
	 * 
	 * @param elementsFactory factory used to generate a new element
	 * @param model model used as starting individual
	 * @param nodeIndex index of the node who learn locally the structure
	 * @param value pre-calculated value of the individual
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	protected CTBNCHillClimbingIndividual(ICTBNCHillClimbingFactory elementsFactory, ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex, double value) throws IllegalArgumentException {
		
		this(elementsFactory, model, nodeIndex);
		this.value = value;
		this.id = null;
	}
	
	@Override
	public double evaluate() {
 
		if( this.value == null)
			this.value = this.elementsFactory.evaluate(this.model, this.nodeIndex);
		
		return this.value;
	}
	
	@Override
	public CTBNCHillClimbingIndividual getBestNeighbor(ICache<String,CTBNCHillClimbingIndividual> cache) {
		
		CTBNCHillClimbingIndividual newInd;
		// Generate support structures
		NodeIndexing nodeIndexing = this.model.getNodeIndexing();
		// Indexes
		int classNodeIndex = -1;
		if( this.model instanceof CTBNClassifier)
			classNodeIndex = nodeIndexing.getClassIndex();
		// Structure data
		boolean[][] originalAdjMatrix = this.model.getAdjMatrix();
		int nParents = CTBNCHillClimbingIndividual.countParents(originalAdjMatrix, this.nodeIndex);
		// Best model data
		CTBNCHillClimbingIndividual bestInd = null;
		
		// Find the best neighbor
		for(int i = 0; i < originalAdjMatrix.length; ++i) {
			if( i == this.nodeIndex) 											// loops are ignored (because loops are defined implicitly in the CTBN)
				continue;
			if( i == classNodeIndex && (!this.elementsFactory.getFeatureSelectionMode()))
				continue;
			if( !originalAdjMatrix[i][this.nodeIndex] && nParents >= this.elementsFactory.getMaxParents()) 	// if we want to add a parent but the node have enough parents
				continue;
			
			// Generate the new structure to try
			boolean[][] newAdjMatrix = new boolean[originalAdjMatrix.length][originalAdjMatrix.length]; 
			for(int k = 0; k < newAdjMatrix.length; ++k)						// clone vector by vector
				newAdjMatrix[k] = originalAdjMatrix[k].clone();
			newAdjMatrix[i][this.nodeIndex] = !newAdjMatrix[i][this.nodeIndex];
			
			String key = CTBNCHillClimbingIndividual.getKey(newAdjMatrix, this.nodeIndex);
			if( cache != null && cache.contains(key))
				newInd = cache.get( key);
			else {
				ICTClassifier<Double, CTDiscreteNode> clonedModel = (ICTClassifier<Double, CTDiscreteNode>)this.model.clone();
				clonedModel.setStructure(newAdjMatrix);
				newInd = this.elementsFactory.newInstance(clonedModel, this.nodeIndex);
				if( cache != null)
					cache.put(key, newInd);
			}
			// Update the best element
			if( bestInd == null || newInd.evaluate() > bestInd.evaluate() || (newInd.evaluate() == bestInd.evaluate() && Math.random() > 0.5 )) {
				bestInd = newInd;
			}
		}
		
		return bestInd;
	}

	/**
	 * Get the model associated to the individual.
	 * 
	 * @return the model associated to the individual
	 */
	public IModel<Double, CTDiscreteNode> getModel() {
		
		return this.model;
	}
	
	/**
	 * Count the number of parents for a node using
	 * the adjacent matrix.
	 * 
	 * @param adjMatrix adjacent matrix of the structure
	 * @param nodeIndex node for which count the parents
	 * @return number of parents
	 */
	private static int countParents(boolean[][] adjMatrix, int nodeIndex) {
		
		int count = 0;
		
		for(int i = 0; i < adjMatrix.length; ++i)
			if( adjMatrix[i][nodeIndex])
				++count;
		
		return count;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.ICacheElement#getId()
	 */
	@Override
	public String getId() {
		
		if( this.id != null)
			return this.id;

		this.id = CTBNCHillClimbingIndividual.getKey( this.model.getAdjMatrix(), this.nodeIndex);
		
		return this.id;
	}
	
	/**
	 * Get the unique key that represent the
	 * parent set for the couple adjacency
	 * matrix-node in input.
	 * 
	 * @param adjMatrix matrix to evaluate
	 * @param nodeIndex index of the node (column) to take into account
	 * @return key that identify the parents of the node of the given index
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	static public String getKey(boolean[][] adjMatrix, int nodeIndex) throws IllegalArgumentException {

		if( adjMatrix == null)
			throw new IllegalArgumentException("Error: null adjMatrix");
		if( adjMatrix.length == 0)
			throw new IllegalArgumentException("Error: empty matrix");
		if( adjMatrix[0].length <= nodeIndex)
			throw new IllegalArgumentException("Error: nodeIndex out of bound");
		
		StringBuilder strBuilder = new StringBuilder(adjMatrix.length);
		for(int i = 0; i < adjMatrix.length; ++i) {
			if( adjMatrix[i][nodeIndex])
				strBuilder.append("1");
			else
				strBuilder.append("0");
		}
		
		return strBuilder.toString();
		
		/*
		long id = (long) 0;
		long pow = 1;
		
		for(int i = 0; i < adjMatrix.length; ++i) {
			if( adjMatrix[i][nodeIndex])
				id += pow;
			pow *= 2;
		}
		
		return id;
		*/
	}

}

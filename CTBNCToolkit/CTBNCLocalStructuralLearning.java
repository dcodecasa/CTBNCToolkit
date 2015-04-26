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
import java.util.Map;
import java.util.TreeMap;

import CTBNCToolkit.optimization.ICache;
import CTBNCToolkit.optimization.IElementFactory;
import CTBNCToolkit.optimization.ILocalSearchIndividual;
import CTBNCToolkit.optimization.LRUCache;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Structural learning algorithm with local search
 * defined by Nodelman et al. (2002) and modified
 * for CTBNCs by Codecasa and Stella (2013).
 * The suggested structure is used as starting point.
 * 
 * @param <K> type of the keys used for the caching of the visited elements
 * @param <T> type of optimization element used
 */
public class CTBNCLocalStructuralLearning<K, T extends ILocalSearchIndividual<K,T>> extends LearningAlgorithm<Double, CTDiscreteNode> {

	private IElementFactory<T> elementsFactory;
	private boolean[][] adjMStructure;
	private int maxCacheEntries;

	/**
	 * Base constructor.
	 * Set the default parameters and a
	 * disconnected structure.
	 * 
	 * @param elementsFactory factory used to generate the individuals for the optimization
	 */
	public CTBNCLocalStructuralLearning(IElementFactory<T> elementsFactory) {
		
		this.elementsFactory = elementsFactory;
		this.adjMStructure = null;
		this.setDefaultParameters();
	}

	@Override
	public void setDefaultParameters() { 

		this.maxCacheEntries = 150;
		
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("maxCacheEntries", 150);
		
		super.setParameters(params);
	}
	
	@Override
	public void setParameters(Map<String, Object> params) throws IllegalArgumentException { 
		
		super.setParameters(params);
		
		Integer tmp;
		// maxCacheEntries
		tmp = (Integer)params.get("maxCacheEntries");
		if( tmp != null)
			this.maxCacheEntries = tmp;
	}

	@Override
	public void setStructure(boolean[][] adjMatrix) throws IllegalArgumentException { 

		this.adjMStructure = adjMatrix;
	}

	@Override
	public String helpParameters() {
		
		String helpStr = "";
		helpStr += "Parameters class CTBNCLocalStructuralLearning:\n";
		helpStr += "maxCacheEntries: Maximum number of entries in the LRU cache. [Default value = 150].\n";
		
		return helpStr;
	}

	@Override
	public ILearningResults learn(ICTClassifier<Double, CTDiscreteNode> model, Collection<ITrajectory<Double>> trainingSet) throws RuntimeException {
		
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		
		// Initialize the structures
		boolean[][] learnedAdjMatrix = new boolean[nodeIndexing.getNodesNumber()][nodeIndexing.getNodesNumber()];	// used to save the learned structure
		boolean[][] localAdjMatrix = new boolean[nodeIndexing.getNodesNumber()][nodeIndexing.getNodesNumber()];	// used to learn a local structure (local = for a node)
		for( int i = 0; i < learnedAdjMatrix.length; ++i)
			for( int j = 0; j < learnedAdjMatrix.length; ++j) {
				learnedAdjMatrix[i][j] = false;
				localAdjMatrix[i][j] = false;
			}
		
		this.elementsFactory.setDataset(trainingSet);					// set the dataset to use in the evaluation
		
		// Hill climbing algorithm
		for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {	// for each node
			CTDiscreteNode node = model.getNode(iNode);
			if( node.isStaticNode())									// Ignore static nodes
				continue;
			
			// Create the cache
			ICache<K, T> cache = new LRUCache<K,T>( this.maxCacheEntries);
				
			// Initialize the new local structure:
			// use a disconnected structure except for the column
			// related to the node which we are going to learn
			// the parents. For this node we copy the suggested
			// structure. In this way we optimize the velocity
			// of the calculation of the sufficient statistics
			// in the learning algorithm.
			if(this.adjMStructure != null)
				for( int iNodeParent = 0; iNodeParent < localAdjMatrix.length; ++iNodeParent)
					localAdjMatrix[iNodeParent][iNode] = this.adjMStructure[iNodeParent][iNode];
			model.setStructure(localAdjMatrix);
			
			// Local Hill climbing algorithm
			// learn the node local structure
			T newInd;
			T currentInd = this.elementsFactory.newInstance(model, iNode);
			if( cache != null)
				cache.put(currentInd.getId(), currentInd);
			do {
				newInd = currentInd.getBestNeighbor(cache);
				if( newInd != null && newInd.evaluate() > currentInd.evaluate())
					currentInd = newInd;
			}while(currentInd == newInd);
			
			// Update structures
			IModel<Double, CTDiscreteNode> bestIndModel = currentInd.getModel();
			boolean[][] bestIndAdjMatrix = bestIndModel.getAdjMatrix();
			for(int iNodeParent = 0; iNodeParent < learnedAdjMatrix.length; ++iNodeParent) {
				// Save the learned local structure
				learnedAdjMatrix[iNodeParent][iNode] =  bestIndAdjMatrix[iNodeParent][iNode];
				// Disconnected the local matrix for the next node
				localAdjMatrix[iNodeParent][iNode] = false;					
			}
		}
		
		// Learn the model with the complete structure
		model.setStructure(learnedAdjMatrix);											// set the learned structure
		return this.elementsFactory.getParamsLearningAlg().learn(model, trainingSet);	// learn the complete model
	}
	
	@Override
	public boolean[][] getStructure() {
		return this.adjMStructure;
	}

}

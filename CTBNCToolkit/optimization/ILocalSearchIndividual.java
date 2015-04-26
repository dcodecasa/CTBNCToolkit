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

import CTBNCToolkit.CTDiscreteNode;
import CTBNCToolkit.IModel;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define an individual managed
 * by local search algorithms.
 * 
 * @param <K> type of the id of the cache elements
 * @param <T> type of the elements managed by local search algorithms
 */
public interface ILocalSearchIndividual<K,T extends ILocalSearchIndividual<K,T>> extends ICacheElement<K>, IOptimizationElement {
	
	/**
	 * Get the best neighbor.
	 * 
	 * @param cache cache used to avoid to evaluate too much time the same individual
	 * @return return the best neighbor of the current element
	 */
	public T getBestNeighbor(ICache<K,T> cache);
	
	/**
	 * Get the model associated to the individual.
	 * 
	 * @return the model associated to the individual
	 */
	public IModel<Double, CTDiscreteNode> getModel();

}

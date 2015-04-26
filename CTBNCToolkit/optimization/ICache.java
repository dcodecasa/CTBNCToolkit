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

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define a cache to use to avoid
 * too many individual evaluations.
 * 
 * @param <K> type of the id of the cache elements
 * @param <V> type of the elements in the cache
 */
public interface ICache<K,V extends ICacheElement<K>> {

	/**
	 * Inform if the id is contained.
	 * 
	 * @param id id to check
	 * @return true if an element with ID=id is contained, false otherwise
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public boolean contains(K id) throws IllegalArgumentException;
	
	/**
	 * Return the element with the parameter
	 * id.
	 * 
	 * @param id id to check
	 * @return the element in the cache with ID=id, if there is not that element return null
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public V get(K id) throws IllegalArgumentException;
		
	/**
	 * Insert the element in the cache using
	 * the key (id) in input.
	 * If the map previously contained a mapping
	 * for the key, the old value is replaced.
	 * 
	 * @param id key that identify the element
	 * @param element element to insert in the cache
	 * @return the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.)
	 * @throws IllegalArgumentException
	 */
	public V put(K id, V element) throws IllegalArgumentException;
	
	/**
	 * Remove the element with the id in
	 * input.
	 * 
	 * @param id id of the element to remove
	 * @return removed element or null if there wasn't element with ID=id
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public V remove(K id) throws IllegalArgumentException;
	
}

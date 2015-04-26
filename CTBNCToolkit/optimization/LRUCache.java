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

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Last Recently Used Cache. Cache that remove
 * the last recently used element.
 *
 * @param <K> type of the id of the cache elements
 * @param <V> type of the elements in the cache
 */
public class LRUCache<K, V extends ICacheElement<K>> implements ICache<K, V> {

	final int maxEntries;
	private LinkedHashMap<K,V> cache;
	
	/**
	 * Constructor.
	 * 
	 * @param maxEntries maximum number of element in the cache
	 */
	public LRUCache( final int maxEntries) {
		
		this( maxEntries, .75F);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param maxEntries maximum number of element in the cache
	 * @param loadFactor load factor in the hash map behind the cache
	 */
	@SuppressWarnings("serial")
	public LRUCache( final int maxEntries, float loadFactor) {
		
		this.maxEntries = maxEntries;
		
		this.cache = new LinkedHashMap<K,V>(this.maxEntries, loadFactor, true) {

			public boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		        return size() >= maxEntries;
		    }
		};

	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.ICache#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(K id) throws IllegalArgumentException {

		if( id == null)
			throw new IllegalArgumentException("Error: null id argument");

		return this.cache.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.ICache#get(java.lang.Object)
	 */
	@Override
	public V get(K id) throws IllegalArgumentException {

		if( id == null)
			throw new IllegalArgumentException("Error: null id argument");

		return this.cache.get(id);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.ICache#remove(java.lang.Object)
	 */
	@Override
	public V remove(K id) throws IllegalArgumentException {

		if( id == null)
			throw new IllegalArgumentException("Error: null id argument");

		return this.cache.remove(id);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.optimization.ICache#put(java.lang.Object, CTBNToolkit.optimization.ICacheElement)
	 */
	@Override
	public V put(K id, V element) throws IllegalArgumentException {

		if( id == null)
			throw new IllegalArgumentException("Error: null id argument");
		if( element == null)
			throw new IllegalArgumentException("Error: null element argument");
		
		return this.cache.put( id, element);
	}

}

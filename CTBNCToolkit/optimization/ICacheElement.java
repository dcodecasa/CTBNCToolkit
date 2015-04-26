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
 * Interface to implement for all the cache
 * elements.
 * 
 * @param <K> type of the returned element id
 */
public interface ICacheElement<K> {

	/**
	 * Return the id of the element.
	 * 
	 * @return id of the element
	 */
	public K getId();
}

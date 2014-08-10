/**
 * Copyright (c) 2012-2013, Daniele Codecasa <codecasa.job@gmail.com>,
 * Models and Algorithms for Data & Text Mining (MAD) laboratory of
 * Milano-Bicocca University, and all the CTBNCToolkit contributors
 * that will follow.
 * All rights reserved.
 * 
 * This file is part of CTBNCToolkit, distributed under the GNU
 * General Public License version 2 (GPLv2).
 * https://github.com/C0dd1/CTBNCToolkit
 *
 * @author Daniele Codecasa and all the CTBNCToolkit contributors that will follow.
 * @license http://www.gnu.org/licenses/gpl-2.0.html
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

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
package CTBNCToolkit.clustering;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define the clustering stopping criterion.
 */
public interface IStopCriterion {

	/**
	 * Method to say if stop the clustering
	 * iterations for parameters clustering.
	 * 
	 * @param iIteration iteration counter
	 * @param changedCluster percentage of trajectories that changed the cluster
	 * @return true if stop the parameters learning algorithm, false otherwise.
	 * @throws RuntimeException in case of errors with the input parameters or in the function
	 */
	public boolean paramsStop(int iIteration, double changedCluster) throws RuntimeException;
	
}

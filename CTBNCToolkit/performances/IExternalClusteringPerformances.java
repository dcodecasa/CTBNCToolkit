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
package CTBNCToolkit.performances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface of clustering performances.
 */
public interface IExternalClusteringPerformances extends IPerformances {

	/**
	 * Get the number of cluster.
	 * 
	 * @return number of cluster.
	 */
	public int clusterNumber();
	
	/**
	 * Convert the index in input to the corresponding
	 * cluster.
	 * Null if doesn't exist.
	 * 
	 * @param index index to convert
	 * @return cluster name associated to the index in input
	 */
	public String indexToCluster(int index);	
	
	/**
	 * Convert the cluster name to the corresponding
	 * index.
	 * Null if doesn't exist.
	 * 
	 * @param value name value of the cluster
	 * @return index associated to the name value in input
	 */
	public Integer clusterToIndex(String value);
	
	/**
	 * Return the matrix of the relations
	 * between clusters and real partitions.
	 * It contains the count for each cluster
	 * and each true class in the original
	 * partitioning.
	 * If it wasn't calculated before, it
	 * will be calculated now. 
	 * 
	 * @return matrix of counts (cluster, true class)
	 */
	public double[][] getClustersPartitionsMatrix();
	
	/**
	 * Return the precision matrix.
	 * P[i][j] = precision of cluster i
	 * associated to class j.
	 * 
	 * @return precision matrix
	 */
	public double[][] getPrecisionMatrix();
	
	/**
	 * Return the recall matrix.
	 * R[i][j] =  recall of cluster i
	 * associated to class j.
	 * 
	 * @return recall matrix
	 */
	public double[][] getRecallMatrix();
	
	/**
	 * Return the f-measure matrix.
	 * F[i][j] = f-measure of cluster i
	 * associated to class j.
	 * 
	 * @return f-measure matrix
	 */
	public double[][] getFMeasureMatrix();
	
	/**
	 * Return the 2x2 association matrix:
	 * SS SD
	 * DS DD
	 * where:
	 * - SS: is the number of data pairs that
	 *    are in the same cluster and in the
	 *    same original partition (true state)
	 * - SD: is the number of data pairs that
	 *    are in the same cluster but in
	 *    different partitions (true state)
	 * - DS: is the number of data pairs that
	 *    are in different clusters but in
	 *    the same partition (true state)
	 * - DD: is the number of data pairs that
	 *    are in different clusters and in
	 *    different partitions
	 * 
	 * @return return 2x2 association matrix
	 */
	public double[][] getAssociationMatrix();
	
	/**
	 * Return the Rand statistic (R):
	 * R = (SS + DD) / M
	 * where:
	 *  - M = SS + SD + DS + DD = N(N-1)/2
	 * 
	 * This metric is defined in [0,1]. Highest
	 * values indicate great similarity between
	 * the clustering and the true partitioning.
	 * 
	 * (see getAssociationMatrix() method)
	 * 
	 * @return return the Rand statistic (R)
	 */
	public double getRandStatistic();
	
	/**
	 * Return the Jaccard Coefficient (J):
	 * J = SS / (SS + SD + DS)
	 * 
	 * This metric is defined in [0,1]. Highest
	 * values indicate great similarity between
	 * the clustering and the true partitioning.
	 * 
	 * (see getAssociationMatrix() method)
	 * 
	 * @return return the Jaccard Coefficient (J)
	 */
	public double getJaccardCoefficient();
	
	/**
	 * Return the Folkes and Mallows index (FM):
	 * FM = SS / sqrt(m1 * m2)
	 * where
	 *  - m1 = SS + SD
	 *  - m2 = SS + DS
	 * 
	 * This metric is defined in [0,1]. Highest
	 * values indicate great similarity between
	 * the clustering and the true partitioning.
	 * 
	 * (see getAssociationMatrix() method)
	 * 
	 * @return return the Folkes and Mallows index (FM)
	 */
	public double getFolkesMallowsIndex();
	

	/**
	 * Set the in sample performances
	 * for the current test (if calculated).
	 * 
	 * @param inSamplePerformances in sample performances
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public void setInSamplePerformances(IExternalClusteringPerformances inSamplePerformances) throws IllegalArgumentException;
	
	/**
	 * Return the in sample performances
	 * if calculated.
	 * If not returns null.
	 * 
	 * @return in sample performances or null value if not calculated.
	 */
	public IExternalClusteringPerformances getInSamplePerformances();
}

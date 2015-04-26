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
package CTBNCToolkit.clustering;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import CTBNCToolkit.*;
import CTBNCToolkit.performances.ClusteringExternalPerformances;
import CTBNCToolkit.tests.*;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZZZJTESTCTBNClusteringParametersLLAlgorithm {

	/**
	 * Test method for {@link CTBNCToolkit.clustering.CTBNClusteringParametersLLAlgorithm#setParameters(java.util.Map)}.
	 */
	@Test
	public void testSetParameters() {
		
		double Mxx_prior = 2.23; double Tx_prior = 0.002; double Px_prior = 1.32;
		boolean hardClustering = true;
		
		CTBNClusteringParametersLLAlgorithm cAlg = new CTBNClusteringParametersLLAlgorithm();
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", Mxx_prior);
		params.put("Tx_prior", Tx_prior);
		params.put("Px_prior", Px_prior);
		params.put("hardClustering", hardClustering);
		
		assertTrue(cAlg.getParameter("Mxx_prior").equals(0.0));
		assertTrue(cAlg.getParameter("Tx_prior").equals(0.0));
		assertTrue(cAlg.getParameter("Px_prior").equals(0.0));
		assertTrue(cAlg.getParameter("hardClustering").equals(false));
		cAlg.setParameters(params);
		assertTrue(cAlg.getParameter("Mxx_prior").equals(Mxx_prior));
		assertTrue(cAlg.getParameter("Tx_prior").equals(Tx_prior));
		assertTrue(cAlg.getParameter("Px_prior").equals(Px_prior));
		assertTrue(cAlg.getParameter("hardClustering").equals(hardClustering));
		cAlg.setDefaultParameters();
		assertTrue(cAlg.getParameter("Mxx_prior").equals(0.0));
		assertTrue(cAlg.getParameter("Tx_prior").equals(0.0));
		assertTrue(cAlg.getParameter("Px_prior").equals(0.0));
		assertTrue(cAlg.getParameter("hardClustering").equals(false));
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.clustering.CTBNClusteringParametersLLAlgorithm#CTBNClusteringParametersLLAlgorithm()}.
	 */
	@Test
	public void testCTBNClusteringParametersLLAlgorithmSoft() { 
		//? TODO valutare il perche dell'errore: 
		//? TODO Exception in thread "RMI TCP Connection(idle)" java.lang.OutOfMemoryError: GC overhead limit exceeded
		
		System.out.println("\n testCTBNClusteringParametersLLAlgorithmSoft");
		
		// Dataset generation
		int datasetSize = 1000;
		int iterationNumber = 100;
		List<ITrajectory<Double>> dataSet = new Vector<ITrajectory<Double>>(datasetSize);
		CTBNClassifierFactory modelFactory = CTBNCTwoParentsFactory1();
		CTBNClassifier trueModel = modelFactory.newInstance();
		for(int i = 0; i < datasetSize; ++i)
			dataSet.add( trueModel.generateTrajectory(11.0));
		
		// Generic parameters
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.005);
		params.put("Px_prior", 1.0);
		params.put("hardClustering", false);
		StandardStopCriterion stopCriterion = new StandardStopCriterion(iterationNumber, 0.1);
		
		// Classification algorithm
		Map<String, Object> paramsClassifyAlg = new TreeMap<String,Object>();
		paramsClassifyAlg.put("probabilities", true);
		CTBNCClassifyAlgorithm classificationAlg = new CTBNCClassifyAlgorithm();
		classificationAlg.setParameters(paramsClassifyAlg);
		
		// Algorithm initialization
		CTBNClusteringParametersLLAlgorithm cAlg = new CTBNClusteringParametersLLAlgorithm();
		cAlg.setParameters(params);
		cAlg.setClassificationAlgorithm(classificationAlg);
		cAlg.setStopCriterion( stopCriterion);
		
		// Learning
		CTBNClassifier model = (CTBNClassifier)trueModel.clone();
		ClusteringResults<Double> results = cAlg.learn(model, dataSet);
		
		// Results printing
		Map<Integer,String> statesMap = new TreeMap<Integer,String>();
		for(int i = 0; i < trueModel.getClassNode().getStatesNumber(); ++i)
			statesMap.put(i, trueModel.getClassNode().getStateName(i));
		Map<Integer,String> clustersMap = new TreeMap<Integer,String>();
		for(int i = 0; i < trueModel.getClassNode().getStatesNumber(); ++i)
			clustersMap.put(i, trueModel.getClassNode().getStateName(i));
		printResults( results, statesMap, clustersMap);
	}

	/**
	 * Test method for {@link CTBNCToolkit.clustering.CTBNClusteringParametersLLAlgorithm#CTBNClusteringParametersLLAlgorithm()}.
	 */
	@Test
	public void testCTBNClusteringParametersLLAlgorithmHard() {
		
		System.out.println("\n testCTBNClusteringParametersLLAlgorithmHard");
		
		// Dataset generation
		int datasetSize = 1000;
		int iterationNumber = 100;
		List<ITrajectory<Double>> dataSet = new Vector<ITrajectory<Double>>(datasetSize);
		CTBNClassifierFactory modelFactory = CTBNCTwoParentsFactory1();
		CTBNClassifier trueModel = modelFactory.newInstance();
		for(int i = 0; i < datasetSize; ++i)
			dataSet.add( trueModel.generateTrajectory(11.0));
		
		// Generic parameters
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.005);
		params.put("Px_prior", 1.0);
		params.put("hardClustering", true);
		StandardStopCriterion stopCriterion = new StandardStopCriterion(iterationNumber, 0.1);
		
		// Classification algorithm
		Map<String, Object> paramsClassifyAlg = new TreeMap<String,Object>();
		paramsClassifyAlg.put("probabilities", false);
		CTBNCClassifyAlgorithm classificationAlg = new CTBNCClassifyAlgorithm();
		classificationAlg.setParameters(paramsClassifyAlg);
		
		// Algorithm initialization
		CTBNClusteringParametersLLAlgorithm cAlg = new CTBNClusteringParametersLLAlgorithm();
		cAlg.setParameters(params);
		cAlg.setClassificationAlgorithm(classificationAlg);
		cAlg.setStopCriterion( stopCriterion);
		
		// Learning
		CTBNClassifier model = (CTBNClassifier)trueModel.clone();
		ClusteringResults<Double> results = cAlg.learn(model, dataSet);
		
		// Results printing
		Map<Integer,String> statesMap = new TreeMap<Integer,String>();
		for(int i = 0; i < trueModel.getClassNode().getStatesNumber(); ++i)
			statesMap.put(i, trueModel.getClassNode().getStateName(i));
		Map<Integer,String> clustersMap = new TreeMap<Integer,String>();
		for(int i = 0; i < trueModel.getClassNode().getStatesNumber(); ++i)
			clustersMap.put(i, trueModel.getClassNode().getStateName(i));
		printResults( results, statesMap, clustersMap);
	}

	/**
	 * Test method for {@link CTBNCToolkit.clustering.CTBNClusteringParametersLLAlgorithm#softSampling(int)}.
	 */
	@Test
	public void testSoftSampling() {
		
		int nS = 5;
		double[] pDistr = CTBNClusteringParametersLLAlgorithm.softSampling(nS);
		
		assertTrue( pDistr.length == 5);
		double sum = 0.0;
		for(int i = 0; i < pDistr.length; ++i) {
			assertTrue(pDistr[i] >= 0);
			assertTrue(pDistr[i] <= 1);
			sum += pDistr[i];
		}
		assertTrue(sum > 0.999999 && sum <= 1.000001);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.clustering.ClusteringAlgorithm#setClassificationAlgorithm(CTBNCToolkit.IClassifyAlgorithm)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetClassificationAlgorithmException() {
		
		CTBNClusteringParametersLLAlgorithm cAlg = new CTBNClusteringParametersLLAlgorithm();
		cAlg.setClassificationAlgorithm(null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.clustering.ClusteringAlgorithm#setStopCriterion(CTBNCToolkit.clustering.IStopCriterion)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetStopCriterionException() {
		
		CTBNClusteringParametersLLAlgorithm cAlg = new CTBNClusteringParametersLLAlgorithm();
		cAlg.setStopCriterion(null);
	}
	
	
	/**
	 * Print the clustering results.
	 * 
	 * @param results clustering results
	 * @param statesMap index to true state map
	 * @param clustersMap index to cluster map
	 */
	private void printResults(ClusteringResults<Double> results, Map<Integer,String> statesMap, Map<Integer,String> clustersMap) {

		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( statesMap, clustersMap);
		performances.addResults( results.getClusterizedTrajectories());
		
		System.out.println("Clustes vs Partitions");
		double[][] counts = performances.getClustersPartitionsMatrix();
		for(int i = 0; i < performances.clusterNumber(); ++i) {
			for(int j = 0; j < performances.classesNumber(); ++j)
				System.out.println("[" + performances.indexToCluster(i) + "," + performances.indexToValue(j) + " = " + counts[i][j] + "]");
			System.out.println();
		}
		
		System.out.println("Precision matrix");
		double[][] p = performances.getPrecisionMatrix();
		for(int i = 0; i < performances.clusterNumber(); ++i) {
			for(int j = 0; j < performances.classesNumber(); ++j)
				System.out.println("[" + performances.indexToCluster(i) + "," + performances.indexToValue(j) + " = " + p[i][j] + "]");
			System.out.println();
		}
		
		System.out.println("Recall matrix");
		double[][] r = performances.getRecallMatrix();
		for(int i = 0; i < performances.clusterNumber(); ++i) {
			for(int j = 0; j < performances.classesNumber(); ++j)
				System.out.println("[" + performances.indexToCluster(i) + "," + performances.indexToValue(j) + " = " + r[i][j] + "]");
			System.out.println();
		}
		
		System.out.println("F-Measure matrix");
		double[][] f = performances.getFMeasureMatrix();
		for(int i = 0; i < performances.clusterNumber(); ++i) {
			for(int j = 0; j < performances.classesNumber(); ++j)
				System.out.println("[" + performances.indexToCluster(i) + "," + performances.indexToValue(j) + " = " + f[i][j] + "]");
			System.out.println();
		}		
		
		System.out.println("Clustes vs Partitions");
		double[][] assM = performances.getAssociationMatrix();
		printMatrix(assM);
		
		System.out.println("R = " + performances.getRandStatistic());
		System.out.println("J = " + performances.getJaccardCoefficient());
		System.out.println("FM = " + performances.getFolkesMallowsIndex());
		
	}
	
	/**
	 * Print the matrix in input.
	 * 
	 * @param counts integer matrix
	 */
	private void printMatrix(double[][] mx) {
		
		for( int i = 0; i < mx.length; ++i) {
			for( int j = 0; j < mx[0].length; ++j)
				System.out.print("[" + mx[i][j] + "]");
			System.out.println();
		}
	}

	/**
	 * CTBNC with two parents version 1
	 */
	private static CTBNClassifierFactory CTBNCTwoParentsFactory1() {

		// Number of variables
		int N = 16;
		// Number of states
		int[] nStates = new int[N];
		nStates[0] = 4;
		nStates[1] = 2;	nStates[2] = 2; nStates[3] = 2;	nStates[4] = 2; nStates[5] = 2;
		nStates[6] = 3;	nStates[7] = 3; nStates[8] = 3;	nStates[9] = 3; nStates[10] = 3;
		nStates[11] = 3;nStates[12] = 4;nStates[13] = 4;nStates[14] = 4; nStates[15] = 4;
		
		// Lambda ranges
		double[][] lambdaRanges = new double[2][N];
		lambdaRanges[0][1] = 1; lambdaRanges[1][1] = 2;
		lambdaRanges[0][2] = 1; lambdaRanges[1][2] = 2;
		lambdaRanges[0][3] = 2; lambdaRanges[1][3] = 4;
		lambdaRanges[0][4] = 2; lambdaRanges[1][4] = 4;
		lambdaRanges[0][5] = 4; lambdaRanges[1][5] = 8;
		lambdaRanges[0][6] = 1; lambdaRanges[1][6] = 2;
		lambdaRanges[0][7] = 1; lambdaRanges[1][7] = 2;
		lambdaRanges[0][8] = 2; lambdaRanges[1][8] = 4;
		lambdaRanges[0][9] = 4; lambdaRanges[1][9] = 8;
		lambdaRanges[0][10] = 4; lambdaRanges[1][10] = 8;
		lambdaRanges[0][11] = 1; lambdaRanges[1][11] = 2;
		lambdaRanges[0][12] = 2; lambdaRanges[1][12] = 4;
		lambdaRanges[0][13] = 2; lambdaRanges[1][13] = 4;
		lambdaRanges[0][14] = 4; lambdaRanges[1][14] = 8;
		lambdaRanges[0][15] = 4; lambdaRanges[1][15] = 8;
		
		// Structure
		boolean[][] adjMatrix = new boolean[N][N];
		for(int i = 0; i < N; ++i)
			for(int j = 0; j < N; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[0][1] = true; adjMatrix[2][1] = true; adjMatrix[3][1] = true;
		adjMatrix[0][4] = true; adjMatrix[5][4] = true; adjMatrix[6][4] = true;
		adjMatrix[0][7] = true; adjMatrix[8][7] = true; adjMatrix[9][7] = true;
		adjMatrix[0][10] = true; adjMatrix[11][10] = true; adjMatrix[12][10] = true;
		adjMatrix[0][13] = true; adjMatrix[14][13] = true; adjMatrix[15][13] = true;
		
		
		return new CTBNClassifierFactory( "CTBNCTwoParents1", nStates, lambdaRanges, adjMatrix);
	}
}

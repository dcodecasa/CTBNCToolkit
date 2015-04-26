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
package CTBNCToolkit.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;



import CTBNCToolkit.CTBNClassifier;
import CTBNCToolkit.IClassificationResult;
import CTBNCToolkit.ITrajectory;
import CTBNCToolkit.NodeIndexing;

/**
 * @author Daniele Codecasa <danielecdcs@gmail.com>
 *
 */
public class ZJTESTCTBNClassifierFactory {

	/**
	 * Test method for {@link CTBNCToolkit.tests.CTBNClassifierFactory#CTBNClassifierFactory(int[], double[][], double)}.
	 */
	@Test
	public void testCTBNClassifierFactoryIntArrayDoubleArrayArrayDouble() {
		System.out.println("TEST ZJTESTCTBNClassifierFactory.testCTBNClassifierFactoryIntArrayDoubleArrayArrayDouble");
		
		// Number of variables
		int N = 3;
		// Number of states
		int[] nStates = new int[N];
		nStates[0] = 3;
		nStates[1] = 2;
		nStates[2] = 3;
		// Lambda ranges
		double[][] lambdaRanges = new double[2][N];
		lambdaRanges[0][0] = 0; lambdaRanges[1][0] = 0;
		lambdaRanges[0][1] = 2; lambdaRanges[1][1] = 4;
		lambdaRanges[0][2] = 10; lambdaRanges[1][2] = 15;
		
		CTBNClassifierFactory factory = new CTBNClassifierFactory("model", nStates, lambdaRanges);
		//assertTrue( factory.getTrajectoryLength() == (40*2/(3*1 + 12.5*2)));
		
		CTBNClassifier nbModel = factory.newInstance("testModel");
		assertTrue(nbModel.getName().equals("testModel"));
		System.out.println(nbModel.toString());
		System.out.println(nbModel.generateTrajectory(factory.getTrajectoryLength()).getTransitionsNumber());
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.CTBNClassifierFactory#CTBNClassifierFactory(int[], double[][], double, boolean[][])}.
	 */
	@Test
	public void testCTBNClassifierFactoryIntArrayDoubleArrayArrayDoubleBooleanArrayArray() {
		System.out.println("TEST ZJTESTCTBNClassifierFactory.testCTBNClassifierFactoryIntArrayDoubleArrayArrayDoubleBooleanArrayArray");
		
		// Number of variables
		int N = 3;
		// Number of states
		int[] nStates = new int[N];
		nStates[0] = 3;
		nStates[1] = 2;
		nStates[2] = 3;
		// Lambda ranges
		double[][] lambdaRanges = new double[2][N];
		lambdaRanges[0][0] = 0; lambdaRanges[1][0] = 0;
		lambdaRanges[0][1] = 2; lambdaRanges[1][1] = 4;
		lambdaRanges[0][2] = 10; lambdaRanges[1][2] = 15;
		// Structure
		boolean[][] adjMatrix = new boolean[N][N];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix.length; ++j)
				adjMatrix[i][j] = (i == 0 && i != j);
		adjMatrix[1][2] = true;
		
		CTBNClassifierFactory factory = new CTBNClassifierFactory("testModel", nStates, lambdaRanges, adjMatrix);
		//assertTrue( factory.getTrajectoryLength() == (40*2/(3*1 + 12.5*2)));
		
		CTBNClassifier nbModel = factory.newInstance();
		assertTrue(nbModel.getName().equals("testModel-1"));
		System.out.println(nbModel.toString());
		System.out.println(nbModel.generateTrajectory(factory.getTrajectoryLength()).getTransitionsNumber());
		assertTrue(factory.newInstance().getName().equals("testModel-2"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.CTBNClassifierFactory#partitionDataset}.
	 * @throws Exception 
	 */
	//! only with the files @Test
	public void testPartitionDataset() throws Exception {
		System.out.println("TEST ZJTESTCTBNClassifierFactory.partitionDataset");
		
		List<ITrajectory<Double>> dataset = new ArrayList<ITrajectory<Double>>();
		Set<String> validColumns = new TreeSet<String>();
		validColumns.add("t");validColumns.add("Class");validColumns.add("N01");
		CTBNCTestFactory.loadDataset("testloadPartitioning", dataset,
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/", ".txt", ',',
				"Class", "t", null, validColumns, 1, 0);
		
		List<List<ITrajectory<Double>>> partitionedDataset1 = CTBNCTestFactory.partitionDataset(
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/DBN-NB2_dT0.2/TestPartitioning",
				dataset, null, ".txt");
		List<List<ITrajectory<Double>>> partitionedDataset2 = CTBNCTestFactory.partitionDataset(
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/NB-results.txt",
				dataset, "naiveBayes1-1-", ".txt");
		
		/*
		List<ITrajectory<Double>> dataset = new ArrayList<ITrajectory<Double>>();
		Set<String> validColumns = new TreeSet<String>();
		validColumns.add("class");
		validColumns.add("A1");validColumns.add("A2");validColumns.add("A3");validColumns.add("A4");validColumns.add("A5");
		validColumns.add("A6");validColumns.add("A7");validColumns.add("A8");validColumns.add("A9");validColumns.add("A10");
		validColumns.add("A11");validColumns.add("A12");validColumns.add("A13");validColumns.add("A14");validColumns.add("A15");
		validColumns.add("A16");validColumns.add("A17");validColumns.add("A18");validColumns.add("A19");validColumns.add("A20");
		validColumns.add("A21");validColumns.add("A22");validColumns.add("A23");validColumns.add("A24");validColumns.add("A25");
		validColumns.add("A26");validColumns.add("A27");validColumns.add("A28");
		CTBNCTestFactory.loadDataset("testloadPartitioning", dataset,
				"/home/daniele/Documents/CTBN-Dataset/Sensors-Stroke/6 classes Full/movement1/", ".csv", ',',
				"class", validColumns, 1);
		
		List<List<ITrajectory<Double>>> partitionedDataset1 = CTBNCTestFactory.partitionDataset(
				"/home/daniele/Documents/CTBN-Dataset/Sensors-Stroke/6 classes Full/movement1/test-results-OLD/NB-results.txt",
				dataset, null, ".csv");
		List<List<ITrajectory<Double>>> partitionedDataset2 = CTBNCTestFactory.partitionDataset(
				"/home/daniele/Documents/CTBN-Dataset/Sensors-Stroke/6 classes Full/movement1/test-results-OLD/NB-results.txt",
				dataset, null, ".csv");
		*/
		// Partitioning print
		for( int i = 0; i < partitionedDataset1.size(); ++i) {
			System.out.println( "Partition " + i + ":");
			for( int j = 0; j < partitionedDataset1.get(i).size(); ++j) {
				System.out.println( partitionedDataset1.get(i).get(j).getName() + " - " + partitionedDataset2.get(i).get(j).getName());
				assertTrue(partitionedDataset1.get(i).get(j).getName().equals( partitionedDataset2.get(i).get(j).getName()));
			}
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.tests.CTBNClassifierFactory#partitionResultDataset}.
	 * @throws Exception 
	 */
	//! only with the files @Test
	public void testPartitionResultDataset() throws Exception {
		System.out.println("TEST ZJTESTCTBNClassifierFactory.partitionResultDatset");
		
		List<IClassificationResult<Double>> dataset1 = new ArrayList<IClassificationResult<Double>>();
		Set<String> validColumns1 = new TreeSet<String>();
		validColumns1.add("time");validColumns1.add("realClass");
		CTBNCTestFactory.loadResultsDataset("loadResultsDataset",
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/DBN-NB2_dT0.2/", 
				".txt", ',', "realClass", "t", null, validColumns1, dataset1);
		List<List<IClassificationResult<Double>>> partitionedDataset1 = CTBNCTestFactory.partitionResultDataset(
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/DBN-NB2_dT0.2/TestPartitioning",
				dataset1, null, ".txt");
		
		List<ITrajectory<Double>> dataset2 = new ArrayList<ITrajectory<Double>>();
		Set<String> validColumns2 = new TreeSet<String>();
		validColumns2.add("t");validColumns2.add("Class");validColumns2.add("N01");
		CTBNCTestFactory.loadDataset("testloadPartitioning", dataset2,
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/", ".txt", ',',
				"Class", "t", null, validColumns2, 1, 0);
		List<List<ITrajectory<Double>>> partitionedDataset2 = CTBNCTestFactory.partitionDataset(
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/NB-results.txt",
				dataset2, "naiveBayes1-1-", ".txt");
				
		// Partitioning print
		for( int i = 0; i < partitionedDataset1.size(); ++i) {
			System.out.println( "Partition " + i + ":");
			for( int j = 0; j < partitionedDataset1.get(i).size(); ++j) {
				System.out.println( partitionedDataset1.get(i).get(j).getName() + " - " + partitionedDataset2.get(i).get(j).getName());
				assertTrue(partitionedDataset1.get(i).get(j).getName().equals( partitionedDataset2.get(i).get(j).getName()));
			}
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.tests.CTBNClassifierFactory#loadResultsDataset}.
	 * @throws Exception 
	 */
	//! only with the files @Test
	public void testLoadResultsDataset() throws Exception {
		System.out.println("TEST ZJTESTCTBNClassifierFactory.loadResultsDataset");
		
		List<IClassificationResult<Double>> dataset = new ArrayList<IClassificationResult<Double>>();
		Set<String> validColumns = new TreeSet<String>();
		validColumns.add("time");validColumns.add("realClass");
		CTBNCTestFactory.loadResultsDataset("loadResultsDataset",
				"/home/daniele/Documents/CTBN-Dataset/SyntheticTest/VM/naiveBayes1/dataset/DBN-NB2_dT0.2/", 
				".txt", ',', "realClass", "t", null, validColumns, dataset);
				
		// Partitioning print
		NodeIndexing nodesIndexing = NodeIndexing.getNodeIndexing("loadResultsDataset");
		assertTrue(dataset.size() == 1000);
		for( int i = 0; i < dataset.size(); ++i) {
			System.out.println( dataset.get(i).getName() + " - " + dataset.get(i).getNodeValue(0, nodesIndexing.getIndex("realClass")) + " - " + dataset.get(i).getClassification());
			double[] pI = dataset.get(0).getPDistribution(0);
			double[] p = dataset.get(i).getPDistribution(3);
			for( int j = 0; j < p.length; ++j) {
				assertTrue(pI[j] == 0.25);
				System.out.print( p[j] + "  ");
			}
			System.out.println();
		}
	}
}

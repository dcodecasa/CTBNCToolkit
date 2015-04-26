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

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

import CTBNCToolkit.*;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTClassificationStandardPerformances {

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.lang.String)}.
	 */
	@Test
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeString() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);

	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeStringException1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(3, "s3");
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.util.Map, java.lang.String)}.
	 */
	@Test
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeMapOfNodeValueTypeIntegerString() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<String, Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 0); stateToIndex.put("s2", 1);  stateToIndex.put("s3", 2);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState, stateToIndex);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.util.Map, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeMapOfNodeValueTypeIntegerStringException1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<String, Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 0); stateToIndex.put("s2", 2);  stateToIndex.put("s3", 1);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState, stateToIndex);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.util.Map, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeMapOfNodeValueTypeIntegerStringException2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<String, Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 3); stateToIndex.put("s2", 1);  stateToIndex.put("s3", 1);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState, stateToIndex);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#ClassificationStandardPerformances(java.util.Map, java.util.Map, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testClassificationStandardPerformancesMapOfIntegerNodeValueTypeMapOfNodeValueTypeIntegerStringException3() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(3, "s3");
		Map<String, Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 0); stateToIndex.put("s2", 1);  stateToIndex.put("s3", 3);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState, stateToIndex);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(performances.valueToIndex( performances.indexToValue(i)) == i);
	}
	
	@Test
	public void testGetLearningTime() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue( Double.isNaN( performances.getLearningTime()));
		performances.setLearningTime( 0.3);
		assertTrue( performances.getLearningTime() == 0.3);
		performances.setLearningTime( 0.0);
		assertTrue( performances.getLearningTime() == 0.0);
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testGetLearningTimeException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue( Double.isNaN( performances.getLearningTime()));
		performances.setLearningTime( -0.1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		performances.addResult(dataset.get(0), -1.02);
	}
	
	@Test
	public void testInferenceTime1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		assertTrue(performances.getInferenceTimes().size() == 0);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResult(dataset.get(0), 1.2);
		assertTrue(performances.getInferenceTimes().size() == 1);
		assertTrue(performances.getAvgInferenceTime() == 1.2);
		assertTrue(performances.getVarianceInferenceTime() == 0.0);
		
		performances.addResult(dataset.get(1), 0.8);
		assertTrue(performances.getInferenceTimes().size() == 2);
		assertTrue(performances.getAvgInferenceTime() == 1.0);
		assertTrue( this.doublesEqual( performances.getVarianceInferenceTime(), Math.pow(0.2, 2)));
		
		performances.addResult(dataset.get(2), 1.0);
		assertTrue(performances.getInferenceTimes().size() == 3);
		assertTrue(performances.getAvgInferenceTime() == 1.0);
		assertTrue( this.doublesEqual( performances.getVarianceInferenceTime(), 2.0*Math.pow(0.2, 2)/3.0));
		
		performances.addResult(dataset.get(3), 1.6);
		assertTrue(performances.getInferenceTimes().size() == 4);
		assertTrue(performances.getAvgInferenceTime() == 4.6/4.0);
		assertTrue( this.doublesEqual( performances.getVarianceInferenceTime(), (Math.pow(1.2 - 4.6/4.0, 2) + Math.pow(0.8 - 4.6/4.0, 2) + Math.pow(1.0 - 4.6/4.0, 2) + Math.pow(1.6 - 4.6/4.0, 2))/4.0));
		
		performances.addResult(dataset.get(4));
		assertTrue( performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResult(dataset.get(5), 3.2);
		assertTrue(performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	
	@Test
	public void testInferenceTime2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.55);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		assertTrue(performances.getInferenceTimes().size() == 0);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResults(dataset, times);
		assertTrue(performances.getInferenceTimes().size() == 10);
		assertTrue( this.doublesEqual( performances.getAvgInferenceTime(), 1.0));
		double var = (2*Math.pow(0.2, 2) + 2*Math.pow(0.7, 2) + 2*Math.pow(0.95, 2) + 2*Math.pow(0.55, 2)) / 10.0;
		assertTrue( this.doublesEqual( performances.getVarianceInferenceTime(), var));
		
		performances.addResult(dataset.get(0));
		assertTrue(performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	
	@Test
	public void testInferenceTime3() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.55);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		assertTrue(performances.getInferenceTimes().size() == 0);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResults(dataset, times);
		assertTrue( performances.getInferenceTimes().size() == 10);
		assertTrue( this.doublesEqual( performances.getAvgInferenceTime(), 1.0));
		double var = (2*Math.pow(0.2, 2) + 2*Math.pow(0.7, 2) + 2*Math.pow(0.95, 2) + 2*Math.pow(0.55, 2)) / 10.0;
		assertTrue( this.doublesEqual( performances.getVarianceInferenceTime(), var));
		
		performances.addResults(dataset, null);
		assertTrue( performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset, times);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException3() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(null);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.54);
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset, times);
	}



	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#addResult(CTBNCToolkit.IClassificationResult)}.
	 */
	@Test
	public void testAddResult() {
		
		double[][] matrix;
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		
		performances.addResult(dataset.get(0));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 1.0)); assertTrue(doublesEqual( matrix[0][1], 0.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 0.0)); assertTrue(doublesEqual( matrix[1][1], 0.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(1));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 1.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 0.0)); assertTrue(doublesEqual( matrix[1][1], 0.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(2));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 0.0)); assertTrue(doublesEqual( matrix[1][1], 0.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(3));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 0.0)); assertTrue(doublesEqual( matrix[1][1], 1.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(4));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 1.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(5));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 0.0));
		
		performances.addResult(dataset.get(6));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 1.0));
		
		performances.addResult(dataset.get(7));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 0.0)); assertTrue(doublesEqual( matrix[2][2], 2.0));
		
		performances.addResult(dataset.get(8));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 1.0)); assertTrue(doublesEqual( matrix[2][2], 2.0));
		
		performances.addResult(dataset.get(9));
		matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 3.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 1.0)); assertTrue(doublesEqual( matrix[2][2], 2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getContingencyMatrix()}.
	 */
	@Test
	public void testGetContingencyMatrix() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[][] matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 3.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 0.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 2.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 0.0)); assertTrue(doublesEqual( matrix[2][1], 1.0)); assertTrue(doublesEqual( matrix[2][2], 2.0));
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getContingencyMatrix()}.
	 */
	@Test
	public void testGetContingencyMatrix2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset2();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[][] matrix = performances.getContingencyMatrix();
		assertTrue(doublesEqual( matrix[0][0], 2.0)); assertTrue(doublesEqual( matrix[0][1], 1.0)); assertTrue(doublesEqual( matrix[0][2], 1.0));
		assertTrue(doublesEqual( matrix[1][0], 1.0)); assertTrue(doublesEqual( matrix[1][1], 1.0)); assertTrue(doublesEqual( matrix[1][2], 0.0));
		assertTrue(doublesEqual( matrix[2][0], 1.0)); assertTrue(doublesEqual( matrix[2][1], 1.0)); assertTrue(doublesEqual( matrix[2][2], 1.0));
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getAccuracy()}.
	 */
	@Test
	public void testGetAccuracy() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getAccuracy(), 7.0/10.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getAccuracy(String)}.
	 */
	@Test
	public void testGetAccuracyString() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] acc = performances.getAccuracy("90%");
		assertTrue(acc.length == 3);
		acc[0] = Math.round(acc[0]*1000) / 1000.0;
		assertTrue(doublesEqual(acc[0], 0.442));
		acc[1] = Math.round(acc[1]*1000) / 1000.0;
		assertTrue(doublesEqual(acc[1], 7.0/10.0));
		acc[2] = Math.round(acc[2]*1000) / 1000.0;
		assertTrue(doublesEqual(acc[2], 0.873));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getAccuracy(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetAccuracyStringException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getAccuracy("10");
	}


	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getError()}.
	 */
	@Test
	public void testGetError() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);

		assertTrue(doublesEqual(performances.getError(), 3.0/10.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecision(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecision() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getPrecision("s1"), 3.0/4.0));
		assertTrue(doublesEqual(performances.getPrecision("s2"), 2.0/4.0));
		assertTrue(doublesEqual(performances.getPrecision("s3"), 2.0/2.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecision(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPrecisionException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getPrecision("s0");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getRecall(java.lang.Object)}.
	 */
	@Test
	public void testGetRecall() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getRecall("s1"), 3.0/4.0));
		assertTrue(doublesEqual(performances.getRecall("s2"), 2.0/3.0));
		assertTrue(doublesEqual(performances.getRecall("s3"), 2.0/3.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getRecall(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetRecallException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getRecall(null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getFMeasure(java.lang.Object)}.
	 */
	@Test
	public void testGetFMeasure() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getFMeasure("s1"), (2.0 / (4.0/3.0 + 4.0/3.0))));
		assertTrue(doublesEqual(performances.getFMeasure("s2"), (2.0 / (4.0/2.0 + 3.0/2.0))));
		assertTrue(doublesEqual(performances.getFMeasure("s3"), (2.0 / (2.0/2.0 + 3.0/2.0))));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getFMeasure(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFMeasureException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getFMeasure("sdda1");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getSensitivity(java.lang.Object)}.
	 */
	@Test
	public void testGetSensitivity() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getSensitivity("s1"), 3.0/4.0));
		assertTrue(doublesEqual(performances.getSensitivity("s2"), 2.0/3.0));
		assertTrue(doublesEqual(performances.getSensitivity("s3"), 2.0/3.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getSensitivity(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSensitivityException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getSensitivity("dsga");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getSpecificity(java.lang.Object)}.
	 */
	@Test
	public void testGetSpecificity() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getSpecificity("s1"), 5.0/6.0));
		assertTrue(doublesEqual(performances.getSpecificity("s2"), 5.0/7.0));
		assertTrue(doublesEqual(performances.getSpecificity("s3"), 7.0/7.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getSpecificity(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSpecificityException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getSpecificity("s22");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getTruePositiveRate(java.lang.Object)}.
	 */
	@Test
	public void testGetTruePositiveRate() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getTruePositiveRate("s1"), 3.0/4.0));
		assertTrue(doublesEqual(performances.getTruePositiveRate("s2"), 2.0/3.0));
		assertTrue(doublesEqual(performances.getTruePositiveRate("s3"), 2.0/3.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getTruePositiveRate(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetTruePositiveRateException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getTruePositiveRate("");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getFalsePositiveRate(java.lang.Object)}.
	 */
	@Test
	public void testGetFalsePositiveRate() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		assertTrue(doublesEqual(performances.getFalsePositiveRate("s1"), 1.0/6.0));
		assertTrue(doublesEqual(performances.getFalsePositiveRate("s2"), 2.0/7.0));
		assertTrue(doublesEqual(performances.getFalsePositiveRate("s3"), 0.0/7.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getFalsePositiveRate(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFalsePositiveRateException() {

		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getFalsePositiveRate("s");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getBrier()}.
	 */
	@Test
	public void testGetBrier1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double brier = Math.pow(0.15, 2) + Math.pow(0.5, 2) + Math.pow(0.5, 2) + Math.pow(0.3, 2) + Math.pow(0.5, 2) + Math.pow(0.15, 2) + Math.pow(0.2, 2) + Math.pow(0.1, 2) + Math.pow(0.65, 2) + Math.pow(0.35, 2);
		brier /= 10;
		assertTrue(doublesEqual(performances.getBrier(), brier));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getBrier()}.
	 */
	@Test
	public void testGetBrier2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset2();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double brier = Math.pow(0.25, 2) + Math.pow(0.5, 2) + Math.pow(0.45, 2) + Math.pow(0.1, 2) + Math.pow(0.6, 2) + Math.pow(0.2, 2) + Math.pow(0.3, 2) + Math.pow(0.75, 2) + Math.pow(0.6, 2);
		brier /= 9;
		assertTrue(doublesEqual(performances.getBrier(), brier));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROCAUC(java.lang.Object)}.
	 */
	@Test
	public void testGetROCAUC() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double AUC;
		AUC = 0.1/0.6 * (3.0/4.0 + 2.0/4.0)/2.0 + 0.5/0.6 * 1.0;
		assertTrue(doublesEqual(performances.getROCAUC("s1"), AUC));
		AUC = 0.2/0.7 * 2.0/3.0 + 0.1/0.7 * (1.0 + 2.0/3.0)/2 + 0.4/0.7 * 1.0;
		assertTrue(doublesEqual(performances.getROCAUC("s2"), AUC));
		AUC = 1.0;
		assertTrue(doublesEqual(performances.getROCAUC("s3"), AUC));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROCAUC(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetAUCException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getROCAUC("");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROC(java.lang.Object)}.
	 */
	@Test
	public void testGetROC1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] point;
		List<double[]> ROC;
		List<double[]> ROCResult;
		// s1
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/6.0; point[1] = 1.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/6.0; point[1] = 2.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/6.0; point[1] = 3.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/6.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/6.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 4.0/6.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 5.0/6.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 6.0/6.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		ROCResult = performances.getROC("s1");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
		// s2
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 1.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/7.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/7.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 3.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 4.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 6.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 7.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		ROCResult = performances.getROC("s2");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
		// s3
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 1.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 4.0/7.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 7.0/7.0; point[1] = 3.0/3.0;  
		ROC.add(point);
		ROCResult = performances.getROC("s3");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROC(java.lang.Object)}.
	 */
	@Test
	public void testGetROC2() {

		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset2();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] point;
		List<double[]> ROC;
		List<double[]> ROCResult;
		// s1
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/5.0; point[1] = 1.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/5.0; point[1] = 2.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/5.0; point[1] = 2.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/5.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 3.0/5.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 5.0/5.0; point[1] = 4.0/4.0; 
		ROC.add(point);
		ROCResult = performances.getROC("s1");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
		// s2
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/7.0; point[1] = 1.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/7.0; point[1] = 1.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/7.0; point[1] = 1.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 2.0/7.0; point[1] = 2.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 4.0/7.0; point[1] = 2.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 5.0/7.0; point[1] = 2.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 6.0/7.0; point[1] = 2.0/2.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 7.0/7.0; point[1] = 2.0/2.0; 
		ROC.add(point);
		ROCResult = performances.getROC("s2");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
		// s3
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0/6.0; point[1] = 1.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/6.0; point[1] = 1.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0/6.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 3.0/6.0; point[1] = 2.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 3.0/6.0; point[1] = 3.0/3.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 6.0/6.0; point[1] = 3.0/3.0;  
		ROC.add(point);
		ROCResult = performances.getROC("s3");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROC(java.lang.Object)}.
	 */
	@Test
	public void testGetROCFawcett() {

		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "p"); indexToState.put(1, "n");
		Collection<IClassificationResult<Double>> dataset = generateFawcettDataset();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] point;
		List<double[]> ROC;
		List<double[]> ROCResult;
		// p
		ROC = new Vector<double[]>();
		point = new double[2]; point[0] = 0.0; point[1] = 0.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0; point[1] = 0.1; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.0; point[1] = 0.2; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.1; point[1] = 0.2; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.1; point[1] = 0.3; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.1; point[1] = 0.4; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.1; point[1] = 0.5; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.2; point[1] = 0.5; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.3; point[1] = 0.5; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.3; point[1] = 0.6; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.4; point[1] = 0.6; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.4; point[1] = 0.7; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.5; point[1] = 0.7; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.5; point[1] = 0.8; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.6; point[1] = 0.8; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.7; point[1] = 0.8; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.8; point[1] = 0.8; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.8; point[1] = 0.9; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.9; point[1] = 0.9; 
		ROC.add(point);
		point = new double[2]; point[0] = 0.9; point[1] = 1.0; 
		ROC.add(point);
		point = new double[2]; point[0] = 1.0; point[1] = 1.0; 
		ROC.add(point);
		ROCResult = performances.getROC("p");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == 2);
			assertTrue(ROCResult.get(i).length == 2);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
		}
		
		assertTrue(doublesEqual( performances.getROCAUC("p"), 0.68));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROC(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetROCException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getROC("");
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallAUC(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecisionRecallAUC() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double AUC;
		AUC = 2.0/4.0 * 1.0 + 1.0/4.0 * (1.0 + 3.0/4.0)/2.0 + 1/4.0 * (3.0/4.0+4.0/5.0)/2.0;
		assertTrue(doublesEqual(performances.getPrecisionRecallAUC("s1"), AUC));
		AUC = 2.0/3.0 * 1.0 + 1/3.0 * (2.0/4.0+3.0/6.0)/2.0;
		assertTrue(doublesEqual(performances.getPrecisionRecallAUC("s2"), AUC));
		AUC = 1.0;
		assertTrue(doublesEqual(performances.getPrecisionRecallAUC("s3"), AUC));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallAUC(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPrecisionRecallAUCException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getPrecisionRecallAUC("s4");
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallCurve(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecisionRecallCurve1() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] point;
		List<double[]> trueCurve;
		List<double[]> resultCurve;
		  
		// s1
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0;
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/2.0; point[0] = 2.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/4.0; point[0] = 3.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/5.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/6.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/8.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/9.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/10.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s1");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
		// s2
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/2.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/3.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/4.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/6.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/7.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/9.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/10.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s2");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
		// s3
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0;
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/2.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/3.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/4.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/7.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/10.0; point[0] = 3.0/3.0;  
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s3");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallCurve(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecisionRecallCurve2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset2();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		double[] point;
		List<double[]> trueCurve;
		List<double[]> resultCurve;
		  
		// s1
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/3.0; point[0] = 2.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/4.0; point[0] = 2.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/6.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/7.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 4.0/9.0; point[0] = 4.0/4.0; 
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s1");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
		// s2
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/2.0; point[0] = 1.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/3.0; point[0] = 1.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/4.0; point[0] = 2.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/6.0; point[0] = 2.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/7.0; point[0] = 2.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/8.0; point[0] = 2.0/2.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/9.0; point[0] = 2.0/2.0; 
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s2");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
		// s3
		trueCurve = new Vector<double[]>();
		point = new double[2]; point[1] = 1.0; point[0] = 0.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/1.0; point[0] = 1.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 1.0/2.0; point[0] = 1.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/3.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 2.0/5.0; point[0] = 2.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/6.0; point[0] = 3.0/3.0; 
		trueCurve.add(point);
		point = new double[2]; point[1] = 3.0/9.0; point[0] = 3.0/3.0;  
		trueCurve.add(point);
		resultCurve = performances.getPrecisionRecallCurve("s3");
		assertTrue(trueCurve.size() == resultCurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == 2);
			assertTrue(resultCurve.get(i).length == 2);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultCurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultCurve.get(i)[1]));
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallCurve(java.lang.Object)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPrecisionRecallCurveException() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Collection<IClassificationResult<Double>> dataset = generateDataset1();
		
		IClassificationSingleRunPerformances<Double> performances = new ClassificationStandardPerformances<Double>(indexToState);
		performances.addResults(dataset);
		
		performances.getPrecisionRecallCurve(null);
	}

	/**
	 * Generate an example dataset proposed
	 * by Fawcett.
	 * 
	 * @return example dataset
	 */
	private List<IClassificationResult<Double>> generateFawcettDataset() {
	
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("FawcettDataset", names, names[0], null);
		
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("p", 0); stateToIndex.put("n", 1); 
		String[] v;
		List<String[]> values;
		List<Double> times = new Vector<Double>(1); times.add( 0.0);
		double[] p;
		List<IClassificationResult<Double>> dataset = new LinkedList<IClassificationResult<Double>>();
		
		// trajectory 1
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		ITrajectory<Double> baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		IClassificationResult<Double> trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.9; p[stateToIndex.get("n")] = 0.1; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("1");
		dataset.add(trj);
		
		// trajectory 2
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.8; p[stateToIndex.get("n")] = 0.2; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("2");
		dataset.add(trj);
		
		// trajectory 3
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.7; p[stateToIndex.get("n")] = 0.3; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("3");
		dataset.add(trj);
		
		// trajectory 4
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.6; p[stateToIndex.get("n")] = 0.4; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("4");
		dataset.add(trj);
		
		// trajectory 5
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.55; p[stateToIndex.get("n")] = 0.45; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("5");
		dataset.add(trj);
		
		// trajectory 6
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.54; p[stateToIndex.get("n")] = 0.46; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("6");
		dataset.add(trj);
		
		// trajectory 7
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.53; p[stateToIndex.get("n")] = 0.47; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("7");
		dataset.add(trj);
		
		// trajectory 8
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.52; p[stateToIndex.get("n")] = 0.48; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("8");
		dataset.add(trj);
		
		// trajectory 9
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.51; p[stateToIndex.get("n")] = 0.49; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("9");
		dataset.add(trj);
		
		// trajectory 10
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.505; p[stateToIndex.get("n")] = 0.495; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("p");				// predicted class
		trj.setName("10");
		dataset.add(trj);
		
		// trajectory 11
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.4; p[stateToIndex.get("n")] = 0.6; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("11");
		dataset.add(trj);
		
		// trajectory 12
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.39; p[stateToIndex.get("n")] = 0.61; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("12");
		dataset.add(trj);
		
		// trajectory 13
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.38; p[stateToIndex.get("n")] = 0.62; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("13");
		dataset.add(trj);
		
		// trajectory 14
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.37; p[stateToIndex.get("n")] = 0.63; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("14");
		dataset.add(trj);
		
		// trajectory 15
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.36; p[stateToIndex.get("n")] = 0.64; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("15");
		dataset.add(trj);
		
		// trajectory 16
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.35; p[stateToIndex.get("n")] = 0.65; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("16");
		dataset.add(trj);
		
		// trajectory 17
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.34; p[stateToIndex.get("n")] = 0.66; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("17");
		dataset.add(trj);
		
		// trajectory 18
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.33; p[stateToIndex.get("n")] = 0.67;  
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("18");
		dataset.add(trj);
		
		// trajectory 19
		v = new String[1];
		v[0] = "p";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.3; p[stateToIndex.get("n")] = 0.7;   
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("19");
		dataset.add(trj);
		
		// trajectory 20
		v = new String[1];
		v[0] = "n";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[2]; p[stateToIndex.get("p")] = 0.1; p[stateToIndex.get("n")] = 0.9;  
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("n");				// predicted class
		trj.setName("20");
		dataset.add(trj);
		
		
		return dataset;
	}
	
	
	/**
	 * Generate an example fake dataset.
	 * 
	 * @return example dataset
	 */
	private List<IClassificationResult<Double>> generateDataset1() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("Dataset1", names, names[0], null);
		
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 0); stateToIndex.put("s2", 1);  stateToIndex.put("s3", 2); 
		String[] v;
		List<String[]> values;
		List<Double> times = new Vector<Double>(1); times.add( 0.0);
		double[] p;
		List<IClassificationResult<Double>> dataset = new LinkedList<IClassificationResult<Double>>();
		
		// trajectory 1
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		CTTrajectory<Double> baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		ClassificationResults<Double> trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.85; p[stateToIndex.get("s2")] = 0.1; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("1");
		dataset.add(trj);
		
		// trajectory 2
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.3; p[stateToIndex.get("s2")] = 0.5; p[stateToIndex.get("s3")] = 0.2; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("2");
		dataset.add(trj);
		
		// trajectory 3
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.5; p[stateToIndex.get("s2")] = 0.4; p[stateToIndex.get("s3")] = 0.1; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("3");
		dataset.add(trj);
		
		// trajectory 4
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.2; p[stateToIndex.get("s2")] = 0.7; p[stateToIndex.get("s3")] = 0.1; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("4");
		dataset.add(trj);
		
		// trajectory 5
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.5; p[stateToIndex.get("s2")] = 0.4; p[stateToIndex.get("s3")] = 0.1; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("5");
		dataset.add(trj);
		
		// trajectory 6
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.85; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("6");
		dataset.add(trj);
		
		// trajectory 7
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.1; p[stateToIndex.get("s3")] = 0.8; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s3");				// predicted class
		trj.setName("7");
		dataset.add(trj);
		
		// trajectory 8
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.05; p[stateToIndex.get("s2")] = 0.05; p[stateToIndex.get("s3")] = 0.9; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s3");				// predicted class
		trj.setName("8");
		dataset.add(trj);
		
		// trajectory 9
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.0; p[stateToIndex.get("s2")] = 0.65; p[stateToIndex.get("s3")] = 0.35; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("9");
		dataset.add(trj);
		
		// trajectory 10
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.65; p[stateToIndex.get("s2")] = 0.3; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("10");
		dataset.add(trj);
		
		
		return dataset;
	}
	
	/**
	 * Generate an example fake dataset.
	 * 
	 * @return example dataset
	 */
	private List<IClassificationResult<Double>> generateDataset2() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("Dataset2", names, names[0], null);
		
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>(); stateToIndex.put("s1", 0); stateToIndex.put("s2", 1);  stateToIndex.put("s3", 2); 
		String[] v;
		List<String[]> values;
		List<Double> times = new Vector<Double>(1); times.add( 0.0);
		double[] p;
		List<IClassificationResult<Double>> dataset = new LinkedList<IClassificationResult<Double>>();
		
		// trajectory 1
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		CTTrajectory<Double> baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		ClassificationResults<Double> trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.75; p[stateToIndex.get("s2")] = 0.1; p[stateToIndex.get("s3")] = 0.15; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("1B");
		dataset.add(trj);
		
		// trajectory 2
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.35; p[stateToIndex.get("s2")] = 0.5; p[stateToIndex.get("s3")] = 0.15; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("2B");
		dataset.add(trj);
		
		// trajectory 3
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.35; p[stateToIndex.get("s2")] = 0.2; p[stateToIndex.get("s3")] = 0.45; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s3");				// predicted class
		trj.setName("3B");
		dataset.add(trj);
		
		// trajectory 4
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.9; p[stateToIndex.get("s2")] = 0.05; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("4B");
		dataset.add(trj);
		
		// trajectory 5
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.6; p[stateToIndex.get("s2")] = 0.35; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("5B");
		dataset.add(trj);
		
		// trajectory 6
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.15; p[stateToIndex.get("s2")] = 0.8; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("6B");
		dataset.add(trj);
		
		// trajectory 7
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.2; p[stateToIndex.get("s3")] = 0.7; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s3");				// predicted class
		trj.setName("7B");
		dataset.add(trj);
		
		// trajectory 8
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.75; p[stateToIndex.get("s2")] = 0.15; p[stateToIndex.get("s3")] = 0.1; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("8B");
		dataset.add(trj);
		
		// trajectory 9
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.6; p[stateToIndex.get("s3")] = 0.3; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("9B");
		dataset.add(trj);
		
		return dataset;
	}
	
	private boolean doublesEqual(double d1, double d2) {
		
		return ((d1 - 0.0000000001)  <= d2) && ((d1 + 0.0000000001) >= d2);
	}
}

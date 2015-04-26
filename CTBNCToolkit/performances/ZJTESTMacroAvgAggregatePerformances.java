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
public class ZJTESTMacroAvgAggregatePerformances {

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#MacroAvgAggregatePerformances(java.util.Map)}.
	 */
	@Test
	public void testMacroAvgAggregatePerformancesMapOfIntegerNodeValueType() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(aggPerformances.valueToIndex( aggPerformances.indexToValue(i)) == i);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#MacroAvgAggregatePerformances(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testMacroAvgAggregatePerformancesMapOfIntegerNodeValueTypeMapOfNodeValueTypeInteger() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		Map<String, Integer> StateToIndexAgg = new TreeMap<String, Integer>(); StateToIndexAgg.put("s1", 2); StateToIndexAgg.put("s2", 0);  StateToIndexAgg.put("s3", 1);
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		for( int i = 0; i < indexToState.size(); ++i)
			assertTrue(aggPerformances.valueToIndex( aggPerformances.indexToValue(i)) == i);
	}

	@Test
	public void testGetLearningTime() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		Map<String, Integer> StateToIndexAgg = new TreeMap<String, Integer>(); StateToIndexAgg.put("s1", 2); StateToIndexAgg.put("s2", 0);  StateToIndexAgg.put("s3", 1);
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.setLearningTime(0.5);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.setLearningTime(0.75);
		performances2.addResults(generateDataset2());
		ClassificationStandardPerformances<Double> performances3 = new ClassificationStandardPerformances<Double>(indexToState);
		performances3.setLearningTime(1.0);
		performances3.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances4 = new ClassificationStandardPerformances<Double>(indexToState);
		performances4.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		
		assertTrue( Double.isNaN( aggPerformances.getLearningTime()));
		assertTrue( Double.isNaN( aggPerformances.getVarianceLearningTime()));
		
		aggPerformances.addPerformances(performances1);
		assertTrue( this.doublesEqual( aggPerformances.getLearningTime(), 0.5));
		assertTrue( this.doublesEqual( aggPerformances.getVarianceLearningTime(), 0.0));
		
		aggPerformances.addPerformances(performances2);
		assertTrue( this.doublesEqual( aggPerformances.getLearningTime(), 0.625));
		assertTrue( this.doublesEqual( aggPerformances.getVarianceLearningTime(), Math.pow(0.125,2)));
		
		aggPerformances.addPerformances(performances3);
		assertTrue( this.doublesEqual( aggPerformances.getLearningTime(), 0.75));
		assertTrue( this.doublesEqual( aggPerformances.getVarianceLearningTime(), 2*Math.pow(0.25,2)/3.0));
	
		aggPerformances.addPerformances(performances4);
		assertTrue( Double.isNaN( aggPerformances.getLearningTime()));
		assertTrue( Double.isNaN( aggPerformances.getVarianceLearningTime()));
		
	}
	
	@Test
	public void testInferenceTime() {
	
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		Map<String, Integer> StateToIndexAgg = new TreeMap<String, Integer>(); StateToIndexAgg.put("s1", 2); StateToIndexAgg.put("s2", 0);  StateToIndexAgg.put("s3", 1);
		
		List<Double> times1 = new Vector<Double>();
		times1.add(0.1);times1.add(0.35);times1.add(0.58);times1.add(0.94);times1.add(1.0);
		times1.add(1.9);times1.add(1.65);times1.add(1.42);times1.add(1.06);times1.add(1.0);
		double var1 = (2*Math.pow( 0.1-1.0, 2) + 2*Math.pow( 0.35-1.0, 2) + 2*Math.pow( 0.58-1.0, 2) + 2*Math.pow( 0.94-1.0, 2)) / 10.0;
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1(), times1);
		
		List<Double> times2 = new Vector<Double>();
		times2.add(9.1);times2.add(9.35);times2.add(9.58);times2.add(9.94);times2.add(11.0);
		times2.add(12.9);times2.add(12.65);times2.add(12.42);times2.add(12.06);
		double var2 = (2*Math.pow( 9.1-11.0, 2) + 2*Math.pow( 9.35-11.0, 2) + 2*Math.pow( 9.58-11.0, 2) + 2*Math.pow( 9.94-11.0, 2)) / 9.0;
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2(), times2);
		
		ClassificationStandardPerformances<Double> performances3 = new ClassificationStandardPerformances<Double>(indexToState);
		performances3.addResults(generateDataset1());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		
		assertTrue( Double.isNaN( aggPerformances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( aggPerformances.getVarianceInferenceTime()));
		
		aggPerformances.addPerformances(performances1);
		assertTrue( this.doublesEqual( aggPerformances.getAvgInferenceTime(), 1.0));
		assertTrue( this.doublesEqual( aggPerformances.getVarianceInferenceTime(), var1));
		
		aggPerformances.addPerformances(performances2);
		assertTrue( this.doublesEqual( aggPerformances.getAvgInferenceTime(), 6.0));
		assertTrue( this.doublesEqual( aggPerformances.getVarianceInferenceTime(), (var1 + var2)/2.0));

		aggPerformances.addPerformances(performances3);
		assertTrue( Double.isNaN( aggPerformances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( aggPerformances.getVarianceInferenceTime()));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#classesNumber()}.
	 */
	@Test
	public void testClassesNumber() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(aggPerformances.classesNumber() == 3);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getPerformances()}.
	 */
	@Test
	public void testGetPerformances() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(((List<ClassificationStandardPerformances<Double>>)aggPerformances.getPerformances()).get(0) == performances1);
		assertTrue(((List<ClassificationStandardPerformances<Double>>)aggPerformances.getPerformances()).get(1) == performances2);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#addPerformances(java.util.Collection)}.
	 */
	@Test
	public void testAddPerformancesCollectionOfISingleRunPerformancesOfTimeTypeNodeValueType() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		Collection<ClassificationStandardPerformances<Double>> testesPerformances = new LinkedList<ClassificationStandardPerformances<Double>>();
		testesPerformances.add(performances1);
		testesPerformances.add(performances2);
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		assertTrue(aggPerformances.datasetDimension() == 0);
		aggPerformances.addPerformances(testesPerformances);
		assertTrue(aggPerformances.datasetDimension() == 19);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#addPerformances(CTBNCToolkit.performances.ISingleRunPerformances)}.
	 */
	@Test
	public void testAddPerformancesISingleRunPerformancesOfTimeTypeNodeValueType() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		assertTrue(aggPerformances.datasetDimension() == 0);
		aggPerformances.addPerformances(performances2);
		assertTrue(aggPerformances.datasetDimension() == 9);
		aggPerformances.addPerformances(performances1);
		assertTrue(aggPerformances.datasetDimension() == 19);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#datasetDimension()}.
	 */
	@Test
	public void testDatasetDimension() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(aggPerformances.datasetDimension() == 19);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getAccuracy()}.
	 */
	@Test
	public void testGetAccuracy() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getAccuracy(), (0.7+4.0/9.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getAccuracy(java.lang.String)}.
	 */
	@Test
	public void testGetAccuracyString() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		double[] acc = aggPerformances.getAccuracy("90%");
		assertTrue(acc.length == 3);
		acc[0] = Math.round(acc[0]*1000) / 1000.0;
		assertTrue(doublesEqual(acc[0], 0.388));
		assertTrue(doublesEqual(acc[1], (0.7+4.0/9.0)/2.0));
		acc[2] = Math.round(acc[2]*1000) / 1000.0;
		assertTrue(doublesEqual(acc[2], 0.738));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getError()}.
	 */
	@Test
	public void testGetError() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getError(), 1.0 - (0.7+4.0/9.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getPrecision(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecision() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getPrecision("s1"),  (3.0/4.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getPrecision("s2"), (2.0/4.0+1.0/3.0)/2.0));
		assertTrue(doublesEqual(aggPerformances.getPrecision("s3"), (2.0/2.0+0.5)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getRecall(java.lang.Object)}.
	 */
	@Test
	public void testGetRecall() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getRecall("s1"), (3.0/4.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getRecall("s2"), (2.0/3.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getRecall("s3"), (2.0/3.0+1.0/3.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getFMeasure(java.lang.Object)}.
	 */
	@Test
	public void testGetFMeasure() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getFMeasure("s1"), (((2.0 / (4.0/3.0 + 4.0/3.0))) + (2.0 / (2.0 + 2.0))) / 2.0 ));
		assertTrue(doublesEqual(aggPerformances.getFMeasure("s2"), (((2.0 / (4.0/2.0 + 3.0/2.0))) + (2.0 / (3.0 + 2.0))) / 2.0 ));
		assertTrue(doublesEqual(aggPerformances.getFMeasure("s3"), (((2.0 / (2.0/2.0 + 3.0/2.0))) + (2.0 / (2.0 + 3.0))) / 2.0 ));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getSensitivity(java.lang.Object)}.
	 */
	@Test
	public void testGetSensitivity() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getSensitivity("s1"), (3.0/4.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getSensitivity("s2"), (2.0/3.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getSensitivity("s3"), (2.0/3.0+1.0/3.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getSpecificity(java.lang.Object)}.
	 */
	@Test
	public void testGetSpecificity() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getSpecificity("s1"), 1.0 - (1.0/6.0+2.0/5.0)/2.0));
		assertTrue(doublesEqual(aggPerformances.getSpecificity("s2"), 1.0 - (2.0/7.0+2.0/7.0)/2.0));
		assertTrue(doublesEqual(aggPerformances.getSpecificity("s3"), 1.0 - (0.0/7.0+1.0/6.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getTruePositiveRate(java.lang.Object)}.
	 */
	@Test
	public void testGetTruePositiveRate() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getTruePositiveRate("s1"), (3.0/4.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getTruePositiveRate("s2"), (2.0/3.0+0.5)/2.0));
		assertTrue(doublesEqual(aggPerformances.getTruePositiveRate("s3"), (2.0/3.0+1.0/3.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getFalsePositiveRate(java.lang.Object)}.
	 */
	@Test
	public void testGetFalsePositiveRate() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		assertTrue(doublesEqual(aggPerformances.getFalsePositiveRate("s1"), (1.0/6.0+2.0/5.0)/2.0));
		assertTrue(doublesEqual(aggPerformances.getFalsePositiveRate("s2"), (2.0/7.0+2.0/7.0)/2.0));
		assertTrue(doublesEqual(aggPerformances.getFalsePositiveRate("s3"), (0.0/7.0+1.0/6.0)/2.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getBrier()}.
	 */
	@Test
	public void testGetBrier() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(1, "s1"); indexToStateAgg.put(2, "s2");  indexToStateAgg.put(0, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		IClassificationAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		double brier1 = Math.pow(0.15, 2) + Math.pow(0.5, 2) + Math.pow(0.5, 2) + Math.pow(0.3, 2) + Math.pow(0.5, 2) + Math.pow(0.15, 2) + Math.pow(0.2, 2) + Math.pow(0.1, 2) + Math.pow(0.65, 2) + Math.pow(0.35, 2);
		brier1 /= 10;
		double brier2 = Math.pow(0.25, 2) + Math.pow(0.5, 2) + Math.pow(0.45, 2) + Math.pow(0.1, 2) + Math.pow(0.6, 2) + Math.pow(0.2, 2) + Math.pow(0.3, 2) + Math.pow(0.75, 2) + Math.pow(0.6, 2);
		brier2 /= 9;
		assertTrue(doublesEqual(aggPerformances.getBrier(), (brier1 + brier2) / 2.0));
	}
	

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getROCAUC(java.lang.Object)}.
	 */
	@Test
	public void testGetROCAUC() {

		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		aggPerformances.setROCSamplesNumber(10);
		aggPerformances.setROCConfidenceInterval("95%");
		
		double AUC;
		AUC = 0.1*((0.25 + 0.5) / 2.0+(0.8 + 0.375) / 2.0)/2.0 + 0.1*((0.8 + 0.375) / 2.0+(1.0 + 0.5) / 2.0)/2.0 + 0.1*((1.0 + 0.5) / 2.0+(1.0 + 0.75) / 2.0)/2.0 + 0.1*((1.0 + 0.75) / 2.0+1.0)/2.0 + 0.6*1.0;
		assertTrue(doublesEqual(aggPerformances.getROCAUC("s1"), AUC));
		AUC =  0.1*((0.6666666667 + 0.5) / 2.0) + 0.1*((0.6666666667 + 0.5) / 2.0+(0.6666666667 + 0.7) / 2.0)/2.0 + 0.1*((0.6666666667 + 0.7) / 2.0+(0.7 + 1.0) / 2.0)/2.0 + 0.1*((0.7 + 1.0) / 2.0+(0.9333333333 + 1.0) / 2.0)/2.0 + 0.1*((0.9333333333 + 1.0) / 2.0+1.0)/2.0 + 0.5*1.0;
		assertTrue(doublesEqual(aggPerformances.getROCAUC("s2"), AUC));
		AUC = 0.1*((1.0 + 1.0/3.0) / 2.0+(1.0 + 0.5333333333) / 2.0)/2.0 + 0.1*((1.0 + 0.5333333333) / 2.0+(1.0 + 0.7) / 2.0)/2.0 + 0.1*((1.0 + 0.7) / 2.0+(1.0 + 0.8) / 2.0)/2.0 + 0.1*((1.0 + 0.8) / 2.0+(1.0 + 0.9) / 2.0)/2.0 + 0.1*((1.0 + 0.9) / 2.0+1.0)/2.0 + 0.5*1.0;
		assertTrue(doublesEqual(aggPerformances.getROCAUC("s3"), AUC));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getROC(java.lang.Object)}.
	 * for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#setROCSamplesNumber(int)},
	 * and for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#setROCConfidenceInterval(java.lang.String)}.
	 */
	@Test
	public void testGetROC() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		aggPerformances.setROCSamplesNumber(10);
		aggPerformances.setROCConfidenceInterval("95%");
		
		double[] point;
		List<double[]> ROC;
		List<double[]> ROCResult;
		// s1
		ROC = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (0.25 + 0.5) / 2.0; point[2] = 0.196; point[3] = 0.596;
		ROC.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (0.8 + 0.375) / 2.0; point[2] = 0.370; point[3] = 0.775;
		ROC.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (1.0 + 0.5) / 2.0; point[2] = 0.525; point[3] = 0.890;
		ROC.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (1.0 + 0.75) / 2.0; point[2] = 0.662; point[3] = 0.962;
		ROC.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.5; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		ROCResult = aggPerformances.getROC("s1");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROCResult.get(i).length == ROC.get(i).length);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
			assertTrue(doublesEqual( ROC.get(i)[2], (Math.round(ROCResult.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( ROC.get(i)[3], (Math.round(ROCResult.get(i)[3]*1000))/1000.0));
		}
		// s2
		ROC = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (0.6666666667 + 0.5) / 2.0; point[2] = 0.367; point[3] = 0.772;
		ROC.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (0.6666666667 + 0.5) / 2.0; point[2] = 0.367; point[3] = 0.772; 
		ROC.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (0.6666666667 + 0.7) / 2.0;  point[2] = 0.459; point[3] = 0.846;
		ROC.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (0.7 + 1.0) / 2.0;  point[2] = 0.633; point[3] = 0.949;
		ROC.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (0.9333333333 + 1.0) / 2.0;  point[2] = 0.781; point[3] = 0.996;
		ROC.add(point);
		point = new double[4]; point[0] = 0.5; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		ROCResult = aggPerformances.getROC("s2");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == ROCResult.get(i).length);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
			assertTrue(doublesEqual( ROC.get(i)[2], (Math.round(ROCResult.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( ROC.get(i)[3], (Math.round(ROCResult.get(i)[3]*1000))/1000.0));
		}
		// s3
		ROC = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (1.0 + 1.0/3.0) / 2.0; point[2] = 0.443; point[3] = 0.834;
		ROC.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (1.0 + 0.5333333333) / 2.0; point[2] = 0.543; point[3] = 0.901;
		ROC.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (1.0 + 0.7) / 2.0; point[2] = 0.633; point[3] = 0.949;
		ROC.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (1.0 + 0.8) / 2.0; point[2] = 0.693; point[3] = 0.973;
		ROC.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (1.0 + 0.9) / 2.0; point[2] = 0.757; point[3] = 0.991;
		ROC.add(point);
		point = new double[4]; point[0] = 0.5; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		ROC.add(point);
		ROCResult = aggPerformances.getROC("s3");
		assertTrue(ROC.size() == ROCResult.size());
		for(int i = 0; i < ROC.size(); ++i) {
			assertTrue(ROC.get(i).length == ROCResult.get(i).length);
			assertTrue(doublesEqual( ROC.get(i)[0], ROCResult.get(i)[0]));
			assertTrue(doublesEqual( ROC.get(i)[1], ROCResult.get(i)[1]));
			assertTrue(doublesEqual( ROC.get(i)[2], (Math.round(ROCResult.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( ROC.get(i)[3], (Math.round(ROCResult.get(i)[3]*1000))/1000.0));
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#getPrecisionRecallAUC(java.lang.Object)}.
	 */
	@Test
	public void testGetPrecisionRecallAUCLAST() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		aggPerformances.setPrecisionRecallSamplesNumber(10);
		aggPerformances.setPrecisionRecallConfidenceInterval("95%");
		
		
		double AUC;
		AUC = 0.2*1.0 + 0.1*(1.0+(1.0 + 0.9) / 2.0)/2.0 + 0.1*((1.0 + 0.9) / 2.0+(1.0 + 0.7) / 2.0)/2.0 + 0.1*((1.0 + 0.7) / 2.0+(1.0 + 0.5) / 2.0)/2.0 + 0.1*((1.0 + 0.5) / 2.0+(0.9 + 0.4888888889) / 2.0)/2.0 + 0.1*((0.9 + 0.4888888889) / 2.0+(0.8 + 0.4777777778) / 2.0)/2.0 + 0.1*((0.8 + 0.4777777778) / 2.0+(0.68 + 0.4666666667) / 2.)/2.0 + 0.1*((0.68 + 0.4666666667) / 2.+(0.54 + 0.4555555556) / 2.0)/2.0 + 0.1*((0.54 + 0.4555555556) / 2.0+(0.4 + 0.4444444444) / 2.0)/2.0;
		assertTrue(doublesEqual(aggPerformances.getPrecisionRecallAUC("s1"), AUC));
		AUC = 0.1*((1.0+(1.0 + 0.8666666667) / 2.0)/2.0 + ((1.0 + 0.8666666667) / 2.0+(1.0 + 0.7333333333) / 2.0)/2.0 + ((1.0 + 0.7333333333) / 2.0+(1.0 + 0.6) / 2.0)/2.0 + ((1.0 + 0.6) / 2.0+(0.9 + 0.4666666667) / 2.0)/2.0 + ((0.9 + 0.4666666667) / 2.0+(0.75 + 0.3333333333) / 2.0)/2.0 + ((0.75 + 0.3333333333) / 2.0+(0.6 + 0.3111111111) / 2.0)/2.0 + ((0.6 + 0.3111111111) / 2.0+(0.48 + 0.2888888889) / 2.0)/2.0 + ((0.48 + 0.2888888889) / 2.0+(0.42 + 0.2666666667) / 2.0)/2.0 + ((0.42 + 0.2666666667) / 2.0+(0.36 + 0.2444444444) / 2.0)/2.0 + ((0.36 + 0.2444444444) / 2.0+(0.3 + 0.2222222222) / 2.0)/2.0);
		assertTrue(doublesEqual(aggPerformances.getPrecisionRecallAUC("s2"), AUC));
		AUC = 0.1*((1.0+(1.0 + 0.85) / 2.0)/2.0 + ((1.0 + 0.85) / 2.0+(1.0 + 0.7) / 2.0)/2.0 + ((1.0 + 0.7) / 2.0+(1.0 + 0.55) / 2.0)/2.0 + ((1.0 + 0.55) / 2.0+(1.0 + 0.48) / 2.0)/2.0 + ((1.0 + 0.48) / 2.0+(1.0 + 0.45) / 2.0)/2.0 + ((1.0 + 0.45) / 2.0+(1.0 + 0.42) / 2.0)/2.0 + ((1.0 + 0.42) / 2.0+(0.93 + 0.3933333333) / 2.0)/2.0 + ((0.93 + 0.3933333333) / 2.0+(0.72 + 0.3733333333) / 2.0)/2.0 + ((0.72 + 0.3733333333) / 2.0+(0.51 + 0.3533333333) / 2.0)/2.0 + ((0.51 + 0.3533333333) / 2.0+(0.3 + 0.3333333333) / 2.0)/2.0);
		assertTrue(doublesEqual(aggPerformances.getPrecisionRecallAUC("s3"), AUC));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClassificationStandardPerformances#getPrecisionRecallCurve(java.lang.Object)},
	 * for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#setPrecisionRecallSamplesNumber(int)}
	 * and for {@link CTBNCToolkit.performances.MacroAvgAggregatePerformances#setPrecisionRecallConfidenceInterval(java.lang.String)}.
	 */
	@Test
	public void testGetPrecisionRecallCurveLAST() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		performances1.addResults(generateDataset1());
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		performances2.addResults(generateDataset2());
		
		MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>> aggPerformances = new MacroAvgAggregatePerformances<Double,ClassificationStandardPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		aggPerformances.setPrecisionRecallSamplesNumber(10);
		aggPerformances.setPrecisionRecallConfidenceInterval("95%");
		
		double[] point;
		List<double[]> trueCurve;
		List<double[]> resultcurve;
		// s1
		trueCurve = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (1.0 + 0.9) / 2.0; point[2] = 0.757; point[3] = 0.991;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (1.0 + 0.7) / 2.0; point[2] = 0.633; point[3] = 0.949;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.5; point[1] = (1.0 + 0.5) / 2.0; point[2] = 0.525; point[3] = 0.890;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (0.9 + 0.4888888889) / 2.0; point[2] = 0.470; point[3] = 0.853;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (0.8 + 0.4777777778) / 2.0; point[2] = 0.417; point[3] = 0.814;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (0.68 + 0.4666666667) / 2.0; point[2] = 0.358; point[3] = 0.764;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (0.54 + 0.4555555556) / 2.0; point[2] = 0.293; point[3] = 0.703;
		trueCurve.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (0.4 + 0.4444444444) / 2.0; point[2] = 0.232; point[3] = 0.638;
		trueCurve.add(point);
		resultcurve = aggPerformances.getPrecisionRecallCurve("s1");
		assertTrue(trueCurve.size() == resultcurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(resultcurve.get(i).length == trueCurve.get(i).length);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultcurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultcurve.get(i)[1]));
			assertTrue(doublesEqual( trueCurve.get(i)[2], (Math.round(resultcurve.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( trueCurve.get(i)[3], (Math.round(resultcurve.get(i)[3]*1000))/1000.0));
		}
		// s2
		trueCurve = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (1.0 + 0.8666666667) / 2.0; point[2] = 0.735; point[3] = 0.986;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (1.0 + 0.7333333333) / 2.0; point[2] = 0.653; point[3] = 0.957;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (1.0 + 0.6) / 2.0; point[2] = 0.578; point[3] = 0.921;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (0.9 + 0.4666666667) / 2.0; point[2] = 0.459; point[3] = 0.846;
		trueCurve.add(point);		
		point = new double[4]; point[0] = 0.5; point[1] = (0.75 + 0.3333333333) / 2.0; point[2] = 0.330; point[3] = 0.739;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (0.6 + 0.3111111111) / 2.0; point[2] = 0.259; point[3] = 0.667;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (0.48 + 0.2888888889) / 2.0; point[2] = 0.203; point[3] = 0.604;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (0.42 + 0.2666666667) / 2.0; point[2] = 0.173; point[3] = 0.566;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (0.36 + 0.2444444444) / 2.0; point[2] = 0.144; point[3] = 0.527;
		trueCurve.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (0.3 + 0.2222222222) / 2.0; point[2] = 0.117; point[3] = 0.486;
		trueCurve.add(point);
		resultcurve = aggPerformances.getPrecisionRecallCurve("s2");
		assertTrue(trueCurve.size() == resultcurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == resultcurve.get(i).length);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultcurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultcurve.get(i)[1]));
			assertTrue(doublesEqual( trueCurve.get(i)[2], (Math.round(resultcurve.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( trueCurve.get(i)[3], (Math.round(resultcurve.get(i)[3]*1000))/1000.0));
		}
		// s3
		trueCurve = new Vector<double[]>();
		point = new double[4]; point[0] = 0.0; point[1] = (1.0 + 1.0) / 2.0; point[2] = 0.832; point[3] = 1.0;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.1; point[1] = (1.0 + 0.85) / 2.0; point[2] = 0.724; point[3] = 0.983;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.2; point[1] = (1.0 + 0.7) / 2.0; point[2] = 0.633; point[3] = 0.949;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.3; point[1] = (1.0 + 0.55) / 2.0; point[2] = 0.551; point[3] = 0.906;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.4; point[1] = (1.0 + 0.48) / 2.0; point[2] = 0.515; point[3] = 0.884;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.5; point[1] = (1.0 + 0.45) / 2.0; point[2] = 0.500; point[3] = 0.874;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.6; point[1] = (1.0 + 0.42) / 2.0; point[2] = 0.485; point[3] = 0.864;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.7; point[1] = (0.93 + 0.3933333333) / 2.0; point[2] = 0.439; point[3] = 0.830;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.8; point[1] = (0.72 + 0.3733333333) / 2.0; point[2] = 0.335; point[3] = 0.743;
		trueCurve.add(point);
		point = new double[4]; point[0] = 0.9; point[1] = (0.51 + 0.3533333333) / 2.0; point[2] = 0.240; point[3] = 0.647;
		trueCurve.add(point);
		point = new double[4]; point[0] = 1.0; point[1] = (0.3 + 0.3333333333) / 2.0; point[2] = 0.154; point[3] = 0.541;
		trueCurve.add(point);
		resultcurve = aggPerformances.getPrecisionRecallCurve("s3");
		assertTrue(trueCurve.size() == resultcurve.size());
		for(int i = 0; i < trueCurve.size(); ++i) {
			assertTrue(trueCurve.get(i).length == resultcurve.get(i).length);
			assertTrue(doublesEqual( trueCurve.get(i)[0], resultcurve.get(i)[0]));
			assertTrue(doublesEqual( trueCurve.get(i)[1], resultcurve.get(i)[1]));
			assertTrue(doublesEqual( trueCurve.get(i)[2], (Math.round(resultcurve.get(i)[2]*1000))/1000.0));
			assertTrue(doublesEqual( trueCurve.get(i)[3], (Math.round(resultcurve.get(i)[3]*1000))/1000.0));
		}
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

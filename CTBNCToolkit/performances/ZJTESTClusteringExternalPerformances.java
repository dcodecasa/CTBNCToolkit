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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.junit.Test;

import CTBNCToolkit.CTBNClassifier;
import CTBNCToolkit.CTTrajectory;
import CTBNCToolkit.ClassificationResults;
import CTBNCToolkit.IClassificationResult;
import CTBNCToolkit.NodeIndexing;
import CTBNCToolkit.SufficientStatistics;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTClusteringExternalPerformances {

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#ClusteringExternalPerformances()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#datasetDimension()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#classesNumber()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#clusterNumber()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#setLearningTime(double)},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getLearningTime()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#setLearnedModel(CTBNCToolkit.ICTClassifier)},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getLearnedModel()}.
	 */
	@Test
	public void testClusteringExternalPerformances() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Collection<IClassificationResult<Double>> trjResults;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		assertTrue(performances.datasetDimension() == 0);
		assertTrue(performances.classesNumber() == nClasses);
		assertTrue(performances.clusterNumber() == nClusters);
		assertTrue(Double.isNaN( performances.getLearningTime()));
		assertTrue(performances.getLearnedModel() == null);
		
		performances.addResult(this.generateClustering(1, nClasses, nClusters).iterator().next());
		assertTrue(performances.datasetDimension() == 1);
		assertTrue(performances.classesNumber() == nClasses);
		assertTrue(performances.clusterNumber() == nClusters);
		performances.setLearningTime(1.32);
		assertTrue(this.dEqual( performances.getLearningTime(), 1.32));
		CTBNClassifier model1 = new CTBNClassifier(NodeIndexing.getNodeIndexing("generateClusteringResults"), "model1");
		performances.setLearnedModel( model1);
		assertTrue(performances.getLearnedModel().getName().equals(model1.getName()));
		
		CTBNClassifier model2 = new CTBNClassifier(NodeIndexing.getNodeIndexing("generateClusteringResults"), "model2");
		performances.setLearnedModel( model2);
		assertTrue(performances.getLearnedModel().getName().equals(model2.getName()));
		performances.setLearningTime(21.2);
		assertTrue(this.dEqual( performances.getLearningTime(), 21.2));
		trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		performances.addResults(trjResults);
		assertTrue(performances.datasetDimension() == (dataSize + 1));
		assertTrue(performances.classesNumber() == nClasses);
		assertTrue(performances.clusterNumber() == nClusters);
		assertTrue(this.dEqual( performances.getLearningTime(), 21.2));
		assertTrue(performances.getLearnedModel().getName().equals(model2.getName()));
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#indexToValue(int)},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#valueToIndex(java.lang.String)}.
	 */
	@Test
	public void testIndexToValue() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
				
		for( int i = 0; i < nClasses; ++i)
			assertTrue(performances.valueToIndex(performances.indexToValue(i)) == i);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#indexToCluster(int)},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#clusterToIndex(java.lang.String)}
	 */
	@Test
	public void testIndexToCluster() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);

		for( int i = 0; i < nClasses; ++i)
			assertTrue(performances.clusterToIndex(performances.indexToCluster(i)) == i);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test
	public void testInferenceTime1() {
		
		int dataSize = 6;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
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
		assertTrue( this.dEqual( performances.getVarianceInferenceTime(), Math.pow(0.2, 2)));
		
		performances.addResult(dataset.get(2), 1.0);
		assertTrue(performances.getInferenceTimes().size() == 3);
		assertTrue(performances.getAvgInferenceTime() == 1.0);
		assertTrue( this.dEqual( performances.getVarianceInferenceTime(), 2.0*Math.pow(0.2, 2)/3.0));
		
		performances.addResult(dataset.get(3), 1.6);
		assertTrue(performances.getInferenceTimes().size() == 4);
		assertTrue(performances.getAvgInferenceTime() == 4.6/4.0);
		assertTrue( this.dEqual( performances.getVarianceInferenceTime(), (Math.pow(1.2 - 4.6/4.0, 2) + Math.pow(0.8 - 4.6/4.0, 2) + Math.pow(1.0 - 4.6/4.0, 2) + Math.pow(1.6 - 4.6/4.0, 2))/4.0));
		
		performances.addResult(dataset.get(4));
		assertTrue( performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResult(dataset.get(5), 3.2);
		assertTrue(performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test
	public void testInferenceTime2() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.55);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		assertTrue(performances.getInferenceTimes().size() == 0);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResults(dataset, times);
		assertTrue(performances.getInferenceTimes().size() == 10);
		assertTrue( this.dEqual( performances.getAvgInferenceTime(), 1.0));
		double var = (2*Math.pow(0.2, 2) + 2*Math.pow(0.7, 2) + 2*Math.pow(0.95, 2) + 2*Math.pow(0.55, 2)) / 10.0;
		assertTrue( this.dEqual( performances.getVarianceInferenceTime(), var));
		
		performances.addResult(dataset.get(0));
		assertTrue(performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test
	public void testInferenceTime3() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.55);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		assertTrue(performances.getInferenceTimes().size() == 0);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
		
		performances.addResults(dataset, times);
		assertTrue( performances.getInferenceTimes().size() == 10);
		assertTrue( this.dEqual( performances.getAvgInferenceTime(), 1.0));
		double var = (2*Math.pow(0.2, 2) + 2*Math.pow(0.7, 2) + 2*Math.pow(0.95, 2) + 2*Math.pow(0.55, 2)) / 10.0;
		assertTrue( this.dEqual( performances.getVarianceInferenceTime(), var));
		
		performances.addResults(dataset, null);
		assertTrue( performances.getInferenceTimes() == null);
		assertTrue( Double.isNaN( performances.getAvgInferenceTime()));
		assertTrue( Double.isNaN( performances.getVarianceInferenceTime()));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException1() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		performances.addResult(dataset.get(0), -1.02);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException2() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(0.3);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(dataset, times);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getVarianceInferenceTime()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInferenceTimeException3() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		List<IClassificationResult<Double>> dataset = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		
		List<Double> times = new Vector<Double>(10);
		times.add(0.8);times.add(null);times.add(0.05);times.add(1.0);times.add(0.45);
		times.add(1.2);times.add(1.7);times.add(1.95);times.add(1.0);times.add(1.54);
		
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(dataset, times);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#addResults(java.util.Collection)}.
	 */
	@Test
	public void testAddResultsCollectionOfIClassificationResultOfTimeType() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		assertTrue(performances.datasetDimension() == 0);
		assertTrue(performances.getResultsTrajectories().size() == 0);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == 0);
		assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
		
		List<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		performances.addResults(trjResults);
		
		assertTrue(performances.datasetDimension() == dataSize);
		assertTrue(performances.getResultsTrajectories().size() == dataSize);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == dataSize);
		assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#addResults(java.util.List, java.util.List)}.
	 */
	@Test
	public void testAddResultsListOfIClassificationResultOfTimeTypeListOfDouble() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		List<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		List<Double> times = new LinkedList<Double>();
		for( int i = 0; i < trjResults.size(); ++i) {
			times.add((double) (i % 5));
		}
		
		assertTrue(performances.datasetDimension() == 0);
		assertTrue(performances.getResultsTrajectories().size() == 0);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == 0);
		assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
		
		performances.addResults(trjResults, times);
		
		assertTrue(performances.datasetDimension() == dataSize);
		assertTrue(performances.getResultsTrajectories().size() == dataSize);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == dataSize);
		assertTrue(performances.getAvgInferenceTime() == 2);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#addResult(CTBNCToolkit.IClassificationResult)}.
	 */
	@Test
	public void testAddResultIClassificationResultOfTimeType() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		List<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		for( int i = 0; i < trjResults.size(); ++i) {
			assertTrue(performances.datasetDimension() == i);
			assertTrue(performances.getResultsTrajectories().size() == i);
			assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
			performances.addResult(trjResults.get(i));
		}
		assertTrue(performances.datasetDimension() == dataSize);
		assertTrue(performances.getResultsTrajectories().size() == dataSize);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == dataSize);
		assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#addResult(CTBNCToolkit.IClassificationResult, double)}.
	 */
	@Test
	public void testAddResultIClassificationResultOfTimeTypeDouble() {
		
		int dataSize = 1000;
		int nClasses = 10;
		int nClusters = 15;
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		
		List<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		assertTrue(Double.isNaN( performances.getAvgInferenceTime()));
		for( int i = 0; i < trjResults.size(); ++i) {
			assertTrue(performances.datasetDimension() == i);
			assertTrue(performances.getResultsTrajectories().size() == i);
			performances.addResult(trjResults.get(i), i % 5);
		}
		assertTrue(performances.datasetDimension() == dataSize);
		assertTrue(performances.getResultsTrajectories().size() == dataSize);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == dataSize);
		assertTrue(performances.getAvgInferenceTime() == 2);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getResultsTrajectories()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#sortResultsTrajectoriesByNames()}.
	 */
	@Test
	public void testGetResultsTrajectories() {
		
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		indexToTrueState.put(0, "AAA");indexToTrueState.put(1, "BBB");indexToTrueState.put(2, "CCC");
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		indexToCluster.put(0, "C1");indexToCluster.put(0, "C2");
		
		ClassificationResults<Double> trj;
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("generateClusteringResults", names, names[0], null);
		// Times
		List<Double> times = new Vector<Double>();
		times.add(Math.random());
		// Values
		List<String[]> values = new Vector<String[]>();
		String[] v = new String[1];
		v[0] = "1";
		values.add(v);
		
		// Empty performances
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		assertTrue(performances.getResultsTrajectories().isEmpty());
		assertTrue(performances.sortResultsTrajectoriesByNames().isEmpty());
		
		// Generate trajectory BBB
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("BBB"); trj.setClassification("C1");
		performances.addResult(trj);
		assertTrue(performances.getResultsTrajectories().size() == 1);
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == 1);
		assertTrue(performances.getResultsTrajectories().get(0).getName().equals("BBB"));
		assertTrue(performances.sortResultsTrajectoriesByNames().get(0).getName().equals("BBB"));
		
		// Generate trajectory CCC
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("CCC"); trj.setClassification("C1");
		performances.addResult(trj);		
		assertTrue(performances.getResultsTrajectories().size() == 2);
		assertTrue(performances.getResultsTrajectories().get(0).getName().equals("BBB"));
		assertTrue(performances.getResultsTrajectories().get(1).getName().equals("CCC"));
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == 2);
		assertTrue(performances.sortResultsTrajectoriesByNames().get(0).getName().equals("BBB"));
		assertTrue(performances.sortResultsTrajectoriesByNames().get(1).getName().equals("CCC"));
		
		// Generate trajectory AAA
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("AAA"); trj.setClassification("C2");
		performances.addResult(trj);		
		assertTrue(performances.getResultsTrajectories().size() == 3);
		assertTrue(performances.getResultsTrajectories().get(0).getName().equals("BBB"));
		assertTrue(performances.getResultsTrajectories().get(1).getName().equals("CCC"));
		assertTrue(performances.getResultsTrajectories().get(2).getName().equals("AAA"));
		assertTrue(performances.sortResultsTrajectoriesByNames().size() == 3);
		assertTrue(performances.sortResultsTrajectoriesByNames().get(0).getName().equals("AAA"));
		assertTrue(performances.sortResultsTrajectoriesByNames().get(1).getName().equals("BBB"));
		assertTrue(performances.sortResultsTrajectoriesByNames().get(2).getName().equals("CCC"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getClustersPartitionsMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getPrecisionMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getRecallMatrix()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getFMeasureMatrix()}.
	 */
	@Test
	public void testGetClustersPartitionsMatrix1() {

		int dataSize = 100;
		int nClasses = 3;
		int nClusters = 3;
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
		
		double[][] counts = performances.getClustersPartitionsMatrix();
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("0")] == 34); 
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("1")] == 0); 
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("2")] == 0);
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("0")] == 0); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("1")] == 33); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("2")] == 0);
		assertTrue( counts[performances.clusterToIndex("2")][performances.valueToIndex("0")] == 0); 
		assertTrue( counts[performances.clusterToIndex("2")][performances.valueToIndex("1")] == 0); 
		assertTrue( counts[performances.clusterToIndex("2")][performances.valueToIndex("2")] == 33);
		
		double[][] p = performances.getPrecisionMatrix();
		assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("0")], 1.0)); assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("1")], 1.0)); assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( p[performances.clusterToIndex("2")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( p[performances.clusterToIndex("2")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( p[performances.clusterToIndex("2")][performances.valueToIndex("2")], 1.0));
		
		double[][] r = performances.getRecallMatrix();
		assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("0")], 1.0)); assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("1")], 1.0)); assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( r[performances.clusterToIndex("2")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( r[performances.clusterToIndex("2")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( r[performances.clusterToIndex("2")][performances.valueToIndex("2")], 1.0));
		
		double[][] f = performances.getFMeasureMatrix();
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("0")], 1.0)); assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("1")], 1.0)); assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("2")], 0.0));
		assertTrue(dEqual( f[performances.clusterToIndex("2")][performances.valueToIndex("0")], 0.0)); assertTrue(dEqual( f[performances.clusterToIndex("2")][performances.valueToIndex("1")], 0.0)); assertTrue(dEqual( f[performances.clusterToIndex("2")][performances.valueToIndex("2")], 1.0));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getClustersPartitionsMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getPrecisionMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getRecallMatrix()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getFMeasureMatrix()}.
	 */
	@Test
	public void testGetClustersPartitionsMatrix2() {
		
		int dataSize = 100;
		int nClasses = 3;
		int nClusters = 2;
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
		
		double[][] counts = performances.getClustersPartitionsMatrix();
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("0")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("1")] == 1*(int)(dataSize/(2*3))); 
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("2")] == 1*(int)(dataSize/(2*3)) + 1);
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("0")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("1")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("2")] == 1*(int)(dataSize/(2*3)));
	
		double[][] p = performances.getPrecisionMatrix();
		assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("0")], 17/(17.0+16.0+17.0))); assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("1")], 16/(17.0+16.0+17.0))); assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("2")], 17/(17.0+16.0+17.0)));
		assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("0")], 17/(17.0+16.0+17.0))); assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("1")], 17/(17.0+16.0+17.0))); assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("2")], 16/(17.0+16.0+17.0)));
		
		double[][] r = performances.getRecallMatrix();
		assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("0")], 17/(17.0*2))); assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("1")], 16/(17.0+16.0))); assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("2")], 17/(17.0+16.0)));
		assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("0")], 17/(17.0*2))); assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("1")], 17/(17.0+16.0))); assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("2")], 16/(17.0+16.0)));
		
		double[][] f = performances.getFMeasureMatrix();
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("0")], 2*17/(17.0*2)*17/(17.0+16.0+17.0)/(17/(17.0*2)+17/(17.0+16.0+17.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("1")], 2*16/(17.0+16.0+17.0)*16/(17.0+16.0)/(16/(17.0+16.0+17.0)+16/(17.0+16.0)))); 
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("2")], 2*17/(17.0+16.0+17.0)*17/(17.0+16.0)/(17/(17.0+16.0+17.0)+17/(17.0+16.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("0")], 2*17/(17.0*2)*17/(17.0+16.0+17.0)/(17/(17.0*2)+17/(17.0+16.0+17.0)))); 
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("1")], 2*17/(17.0+16.0+17.0)*17/(17.0+16.0)/(17/(17.0+16.0+17.0)+17/(17.0+16.0)))); 
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("2")], 2*16/(17.0+16.0+17.0)*16/(17.0+16.0)/(16/(17.0+16.0+17.0)+16/(17.0+16.0))));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getClustersPartitionsMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getPrecisionMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getRecallMatrix()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getFMeasureMatrix()}.
	 */
	@Test
	public void testGetClustersPartitionsMatrix3() {
		
		int dataSize = 100;
		int nClasses = 2;
		int nClusters = 3;
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
		
		double[][] counts = performances.getClustersPartitionsMatrix();
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("0")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("1")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("2")][performances.valueToIndex("0")] == 1*(int)(dataSize/(2*3)) + 1);
		assertTrue( counts[performances.clusterToIndex("0")][performances.valueToIndex("1")] == 1*(int)(dataSize/(2*3)) + 1); 
		assertTrue( counts[performances.clusterToIndex("1")][performances.valueToIndex("0")] == 1*(int)(dataSize/(2*3))); 
		assertTrue( counts[performances.clusterToIndex("2")][performances.valueToIndex("1")] == 1*(int)(dataSize/(2*3)));
		
		double[][] p = performances.getPrecisionMatrix();
		assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("0")], 17/(17.0*2))); assertTrue(dEqual( p[performances.clusterToIndex("0")][performances.valueToIndex("1")], 17/(17.0*2)));
		assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("0")], 16/(17.0+16.0))); assertTrue(dEqual( p[performances.clusterToIndex("1")][performances.valueToIndex("1")], 17/(17.0+16.0)));
		assertTrue(dEqual( p[performances.clusterToIndex("2")][performances.valueToIndex("0")], 17/(17.0+16.0))); assertTrue(dEqual( p[performances.clusterToIndex("2")][performances.valueToIndex("1")], 16/(17.0+16.0)));
		
		double[][] r = performances.getRecallMatrix();
		assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("0")], 17/(17.0+16.0+17.0))); assertTrue(dEqual( r[performances.clusterToIndex("0")][performances.valueToIndex("1")], 17/(17.0+16.0+17.0)));
		assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("0")], 16/(17.0+16.0+17.0))); assertTrue(dEqual( r[performances.clusterToIndex("1")][performances.valueToIndex("1")], 17/(17.0+16.0+17.0)));
		assertTrue(dEqual( r[performances.clusterToIndex("2")][performances.valueToIndex("0")], 17/(17.0+16.0+17.0))); assertTrue(dEqual( r[performances.clusterToIndex("2")][performances.valueToIndex("1")], 16/(17.0+16.0+17.0)));
		
		double[][] f = performances.getFMeasureMatrix();
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("0")], 2*17/(17.0*2)*17/(17.0+16.0+17.0)/(17/(17.0*2)+17/(17.0+16.0+17.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("0")][performances.valueToIndex("1")], 2*17/(17.0*2)*17/(17.0+16.0+17.0)/(17/(17.0*2)+17/(17.0+16.0+17.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("0")], 2*16/(17.0+16.0)*16/(17.0+16.0+17.0)/(16/(17.0+16.0)+16/(17.0+16.0+17.0)))); 
		assertTrue(dEqual( f[performances.clusterToIndex("1")][performances.valueToIndex("1")], 2*17/(17.0+16.0)*17/(17.0+16.0+17.0)/(17/(17.0+16.0)+17/(17.0+16.0+17.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("2")][performances.valueToIndex("0")], 2*17/(17.0+16.0)*17/(17.0+16.0+17.0)/(17/(17.0+16.0)+17/(17.0+16.0+17.0))));
		assertTrue(dEqual( f[performances.clusterToIndex("2")][performances.valueToIndex("1")], 2*16/(17.0+16.0)*16/(17.0+16.0+17.0)/(16/(17.0+16.0)+16/(17.0+16.0+17.0))));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAssociationMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getRandStatistic()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getJaccardCoefficient()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getFolkesMallowsIndex()}.
	 */
	@Test
	public void testGetFolkesMallowsIndex1() {
		
		int dataSize = 10;
		int nClasses = 2;
		int nClusters = 2;
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
		
		double[][] mx = performances.getAssociationMatrix();
		assertTrue(mx.length == 2);
		assertTrue(mx[0].length == 2);
		assertTrue(mx[1].length == 2);
		assertTrue(mx[0][0] + mx[0][1] + mx[1][0] + mx[1][1] == dataSize*(dataSize - 1)/2);
		assertTrue(mx[0][0] == 10*2);
		assertTrue(mx[0][1] == 0);
		assertTrue(mx[1][0] == 0);
		assertTrue(mx[1][1] == 25);
		
		assertTrue(performances.getRandStatistic() == (mx[0][0] + mx[1][1])/((double) dataSize*(dataSize - 1)/2));
		assertTrue(performances.getJaccardCoefficient() == mx[0][0] / ((double) mx[0][0] + mx[0][1] + mx[1][0]));
		assertTrue(performances.getFolkesMallowsIndex() == Math.sqrt( (mx[0][0]/(mx[0][0]+mx[0][1])) * (mx[0][0]/(mx[0][0]+mx[1][0])) ));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getAssociationMatrix()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getRandStatistic()},
	 * for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getJaccardCoefficient()},
	 * and for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getFolkesMallowsIndex()}.
	 */
	@Test
	public void testGetFolkesMallowsIndex2() {
		
		int dataSize = 6;
		int nClasses = 2;
		int nClusters = 3;
		Collection<IClassificationResult<Double>> trjResults = this.generateClustering(dataSize, nClasses, nClusters);
		Map<Integer,String> indexToTrueState = new TreeMap<Integer,String>();
		for(int i = 0; i < nClasses; ++i)
			indexToTrueState.put(i, "" + i);
		Map<Integer,String> indexToCluster = new TreeMap<Integer,String>();
		for(int i = 0; i < nClusters; ++i)
			indexToCluster.put(i, "" + i);
		ClusteringExternalPerformances<Double> performances = new ClusteringExternalPerformances<Double>( indexToTrueState, indexToCluster);
		performances.addResults(trjResults);
		
		double[][] mx = performances.getAssociationMatrix();
		assertTrue(mx.length == 2);
		assertTrue(mx[0].length == 2);
		assertTrue(mx[1].length == 2);
		assertTrue(mx[0][0] + mx[0][1] + mx[1][0] + mx[1][1] == dataSize*(dataSize - 1)/2);
		assertTrue(mx[0][0] == 0);
		assertTrue(mx[0][1] == 3);
		assertTrue(mx[1][0] == 6);
		assertTrue(mx[1][1] == 6);
		
		assertTrue(performances.getRandStatistic() == (mx[0][0] + mx[1][1])/((double) dataSize*(dataSize - 1)/2));
		assertTrue(performances.getJaccardCoefficient() == mx[0][0] / ((double) mx[0][0] + mx[0][1] + mx[1][0]));
		assertTrue(performances.getFolkesMallowsIndex() == Math.sqrt( (mx[0][0]/(mx[0][0]+mx[0][1])) * (mx[0][0]/(mx[0][0]+mx[1][0])) ));
	}
	
	
	/**
	 * Generate a clusterization with
	 * N trajectories classified with the
	 * formula i%nClasses for the real class
	 * and i%nClusters for the clustering
	 * results. Each trajectory has name i,
	 * where i is the counter from 0 to N-1. 
	 * 
	 * @param nDataset dataSize dimension
	 * @param nClasses number of classes
	 * @param nClusters number of clusters
	 * @return set of clusterized trajectories
	 */
	private List<IClassificationResult<Double>> generateClustering(int dataSize, int nClasses, int nClusters) {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("generateClusteringResults", names, names[0], null);
		
		// Generate the classified trajectories
		List<IClassificationResult<Double>> trajectories = new Vector<IClassificationResult<Double>>();
		for(int i = 0; i < dataSize; ++i) {
			// Times
			List<Double> times = new Vector<Double>();
			times.add(Math.random());
			// Values
			List<String[]> values = new Vector<String[]>();
			String[] v = new String[1];
			v[0] = "" + (i % nClasses);
			values.add(v);
			// Generate trajectories
			trajectories.add( new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values)));
			trajectories.get(i).setName("" + i);
			trajectories.get(i).setClassification("" + (i % nClusters));
		}
		// Generate sufficient statistics
		SufficientStatistics[] sStats = new SufficientStatistics[2];
		sStats[0] = null; sStats[1] = null;
		
		return trajectories;
	}

	
	/**
	 * Compares 2 double values.
	 * 
	 * @param d1 double value 1
	 * @param d2 double value 2
	 * @return true if they are equal, false otherwise.
	 */
	boolean dEqual(double d1, double d2) {
		
		double eps = 0.00001;
		
		return (((d1-eps) < d2) && ((d1+eps) > d2)); 
	}
}
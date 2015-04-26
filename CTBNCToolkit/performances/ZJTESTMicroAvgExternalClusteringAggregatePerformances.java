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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.junit.Test;

import CTBNCToolkit.CTTrajectory;
import CTBNCToolkit.ClassificationResults;
import CTBNCToolkit.IClassificationResult;
import CTBNCToolkit.NodeIndexing;
import CTBNCToolkit.SufficientStatistics;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTMicroAvgExternalClusteringAggregatePerformances {

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#datasetDimension()},
	 * for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#clusterNumber()},
	 * and for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#classesNumber()}.
	 */
	@Test
	public void testDatasetDimension() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		assertTrue( aggPerformances.classesNumber() == 2);
		assertTrue( aggPerformances.clusterNumber() == 2);
		assertTrue( aggPerformances.datasetDimension() == 9);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getLearningTime()},
	 * for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getVarianceLearningTime()},
	 * for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getAvgInferenceTime()},
	 * and for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getVarianceInferenceTime()}.
	 */
	@Test
	public void testGetLearningTime() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		// testGetLearningTime
		assertTrue( this.dEqual( aggPerformances.getLearningTime(), 1.5));
		// testGetVarianceLearningTime
		assertTrue( this.dEqual( aggPerformances.getVarianceLearningTime(), Math.pow(0.5,2)));
		// testGetAvgInferenceTime
		assertTrue( this.dEqual( aggPerformances.getAvgInferenceTime(), 16/9.0));
		// testGetVarianceInferenceTime
		assertTrue( this.dEqual( aggPerformances.getVarianceInferenceTime(), (2*Math.pow(1.0-16/9.0, 2)+7*Math.pow(2.0-16/9.0, 2)) / 9.0));
	}
	

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#setLearnedModel(CTBNCToolkit.ICTClassifier)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetLearnedModel() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		aggPerformances.setLearnedModel( null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getLearnedModel()}.
	 */
	@Test(expected = RuntimeException.class)
	public void testGetLearnedModel() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		aggPerformances.getLearnedModel();
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#setLearningTime(double)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetLearningTime() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		aggPerformances.setLearningTime( -10.0);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#indexToValue(int)},
	 * and for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#valueToIndex(java.lang.String)}.
	 */
	@Test
	public void testIndexToValue() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		for(int i = 0; i < aggPerformances.classesNumber(); ++i)
			assertTrue( aggPerformances.valueToIndex( aggPerformances.indexToValue( i)) == i);
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#indexToCluster(int)},
	 * and for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#clusterToIndex(java.lang.String)}.
	 */
	@Test
	public void testIndexToCluster() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		for(int i = 0; i < aggPerformances.classesNumber(); ++i)
			assertTrue( aggPerformances.clusterToIndex( aggPerformances.indexToCluster( i)) == i);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getClustersPartitionsMatrix()}.
	 */
	@Test
	public void testGetClustersPartitionsMatrix() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		double[][] mx = aggPerformances.getClustersPartitionsMatrix();
	
		assertTrue( this.dEqual( mx[0][0], 1.5));
		assertTrue( this.dEqual( mx[0][1], 0.5));
		assertTrue( this.dEqual( mx[1][0], 1));
		assertTrue( this.dEqual( mx[1][1], 1.5));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getPrecisionMatrix()}.
	 */
	@Test
	public void testGetPrecisionMatrix() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		double[][] mx = aggPerformances.getPrecisionMatrix();
	
		assertTrue( this.dEqual( mx[0][0], 3/4.0));
		assertTrue( this.dEqual( mx[0][1], 1/4.0));
		assertTrue( this.dEqual( mx[1][0], 2/5.0));
		assertTrue( this.dEqual( mx[1][1], 3/5.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getRecallMatrix()}.
	 */
	@Test
	public void testGetRecallMatrix() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		double[][] mx = aggPerformances.getRecallMatrix();
	
		assertTrue( this.dEqual( mx[0][0], 3/5.0));
		assertTrue( this.dEqual( mx[0][1], 1/4.0));
		assertTrue( this.dEqual( mx[1][0], 2/5.0));
		assertTrue( this.dEqual( mx[1][1], 3/4.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getFMeasureMatrix()}.
	 */
	@Test
	public void testGetFMeasureMatrix() {

		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		double[][] mx = aggPerformances.getFMeasureMatrix();
	
		assertTrue( this.dEqual( mx[0][0], 2*(3/4.0*3/5.0)/(3/4.0+3/5.0)));
		assertTrue( this.dEqual( mx[0][1], 2*(1/4.0*1/4.0)/(1/4.0+1/4.0)));
		assertTrue( this.dEqual( mx[1][0], 2*(2/5.0*2/5.0)/(2/5.0+2/5.0)));
		assertTrue( this.dEqual( mx[1][1], 2*(3/5.0*3/4.0)/(3/5.0+3/4.0)));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getAssociationMatrix()}.
	 */
	@Test
	public void testGetAssociationMatrix() {

		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		double[][] mx = aggPerformances.getAssociationMatrix();
	
		assertTrue( this.dEqual( mx[0][0], 1.5));
		assertTrue( this.dEqual( mx[0][1], 1.5));
		assertTrue( this.dEqual( mx[1][0], 1.5));
		assertTrue( this.dEqual( mx[1][1], 3.5));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getRandStatistic()}.
	 */
	@Test
	public void testGetRandStatistic() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		assertTrue( this.dEqual( aggPerformances.getRandStatistic(), 5/8.0));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getJaccardCoefficient()}.
	 */
	@Test
	public void testGetJaccardCoefficient() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		assertTrue( this.dEqual( aggPerformances.getJaccardCoefficient(), 1.5/4.5));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MacroAvgExternalClusteringAggregatePerformances#getFolkesMallowsIndex()}.
	 */
	@Test
	public void testGetFolkesMallowsIndex() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		assertTrue( this.dEqual( aggPerformances.getFolkesMallowsIndex(), Math.sqrt(1.5/3.0*1.5/3.0)));
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.MicroAvgExternalClusteringAggregatePerformances#addPerformances(java.util.Collection)},
	 * for {@link CTBNCToolkit.performances.MicroAvgExternalClusteringAggregatePerformances#addPerformances(CTBNCToolkit.performances.IExternalClusteringSingleRunPerformances)} and,
	 * for {@link CTBNCToolkit.performances.MicroAvgExternalClusteringAggregatePerformances#getPerformances()}.
	 */
	@Test
	public void testAddPerformancesPType() {
		
		Map<Integer,String> statesMap = new TreeMap<Integer,String>(); statesMap.put(0, "cla1"); statesMap.put(1, "cla2");
		Map<Integer,String> clustersMap = new TreeMap<Integer,String>(); clustersMap.put(0, "clu1"); clustersMap.put(1, "clu2");
		
		// Single inserting
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> performances =
				new MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>>( statesMap, clustersMap);
		
		ClusteringExternalPerformances<Double> singleRun1 = new ClusteringExternalPerformances<Double>( statesMap, clustersMap);
		List<Double> times = new Vector<Double>(); times.add(1.0); times.add(1.0); times.add(2.0); times.add(2.0);
		singleRun1.addResults(this.generateClustering1(), times);
		singleRun1.setLearningTime( 1.0);
		performances.addPerformances(singleRun1);
		
		ClusteringExternalPerformances<Double> singleRun2 = new ClusteringExternalPerformances<Double>( statesMap, clustersMap);
		times = new Vector<Double>(); times.add(2.0); times.add(2.0); times.add(2.0);  times.add(2.0); times.add(2.0);
		singleRun2.addResults(this.generateClustering2(), times);
		singleRun2.setLearningTime( 2.0);
		performances.addPerformances(singleRun2);
		// Evaluation
		Iterator<ClusteringExternalPerformances<Double>> pIterator = performances.getPerformances().iterator();
		assertTrue( this.dEqual( pIterator.next().getLearningTime(), 1.0));
		assertTrue( this.dEqual( pIterator.next().getLearningTime(), 2.0));
		
		
		// Collection inserting
		List<ClusteringExternalPerformances<Double>> pList = new LinkedList<ClusteringExternalPerformances<Double>>();
		pList.add(singleRun1);pList.add(singleRun2);
		performances = new MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>>( statesMap, clustersMap);
		performances.addPerformances(pList);
		// Evaluation
		pIterator = performances.getPerformances().iterator();
		assertTrue( this.dEqual( pIterator.next().getLearningTime(), 1.0));
		assertTrue( this.dEqual( pIterator.next().getLearningTime(), 2.0));
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.performances.ClusteringExternalPerformances#getResultsTrajectories()}.
	 */
	@Test
	public void testGetResultsTrajectories() {
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> aggPerformances = generatePerformances();
		Iterator<IClassificationResult<Double>> rTrjIter = aggPerformances.getResultsTrajectories().iterator();
		assertTrue(rTrjIter.next().getName().equals("1"));
		assertTrue(rTrjIter.next().getName().equals("2"));
		assertTrue(rTrjIter.next().getName().equals("3"));
		assertTrue(rTrjIter.next().getName().equals("4"));
		assertTrue(rTrjIter.next().getName().equals("1"));
		assertTrue(rTrjIter.next().getName().equals("2"));
		assertTrue(rTrjIter.next().getName().equals("3"));
		assertTrue(rTrjIter.next().getName().equals("4"));
		assertTrue(rTrjIter.next().getName().equals("5"));
	}

	
	/**
	 * Generate and aggregate performances.
	 * 
	 * @return aggregate performances
	 */
	private MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> generatePerformances() {
		
		Map<Integer,String> statesMap = new TreeMap<Integer,String>(); statesMap.put(0, "cla1"); statesMap.put(1, "cla2");
		Map<Integer,String> clustersMap = new TreeMap<Integer,String>(); clustersMap.put(0, "clu1"); clustersMap.put(1, "clu2");
		
		MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>> performances =
				new MicroAvgExternalClusteringAggregatePerformances<Double,ClusteringExternalPerformances<Double>>( statesMap, clustersMap);
		
		ClusteringExternalPerformances<Double> singleRun = new ClusteringExternalPerformances<Double>( statesMap, clustersMap);
		List<Double> times = new Vector<Double>(); times.add(1.0); times.add(1.0); times.add(2.0); times.add(2.0);
		singleRun.addResults(this.generateClustering1(), times);
		singleRun.setLearningTime( 1.0);
		performances.addPerformances(singleRun);
		
		singleRun = new ClusteringExternalPerformances<Double>( statesMap, clustersMap);
		times = new Vector<Double>(); times.add(2.0); times.add(2.0); times.add(2.0);  times.add(2.0); times.add(2.0);
		singleRun.addResults(this.generateClustering2(), times);
		singleRun.setLearningTime( 2.0);
		performances.addPerformances(singleRun);
		
		return performances;
	}
	
	/**
	 * Generate 4 perfect clusterized trajectories.
	 * 
	 * @return set of clusterized trajectories
	 */
	private List<IClassificationResult<Double>> generateClustering1() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("generateClusteringResults", names, names[0], null);
		
		// Generate sufficient statistics
		SufficientStatistics[] sStats = new SufficientStatistics[2];
		sStats[0] = null; sStats[1] = null;
				
		// Generate the classified trajectories
		ClassificationResults<Double> trj;
		List<IClassificationResult<Double>> trajectories = new Vector<IClassificationResult<Double>>();
		// Times
		List<Double> times = new Vector<Double>();
		times.add(Math.random());
		
		// trj1
		// Values
		List<String[]> values = new Vector<String[]>();
		String[] v = new String[1];
		v[0] = "cla1";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("1");
		trj.setClassification("clu1");
		trajectories.add( trj);
		
		// trj2
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla1";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("2");
		trj.setClassification("clu1");
		trajectories.add( trj);
		
		// trj3
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla2";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("3");
		trj.setClassification("clu2");
		trajectories.add( trj);
		
		// trj4
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla2";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("4");
		trj.setClassification("clu2");
		trajectories.add( trj);
		
		return trajectories;
	}

	/**
	 * Generate 4 not well clusterized trajectories.
	 * 
	 * @return set of clusterized trajectories
	 */
	private List<IClassificationResult<Double>> generateClustering2() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("generateClusteringResults", names, names[0], null);
		
		// Generate sufficient statistics
		SufficientStatistics[] sStats = new SufficientStatistics[2];
		sStats[0] = null; sStats[1] = null;
				
		// Generate the classified trajectories
		ClassificationResults<Double> trj;
		List<IClassificationResult<Double>> trajectories = new Vector<IClassificationResult<Double>>();
		// Times
		List<Double> times = new Vector<Double>();
		times.add(Math.random());
		
		// trj1
		// Values
		List<String[]> values = new Vector<String[]>();
		String[] v = new String[1];
		v[0] = "cla1";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("1");
		trj.setClassification("clu1");
		trajectories.add( trj);
		
		// trj2
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla1";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("2");
		trj.setClassification("clu2");
		trajectories.add( trj);
		
		// trj3
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla2";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("3");
		trj.setClassification("clu2");
		trajectories.add( trj);
		
		// trj4
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla2";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("4");
		trj.setClassification("clu1");
		trajectories.add( trj);
		
		
		// trj5
		// Values
		values = new Vector<String[]>();
		v = new String[1];
		v[0] = "cla1";
		values.add(v);
		// Generate trajectories
		trj = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		trj.setName("5");
		trj.setClassification("clu2");
		trajectories.add( trj);
		
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
	
	/**
	 * Print the matrix in input.
	 * 
	 * @param counts integer matrix
	 */
	@SuppressWarnings("unused")
	private void printMatrix(double[][] mx) {
		
		for( int i = 0; i < mx.length; ++i) {
			for( int j = 0; j < mx[0].length; ++j)
				System.out.print("[" + mx[i][j] + "]");
			System.out.println();
		}
	}
}

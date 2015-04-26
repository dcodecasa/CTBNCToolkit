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

import java.util.*;

import org.junit.Test;

import CTBNCToolkit.*;
import CTBNCToolkit.performances.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTGenericTestResults {

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#GenericTestResults(java.lang.String, java.util.List, CTBNCToolkit.performances.IClassificationPerformances)},
	 * for {@link CTBNCToolkit.tests.GenericTestResults#getModelName()}, for {@link CTBNCToolkit.tests.GenericTestResults#getTrueModel()},
	 * for {@link CTBNCToolkit.tests.GenericTestResults#getDataset()}, and for {@link CTBNCToolkit.tests.GenericTestResults#getPerformances()}.
	 */
	@Test
	public void testGenericTestResultsStringListOfITrajectoryOfTimeTypeNodeValueTypePType() {
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		assertTrue(testResults.getModelName().equals("Model1"));
		assertTrue(testResults.getDataset().size() == 19);
		assertTrue(testResults.getTrueModel() == null);
		assertTrue( doublesEqual(testResults.getPerformances().getMacroAveraging().getAccuracy(), (0.7+4.0/9.0)/2.0));
		assertTrue( doublesEqual(testResults.getPerformances().getMicroAveraging().getAccuracy(), 5.5/9.5));
	}



	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#resultsToString(java.lang.String)}.
	 */
	@Test
	public void testResultsToStringString() {
		System.out.println("ZJTESTGenericTestResults.testResultsToStringString");
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(testResults.resultsToString("class"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#resultsToString(CTBNCToolkit.performances.ISingleRunPerformances, java.lang.String)}.
	 */
	@Test
	public void testResultsToStringISingleRunPerformancesOfTimeTypeNodeValueTypeString() {
		System.out.println("ZJTESTGenericTestResults.testResultsToStringISingleRunPerformancesOfTimeTypeNodeValueTypeString");
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(GenericTestResults.resultsToString(testResults.getPerformances().getMacroAveraging().getPerformances().iterator().next()));
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#metricsToString(java.util.Set, java.lang.String, int, java.lang.String)},
	 * and Test method for {@link CTBNCToolkit.tests.GenericTestResults#metricsHeader(java.util.Set)}.
	 */
	@Test
	public void testMetricsToStringSetOfNodeValueTypeStringIntString() {		
		System.out.println("ZJTESTGenericTestResults.testMetricsToStringSetOfNodeValueTypeStringIntString");
		
		Set<String> classStates = new TreeSet<String>(); classStates.add("s1");classStates.add("s2");classStates.add("s3");
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.metricsHeader(classStates) + "\n" +
				testResults.metricsToString(classStates, "90%", testResults.getDataset().size(), "JTest", 2)
				);
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#performancesSummary(java.lang.String, java.lang.String, int)},
	 * and Test method for {@link CTBNCToolkit.tests.GenericTestResults#performacesClassificationSummaryHeader()}.
	 */
	@Test
	public void testPerformancesSummaryStringStringInt() {
		System.out.println("ZJTESTGenericTestResults.testPerformancesSummaryStringStringInt");
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.performacesSummaryHeader(false) + "\n" +
				testResults.performancesSummary("JTest", "90%", testResults.getDataset().size(), 2)
				);
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#printROC(CTBNCToolkit.performances.MacroAvgAggregatePerformances, java.lang.Object, java.lang.String)},
	 * and Test method for {@link CTBNCToolkit.tests.GenericTestResults#ROCHeader(boolean)}.
	 */
	@Test
	public void testPrintROCMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeStringException() {
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.printROC(testResults.getPerformances().getMacroAveraging(), "s4", "90%")
				);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#printROC(CTBNCToolkit.performances.MacroAvgAggregatePerformances, java.lang.Object, java.lang.String)},
	 * and Test method for {@link CTBNCToolkit.tests.GenericTestResults#ROCHeader(boolean)}.
	 */
	@Test
	public void testPrintROCMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeString() {
		System.out.println("ZJTESTGenericTestResults.testPrintROCMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeString");
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.printROC(testResults.getPerformances().getMacroAveraging(), "s1", "90%")
				);
	}

	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#printPRCurve(CTBNCToolkit.performances.MacroAvgAggregatePerformances, java.lang.Object, java.lang.String)},
	 * and for {@link CTBNCToolkit.tests.GenericTestResults#PRHeader(boolean)}.
	 */
	@Test
	public void testPrintPRCurveMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeStringException() {
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.printPRCurve(testResults.getPerformances().getMacroAveraging(), "s4", "90%")
				);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#printPRCurve(CTBNCToolkit.performances.MacroAvgAggregatePerformances, java.lang.Object, java.lang.String)},
	 * and for {@link CTBNCToolkit.tests.GenericTestResults#PRHeader(boolean)}.
	 */
	@Test
	public void testPrintPRCurveMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeString() {
		
		System.out.println("ZJTESTGenericTestResults.testPrintPRCurveMacroAvgAggregatePerformancesOfTimeTypeNodeValueTypeNodeValueTypeString");
		
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = generateTestResults();
		System.out.println(
				GenericTestResults.printPRCurve(testResults.getPerformances().getMacroAveraging(), "s3", "90%")
				);
	}


	/**
	 * Test method for {@link CTBNCToolkit.tests.GenericTestResults#compareMethods(java.util.List)}.
	 */
	@Test
	public void testCompareMethodsListOfGenericTestResultsOfTimeTypeNodeValueTypeNodeTypeTMMicroMacroPerformancesContainerOfTimeTypeNodeValueType() {
		
		System.out.println("ZJTESTGenericTestResults.testCompareMethodsListOfGenericTestResultsOfTimeTypeNodeValueTypeNodeTypeTMMicroMacroPerformancesContainerOfTimeTypeNodeValueType");
		
		
		List<GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>>> resultsList =
				new Vector<GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>>>(3);
		resultsList.add( generateTestResults());
		resultsList.add( generateTestResults());
		resultsList.add( generateTestResults2());
		resultsList.add( generateTestResults2());
		System.out.println(
				GenericTestResults.compareMethods(resultsList)
				);
	}
	
	
	/**
	 * Generate the test results from
	 * two examples datasets.
	 * 
	 * @return test results from 2 examples dataset
	 */
	private GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> generateTestResults() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		List<IClassificationResult<Double>> dataset1 = generateDataset1();
		performances1.addResults(dataset1);
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		List<IClassificationResult<Double>> dataset2 = generateDataset2();
		performances2.addResults(dataset2);
		
		MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>> aggPerformances = new MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		List<ITrajectory<Double>> dataset = new LinkedList<ITrajectory<Double>>();
		for( int i = 0; i < dataset1.size(); ++i)
			dataset.add(dataset1.get(i));
		for( int i = 0; i < dataset2.size(); ++i)
			dataset.add(dataset2.get(i));
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = 
				new GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>>(
						"Model1", dataset, aggPerformances);
		
		return testResults;
		
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
	
	/**
	 * Generate the test results from
	 * two examples datasets.
	 * 
	 * @return test results from 2 examples dataset
	 */
	private GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> generateTestResults2() {
		
		Map<Integer, String> indexToState = new TreeMap<Integer,String>(); indexToState.put(0, "s1"); indexToState.put(1, "s2");  indexToState.put(2, "s3");
		Map<Integer, String> indexToStateAgg = new TreeMap<Integer,String>(); indexToStateAgg.put(2, "s1"); indexToStateAgg.put(0, "s2");  indexToStateAgg.put(1, "s3");
		
		ClassificationStandardPerformances<Double> performances1 = new ClassificationStandardPerformances<Double>(indexToState);
		List<IClassificationResult<Double>> dataset1 = generateDataset3();
		performances1.addResults(dataset1);
		ClassificationStandardPerformances<Double> performances2 = new ClassificationStandardPerformances<Double>(indexToState);
		List<IClassificationResult<Double>> dataset2 = generateDataset4();
		performances2.addResults(dataset2);
		
		MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>> aggPerformances = new MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>(indexToStateAgg);
		aggPerformances.addPerformances(performances1);
		aggPerformances.addPerformances(performances2);
		
		List<ITrajectory<Double>> dataset = new LinkedList<ITrajectory<Double>>();
		for( int i = 0; i < dataset1.size(); ++i)
			dataset.add(dataset1.get(i));
		for( int i = 0; i < dataset2.size(); ++i)
			dataset.add(dataset2.get(i));
		GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>> testResults = 
				new GenericTestResults<Double,CTDiscreteNode,CTBNClassifier,MicroMacroClassificationPerformances<Double,IClassificationSingleRunPerformances<Double>>>(
						"Model2", dataset, aggPerformances);
		
		return testResults;
		
	}
	
	
	/**
	 * Generate an example fake dataset.
	 * 
	 * @return example dataset
	 */
	private List<IClassificationResult<Double>> generateDataset3() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("Dataset3", names, names[0], null);
		
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
		trj.setClassification("s2");				// predicted class
		trj.setName("1A");
		dataset.add(trj);
		
		// trajectory 2
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.3; p[stateToIndex.get("s2")] = 0.5; p[stateToIndex.get("s3")] = 0.2; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s3");				// predicted class
		trj.setName("2A");
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
		trj.setName("3A");
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
		trj.setName("4A");
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
		trj.setName("5A");
		dataset.add(trj);
		
		// trajectory 6
		v = new String[1];
		v[0] = "s2";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.85; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s1");				// predicted class
		trj.setName("6A");
		dataset.add(trj);
		
		// trajectory 7
		v = new String[1];
		v[0] = "s3";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.1; p[stateToIndex.get("s2")] = 0.1; p[stateToIndex.get("s3")] = 0.8; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("7A");
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
		trj.setName("8A");
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
		trj.setName("9A");
		dataset.add(trj);
		
		// trajectory 10
		v = new String[1];
		v[0] = "s1";								// true class
		values = new Vector<String[]>(1); values.add(v);
		baseTrj = new CTTrajectory<Double>(nodeIndexing, times, values);
		trj = new ClassificationResults<Double>(baseTrj);
		p = new double[3]; p[stateToIndex.get("s1")] = 0.65; p[stateToIndex.get("s2")] = 0.3; p[stateToIndex.get("s3")] = 0.05; 
		trj.setProbability(0, p, stateToIndex);
		trj.setClassification("s2");				// predicted class
		trj.setName("10A");
		dataset.add(trj);
		
		
		return dataset;
	}
	
	/**
	 * Generate an example fake dataset.
	 * 
	 * @return example dataset
	 */
	private List<IClassificationResult<Double>> generateDataset4() {
		
		String[] names = new String[1]; names[0] = "class";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("Dataset4", names, names[0], null);		

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
		trj.setClassification("s2");				// predicted class
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
		trj.setClassification("s3");				// predicted class
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
		trj.setClassification("s1");				// predicted class
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
		trj.setClassification("s2");				// predicted class
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

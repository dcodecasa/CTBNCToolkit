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
package CTBNCToolkit;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTClassificationTransition {

	@Test
	public void testGetTimeNGetNodeValue() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("aaa");
		int idxB = nodeIndexing.getIndex("bbb");
		int idxC = nodeIndexing.getIndex("ccc");
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = null;
		
		Double time = 0.5;
		
		CTTransition<Double> ctT = new CTTransition<Double>(nodeIndexing, time,values);
		ClassificationTransition<Double> t = new ClassificationTransition<Double>(ctT);
		
		assertTrue(t.getTime() == time);
		assertTrue(t.getNodeValue(idxA).equals("aaa_value"));
		assertTrue(t.getNodeValue(idxB).equals("bbb_value"));
		assertTrue(t.getNodeValue(idxC) == null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCTTransitionException() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[2]; values[0] = "aaa_value"; values[1] = "bbb_value";
		
		Double time = 0.5;
		
		new CTTransition<Double>(nodeIndexing, time,values);
	}

	/**
	 * Test method for {@link CTBNCToolkit.ClassificationTransition#setNotNormalizedDistribution(double[], java.util.Map)}
	 * and {@link CTBNCToolkit.ClassificationTransition#getNotNormalizedValue(java.lang.String)}.
	 */
	@Test
	public void testSetProbabilityDistribution1() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = "ccc_value";
		
		Double time = 0.5;
		
		CTTransition<Double> ctT = new CTTransition<Double>(nodeIndexing,time,values);
		ClassificationTransition<Double> t = new ClassificationTransition<Double>(ctT);
		
		double[] p = new double[3];
		p[0] = 0.1;p[1] = 0.55;p[2] = 0.35;
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		stateToIndex.put("s1", 0);stateToIndex.put("s2", 1);stateToIndex.put("s3", 2);
		t.setProbability(p, stateToIndex);
		
		assertTrue( t.getProbability("s1") == p[stateToIndex.get("s1")]);
		assertTrue( t.getProbability("s2") == p[stateToIndex.get("s2")]);
		assertTrue( t.getProbability("s3") == p[stateToIndex.get("s3")]);
	}

	/**
	 * Test method for {@link CTBNCToolkit.ClassificationTransition#setNotNormalizedDistribution(double[], java.util.Map)}
	 * and {@link CTBNCToolkit.ClassificationTransition#getNotNormalizedValue(java.lang.String)}.
	 */
	public void testSetProbabilityDistribution2() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = "ccc_value";
		Double time = 0.5;
		
		CTTransition<Double> ctT = new CTTransition<Double>(nodeIndexing, time,values);
		ClassificationTransition<Double> t = new ClassificationTransition<Double>(ctT);
		
		assertTrue( t.getProbability("s1") == null);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.ClassificationTransition#setNotNormalizedDistribution(double[], java.util.Map)}
	 * and {@link CTBNCToolkit.ClassificationTransition#getNotNormalizedValue(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetProbabilityDistributionException() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = "ccc_value";
		Double time = 0.5;
		
		CTTransition<Double> ctT = new CTTransition<Double>(nodeIndexing, time,values);
		ClassificationTransition<Double> t = new ClassificationTransition<Double>(ctT);
		
		double[] p = new double[3];
		p[0] = 0.1;p[1] = 0.55;p[2] = 0.35;
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		stateToIndex.put("s1", 0);stateToIndex.put("s2", 1);stateToIndex.put("s3", 2);
		t.setProbability(p, stateToIndex);
		
		t.getProbability("s0");
	}

}

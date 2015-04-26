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

import org.junit.Test;

public class ZJTESTCTTransition {

	@Test
	public void testGetTimeNGetNodeValue() {
		Double time = 0.5;

		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = null;
		
		CTTransition<Double> t = new CTTransition<Double>(nodeIndexing, time, values);
		assertTrue(t.getTime() == time);
		assertTrue(t.getNodeValue(nodeIndexing.getIndex(nodeNames[0])).equals(values[0]));
		assertTrue(t.getNodeValue(nodeIndexing.getIndex(nodeNames[1])).equals(values[1]));
		assertTrue(t.getNodeValue(nodeIndexing.getIndex(nodeNames[2])) == null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTransitionNodesException1() {
		Double time = 0.5;

		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = null;
		
		CTTransition<Double> t = new CTTransition<Double>(nodeIndexing, time, values);
		assertTrue(t.getTime() == time);
		assertTrue(t.getNodeValue(-1).equals(values[0]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTransitionNodesException2() {
		Double time = 0.5;

		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[3]; values[0] = "aaa_value"; values[1] = "bbb_value"; values[2] = null;
		
		CTTransition<Double> t = new CTTransition<Double>(nodeIndexing, time, values);
		assertTrue(t.getTime() == time);
		assertTrue(t.getNodeValue(3).equals(values[0]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTransitionNodesException3() {
		Double time = 0.5;

		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("indexing", nodeNames, nodeNames[0], null);
		String[] values = new String[2]; values[0] = "aaa_value"; values[1] = "bbb_value";
		
		new CTTransition<Double>(nodeIndexing, time, values);
	}

}

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

import java.util.*;

import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTClassificationResults {

	/**
	 * Test method for {@link CTBNCToolkit.ClassificationResults#ClassificationResults(CTBNCToolkit.CTTrajectory, java.util.List)}.
	 */
	@Test
	public void testClassificationResultsCTTrajectoryOfTimeTypeStringListOfTimeType() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClassificationResultsCTTrajectoryOfTimeTypeStringListOfTimeType", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		List<Double> timeStream = new Vector<Double>();
		timeStream.add(0.0);timeStream.add(0.2);timeStream.add(0.4);timeStream.add(0.6);timeStream.add(0.8);
		timeStream.add(1.0);timeStream.add(1.2);timeStream.add(1.4);timeStream.add(1.6);timeStream.add(1.8);
		timeStream.add(2.0);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[idxA] = "Va12"; v[idxB] = "Vb1"; v[idxC] = "Vc123";
		values.add(v);
		v = new String[3];
		v[idxA] = null; v[idxB] = "Vb2"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va3"; v[idxB] = "Vb3"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va4"; v[idxB] = "Vb4"; v[idxC] = "Vc4";
		values.add(v);
		
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values), timeStream);
		assertTrue( tr.getTransitionsNumber() == 12);
		assertTrue( tr.getNodeValue( 0, idxA).equals("Va12"));
		assertTrue( tr.getNodeValue( 0, idxB).equals("Vb1"));
		assertTrue( tr.getNodeValue( 0, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 1, idxA).equals("Va12"));
		assertTrue( tr.getNodeValue( 1, idxB).equals("Vb2"));
		assertTrue( tr.getNodeValue( 1, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 2, idxA).equals("Va12"));
		assertTrue( tr.getNodeValue( 2, idxB).equals("Vb2"));
		assertTrue( tr.getNodeValue( 2, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 3, idxA).equals("Va3"));
		assertTrue( tr.getNodeValue( 3, idxB).equals("Vb3"));
		assertTrue( tr.getNodeValue( 3, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 4, idxA).equals("Va3"));
		assertTrue( tr.getNodeValue( 4, idxB).equals("Vb3"));
		assertTrue( tr.getNodeValue( 4, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 5, idxA).equals("Va3"));
		assertTrue( tr.getNodeValue( 5, idxB).equals("Vb3"));
		assertTrue( tr.getNodeValue( 5, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 6, idxA).equals("Va3"));
		assertTrue( tr.getNodeValue( 6, idxB).equals("Vb3"));
		assertTrue( tr.getNodeValue( 6, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 7, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 7, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 7, idxC).equals("Vc4"));
		assertTrue( tr.getNodeValue( 8, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 8, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 8, idxC).equals("Vc4"));
		assertTrue( tr.getNodeValue( 9, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 9, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 9, idxC).equals("Vc4"));
		assertTrue( tr.getNodeValue( 10, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 10, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 10, idxC).equals("Vc4"));
		assertTrue( tr.getNodeValue( 11, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 11, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 11, idxC).equals("Vc4"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.ClassificationResults#setClassification(java.lang.String)}
	 * and Test method for {@link CTBNCToolkit.ClassificationResults#getClassification()}.
	 */
	@Test
	public void testSetGetClass() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testSetGetClass", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		List<Double> timeStream = new Vector<Double>();
		timeStream.add(0.0);timeStream.add(0.2);timeStream.add(0.4);timeStream.add(0.6);timeStream.add(0.8);
		timeStream.add(1.0);timeStream.add(1.2);timeStream.add(1.4);timeStream.add(1.6);timeStream.add(1.8);
		timeStream.add(2.0);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[idxA] = "Va12"; v[idxB] = "Vb1"; v[idxC] = "Vc123";
		values.add(v);
		v = new String[3];
		v[idxA] = null; v[idxB] = "Vb2"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va3"; v[idxB] = "Vb3"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va4"; v[idxB] = "Vb4"; v[idxC] = "Vc4";
		values.add(v);
		
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values), timeStream);
		tr.setClassification("ciao");
		assertTrue(tr.getClassification().equals("ciao"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.ClassificationResults#setNotNormalizedDistribution(int, double[], java.util.Map)}
	 * and {@link CTBNCToolkit.ClassificationResults#getNotNormalizedValue(int, java.lang.String)}.
	 */
	@Test
	public void testSetGetProbabilityDistribution() {

		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testSetGetProbabilityDistribution", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		List<Double> timeStream = new Vector<Double>();
		timeStream.add(0.0);timeStream.add(0.2);timeStream.add(0.4);timeStream.add(0.6);timeStream.add(0.8);
		timeStream.add(1.0);timeStream.add(1.2);timeStream.add(1.4);timeStream.add(1.6);timeStream.add(1.8);
		timeStream.add(2.0);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[idxA] = "Va12"; v[idxB] = "Vb1"; v[idxC] = "Vc123";
		values.add(v);
		v = new String[3];
		v[idxA] = null; v[idxB] = "Vb2"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va3"; v[idxB] = "Vb3"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va4"; v[idxB] = "Vb4"; v[idxC] = "Vc4";
		values.add(v);
		
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values), timeStream);
		
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();
		stateToIndex.put("s1", 0);stateToIndex.put("s2", 1);stateToIndex.put("s3", 2);
		
		for(int iTransition = 0; iTransition < tr.getTransitionsNumber(); ++iTransition) {
			double[] p = new double[3];
			if( iTransition % 3 == 0) {
				p[0] = 0.1;p[1] = 0.55;p[2] = 0.35;
			} else if( iTransition % 3 == 1) {
				p[0] = 0.5;p[1] = 0.3;p[2] = 0.2;
			} else {
				p[0] = 0.4;p[1] = 0.39;p[2] = 0.21;
			}
				
			tr.setProbability(iTransition, p, stateToIndex);
		}
		
		for(int iTransition = 0; iTransition < tr.getTransitionsNumber(); ++iTransition) {
			
			if( iTransition % 3 == 0) {
				assertTrue( tr.getProbability(iTransition, "s1") == 0.1);
				assertTrue( tr.getProbability(iTransition, "s2") == 0.55);
				assertTrue( tr.getProbability(iTransition, "s3") == 0.35);
			} else if( iTransition % 3 == 1) {
				assertTrue( tr.getProbability(iTransition, "s1") == 0.5);
				assertTrue( tr.getProbability(iTransition, "s2") == 0.3);
				assertTrue( tr.getProbability(iTransition, "s3") == 0.2);
			} else {
				assertTrue( tr.getProbability(iTransition, "s1") == 0.4);
				assertTrue( tr.getProbability(iTransition, "s2") == 0.39);
				assertTrue( tr.getProbability(iTransition, "s3") == 0.21);
			}
		}
	}
	
	@Test
	public void testGetTransitionsNumber() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGetTransitionsNumber", nodeNames, nodeNames[0], null);
		
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		assertTrue( tr.getTransitionsNumber() == 0);
	}
	
	@Test
	public void testGetNodeValue() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGetNodeValue", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[idxA] = "Va12"; v[idxB] = "Vb1"; v[idxC] = "Vc123";
		values.add(v);
		v = new String[3];
		v[idxA] = null; v[idxB] = "Vb2"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va3"; v[idxB] = "Vb3"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va4"; v[idxB] = "Vb4"; v[idxC] = "Vc4";
		values.add(v);
		
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		assertTrue( tr.getTransitionsNumber() == 4);
		assertTrue( tr.getNodeValue( 0, idxA).equals("Va12"));
		assertTrue( tr.getNodeValue( 0, idxB).equals("Vb1"));
		assertTrue( tr.getNodeValue( 0, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 1, idxA).equals("Va12"));
		assertTrue( tr.getNodeValue( 1, idxB).equals("Vb2"));
		assertTrue( tr.getNodeValue( 1, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 2, idxA).equals("Va3"));
		assertTrue( tr.getNodeValue( 2, idxB).equals("Vb3"));
		assertTrue( tr.getNodeValue( 2, idxC).equals("Vc123"));
		assertTrue( tr.getNodeValue( 3, idxA).equals("Va4"));
		assertTrue( tr.getNodeValue( 3, idxB).equals("Vb4"));
		assertTrue( tr.getNodeValue( 3, idxC).equals("Vc4"));
		
	}
	
	@Test
	public void testGetTransition() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGetTransition", nodeNames, nodeNames[0], null);
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[idxA] = "Va12"; v[idxB] = "Vb1"; v[idxC] = "Vc123";
		values.add(v);
		v = new String[3];
		v[idxA] = null; v[idxB] = "Vb2"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va3"; v[idxB] = "Vb3"; v[idxC] = null;
		values.add(v);
		v = new String[3];
		v[idxA] = "Va4"; v[idxB] = "Vb4"; v[idxC] = "Vc4";
		values.add(v);
		
		ClassificationResults<Double> tr = new ClassificationResults<Double>(new CTTrajectory<Double>(nodeIndexing, times, values));
		assertTrue(tr.getTransitionsNumber() == 4);
		assertTrue(tr.getTransitionTime(0) == 0.0);
		assertTrue(tr.getTransitionTime(1) == 0.2);
		assertTrue(tr.getTransitionTime(2) == 0.6);
		assertTrue(tr.getTransitionTime(3) == 1.3);
	}

}

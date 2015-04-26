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
import java.util.*;

public class ZJTESTCTTrajectory {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException1() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testConstructorException1", nodeNames, nodeNames[0], null);
		
		List<String[]> values = new Vector<String[]>(1);
		String[] rowValues = new String[3]; rowValues[0] = "aaa_value"; rowValues[1] = "bbb_value"; rowValues[2] = null;
		values.add(rowValues);
		
		List<Double> times = null;
		
		new CTTrajectory<Double>(nodeIndexing, times, values);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException2() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testConstructorException2", nodeNames, nodeNames[0], null);
		
		List<Double> times = new Vector<Double>();
		List<String[]> values = null;
		
		new CTTrajectory<Double>(nodeIndexing, times, values);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException3() {
		
		List<String[]> values = new Vector<String[]>(1);
		String[] rowValues = new String[3]; rowValues[0] = "testConstructorException3"; rowValues[1] = "bbb_value"; rowValues[2] = null;
		values.add(rowValues);
		List<Double> times = new Vector<Double>();
		times.add(0.0);
		
		new CTTrajectory<Double>(null, times, values);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException4() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testConstructorException4", nodeNames, nodeNames[0], null);
		
		List<String[]> values = new Vector<String[]>(0);
		List<Double> times = new Vector<Double>();
		times.add(0.0);
		
		new CTTrajectory<Double>(nodeIndexing, times, values);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorException5() {
		
		List<Double> times = new Vector<Double>();
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testConstructorException5", nodeNames, nodeNames[0], null);
		
		List<String[]> values = new Vector<String[]>(1);
		String[] rowValues = new String[3]; rowValues[0] = "aaa_value"; rowValues[1] = "bbb_value"; rowValues[2] = null;
		values.add(rowValues);
		
		new CTTrajectory<Double>(nodeIndexing, times, values);
	}	

	@Test
	public void testGetTransitionsNumber() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "aaa"; nodeNames[1] = "bbb"; nodeNames[2] = "ccc";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGetTransitionsNumber", nodeNames, nodeNames[0], null);
		
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		CTTrajectory<Double> tr = new CTTrajectory<Double>(nodeIndexing, times, values);
		assertTrue( tr.getTransitionsNumber() == 0);
	}
	
	@Test
	public void testGetNodeValue() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGetNodeValue", nodeNames, nodeNames[0], null);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[0] = "Va12"; v[1] = "Vb1"; v[2] = "Vc123";
		values.add(v);
		v = new String[3];
		v[0] = null; v[1] = "Vb2"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va3"; v[1] = "Vb3"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va4"; v[1] = "Vb4"; v[2] = "Vc4";
		values.add(v);
		
		CTTrajectory<Double> tr = new CTTrajectory<Double>(nodeIndexing, times, values);
		assertTrue( tr.getTransitionsNumber() == 4);
		assertTrue( tr.getNodeValue( 0, nodeIndexing.getIndex("A")).equals("Va12"));
		assertTrue( tr.getNodeValue( 0, nodeIndexing.getIndex("B")).equals("Vb1"));
		assertTrue( tr.getNodeValue( 0, nodeIndexing.getIndex("C")).equals("Vc123"));
		assertTrue( tr.getNodeValue( 1, nodeIndexing.getIndex("A")).equals("Va12"));
		assertTrue( tr.getNodeValue( 1, nodeIndexing.getIndex("B")).equals("Vb2"));
		assertTrue( tr.getNodeValue( 1, nodeIndexing.getIndex("C")).equals("Vc123"));
		assertTrue( tr.getNodeValue( 2, nodeIndexing.getIndex("A")).equals("Va3"));
		assertTrue( tr.getNodeValue( 2, nodeIndexing.getIndex("B")).equals("Vb3"));
		assertTrue( tr.getNodeValue( 2, nodeIndexing.getIndex("C")).equals("Vc123"));
		assertTrue( tr.getNodeValue( 3, nodeIndexing.getIndex("A")).equals("Va4"));
		assertTrue( tr.getNodeValue( 3, nodeIndexing.getIndex("B")).equals("Vb4"));
		assertTrue( tr.getNodeValue( 3, nodeIndexing.getIndex("C")).equals("Vc4"));
		
	}
	
	@Test
	public void testGetTransitionTime() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("getTransitionTime", nodeNames, nodeNames[0], null);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.3);
		v = new String[3];
		v[0] = "Va12"; v[1] = "Vb1"; v[2] = "Vc123";
		values.add(v);
		v = new String[3];
		v[0] = null; v[1] = "Vb2"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va3"; v[1] = "Vb3"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va4"; v[1] = "Vb4"; v[2] = "Vc4";
		values.add(v);
		
		CTTrajectory<Double> tr = new CTTrajectory<Double>(nodeIndexing, times, values);
		assertTrue( tr.getTransitionsNumber() == 4);
		assertTrue(tr.getTransitionTime(0) == 0.0);
		assertTrue(tr.getTransitionTime(1) == 0.2);
		assertTrue(tr.getTransitionTime(2) == 0.6);
		assertTrue(tr.getTransitionTime(3) == 1.3);
	}
	
	@Test
	public void testCutTrajectory() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "A"; nodeNames[1] = "B"; nodeNames[2] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testCutTrajectory", nodeNames, nodeNames[0], null);
		
		String[] v;
		List<Double> times = new Vector<Double>();
		List<String[]> values = new Vector<String[]>();
		times.add(0.0);times.add(0.2);times.add(0.6);times.add(1.2);
		v = new String[3];
		v[0] = "Va12"; v[1] = "Vb1"; v[2] = "Vc123";
		values.add(v);
		v = new String[3];
		v[0] = null; v[1] = "Vb2"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va3"; v[1] = "Vb3"; v[2] = null;
		values.add(v);
		v = new String[3];
		v[0] = "Va4"; v[1] = "Vb4"; v[2] = "Vc4";
		values.add(v);
		
		CTTrajectory<Double> tr = new CTTrajectory<Double>(nodeIndexing, times, values);
		
		CTTrajectory<Double> newTr = CTTrajectory.cutTrajectory( tr, 0.75);
		assertTrue( newTr.getTransitionsNumber() == 4);
		assertTrue( this.doubleEquals( newTr.getTransitionTime(0), 0.0));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(1), 0.2));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(2), 0.6));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(3), 0.9));
		assertTrue(newTr.getNodeValue( 3, 0).equals("Va3"));
		assertTrue(newTr.getNodeValue( 3, 1).equals("Vb3"));
		assertTrue(newTr.getNodeValue( 3, 2).equals("Vc123"));
		
		newTr = CTTrajectory.cutTrajectory( tr, 0.5);
		assertTrue( newTr.getTransitionsNumber() == 3);
		assertTrue( this.doubleEquals( newTr.getTransitionTime(0), 0.0));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(1), 0.2));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(2), 0.6));
		assertTrue(newTr.getNodeValue( 2, 0).equals("Va3"));
		assertTrue(newTr.getNodeValue( 2, 1).equals("Vb3"));
		assertTrue(newTr.getNodeValue( 2, 2).equals("Vc123"));
		
		newTr = CTTrajectory.cutTrajectory( tr, 0.25);
		assertTrue( newTr.getTransitionsNumber() == 3);
		assertTrue( this.doubleEquals( newTr.getTransitionTime(0), 0.0));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(1), 0.2));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(2), 0.3));
		assertTrue(newTr.getNodeValue( 2, 0).equals("Va12"));
		assertTrue(newTr.getNodeValue( 2, 1).equals("Vb2"));
		assertTrue(newTr.getNodeValue( 2, 2).equals("Vc123"));
		
		newTr = CTTrajectory.cutTrajectory( tr, 0.1);
		assertTrue( newTr.getTransitionsNumber() == 2);
		assertTrue( this.doubleEquals( newTr.getTransitionTime(0), 0.0));
		assertTrue( this.doubleEquals( newTr.getTransitionTime(1), 0.12));
		assertTrue(newTr.getNodeValue( 1, 0).equals("Va12"));
		assertTrue(newTr.getNodeValue( 1, 1).equals("Vb1"));
		assertTrue(newTr.getNodeValue( 1, 2).equals("Vc123"));
		
	}
	
	private boolean doubleEquals(double d1, double d2) {
		
		double eps = 0.00001;
		
		return d1 - eps < d2 && d1 + eps > d2;
	}
}

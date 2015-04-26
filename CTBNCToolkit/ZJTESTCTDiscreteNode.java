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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCTDiscreteNode {

	@Test(expected = IllegalStateException.class)
	public void testDiscreteNodeStringSetOfStringException() {
			
		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("a@a");states.add("bbb");states.add("ccc");
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		assertTrue(!node.validatedCIMs());
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#addParent(CTBNCToolkit.DiscreteNode)}
	 * and {@link CTBNCToolkit.CTDiscreteNode#removeParent(CTBNCToolkit.DiscreteNode)}.
	 */
	@Test
	public void testChildrenNParent() {
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		DiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		DiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		DiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		states = new TreeSet<String>();states.add("c2_1");states.add("c2_2");states.add("c2_3");
		DiscreteNode nodeC2 = new CTDiscreteNode("c2",states);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 1);
		
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeP1.getChildrenNumber() == 1);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 1);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 0);
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP2.getParentsNumber() == 0);
		assertTrue(nodeP1.getChildIndex(nodeC1.getName()) == 0);
		assertTrue(nodeP1.getChild(0) == nodeC1);
		assertTrue(nodeP1.getChild(nodeC1.getName()) == nodeC1);
		assertTrue(nodeC1.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC1.getParent(0) == nodeP1);
		assertTrue(nodeC1.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 2);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 1);
		assertTrue(!nodeP1.addChild(nodeC1));
		
		assertTrue(nodeC2.addParent(nodeP1));
		assertTrue(nodeP1.getChildrenNumber() == 2);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 1);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 1);
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP2.getParentsNumber() == 0);
		assertTrue(nodeP1.getChildIndex(nodeC1.getName()) == 0);
		assertTrue(nodeP1.getChild(0) == nodeC1);
		assertTrue(nodeP1.getChild(nodeC1.getName()) == nodeC1);
		assertTrue(nodeP1.getChildIndex(nodeC2.getName()) == 1);
		assertTrue(nodeP1.getChild(1) == nodeC2);
		assertTrue(nodeP1.getChild(nodeC2.getName()) == nodeC2);
		assertTrue(nodeC1.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC1.getParent(0) == nodeP1);
		assertTrue(nodeC1.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(nodeC2.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC2.getParent(0) == nodeP1);
		assertTrue(nodeC2.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 2);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 2);
		assertTrue(!nodeC2.addParent(nodeP1));
		
		assertTrue(nodeP2.addChild(nodeC2));
		assertTrue(nodeP1.getChildrenNumber() == 2);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 1);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 2);
		assertTrue(nodeP2.getChildrenNumber() == 1);
		assertTrue(nodeP2.getParentsNumber() == 0);
		assertTrue(nodeP1.getChildIndex(nodeC1.getName()) == 0);
		assertTrue(nodeP1.getChild(0) == nodeC1);
		assertTrue(nodeP1.getChild(nodeC1.getName()) == nodeC1);
		assertTrue(nodeP1.getChildIndex(nodeC2.getName()) == 1);
		assertTrue(nodeP1.getChild(1) == nodeC2);
		assertTrue(nodeP1.getChild(nodeC2.getName()) == nodeC2);
		assertTrue(nodeC1.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC1.getParent(0) == nodeP1);
		assertTrue(nodeC1.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(nodeC2.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC2.getParent(0) == nodeP1);
		assertTrue(nodeC2.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(nodeC2.getParentIndex(nodeP2.getName()) == 1);
		assertTrue(nodeC2.getParent(1) == nodeP2);
		assertTrue(nodeC2.getParent(nodeP2.getName()) == nodeP2);
		assertTrue(nodeP2.getChildIndex(nodeC2.getName()) == 0);
		assertTrue(nodeP2.getChild(0) == nodeC2);
		assertTrue(nodeP2.getChild(nodeC2.getName()) == nodeC2);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 2);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 6);
		assertTrue(!nodeP2.addChild(nodeC2));
		
		assertTrue(nodeC2.removeParent(nodeP1));
		assertTrue(nodeP1.getChildrenNumber() == 1);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 1);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 1);
		assertTrue(nodeP2.getChildrenNumber() == 1);
		assertTrue(nodeP2.getParentsNumber() == 0);
		assertTrue(nodeP1.getChildIndex(nodeC1.getName()) == 0);
		assertTrue(nodeP1.getChild(0) == nodeC1);
		assertTrue(nodeP1.getChild(nodeC1.getName()) == nodeC1);
		assertTrue(nodeC1.getParentIndex(nodeP1.getName()) == 0);
		assertTrue(nodeC1.getParent(0) == nodeP1);
		assertTrue(nodeC1.getParent(nodeP1.getName()) == nodeP1);
		assertTrue(nodeC2.getParentIndex(nodeP2.getName()) == 0);
		assertTrue(nodeC2.getParent(0) == nodeP2);
		assertTrue(nodeC2.getParent(nodeP2.getName()) == nodeP2);
		assertTrue(nodeP2.getChildIndex(nodeC2.getName()) == 0);
		assertTrue(nodeP2.getChild(0) == nodeC2);
		assertTrue(nodeP2.getChild(nodeC2.getName()) == nodeC2);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 2);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 3);
		assertTrue(!nodeC2.removeParent(nodeP1));
		
		assertTrue(nodeP1.removeChild(nodeC1));
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 0);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 1);
		assertTrue(nodeP2.getChildrenNumber() == 1);
		assertTrue(nodeP2.getParentsNumber() == 0);
		assertTrue(nodeP2.getChildIndex(nodeC2.getName()) == 0);
		assertTrue(nodeP2.getChild(0) == nodeC2);
		assertTrue(nodeP2.getChild(nodeC2.getName()) == nodeC2);
		assertTrue(nodeC2.getParentIndex(nodeP2.getName()) == 0);
		assertTrue(nodeC2.getParent(0) == nodeP2);
		assertTrue(nodeC2.getParent(nodeP2.getName()) == nodeP2);
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 3);
		assertTrue(!nodeP1.removeChild(nodeC1));
		
		assertTrue(nodeP2.removeChild(nodeC2));
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 0);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 0);
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP2.getParentsNumber() == 0);	
		assertTrue(((CTDiscreteNode) nodeP1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeP2).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC1).getNumberParentsEntries() == 1);
		assertTrue(((CTDiscreteNode) nodeC2).getNumberParentsEntries() == 1);
		assertTrue(!nodeP2.removeChild(nodeC2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException1() {
		// Not empty state set
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1", new TreeSet<String>());
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getChild(1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException2() {
		// Not empty state set
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1", new TreeSet<String>());
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getParent(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException3() {
		// Not empty state set
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1", new TreeSet<String>());
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getChild("ciao");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException4() {
		// Not empty state set
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1", new TreeSet<String>());
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1", new TreeSet<String>());
		nodeP1.addParent(nodeC1);
		
		nodeP1.getParent("ciao");
	}
	
	@Test(expected = RuntimeException.class)
	public void testChildrenNParentException5() {	
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states,true);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states,true);
		assertTrue(nodeC1.addParent(nodeP1));
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#CTDiscreteNode(java.lang.String)}.
	 */
	@Test
	public void testCTDiscreteNodeString() {
		CTDiscreteNode node = new CTDiscreteNode("prova", new TreeSet<String>());
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 0);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(!node.validatedCIMs());
		assertTrue(node.getNumberParentsEntries() == 1);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#CTDiscreteNode(java.lang.String, java.util.Set)}.
	 */
	@Test
	public void testDiscreteNodeStringSetOfString1() {

		// Empty state set
		Set<String> states = new TreeSet<String>();		
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 0);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(!node.validatedCIMs());
		assertTrue(node.getNumberParentsEntries() == 1);
	}
		
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#CTDiscreteNode(java.lang.String, java.util.Set)}.
	 */
	@Test
	public void testDiscreteNodeStringSetOfString2() {
			
		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(!node.validatedCIMs());
		assertTrue(node.getNumberParentsEntries() == 1);
	}
	

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#getCurrentParentsEntry()}.
	 */
	@Test
	public void testGetCurrentParentsEntry() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		assertTrue(nodeP1.addChild(nodeC1));
		nodeP1.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		nodeP1.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 1);
		assertTrue(nodeC1.addParent(nodeP2));
		nodeP1.setEvidence(0);nodeP2.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		nodeP1.setEvidence(1);nodeP2.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 1);
		nodeP1.setEvidence(0);nodeP2.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 2);
		nodeP1.setEvidence(1);nodeP2.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 3);
		nodeP1.setEvidence(0);nodeP2.setEvidence(2);
		assertTrue(nodeC1.getCurrentParentsEntry() == 4);
		nodeP1.setEvidence(1);nodeP2.setEvidence(2);
		assertTrue(nodeC1.getCurrentParentsEntry() == 5);
		
	}
	
	/**
	 * Test method for setParentsEntry)=
	 */
	@Test
	public void testSetParentsEntry() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");states.add("p1_3");states.add("p1_4");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("p3_1");states.add("p3_2");states.add("p3_3");
		CTDiscreteNode nodeP3 = new CTDiscreteNode("p3",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		assertTrue(nodeC1.addParent(nodeP3));
		
		for( int pE = 0; pE < nodeC1.getNumberParentsEntries(); ++pE) {
			nodeC1.setParentsEntry(pE);
			assertTrue(nodeC1.getCurrentParentsEntry() == pE);
		}
		
		for( int pE = 0; pE < nodeP1.getNumberParentsEntries(); ++pE) {
			nodeP1.setParentsEntry(pE);
			assertTrue(nodeP1.getCurrentParentsEntry() == pE);
		}
		
		for( int pE = 0; pE < nodeP2.getNumberParentsEntries(); ++pE) {
			nodeP2.setParentsEntry(pE);
			assertTrue(nodeP2.getCurrentParentsEntry() == pE);
		}
		
		for( int pE = 0; pE < nodeP3.getNumberParentsEntries(); ++pE) {
			nodeP3.setParentsEntry(pE);
			assertTrue(nodeP3.getCurrentParentsEntry() == pE);
		}
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#getCurrentParentsEntry()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetCurrentParentsEntryException() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		assertTrue(nodeP1.addChild(nodeC1));
		nodeP1.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		nodeP1.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 1);
		assertTrue(nodeC1.addParent(nodeP2));
		nodeP1.setEvidence(0);nodeP2.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 0);
		nodeP1.setEvidence(1);nodeP2.setEvidence(0);
		assertTrue(nodeC1.getCurrentParentsEntry() == 1);
		nodeP1.setEvidence(0);nodeP2.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 2);
		nodeP1.setEvidence(1);nodeP2.setEvidence(1);
		assertTrue(nodeC1.getCurrentParentsEntry() == 3);
		nodeP1.setEvidence(0);nodeP2.setEvidence(2);
		assertTrue(nodeC1.getCurrentParentsEntry() == 4);
		nodeP1.setEvidence(1);nodeP2.setEvidence(2);
		assertTrue(nodeC1.getCurrentParentsEntry() == 5);
		nodeP1.retractEvidence();
		nodeC1.getCurrentParentsEntry();
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#getCIMValue(int, int, int)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testGetCIMValueException1() {
		Set<String> states = new TreeSet<String>();		
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		assertTrue(node.getNumberParentsEntries() == 1);
	
		node.getCIMValue(0, 0, 0);
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetCIMValueException2() {
		Set<String> states = new TreeSet<String>();		
		states.add("aaa");
		states.add("bbb");
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		assertTrue(node.getNumberParentsEntries() == 1);
		node.getCIMValue(0, 0, 0);
		node.getCIMValue(0, 1, 1);
		node.getCIMValue(0, 1, 2);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#setCIMValue(int, int, int, double)}.
	 */
	@Test
	public void testSetNGetCIMValue() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));

		nodeP1.setCIMValue(0, 0, 0, -1.0);
		nodeP1.setCIMValue(0, 0, 1, 1.0);
		nodeP1.setCIMValue(0, 1, 0, 2.0);
		nodeP1.setCIMValue(0, 1, 1, -2.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 0) == -1.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 1) == 1.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 0) == 2.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 1) == -2.0);
		assertTrue(!nodeP1.validatedCIMs());
		assertTrue(nodeP1.checkCIMs() == -1);
		assertTrue(nodeP1.validatedCIMs());
		
		nodeP1.setCIMValue(0, 0, 0, -2.0);
		nodeP1.setCIMValue(0, 0, 1, 2.0);
		nodeP1.setCIMValue(0, 1, 0, 3.0);
		nodeP1.setCIMValue(0, 1, 1, -3.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 0) == -2.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 1) == 2.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 0) == 3.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 1) == -3.0);
		assertTrue(!nodeP1.validatedCIMs());
		assertTrue(nodeP1.checkCIMs() == -1);
		assertTrue(nodeP1.validatedCIMs());
		
		nodeP2.setCIMValue(0, 0, 0, -3.0);
		nodeP2.setCIMValue(0, 0, 1, 0.5);
		nodeP2.setCIMValue(0, 0, 2, 2.5);
		nodeP2.setCIMValue(0, 1, 0, 3.0);
		nodeP2.setCIMValue(0, 1, 1, -5.5);
		nodeP2.setCIMValue(0, 1, 2, 2.5);
		nodeP2.setCIMValue(0, 2, 0, 1.0);
		nodeP2.setCIMValue(0, 2, 1, 0.5);
		nodeP2.setCIMValue(0, 2, 2, -1.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 0) == -3.0);
		assertTrue(nodeP2.getCIMValue(0, 0, 1) == 0.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 2) == 2.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 0) == 3.0);
		assertTrue(nodeP2.getCIMValue(0, 1, 1) == -5.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 2) == 2.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 0) == 1.0);
		assertTrue(nodeP2.getCIMValue(0, 2, 1) == 0.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 2) == -1.5);
		assertTrue(!nodeP2.validatedCIMs());
		assertTrue(nodeP2.checkCIMs() == -1);
		assertTrue(nodeP2.validatedCIMs());
		
		nodeP2.setCIMValue(0, 0, 0, -5.0);
		nodeP2.setCIMValue(0, 0, 1, 1.5);
		nodeP2.setCIMValue(0, 0, 2, 3.5);
		nodeP2.setCIMValue(0, 1, 0, 4.0);
		nodeP2.setCIMValue(0, 1, 1, -7.5);
		nodeP2.setCIMValue(0, 1, 2, 3.5);
		nodeP2.setCIMValue(0, 2, 0, 2.0);
		nodeP2.setCIMValue(0, 2, 1, 1.5);
		nodeP2.setCIMValue(0, 2, 2, -3.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 0) == -5.0);
		assertTrue(nodeP2.getCIMValue(0, 0, 1) == 1.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 2) == 3.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 0) == 4.0);
		assertTrue(nodeP2.getCIMValue(0, 1, 1) == -7.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 2) == 3.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 0) == 2.0);
		assertTrue(nodeP2.getCIMValue(0, 2, 1) == 1.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 2) == -3.5);
		assertTrue(!nodeP2.validatedCIMs());
		assertTrue(nodeP2.checkCIMs() == -1);
		assertTrue(nodeP2.validatedCIMs());
		
		for(int pE = 0; pE < nodeC1.getNumberParentsEntries(); ++pE) {
			double[][] cim = new double[nodeC1.getStatesNumber()][nodeC1.getStatesNumber()];
			for(int i = 0; i < cim.length; ++i) {
				double rowSum = 0;
				for(int j = 0; j < cim.length; ++j)
					if( i != j) {
						cim[i][j] = (new Random()).nextDouble();
						rowSum += cim[i][j]; 
					}
				cim[i][i] = -rowSum;
			}
			nodeC1.setCIM(pE, cim);
			for(int i = 0; i < cim.length; ++i)
				for(int j = 0; j < cim.length; ++j)
					assertTrue( nodeC1.getCIMValue(pE, i, j) == cim[i][j]);
		}
		assertTrue(!nodeC1.validatedCIMs());
		assertTrue(nodeC1.checkCIMs() == -1);
		assertTrue(nodeC1.validatedCIMs());
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#setCIMValue(int, int, int, double)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetNGetCIMValueException1() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		nodeP1.setCIMValue(1, 0, 0, -1.0);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#setCIMValue(int, int, int, double)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetNGetCIMValueException2() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		nodeP1.setCIMValue(0, 0, 3, -1.0);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#setCIMValue(int, int, int, double)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetNGetCIMValueException3() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		nodeC1.setCIMValue(nodeC1.getNumberParentsEntries(), 0, 0, -1.0);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#setCIMValue(int, int, int, double)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testSetNGetCIMValueException4() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		nodeC1.setCIMValue(nodeC1.getNumberParentsEntries() -1 , 2, 1, -1.0);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#validatedCIMs()}
	 * and {@link CTBNCToolkit.CTDiscreteNode#checkCIMs()}.
	 */
	@Test
	public void testCheckCIMsNValidatedCIMsCTNode() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states);
		states = new TreeSet<String>();states.add("c1_1");states.add("c1_2");
		CTDiscreteNode nodeC1 = new CTDiscreteNode("c1",states);
		
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC1.addParent(nodeP2));
		
		nodeP1.setCIMValue(0, 0, 0, -1.0);
		nodeP1.setCIMValue(0, 0, 1, 1.0);
		nodeP1.setCIMValue(0, 1, 0, 2.0);
		nodeP1.setCIMValue(0, 1, 1, -2.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 0) == -1.0);
		assertTrue(nodeP1.getCIMValue(0, 0, 1) == 1.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 0) == 2.0);
		assertTrue(nodeP1.getCIMValue(0, 1, 1) == -2.0);
		assertTrue(!nodeP1.validatedCIMs());
		assertTrue(nodeP1.checkCIMs() == -1);
		assertTrue(nodeP1.validatedCIMs());
		
		nodeP2.setCIMValue(0, 0, 0, -3.0);
		nodeP2.setCIMValue(0, 0, 1, 0.5);
		nodeP2.setCIMValue(0, 0, 2, 2.5);
		nodeP2.setCIMValue(0, 1, 0, 3.0);
		nodeP2.setCIMValue(0, 1, 1, -5.5);
		nodeP2.setCIMValue(0, 1, 2, 2.5);
		nodeP2.setCIMValue(0, 2, 0, 1.0);
		nodeP2.setCIMValue(0, 2, 1, 0.5);
		nodeP2.setCIMValue(0, 2, 2, -1.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 0) == -3.0);
		assertTrue(nodeP2.getCIMValue(0, 0, 1) == 0.5);
		assertTrue(nodeP2.getCIMValue(0, 0, 2) == 2.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 0) == 3.0);
		assertTrue(nodeP2.getCIMValue(0, 1, 1) == -5.5);
		assertTrue(nodeP2.getCIMValue(0, 1, 2) == 2.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 0) == 1.0);
		assertTrue(nodeP2.getCIMValue(0, 2, 1) == 0.5);
		assertTrue(nodeP2.getCIMValue(0, 2, 2) == -1.5);
		assertTrue(!nodeP2.validatedCIMs());
		assertTrue(nodeP2.checkCIMs() == -1);
		assertTrue(nodeP2.validatedCIMs());
		
		for(int pE = 0; pE < nodeC1.getNumberParentsEntries(); ++pE) {
			double[][] cim = new double[nodeC1.getStatesNumber()][nodeC1.getStatesNumber()];
			for(int i = 0; i < cim.length; ++i) {
				double rowSum = 0;
				for(int j = 0; j < cim.length; ++j)
					if( i != j) {
						cim[i][j] = (new Random()).nextDouble();
						rowSum += cim[i][j]; 
					}
				if( pE != 2)
					cim[i][i] = -rowSum;
				else
					cim[i][i] = 0;
			}
			nodeC1.setCIM(pE, cim);
			for(int i = 0; i < cim.length; ++i)
				for(int j = 0; j < cim.length; ++j)
					assertTrue( nodeC1.getCIMValue(pE, i, j) == cim[i][j]);
		}
		assertTrue(!nodeC1.validatedCIMs());
		assertTrue(nodeC1.checkCIMs() == 2);
		assertTrue(!nodeC1.validatedCIMs());
	}
	
	
	/**
	 * Test method for {@link CTBNCToolkit.CTDiscreteNode#validatedCIMs()}
	 * and {@link CTBNCToolkit.CTDiscreteNode#checkCIMs()}.
	 */
	@Test
	public void testCheckCIMsNValidatedCIMsStaticNode() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("p1_1");states.add("p1_2");
		CTDiscreteNode nodeP1 = new CTDiscreteNode("p1",states,true);
		states = new TreeSet<String>();states.add("p2_1");states.add("p2_2");states.add("p2_3");
		CTDiscreteNode nodeP2 = new CTDiscreteNode("p2",states,true);
	
		nodeP1.setCIMValue(0, 0, 0, 0.5);
		nodeP1.setCIMValue(0, 0, 1, 0.5);
		assertTrue(nodeP1.getCIMValue(0, 0, 0) == 0.5);
		assertTrue(nodeP1.getCIMValue(0, 0, 1) == 0.5);
		assertTrue(!nodeP1.validatedCIMs());
		assertTrue(nodeP1.checkCIMs() == -1);
		assertTrue(nodeP1.validatedCIMs());
		
		nodeP2.setCIMValue(0, 0, 0, 0.45);
		nodeP2.setCIMValue(0, 0, 1, 0.35);
		nodeP2.setCIMValue(0, 0, 2, 0.2);
		assertTrue(nodeP2.getCIMValue(0, 0, 0) == 0.45);
		assertTrue(nodeP2.getCIMValue(0, 0, 1) == 0.35);
		assertTrue(nodeP2.getCIMValue(0, 0, 2) == 0.2);
		assertTrue(!nodeP2.validatedCIMs());
		assertTrue(nodeP2.checkCIMs() == -1);
		assertTrue(nodeP2.validatedCIMs());
		
		nodeP2.setCIMValue(0, 0, 2, 0.1);
		assertTrue(nodeP2.getCIMValue(0, 0, 2) == 0.1);
		assertTrue(!nodeP2.validatedCIMs());
		assertTrue(nodeP2.checkCIMs() == 0);
		assertTrue(!nodeP2.validatedCIMs());
	}

	@Test(expected = RuntimeException.class)
	public void testSampleTransitionTimeException1() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,true);
		
		double[][] cim = new double[1][2];
		cim[0][0] = 0.3; cim[0][1] = 0.7;
		node.setCIM(0, cim);
		assertTrue( node.checkCIMs() == -1);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		node.sampleTransitionTime();	// error because it is a static node
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleTransitionTimeException2() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		node.sampleTransitionTime();	// error because a parent is not instanced
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleTransitionTimeException3() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		node.sampleTransitionTime();	// error because it is not instanced
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleTransitionTimeException4() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		node.sampleTransitionTime();	// error because its CIMs are not validated
	}
	
	@Test
	public void testSampleTransitionTime() {
		
		System.out.println("TEST: ZJTESTCTDiscreteNode.testSampleTransitionTime");
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		node.setEvidence(1);
		assertTrue( node.isInstanced());
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		System.out.println("sampledTime = " + node.sampleTransitionTime());	// no error
	}
	
	@Test
	public void testSampleState1() {
		
		System.out.println("TEST: ZJTESTCTDiscreteNode.testSampleState1");
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,true);
		
		double[][] cim = new double[1][2];
		cim[0][0] = 0.3; cim[0][1] = 0.7;
		node.setCIM(0, cim);
		assertTrue( node.checkCIMs() == -1);
		
		System.out.println("sampled state 1 = " + node.sampleState());			// no error
		node.setEvidence(0);		
		System.out.println("sampled state 2 = " + node.sampleState());			// no error
	}
	
	@Test
	public void testSampleState2() {
		
		System.out.println("TEST: ZJTESTCTDiscreteNode.testSampleState2");
		Set<String> states2;Set<String> states3;
		// Not empty state set
		states2 = new TreeSet<String>();states2.add("n1_1");states2.add("n1_2");
		states3 = new TreeSet<String>();states3.add("n1_1");states3.add("n1_2");states3.add("n1_3");
		CTDiscreteNode node = new CTDiscreteNode("node",states3,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states2,true);
		parent.addChild(node);
		
		double[][] cim = new double[3][3];
		cim[0][0] = -2; cim[0][1] = 1; cim[0][2] = 1;
		cim[1][0] = 2; cim[1][1] = -4; cim[1][2] = 2;
		cim[2][0] = 2; cim[2][1] = 1; cim[2][2] = -3;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		node.setEvidence(1);
		assertTrue( node.isInstanced());
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		System.out.println("sampled state 1 = " + node.sampleState());			// no error
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleStateException1() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,true);
		
		double[][] cim = new double[1][2];
		cim[0][0] = 0.3; cim[0][1] = 0.7;
		node.setCIM(0, cim);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		node.sampleState();				// error because it is not validated
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleStateException2() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		node.sampleState();				// error because a parent is not instanced
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleStateException3() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		assertTrue( node.checkCIMs() == -1);
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		node.sampleState();				// error because it is not instanced
	}
	
	@Test(expected = RuntimeException.class)
	public void testSampleStateException4() {
		
		Set<String> states;
		// Not empty state set
		states = new TreeSet<String>();states.add("n1_1");states.add("n1_2");
		CTDiscreteNode node = new CTDiscreteNode("node",states,false);
		CTDiscreteNode parent = new CTDiscreteNode("parent",states,true);
		parent.addChild(node);
		
		double[][] cim = new double[2][2];
		cim[0][0] = -1; cim[0][1] = 1;
		cim[1][0] = 2; cim[1][1] = -2;
		node.setCIM(0, cim);
		node.setCIM(1, cim);
		
		node.setEvidence(0);
		assertTrue( node.isInstanced());
		
		parent.setEvidence(0);
		assertTrue( parent.isInstanced());
		
		node.sampleState();				// error because its CIMs are not validated
	}
	
	@Test
	public void testClone() {
		
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		CTDiscreteNode node = new CTDiscreteNode("prova", states);
		node.setEvidence("ccc");
		CTDiscreteNode clonedNode = node.clone();
		
		assertTrue(node.getCurrentState()== clonedNode.getCurrentState());
		assertTrue(node.getName() == clonedNode.getName());
		assertTrue(node.isInstanced() == clonedNode.isInstanced());
		assertTrue(node.getStatesNumber() == clonedNode.getStatesNumber());
		assertTrue(node.isStaticNode() == clonedNode.isStaticNode());
		for( int i = 0; i < node.getStatesNumber(); ++i)
			clonedNode.getStateIndex(node.getStateName(i));
		
		
		
	}
}

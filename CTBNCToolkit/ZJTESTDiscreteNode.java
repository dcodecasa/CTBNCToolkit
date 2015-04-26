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

/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Tests for DiscreteNode class
 */
public class ZJTESTDiscreteNode {

	@Test
	public void testDiscreteNodeStringSetOfString1() {

		// Empty state set
		Set<String> states = new TreeSet<String>();		
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 0);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
	}
		
	@Test
	public void testDiscreteNodeStringSetOfString2() {
			
		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetStateNameException() {
		
		// Empty state set
		Set<String> states = new TreeSet<String>();		
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 0);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		node.getStateIndex("aaa");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetStateIndexException() {
		
		// Empty state set
		Set<String> states = new TreeSet<String>();		
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 0);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		node.getStateName(0);
	}
	
	@Test
	public void testGetStateName() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		assertTrue(node.getStateName(0).equals("aaa"));
		assertTrue(node.getStateName(1).equals("bbb"));
		assertTrue(node.getStateName(2).equals("ccc"));
	}
	
	@Test
	public void testGetStateIndex() {
	
		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		assertTrue(node.getStateIndex("aaa") == 0);
		assertTrue(node.getStateIndex("bbb") == 1);
		assertTrue(node.getStateIndex("ccc") == 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tesStateException1() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		node.setEvidence(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void tesStateException2() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		node.setEvidence(3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void tesStateException3() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		node.setEvidence("ciao");
	}
	
	@Test
	public void tesState1() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
		node.setEvidence(0);
		assertTrue(node.isInstanced());
		assertTrue(node.getCurrentState().equals("aaa"));
		node.retractEvidence();
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
		node.setEvidence("ccc");
		assertTrue(node.isInstanced());
		assertTrue(node.getCurrentState().equals("ccc"));
		node.retractEvidence();
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
	}
	
	@Test
	public void tesState2() {

		// Not empty state set
		Set<String> states = new TreeSet<String>();	
		states.add("aaa"); states.add("bbb"); states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		assertTrue(node.getName().equals("prova"));
		assertTrue(node.getChildrenNumber() == 0);
		assertTrue(node.getParentsNumber() == 0);
		assertTrue(node.getStatesNumber() == 3);
		assertTrue(node.getCurrentState() == null);
		assertTrue(!node.isInstanced());
		assertTrue(node.getParentsNumber() == 0);
		
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
		node.setEvidence(0);
		assertTrue(node.isInstanced());
		assertTrue(node.getCurrentState().equals("aaa"));
		node.retractEvidence();
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
		node.setEvidence("ccc");
		assertTrue(node.isInstanced());
		assertTrue(node.getCurrentState().equals("ccc"));
		node.retractEvidence();
		assertTrue(!node.isInstanced());
		assertTrue(node.getCurrentState() == null);
	}
	
//	@Test(expected = IllegalStateException.class)
//	public void testAddStateException1() {
//		// Not empty state set
//		DiscreteNode node = new DiscreteNode("prova");
//		node.addChild(new DiscreteNode("child"));
//		
//		node.addState("aaa");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void testAddStateException2() {
//		// Not empty state set
//		DiscreteNode node = new DiscreteNode("prova");
//		node.addParent(new DiscreteNode("parent"));
//		
//		node.addState("aaa");
//	}
//	
//	@Test
//	public void testAddState() {
//		// Not empty state set
//		DiscreteNode node = new DiscreteNode("prova");
//		assertTrue(node.getName().equals("prova"));
//		assertTrue(node.getChildrenNumber() == 0);
//		assertTrue(node.getParentNumber() == 0);
//		assertTrue(node.getStatesNumber() == 0);
//		assertTrue(node.getCurrentState() == null);
//		assertTrue(!node.isInstanced());
//		assertTrue(node.getParentNumber() == 0);
//		
//		node.addState("aaa");
//		assertTrue(node.getStatesNumber() == 1);
//		node.addState("aaa");
//		assertTrue(node.getStatesNumber() == 1);
//		node.addState("bbb");
//		assertTrue(node.getStatesNumber() == 2);
//		node.addState("ccc");
//		assertTrue(node.getStatesNumber() == 3);
//		node.addState("bbb");
//		assertTrue(node.getStatesNumber() == 3);
//		
//		assertTrue(node.getStateName(0).equals("aaa"));
//		assertTrue(node.getStateName(1).equals("bbb"));
//		assertTrue(node.getStateName(2).equals("ccc"));
//		
//		assertTrue(node.getStateIndex("aaa") == 0);
//		assertTrue(node.getStateIndex("bbb") == 1);
//		assertTrue(node.getStateIndex("ccc") == 2);
//	}

	@Test
	public void testChildrenNParent() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeP2 = new DiscreteNode("p2", new TreeSet<String>());
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeC2 = new DiscreteNode("c2", new TreeSet<String>());
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		
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
		assertTrue(!nodeP2.removeChild(nodeC2));
	}
	
	@Test
	public void testRemoveAllChildrenNParents() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeP2 = new DiscreteNode("p2", new TreeSet<String>());
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		DiscreteNode nodeC2 = new DiscreteNode("c2", new TreeSet<String>());
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		
		assertFalse(nodeP1.removeAllChildren());
		assertFalse(nodeP1.removeAllParents());
		assertFalse(nodeP2.removeAllChildren());
		assertFalse(nodeP2.removeAllParents());
		assertFalse(nodeC1.removeAllChildren());
		assertFalse(nodeC1.removeAllParents());
		assertFalse(nodeC2.removeAllChildren());
		assertFalse(nodeC2.removeAllParents());
		
		assertTrue(nodeP1.addChild(nodeC1));
		assertTrue(nodeC2.addParent(nodeP1));
		assertTrue(nodeP2.addChild(nodeC2));
		assertTrue(nodeP1.getChildrenNumber() == 2);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 1);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 2);
		assertTrue(nodeP2.getChildrenNumber() == 1);
		assertTrue(nodeP2.getParentsNumber() == 0);
		
		assertTrue(nodeP1.removeAllChildren());
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 0);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 1);
		assertTrue(nodeP2.getChildrenNumber() == 1);
		assertTrue(nodeP2.getParentsNumber() == 0);
		
		assertFalse(nodeP1.removeAllParents());
		assertFalse(nodeC1.removeAllChildren());
		assertFalse(nodeC1.removeAllParents());
		assertFalse(nodeC2.removeAllChildren());
		
		assertTrue(nodeC2.removeAllParents());
		assertTrue(nodeP1.getChildrenNumber() == 0);
		assertTrue(nodeP1.getParentsNumber() == 0);
		assertTrue(nodeC1.getChildrenNumber() == 0);
		assertTrue(nodeC1.getParentsNumber() == 0);
		assertTrue(nodeC2.getChildrenNumber() == 0);
		assertTrue(nodeC2.getParentsNumber() == 0);
		assertTrue(nodeP2.getChildrenNumber() == 0);
		assertTrue(nodeP2.getParentsNumber() == 0);
		
		assertFalse(nodeP2.removeAllChildren());
		assertFalse(nodeP2.removeAllParents());
		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException1() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getChild(1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException2() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getParent(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException3() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		nodeP1.addChild(nodeC1);
		
		nodeP1.getChild("ciao");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChildrenNParentException4() {
		// Not empty state set
		DiscreteNode nodeP1 = new DiscreteNode("p1", new TreeSet<String>());
		DiscreteNode nodeC1 = new DiscreteNode("c1", new TreeSet<String>());
		nodeP1.addParent(nodeC1);
		
		nodeP1.getParent("ciao");
	}
	
	@Test
	public void testClone() {
		
		Set<String> states = new TreeSet<String>();	
		states.add("aaa");states.add("bbb");states.add("ccc");
		DiscreteNode node = new DiscreteNode("prova", states);
		node.setEvidence("ccc");
		DiscreteNode clonedNode = node.clone();
		
		assertTrue(node.getCurrentState() == clonedNode.getCurrentState());
		assertTrue(node.getName() == clonedNode.getName());
		assertTrue(node.isInstanced() == clonedNode.isInstanced());
		assertTrue(node.getStatesNumber() == clonedNode.getStatesNumber());
		for( int i = 0; i < node.getStatesNumber(); ++i)
			clonedNode.getStateIndex(node.getStateName(i));
		
	}
	
}

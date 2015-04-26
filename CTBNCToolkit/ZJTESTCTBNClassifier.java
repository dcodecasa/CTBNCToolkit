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

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCTBNClassifier {

	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#CTBNClassifier(java.lang.String)}.
	 */
	@Test
	public void testCTBNClassifierString() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testCTBNClassifierString", nodesNames, nodesNames[0], null);
		
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore");
		assertTrue(model.getName().equals("classificatore"));
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#CTBNClassifier(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCTBNClassifierStringException() {
		
		
		CTBNClassifier model = new CTBNClassifier(null, "classificatore");
		assertTrue(model.getName().equals("classificatore"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#CTBNClassifier(java.lang.String, java.util.Set)}.
	 */
	@Test
	public void testCTBNClassifierStringSetOfCTDiscreteNode() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testCTBNClassifierStringSetOfCTDiscreteNode", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		assertTrue(model.getName().equals("classificatore"));
		assertTrue(nodeIndexing.getNodesNumber() == 4);
		assertTrue(model.getClassNode() == classNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("Class")) == classNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("A")) == aNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("B")) == bNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("C")) == cNode);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#CTBNClassifier(java.lang.String, java.util.Set, java.lang.String)}.
	 */
	@Test
	public void testCTBNClassifierStringSetOfCTDiscreteNodeString() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testCTBNClassifierStringSetOfCTDiscreteNodeString", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		assertTrue(model.getName().equals("classificatore"));
		assertTrue(nodeIndexing.getNodesNumber() == 4);
		assertTrue(model.getClassNode() == classNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("Class")) == classNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("A")) == aNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("B")) == bNode);
		assertTrue(model.getNode(nodeIndexing.getIndex("C")) == cNode);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#generateTrajectory(double)}.
	 */
	@Test
	public void testGenerateTrajectory() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testGenerateTrajectory", nodesNames, nodesNames[0], null);
		
		double[][] cim;
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);
		// CIM
		cim = new double[1][2]; cim[0][0] = 0.5; cim[0][1] = 0.5; classNode.setCIM(0, cim);assertTrue(classNode.checkCIMs() == -1);
		cim = new double[2][2]; cim[0][0] = -0.1; cim[0][1] = 0.1; cim[1][0] = 0.1; cim[1][1] = -0.1; aNode.setCIM(0, cim);
		cim = new double[2][2]; cim[0][0] = -5; cim[0][1] = 5; cim[1][0] = 5; cim[1][1] = -5; aNode.setCIM(1, cim);assertTrue(aNode.checkCIMs() == -1);
		cim = new double[3][3]; cim[0][0] = -0.7; cim[0][1] = 0.5; cim[0][2] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[2][0] = 2; cim[2][1] = 1.3; cim[2][2] = -3.3;
		bNode.setCIM(0, cim);cNode.setCIM(0, cim);
		cim = new double[3][3]; cim[2][2] = -0.7; cim[2][1] = 0.5; cim[2][0] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[0][2] = 2; cim[0][1] = 1.3; cim[0][0] = -3.3;
		bNode.setCIM(1, cim); cNode.setCIM(1, cim);
		assertTrue(bNode.checkCIMs() == -1);
		assertTrue(cNode.checkCIMs() == -1);
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		double T = 1.0;
		ITrajectory<Double> trj = model.generateTrajectory(T);
		System.out.println(trj.toString());
		assertTrue( trj.getTransitionTime(0) == 0.0);
		assertTrue( trj.getTransitionTime(trj.getTransitionsNumber() - 1) == T);
		for(int iJmp = 1; iJmp < trj.getTransitionsNumber(); ++iJmp) {
			int count = 0;
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
				if( !trj.getNodeValue(iJmp - 1, iNode).equals( trj.getNodeValue(iJmp, iNode)))
					++count;
			
			if(iJmp != trj.getTransitionsNumber() - 1)
				assertTrue(count == 1);
			else
				assertTrue(count < 2);
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNClassifier#clone()}.
	 */
	@Test
	public void testClone() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClone", nodesNames, nodesNames[0], null);

		double[][] cim;
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);
		// CIM
		cim = new double[1][2]; cim[0][0] = 0.5; cim[0][1] = 0.5; classNode.setCIM(0, cim);assertTrue(classNode.checkCIMs() == -1);
		cim = new double[2][2]; cim[0][0] = -0.1; cim[0][1] = 0.1; cim[1][0] = 0.1; cim[1][1] = -0.1; aNode.setCIM(0, cim);
		cim = new double[2][2]; cim[0][0] = -5; cim[0][1] = 5; cim[1][0] = 5; cim[1][1] = -5; aNode.setCIM(1, cim);assertTrue(aNode.checkCIMs() == -1);
		cim = new double[3][3]; cim[0][0] = -0.7; cim[0][1] = 0.5; cim[0][2] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[2][0] = 2; cim[2][1] = 1.3; cim[2][2] = -3.3;
		bNode.setCIM(0, cim);cNode.setCIM(0, cim);
		cim = new double[3][3]; cim[2][2] = -0.7; cim[2][1] = 0.5; cim[2][0] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[0][2] = 2; cim[0][1] = 1.3; cim[0][0] = -3.3;
		bNode.setCIM(1, cim); cNode.setCIM(1, cim);
		assertTrue(bNode.checkCIMs() == -1);
		assertTrue(cNode.checkCIMs() == -1);
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		CTBNClassifier clonedModel = (CTBNClassifier) model.clone();
		
		boolean[][] adj = model.getAdjMatrix();
		boolean[][] cAdj = clonedModel.getAdjMatrix();
		assertTrue(adj.length == cAdj.length);
		assertTrue(adj[0].length == cAdj[0].length);
		for(int i = 0; i < adj.length; ++i)
			for(int j = 0; j < adj[0].length; ++j)
				assertTrue(adj[i][j] == cAdj[i][j]);
		if( model.getClassNode() == null)
			assertTrue( model.getClassNode() == clonedModel.getClassNode());
		else
			compareNodes((CTDiscreteNode) model.getClassNode(), (CTDiscreteNode) clonedModel.getClassNode());
		assertTrue(model.getName().equals(clonedModel.getName()));
		compareNodes((CTDiscreteNode) model.getNode(nodeIndexing.getIndex("Class")), (CTDiscreteNode) clonedModel.getNode(nodeIndexing.getIndex("Class")));
		compareNodes((CTDiscreteNode) model.getNode(nodeIndexing.getIndex("A")), (CTDiscreteNode) clonedModel.getNode(nodeIndexing.getIndex("A")));
		compareNodes((CTDiscreteNode) model.getNode(nodeIndexing.getIndex("B")), (CTDiscreteNode) clonedModel.getNode(nodeIndexing.getIndex("B")));
		compareNodes((CTDiscreteNode) model.getNode(nodeIndexing.getIndex("C")), (CTDiscreteNode) clonedModel.getNode(nodeIndexing.getIndex("C")));
	}

	/**
	 * Compare 2 nodes.
	 * 
	 * @param node1 first node
	 * @param node2 second node
	 */
	private void compareNodes(CTDiscreteNode node1, CTDiscreteNode node2) {

		assertTrue( node1.getCurrentState() == node2.getCurrentState() || node1.getCurrentState().equals( node2.getCurrentState()));
		assertTrue( node1.getName().equals(node2.getName()));
		assertTrue( node1.getNumberParentsEntries() == node2.getNumberParentsEntries());
		assertTrue( node1.getStatesNumber() == node2.getStatesNumber());
		assertTrue( node1.isInstanced() == node2.isInstanced());
		assertTrue( node1.isStaticNode() == node2.isStaticNode());
		for(int pE = 0; pE < node1.getNumberParentsEntries(); ++pE)
			for(int s0 = 0; s0 < node1.getStatesNumber(); ++s0)
				if( node1.isStaticNode())
					assertTrue(node1.getCIMValue(pE, 0, s0) == node2.getCIMValue(pE, 0, s0));
				else
					for(int s1 = 0; s1 < node1.getStatesNumber(); ++s1)
						assertTrue(node1.getCIMValue(pE, s0, s1) == node2.getCIMValue(pE, s0, s1));
		assertTrue(node1.getChildrenNumber() == node2.getChildrenNumber());
		assertTrue(node1.getParentsNumber() == node2.getParentsNumber());
		for(int i = 0; i < node1.getChildrenNumber(); ++i)
			compareNodes(node1.getChild(i), node2.getChild(i));
	}

	/**
	 * Test method for {@link CTBNCToolkit.DiscreteModel#addNode(CTBNCToolkit.IDiscreteNode)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddNodeException1() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testAddNodeException1", nodesNames, nodesNames[0], null);
		
		// Model generation
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		new CTDiscreteNode(nodesNames[0], states2, true);
		new CTDiscreteNode(nodesNames[1], states2, false);
		new CTDiscreteNode(nodesNames[2], states3, false);
		new CTDiscreteNode(nodesNames[3], states3, false);
		
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore");
		model.getNode(nodeIndexing.getClassIndex());
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.DiscreteModel#addNode(CTBNCToolkit.IDiscreteNode)}.
	 */
	@Test
	public void testAddNode() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testAddNode", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		classNode = new CTDiscreteNode("Class", states2, true);
		aNode = new CTDiscreteNode("A", states2, false);
		bNode = new CTDiscreteNode("B", states3, false);
		cNode = new CTDiscreteNode("C", states3, false);
		
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore");
		model.addNode(classNode);
		model.getNode(nodeIndexing.getIndex("Class"));
		model.addNode(aNode);
		model.getNode(nodeIndexing.getIndex("A"));
		model.addNode(bNode);
		model.getNode(nodeIndexing.getIndex("B"));
		model.addNode(cNode);
		model.getNode(nodeIndexing.getIndex("C"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.DiscreteModel#getAdjMatrix()}.
	 */
	@Test
	public void testGetAdjMatrix() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testAddNode", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);aNode.addParent(cNode);cNode.addChild(bNode);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		boolean[][] adj = model.getAdjMatrix();
		assertTrue(!adj[idxClass][idxClass]);assertTrue(adj[idxClass][idxA]);assertTrue(adj[idxClass][idxB]);assertTrue(adj[idxClass][idxC]);
		assertTrue(!adj[idxA][idxClass]);assertTrue(!adj[idxA][idxA]);assertTrue(!adj[idxA][idxB]);assertTrue(!adj[idxA][idxC]);
		assertTrue(!adj[idxB][idxClass]);assertTrue(!adj[idxB][idxA]);assertTrue(!adj[idxB][idxB]);assertTrue(!adj[idxB][idxC]);
		assertTrue(!adj[idxC][idxClass]);assertTrue(adj[idxC][idxA]);assertTrue(adj[idxC][idxB]);assertTrue(!adj[idxC][idxC]);
	}

	/**
	 * Test method for {@link CTBNCToolkit.DiscreteModel#setStructure(boolean[][])}.
	 */
	@Test
	public void testSetStructureBooleanArrayArray() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testAddNode", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);aNode.addParent(cNode);cNode.addChild(bNode);
		
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		boolean[][] adj = model.getAdjMatrix();
		
		nodes = new TreeSet<CTDiscreteNode>();
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		CTBNClassifier model2 = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		model2.setStructure(adj);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		boolean[][] adj2 = model2.getAdjMatrix();
		assertTrue(!adj2[idxClass][idxClass]);assertTrue(adj2[idxClass][idxA]);assertTrue(adj2[idxClass][idxB]);assertTrue(adj2[idxClass][idxC]);
		assertTrue(!adj2[idxA][idxClass]);assertTrue(!adj2[idxA][idxA]);assertTrue(!adj2[idxA][idxB]);assertTrue(!adj2[idxA][idxC]);
		assertTrue(!adj2[idxB][idxClass]);assertTrue(!adj2[idxB][idxA]);assertTrue(!adj2[idxB][idxB]);assertTrue(!adj2[idxB][idxC]);
		assertTrue(!adj2[idxC][idxClass]);assertTrue(adj2[idxC][idxA]);assertTrue(adj2[idxC][idxB]);assertTrue(!adj2[idxC][idxC]);
	}

	/**
	 * Test method for {@link CTBNCToolkit.DiscreteModel#setStructure(boolean[][], java.util.Map)}.
	 */
	@Test
	public void testSetStructureBooleanArrayArrayMapOfIntegerString() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testAddNode", nodesNames, nodesNames[0], null);
		
		// Model generation
		CTDiscreteNode classNode, aNode, bNode, cNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);classNode.addChild(cNode);aNode.addParent(cNode);cNode.addChild(bNode);
		
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		boolean[][] adj = model.getAdjMatrix();
		
		nodes = new TreeSet<CTDiscreteNode>();
		nodes.add(classNode = new CTDiscreteNode(nodesNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodesNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodesNames[2], states3, false));
		nodes.add(cNode = new CTDiscreteNode(nodesNames[3], states3, false));
		CTBNClassifier model2 = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		model2.setStructure(adj);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		boolean[][] adj2 = model2.getAdjMatrix();
		assertTrue(!adj2[idxClass][idxClass]);assertTrue(adj2[idxClass][idxA]);assertTrue(adj2[idxClass][idxB]);assertTrue(adj2[idxClass][idxC]);
		assertTrue(!adj2[idxA][idxClass]);assertTrue(!adj2[idxA][idxA]);assertTrue(!adj2[idxA][idxB]);assertTrue(!adj2[idxA][idxC]);
		assertTrue(!adj2[idxB][idxClass]);assertTrue(!adj2[idxB][idxA]);assertTrue(!adj2[idxB][idxB]);assertTrue(!adj2[idxB][idxC]);
		assertTrue(!adj2[idxC][idxClass]);assertTrue(adj2[idxC][idxA]);assertTrue(adj2[idxC][idxB]);assertTrue(!adj2[idxC][idxC]);
	}

}

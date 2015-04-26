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
public class ZJTESTCTBNCParameterLLAlgorithm {

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#CTBNCParameterLLAlgorithm()}.
	 */
	@Test
	public void testCTBNCParameterLLAlgorithm() {
		
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		assertTrue(alg.getMxxPrior() == 0.0);
		assertTrue(alg.getTxPrior() == 0.0);
		assertTrue(alg.getPxPrior() == 0.0);
		assertTrue(alg.getStructure() == null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#setParameters(java.util.Map)}
	 * and {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#setDefaultParameters()}.
	 */
	@Test
	public void testSetParameters() {
		
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 2.0);
		params.put("Px_prior", 3.0);
		alg.setParameters(params);
		assertTrue(alg.getMxxPrior() == 1.0);
		assertTrue(alg.getTxPrior() == 2.0);
		assertTrue(alg.getPxPrior() == 3.0);
		assertTrue(alg.getStructure() == null);
		
		alg.setDefaultParameters();
		assertTrue(alg.getMxxPrior() == 0.0);
		assertTrue(alg.getTxPrior() == 0.0);
		assertTrue(alg.getPxPrior() == 0.0);
		assertTrue(alg.getStructure() == null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#setStructure(boolean[][])}.
	 */
	@Test
	public void testSetStructure() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testSetStructure", nodesNames, nodesNames[0], null);
		
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
		
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[0][1] = true;adjMatrix[0][2] = true;adjMatrix[0][3] = true;
		
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 1.0);
		params.put("Px_prior", 1.0);
		alg.setParameters(params);
		alg.setStructure(adjMatrix);
		
		Set<ITrajectory<Double>> trSet = new TreeSet<ITrajectory<Double>>();
		
		alg.learn(clModel, trSet);
		assertTrue(classNode.getParentsNumber() == 0);
		assertTrue(classNode.getChildrenNumber() == 3);
		assertTrue(classNode.getNumberParentsEntries() == 1);
		assertTrue(aNode.getParentsNumber() == 1);
		assertTrue(aNode.getChildrenNumber() == 0);
		assertTrue(aNode.getNumberParentsEntries() == 2);
		assertTrue(bNode.getParentsNumber() == 1);
		assertTrue(bNode.getChildrenNumber() == 0);
		assertTrue(bNode.getNumberParentsEntries() == 2);
		assertTrue(cNode.getParentsNumber() == 1);
		assertTrue(cNode.getChildrenNumber() == 0);
		assertTrue(cNode.getNumberParentsEntries() == 2);
		assertTrue(classNode.getChild(0) == aNode);
		assertTrue(classNode.getChild(1) == bNode);
		assertTrue(classNode.getChild(2) == cNode);
		assertTrue(aNode.getParent(0) == classNode);
		assertTrue(bNode.getParent(0) == classNode);
		assertTrue(cNode.getParent(0) == classNode);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#helpParameters()}.
	 */
	@Test
	public void testHelpParameters() {
		System.out.println("TEST ZJTESTCTBNCParameterLLAlgorithm.testHelpParameters");
		
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		System.out.println(alg.helpParameters());
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearnPrior() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testLearnPrior", nodesNames, nodesNames[0], null);
		
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
		
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[0][1] = true;adjMatrix[0][2] = true;adjMatrix[0][3] = true;
		
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 1.0);
		params.put("Px_prior", 1.0);
		alg.setParameters(params);
		alg.setStructure(adjMatrix);
		
		Set<ITrajectory<Double>> trSet = new TreeSet<ITrajectory<Double>>();
		alg.learn(clModel, trSet);
		// classNode
		for(int pE = 0; pE < classNode.getNumberParentsEntries(); ++pE) {
			double[][] cim = classNode.getCIM(pE);
			assertTrue( cim.length == 1);
			assertTrue( cim[0].length == 2);
			assertTrue( cim[0][classNode.getStateIndex("s1")] == 0.5);
			assertTrue( cim[0][classNode.getStateIndex("s2")] == 0.5);
		}
		// aNode
		for(int pE = 0; pE < aNode.getNumberParentsEntries(); ++pE) {
			double[][] cim = aNode.getCIM(pE);
			assertTrue( cim.length == 2);
			assertTrue( cim[0].length == 2);
			assertTrue( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s1")] == -1.0);
			assertTrue( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s2")] == 1.0);
			assertTrue( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s1")] == 1.0);
			assertTrue( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s2")] == -1.0);
		}
		// bNode
		for(int pE = 0; pE < bNode.getNumberParentsEntries(); ++pE) {
			double[][] cim = bNode.getCIM(pE);
			assertTrue( cim.length == 3);
			assertTrue( cim[0].length == 3);
			assertTrue( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s1")] == -2.0);
			assertTrue( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s2")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s3")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s1")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s2")] == -2.0);
			assertTrue( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s3")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s1")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s2")] == 1.0);
			assertTrue( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s3")] == -2.0);
		}
		// cNode
		for(int pE = 0; pE < cNode.getNumberParentsEntries(); ++pE) {
			double[][] cim = cNode.getCIM(pE);
			assertTrue( cim.length == 3);
			assertTrue( cim[0].length == 3);
			assertTrue( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s1")] == -2.0);
			assertTrue( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s2")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s3")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s1")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s2")] == -2.0);
			assertTrue( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s3")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s1")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s2")] == 1.0);
			assertTrue( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s3")] == -2.0);
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTBNCParameterLLAlgorithm#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn() {
		
		String[] nodesNames = new String[4]; nodesNames[0] = "Class"; nodesNames[1] = "A"; nodesNames[2] = "B"; nodesNames[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testLearn", nodesNames, nodesNames[0], null);
		
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

		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[0][1] = true;adjMatrix[0][2] = true;adjMatrix[0][3] = true;
		
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		CTBNCParameterLLAlgorithm  alg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 1.0);
		params.put("Px_prior", 1.0);
		alg.setParameters(params);
		alg.setStructure(adjMatrix);
		
		Vector<ITrajectory<Double>> trVect = new Vector<ITrajectory<Double>>();
		// Times
		Vector<Double> times = new Vector<Double>();
		times.add(0.0);times.add(5.0); times.add(6.4);
		// Values (in the CTBN framework can transit only one node, but to test easily more value i relaxed this condition in the tests)
		String[] v;
		Vector<String[]> values = new Vector<String[]>();
		v = new String[4];										// time = 0.0
		v[nodeIndexing.getIndex("Class")] = "s2";v[nodeIndexing.getIndex("A")] = "s2";
		v[nodeIndexing.getIndex("B")] = "s1";v[nodeIndexing.getIndex("C")] = "s3";
		values.add(v);
		v = new String[4];										// time = 5.0
		v[0] = "s2";v[1] = "s1";v[2] = null;v[3] = "s2";
		values.add(v);
		v = new String[4];										// time = 6.4
		v[0] = null;v[1] = "s2";v[2] = null;v[3] = null;
		values.add(v);
		trVect.add(new CTTrajectory<Double>(nodeIndexing, times, values));
		
		values = new Vector<String[]>();
		v = new String[4];										// time = 0.0
		v[0] = "s1";v[1] = "s1";v[2] = "s2";v[3] = "s3";
		values.add(v);
		v = new String[4];										// time = 5.0
		v[0] = "s1";v[1] = null;v[2] = "s1";v[3] = null;
		values.add(v);
		v = new String[4];										// time = 6.4
		v[0] = null;v[1] = "s2";v[2] = null;v[3] = null;
		values.add(v);
		trVect.add(new CTTrajectory<Double>(nodeIndexing,times, values));
		alg.learn(clModel, trVect);
		//printAdjMatrix(clModel.getAdjMatrix());
		 
		double[][] cim;
		// classNode
		cim =  classNode.getCIM(0);
		assertTrue( cim.length == 1);
		assertTrue( cim[0].length == 2);
		assertTrue( equalDouble( cim[0][classNode.getStateIndex("s1")], 2/4.0));
		assertTrue( equalDouble( cim[0][classNode.getStateIndex("s2")], 2/4.0));
		// aNode (class = s1)
		cim = aNode.getCIM(classNode.getStateIndex("s1"));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s1")], -2.0/(7.4)));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s2")], 2.0/(7.4)*1.0));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s1")], 1.0));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s2")], -1.0));
		// aNode (class = s2)
		cim = aNode.getCIM(classNode.getStateIndex("s2"));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s1")], -(2.0/2.4)));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s1")][aNode.getStateIndex("s2")], (2.0/2.4)*1.0));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s1")], (2/6.0)*1.0));
		assertTrue( equalDouble( cim[aNode.getStateIndex("s2")][aNode.getStateIndex("s2")], -(2/6.0)));
		// bNode (class = s1)
		cim = bNode.getCIM(classNode.getStateIndex("s1"));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s1")], -(2.0/2.4)));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s2")], (2.0/2.4)*1.0/2.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s3")], (2.0/2.4)*1.0/2.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s1")], (3.0/6.0)*2.0/3.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s2")], -(3.0/6.0)));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s3")], (3.0/6.0)*1.0/3.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s1")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s2")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s3")], -2.0));
		// bNode (class = s2)
		cim = bNode.getCIM(classNode.getStateIndex("s2"));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s1")], -(2.0/7.4)));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s2")], (2.0/7.4)*0.5));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s1")][bNode.getStateIndex("s3")], (2.0/7.4)*0.5));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s1")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s2")], -2.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s2")][bNode.getStateIndex("s3")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s1")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s2")], 1.0));
		assertTrue( equalDouble( cim[bNode.getStateIndex("s3")][bNode.getStateIndex("s3")], -2.0));
		// cNode (class = s1)
		cim = cNode.getCIM(classNode.getStateIndex("s1"));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s1")], -2.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s2")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s3")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s1")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s2")], -2.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s3")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s1")], (2.0/7.4)*0.5));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s2")], (2.0/7.4)*0.5));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s3")], -(2.0/7.4)));
		// cNode (class = s2)
		cim = cNode.getCIM(classNode.getStateIndex("s2"));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s1")], -2.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s2")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s1")][cNode.getStateIndex("s3")], 1.0));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s1")], (2.0/2.4)*0.5));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s2")], -(2.0/2.4)));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s2")][cNode.getStateIndex("s3")], (2.0/2.4)*0.5));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s1")], (3.0/6.0)*(1.0/3.0)));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s2")], (3.0/6.0)*(2.0/3.0)));
		assertTrue( equalDouble( cim[cNode.getStateIndex("s3")][cNode.getStateIndex("s3")], -(3.0/6.0)));
	}
	
	private boolean equalDouble(double a, double b) {
		
		return Math.abs(a - b) < 0.000001; 
	}

	@SuppressWarnings("unused")
	private void printCIM(double[][] cim) {
		for(int i = 0; i < cim.length; ++i) {
			for(int j = 0; j < cim[0].length; ++j)
				System.out.print(" [" + cim[i][j] + "] ");
			System.out.println();
		}
	}
	
	@SuppressWarnings("unused")
	private void printAdjMatrix(boolean[][] matrix) {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix[0].length; ++j)
				if(matrix[i][j])
					System.out.print("1");
				else
					System.out.print("0");
			System.out.println();
		}
	}
}

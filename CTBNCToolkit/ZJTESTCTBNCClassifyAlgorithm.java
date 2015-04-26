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
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCTBNCClassifyAlgorithm {

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCClassifyAlgorithm#classify(CTBNCToolkit.ICTClassifier, CTBNCToolkit.ITrajectory)}.
	 */
	@Test
	public void testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleString() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "Class"; nodeNames[1] = "A"; nodeNames[2] = "B";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleString", nodeNames, nodeNames[0], null);
		int idxClass = nodeIndexing.getIndex("Class");
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		
		double[][] cim;
		// Model generation
		CTDiscreteNode classNode, aNode, bNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodeNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodeNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodeNames[2], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);
		// CIM
		cim = new double[1][2]; cim[0][0] = 0.6; cim[0][1] = 1.0-cim[0][0]; classNode.setCIM(0, cim);assertTrue(classNode.checkCIMs() == -1);
		cim = new double[2][2]; cim[0][0] = -0.1; cim[0][1] = 0.1; cim[1][0] = 0.4; cim[1][1] = -0.4; aNode.setCIM(0, cim);
		cim = new double[2][2]; cim[0][0] = -0.3; cim[0][1] = 0.3; cim[1][0] = 0.5; cim[1][1] = -0.5; aNode.setCIM(1, cim);assertTrue(aNode.checkCIMs() == -1);
		cim = new double[3][3]; cim[0][0] = -0.7; cim[0][1] = 0.5; cim[0][2] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[2][0] = 2; cim[2][1] = 1.3; cim[2][2] = -3.3;
		bNode.setCIM(0, cim);
		cim = new double[3][3]; cim[2][2] = -0.7; cim[2][1] = 0.5; cim[2][0] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[0][2] = 2; cim[0][1] = 1.3; cim[0][0] = -3.3;
		bNode.setCIM(1, cim);
		assertTrue(bNode.checkCIMs() == -1);
		// Model
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		// Trajectory Generation
		// Times
		Vector<Double> times = new Vector<Double>();
		times.add(0.0);times.add(5.0);
		// Values (in the CTBN framework can transit only one node, but to test easily more value i relaxed this condition in the tests)
		String[] v;
		Vector<String[]> values = new Vector<String[]>();
		v = new String[3];						// time = 0.0
		v[idxClass] = "s2";v[idxA] = "s2";v[idxB] = "s1";
		values.add(v);
		v = new String[3];						// time = 5.0
		v[idxClass] = "s2";v[idxA] = "s1";v[idxB] = null;
		values.add(v);
		// Trajectory
		ITrajectory<Double> trj = new CTTrajectory<Double>(nodeIndexing, times, values);
		// Inference
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("probabilities", true);
		CTBNCClassifyAlgorithm clAlgorithm = new CTBNCClassifyAlgorithm();
		clAlgorithm.setParameters(params);
		
		IClassificationResult<Double> result;		
		int nTest = 10000;
		double acc = 0.0;
		for(int i = 0; i < nTest; ++i) {
			trj = clModel.generateTrajectory(10.0);
			result = clAlgorithm.classify(clModel, trj);
			if( trj.getNodeValue(0, nodeIndexing.getClassIndex()).equals( result.getClassification())) {				
				++acc;
				//System.out.println("OK (" + result.getClassification() + ") s1 = " + result.getNotNormalizedValue(result.getTransitionsNumber() - 1, "s1"));
				//System.out.println("OK (" + result.getClassification() + ") s2 = " + result.getNotNormalizedValue(result.getTransitionsNumber() - 1, "s2"));
			} else {
				//System.out.println("NO (" + result.getClassification() + ") s1 = " + result.getNotNormalizedValue(result.getTransitionsNumber() - 1, "s1"));
				//System.out.println("NO (" + result.getClassification() + ") s2 = " + result.getNotNormalizedValue(result.getTransitionsNumber() - 1, "s2"));
			}
			
			// Check the probabilities validity
			double sum;
			for( int jmp = 0; jmp < result.getTransitionsNumber(); ++jmp)
			{
				sum = 0.0;
				for(int j = 0; j < classNode.getStatesNumber(); ++j)
					sum += result.getProbability(jmp, classNode.getStateName(j));
				assertTrue(sum > 0.9999 && sum < 1.0001);
			}
			sum = 0.0;
			for(int j = 0; j < classNode.getStatesNumber(); ++j)
				sum += result.getProbability(classNode.getStateName(j));
			assertTrue(sum > 0.9999 && sum < 1.0001);
		}
		acc /= nTest;
		System.out.println("accuracy = " + acc);
		assertTrue(acc > 0.99);
		
		//printTrajectory(clModel.generateTrajectory(10.0));
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTBNCClassifyAlgorithm#classify(CTBNCToolkit.ICTClassifier, CTBNCToolkit.ITrajectory)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testClassifyRandom() {
		
		int nTest = 10;				// nTest models
		int inTest = 10;			// tested inTest times
		int rejectedModel = 0;
		double acc = 0.0;
		for(int iTest = 0; iTest < nTest + rejectedModel; ++iTest) {
			System.out.println("Model " + iTest + " generation");
			
			// Model generation
			TreeSet<String> states;
			int nStates;
			Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
			assertTrue(nodes.size() == 0);
			
			states = new TreeSet<String>();												// class generation
			nStates = (int) Math.round(Math.random()*1)+2;
			for(int s = 0; s < nStates; ++s)
				states.add("c" + s);
			CTDiscreteNode classNode = new CTDiscreteNode("Class", states, true);
			nodes.add(classNode);
			
			Vector<CTDiscreteNode> staticNodes = new Vector<CTDiscreteNode>();			// nodes generation
			Vector<CTDiscreteNode> nodesVector = new Vector<CTDiscreteNode>();
			int nNodes = (int) Math.round(Math.random()*5)+2;
			String[] nodeNames = new String[nNodes + 1];
			for(int iNode = 0; iNode < nNodes; ++iNode) {
				states = new TreeSet<String>();
				nStates = (int) Math.round(Math.random()*1)+2;
				for(int s = 0; s < nStates; ++s)
					states.add("s" + s);
				
				nodeNames[iNode] = "n" + iNode;
				boolean isStatic = (Math.random() > 1);//TODO static can not have parents in the current version //? 0.8);
				CTDiscreteNode node = new CTDiscreteNode(nodeNames[iNode], states, isStatic);
				if(isStatic)
					staticNodes.add(node);
				nodesVector.add(node);
				nodes.add(node);
			}
			nodeNames[nNodes] = "Class";
			assertTrue(nodeNames.length == nNodes + 1);
			assertTrue(nodes.size() == nNodes + 1);
			
			NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClassifyRandom-" + iTest, nodeNames, nodeNames[nNodes], null);
			if( iTest != 0)
				assertTrue(nodeIndexing != NodeIndexing.getNodeIndexing("testClassifyRandom-" + (iTest - 1)));
			
			for(int iNode = 0; iNode < nNodes; ++iNode) {								// graphical structure generation
			
				Vector<CTDiscreteNode> samplingVector;
				CTDiscreteNode node = nodesVector.get(iNode);
				if( node.isStaticNode())
					samplingVector = (Vector<CTDiscreteNode>) staticNodes.clone();
				else
					samplingVector = (Vector<CTDiscreteNode>) nodesVector.clone();
				
				int nParent = (int) Math.round(Math.random()*1);
				if( nParent > (samplingVector.size() - 1))
					nParent = samplingVector.size() - 1;
				
				for( int iParent = 0; iParent < nParent; ++iParent) {
					int sampledParent = (int) Math.round(Math.random()*(samplingVector.size() - iParent - 1));
					CTDiscreteNode sampledNode = samplingVector.get(sampledParent);
					if( sampledNode == node) {
						--iParent;
						continue;
					}
					
					node.addParent(sampledNode);
					samplingVector.add(sampledParent, samplingVector.get(samplingVector.size() - iParent - 1));
					samplingVector.add(samplingVector.size() - iParent - 1, sampledNode);
				}
					
				node.addParent(classNode);
			}
			
			nodesVector.add(classNode);													// cim generation
			for(int iNode = 0; iNode < nodesVector.size(); ++iNode) {
				CTDiscreteNode node = nodesVector.get(iNode);
				for(int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					double[][] cim;
					if( node.isStaticNode()) {
						cim = new double[1][node.getStatesNumber()];
						cim[0] = generateProbabilityDistribution(node.getStatesNumber());
					} else {
						cim = new double[node.getStatesNumber()][node.getStatesNumber()];
						for(int s = 0; s < node.getStatesNumber(); ++s) {
							double q = Math.random()*0.5+1;
							double[] p = generateProbabilityDistribution(node.getStatesNumber() - 1);
							
							int count = 0;
							for( int i = 0; i < node.getStatesNumber(); ++i) {
								if( i == s)
									cim[s][i] = -q;
								else
									cim[s][i] = q*p[count++];
							}
						}
					}
					node.setCIM(pE, cim);
				}
				assertTrue( node.checkCIMs() == -1);
			}
			assertTrue( classNode.getChildrenNumber() == nNodes);
	
			CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);		// Model instantiation
			System.out.println("N nodes = " + nNodes + " +1");
			System.out.println("Class node (" + nodeIndexing.getClassIndex() + ") has " + classNode.getChildrenNumber() + " children");
			printAdjMatrix( clModel.getAdjMatrix());
			
			// Inference
			ITrajectory<Double> trj;
			CTBNCClassifyAlgorithm clAlgorithm = new CTBNCClassifyAlgorithm();
			IClassificationResult<Double> result;
		
			int nReject = 0;
			int iAcc = 0;
			for( int iiTest = 0; iiTest < inTest && nReject != 2; ++iiTest)
			{
				System.out.println("Model " + iTest + " test " + iiTest);
				trj = clModel.generateTrajectory(30.0);
				if(trj.getTransitionsNumber() < 50) {
					System.out.println("Trajectory with " + trj.getTransitionsNumber() + " transitions REJECTED!");
					--iiTest;
					++nReject;
				} else {
					nReject = 0;
				}
				
				if(nReject == 2) {
					System.out.println(" MODEL REJECTED!");
					++rejectedModel;
					iAcc = 0;
				} else {
					System.out.println("Inference over a " + trj.getTransitionsNumber() + " transitions trajectory");
					result = clAlgorithm.classify(clModel, trj);
					if( trj.getNodeValue(0, nodeIndexing.getClassIndex()).equals( result.getClassification()))				
						++iAcc;
				}
			}
			acc += iAcc;
		}
		acc /= nTest*inTest;
		System.out.println("accuracy = " + acc);
		assertTrue(acc > 0.7);
		
		//printTrajectory(clModel.generateTrajectory(10.0));
	}
	
	void printAdjMatrix(boolean[][] matrix) {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix.length; ++j)
				if(matrix[i][j])
					System.out.print("1");
				else
					System.out.print("0");
			System.out.println();
		}
	}
	
	double[] generateProbabilityDistribution(int length) {
		
		double[] p = new double[length];
		double sum = 0.0;
		for(int i = 0; i < length - 1; ++i) {
			p[i] = Math.random()*(1-sum); 
			sum += p[i];
		}
		p[length - 1] = 1 - sum;
		
		boolean reject = false;
		for(int i = 0; i < length; ++i) {
			if( p[i] < 0.2)
				reject = true;
		}
		if( reject)
			p = generateProbabilityDistribution(length);
		
		return p;
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.CTBNCClassifyAlgorithm#classify(CTBNCToolkit.ICTClassifier, CTBNCToolkit.ITrajectory, double)}.
	 */
	@Test
	public void testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleStringDouble() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "Class"; nodeNames[1] = "A"; nodeNames[2] = "B";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleString", nodeNames, nodeNames[0], null);
		int idxClass = nodeIndexing.getIndex("Class");
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		
		double[][] cim;
		// Model generation
		CTDiscreteNode classNode, aNode, bNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodeNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodeNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodeNames[2], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);
		// CIM
		cim = new double[1][2]; cim[0][0] = 0.6; cim[0][1] = 1.0-cim[0][0]; classNode.setCIM(0, cim);assertTrue(classNode.checkCIMs() == -1);
		cim = new double[2][2]; cim[0][0] = -0.1; cim[0][1] = 0.1; cim[1][0] = 0.4; cim[1][1] = -0.4; aNode.setCIM(0, cim);
		cim = new double[2][2]; cim[0][0] = -0.3; cim[0][1] = 0.3; cim[1][0] = 0.5; cim[1][1] = -0.5; aNode.setCIM(1, cim);assertTrue(aNode.checkCIMs() == -1);
		cim = new double[3][3]; cim[0][0] = -0.7; cim[0][1] = 0.5; cim[0][2] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[2][0] = 2; cim[2][1] = 1.3; cim[2][2] = -3.3;
		bNode.setCIM(0, cim);
		cim = new double[3][3]; cim[2][2] = -0.7; cim[2][1] = 0.5; cim[2][0] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[0][2] = 2; cim[0][1] = 1.3; cim[0][0] = -3.3;
		bNode.setCIM(1, cim);
		assertTrue(bNode.checkCIMs() == -1);
		// Model
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		// Trajectory Generation
		// Times
		Vector<Double> times = new Vector<Double>();
		times.add(0.0);times.add(2.2);times.add(5.0);
		// Values (in the CTBN framework can transit only one node, but to test easily more value i relaxed this condition in the tests)
		String[] v;
		Vector<String[]> values = new Vector<String[]>();
		v = new String[3];										// time = 0.0
		v[idxClass] = "s2";v[idxA] = "s2";v[idxB] = "s1";
		values.add(v);
		v = new String[3];										// time = 2.2
		v[idxClass] = null;v[idxA] = null;v[idxB] = "s3";
		values.add(v);
		v = new String[3];										// time = 5.0
		v[idxClass] = "s2";v[idxA] = "s1";v[idxB] = null;
		values.add(v);
		// Trajectory
		ITrajectory<Double> trj = new CTTrajectory<Double>(nodeIndexing, times, values);
		// Inference
		CTBNCClassifyAlgorithm clAlgorithm = new CTBNCClassifyAlgorithm();
		//this.printTrajectory(trj);
		IClassificationResult<Double> result = clAlgorithm.classify(clModel, trj, 0.9);
		//this.printTrajectory(result);
		assertTrue( result.getTransitionsNumber() == 8);
		assertTrue( result.getTransitionTime(0) == 0.0);
		assertTrue( result.getTransitionTime(1) == 0.9);
		assertTrue( result.getTransitionTime(2) == 1.8);
		assertTrue( result.getTransitionTime(3) == 2.2);
		assertTrue( result.getTransitionTime(4) == 2.7);
		assertTrue( result.getTransitionTime(5) == 3.6);
		assertTrue( result.getTransitionTime(6) == 4.5);
		assertTrue( result.getTransitionTime(7) == 5.0);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCClassifyAlgorithm#classify(CTBNCToolkit.ICTClassifier, CTBNCToolkit.ITrajectory, java.util.Vector)}.
	 */
	@Test
	public void testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleStringVectorOfDouble() {
		
		String[] nodeNames = new String[3]; nodeNames[0] = "Class"; nodeNames[1] = "A"; nodeNames[2] = "B";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testClassifyICTClassifierOfDoubleStringCTDiscreteNodeITrajectoryOfDoubleString", nodeNames, nodeNames[0], null);
		int idxClass = nodeIndexing.getIndex("Class");
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		
		double[][] cim;
		// Model generation
		CTDiscreteNode classNode, aNode, bNode;
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(classNode = new CTDiscreteNode(nodeNames[0], states2, true));
		nodes.add(aNode = new CTDiscreteNode(nodeNames[1], states2, false));
		nodes.add(bNode = new CTDiscreteNode(nodeNames[2], states3, false));
		classNode.addChild(aNode);classNode.addChild(bNode);
		// CIM
		cim = new double[1][2]; cim[0][0] = 0.6; cim[0][1] = 1.0-cim[0][0]; classNode.setCIM(0, cim);assertTrue(classNode.checkCIMs() == -1);
		cim = new double[2][2]; cim[0][0] = -0.1; cim[0][1] = 0.1; cim[1][0] = 0.4; cim[1][1] = -0.4; aNode.setCIM(0, cim);
		cim = new double[2][2]; cim[0][0] = -0.3; cim[0][1] = 0.3; cim[1][0] = 0.5; cim[1][1] = -0.5; aNode.setCIM(1, cim);assertTrue(aNode.checkCIMs() == -1);
		cim = new double[3][3]; cim[0][0] = -0.7; cim[0][1] = 0.5; cim[0][2] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[2][0] = 2; cim[2][1] = 1.3; cim[2][2] = -3.3;
		bNode.setCIM(0, cim);
		cim = new double[3][3]; cim[2][2] = -0.7; cim[2][1] = 0.5; cim[2][0] = 0.2; cim[1][0] = 1.0; cim[1][1] = -1.6; cim[1][2] = 0.6; cim[0][2] = 2; cim[0][1] = 1.3; cim[0][0] = -3.3;
		bNode.setCIM(1, cim);
		assertTrue(bNode.checkCIMs() == -1);
		// Model
		CTBNClassifier clModel = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		// Trajectory Generation
		// Times
		Vector<Double> times = new Vector<Double>();
		times.add(0.0);times.add(2.2);times.add(5.0);
		// Values (in the CTBN framework can transit only one node, but to test easily more value i relaxed this condition in the tests)
		String[] v;
		Vector<String[]> values = new Vector<String[]>();
		v = new String[3];										// time = 0.0
		v[idxClass] = "s2";v[idxA] = "s2";v[idxB] = "s1";
		values.add(v);
		v = new String[3];										// time = 2.2
		v[idxClass] = null;v[idxA] = null;v[idxB] = "s3";
		values.add(v);
		v = new String[3];										// time = 5.0
		v[idxClass] = "s2";v[idxA] = "s1";v[idxB] = null;
		values.add(v);
		// Trajectory
		ITrajectory<Double> trj = new CTTrajectory<Double>(nodeIndexing, times, values);
		// Inference
		Vector<Double> timesChecks = new Vector<Double>();
		timesChecks.add(0.1);timesChecks.add(0.2);timesChecks.add(0.3);timesChecks.add(2.1);timesChecks.add(2.2);timesChecks.add(2.3);
		CTBNCClassifyAlgorithm clAlgorithm = new CTBNCClassifyAlgorithm();
		//this.printTrajectory(trj);
		IClassificationResult<Double> result = clAlgorithm.classify(clModel, trj, timesChecks);
		//this.printTrajectory(result);
		assertTrue( result.getTransitionsNumber() == 8);
		assertTrue( result.getTransitionTime(0) == 0.0);
		assertTrue( result.getTransitionTime(1) == 0.1);
		assertTrue( result.getTransitionTime(2) == 0.2);
		assertTrue( result.getTransitionTime(3) == 0.3);
		assertTrue( result.getTransitionTime(4) == 2.1);
		assertTrue( result.getTransitionTime(5) == 2.2);
		assertTrue( result.getTransitionTime(6) == 2.3);
		assertTrue( result.getTransitionTime(7) == 5.0);
	}

}

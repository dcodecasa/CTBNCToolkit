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
package CTBNCToolkit.optimization;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

import CTBNCToolkit.CTBNCParameterLLAlgorithm;
import CTBNCToolkit.CTBNClassifier;
import CTBNCToolkit.CTDiscreteNode;
import CTBNCToolkit.CTTrajectory;
import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IModel;
import CTBNCToolkit.ITrajectory;
import CTBNCToolkit.NodeIndexing;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCTBNCHillClimbingIndividual {

	/**
	 * Test method for {@link CTBNCToolkit.optimization.CTBNCHillClimbingIndividual#CTBNCHillClimbingIndividual(CTBNCToolkit.IModel, java.lang.String, CTBNCToolkit.CTBNCParameterLLAlgorithm, java.util.Collection, int)}
	 * and for {@link CTBNCToolkit.optimization.CTBNCHillClimbingIndividual#getModel()}.
	 */
	@Test
	public void testCTBNCHillClimbingIndividual() {
		
		generateDataset( 1);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		CTBNCParameterLLAlgorithm alg = generateLLParamsAlgorithm(1.0, 1.0, 1.0);
		LLHillClimbingFactory factory = new LLHillClimbingFactory(alg, 2, true, false);
		CTBNCHillClimbingIndividual ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("A"));
		assertTrue(ind.getModel() == model);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.optimization.CTBNCHillClimbingIndividual#CTBNCHillClimbingIndividual(CTBNCToolkit.IModel, java.lang.String, CTBNCToolkit.CTBNCParameterLLAlgorithm, java.util.Collection, int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCTBNCHillClimbingIndividualException() {
		
		Collection<ITrajectory<Double>> dataset = generateDataset( 10);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		CTBNCParameterLLAlgorithm alg = generateLLParamsAlgorithm(1.0, 1.0, 1.0);		
		LLHillClimbingFactory factory = new LLHillClimbingFactory(alg, 2, true, false);
		factory.setDataset(dataset);
		@SuppressWarnings("unused")
		CTBNCHillClimbingIndividual ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("Class"));
	}

	/**
	 * Test method for {@link CTBNCToolkit.optimization.CTBNCHillClimbingIndividual#evaluate()}.
	 */
	@Test
	public void testEvaluate() {
		System.out.println("TEST: ZJTESTCTBNCHillClimbingIndividual.testEvaluate");
		
		Collection<ITrajectory<Double>> dataset = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		CTBNCParameterLLAlgorithm alg = generateLLParamsAlgorithm(1.0, 1.0, 1.0);
		LLHillClimbingFactory factory = new LLHillClimbingFactory(alg, 3, true, false);
		factory.setDataset(dataset);
		CTBNCHillClimbingIndividual ind;
		
		boolean[][] adjMatrix = new boolean[nodeIndexing.getNodesNumber()][nodeIndexing.getNodesNumber()];
		for( int i = 0 ; i < adjMatrix.length; ++i)
			for( int j = 0; j < adjMatrix.length; ++j)
				adjMatrix[i][j] = false;
		for( int j = 0; j < adjMatrix.length; ++j)
			if( j != nodeIndexing.getClassIndex())
				adjMatrix[nodeIndexing.getClassIndex()][j] = true;
		
		boolean[][] testedAdjMatrix;
		// Class,A -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		testedAdjMatrix[nodeIndexing.getIndex("A")][nodeIndexing.getIndex("C")] = true;
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		double clAVal = ind.evaluate();
		System.out.println("Class,A->C = " + ind.evaluate());
		// Class -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		System.out.println("Class->C = " + ind.evaluate());
		assertTrue(clAVal > ind.evaluate());
		// Class,B -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		testedAdjMatrix[nodeIndexing.getIndex("B")][nodeIndexing.getIndex("C")] = true;
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		System.out.println("Class,B->C = " + ind.evaluate());
		assertTrue(clAVal > ind.evaluate());
		// Class,A,B -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		testedAdjMatrix[nodeIndexing.getIndex("A")][nodeIndexing.getIndex("C")] = true;
		testedAdjMatrix[nodeIndexing.getIndex("B")][nodeIndexing.getIndex("C")] = true;
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		System.out.println("Class,A,B->C = " + ind.evaluate());
		assertTrue(clAVal > ind.evaluate());
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.optimization.CTBNCHillClimbingIndividual#getBestNeighbor()}.
	 */
	@Test
	public void testGetBestNeighbor() {
		
		Collection<ITrajectory<Double>> dataset = generateDataset( 10);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		CTBNCParameterLLAlgorithm alg = generateLLParamsAlgorithm(1.0, 1.0, 1.0);
		LLHillClimbingFactory factory = new LLHillClimbingFactory(alg, 4, true, false);
		factory.setDataset(dataset);
		CTBNCHillClimbingIndividual ind;
		
		boolean[][] adjMatrix = new boolean[nodeIndexing.getNodesNumber()][nodeIndexing.getNodesNumber()];
		for( int i = 0 ; i < adjMatrix.length; ++i)
			for( int j = 0; j < adjMatrix.length; ++j)
				adjMatrix[i][j] = false;
		for( int j = 0; j < adjMatrix.length; ++j)
			if( j != nodeIndexing.getClassIndex())
				adjMatrix[nodeIndexing.getClassIndex()][j] = true;
		
		IModel<Double, CTDiscreteNode> bestNModel;
		boolean[][] bestNeighMatrix;
		boolean[][] testedAdjMatrix;
		// Class,B -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		testedAdjMatrix[nodeIndexing.getIndex("B")][nodeIndexing.getIndex("C")] = true;
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		bestNeighMatrix = cloneAdjMatrix(adjMatrix);
		bestNModel = ind.getBestNeighbor(null).getModel();;
		compareAdjMatrix( bestNModel.getAdjMatrix(), bestNModel, bestNeighMatrix, model);
		// Class,A,B -> C
		testedAdjMatrix = cloneAdjMatrix(adjMatrix);
		testedAdjMatrix[nodeIndexing.getIndex("A")][nodeIndexing.getIndex("C")] = true;
		testedAdjMatrix[nodeIndexing.getIndex("B")][nodeIndexing.getIndex("C")] = true;
		model.setStructure(testedAdjMatrix);
		ind = new CTBNCHillClimbingIndividual(factory, model, nodeIndexing.getIndex("C"));
		bestNeighMatrix = cloneAdjMatrix(adjMatrix);
		bestNeighMatrix[nodeIndexing.getIndex("A")][nodeIndexing.getIndex("C")] = true;
		bestNModel = ind.getBestNeighbor(null).getModel();
		compareAdjMatrix( bestNModel.getAdjMatrix(), bestNModel, bestNeighMatrix, model);
		
	}
	
	public static CTBNClassifier generateClassifierModel() {
		
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		// Model generation
		Set<String> states2 = new TreeSet<String>();
		Set<String> states3 = new TreeSet<String>();
		Set<CTDiscreteNode> nodes = new TreeSet<CTDiscreteNode>();
		states2.add("s1");states2.add("s2");
		states3.add("s1");states3.add("s2");states3.add("s3");
		nodes.add(new CTDiscreteNode("Class", states2, true));
		nodes.add(new CTDiscreteNode("A", states2, false));
		nodes.add(new CTDiscreteNode("B", states2, false));
		nodes.add(new CTDiscreteNode("C", states3, false));
		// Model
		CTBNClassifier model = new CTBNClassifier(nodeIndexing, "classificatore", nodes);
		
		return model;
	}

	/**
	 * Generate the dataset from a structure of this type:
	 * Class->A Class->B Class->C A->C
	 * 
	 * @param N number of trajectory to generate
	 * @return the generated dataset
	 */
	public static Vector<ITrajectory<Double>> generateDataset(int N) {
		
		Vector<ITrajectory<Double>> dataset = new Vector<ITrajectory<Double>>(N);
		for( int i = 0; i < N; ++i)
			dataset.add(generateTrajectory());
		
		return dataset;
	}
	
	public static ITrajectory<Double> generateTrajectory() {
		
		String[] names = new String[4]; names[0] = "Class"; names[1] = "A"; names[2] = "B"; names[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset", names, names[0], null);
		
		String[] v;
		List<String[]> values = new Vector<String[]>();
		List<Double> times = new Vector<Double>();
	
		int classState, aState, bState, cState;
		// t = 0.0
		times.add(0.0);
		v = new String[4];
		if( Math.random() < 0.7) {	// Class
			v[nodeIndexing.getIndex("Class")] = "s1"; classState = 1;
		}else {
			v[nodeIndexing.getIndex("Class")] = "s2"; classState = 2;
		}
		if( Math.random() < 0.5) {	// A
			v[nodeIndexing.getIndex("A")] = "s1"; aState = 1;
		}else {
			v[nodeIndexing.getIndex("A")] = "s2"; aState = 2;	
		}
		if( Math.random() < 0.5) {	// B
			v[nodeIndexing.getIndex("B")] = "s1"; bState = 1;
		}else {
			v[nodeIndexing.getIndex("B")] = "s2"; bState = 2;	
		}
		if( Math.random() < 1/3) {	// C
			v[nodeIndexing.getIndex("C")] = "s1"; cState = 1;
		}else if( Math.random() < 1/2) { 
			v[nodeIndexing.getIndex("C")] = "s2"; cState = 2;	
		}else {
			v[nodeIndexing.getIndex("C")] = "s3"; cState = 3;
		}
		values.add(v);
		// t > 0
		for(int t = 1; t < 100; ++t) {
			// C
			times.add((t * 0.1) * (classState - 1)*2);
			v = new String[4];
			if( aState == 1)
				++cState;
			else
				--cState;
			if( cState == 1 || cState == 4) {
				v[nodeIndexing.getIndex("C")] = "s1"; cState = 1;
			}else if( cState == 2) {
				v[nodeIndexing.getIndex("C")] = "s2"; cState = 2;
			}else if( cState == 3 || cState == 0) {
				v[nodeIndexing.getIndex("C")] = "s3"; cState = 3;
			}else
				assertTrue(false);
			values.add(v);
			// A
			if(t % 10 == 0)	{
				times.add((t * 0.1 + 0.01) * (classState - 1)*2);
				v = new String[4];
				if( aState == 1) { 
					v[nodeIndexing.getIndex("A")] = "s2"; aState = 2;
				}else {
					v[nodeIndexing.getIndex("A")] = "s1"; aState = 1;
				}
				values.add(v);
			}
			// B
			if( t % 20 == 0) {
				times.add((t * 0.1 + 0.02) * (classState - 1)*2);
				v = new String[4];
				if( bState == 1) { 
					v[nodeIndexing.getIndex("B")] = "s2"; bState = 2;
				}else {
					v[nodeIndexing.getIndex("B")] = "s1"; bState = 1;
				}
				values.add(v);
			}
		}
		
		return new CTTrajectory<Double>(nodeIndexing, times, values);
	}
	
	public CTBNCParameterLLAlgorithm generateLLParamsAlgorithm(double Mxx_prior, double Tx_prior, double Px_prior) {
		
		CTBNCParameterLLAlgorithm paramsLAlg = new CTBNCParameterLLAlgorithm();
		Map<String, Object> lParams = new TreeMap<String,Object>();
		lParams.put("Mxx_prior", Mxx_prior);
		lParams.put("Tx_prior", Tx_prior);
		lParams.put("Px_prior", Px_prior);
		paramsLAlg.setParameters(lParams);
		
		return paramsLAlg;
	}
	
	private static boolean[][] cloneAdjMatrix(boolean[][] adjMatrix) {
		
		boolean[][] newAdjMatrix = new boolean[adjMatrix.length][adjMatrix.length];
		for(int i = 0; i < adjMatrix.length; ++i)
			newAdjMatrix[i] = adjMatrix[i].clone();
		
		return newAdjMatrix;
	}

	private static void compareAdjMatrix(boolean[][] adjM1, IModel<Double, CTDiscreteNode> model1, boolean[][] adjM2, IModel<Double, CTDiscreteNode> model2) {
		
		assertTrue( adjM1.length == adjM2.length);
		for(int i = 0; i < adjM1.length; ++i) {
			assertTrue( adjM1[i].length == adjM2[i].length);
			for( int j = 0; j < adjM1[i].length; ++j) {
				assertTrue( adjM1[i][j] == adjM2[i][j]);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void printAdjMatrix(boolean[][] matrix) {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix.length; ++j)
				if(matrix[i][j])
					System.out.print("1");
				else
					System.out.print("0");
			System.out.println();
		}
	}	
}

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

import java.util.*;

import org.junit.Test;

import CTBNCToolkit.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCLLHillClimbingFactory {

	/**
	 * Test method for {@link CTBNCToolkit.optimization.CLLHillClimbingFactory#CLLHillClimbingFactory(CTBNCToolkit.ILearningAlgorithm, int, boolean, boolean)}.
	 */
	@Test
	public void testCLLHillClimbingFactory() {

		generateDataset( 1);
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
				
		CTBNCParameterLLAlgorithm paramsLAlg = new CTBNCParameterLLAlgorithm();
		Map<String, Object> lParams = new TreeMap<String,Object>();
		lParams.put("Mxx_prior", 1.0);
		lParams.put("Tx_prior", 0.1);
		lParams.put("Px_prior", 1.0);
		paramsLAlg.setParameters(lParams);
		
		CLLHillClimbingFactory factory = new CLLHillClimbingFactory(paramsLAlg, 3, true, false);
		
		CTBNCHillClimbingIndividual ind;
		ind = factory.newInstance(model, nodeIndexing.getIndex("A"));
		assertTrue(ind.getModel() == model);
		ind = factory.newInstance(model, nodeIndexing.getIndex("A"), 4.0);
		assertTrue(ind.evaluate() == 4.0);
		
		assertTrue(factory.getDimensionPenalty() == true);
		assertTrue(factory.getFeatureSelectionMode() == false);
		assertTrue(factory.getMaxParents() == 3);
	}

	/**
	 * Test method for {@link CTBNCToolkit.optimization.CLLHillClimbingFactory#evaluate(CTBNCToolkit.IModel, java.lang.String)}.
	 */
	@Test
	public void testEvaluate1() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 100);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		int idxClass = nodeIndexing.getIndex("Class");
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		CLLHillClimbingFactory elemFactory = new CLLHillClimbingFactory(paramsAlg, 4, false, true);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		printAdjMatrix(model);
		//?TODO assertTrue(learnedStructure[idxClass][idxA]);
		//?TODO assertTrue(learnedStructure[idxClass][idxB]);
		//?TODO assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		//?TODO assertTrue(learnedStructure[idxA][idxC]);
		assertFalse(learnedStructure[idxA][idxClass]);
		assertFalse(learnedStructure[idxA][idxA]);
		assertFalse(learnedStructure[idxA][idxB]);
		assertFalse(learnedStructure[idxB][idxClass]);
		assertFalse(learnedStructure[idxB][idxA]);
		assertFalse(learnedStructure[idxB][idxB]);
		assertFalse(learnedStructure[idxB][idxC]);
		assertFalse(learnedStructure[idxC][idxClass]);
		assertFalse(learnedStructure[idxC][idxA]);
		assertFalse(learnedStructure[idxC][idxB]);
		assertFalse(learnedStructure[idxC][idxC]);
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.optimization.CLLHillClimbingFactory#evaluate(CTBNCToolkit.IModel, java.lang.String)}.
	 */
	@Test
	public void testEvaluate2() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		int idxClass = nodeIndexing.getIndex("Class");
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		CLLHillClimbingFactory elemFactory = new CLLHillClimbingFactory(paramsAlg, 4, false, true);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = (i != j);
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		printAdjMatrix(model);
		//?TODO assertTrue(learnedStructure[idxClass][idxA]);
		//?TODO assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
		assertFalse(learnedStructure[idxA][idxClass]);
		assertFalse(learnedStructure[idxA][idxA]);
		//?TODO assertFalse(learnedStructure[idxA][idxB]);
		assertFalse(learnedStructure[idxB][idxClass]);
		//?TODO assertFalse(learnedStructure[idxB][idxA]);
		assertFalse(learnedStructure[idxB][idxB]);
		assertFalse(learnedStructure[idxB][idxC]);
		assertFalse(learnedStructure[idxC][idxClass]);
		//?TODO assertFalse(learnedStructure[idxC][idxA]);
		//?TODO assertFalse(learnedStructure[idxC][idxB]);
		assertFalse(learnedStructure[idxC][idxC]);
		
	}
	
	/**
	 * Test method for setDataset function.
	 */ 
	@Test(expected = IllegalArgumentException.class)
	public void testSetDataset() {
		
		CLLHillClimbingFactory factory = new CLLHillClimbingFactory(new CTBNCParameterLLAlgorithm(), 3, true, false);
		factory.setDataset( null);
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
	
	//@SuppressWarnings("unused")
	private static void printAdjMatrix(IModel<Double, CTDiscreteNode> model) { 
		
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		for(int i = 0; i < nodeIndexing.getNodesNumber(); ++i)
			System.out.print("[" + model.getNode(i).getName() + "]");
		System.out.println();
		
		printAdjMatrix(model.getAdjMatrix());
	}
}

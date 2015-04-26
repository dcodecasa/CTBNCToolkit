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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

import CTBNCToolkit.optimization.CTBNCHillClimbingIndividual;
import CTBNCToolkit.optimization.LLHillClimbingFactory;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTCTBNCLocalAlgorithm {

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#CTBNCLocalLLAlgorithm()}.
	 */
	@Test
	public void testCTBNCLocalLLAlgorithm() {
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 2.0);
		params.put("Px_prior", 3.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, true, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		assertTrue(alg.getStructure() == null);
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#setParameters(java.util.Map)}
	 * and for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#setDefaultParameters()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersException() {
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 2.0);
		params.put("Px_prior", 3.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, true, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		alg.getParameter("nome");
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#setStructure(boolean[][], java.util.Map)}.
	 */
	@Test
	public void testSetStructure() {
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, true, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[2][1] = true;adjMatrix[2][0] = true;adjMatrix[2][3] = true;
		alg.setStructure(adjMatrix);
		
		compareAdjMatrix(adjMatrix, alg.getStructure());
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#helpParameters()}.
	 */
	@Test
	public void testHelpParameters() {
		System.out.println("TEST ZJTESTCTBNCLocalLLAlgorithm.testHelpParameters");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, true, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		System.out.println(alg.helpParameters());	
	}

	/**
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn1() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 3, false, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[idxClass][idxA] = true;adjMatrix[idxClass][idxB] = true;adjMatrix[idxClass][idxC] = true;
		adjMatrix[idxB][idxC] = true;adjMatrix[idxC][idxB] = true;adjMatrix[idxC][idxA] = true;
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		assertTrue(learnedStructure[idxClass][idxA]);
		assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
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
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn2() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, false, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = (i != j);
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		assertTrue(learnedStructure[idxClass][idxA]);
		assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
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
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn3() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, false, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);

		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[idxClass][idxA] = true;adjMatrix[idxClass][idxB] = true;adjMatrix[idxClass][idxC] = true;
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		assertTrue(learnedStructure[idxClass][idxA]);
		assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
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
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn4() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, false, false);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		adjMatrix[idxClass][idxA] = true;adjMatrix[idxClass][idxB] = true;adjMatrix[idxClass][idxC] = true;
		adjMatrix[idxB][idxA] = true;adjMatrix[idxC][idxA] = true;
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		assertTrue(learnedStructure[idxClass][idxA]);
		assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
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
	 * Test method for {@link CTBNCToolkit.CTBNCLocalStructuralLearning#learn(CTBNCToolkit.IModel, java.util.Collection)}.
	 */
	@Test
	public void testLearn5() {
		
		Collection<ITrajectory<Double>> trainingSet = generateDataset( 30);
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testDataset");
		
		CTBNCParameterLLAlgorithm  paramsAlg = new CTBNCParameterLLAlgorithm();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", 1.0);
		params.put("Tx_prior", 0.01);
		params.put("Px_prior", 1.0);
		paramsAlg.setParameters(params);
		LLHillClimbingFactory elemFactory = new LLHillClimbingFactory(paramsAlg, 4, true, true);
		CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> alg = new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(elemFactory);
		
		int idxClass = nodeIndexing.getClassIndex();
		int idxA = nodeIndexing.getIndex("A");
		int idxB = nodeIndexing.getIndex("B");
		int idxC = nodeIndexing.getIndex("C");
		boolean[][] adjMatrix = new boolean[4][4];
		for(int i = 0; i < adjMatrix.length; ++i)
			for(int j = 0; j < adjMatrix[i].length; ++j)
				adjMatrix[i][j] = false;
		alg.setStructure(adjMatrix);
		
		ICTClassifier<Double, CTDiscreteNode> model = generateClassifierModel();
		
		alg.learn(model, trainingSet);
		boolean[][] learnedStructure = model.getAdjMatrix();
		assertTrue(learnedStructure[idxClass][idxA]);
		assertTrue(learnedStructure[idxClass][idxB]);
		assertTrue(learnedStructure[idxClass][idxC]);
		assertFalse(learnedStructure[idxClass][idxClass]);
		assertTrue(learnedStructure[idxA][idxC]);
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
	
	private static void compareAdjMatrix(boolean[][] adjM1, boolean[][] adjM2) {
		
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

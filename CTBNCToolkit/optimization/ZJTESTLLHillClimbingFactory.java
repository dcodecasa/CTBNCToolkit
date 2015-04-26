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
public class ZJTESTLLHillClimbingFactory {

	/**
	 * Test method for {@link CTBNCToolkit.optimization.LLHillClimbingFactory#HillClimbingElementFactory(CTBNCToolkit.ILearningAlgorithm, int, boolean, boolean)}
	 * and for {@link CTBNCToolkit.optimization.LLHillClimbingFactory#newInstance(CTBNCToolkit.IModel, java.lang.String, java.util.Collection)}
	 * and for {@link CTBNCToolkit.optimization.LLHillClimbingFactory#newInstance(CTBNCToolkit.IModel, java.lang.String, java.util.Collection, double)}.
	 */
	@Test
	public void testHillClimbingElementFactory() {
		
		String[] names = new String[4]; names[0] = "Class"; names[1] = "A"; names[2] = "B"; names[3] = "C";
		NodeIndexing nodeIndexing = NodeIndexing.getNodeIndexing("testHillClimbingElementFactory", names, names[0], null);
		
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
		
		LLHillClimbingFactory factory = new LLHillClimbingFactory(paramsLAlg, 3, true, false);
		
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
	 * Test method for setDataset function.
	 */ 
	@Test(expected = IllegalArgumentException.class)
	public void testSetDataset() {
		
		LLHillClimbingFactory factory = new LLHillClimbingFactory(new CTBNCParameterLLAlgorithm(), 3, true, false);
		factory.setDataset( null);
	}

}

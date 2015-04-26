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
package CTBNCToolkit.clustering;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZZZJTESTStandardStopCriterion {

	/**
	 * Test method for {@link CTBNCToolkit.clustering.StandardStopCriterion#StandardStopCriterion(int, double)},
	 * for {@link CTBNCToolkit.clustering.StandardStopCriterion#getMaxIteration()},
	 * for {@link CTBNCToolkit.clustering.StandardStopCriterion#getChangedBound()},
	 * and for {@link CTBNCToolkit.clustering.StandardStopCriterion#paramsStop(int, double)}.
	 */
	@Test
	public void testParamsStop() {
		
		StandardStopCriterion stopC = new StandardStopCriterion(2131, 0.324);
		assertTrue( stopC.getMaxIteration() == 2131);
		assertTrue( stopC.getChangedBound() == 0.324);
		assertFalse( stopC.paramsStop(0, 0.325));
		assertFalse( stopC.paramsStop(2131, 0.635));
		assertTrue( stopC.paramsStop(2132, 1.0));
		assertTrue( stopC.paramsStop(0, 0.324));
		assertTrue( stopC.paramsStop(2132, 0.0));
		
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.clustering.StandardStopCriterion#StandardStopCriterion(int, double)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testParamsStopException1() {
		
		new StandardStopCriterion(2131, -0.21);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.clustering.StandardStopCriterion#StandardStopCriterion(int, double)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testParamsStopException2() {
		
		new StandardStopCriterion(743, 1.4);
	}

	/**
	 * Test method for {@link CTBNCToolkit.clustering.StandardStopCriterion#StandardStopCriterion(int, double)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testParamsStopException3() {
		
		new StandardStopCriterion(0, 1.0);
	}
	
}

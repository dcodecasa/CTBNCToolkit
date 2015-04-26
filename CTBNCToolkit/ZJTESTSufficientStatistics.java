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

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 */
public class ZJTESTSufficientStatistics {

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#SufficientStatistics()}.
	 */
	@Test
	public void testSufficientStatistics() {
		
		SufficientStatistics sStats = new SufficientStatistics();
		assertTrue( sStats.counts == null);
		assertTrue( sStats.Px == null);
		assertTrue( sStats.Mx == null);
		assertTrue( sStats.Mxx == null);
		assertTrue( sStats.Tx == null);
		assertFalse( sStats.isSet());
		
	}

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#SufficientStatistics(int, int, boolean, double, double, double)}.
	 */
	@Test
	public void testSufficientStatisticsIntIntBooleanDoubleDoubleDouble1() {
		
		int nPE = 2; int nS = 3;
		boolean isStatic = true;
		double pPrior = 1.0; double mPrior = 2.0; double tPrior = 0.1;
		
		SufficientStatistics sStats = new SufficientStatistics(nPE, nS, isStatic, pPrior, mPrior, tPrior);
		assertTrue( sStats.isSet());
		assertTrue( sStats.isStatic() == isStatic);
		assertTrue( sStats.parentEntriesNumber() == nPE);
		assertTrue( sStats.statesNumber() == nS);
		assertTrue( sStats.Px.length == nPE);
		assertTrue( sStats.Px[0].length == 1);
		assertTrue( sStats.Px[0][0].length == nS);
		assertTrue( sStats.counts.length == nPE);
		assertTrue( sStats.Mxx == null);
		assertTrue( sStats.Mx == null);
		assertTrue( sStats.Tx == null);
		
		for(int pE = 0; pE < sStats.parentEntriesNumber(); ++pE) {
			assertTrue( sStats.counts[pE] == pPrior*nS);
			for(int sE = 0; sE < sStats.statesNumber(); ++sE) {
				assertTrue( sStats.Px[pE][0][sE] == pPrior);
			}
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#SufficientStatistics(int, int, boolean, double, double, double)}.
	 */
	@Test
	public void testSufficientStatisticsIntIntBooleanDoubleDoubleDouble2() {
		
		int nPE = 2; int nS = 3;
		boolean isStatic = false;
		double pPrior = 2.0; double mPrior = 1.0; double tPrior = 0.1;
		
		SufficientStatistics sStats = new SufficientStatistics(nPE, nS, isStatic, pPrior, mPrior, tPrior);
		assertTrue( sStats.isSet());
		assertTrue( sStats.isStatic() == isStatic);
		assertTrue( sStats.parentEntriesNumber() == nPE);
		assertTrue( sStats.statesNumber() == nS);
		assertTrue( sStats.Px == null);
		assertTrue( sStats.counts == null);
		assertTrue( sStats.Mxx.length == nPE);
		assertTrue( sStats.Mxx[0].length == nS);
		assertTrue( sStats.Mxx[0][0].length == nS);
		assertTrue( sStats.Mx.length == nPE);
		assertTrue( sStats.Mx[0].length == nS);
		assertTrue( sStats.Tx.length == nPE);
		assertTrue( sStats.Tx[0].length == nS);
		
		for(int pE = 0; pE < sStats.parentEntriesNumber(); ++pE) {
			for(int fSE = 0; fSE < sStats.statesNumber(); ++fSE) {
				assertTrue( sStats.Tx[pE][fSE] == tPrior);
				assertTrue( sStats.Mx[pE][fSE] == mPrior*(nS-1));
				for(int sSE = 0; sSE < sStats.statesNumber(); ++sSE) {
					if( fSE == sSE)
						continue;
					assertTrue( sStats.Mxx[pE][fSE][sSE] == mPrior);
				}
			}
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#clone()}.
	 */
	@Test
	public void testClone1() {
		
		int nPE = 2; int nS = 3;
		boolean isStatic = true;
		double pPrior = 1.0; double mPrior = 2.0; double tPrior = 0.1;
		
		SufficientStatistics initStats = new SufficientStatistics(nPE, nS, isStatic, pPrior, mPrior, tPrior);
		SufficientStatistics sStats = initStats.clone();
		assertFalse( initStats == sStats);
		assertTrue( sStats.isSet());
		assertTrue( sStats.isStatic() == isStatic);
		assertTrue( sStats.parentEntriesNumber() == nPE);
		assertTrue( sStats.statesNumber() == nS);
		assertTrue( sStats.Px.length == nPE);
		assertTrue( sStats.Px[0].length == 1);
		assertTrue( sStats.Px[0][0].length == nS);
		assertTrue( sStats.counts.length == nPE);
		assertTrue( sStats.Mxx == null);
		assertTrue( sStats.Mx == null);
		assertTrue( sStats.Tx == null);
		
		for(int pE = 0; pE < sStats.parentEntriesNumber(); ++pE) {
			assertTrue( sStats.counts[pE] == pPrior*nS);
			for(int sE = 0; sE < sStats.statesNumber(); ++sE) {
				assertTrue( sStats.Px[pE][0][sE] == pPrior);
			}
		}
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#clone()}.
	 */
	@Test
	public void testClone2() {
		
		int nPE = 2; int nS = 3;
		boolean isStatic = false;
		double pPrior = 2.0; double mPrior = 1.0; double tPrior = 0.1;
		
		SufficientStatistics initStats = new SufficientStatistics(nPE, nS, isStatic, pPrior, mPrior, tPrior);
		SufficientStatistics sStats = initStats.clone();
		assertFalse( initStats == sStats);
		assertTrue( sStats.isSet());
		assertTrue( sStats.isStatic() == isStatic);
		assertTrue( sStats.parentEntriesNumber() == nPE);
		assertTrue( sStats.statesNumber() == nS);
		assertTrue( sStats.Px == null);
		assertTrue( sStats.counts == null);
		assertTrue( sStats.Mxx.length == nPE);
		assertTrue( sStats.Mxx[0].length == nS);
		assertTrue( sStats.Mxx[0][0].length == nS);
		assertTrue( sStats.Mx.length == nPE);
		assertTrue( sStats.Mx[0].length == nS);
		assertTrue( sStats.Tx.length == nPE);
		assertTrue( sStats.Tx[0].length == nS);
		
		for(int pE = 0; pE < sStats.parentEntriesNumber(); ++pE) {
			for(int fSE = 0; fSE < sStats.statesNumber(); ++fSE) {
				assertTrue( sStats.Tx[pE][fSE] == tPrior);
				assertTrue( sStats.Mx[pE][fSE] == mPrior*(nS-1));
				for(int sSE = 0; sSE < sStats.statesNumber(); ++sSE) {
					if( fSE == sSE)
						continue;
					assertTrue( sStats.Mxx[pE][fSE][sSE] == mPrior);
				}
			}
		}
	}

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#parentEntriesNumber()}.
	 */
	@Test(expected = RuntimeException.class)
	public void testParentEntriesNumberException() {
		
		SufficientStatistics sStats = new SufficientStatistics();
		sStats.parentEntriesNumber();
	}

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#statesNumber()}.
	 */
	@Test(expected = RuntimeException.class)
	public void testStatesNumberException() {
		
		SufficientStatistics sStats = new SufficientStatistics();
		sStats.statesNumber();
	}

	/**
	 * Test method for {@link CTBNCToolkit.SufficientStatistics#isStatic()}.
	 */
	@Test(expected = RuntimeException.class)
	public void testIsStaticException() {
		
		SufficientStatistics sStats = new SufficientStatistics();
		sStats.isStatic();
	}

}

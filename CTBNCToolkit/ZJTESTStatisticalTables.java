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
public class ZJTESTStatisticalTables {

	/**
	 * Test method for {@link CTBNCToolkit.StatisticalTables#tStudentTable(int, java.lang.String)}.
	 */
	@Test
	public void testTStudentTable() {
		
		assertTrue( StatisticalTables.tStudentTable(9, "99.8%") == 4.297);
		assertTrue( StatisticalTables.tStudentTable(9, "99%") == 3.25);
		assertTrue( StatisticalTables.tStudentTable(9, "98%") == 2.821);
		assertTrue( StatisticalTables.tStudentTable(9, "90%") == 1.833);
		assertTrue( StatisticalTables.tStudentTable(9, "80%") == 1.383);
		assertTrue( StatisticalTables.tStudentTable(9, "60%") == 0.883);
		assertTrue( StatisticalTables.tStudentTable(121, "80%") == 1.282);
		assertTrue( StatisticalTables.tStudentTable(120, "70%") == 1.041);
		assertTrue( StatisticalTables.tStudentTable(119, "99.8%") == 3.174);
		assertTrue( StatisticalTables.tStudentTable(15, "50%") == 0.691);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.StatisticalTables#tStudentTable(int, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTStudentTableException1() {
		
		StatisticalTables.tStudentTable(55, null);
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.StatisticalTables#tStudentTable(int, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTStudentTableException2() {
		
		StatisticalTables.tStudentTable(0, "90%");
	}
	
	/**
	 * Test method for {@link CTBNCToolkit.StatisticalTables#tStudentTable(int, java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTStudentTableException3() {
		
		StatisticalTables.tStudentTable(3, "30%");
	}
	
	

}

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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Static class for statistical tables.
 */
public class StatisticalTables {


	private static List<Map<String,Double>> tTable = null;

	
	/**
	 * Set the t-student table.
	 */
	private static void setTStudentTable() {
		
		if( tTable != null)
			return;
		
		tTable = new Vector<Map<String,Double>>(40);
		Map<String,Double> row;
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 1.0);row.put("50%", 1.000);row.put("60%", 1.376);row.put("70%", 1.963);row.put("80%", 3.078);row.put("90%", 6.314);row.put("95%", 12.71);row.put("98%", 31.82);row.put("99%", 63.66);row.put("99.5%", 127.3);row.put("99.8%", 318.3);row.put("99.9%", 636.6);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 2.0);row.put("50%", 0.816);row.put("60%", 1.061);row.put("70%", 1.386);row.put("80%", 1.886);row.put("90%", 2.920);row.put("95%", 4.303);row.put("98%", 6.965);row.put("99%", 9.925);row.put("99.5%", 14.09);row.put("99.8%", 22.33);row.put("99.9%", 31.60);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 3.0);row.put("50%", 0.765);row.put("60%", 0.978);row.put("70%", 1.250);row.put("80%", 1.638);row.put("90%", 2.353);row.put("95%", 3.182);row.put("98%", 4.541);row.put("99%", 5.841);row.put("99.5%", 7.453);row.put("99.8%", 10.21);row.put("99.9%", 12.92);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 4.0);row.put("50%", 0.741);row.put("60%", 0.941);row.put("70%", 1.190);row.put("80%", 1.533);row.put("90%", 2.132);row.put("95%", 2.776);row.put("98%", 3.747);row.put("99%", 4.604);row.put("99.5%", 5.598);row.put("99.8%", 7.173);row.put("99.9%", 8.610);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 5.0);row.put("50%", 0.727);row.put("60%", 0.920);row.put("70%", 1.156);row.put("80%", 1.476);row.put("90%", 2.015);row.put("95%", 2.571);row.put("98%", 3.365);row.put("99%", 4.032);row.put("99.5%", 4.773);row.put("99.8%", 5.893);row.put("99.9%", 6.869);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 6.0);row.put("50%", 0.718);row.put("60%", 0.906);row.put("70%", 1.134);row.put("80%", 1.440);row.put("90%", 1.943);row.put("95%", 2.447);row.put("98%", 3.143);row.put("99%", 3.707);row.put("99.5%", 4.317);row.put("99.8%", 5.208);row.put("99.9%", 5.959);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 7.0);row.put("50%", 0.711);row.put("60%", 0.896);row.put("70%", 1.119);row.put("80%", 1.415);row.put("90%", 1.895);row.put("95%", 2.365);row.put("98%", 2.998);row.put("99%", 3.499);row.put("99.5%", 4.029);row.put("99.8%", 4.785);row.put("99.9%", 5.408);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 8.0);row.put("50%", 0.706);row.put("60%", 0.889);row.put("70%", 1.108);row.put("80%", 1.397);row.put("90%", 1.860);row.put("95%", 2.306);row.put("98%", 2.896);row.put("99%", 3.355);row.put("99.5%", 3.833);row.put("99.8%", 4.501);row.put("99.9%", 5.041);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 9.0);row.put("50%", 0.703);row.put("60%", 0.883);row.put("70%", 1.100);row.put("80%", 1.383);row.put("90%", 1.833);row.put("95%", 2.262);row.put("98%", 2.821);row.put("99%", 3.250);row.put("99.5%", 3.690);row.put("99.8%", 4.297);row.put("99.9%", 4.781);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 10.0);row.put("50%", 0.700);row.put("60%", 0.879);row.put("70%", 1.093);row.put("80%", 1.372);row.put("90%", 1.812);row.put("95%", 2.228);row.put("98%", 2.764);row.put("99%", 3.169);row.put("99.5%", 3.581);row.put("99.8%", 4.144);row.put("99.9%", 4.587);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 11.0);row.put("50%", 0.697);row.put("60%", 0.876);row.put("70%", 1.088);row.put("80%", 1.363);row.put("90%", 1.796);row.put("95%", 2.201);row.put("98%", 2.718);row.put("99%", 3.106);row.put("99.5%", 3.497);row.put("99.8%", 4.025);row.put("99.9%", 4.437);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 12.0);row.put("50%", 0.695);row.put("60%", 0.873);row.put("70%", 1.083);row.put("80%", 1.356);row.put("90%", 1.782);row.put("95%", 2.179);row.put("98%", 2.681);row.put("99%", 3.055);row.put("99.5%", 3.428);row.put("99.8%", 3.930);row.put("99.9%", 4.318);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 13.0);row.put("50%", 0.694);row.put("60%", 0.870);row.put("70%", 1.079);row.put("80%", 1.350);row.put("90%", 1.771);row.put("95%", 2.160);row.put("98%", 2.650);row.put("99%", 3.012);row.put("99.5%", 3.372);row.put("99.8%", 3.852);row.put("99.9%", 4.221);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 14.0);row.put("50%", 0.692);row.put("60%", 0.868);row.put("70%", 1.076);row.put("80%", 1.345);row.put("90%", 1.761);row.put("95%", 2.145);row.put("98%", 2.624);row.put("99%", 2.977);row.put("99.5%", 3.326);row.put("99.8%", 3.787);row.put("99.9%", 4.140);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 15.0);row.put("50%", 0.691);row.put("60%", 0.866);row.put("70%", 1.074);row.put("80%", 1.341);row.put("90%", 1.753);row.put("95%", 2.131);row.put("98%", 2.602);row.put("99%", 2.947);row.put("99.5%", 3.286);row.put("99.8%", 3.733);row.put("99.9%", 4.073);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 16.0);row.put("50%", 0.690);row.put("60%", 0.865);row.put("70%", 1.071);row.put("80%", 1.337);row.put("90%", 1.746);row.put("95%", 2.120);row.put("98%", 2.583);row.put("99%", 2.921);row.put("99.5%", 3.252);row.put("99.8%", 3.686);row.put("99.9%", 4.015);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 17.0);row.put("50%", 0.689);row.put("60%", 0.863);row.put("70%", 1.069);row.put("80%", 1.333);row.put("90%", 1.740);row.put("95%", 2.110);row.put("98%", 2.567);row.put("99%", 2.898);row.put("99.5%", 3.222);row.put("99.8%", 3.646);row.put("99.9%", 3.965);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 18.0);row.put("50%", 0.688);row.put("60%", 0.862);row.put("70%", 1.067);row.put("80%", 1.330);row.put("90%", 1.734);row.put("95%", 2.101);row.put("98%", 2.552);row.put("99%", 2.878);row.put("99.5%", 3.197);row.put("99.8%", 3.610);row.put("99.9%", 3.922);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 19.0);row.put("50%", 0.688);row.put("60%", 0.861);row.put("70%", 1.066);row.put("80%", 1.328);row.put("90%", 1.729);row.put("95%", 2.093);row.put("98%", 2.539);row.put("99%", 2.861);row.put("99.5%", 3.174);row.put("99.8%", 3.579);row.put("99.9%", 3.883);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 20.0);row.put("50%", 0.687);row.put("60%", 0.860);row.put("70%", 1.064);row.put("80%", 1.325);row.put("90%", 1.725);row.put("95%", 2.086);row.put("98%", 2.528);row.put("99%", 2.845);row.put("99.5%", 3.153);row.put("99.8%", 3.552);row.put("99.9%", 3.850);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 21.0);row.put("50%", 0.686);row.put("60%", 0.859);row.put("70%", 1.063);row.put("80%", 1.323);row.put("90%", 1.721);row.put("95%", 2.080);row.put("98%", 2.518);row.put("99%", 2.831);row.put("99.5%", 3.135);row.put("99.8%", 3.527);row.put("99.9%", 3.819);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 22.0);row.put("50%", 0.686);row.put("60%", 0.858);row.put("70%", 1.061);row.put("80%", 1.321);row.put("90%", 1.717);row.put("95%", 2.074);row.put("98%", 2.508);row.put("99%", 2.819);row.put("99.5%", 3.119);row.put("99.8%", 3.505);row.put("99.9%", 3.792);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 23.0);row.put("50%", 0.685);row.put("60%", 0.858);row.put("70%", 1.060);row.put("80%", 1.319);row.put("90%", 1.714);row.put("95%", 2.069);row.put("98%", 2.500);row.put("99%", 2.807);row.put("99.5%", 3.104);row.put("99.8%", 3.485);row.put("99.9%", 3.767);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 24.0);row.put("50%", 0.685);row.put("60%", 0.857);row.put("70%", 1.059);row.put("80%", 1.318);row.put("90%", 1.711);row.put("95%", 2.064);row.put("98%", 2.492);row.put("99%", 2.797);row.put("99.5%", 3.091);row.put("99.8%", 3.467);row.put("99.9%", 3.745);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 25.0);row.put("50%", 0.684);row.put("60%", 0.856);row.put("70%", 1.058);row.put("80%", 1.316);row.put("90%", 1.708);row.put("95%", 2.060);row.put("98%", 2.485);row.put("99%", 2.787);row.put("99.5%", 3.078);row.put("99.8%", 3.450);row.put("99.9%", 3.725);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 26.0);row.put("50%", 0.684);row.put("60%", 0.856);row.put("70%", 1.058);row.put("80%", 1.315);row.put("90%", 1.706);row.put("95%", 2.056);row.put("98%", 2.479);row.put("99%", 2.779);row.put("99.5%", 3.067);row.put("99.8%", 3.435);row.put("99.9%", 3.707);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 27.0);row.put("50%", 0.684);row.put("60%", 0.855);row.put("70%", 1.057);row.put("80%", 1.314);row.put("90%", 1.703);row.put("95%", 2.052);row.put("98%", 2.473);row.put("99%", 2.771);row.put("99.5%", 3.057);row.put("99.8%", 3.421);row.put("99.9%", 3.690);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 28.0);row.put("50%", 0.683);row.put("60%", 0.855);row.put("70%", 1.056);row.put("80%", 1.313);row.put("90%", 1.701);row.put("95%", 2.048);row.put("98%", 2.467);row.put("99%", 2.763);row.put("99.5%", 3.047);row.put("99.8%", 3.408);row.put("99.9%", 3.674);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 29.0);row.put("50%", 0.683);row.put("60%", 0.854);row.put("70%", 1.055);row.put("80%", 1.311);row.put("90%", 1.699);row.put("95%", 2.045);row.put("98%", 2.462);row.put("99%", 2.756);row.put("99.5%", 3.038);row.put("99.8%", 3.396);row.put("99.9%", 3.659);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 30.0);row.put("50%", 0.683);row.put("60%", 0.854);row.put("70%", 1.055);row.put("80%", 1.310);row.put("90%", 1.697);row.put("95%", 2.042);row.put("98%", 2.457);row.put("99%", 2.750);row.put("99.5%", 3.030);row.put("99.8%", 3.385);row.put("99.9%", 3.646);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 40.0);row.put("50%", 0.681);row.put("60%", 0.851);row.put("70%", 1.050);row.put("80%", 1.303);row.put("90%", 1.684);row.put("95%", 2.021);row.put("98%", 2.423);row.put("99%", 2.704);row.put("99.5%", 2.971);row.put("99.8%", 3.307);row.put("99.9%", 3.551);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 50.0);row.put("50%", 0.679);row.put("60%", 0.849);row.put("70%", 1.047);row.put("80%", 1.299);row.put("90%", 1.676);row.put("95%", 2.009);row.put("98%", 2.403);row.put("99%", 2.678);row.put("99.5%", 2.937);row.put("99.8%", 3.261);row.put("99.9%", 3.496);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 60.0);row.put("50%", 0.679);row.put("60%", 0.848);row.put("70%", 1.045);row.put("80%", 1.296);row.put("90%", 1.671);row.put("95%", 2.000);row.put("98%", 2.390);row.put("99%", 2.660);row.put("99.5%", 2.915);row.put("99.8%", 3.232);row.put("99.9%", 3.460);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 80.0);row.put("50%", 0.678);row.put("60%", 0.846);row.put("70%", 1.043);row.put("80%", 1.292);row.put("90%", 1.664);row.put("95%", 1.990);row.put("98%", 2.374);row.put("99%", 2.639);row.put("99.5%", 2.887);row.put("99.8%", 3.195);row.put("99.9%", 3.416);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 100.0);row.put("50%", 0.677);row.put("60%", 0.845);row.put("70%", 1.042);row.put("80%", 1.290);row.put("90%", 1.660);row.put("95%", 1.984);row.put("98%", 2.364);row.put("99%", 2.626);row.put("99.5%", 2.871);row.put("99.8%", 3.174);row.put("99.9%", 3.390);
		tTable.add( row);
		
		row = new TreeMap<String,Double>();
		row.put("degrees", 120.0);row.put("50%", 0.677);row.put("60%", 0.845);row.put("70%", 1.041);row.put("80%", 1.289);row.put("90%", 1.658);row.put("95%", 1.980);row.put("98%", 2.358);row.put("99%", 2.617);row.put("99.5%", 2.860);row.put("99.8%", 3.160);row.put("99.9%", 3.373);
		tTable.add( row);

		row = new TreeMap<String,Double>();
		row.put("degrees", Double.POSITIVE_INFINITY);row.put("50%", 0.674);row.put("60%", 0.842);row.put("70%", 1.036);row.put("80%", 1.282);row.put("90%", 1.645);row.put("95%", 1.960);row.put("98%", 2.326);row.put("99%", 2.576);row.put("99.5%", 2.807);row.put("99.8%", 3.090);row.put("99.9%", 3.291);
		tTable.add( row);
	}
	
	
	/**
	 * Find the correct row in the table
	 * given the degrees of freedom.
	 * 
	 * @param df freedom degrees
	 * @return return the index of the table
	 */
	static private int findRow(int df) {
		
		int i = 0;
		for( ; i < tTable.size() && df > tTable.get(i).get("degrees"); ++i);
		// Takes the conservative decision except
		// for the value after 120 where it takes
		// infinity row
		if( df != tTable.get(i).get("degrees") && i < tTable.size()-1)	
			--i;
		
		return i;
	}
	
	
	/**
	 * Return the value of the t-student
	 * table.
	 * (i.e. df = 9 conf = 99% => 3.250 (P(x>=3.250) = 99.5%))
	 * 
	 * 
	 * @param df degrees of freedom
	 * @param confidenceLevel two tails confidence level
	 * @return t-student value
	 * @param IllegalArgumentException in case of invalid arguments
	 */
	static public double tStudentTable(int df, String confidenceLevel) throws IllegalArgumentException{
		
		if( df < 1)
			throw new IllegalArgumentException("Error: the dregees of freedom must be in [1,+oo]");
		if( confidenceLevel == null)
			throw new IllegalArgumentException("Error: null confidence level");
				
		if( tTable == null)
			StatisticalTables.setTStudentTable();
		
		Double z = tTable.get( findRow(df)).get(confidenceLevel);
		if( z == null)
			throw new IllegalArgumentException("Error: confidence level don't recogniezed");
		
		return z;		
	}
	
}

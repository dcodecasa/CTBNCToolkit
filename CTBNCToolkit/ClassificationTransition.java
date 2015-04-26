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

import java.util.Map;


/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Implement the transitions with the opportunity
 * to get the classification results.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public class ClassificationTransition<TimeType extends Number> implements IClassificationTransition<TimeType> {

	ITransition<TimeType> transition;
	private double[] pDistr;
	private Map<String,Integer> stateToIndex;
	
	/**
	 * Base constructor.
	 * 
	 * @param transition transition to associate to this result
	 */
	public ClassificationTransition(ITransition<TimeType> transition) {
		
		this.transition = transition;
		this.pDistr = null;
		this.stateToIndex = null;
	}

	@Override
	public void setProbability(double[] p, Map<String,Integer> stateToIndex) throws IllegalArgumentException {
		
		if( p == null)
			throw new IllegalArgumentException("Error: null probability distribution");
		double sum = 0.0;
		for( int i = 0; i < p.length; ++i) {
			sum += p[i];
			if( Double.isNaN(p[i]))
				throw new IllegalArgumentException("Error: NaN probability value");
			if(p[i] < 0)
				throw new IllegalArgumentException("Error: probability value smaller then 0");
			if(p[i] > 1)
				throw new IllegalArgumentException("Error: probability value bigger then 1");
		}
		if( sum + 0.0001 < 1.0 || sum - 0.0001 > 1.0)
			throw new IllegalArgumentException("Error: probability distribution doesn't sum 1");
		
		this.pDistr = p;
		this.stateToIndex = stateToIndex;
	}
	
	@Override
	public double[] getPDistribution() {
		
		if( this.pDistr == null || this.stateToIndex == null)
			return null;
		
		return this.pDistr;
	}
	
	@Override
	public Double getProbability(String classStateName) throws RuntimeException {
		
		if( this.pDistr == null || this.stateToIndex == null)
			return null;
		
		Integer cIndex = this.stateToIndex.get(classStateName);
		if( cIndex == null)
			throw new IllegalArgumentException("Error: class state " + classStateName + " not found");
		
		return this.pDistr[cIndex];
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITransition#getTime()
	 */
	@Override
	public TimeType getTime() {

		return this.transition.getTime();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITransition#getNodeValue(int)
	 */
	@Override
	public String getNodeValue(int nodeIndex) {
		
		return this.transition.getNodeValue(nodeIndex);
	}
	
}

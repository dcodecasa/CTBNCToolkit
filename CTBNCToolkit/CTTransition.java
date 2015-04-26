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

/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that define standard transitions.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public class CTTransition<TimeType extends Number> implements ITransition<TimeType> {
	
	private NodeIndexing nodeIndexing;
	private TimeType time;
	private String[] values;
	
	/**
	 * Basic constructor for transition
	 * without any node change.
	 * 
	 * @param nodeIndexing global node indexing to use
	 * @param time time of the transition
	 */
	public CTTransition(NodeIndexing nodeIndexing, TimeType time) {
		
		if( nodeIndexing == null)
			throw new IllegalArgumentException("Error: null nodeIndexing argument");
		
		this.nodeIndexing = nodeIndexing;
		this.time = time;
		this.values = new String[this.nodeIndexing.getNodesNumber()];
		for( int i = 0; i < this.values.length; ++i)
			this.values[i] = null;
	}
	
	
	/**
	 * Basic constructor.
	 * 
	 * @param nodeIndexing global node indexing to use
	 * @param time time of the transition
	 * @param values values for each node (they must be synchronized with the global indexing system)
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public CTTransition(NodeIndexing nodeIndexing,TimeType time, String[] values) throws IllegalArgumentException {
		
		if( nodeIndexing == null)
			throw new IllegalArgumentException("Error: null nodeIndexing argument");
		if( values == null)
			throw new IllegalArgumentException("Error: null argument values");
		if( values.length != nodeIndexing.getNodesNumber())
			throw new IllegalArgumentException("Error: the array dimension doesn't corresponds with the general node indexing");
		
		this.nodeIndexing = nodeIndexing;
		this.time = time;
		this.values = values;
	}
	
	@Override
	public TimeType getTime() {
		
		return this.time;
	}

	@Override
	public String getNodeValue(int nodeIndex) throws IllegalArgumentException {
		
		if( nodeIndex < 0 || nodeIndex >= this.values.length)
			throw new IllegalArgumentException("Error: index out of bound");
		
		return this.values[nodeIndex];
	}

}

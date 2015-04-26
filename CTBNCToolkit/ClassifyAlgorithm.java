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
 * Abstract class that define e classification algorithm.
 * In particular define the parameters management.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> node type
 */
public abstract class ClassifyAlgorithm<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> implements IClassifyAlgorithm<TimeType,NodeType> {
	
	private Map<String, Object> params;
	
	@Override
	public void setParameters(Map<String, Object> params) throws IllegalArgumentException {
		
		this.params = params;
	}
	
	@Override
	public Object getParameter(String name) throws IllegalArgumentException {
		
		if( this.params == null || this.params.isEmpty())
			throw new IllegalArgumentException("Error: no parameters are defined");
		
		Object value = params.get( name);
		if( value == null)
			throw new IllegalArgumentException("Error: parameter " + name + " not found");
		
		return value;
	}
}
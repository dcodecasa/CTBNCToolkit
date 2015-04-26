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

import java.util.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that define discrete nodes.
 */
public class DiscreteNode extends Node implements IDiscreteNode{

	private Integer currentState;
	private List<String> states;
	private Map<String, Integer> nameToIndex;
	
	/**
	 * Constructor
	 * 
	 * @param nodeName node name
	 * @param states set of states of the node
	 */
	public DiscreteNode(String nodeName, Set<String> states) {
		
		super(nodeName);
		
		this.currentState = null;
		this.states = new Vector<String>();
		this.nameToIndex = new TreeMap<String, Integer>();
		
		Iterator<String> iter = states.iterator();
		Integer i = 0;
		while(iter.hasNext())
		{
			this.states.add(iter.next());
			this.nameToIndex.put(this.states.get(i), i);
			i++;
		}
	}
	
	@Override
	public boolean isInstanced() {
		
		return (this.currentState != null);
	}
	
	@Override
	public String getCurrentState() {
		
		return (this.currentState == null ? null : this.states.get(this.currentState));
	}
	
	@Override
	public Integer getCurrentStateIndex() {
		
		return this.currentState;
	}

	@Override
	public int getStatesNumber() {

		return this.states.size();
	}

	@Override
	public String getStateName(int i) throws IllegalArgumentException {

		if( i < 0 || i >= this.states.size())
			throw new IllegalArgumentException("Error: State index " + i + " out of bound");
		
		return this.states.get(i);
	}

	@Override
	public Integer getStateIndex(String stateValue) throws IllegalArgumentException {
		
		Integer index = this.nameToIndex.get(stateValue);
		if( index == null)
			throw new IllegalArgumentException("Error: State " + stateValue + " didn't find!");
		
		return index;
	}
	
	@Override
	public void retractEvidence() {
		
		this.currentState = null;
	}

	@Override
	public void setEvidence(int stateIndex) throws IllegalArgumentException {
		
		if( stateIndex < 0 || stateIndex >= this.states.size())
			throw new IllegalArgumentException("Error: State index " + stateIndex + " out of bound");
		
		this.currentState = stateIndex;
	}
	
	@Override
	public void setEvidence(String stateValue) throws IllegalArgumentException {
		
		Integer index = this.nameToIndex.get(stateValue);
		if( index == null)
			throw new IllegalArgumentException("Error: State " + stateValue + " didn't find!");
		
		this.currentState = index;
	}
	
	/**
	 * Clone the node without coping the parental
	 * relation but only the name and the states.
	 * 
	 * @return the cloned node
	 */
	@Override
	public DiscreteNode clone() {
		
		DiscreteNode clonedNode = new DiscreteNode(this.getName(), new TreeSet<String>());
		
		for( int i = 0; i < this.states.size(); ++i) {
			clonedNode.states.add(this.states.get(i));
			clonedNode.nameToIndex.put(this.states.get(i), i);
		}
		clonedNode.currentState = this.currentState;

		return clonedNode;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.INode#forceBinaryStates(java.lang.String)
	 */
	@Override
	public void forceBinaryStates(String state) throws RuntimeException {

		if( this.isInstanced())
			throw new RuntimeException("Error: forceBinaryStates method can not be called if the node is instanciated");
		
		if( this.nameToIndex.get( state) == null)
			throw new IllegalArgumentException("Error: state " + state + " not found");
		
		List<String> newStates = new Vector<String>(2);
		Map<String,Integer> newNameToIndex = new TreeMap<String,Integer>();
		newStates.add( state);
		newNameToIndex.put( state, 0);
		newStates.add( MultipleCTBNC.otherStateName);
		newNameToIndex.put( MultipleCTBNC.otherStateName, 1);
		
		this.states = newStates;
		this.nameToIndex = newNameToIndex;		
	}	
}

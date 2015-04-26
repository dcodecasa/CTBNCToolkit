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
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Interface that define discrete nodes.
 */
public interface IDiscreteNode extends INode{
	
	/**
	 * Return the number of states
	 * 
	 * @return number of states
	 */
	public int getStatesNumber();
	
	/**
	 * Return the state name given the index.
	 * 
	 * @param i state index
	 * @return state name
	 * @throws IllegalArgumentException if there is not that index
	 */
	public String getStateName(int i) throws IllegalArgumentException;
	
	/**
	 * Return the index of the state.
	 * 
	 * @param stateName state name
	 * @return index of the state
	 * @throws IllegalArgumentException if there is not that state
	 */
	public Integer getStateIndex(String stateName) throws IllegalArgumentException;
	
	/**
	 * Set the state of the node.
	 * 
	 * @param stateIndex state index
	 * @throws IllegalArgumentException if illegal evidence
	 */
	public void setEvidence(int stateIndex) throws IllegalArgumentException;
	
}

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
 * Interface that define generic nodes.
 */
public interface INode extends Comparable<INode> {
	
	/**
	 * Get the node name.
	 * 
	 * @return node name
	 */
	public String getName();
	
	/**
	 * Information about the instantiation of the node.
	 * 
	 * @return true if the node is instanced, false otherwise
	 */
	public boolean isInstanced();
	
	/**
	 * Obtain the node value.
	 * Null if the node is not instanced.
	 * 
	 * @return node value or null if the node is not instanced
	 */
	public String getCurrentState();
	
	/**
	 * Obtain the node state index.
	 * null if the node is not instanced.
	 * 
	 * @return node state or null if the node is not instanced
	 */
	public Integer getCurrentStateIndex();
	
	/**
	 * Return the number of children.
	 * 
	 * @return number of children
	 */
	public int getChildrenNumber();
	
	/**
	 * Return the number of parents.
	 * 
	 * @return number of parents
	 */
	public int getParentsNumber();
	
	/**
	 * Get a child given its index.
	 * 
	 * @param i child index
	 * @return the child node
	 * @throws IllegalArgumentException if there is not child with that index
	 */
	public INode getChild(int i) throws IllegalArgumentException;
	
	/**
	 * Get a child given its name.
	 * 
	 * @param childName child name
	 * @return the child node
	 * @throws IllegalArgumentException if there is not child with that name
	 */
	public INode getChild(String childName) throws IllegalArgumentException;
	
	/**
	 * Get the child index given its name.
	 * 
	 * @param childName child name
	 * @return the child index or null if there is not a child with that name
	 */
	public Integer getChildIndex(String childName);
	
	/**
	 * Get a parent given its index.
	 * 
	 * @param i parent index
	 * @return the parent node
	 * @throws IllegalArgumentException if there is not parent with that index
	 */
	public INode getParent(int i) throws IllegalArgumentException;
	
	/**
	 * Get a parent given its name.
	 * 
	 * @param parentName parent name
	 * @return the parent node
	 * @throws IllegalArgumentException if there is not parent with that name
	 */
	public INode getParent(String parentName) throws IllegalArgumentException;
	
	/**
	 * Get the parent index give its name.
	 * 
	 * @param parentName parent name
	 * @return the index of the parent or null if there is not a parent with that name
	 */
	public Integer getParentIndex(String parentName);
		
	/**
	 * Remove the evidence.
	 */
	public void retractEvidence();
	
	/**
	 * Set the state of the node.
	 * 
	 * @param evidence the evidence
	 * @throws IllegalArgumentException if illegal evidence
	 */
	public void setEvidence(String evidence) throws IllegalArgumentException;
	
	/**
	 * Add a child relation.
	 * 
	 * @param child new child
	 * @return false if this relation was define before
	 * @throws RuntimeException if some error occur
	 */
	public boolean addChild(INode child) throws RuntimeException;
	
	/**
	 * Remove a child.
	 * 
	 * @param child the child to remove
	 * @return false if there is not a child relation
	 * @throws RuntimeException if some error occur 
	 */
	public boolean removeChild(INode child) throws RuntimeException;
	
	/**
	 * Remove all the node children.
	 * 
	 * @return false if there were no children, true if there was at least one
	 * @throws RuntimeException if some error occur
	 */
	public boolean removeAllChildren() throws RuntimeException;
	
	/**
	 * Add a parental relation.
	 * 
	 * @param parent new parent
	 * @return false if this relation was define before
	 * @throws RuntimeException if some error occur
	 */
	public boolean addParent(INode parent) throws RuntimeException;
	
	/**
	 * Remove a parent.
	 * 
	 * @param parent the parent to remove
	 * @return false if there is not a parental relation
	 * @throws RuntimeException if some error occur
	 */
	public boolean removeParent(INode parent) throws RuntimeException;
	
	/**
	 * Remove all the node parents.
	 * 
	 * @return false if there were no parents, true if there was at least one
	 * @throws RuntimeException if some error occur
	 */
	public boolean removeAllParents() throws RuntimeException;
	
	/**
	 * Transform the node in a binary node where the
	 * state in input is kept while all the other
	 * states are joined in a new one.
	 * 
	 * @param state state to keep
	 * @throws RuntimeException if some error occur
	 */
	public void forceBinaryStates(String state) throws RuntimeException;
}

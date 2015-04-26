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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Abstract class that define nodes
 * and their base properties.
 */
public abstract class Node implements INode {

	private String name;
	
	private List<Node> children;
	private Map<String, Integer> childNameToIndex;
	private List<Node> parents;
	private Map<String, Integer> parentNameToIndex;
	
	/**
	 * Base constructor.
	 * 
	 * @param nodeName node name
	 */
	public Node(String nodeName) {
		
		this.name = nodeName;
		
		this.children = new LinkedList<Node>();
		this.childNameToIndex = new TreeMap<String, Integer>();
		
		this.parents = new LinkedList<Node>();
		this.parentNameToIndex = new TreeMap<String, Integer>();
	}
	
	@Override
	public String getName() {
		
		return this.name;
	}
	
	@Override
	public int getChildrenNumber() {
		
		return this.children.size();
	}
	
	@Override
	public int getParentsNumber() {
		
		return this.parents.size();
	}
	
	@Override
	public INode getChild(int i) throws IllegalArgumentException {
		
		if( i < 0 || i >= this.children.size())
			throw new IllegalArgumentException("Error: Children index " + i + " out of bound");
		
		return this.children.get(i);
	}
	
	@Override
	public INode getChild(String childName) throws IllegalArgumentException {
		
		Integer childIndex = this.childNameToIndex.get(childName);
		if( childIndex == null)
			throw new IllegalArgumentException("Error: Children name " + childName + " not found");
		
		return this.children.get(childIndex);
	}

	@Override
	public Integer getChildIndex(String childName) {
		
		return this.childNameToIndex.get( childName);
	}
	
	@Override
	public INode getParent(int i) throws IllegalArgumentException {
		
		if( i < 0 || i >= this.parents.size())
			throw new IllegalArgumentException("Error: Parent index " + i + " out of bound");
		
		return this.parents.get(i);
	}
	
	@Override
	public INode getParent(String parentName) throws IllegalArgumentException {
		
		Integer parentIndex = this.parentNameToIndex.get(parentName);
		if( parentIndex == null)
			throw new IllegalArgumentException("Error: Parent name " + parentName + " not found");
		
		return this.parents.get(parentIndex);
	}
	
	@Override
	public Integer getParentIndex(String parentName) {
		
		return this.parentNameToIndex.get( parentName);
	}
	
	@Override
	public boolean addChild(INode child) throws RuntimeException {
	
		return child.addParent( this);
	}
	
	@Override
	public boolean removeChild(INode child) throws RuntimeException {
		
		return child.removeParent( this);
	}
	
	@Override
	public boolean removeAllChildren() throws RuntimeException {
		
		if( this.getChildrenNumber() == 0)
			return false;
		
		while( this.getChildrenNumber() > 0)
			this.removeChild( this.getChild( 0));
		
		return true;
	}
	
	@Override
	public boolean addParent(INode parent) throws RuntimeException {
		
		if( this == parent)
			throw new IllegalArgumentException("Error: a node can not be a parent of itself");
		
		// Aligned checking
		Node dParent = (Node) parent;
		if( this.parentNameToIndex.containsKey(dParent.getName()) != dParent.childNameToIndex.containsKey(this.name))
			throw new RuntimeException("Error: not aligned parents (node = " + this.name + ") and children " + dParent.getName() + ")");
		
		if( this.parentNameToIndex.containsKey(dParent.getName()))
			return false;
		
		// Parent adding for the child
		this.parentNameToIndex.put(dParent.getName(), this.parents.size());
		this.parents.add(dParent);
		// Child adding for the parent
		dParent.childNameToIndex.put(this.name, dParent.children.size());
		dParent.children.add(this);
		
		return true;
	}
	
	@Override
	public boolean removeParent(INode parent) throws RuntimeException {
		
		// Aligned checking
		Node dParent = (Node) parent;
		if( this.parentNameToIndex.containsKey(dParent.getName()) != dParent.childNameToIndex.containsKey(this.name))
			throw new RuntimeException("Error: not aligned parents (node = " + this.name + ") and children " + dParent.getName() + ")");
		
		if( !this.parentNameToIndex.containsKey(dParent.getName()))
			return false;
		
		int indexToRemove;
		// Child removing for the parent
		indexToRemove = this.parentNameToIndex.get(dParent.getName());
		this.parents.remove(indexToRemove);
		this.parentNameToIndex.remove(dParent.getName());
		this.parentNameToIndex = Node.updateIndexes(this.parentNameToIndex, indexToRemove);
		// Parent removing for the child
		indexToRemove = dParent.childNameToIndex.get(this.name);
		dParent.children.remove(indexToRemove);
		dParent.childNameToIndex.remove(this.name);
		dParent.childNameToIndex = Node.updateIndexes(dParent.childNameToIndex, indexToRemove);
		
		return true;
	}
	
	@Override
	public boolean removeAllParents() throws RuntimeException {
		
		if( this.getParentsNumber() == 0)
			return false;
		
		while( this.getParentsNumber() > 0)
			this.removeParent( this.getParent( 0));
		
		return true;
	}
	

	@Override
	public int compareTo(INode node) {
		return  this.name.compareTo(node.getName());
	}
	
	/**
	 * Update an index map reducing by 1 the values of all the key
	 * that has a value bigger then index in input.
	 * 
	 * @param indexesMap index map to update
	 * @param index index bound
	 * @return the updated map
	 */
	static private Map<String,Integer> updateIndexes(Map<String,Integer> indexesMap, int index) {
		
		Iterator<String> keys = indexesMap.keySet().iterator();
		
		while(keys.hasNext()) {
			String k = keys.next();
			int v = indexesMap.get(k);
			
			if( v > index)
				indexesMap.put(k, v - 1);
		}
			
		return indexesMap;
	}
	
	/**
	 * Verify if two double have the same values with
	 * an error margin.
	 * 
	 * @param v1 value 1
	 * @param v2 value 2
	 * @param eps epsilon error
	 * @return true if the values are equal with eps tollerance, false otherwise
	 */
	static protected boolean equalsDoubles(double v1, double v2, double eps) {
		
		double sum = v1 - v2;
		if(sum < eps && sum > -eps)
			return true;
		return false;
	}
}

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
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Indexing singleton used to synchronize all the
 * structures that use nodes in an unique indexing
 * in order to avoid to manage strings.
 * Singleton class.
 */
public class NodeIndexing {

	private static Map<String,NodeIndexing> indexMap = new TreeMap<String,NodeIndexing>();;
	
	private int classIndex;
	private Map<String,Integer> nameToIndex;
	private Map<Integer,String> indexToName;
	
	
    /**
	 * Base constructor.
	 * It is guaranteed the same order of
     * the nodes as in the name vector.
	 * 
	 * @param names array of nodes names.
	 * @param classNodeName name of the class node
	 * @param validColumns set of the names of the column of the dataset to load (null to accept all the column)
	 * @throws IllegalArgumentException in case of illegal argument
	 */
    private NodeIndexing(String[] names, String classNodeName, Set<String> validColumns) throws IllegalArgumentException {
    	
    	this.nameToIndex = new TreeMap<String,Integer>();
    	this.indexToName = new TreeMap<Integer,String>();
    	
    	this.classIndex = -1;
    	for(int i = 0; i < names.length; ++i) {
    		if( validColumns != null && (!validColumns.contains(names[i])))	// column to ignore
    			continue;
    		
    		if( this.nameToIndex.containsKey( names[i]))
    			throw new IllegalArgumentException("Error: two nodes with the same name (i.e. " + names[i] + ")");
    		
    		int index = this.nameToIndex.size();
    		this.nameToIndex.put(names[i], index);
    		this.indexToName.put(index, names[i]);
    		
    		if( classNodeName.equals( names[i]))
    			this.classIndex = index;
    	}
    }
    

    /**
     * Return (and eventually generate) the
     * instance of the indexing.
     * It is guaranteed the same order of
     * the nodes as in the name vector.
     * 
     * @param indexingName name of the indexing to get
     * @param names array of names to use in the indexing
     * @param classNodeName name of the class node
     * @param validColumns set of the names of the column of the dataset to load (null to accept all the column)
     * @return instance of the indexing
     * @throws IllegalArgumentException in case of illegal argument
     */
    public static synchronized NodeIndexing getNodeIndexing(String indexingName, String[] names, String classNodeName, Set<String> validColumns) throws IllegalArgumentException {
        
        NodeIndexing nodeIndexing = indexMap.get(indexingName);
        if( nodeIndexing == null) {
        	nodeIndexing = new NodeIndexing(names, classNodeName, validColumns);
        	indexMap.put(indexingName, nodeIndexing);
        }
        
        return nodeIndexing;
    }
    
    
    /**
     * Return the created indexing.
     * 
     * @param modelName name of the model who's the index is associate
     * @return instance of the indexing
     * @throws RuntimeException if the indexing wasn't generated before
     */
    public static NodeIndexing getNodeIndexing(String modelName) throws RuntimeException {
    	
    	NodeIndexing nodeIndexing = indexMap.get(modelName);
    	if( nodeIndexing == null)
    		throw new RuntimeException("Error: node indexing instance doesn't exist for model " + modelName);
    	
    	return nodeIndexing;
    }
	
	/**
	 * Return the index given the name.
	 * 
	 * @param name name
	 * @return index associated with the name (null if doesn't exist)
	 */
	public Integer getIndex(String name) {
		
		return this.nameToIndex.get(name);
	}
	
	/**
	 * Return the name given its index.
	 * 
	 * @param index index
	 * @return name associated with the index (null if doesn't exist)
	 */
	public String getName(int index) {
		
		return this.indexToName.get(index);
	}
	
	/**
	 * Return the index of the class node.
	 * 
	 * @return index of the class node
	 */
	public int getClassIndex() {
		
		return this.classIndex;
	}
	
	/**
	 * Return the name of the class node.
	 * 
	 * @return name of the class node
	 */
	public String getClassName() {
		
		return this.indexToName.get( this.classIndex);
	}
	
	/**
	 * Return the number of indexed elements.
	 * 
	 * @return number of indexed elements
	 */
	public int getNodesNumber() {
		
		return this.indexToName.size();
	}
}

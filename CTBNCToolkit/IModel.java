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
 * Interface that defines a generic model.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 */
public interface IModel<TimeType extends Number & Comparable<TimeType>, NodeType extends INode> {
	
	/**
	 * Get the model name.
	 * 
	 * @return model name
	 */
	public String getName();
	
	/**
	 * Get a node.
	 * 
	 * @param iNode node index
	 * @return the node
	 * @throws IllegalArgumentException if there is not node with that index
	 */
	public NodeType getNode(int iNode) throws IllegalArgumentException;
	
	/**
	 * Generate a complete evidence trajectory.
	 * 
	 * @param T trajectory length (number of time slices in discrete time, maximum time length in continuous time)
	 * @return a complete evidence trajectory
	 * @throws RuntimeException if the input is not correct
	 */
	public ITrajectory<TimeType> generateTrajectory(double T) throws RuntimeException;
	
	/**
	 * Generate the structure of graph using adjacent matrix.
	 * The matrix identify the children for each node.
	 *  
	 * @return the adjacent matrix of the graph
	 */
	public boolean[][] getAdjMatrix();
	
	/**
	 * Set the structure of the model.
	 * The matrix identify the children for each node.
	 *  
	 * @param adjMatrix adjacency matrix that represent a graph structure (NxN where N is the number of node)
	 * @throws RuntimeException if there is some error with the defined structure
	 */
	public void setStructure(boolean[][] adjMatrix) throws RuntimeException;
	
	/**
	 * Clone the model.
	 * 
	 * @return cloned model
	 */
	public IModel<TimeType, NodeType> clone();
	
	/**
	 * Return the global node indexing
	 * associate with the model.
	 *  
	 * @return global node indexing
	 */
	public NodeIndexing getNodeIndexing();
}

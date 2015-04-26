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
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 * 
 * Class that define a standard trajectory version.
 * 
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public class CTTrajectory<TimeType extends Number> implements ITrajectory<TimeType> {

	private NodeIndexing nodeIndexing;
	private String name;
	private List<CTTransition<TimeType>> transitions;
	
	/**
	 * Empty constructor. To use the trajectory
	 * after called this constructor you have to
	 * call the function setTransisions.
	 * 
	 * @param nodeIndexing global node indexing to use
	 */
	protected CTTrajectory(NodeIndexing nodeIndexing) {

		if( nodeIndexing == null)
			throw new IllegalArgumentException("Error: null nodeIndexing argument");
		
		this.nodeIndexing = nodeIndexing;
		this.transitions = new Vector<CTTransition<TimeType>>();
	}
	
	/**
	 * Base constructor.
	 * Note: please insert only the changed nodes
	 * for each time slice.
	 * 
	 * @param nodeIndexing global node indexing to use
	 * @param times list of time value
	 * @param values list of nodes values in the related time
	 * @throws IllegalArgumentException in case of wrong arguments
	 */
	public CTTrajectory(NodeIndexing nodeIndexing, List<TimeType> times, List<String[]> values) throws IllegalArgumentException {
		
		if( nodeIndexing == null)
			throw new IllegalArgumentException("Error: null nodeIndexing argument");
		if( times == null)
			throw new IllegalArgumentException("Error: null value for the time list");
		if( values == null)
			throw new IllegalArgumentException("Error: null value for the values list");
		if( times.size() != values.size())
			throw new IllegalArgumentException("Error: lists of the constructor argument with different dimension");
		
		this.nodeIndexing = nodeIndexing;
		this.transitions = new Vector<CTTransition<TimeType>>(times.size());
		for(int i = 0; i < times.size(); ++i) {
			this.transitions.add(new CTTransition<TimeType>(this.nodeIndexing, times.get(i), values.get(i)));
		}
	}
	
	/**
	 * Set manually the transitions
	 * 
	 * @param transitions the transitions to set.
	 */
	protected void setTransisions(List<CTTransition<TimeType>> transitions) {

		this.transitions = transitions;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getTransitionsNumber()
	 */
	@Override
	public int getTransitionsNumber() {

		return this.transitions.size();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getNodeValue(int, int)
	 */
	@Override
	public String getNodeValue(int iTransition, int nodeIndex) throws IllegalArgumentException {

		if( iTransition < 0 || iTransition >=  this.transitions.size())
			throw new IllegalArgumentException("Error: transition index out of bound");
		
		String result = null;
		for(;iTransition >= 0 && result == null; --iTransition)
			result = this.transitions.get(iTransition).getNodeValue( nodeIndex);
		
		if( result == null)
			throw new IllegalArgumentException("Error: node " + this.nodeIndexing.getName( nodeIndex) + " didn't find");
		
		return result;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getTransitionTime(int)
	 */
	@Override
	public TimeType getTransitionTime(int iTransition) throws IllegalArgumentException {
		
		if( iTransition < 0 || iTransition >=  this.transitions.size())
			throw new IllegalArgumentException("Error: transition index out of bound");
		
		return this.transitions.get( iTransition).getTime();
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getTransition(int)
	 */
	@Override
	public CTTransition<TimeType> getTransition(int iTransition) {

		if( iTransition < 0 || iTransition >=  this.transitions.size())
			throw new IllegalArgumentException("Error: transition index out of bound");
		
		return this.transitions.get(iTransition);
	}
	
	/**
	 * Print the trajectory.
	 */
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		// Handing
		strBuilder.append( "t");
		for(int iNode = 0; iNode < this.nodeIndexing.getNodesNumber(); ++iNode)
			strBuilder.append( "," + this.nodeIndexing.getName(iNode));
		strBuilder.append( "\n");
		
		// Transitions
		for( int iJmp = 0; iJmp < this.getTransitionsNumber(); ++iJmp) {
			strBuilder.append( this.getTransition(iJmp).getTime());
			for(int iNode = 0; iNode < this.nodeIndexing.getNodesNumber(); ++iNode)
				strBuilder.append( "," + this.getNodeValue(iJmp, iNode));
			strBuilder.append( "\n");
		}
		
		return strBuilder.toString();
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getName()
	 */
	@Override
	public String getName() {
		
		return this.name;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getNodeIndexing()
	 */
	@Override
	public NodeIndexing getNodeIndexing() {
		
		return this.nodeIndexing;
	}

	
	/**
	 * Cut the trajectory length to a 
	 * percentage of the original length.
	 * 
	 * @param trj trajectory to cut
	 * @param cutPercentage percentage of cutting in (0,1]
	 * @return new shorter trajectory (with a subset of the same instance of transitions)
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	static public CTTrajectory<Double> cutTrajectory(CTTrajectory<Double> trj, double cutPercentage) //? TODO test
			throws IllegalArgumentException {

		if( trj == null)
			throw new IllegalArgumentException("Error: null trajectory");
		if( trj.getTransitionsNumber() == 0)
			throw new IllegalArgumentException("Error: empty trajectory (name = " + trj.getName() + ")");
		if( cutPercentage <= 0 || cutPercentage > 1)
			throw new IllegalArgumentException("Error: the cutting percentage must be in (0,1]");
		
		// Searching of the last transition to keep
		double tMax = trj.getTransitionTime( trj.getTransitionsNumber() - 1) * cutPercentage;
		int iMin = 0; 
		int iMax = trj.getTransitionsNumber() - 1;
		while( iMin <= iMax) {
			int i = iMin + (iMax - iMin)/2;
			CTTransition<Double> trans = trj.getTransition(i);
			
			if( trans.getTime() < tMax)
				iMin = i + 1;
			else if( trans.getTime() > tMax)			
				iMax = i - 1;
			else {
				iMax = i;
				iMin = i+1;
			}
		}
		CTTransition<Double> lastTrans = trj.getTransition(iMax);
		
		// Transition of the new trajectory
		List<CTTransition<Double>> trjList = trj.transitions.subList(0, iMax + 1);
		if( lastTrans.getTime() != tMax) {		// define the termination point
			String[] values = new String[trj.nodeIndexing.getNodesNumber()];
			for(int i = 0; i < trj.nodeIndexing.getNodesNumber(); ++i)
				values[i] =  lastTrans.getNodeValue(i);
			
			trjList.add(new CTTransition<Double>(trj.nodeIndexing, tMax, values));
		}
		
		// New trajectory generation
		CTTrajectory<Double> newTrj = new CTTrajectory<Double>(trj.nodeIndexing);
		newTrj.setTransisions( trjList);
		newTrj.setName( trj.getName());
		
		return newTrj;
	}

}

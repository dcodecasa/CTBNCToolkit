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
 * Implement the trajectory with the addition
 * capability to get the class information in
 * order to implement the results of a classification
 * process.
 *
 * @param <TimeType> type of the time interval (Integer = discrete time, Double = continuous time)
 */
public class ClassificationResults<TimeType extends Number & Comparable<TimeType>> implements IClassificationResult<TimeType> {

	private NodeIndexing nodeIndexing;
	private String name;
	private String className;	
	private List<ClassificationTransition<TimeType>> transitions;
	
	/**
	 * Base constructor.
	 * 
	 * @param trajectory the trajectory over which generate the results
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public ClassificationResults(ITrajectory<TimeType> trajectory) throws IllegalArgumentException {
		this(trajectory, new Vector<TimeType>());
	}
	
	/**
	 * Constructor to generate the results with a finer
	 * granularity.
	 * 
	 * @param trajectory the trajectory over which generate the results
	 * @param timeStream a time stream to add the results at particular times
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public ClassificationResults(ITrajectory<TimeType> trajectory, List<TimeType> timeStream) throws IllegalArgumentException {

		this.nodeIndexing = trajectory.getNodeIndexing();
		this.transitions = new Vector<ClassificationTransition<TimeType>>(trajectory.getTransitionsNumber() + timeStream.size());
		
		this.name = trajectory.getName();
		int iTrj = 0;									// index over trajectory
		int iTS = 0;									// index over time stream
		while(iTrj < trajectory.getTransitionsNumber() || iTS < timeStream.size()) {
			if( iTrj < trajectory.getTransitionsNumber() && (iTS == timeStream.size() || trajectory.getTransitionTime(iTrj).compareTo( timeStream.get(iTS)) <= 0)) {
				
				this.transitions.add( new ClassificationTransition<TimeType>( trajectory.getTransition( iTrj)));
				
				// Update the indexes
				if( iTS != timeStream.size() && trajectory.getTransitionTime(iTrj).compareTo( timeStream.get(iTS)) == 0)
					++iTS;
				++iTrj;
			} else {
				
				this.transitions.add( new ClassificationTransition<TimeType>( new CTTransition<TimeType>( this.nodeIndexing, timeStream.get(iTS))));
				++iTS;
			}
		}
	}

	@Override
	public void setClassification(String className) {

		this.className = className;
	}

	@Override
	public String getClassification() {

		return this.className;
	}

	@Override
	public void setProbability(int iTransition, double[] p, Map<String, Integer> stateToIndex) throws IllegalArgumentException {
		
		((ClassificationTransition<TimeType>) this.getTransition(iTransition)).setProbability(p, stateToIndex);
	}

	@Override
	public Double getProbability(int iTransition, String classStateName) throws IllegalArgumentException {
	
		return ((ClassificationTransition<TimeType>) this.getTransition(iTransition)).getProbability(classStateName);
	}
	
	@Override
	public double[] getPDistribution(int iTransition) throws IllegalArgumentException {
		
		return ((ClassificationTransition<TimeType>) this.getTransition(iTransition)).getPDistribution();
	}
	
	@Override
	public double[] getPDistribution() {
		
		return this.getPDistribution(this.getTransitionsNumber() - 1);
	}
	
	@Override
	public Double getProbability(String classStateName) throws IllegalArgumentException {
		
		return this.getProbability(this.getTransitionsNumber() - 1, classStateName);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.IClassificationResult#resultToString()
	 */
	@Override
	public String resultToString() {

		Double p = this.getProbability(this.className);
		if( p != null)
			return this.getName() + ": True Class: " + this.getNodeValue(0, this.nodeIndexing.getClassIndex()) + ", Predicted: " + this.getClassification() + ", Probability: " + p;
		else
			return this.getName() + ": True Class: " + this.getNodeValue(0, this.nodeIndexing.getClassIndex()) + ", Predicted: " + this.getClassification();
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
	 * @see CTBNToolkit.ITrajectory#getTransitionsNumber()
	 */
	@Override
	public int getTransitionsNumber() {

		return this.transitions.size();
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getTransitionTime(int)
	 */
	@Override
	public TimeType getTransitionTime(int iTransition)
			throws IllegalArgumentException {
		
		if( iTransition < 0 || iTransition >=  this.transitions.size())
			throw new IllegalArgumentException("Error: transition index out of bound");
		
		return this.transitions.get( iTransition).getTime();
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
	 * @see CTBNToolkit.ITrajectory#getTransition(int)
	 */
	@Override
	public ITransition<TimeType> getTransition(int iTransition)
			throws IllegalArgumentException {
		
		if( iTransition < 0 || iTransition >=  this.transitions.size())
			throw new IllegalArgumentException("Error: transition index out of bound");
		
		return this.transitions.get(iTransition);
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.ITrajectory#getNodeIndexing()
	 */
	@Override
	public NodeIndexing getNodeIndexing() {

		return this.nodeIndexing;
	}

}

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
 * This class extend the discrete node adding the CIM
 * table.
 * This class doesn't allow states names that contain
 * character @ (it is used as special character).
 */
public class CTDiscreteNode extends DiscreteNode {

	private boolean staticNode;
	private int nParentsEntries = 1;
	private boolean validatedCIMs = false;
	private List<double[][]> CIMs = null;
	
	/**
	 * Constructor of a node that evolves in continuous time.
	 * 
	 * @param nodeName node name
	 * @param states set of states of the node
	 * @exception IllegalStateException if a state contains character '@'
	 */
	public CTDiscreteNode(String nodeName, Set<String> states) throws IllegalStateException {
		this(nodeName, states, false);
	}
	
	/**
	 * Constructor
	 * 
	 * @param nodeName node name
	 * @param states set of states of the node
	 * @param staticNode true if the node is static, false if can evolve continuously in time
	 * @exception IllegalStateException if a state contains character '@'
	 */
	public CTDiscreteNode(String nodeName, Set<String> states, boolean staticNode) throws IllegalStateException {
		super(nodeName, states);
		
		Iterator<String> iter = states.iterator();
		while(iter.hasNext())
			if(iter.next().contains("@"))
				throw new IllegalStateException("Error: states names can not contain character '@'");
		
		this.staticNode = staticNode;
		this.CIMs = CTDiscreteNode.generateCIMsorCPTs(this.nParentsEntries, states.size(), this.staticNode);
		this.validatedCIMs = false;
	}
	
	/**
	 * Returns the entry value to get the right CIM
	 * given all the parents instanced.
	 * 
	 * @return the parents entry value
	 * @throws IllegalArgumentException if one parent is not instanced 
	 */
	public int getCurrentParentsEntry() throws IllegalArgumentException {

		int entry = 0;
		
		int parentMultiplier = 1;
		for(int i = 0; i < this.getParentsNumber(); ++i) {
			Integer stateIndex = this.getParent(i).getCurrentStateIndex();
			if( stateIndex == null)
				throw new IllegalArgumentException("Error: parent number " + i + " (" + this.getParent(i).getName() + ") is not instanced"); 
			entry += stateIndex * parentMultiplier;
			parentMultiplier *= this.getParent(i).getStatesNumber();
		}
		
		return entry;
	}
	
	/**
	 * Set the parents values given the parent
	 * entry.
	 * 
	 * @param pE parent entry (depend from the parents ordering)
	 * @throws IllegalArgumentException
	 */
	public void setParentsEntry(int pE) throws IllegalArgumentException {
	
		if( this.getParentsNumber() == 0)
			return;
		
		int parentMultiplier = this.getNumberParentsEntries();
		int i = this.getParentsNumber() - 1;
		do {
			CTDiscreteNode parent = this.getParent(i);
			parentMultiplier /= parent.getStatesNumber();
			
			int newState = pE / parentMultiplier;
			parent.setEvidence( newState);
			
			pE %= parentMultiplier;
			--i;
		}while(i >= 0);
		
	}
	
	/**
	 * Return the number of parents entries.
	 * 
	 * @return number of parents entries.
	 */
	public int getNumberParentsEntries() {
		
		return this.nParentsEntries;
	}
	
	/**
	 * Obtain the CIM value for a particular entry.
	 * 
	 * @param parentEntry parent entry that identify the parents values
	 * @param s0 state 0 (for static node always 0)
	 * @param s1 state 1 (for static node the index of the state for which get the probability)
	 * @return q value
	 * @throws RuntimeException can return exceptions if the indexes are out of bound or there aren't states
	 */
	public double getCIMValue(int parentEntry, int s0, int s1) throws RuntimeException {
		
		return this.CIMs.get(parentEntry)[s0][s1];
	}
	
	/**
	 * Return the CIM given the parent entry.
	 * 
	 * @param parentEntry parent entry that identify the parents values
	 * @return the CIM given the parents value
	 * @throws RuntimeException can return exceptions if the indexes are out of bound or there aren't states
	 */
	public double[][] getCIM(int parentEntry) throws RuntimeException {
		
		return this.CIMs.get(parentEntry);
	}
	
	/**
	 * Insert a value in the CIM for a particular
	 * parent entry.
	 * 
	 * @param parentEntry parent entry that identify the parents values
	 * @param s0 state 0
	 * @param s1 state 1
	 * @param value value to insert
	 * @throws RuntimeException can return exceptions if the indexes are out of bound or there aren't states
	 */
	public void setCIMValue(int parentEntry, int s0, int s1, double value) throws RuntimeException {
		
		this.CIMs.get(parentEntry)[s0][s1] = value;
		this.validatedCIMs = false;
	}
	
	/**
	 * Insert a complete CIM for a particular parent
	 * entry.
	 * 
	 * @param parentEntry parent entry that identify the parents values
	 * @param CIM CIM to insert
	 * @throws RuntimeException can return exceptions if the indexes are out of bound or there aren't states
	 */
	public void setCIM(int parentEntry, double[][] CIM) throws RuntimeException {
		
		if(CIM != null) {
			if( (!this.isStaticNode()) && CIM.length != CIM[0].length)
				throw new IllegalArgumentException("The CIM must be a squared matrix in continuous time nodes");
			else if( this.isStaticNode() && CIM.length != 1)
				throw new IllegalArgumentException("The CIM must be a vector in static nodes");
			
			if(CIM[0].length != this.getStatesNumber())
				throw new IllegalArgumentException("The CIM dimensions must correspond with number of states of the node");
		}
		
		this.CIMs.set(parentEntry, CIM);
		this.validatedCIMs = false;
	}
	
	/**
	 * Informs if the node is a static node or a
	 * continuous time node.
	 * The class is a static node.
	 * 
	 * @return true if it is a static node, false otherwise.
	 */
	public boolean isStaticNode() {
		return this.staticNode;
	}
	
	/**
	 * True if the CIMs were validated,
	 * false otherwise.
	 *
	 * @return if all the CIMs were validated.
	 */
	public boolean validatedCIMs() {
		
		return this.validatedCIMs;
	}
	
	/**
	 * Check the validity of all the CIMs
	 * and return the validation result.
	 * 
	 * @return -1 if all the CIMs are validated, otherwise the index of the first not valid parent entry CIM
	 */
	public int checkCIMs() {
		
		if( this.validatedCIMs)
			return -1;
		if( this.CIMs == null)
			return 0;
		
		for( int pE = 0; pE < this.CIMs.size(); ++pE) {
			double[][] cim = this.CIMs.get(pE);
			if(cim == null)
				return pE;
			
			for(int s0 = 0; s0 < cim.length; ++s0) {
				double sum = 0.0;
				for( int s1 = 0; s1 < cim[s0].length; ++s1) {
					if(this.staticNode) {
						if (cim[s0][s1] < 0 || cim[s0][s1] > 1)
							return pE;
					} else if((s0 == s1 && cim[s0][s1] > 0) || (s0 != s1 && cim[s0][s1] < 0))
						return pE;
					
					sum += cim[s0][s1];
				}
				if(((!this.staticNode) && (!Node.equalsDoubles(sum, 0.0, 0.0000001))) || (this.staticNode && (!Node.equalsDoubles(sum, 1.0, 0.0000001))))
					return pE; 
			}		
		}
		
		this.validatedCIMs = true;
		return -1;
	}

	/**
	 * Sample the next transition time given
	 * the current state and the current parents
	 * states.
	 * 
	 * @return the sampled time
	 * @throws RuntimeException if the system state doesn't allow to generate a transition time
	 */
	public double sampleTransitionTime() throws RuntimeException {
		
		if( this.isStaticNode())
			throw new IllegalStateException("Error: static nodes can not change state in time");
		if( !this.validatedCIMs())
			throw new IllegalStateException("Error: CIMs are not validated");
		if( !this.isInstanced())
			throw new IllegalStateException("Error: transition time can be calculated only for instanced nodes");
		
		double lambda = -this.getCIMValue(this.getCurrentParentsEntry(), this.getCurrentStateIndex(), this.getCurrentStateIndex());
		if( lambda == 0)						// if lambda is equal to zero the state is an absorbing state and can not change
			return Double.POSITIVE_INFINITY;
		
		return CTDiscreteNode.expSample(lambda);
	}
	
	/**
	 * Sample a state given the parents.
	 * If the node is static, sample a state
	 * using the prior. If the node is continuous
	 * sample the next jumping state.
	 * Note: sample a new state but doesn't set it.
	 * 
	 * @return index of the sampled state
	 * @throws RuntimeException if the system state doesn't allow to generate a new state
	 */
	public int sampleState() throws RuntimeException {
		
		if( !this.validatedCIMs())
			throw new IllegalStateException("Error: CIMs are not validated");
		
		if( this.isStaticNode())						// if the node is static
			return DiscreteModel.sample(this.getCIM(this.getCurrentParentsEntry())[0]);
		else if( !this.isInstanced())					// if the node is continuous but not instanced
			throw new IllegalStateException("Error: the next state can be calculated only for instanced continuous nodes");
		
		// If the node is continuous and it is instanced
		int stateIndex = this.getCurrentStateIndex();
		double[] pDistr = (this.getCIM(this.getCurrentParentsEntry())[stateIndex]).clone();
		if( pDistr.length < 2)
			throw new IllegalStateException("Error: in continuous node to jump to the next state you need at least 2 states");
		if( pDistr[stateIndex] == 0)
			throw new IllegalStateException("Error: the node it is in an absorbing state. New states can not be sampled");
		
		for(int i = 0; i < pDistr.length; ++i) 			// generate the probability distribution
			if( i != stateIndex)
				pDistr[i] /= -pDistr[stateIndex];		
		pDistr[stateIndex] = 0.0;

		return DiscreteModel.sample(pDistr);
	}
	
	@Override
	public boolean addParent(INode parent) throws RuntimeException {
		
		CTDiscreteNode ctParent = (CTDiscreteNode) parent;
		if( this.staticNode)
			throw new RuntimeException("Error: static nodes (class node) can not have parent");
		
		if( super.addParent(ctParent)) {
			this.nParentsEntries *= ctParent.getStatesNumber();
			this.CIMs = CTDiscreteNode.generateCIMsorCPTs( this.nParentsEntries, this.getStatesNumber(), this.staticNode);
			this.validatedCIMs = false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean removeParent(INode parent) throws RuntimeException {
		
		CTDiscreteNode ctParent = (CTDiscreteNode) parent;
		if( super.removeParent(ctParent)) {
			this.nParentsEntries /= ctParent.getStatesNumber();
			this.CIMs = CTDiscreteNode.generateCIMsorCPTs( this.nParentsEntries, this.getStatesNumber(), this.staticNode);
			this.validatedCIMs = false;
			return true;
		}
		
		return false;
	}
	
//	@Override
//	protected int addState(String stateName) throws IllegalStateException {
//		
//		if(stateName.contains("@"))
//			throw new IllegalStateException("Error: states names can not contain character '@'");
//		
//		int index = super.addState(stateName);
//		if( index != -1) {
//			this.CIMs = CTDiscreteNode.generateCIMsorCPTs( this.nParentsEntries, this.getStatesNumber(), this.staticNode);
//			this.validatedCIMs = false;
//		}
//		
//		return index;
//	}
	
	@Override
	public CTDiscreteNode getChild(int i) throws IllegalArgumentException {
		return (CTDiscreteNode) super.getChild(i);
	}
	
	@Override
	public CTDiscreteNode getChild(String childName) throws IllegalArgumentException {
		return (CTDiscreteNode) super.getChild(childName);
	}
	
	@Override
	public CTDiscreteNode getParent(int i) throws IllegalArgumentException {
		return (CTDiscreteNode) super.getParent(i); 
	}
	
	@Override
	public CTDiscreteNode getParent(String parentName) throws IllegalArgumentException {
		return (CTDiscreteNode) super.getParent(parentName);
	}
	
	/**
	 * Clone the node without coping the parental
	 * relation but only the name and the states.
	 * Also the CIMs are not copied.
	 * 
	 * @return the cloned node
	 */
	@Override
	public CTDiscreteNode clone() {
		
		TreeSet<String> states = new TreeSet<String>();
		for( int i = 0; i < this.getStatesNumber(); ++i)
			states.add(this.getStateName(i));
		
		CTDiscreteNode clonedNode = new CTDiscreteNode(this.getName(), states, this.staticNode);
		if( this.getCurrentState() != null)
			clonedNode.setEvidence(this.getCurrentState());
		
		return clonedNode;
	}
	
	/**
	 * Generate the CIMs or the CPTs for the node.
	 * 
	 * @param nParentsEntries number of entries for the parents
	 * @param nStates number of node states
	 * @param CPT true if the method has to generate CPTs, false if has to generate CIMs 
	 * @return the generated CIMs (null if there are no states)
	 */
	static private List<double[][]> generateCIMsorCPTs(int nParentsEntries, int nStates, boolean CPT) {
		
		if(nStates < 1)
			return null;
		
		List<double[][]> newCIMs = new Vector<double[][]>(nParentsEntries);
		for( int i = 0; i < nParentsEntries; ++i)
			if(!CPT)
				newCIMs.add(new double[nStates][nStates]);
			else
				newCIMs.add(new double[1][nStates]);
			
		return newCIMs;
	}
	
	/**
	 * Sample a value with exponential distribution
	 * with parameter lambda.
	 * 
	 * @param lambda lambda parameter of exponential distribution
	 * @return sampled time
	 */
	static public double expSample(double lambda) {

		return -Math.log(1 - Math.random()) / lambda ;
	}
	
}

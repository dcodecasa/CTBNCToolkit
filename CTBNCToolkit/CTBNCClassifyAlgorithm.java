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
 * Classification algorithm described in "Continuous Time
 * Bayesian Network Classifier" by Stella and Amer.
 * 
 * It returns the log-likelihood for each class.
 */
public class CTBNCClassifyAlgorithm extends ClassifyAlgorithm<Double, CTDiscreteNode> {

	private boolean probabilities;
	private IClassifyDecider classDecider = null;
	
	/**
	 * Base constructor.
	 */
	public CTBNCClassifyAlgorithm() {
		
		this.setDefaultParameters();
	}
	
	@Override
	public void setParameters(Map<String, Object> params) throws IllegalArgumentException {
		
		super.setParameters(params);
		
		Boolean tmpBoolean;
		// probabilities
		tmpBoolean = (Boolean)params.get("probabilities");
		if( tmpBoolean != null)
			this.probabilities = tmpBoolean;
		// class decider
		this.classDecider = (IClassifyDecider)params.get("classifyDecider");
		
	}
	
	@Override
	public String helpParameters() {
		
		String helpStr = "";
		helpStr += "Parameters class CTBNCClassifyAlgorithm:\n";
		helpStr += "probabilities: true if we want to know the probability distribution of the class for each transition, false if we want to know the probability only at the end of the trajector. [Default value = false].\n";
		helpStr += "classifyDecider: the decider that can be used to choose the class from the probability distribution. If not defined the most likely class is chose. If defined the probability calculation is forced. [Default value = null].\n";
		return helpStr;  
	}
	
	@Override
	public void setDefaultParameters() {

		this.probabilities = false;
		this.classDecider = null;
		
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("probabilities", this.probabilities);
		super.setParameters(params);
		
	}

	@Override
	public IClassificationResult<Double> classify( ICTClassifier<Double, CTDiscreteNode> model, ITrajectory<Double> trajectory) throws RuntimeException {
		
		return this.classify(model, trajectory, new Vector<Double>(0));
	}

	@Override
	public IClassificationResult<Double> classify( ICTClassifier<Double, CTDiscreteNode> model, ITrajectory<Double> trajectory, Double samplingInterval) throws RuntimeException {
		
		Vector<Double> timeStream = new Vector<Double>();
		double t = trajectory.getTransitionTime(0) + samplingInterval;
		double tEnd = trajectory.getTransitionTime(trajectory.getTransitionsNumber() - 1);
		while(t < tEnd) {
			timeStream.add(t);
			t += samplingInterval;
		}
		
		return this.classify(model, trajectory, timeStream);
	}
	
	@Override
	public IClassificationResult<Double> classify( ICTClassifier<Double, CTDiscreteNode> model, ITrajectory<Double> trajectory, Vector<Double> timeStream) throws RuntimeException {

		if( trajectory == null || trajectory.getTransitionsNumber() == 0)
			throw new IllegalArgumentException("Error: empty trajectory to classify");
		
		// Data initialization
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		CTDiscreteNode classNode = (CTDiscreteNode) model.getClassNode();		// class node
		int classNodeIndex = nodeIndexing.getClassIndex();						// class node index
		ClassificationResults<Double> results = new ClassificationResults<Double>(trajectory, timeStream); 	// the result trajectory
		Map<String,Integer> stateToIndex = new TreeMap<String,Integer>();		// to load the probability
		for(int iClass = 0; iClass < classNode.getStatesNumber(); ++iClass)
			stateToIndex.put(classNode.getStateName(iClass), iClass);
		 
		// Initialization at time 0
		double lastTime;
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
			CTDiscreteNode cNode = model.getNode(iNode);
			int check = cNode.checkCIMs();
			if( check != -1)
				throw new RuntimeException("Error: in CIM validation for node " + cNode.getName() + " and parent entry " + check);
			if( iNode == classNodeIndex)
				continue;
			cNode.setEvidence(results.getNodeValue(0, iNode)); 					// set the evidence
		}
		lastTime = results.getTransitionTime(0);								// set the last changing time
	
		// Initialize log likelihood
		double[] ll = new double[classNode.getStatesNumber()];
		double[] llContribution = new double[classNode.getStatesNumber()];		// contribution of the last jump (calculated as ll(t) - ll(t-1))
		for(int iClass = 0; iClass < ll.length; ++iClass) {						// for each possible class value
			classNode.setEvidence(iClass); 										// set the class
			// Calculate the log likelihood from the class prior
			// and from the probability of all the static nodes
			llContribution[iClass] = 0.0;
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {		// calculate the log likelihood over all the static nodes (note that no static nodes can have continuous time parents)
				CTDiscreteNode node = model.getNode(iNode);
				if( node.isStaticNode())
					llContribution[iClass] += Math.log( node.getCIMValue(node.getCurrentParentsEntry(), 0, node.getCurrentStateIndex()));
			}
			ll[iClass] = llContribution[iClass];
		}
		if( this.probabilities || this.classDecider != null)
			results.setProbability(0, logPToP(llContribution, null), stateToIndex);
		
		// Classification algorithm
		for(int iJmp = 1; iJmp < results.getTransitionsNumber(); ++iJmp) {					// for each transition in the trajectory
			
			double deltaT = results.getTransitionTime( iJmp) - lastTime;					// delta time between two jumps
			
			for(int iClass = 0; iClass < classNode.getStatesNumber(); ++iClass) {  			// for each possible class
				llContribution[iClass] = 0.0;
				classNode.setEvidence(iClass); 												// set the class

				for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {		// for each node
					if(iNode == classNodeIndex)
						continue;
					
					CTDiscreteNode node = model.getNode(iNode);
					int previousStateIndex = node.getCurrentStateIndex();
					int nextStateIndex = node.getStateIndex( results.getNodeValue(iJmp, iNode));
					if( node.isStaticNode()) {												// ignore the static nodes
						if( previousStateIndex != nextStateIndex)							// if the node is static and it is changed value there is an error in the arguments
							throw new IllegalArgumentException("Error: static nodes can not change their value in the trajectory");
						else
							continue;
					}
					int pE = node.getCurrentParentsEntry();
					
					// Update log-likelihood with the "remain in the state" contribute
					// ll update (ll = ll - q*dT)
					llContribution[iClass] += node.getCIMValue(pE, previousStateIndex, previousStateIndex) * deltaT;
					
					// Update log-likelihood with the jump contribute
					if(previousStateIndex == nextStateIndex)								// if the node is not really changed (the class node must be ignored because the actual value can be different from the right one in the trajectory)
						continue;															// ignore the node					
					// ll update v3(ll = ll + log(q_xx'))
					llContribution[iClass] += Math.log( node.getCIMValue(pE, previousStateIndex, nextStateIndex));
				}
				ll[iClass] += llContribution[iClass];
			}
			
			// Set the new values for the changed nodes
			for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {		// for each node
				CTDiscreteNode node = model.getNode(iNode);
				if( node.isStaticNode())
					continue;
				
				int previousStateIndex = node.getCurrentStateIndex();
				int nextStateIndex = node.getStateIndex( results.getNodeValue(iJmp, iNode));
				if( previousStateIndex == nextStateIndex)
					continue;
				
				node.setEvidence( nextStateIndex);  									// set the evidence
			}
			// Time update
			lastTime = results.getTransitionTime(iJmp);
			
			// Update the ll in the results	
			if( this.probabilities || this.classDecider != null)
				results.setProbability(iJmp, logPToP(llContribution, results.getPDistribution(iJmp - 1)), stateToIndex);
		}
		
		// Find the best class
		if( this.classDecider == null) {
			int iBest = 0;
			for( int iClass = 1; iClass < ll.length; ++iClass)
				if( ll[iClass] > ll[iBest])
					iBest = iClass;
			results.setClassification(classNode.getStateName(iBest));
		}else{
			results.setClassification( classNode.getStateName( this.classDecider.decide(results)));
		}
		
		return results;
	}

	/**
	 * Transform the vector of log probabilities contribution
	 * in a distribution.
	 * 
	 * @param llContribution vector of log probability contribution in the last time slice
	 * @param pPrevious probability distribution in the previous jump
	 * @return the corresponding probability distribution
	 */
	private double[] logPToP(double[] llContribution, double[] pPrevious) {
	
		double[] p = new double[llContribution.length];
		
		double sum = 0.0;
		for( int i = 0; i < llContribution.length; ++i) {
			p[i] = Math.exp(llContribution[i]);
			sum += p[i];
		}
		// Normalization
		if( pPrevious != null)
			if( sum != 0.0) {
				double sum2 = 0.0;
				for( int i = 0; i < llContribution.length; ++i) {
					p[i] = (p[i] / sum) * pPrevious[i];
					sum2 += p[i];
				}
				for( int i = 0; i < llContribution.length; ++i)
					p[i] = (p[i] / sum2);
			} else
				for( int i = 0; i < llContribution.length; ++i)
					p[i] = pPrevious[i];
		else
			for( int i = 0; i < llContribution.length; ++i)
				p[i] = (p[i] / sum);							// the sum can not be zero because at the first time slice there is the class prior
		
		return p;
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.IClassifyAlgorithm#probabilityFlag()
	 */
	@Override
	public boolean probabilityFlag() {
		
		return this.probabilities;
	}
	
}

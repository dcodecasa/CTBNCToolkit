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
 * Class that implement the maximum log likelihood
 * algorithm to learn the parameters of a given
 * CTBNC model.
 * 
 * The learning algorithm use the sufficient statistics
 * of the class LLSufficientStatistics.
 * 
 * Note: if the learned CIMs generate an error because
 * they seem not correct, please add the priors because
 * the problem is usually related to the zeros due to
 * the not enough big data set.
 */
public class CTBNCParameterLLAlgorithm extends LearningAlgorithm<Double, CTDiscreteNode> {

	private boolean[][] adjMStructure;
	private double Mxx_prior;
	private double Tx_prior;
	private double Px_prior;

	/**
	 * Base constructor.
	 * Set the default parameters and a
	 * disconnected structure.
	 */
	public CTBNCParameterLLAlgorithm() {
		
		this.adjMStructure = null;
		this.setDefaultParameters();
	}

	@Override
	public void setDefaultParameters() {

		this.Mxx_prior = 0.0;
		this.Tx_prior = 0.0;
		this.Px_prior = 0.0;
		
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", this.Mxx_prior);
		params.put("Tx_prior", this.Tx_prior);
		params.put("Px_prior", this.Px_prior);
		super.setParameters(params);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> params) throws IllegalArgumentException {

		super.setParameters(params);
		
		Double tmp;
		// Mxx_prior
		tmp = (Double)params.get("Mxx_prior");
		if( tmp != null)
			this.Mxx_prior = tmp;
			
		// Tx_prior
		tmp = (Double)params.get("Tx_prior");
		if( tmp != null)
			this.Tx_prior = tmp;
		
		// Px_prior
		tmp = (Double)params.get("Px_prior");
		if( tmp != null)
			this.Px_prior = tmp;
	}

	@Override
	public void setStructure(boolean[][] adjMatrix) throws IllegalArgumentException {

		this.adjMStructure = adjMatrix;
	}

	@Override
	public String helpParameters() {
		
		String helpStr = "";
		helpStr += "Parameters class CTBNCParameterLLAlgorithm:\n";
		helpStr += "Mxx_prior: prior for the value M[x,x'|u] (equal value for each x, x' and u). The sum over x' is the prior for M[x|u]. [Default value = 0].\n";
		helpStr += "Tx_prior: prior for the value T[x|u] (equal value for each x and u). [Default value = 0]. \n";
		helpStr += "Px_prior: prior for the value P[x|u] of the distribution of static nodes (equal value for each x and u). [Default value = 0]. \n";
		
		return helpStr;
	}
	
	/**
	 * Initialize the structure for the sufficient statistics
	 * calculation.
	 * 
	 * @param model the model to learn
	 * @return the sufficient statistics data initialized
	 * @throws RuntimeException in case of error during the process
	 */
	private SufficientStatistics[] learningInitialization(IModel<Double, CTDiscreteNode> model) throws RuntimeException{
		
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		SufficientStatistics lData[] = new SufficientStatistics[nodeIndexing.getNodesNumber()];
		for(int i = 0; i < lData.length; ++i) {
			CTDiscreteNode node = model.getNode(i);
			lData[i] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(),
					this.Px_prior, this.Mxx_prior, this.Tx_prior);
		}
		
		return lData;
	}
	
	/**
	 * Calculate the sufficient statistics for the
	 * parameters learning.
	 * 
	 * @param model the model to learn
	 * @param trainingSet the training set used to learn
	 * @return return the sufficient statistics learned
	 * @throws RuntimeException in case of some errors
	 */
	private SufficientStatistics[] calculateSufficientStatistics(IModel<Double, CTDiscreteNode> model, Collection<ITrajectory<Double>> trainingSet) throws RuntimeException {
		
		// Data initialization
		SufficientStatistics lData[] = learningInitialization(model);
		
		// Sufficient statistics calculation
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		Iterator<ITrajectory<Double>> itTrj = trainingSet.iterator(); 
		while( itTrj.hasNext()) { 													// for each trajectory in the training set
			ITrajectory<Double> trj = itTrj.next();
			
			if( trj.getTransitionsNumber() == 0)
				continue;

			// Initialization at time 0
			double lastTime = trj.getTransitionTime(0);								// set the last changing time
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
				CTDiscreteNode cNode = model.getNode(iNode);
				cNode.setEvidence(trj.getNodeValue(0, iNode)); 						// set the evidence
			}

			// Sufficient statistics for static nodes
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
				CTDiscreteNode cNode = model.getNode(iNode);
				if( cNode.isStaticNode()) {
					int pE = cNode.getCurrentParentsEntry();						// because we need the parent entry we can not join together the two loops
					++lData[iNode].Px[pE][0][cNode.getCurrentStateIndex()];
					++lData[iNode].counts[pE];
				}
			}

			// Sufficient statistics for the continuous nodes
			// For each jump
			for( int iJmp = 1; iJmp < trj.getTransitionsNumber(); ++iJmp) {
				
				double deltaT = trj.getTransitionTime(iJmp) - lastTime;
				
				// For each node (update the sufficient statistics)
				for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
					// Get the node information
					CTDiscreteNode node = model.getNode( iNode);
					int prevStIndex = node.getCurrentStateIndex();
					int nextStIndex = node.getStateIndex( trj.getNodeValue( iJmp, iNode));
					int pE = node.getCurrentParentsEntry();
					
					// Update the jump values
					if( !node.isStaticNode()) {
						lData[iNode].Tx[pE][prevStIndex] += deltaT;
						if(prevStIndex != nextStIndex) {								// if the node really jumped
							++lData[iNode].Mxx[pE][prevStIndex][nextStIndex];
							++lData[iNode].Mx[pE][prevStIndex];
						} 
					}else if(prevStIndex != nextStIndex) 
						throw new IllegalArgumentException("Error: static nodes (" + node.getName() + ") can not change their state during a trajectory (trj = " + trj.getName() + ")");
				}
	
				// For each changed node (set the new state)
				for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
					// Get the node information
					CTDiscreteNode node = model.getNode( iNode);
					int prevStIndex = node.getCurrentStateIndex();
					int nextStIndex = node.getStateIndex( trj.getNodeValue( iJmp, iNode));
					if( prevStIndex == nextStIndex)
						continue;
					
					node.setEvidence(nextStIndex);
				}

				// Update the last transition time 
				lastTime = trj.getTransitionTime(iJmp);
				
			} // end of the for over the jumps in a trajectory
		} // end while over trajectories

		// Retract all the evidences
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
			model.getNode(iNode).retractEvidence();

		return lData;
	}
	
	/**
	 * Calculate the parameters from the sufficient statistics
	 * and set the CIMs in the model.
	 * 
	 * @param model the model to learn
	 * @param lData the sufficient statistics
	 * @throws RuntimeException if some error occurs
	 */
	private void setCIMs(IModel<Double, CTDiscreteNode> model, SufficientStatistics lData[]) throws RuntimeException {
		// Parameter calculation
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
			CTDiscreteNode node = model.getNode(iNode);
			if( node.isStaticNode()) {
				for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					// Probability distribution calculation
					double[][] prob = new double[1][node.getStatesNumber()];
					for( int sE = 0; sE < node.getStatesNumber(); ++sE)
						prob[0][sE] = lData[iNode].Px[pE][0][sE] / lData[iNode].counts[pE];
					node.setCIM( pE, prob);
				}
			}else {
				for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					double[][] cim = new double[node.getStatesNumber()][node.getStatesNumber()];
					for( int fsE = 0; fsE < node.getStatesNumber(); ++fsE) {
						// CIM calculation
						cim[fsE][fsE] = -lData[iNode].Mx[pE][fsE] /  lData[iNode].Tx[pE][fsE];
						for( int ssE = 0; ssE < node.getStatesNumber(); ++ssE)
							if( fsE != ssE)
								cim[fsE][ssE] = lData[iNode].Mxx[pE][fsE][ssE] / lData[iNode].Tx[pE][fsE];
								// cim[fsE][ssE] = -cim[fsE][fsE] * (lData[iNode].Mxx[pE][fsE][ssE] / lData[iNode].Mx[pE][fsE]);
					}
					node.setCIM(pE, cim);
				}
			}
			
			int check = node.checkCIMs();
			if( check != -1)
				throw new RuntimeException("Error: unexpected error in CIM validation for node " + node.getName() + " and parent entry " + check + " (the inserting of the priors imaginary counts can solve this exception)");  
		}
	}
	
	@Override
	public GenericLearningResults learn(ICTClassifier<Double, CTDiscreteNode> model,  Collection<ITrajectory<Double>> trainingSet) throws RuntimeException {
		
		if( model == null)
			throw new IllegalArgumentException("Error: null model argument");
		
		// Set the structure
		if( this.adjMStructure != null)
			model.setStructure(this.adjMStructure);
		
		// Sufficient statistics calculation
		SufficientStatistics lData[] = calculateSufficientStatistics(model, trainingSet);
		
		// Calculate the parameter and update the CIMs of the model
		setCIMs(model, lData);
		
		return new GenericLearningResults(lData);
	}

	/**
	 * Return set prior over the sufficient
	 * statistic M[x,x'|u] for continuous time
	 * nodes.
	 * 
	 * @return the prior
	 */
	protected double getMxxPrior() {
		return this.Mxx_prior;
	}
	
	/**
	 * Return set prior over the sufficient
	 * statistic T[x|u] for continuous time
	 * nodes.
	 * 
	 * @return the prior
	 */
	protected double getTxPrior() {
		return this.Tx_prior;
	}
	
	/**
	 * Return set prior over the sufficient
	 * statistic P[x|u] for the static nodes.
	 * 
	 * @return the prior
	 */
	protected double getPxPrior() {
		return this.Px_prior;
	}
	
	@Override
	public boolean[][] getStructure() {
		return this.adjMStructure;
	}
	
}


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
package CTBNCToolkit.clustering;

import java.util.*;

import CTBNCToolkit.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Clustering algorithm used to learn parameters
 * over a fixed structure.
 * The clustering algorithm use a classification
 * algorithm. If not set CTBNCClassifyAlgorithm
 * is used.
 * 
 * The standard stopping criterion take in account
 * only the maximum number of iteration set to
 * StandardStopCriterion(10, 1/100.0).
 * 
 * Note: the input model in the learning required
 * the static node that represent the class.
 * The name of this static node must be declared
 * in the parameters of the algorithm ("Class" is
 * the default name).
 */
public class CTBNClusteringParametersLLAlgorithm extends ClusteringAlgorithm<Double, CTDiscreteNode> {

	private boolean[][] adjMStructure;
	private double Mxx_prior;
	private double Tx_prior;
	private double Px_prior;
	private boolean hardClustering;
	
	
	/**
	 * Base constructor.
	 * Set the default parameters and a
	 * disconnected structure.
	 */
	public CTBNClusteringParametersLLAlgorithm() {

		this.adjMStructure = null;
		
		// Classification algorithm setting
		Map<String, Object> clAlgParams = new TreeMap<String,Object>();
		clAlgParams.put("probabilities", true);
		CTBNCClassifyAlgorithm classAlg = new CTBNCClassifyAlgorithm();
		classAlg.setParameters(clAlgParams);
		super.setClassificationAlgorithm( classAlg);
		
		// Stopping criterion setting
		super.setStopCriterion( new StandardStopCriterion(10, 1/100.0));
		
		// Setting default parameters
		this.setDefaultParameters();
	}
	
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#setDefaultParameters()
	 */
	@Override
	public void setDefaultParameters() {
		
		this.Mxx_prior = 0.0;
		this.Tx_prior = 0.0;
		this.Px_prior = 0.0;
		this.hardClustering = false;
		
		Map<String, Object> params = new TreeMap<String,Object>();
		params.put("Mxx_prior", this.Mxx_prior);
		params.put("Tx_prior", this.Tx_prior);
		params.put("Px_prior", this.Px_prior);
		params.put("hardClustering", this.hardClustering);
		super.setParameters(params);
	}
	
	
	@Override
	public void setParameters(Map<String, Object> params) throws IllegalArgumentException {

		super.setParameters(params);
		
		Double tmpDouble;
		Boolean tmpBool;
		// Mxx_prior
		tmpDouble = (Double)params.get("Mxx_prior");
		if( tmpDouble != null)
			this.Mxx_prior = tmpDouble;
			
		// Tx_prior
		tmpDouble = (Double)params.get("Tx_prior");
		if( tmpDouble != null)
			this.Tx_prior = tmpDouble;
		
		// Px_prior
		tmpDouble = (Double)params.get("Px_prior");
		if( tmpDouble != null)
			this.Px_prior = tmpDouble;
		
		// hardClustering
		tmpBool = (Boolean)params.get("hardClustering");
		if( tmpBool != null)
			this.hardClustering = tmpBool;

	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#setStructure(boolean[][])
	 */
	@Override
	public void setStructure(boolean[][] adjMatrix) throws IllegalArgumentException {
		
		this.adjMStructure = adjMatrix;
		
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#helpParameters()
	 */
	@Override
	public String helpParameters() {

		String helpStr = "";
		helpStr += "Parameters class CTBNClusteringParametersLLAlgorithm:\n";
		helpStr += "Mxx_prior: prior for the value M[x,x'|u] (equal value for each x, x' and u). The sum over x' is the prior for M[x|u]. [Default value = 0].\n";
		helpStr += "Tx_prior: prior for the value T[x|u] (equal value for each x and u). [Default value = 0]. \n";
		helpStr += "Px_prior: prior for the value P[x|u] of the distribution of static nodes (equal value for each x and u). [Default value = 0]. \n";
		helpStr += "hardClustering: true to use hard clustering algorithm, false to use soft clustering algorithm. [Default value = false]. \n";
		
		return helpStr;
	}

	
	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#learn(CTBNToolkit.IModel, java.util.Collection)
	 */
	@Override
	public ClusteringResults<Double> learn(
			ICTClassifier<Double, CTDiscreteNode> model,
			Collection<ITrajectory<Double>> trainingSet)
			throws RuntimeException {

		
		// Parameters checking
		if( (!this.classificationAlgorithm.probabilityFlag()) && (!this.hardClustering))
			throw new IllegalArgumentException("Soft clustering algorithm needs a classification algorithm able to return the probability distribution of the class");
		if( trainingSet == null)
			throw new IllegalArgumentException("Error: null training set argument");
		if( trainingSet.size() == 0)
			throw new IllegalArgumentException("Error: empty training set");
		if( model == null)
			throw new IllegalArgumentException("Error: null model argument");
		
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int classNodeIndex = nodeIndexing.getClassIndex();
		if( classNodeIndex == -1)
			throw new IllegalArgumentException("Error: general indexing method doesn't contain " + classNodeIndex + " class node index");
		
		//Algorithm initialization
		// Set the structure
		if( this.adjMStructure != null)
			model.setStructure(this.adjMStructure);		

		// Generate the sufficient statistics divided by trajectory
		SufficientStatistics[][] trjSStats = generateTrajectoriesSufficientStatistics(model, trainingSet);
		// Generate initial sufficient statistics
		SufficientStatistics[] sStats = calculateInitialSufficientStatistics(model, trjSStats);
		// Set the parameters
		setCIMs(model, sStats);
		
		// Clustering algorithm
		double changedCluster = Double.MAX_VALUE;
		List<IClassificationResult<Double>> clusterizedTrajectories = initializeClusterizedTrajectories(trainingSet);
		for(int i = 0; true; ++i) {

			// Classification algorithm
			changedCluster = 0;
			for(int iTrj = 0; iTrj < clusterizedTrajectories.size(); ++iTrj) {
				IClassificationResult<Double> trjResult = this.classificationAlgorithm.classify(model, clusterizedTrajectories.get(iTrj));	// classify
				if( !trjResult.getClassification().equals(clusterizedTrajectories.get(iTrj).getClassification()))  // count the trajectories that changed value 
					++changedCluster;
				clusterizedTrajectories.set(iTrj, trjResult);						// add the new classified trajectory
			}
			
			// Stopping criteria evaluation
			if( stopCriterion.paramsStop(i, changedCluster/clusterizedTrajectories.size()))
				break;
			
			// New sufficient statistics calculation
			if( this.hardClustering)
				sStats = calculateNewHardSufficientStatistics(model, trjSStats, clusterizedTrajectories, sStats);
			else
				sStats = calculateNewSoftSufficientStatistics(model, trjSStats, clusterizedTrajectories, sStats);
			// Set the parameters
			setCIMs(model, sStats);
		}
		
		return new ClusteringResults<Double>(sStats, clusterizedTrajectories);
	}
	
	
	/**
	 * Calculate hard sufficient statistics.
	 * Make the calculation using the sufficient
	 * statistics for each trajectory (trjSStats) 
	 * and the initialized vector of sufficient
	 * statistics.
	 * If this vector will contain old sufficient
	 * statistics for the nodes (not the class) that
	 * have not the class as parent, these sufficient
	 * statistics are left unchanged. Instead, if
	 * the statistics of a node are null than the
	 * new statistics are normally calculated.
	 * 
	 * Note: the trajectories sufficient statistics
	 * are calculated supposing the assignment of
	 * state 0 for the class node.
	 * 
	 * @param model model for which we are calculating the sufficient statistics
	 * @param trjSStats sufficient statistics for each trajectories
	 * @param clusterizedTrajectories trajectories previous classificated
	 * @param stats initialized vector of sufficient statistics
	 * @return completed sufficient statistics over all trajectories
	 */
	private SufficientStatistics[] calculateNewHardSufficientStatistics(
			ICTClassifier<Double, CTDiscreteNode> model,
			SufficientStatistics[][] trjSStats,
			List<IClassificationResult<Double>> clusterizedTrajectories,
			SufficientStatistics[] stats) {
		
		// Initialization
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int iClassNode = nodeIndexing.getClassIndex();
		CTDiscreteNode classNode = model.getNode(iClassNode);
		stats[iClassNode] = new SufficientStatistics(classNode.getNumberParentsEntries(), classNode.getStatesNumber(), classNode.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
		//! Safety check. To extend the code it is necessary
		// define the soft sample for the instantiation of
		// the parents of the class node (see next for)
		if( stats[iClassNode].Px.length != 1)
			throw new RuntimeException("Error: clustering algorithm is developted for class node that have not parents");
		
		// Class node sufficient statistics
		int[] trjClassIdx = new int[clusterizedTrajectories.size()];
		for(int iTrj = 0; iTrj < clusterizedTrajectories.size(); ++iTrj) {
			trjClassIdx[iTrj] = classNode.getStateIndex( clusterizedTrajectories.get( iTrj).getClassification());

			++stats[iClassNode].Px[0][0][trjClassIdx[iTrj]];
			++stats[iClassNode].counts[0];
		}
				
		// Sufficient statistics calculation
		for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {			// for each node
			if( iNode == iClassNode)													// ignore the class node
				continue;
			
			CTDiscreteNode node = model.getNode(iNode);
			
			if( node.getParentIndex( classNode.getName()) == null) {					// if the class is not in the node parent set
				if( stats[iNode] != null)												// if the statistics are previously calculated
					continue;															// ignore the node and keep the old statistics
				
				stats[iNode] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
				if( node.isStaticNode()) {												// if the node is static
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Px.length; ++pE) {
							stats[iNode].counts[pE] += trjSStats[iTrj][iNode].counts[pE];
							for( int sE = 0; sE < trjSStats[iTrj][iNode].Px[0][0].length; ++sE)
								stats[iNode].Px[pE][0][sE] += trjSStats[iTrj][iNode].Px[pE][0][sE];
						}
				} else { 																// if the node is dynamic
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Mxx.length; ++pE)
							for( int fSE = 0; fSE < trjSStats[iTrj][iNode].Mxx[0].length; ++fSE) {
								stats[iNode].Tx[pE][fSE] += trjSStats[iTrj][iNode].Tx[pE][fSE];
								stats[iNode].Mx[pE][fSE] += trjSStats[iTrj][iNode].Mx[pE][fSE];
								for( int sSE = 0; sSE < trjSStats[iTrj][iNode].Mxx[0][0].length; ++sSE)
									stats[iNode].Mxx[pE][fSE][sSE] += trjSStats[iTrj][iNode].Mxx[pE][fSE][sSE];
							}
				}
			} else {																	// if the class node is a parent in the node parent set
				stats[iNode] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
				
				if( node.isStaticNode()) {												// if the node is static
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Px.length; ++pE) {
							// Get class state in the current parent entry
							node.setParentsEntry(pE);
							if( classNode.getCurrentStateIndex() != trjClassIdx[iTrj])	// if it is not the parent entry with the right class state
								continue;
							// Get the pE used to save the parameters for each trajectories
							classNode.setEvidence(0);
							int pETrj = node.getCurrentParentsEntry();
							
							stats[iNode].counts[pE] += trjSStats[iTrj][iNode].counts[pETrj];
							for( int sE = 0; sE < trjSStats[iTrj][iNode].Px[0][0].length; ++sE)
								stats[iNode].Px[pE][0][sE] += trjSStats[iTrj][iNode].Px[pETrj][0][sE];
						}
				} else { 																// if the node is dynamic
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Mxx.length; ++pE) {
							// Get class state in the current parent entry
							node.setParentsEntry(pE);
							if( classNode.getCurrentStateIndex() != trjClassIdx[iTrj])	// if it is not the parent entry with the right class state
								continue;
							// Get the pE used to save the parameters for each trajectories
							classNode.setEvidence(0);
							int pETrj = node.getCurrentParentsEntry();							

							for( int fSE = 0; fSE < trjSStats[iTrj][iNode].Mxx[0].length; ++fSE) {
								stats[iNode].Tx[pE][fSE] += trjSStats[iTrj][iNode].Tx[pETrj][fSE];
								stats[iNode].Mx[pE][fSE] += trjSStats[iTrj][iNode].Mx[pETrj][fSE];
								for( int sSE = 0; sSE < trjSStats[iTrj][iNode].Mxx[0][0].length; ++sSE)
									stats[iNode].Mxx[pE][fSE][sSE] += trjSStats[iTrj][iNode].Mxx[pETrj][fSE][sSE];
							}
						}
				}
			}
		}
		
		// Retract all the evidences
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
			model.getNode(iNode).retractEvidence();
		
		return stats;
	}


	/**
	 * Calculate soft sufficient statistics for
	 * all the nodes except the class node.
	 * Make the calculation using the sufficient
	 * statistics for each trajectory (trjSStats),
	 * the probability distribution of the class
	 * for each trajectory (trjPDist) and the
	 * initialized vector of sufficient statistics
	 * with the statistics of the class calculated
	 * previously.
	 * If this vector will contain old sufficient
	 * statistics for the nodes that have not the
	 * class as parent, these sufficient statistics
	 * are left unchanged. Instead, if the statistics
	 * of a node are null than the new statistics
	 * are normally calculated.
	 * 
	 * Note: the trajectories sufficient statistics
	 * are calculated supposing the assignment of
	 * state 0 for the class node.
	 * 
	 * @param model model for which we are calculating the sufficient statistics
	 * @param trjSStats sufficient statistics for each trajectories
	 * @param trjPDist probability distribution over the class for each trajectory
	 * @param stats initialized vector of sufficient statistics (class sufficient statistics must be calculated previously)
	 * @return completed sufficient statistics over all trajectories
	 */
	private SufficientStatistics[] calculateSoftSufficientStatistics(
			ICTClassifier<Double, CTDiscreteNode> model,
			SufficientStatistics[][] trjSStats,
			double[][] trjPDist,
			SufficientStatistics[] stats) {
		
		// Initialization
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int iClassNode = nodeIndexing.getClassIndex();
		CTDiscreteNode classNode = model.getNode(iClassNode);
		
		// Sufficient statistics calculation
		for(int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {		// for each node
			if( iNode == iClassNode)												// ignore the class node
				continue;
			
			CTDiscreteNode node = model.getNode(iNode);
			
			if( node.getParentIndex( classNode.getName()) == null) {				// if the class is not in the node parent set
				if( stats[iNode] != null)											// if the statistics are previously calculated
					continue;														// ignore the node and keep the old statistics
				
				stats[iNode] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
				if( node.isStaticNode()) {											// if the node is static
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Px.length; ++pE) {
							stats[iNode].counts[pE] += trjSStats[iTrj][iNode].counts[pE];
							for( int sE = 0; sE < trjSStats[iTrj][iNode].Px[0][0].length; ++sE)
								stats[iNode].Px[pE][0][sE] += trjSStats[iTrj][iNode].Px[pE][0][sE];
						}
				} else { 															// if the node is dynamic
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Mxx.length; ++pE)
							for( int fSE = 0; fSE < trjSStats[iTrj][iNode].Mxx[0].length; ++fSE) {
								stats[iNode].Tx[pE][fSE] += trjSStats[iTrj][iNode].Tx[pE][fSE];
								stats[iNode].Mx[pE][fSE] += trjSStats[iTrj][iNode].Mx[pE][fSE];
								for( int sSE = 0; sSE < trjSStats[iTrj][iNode].Mxx[0][0].length; ++sSE)
									stats[iNode].Mxx[pE][fSE][sSE] += trjSStats[iTrj][iNode].Mxx[pE][fSE][sSE];
							}
				}
			} else {																// if the class node is a parent in the node parent set
				stats[iNode] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
				
				if( node.isStaticNode()) {											// if the node is static
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Px.length; ++pE) {
							// Get class state in the current parent entry
							node.setParentsEntry(pE);
							int iClassState = classNode.getCurrentStateIndex();
							// Get the pE used to save the parameters for each trajectories
							classNode.setEvidence(0);
							int pETrj = node.getCurrentParentsEntry();
							
							stats[iNode].counts[pE] += ( trjSStats[iTrj][iNode].counts[pETrj] * trjPDist[iTrj][iClassState]);
							for( int sE = 0; sE < trjSStats[iTrj][iNode].Px[0][0].length; ++sE)
								stats[iNode].Px[pE][0][sE] += ( trjSStats[iTrj][iNode].Px[pETrj][0][sE] * trjPDist[iTrj][iClassState]);
						}
				} else { 															// if the node is dynamic
					for(int iTrj = 0; iTrj < trjSStats.length; ++iTrj)
						for( int pE = 0; pE < trjSStats[iTrj][iNode].Mxx.length; ++pE) {
							// Get class state in the current parent entry
							node.setParentsEntry(pE);
							int iClassState = classNode.getCurrentStateIndex();
							// Get the pE used to save the parameters for each trajectories
							classNode.setEvidence(0);
							int pETrj = node.getCurrentParentsEntry();							

							for( int fSE = 0; fSE < trjSStats[iTrj][iNode].Mxx[0].length; ++fSE) {
								stats[iNode].Tx[pE][fSE] += ( trjSStats[iTrj][iNode].Tx[pETrj][fSE] * trjPDist[iTrj][iClassState]);
								stats[iNode].Mx[pE][fSE] += ( trjSStats[iTrj][iNode].Mx[pETrj][fSE] * trjPDist[iTrj][iClassState]);
								for( int sSE = 0; sSE < trjSStats[iTrj][iNode].Mxx[0][0].length; ++sSE)
									stats[iNode].Mxx[pE][fSE][sSE] += ( trjSStats[iTrj][iNode].Mxx[pETrj][fSE][sSE] * trjPDist[iTrj][iClassState]);
							}
						}
				}
			}
		}
		
		// Retract all the evidences
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
			model.getNode(iNode).retractEvidence();
		
		return stats;
	}
	
	
	/**
	 * Calculate the new sufficient statistics
	 * using the sufficient statistics memorized
	 * for each trajectory and the previous
	 * classification.
	 * 
	 * @param model model for which we are calculating the sufficient statistics
	 * @param trjSStats sufficient statistics for each trajectories
	 * @param clusterizedTrajectories trajectories previous clusterized
	 * @param oldStats old sufficient statistics used to avoid to calculates statistics for the nodes that doesn't have the class as parent
	 * @return the results of the clustering (sufficient statistics and clusterized training set in the same order of the original training set)
	 */
	private SufficientStatistics[] calculateNewSoftSufficientStatistics(
			ICTClassifier<Double, CTDiscreteNode> model,
			SufficientStatistics[][] trjSStats,
			List<IClassificationResult<Double>> trajectories,
			SufficientStatistics[] oldStats) {

		// Initialization
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int iClassNode = nodeIndexing.getClassIndex();
		CTDiscreteNode classNode = model.getNode(iClassNode);
		oldStats[iClassNode] = new SufficientStatistics(classNode.getNumberParentsEntries(), classNode.getStatesNumber(), classNode.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
		//! Safety check. To extend the code it is necessary
		// define the soft sample for the instantiation of
		// the parents of the class node (see next for)
		if( oldStats[iClassNode].Px.length != 1)
			throw new RuntimeException("Error: clustering algorithm is developted for class node that have not parents");
		
		// Trajectories class initialization and class node sufficient statistics
		double[][] trjPDist = new double[trjSStats.length][classNode.getStatesNumber()];
		for(int iTrj = 0; iTrj < trjPDist.length; ++iTrj) {
			trjPDist[iTrj] = trajectories.get( iTrj).getPDistribution();
			
			for(int sE = 0; sE < trjPDist[0].length; ++sE) {
				oldStats[iClassNode].Px[0][0][sE] += trjPDist[iTrj][sE];
				oldStats[iClassNode].counts[0] += trjPDist[iTrj][sE];
			}
		}
		
		return calculateSoftSufficientStatistics(model, trjSStats, trjPDist, oldStats);
	}


	/**
	 * Calculate the initial sufficient statistics.
	 *  
	 * @param model model for which we are calculating the sufficient statistics
	 * @param trjSStats sufficient statistics for each trajectories
	 * @return initial sufficient statistics
	 */
	private SufficientStatistics[] calculateInitialSufficientStatistics(
			ICTClassifier<Double, CTDiscreteNode> model,
			SufficientStatistics[][] trjSStats) {

		// Initialization
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int iClassNode = nodeIndexing.getClassIndex();
		CTDiscreteNode classNode = model.getNode(iClassNode);
		SufficientStatistics[] stats = new SufficientStatistics[nodeIndexing.getNodesNumber()];
		stats[iClassNode] = new SufficientStatistics(classNode.getNumberParentsEntries(), classNode.getStatesNumber(), classNode.isStaticNode(), this.Px_prior, this.Mxx_prior, this.Tx_prior);
		//! Safety check. To extend the code it is necessary
		// define the soft sample for the instantiation of
		// the parents of the class node (see next for)
		if( stats[iClassNode].Px.length != 1)
			throw new RuntimeException("Error: clustering algorithm is developted for class node that have not parents");
		
		// Trajectories class initialization and class node sufficient statistics
		double[][] trjPDist = new double[trjSStats.length][classNode.getStatesNumber()];
		for(int iTrj = 0; iTrj < trjPDist.length; ++iTrj) {
			trjPDist[iTrj] = softSampling(classNode.getStatesNumber());
			
			for(int sE = 0; sE < trjPDist[0].length; ++sE) {
				stats[iClassNode].Px[0][0][sE] += trjPDist[iTrj][sE];
				stats[iClassNode].counts[0] += trjPDist[iTrj][sE];
			}
		}
		
		return calculateSoftSufficientStatistics(model, trjSStats, trjPDist, stats);
	}


	/**
	 * Generate a matrix of sufficient statistics.
	 * Rows represent the trajectory in the training
	 * set trajectories order. Column represent
	 * all the nodes in the model.
	 * 
	 * The generated sufficient statistics are
	 * generated supposing always the assignment
	 * of state zero for the class node.
	 * 
	 * @param model model used to generate the sufficient statistics
	 * @param trainingSet training set used to calculate the counts
	 * @return matrix of sufficient statistics for each trajectory supposing always the state zero value for the class node
	 */
	private SufficientStatistics[][] generateTrajectoriesSufficientStatistics(
			IModel<Double, CTDiscreteNode> model,
			Collection<ITrajectory<Double>> trainingSet) {
		
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		int iClassNode = nodeIndexing.getClassIndex();

		// Initialize the sufficient statistics
		SufficientStatistics[][] stats = initializeTrajectoriesSufficientStatistics(model, trainingSet);
		
		// Sufficient statistics calculation
		Iterator<ITrajectory<Double>> itTrj = trainingSet.iterator();
		for( int iTrj = 0; itTrj.hasNext(); ++iTrj) {								// for each trajectory in the training set
			ITrajectory<Double> trj = itTrj.next();
			
			if( trj.getTransitionsNumber() == 0)
				continue;

			// Initialization at time 0
			double lastTime = trj.getTransitionTime(0);								// set the last changing time
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
				CTDiscreteNode cNode = model.getNode(iNode);
				if( iNode == iClassNode)											// because the class is unknown we suppose the first
					cNode.setEvidence(0); 											// set the evidence
				else
					cNode.setEvidence(trj.getNodeValue(0, iNode)); 		// set the evidence
			}

			// Sufficient statistics for static nodes
			for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
				CTDiscreteNode cNode = model.getNode(iNode);
				if( cNode.isStaticNode()) {
					int pE = cNode.getCurrentParentsEntry();						// Note: because we need the parent entry we can not join together the two loops
					++stats[iTrj][iNode].Px[pE][0][cNode.getCurrentStateIndex()];
					++stats[iTrj][iNode].counts[pE];
				}
			}

			// Sufficient statistics for the continuous nodes
			// For each jump
			for( int iJmp = 1; iJmp < trj.getTransitionsNumber(); ++iJmp) {
				
				double deltaT = trj.getTransitionTime(iJmp) - lastTime;
				
				// For each node (update the sufficient statistics)
				for(int chNodeIndex = 0; chNodeIndex < nodeIndexing.getNodesNumber(); ++chNodeIndex) {
					if( chNodeIndex == iClassNode)										// ignore the class node
						continue;
					
					// Get the node information
					CTDiscreteNode chNode = model.getNode( chNodeIndex);
					int prevStIndex = chNode.getCurrentStateIndex();
					int nextStIndex = chNode.getStateIndex( trj.getNodeValue( iJmp, chNodeIndex));
					int pE = chNode.getCurrentParentsEntry();
					
					// Update the jump values
					if( !chNode.isStaticNode()) {
						stats[iTrj][chNodeIndex].Tx[pE][prevStIndex] += deltaT;
						if(prevStIndex != nextStIndex) {								// if the node really jumped
							++stats[iTrj][chNodeIndex].Mxx[pE][prevStIndex][nextStIndex];
							++stats[iTrj][chNodeIndex].Mx[pE][prevStIndex];
						} 
					}else if(prevStIndex != nextStIndex) 
						throw new IllegalArgumentException("Error: static nodes (" + chNode.getName() + ") can not change their state during a trajectory (trj = " + trj.getName() + ")");
				}
	
				// For each changed node (set the new state)
				for(int chNodeIndex = 0; chNodeIndex < nodeIndexing.getNodesNumber(); ++chNodeIndex) {
					if( chNodeIndex == iClassNode)										// ignore the class node
						continue;
					
					// Get the node information
					CTDiscreteNode chNode = model.getNode( chNodeIndex);
					int prevStIndex = chNode.getCurrentStateIndex();
					int nextStIndex = chNode.getStateIndex( trj.getNodeValue( iJmp, chNodeIndex));
					if( prevStIndex == nextStIndex)
						continue;
					
					chNode.setEvidence(nextStIndex);
				}

				// Update the last transition time 
				lastTime = trj.getTransitionTime(iJmp);
				
			} // end of the for over the jumps in a trajectory
		} // end for over trajectories

		// Retract all the evidences
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode)
			model.getNode(iNode).retractEvidence();

		return stats;
	}


	/**
	 * Initialize the sufficient statistics
	 * for each trajectory and put to zero
	 * the statistics values.
	 * 
	 * @param model model used to generate the sufficient statistics
	 * @param trainingSet training set used to calculate the counts
	 * @return matrix of initialized sufficient statistics with zero values
	 */
	private SufficientStatistics[][] initializeTrajectoriesSufficientStatistics(
			IModel<Double, CTDiscreteNode> model,
			Collection<ITrajectory<Double>> trainingSet) {
		 
		SufficientStatistics[][] stats = new SufficientStatistics[trainingSet.size()][model.getNodeIndexing().getNodesNumber()];
		for(int iNode = 0; iNode < stats[0].length; ++iNode) {
			CTDiscreteNode node = model.getNode(iNode);
			for( int iTrj = 0; iTrj < stats.length; ++iTrj)
				stats[iTrj][iNode] = new SufficientStatistics(node.getNumberParentsEntries(), node.getStatesNumber(), node.isStaticNode(), 0.0, 0.0, 0.0);
		}
		
		return stats;
	}
	
	
	/**
	 * Initialize the clusterable trajectories
	 * of the training set.
	 * 
	 * @param trainingSet the training set from which generate the clusterable trajectories
	 * @return clusterable trajectories initialized
	 */
	private List<IClassificationResult<Double>> initializeClusterizedTrajectories(
			Collection<ITrajectory<Double>> trainingSet) {
		
		List<IClassificationResult<Double>> trajectories = new Vector<IClassificationResult<Double>>(trainingSet.size());
		Iterator<ITrajectory<Double>> itTrj = trainingSet.iterator();
		for( int i = 0; itTrj.hasNext(); ++i) {
			ITrajectory<Double> trj = itTrj.next();
			if( IClassificationResult.class.isAssignableFrom(trj.getClass()))
				trajectories.add(i, (IClassificationResult<Double>) trj);
			else
				trajectories.add(i, new ClassificationResults<Double>(trj));
		}
		
		return trajectories;
	}
	
	
	/**
	 * Calculate the parameters from the sufficient statistics
	 * and set the CIMs in the model.
	 * 
	 * @param model the model to learn
	 * @param sStats the sufficient statistics
	 * @throws RuntimeException if some error occurs
	 */
	private void setCIMs(IModel<Double, CTDiscreteNode> model, SufficientStatistics sStats[]) throws RuntimeException {
		// Parameter calculation
		NodeIndexing nodeIndexing = model.getNodeIndexing();
		for( int iNode = 0; iNode < nodeIndexing.getNodesNumber(); ++iNode) {
			CTDiscreteNode node = model.getNode(iNode);
			if( node.isStaticNode()) {
				for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					// Probability distribution calculation
					double[][] prob = new double[1][node.getStatesNumber()];
					for( int sE = 0; sE < node.getStatesNumber(); ++sE)
						prob[0][sE] = sStats[iNode].Px[pE][0][sE] / sStats[iNode].counts[pE];
					node.setCIM( pE, prob);
				}
			}else {
				for( int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
					double[][] cim = new double[node.getStatesNumber()][node.getStatesNumber()];
					for( int fsE = 0; fsE < node.getStatesNumber(); ++fsE) {
						// CIM calculation
						cim[fsE][fsE] = -sStats[iNode].Mx[pE][fsE] /  sStats[iNode].Tx[pE][fsE];
						for( int ssE = 0; ssE < node.getStatesNumber(); ++ssE)
							if( fsE != ssE)
								cim[fsE][ssE] = sStats[iNode].Mxx[pE][fsE][ssE] / sStats[iNode].Tx[pE][fsE];
					}
					node.setCIM(pE, cim);
				}
			}
			
			int check = node.checkCIMs();
			if( check != -1)
				throw new RuntimeException("Error: unexpected error in CIM validation for node " + node.getName() + " and parent entry " + check);
		}
	}
	
	
	/**
	 * Sample a probability distribution
	 * over the states.
	 * 
	 * @param nStates number of states
	 * @return a probability distribution over the states
	 */
	static public double[] softSampling(int nStates) {
		
		double sum = 0.0;
		double[] p = new double[nStates];
		
		for(int i = 0; i < nStates; ++i) {
			p[i] = Math.random();
			sum += p[i];
		}
		for(int i = 0; i < nStates; ++i) {
			p[i] /= sum;
		}
		
		return p;
	}


	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningAlgorithm#getStructure()
	 */
	@Override
	public boolean[][] getStructure() {

		return this.adjMStructure;
	}

}

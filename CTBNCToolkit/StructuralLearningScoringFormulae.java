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

import java.util.Collection;

import org.apache.commons.math3.special.Gamma;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that contain some static methods to calculate
 * the scoring during the structural learning process.
 */
public class StructuralLearningScoringFormulae {

	
	/**
	 * Calculate the log-likelihood scoring.
	 * 
	 * @param model model to evaluate
	 * @param nodeIndex index of the node that we are evaluating
	 * @param dataset dataset used for the evaluation
	 * @param paramsLearningAlg algorithm used to learn the parameters
	 * @param dimensionPenalty true if the scoring of the individual has to give a penalty related to the dimension of the structure, false otherwise 
	 * @return log likelihood scoring
	 * @throws RuntimeException in case of some error during the calculation
	 */
	public static double logLikelihoodScore(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex,
			Collection<ITrajectory<Double>> dataset,
			ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg,
			boolean dimensionPenalty) throws RuntimeException {
		
		if( dataset == null)
			throw new RuntimeException("Error: dataset didn't set");
		if( paramsLearningAlg.getStructure() != null)
			throw new RuntimeException("Error: parameter algorithm used in the scoring function for structural learning can not have structure setted");
		
		CTDiscreteNode node = model.getNode(nodeIndex);
		if( node.isStaticNode())
			throw new IllegalArgumentException("Error: this scoring function is not defined for static nodes");
		
		double llScore = 0.0;
		double mxx = (Double) paramsLearningAlg.getParameter("Mxx_prior");
		double tx = (Double) paramsLearningAlg.getParameter("Tx_prior");
		
		SufficientStatistics[] ss = paramsLearningAlg.learn(model, dataset).getSufficientStatistics();
		
		// Marginal value calculation
		double mx = mxx*(node.getStatesNumber() - 1);
		for(int fsE = 0; fsE < node.getStatesNumber(); ++fsE) {
			for(int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
				
				// Calculate MargLq (q-value)
				llScore += Gamma.logGamma( ss[nodeIndex].Mx[pE][fsE] + 1);
				llScore += (mx + 1) * Math.log( tx);
				llScore -= Gamma.logGamma( mx + 1);
				llScore -= (ss[nodeIndex].Mx[pE][fsE] + 1) * Math.log( ss[nodeIndex].Tx[pE][fsE]);
				
				// Calculate MargLth (theta)
				llScore += Gamma.logGamma( mx);
				llScore -= Gamma.logGamma( ss[nodeIndex].Mx[pE][fsE]);
				for(int ssE = 0; ssE < node.getStatesNumber(); ++ssE) {
					if( fsE == ssE)
						continue;
					
					llScore += Gamma.logGamma( ss[nodeIndex].Mxx[pE][fsE][ssE]);
					llScore -= Gamma.logGamma( mxx);
				}
			}
		}
		
		// llScore = llScore - ln|X|*Dim[X]/2
		if( dimensionPenalty) {
			double dimX = (node.getStatesNumber() - 1) * node.getStatesNumber() * node.getNumberParentsEntries();
			llScore -= Math.log( dataset.size()) * dimX / 2;
		}
		
		return llScore;
	}
	
	/**
	 * Calculate the conditional log-likelihood scoring.
	 * 
	 * @param model model to evaluate
	 * @param nodeIndex index of the node that we are evaluating
	 * @param dataset dataset used for the evaluation
	 * @param paramsLearningAlg algorithm used to learn the parameters
	 * @param dimensionPenalty true if the scoring of the individual has to give a penalty related to the dimension of the structure, false otherwise
	 * @param expApprox N number of iteration used to approximate the exponential
	 * @return conditional log-likelihood scoring
	 * @throws RuntimeException in case of some error during the calculation
	 */
	public static double conditionalLogLikelihoodScore(ICTClassifier<Double, CTDiscreteNode> model, int nodeIndex,
			Collection<ITrajectory<Double>> dataset,
			ILearningAlgorithm<Double, CTDiscreteNode> paramsLearningAlg,
			boolean dimensionPenalty, int expApprox) throws RuntimeException {
		
		if( dataset == null)
			throw new RuntimeException("Error: dataset didn't set");
		if( paramsLearningAlg.getStructure() != null)
			throw new RuntimeException("Error: parameter algorithm used in the scoring function for structural learning can not have structure setted");
		
		CTDiscreteNode node = model.getNode(nodeIndex);
		if( node.isStaticNode())
			throw new IllegalArgumentException("Error: this scoring function is not defined for static nodes");
		
		// If the node has not the class as parent
		// its contribute (score) is 0
		CTBNClassifier clModel = (CTBNClassifier) model;
		CTDiscreteNode classNode = clModel.getClassNode();
		if( node.getParentIndex( classNode.getName()) == null)
			return 0.0;
		
		// Calculate the sufficient statistics
		SufficientStatistics[] ss = paramsLearningAlg.learn(model, dataset).getSufficientStatistics();
		
		// Data initialization
		if( classNode.getParentsNumber() > 0) 
			throw new RuntimeException("Error: this version of the algorithm doesn't work with classes that have parents");
		// The previous bound can be relaxed only if
		// we are able to calculate the following
		// distribution taking care about the possible
		// parents values.
		// It can be complex if one of the predecessor
		// of the class node is a parent of successor
		// of the class node.
		double[] tmpcim = classNode.getCIM(0)[0];					// normalization factor for each class
		double[] norm = new double[tmpcim.length];
		for( int i = 0; i < norm.length; ++i)
			norm[i] = tmpcim[i];
		double score = 0.0;											// scoring
		double tmp;
		
		// Scoring calculation		
		for(int pE = 0; pE < node.getNumberParentsEntries(); ++pE) {
			
			node.setParentsEntry( pE);								// set the parents' values
			double[][] cim = node.getCIM(pE);						// get the cim
			int clStateIndex = classNode.getCurrentStateIndex();	// get the class state index
			
			tmp = 0.0;			
			for(int fsE = 0; fsE < node.getStatesNumber(); ++fsE) {
				
				tmp += ss[nodeIndex].Mx[pE][fsE] * Math.log(-cim[fsE][fsE]);
				tmp += cim[fsE][fsE] * ss[nodeIndex].Tx[pE][fsE];
				
				for(int ssE = 0; ssE < node.getStatesNumber(); ++ssE) {
					if( fsE == ssE)
						continue;
					
					tmp += ss[nodeIndex].Mxx[pE][fsE][ssE] * Math.log(cim[fsE][ssE]/(-cim[fsE][fsE]));
				}
			}
			score += tmp;
			norm[clStateIndex] += tmp;
		}
		// Use the maximum value as fast approximation
		// (i.e. log(e^x1+...+e^xn) ~= max{x1,...,xn}
		// when the real computation go to infinity
		// because of the values it does not add a
		// significative error
		double sumNorm = 0.0;
		double maxExp = Double.NEGATIVE_INFINITY;
		for( int clInd = 0; clInd < norm.length; ++clInd) {
			if( maxExp < norm[clInd])
				maxExp = norm[clInd];
			sumNorm += Math.exp(norm[clInd]);
		}
		if( Double.isInfinite(sumNorm))
			score -= maxExp;
		else
			score -= Math.log(sumNorm);

		// score = score - ln|X|*Dim[X]/2
		if( dimensionPenalty) {
			double dimX = (node.getStatesNumber() - 1) * node.getStatesNumber() * node.getNumberParentsEntries();
			score -= Math.log( dataset.size()) * dimX / 2;
		}
		
		return score;
	}
}

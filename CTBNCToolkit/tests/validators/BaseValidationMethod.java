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
package CTBNCToolkit.tests.validators;

import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.ILearningResults;
import CTBNCToolkit.IModel;
import CTBNCToolkit.INode;
import CTBNCToolkit.MultipleCTBNC;
import CTBNCToolkit.MultipleCTBNCLearningResults;
import CTBNCToolkit.NodeIndexing;
import CTBNCToolkit.SufficientStatistics;
import CTBNCToolkit.performances.IPerformances;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Abstract class for validation methods. 
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public abstract class BaseValidationMethod<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends IModel<TimeType, NodeType>,PType extends IPerformances> implements IValidationMethod<TimeType, NodeType, TM, PType> {

	private boolean verbose;
	private boolean printSS;
	private boolean clusterInSample;
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.tests.validators.IValidationMethod#setVerbose(boolean)
	 */
	@Override
	public void setVerbose(boolean verbose) {

		this.verbose = verbose;		
	}

	/* (non-Javadoc)
	 * @see CTBNToolkit.tests.validators.IValidationMethod#getVerbose()
	 */
	@Override
	public boolean getVerbose() {

		return this.verbose;
	}
	
	/**
	 * Allow (true) or disable (false) the
	 * in sample performances calculation for
	 * clustering algorithm.
	 * It is done using all the trajectories
	 * used in the training set.
	 * 
	 * @param flag flag to allow the in sample performances calculation (true) or disable it (false)
	 */
	public void setClusterInSampleFlag( boolean flag) {
		
		this.clusterInSample = flag;
	}
	
	/**
	 * True if the in sample performances
	 * calculation for clustering is enabled.
	 * False otherwise.
	 * 
	 * @return true if the in sample performances calculation for clustering is enabled, false otherwise.
	 */
	public boolean getClusterInSampleFlag() {
		
		return this.clusterInSample;
	}
	
	/**
	 * Allow (true) or disable (false) the
	 * sufficient statistics printing.
	 * The SS are printed only if the verbose
	 * printing is active.
	 * 
	 * @param flag flag to allow the sufficient statistics printing (true) or disable it (false)
	 */
	public void setPrintSSFlag( boolean flag) {
		
		this.printSS = flag;
	}
	
	/**
	 * True if the printing of the sufficient statistics
	 * is enabled.
	 * False otherwise.
	 * 
	 * @return true if the printing of sufficient statistics is enabled, false otherwise.
	 */
	public boolean getPrintingSSFlag() {
		
		return this.printSS;
	}
	
	
	/**
	 * Print the string only if the verbose
	 * mode is active.
	 * 
	 * @param str string to print
	 */
	protected void verbosePrint(String str) 
	{
		if( this.verbose)
			System.out.print(str);
	}
	

	/**
	 * Print the sufficient statistics.
	 * 
	 * @param learningResult learning result
	 * @param mdl learned model
	 */
	@SuppressWarnings("unchecked")
	protected void printSufficientStatistics(ILearningResults learningResult, @SuppressWarnings("rawtypes") ICTClassifier mdl) {
		
		if( !(this.getVerbose() && this.getPrintingSSFlag()))
			return;		
		
		if( MultipleCTBNCLearningResults.class.isAssignableFrom( learningResult.getClass()))
			printMultipleSufficientStatistics((MultipleCTBNCLearningResults) learningResult, (MultipleCTBNC<TimeType,NodeType>) mdl);
		
		SufficientStatistics[] ss = learningResult.getSufficientStatistics();
		NodeIndexing nodeIndexing = mdl.getNodeIndexing();
		
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < nodeIndexing.getNodesNumber(); ++i) {
			strBuilder.append(nodeIndexing.getName(i) + ":\n");
			strBuilder.append(ss[i].toString(mdl, i));
		}
		
		this.verbosePrint(strBuilder.toString());
	}

	
	/**
	 * Print the sufficient statistics for MultipleCTBNC
	 * models.
	 * 
	 * @param learningResults learning result
	 * @param mdl learned MultipleCTBNC model
	 */
	protected void printMultipleSufficientStatistics(MultipleCTBNCLearningResults learningResults,
			MultipleCTBNC<TimeType, NodeType> mdl) {
		
		// Print the sufficient statistics of each expert
		for( int i = 0; i < learningResults.getNumberOfModel(); ++i) {
			
			this.verbosePrint("Model expert for \"" + mdl.getClassName( i) + "\" class\n");
			this.printSufficientStatistics( learningResults.getResult( i), mdl.getModel( i));
			this.verbosePrint("\n\n");
		}
		
	}

}

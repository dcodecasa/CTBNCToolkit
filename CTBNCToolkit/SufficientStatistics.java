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
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that represent the sufficient statistics
 * for log-likelihood models.
 * 
 * Continuous times variables statistics:
 * Mxx[parentEntry][fistState][secondState] = number of jumps from firstState to secondState when the parents are in parentEntry condition
 * Tx[parentEntry][state] = amount of time in which the node is in state and the parents are in parentEntry condition
 * Mx[parentEntry][state] = number of jumps from state when the parents are in parentEntry condition (M[pE][s] = sum_s' M[pE][s][s'])
 * 
 * Static variables statistics:
 * Px[parentEntry][1][state] = number of occurrences over the states when the parent are in parentEntry condition
 * count[parentEntry] = number of instances in the dataset in which the parent are in parentEntry condition
 */
public class SufficientStatistics {
	
	// CT Learning data
	public double[][][] Mxx = null;
	public double[][] Tx;
	public double[][] Mx = null;
	// Static node learning data
	public double[][][] Px;
	public double[] counts;
	
	/**
	 * Base constructor.
	 * Set all statistics to null.
	 */
	public SufficientStatistics() {
		
		this.Mxx = null;
		this.Tx = null;
		this.Mx = null;
		this.Px = null;
		this.counts = null;
	}
	
	
	/**
	 * Generate the sufficient statistics
	 * for a node with the characteristics
	 * in input.
	 * 
	 * @param nPE number of parent entries (validity range [1, +oo))
	 * @param nS number of states (validity range [1, +oo))
	 * @param isStatic true if it is a static nodes, false otherwise
	 * @param pPrior prior for static nodes (validity range [0, +oo))
	 * @param mPrior transition prior for continuous time nodes (validity range [0, +oo))
	 * @param tPrior time prior for continuous time nodes (validity range [0, +oo))
	 * @throws IllegalArgumentException in case of illegal arguments
	 */
	public SufficientStatistics(int nPE, int nS, boolean isStatic, double pPrior, double mPrior, double tPrior) throws IllegalArgumentException {
		
		if( nPE < 1)
			throw new IllegalArgumentException("Error: parent entry number must be at least 1");
		if( nS < 1)
			throw new IllegalArgumentException("Error: the states number must be at least 1");
		if( pPrior < 0)
			throw new IllegalArgumentException("Error: static nodes prior must be a real positive number");
		if( mPrior < 0)
			throw new IllegalArgumentException("Error: dinamic nodes counts prior must be a real positive number");
		if( tPrior < 0)
			throw new IllegalArgumentException("Error: dinamic nodes time prior must be a real positive number");
		
		if( isStatic) {
			this.Mxx = null;
			this.Tx = null;
			this.Mx = null;
			
			this.Px = new double[nPE][1][nS];
			this.counts = new double[nPE];
			for(int pE = 0; pE < nPE; ++pE)	{		 	// parent entry
				this.counts[pE] = 0.0;
				for(int sE = 0; sE < nS; ++sE) {		// state entry
					this.Px[pE][0][sE] = pPrior;
					this.counts[pE] += pPrior;
				}
			}
		}else {
			this.Px = null;
			this.counts = null;
			
			this.Mxx = new double[nPE][nS][nS];
			this.Mx = new double[nPE][nS];
			this.Tx = new double[nPE][nS];
			for(int pE = 0; pE < nPE; ++pE)				// parent entry
				for(int fsE = 0; fsE < nS; ++fsE) { 	// first state entry
					this.Tx[pE][fsE] = tPrior;
					this.Mx[pE][fsE] = 0.0;
					for(int ssE = 0; ssE < nS; ++ssE)  	// second state entry
						if( fsE != ssE) {
							this.Mxx[pE][fsE][ssE] = mPrior;
							this.Mx[pE][fsE] += mPrior;
						}
				}
		}
	}
	
	
	@Override
	public SufficientStatistics clone() {
		
		SufficientStatistics newStats = new SufficientStatistics();
		if( !this.isSet())
			return newStats;
		
		if( this.Mxx == null) {
			newStats.Px = new double[this.Px.length][this.Px[0].length][this.Px[0][0].length];
			newStats.counts = new double[this.counts.length];
			
			for(int pE = 0; pE < this.Px.length; ++pE) {
				newStats.counts[pE] = this.counts[pE];
				for(int fS = 0; fS < this.Px[0].length; ++fS)
					for(int sS = 0; sS < this.Px[0][0].length; ++sS)
						newStats.Px[pE][fS][sS] = this.Px[pE][fS][sS];
			}			
		}else {
			newStats.Mxx = new double[this.Mxx.length][this.Mxx[0].length][this.Mxx[0][0].length];
			newStats.Mx = new double[this.Mx.length][this.Mx[0].length];
			newStats.Tx = new double[this.Tx.length][this.Tx[0].length];
			
			for(int pE = 0; pE < this.Mxx.length; ++pE)
				for(int fS = 0; fS < this.Mxx[0].length; ++fS) {
					newStats.Tx[pE][fS] = this.Tx[pE][fS];
					newStats.Mx[pE][fS] = this.Mx[pE][fS];
					for(int sS = 0; sS < this.Mxx[0][0].length; ++sS)
						newStats.Mxx[pE][fS][sS] = this.Mxx[pE][fS][sS];
				}	
		}
		
		return newStats;
	}
	
	
	/**
	 * Return true if the sufficient statistics
	 * are defined. False otherwise.
	 * 
	 * @return true if the sufficient statistics are define, otherwise false.
	 */
	public boolean isSet() {
		
		return ((this.Mxx != null && this.Mx != null && this.Tx != null) || (this.Px != null && this.counts != null));
	}
	
	
	/**
	 * Return the number of parent entries.
	 * 
	 * @return number of parent entries
	 * @throws RuntimeException if the sufficient statistics is not set
	 */
	public int parentEntriesNumber() throws RuntimeException {
		
		if( !this.isSet())
			throw new RuntimeException("Error: sufficient statistics didn't set");
		
		if( this.Mxx == null)
			return this.Px.length;
		else
			return this.Mxx.length;
	}
	
	/**
	 * Return the number of states
	 * 
	 * @return number of states
	 * @throws RuntimeException if the sufficient statistics is not set
	 */
	public int statesNumber() throws RuntimeException {
		
		if( !this.isSet())
			throw new RuntimeException("Error: sufficient statistics didn't set");
		
		if( this.Mxx == null)
			return this.Px[0][0].length;
		else
			return this.Mxx[0].length;
	}
	
	/**
	 * Return the type of the node.
	 * 
	 * @return true if it is a static node, false otherwise.
	 * @throws RuntimeException if the sufficient statistics is not set
	 */
	public boolean isStatic() throws RuntimeException {
		
		if( !this.isSet())
			throw new RuntimeException("Error: sufficient statistics didn't set");
		
		return this.Mxx == null;
	}
	
	
	/**
	 * Return the string representation
	 * of the sufficient statistics.
	 * 
	 * @param mdl model where the sufficient statistics are calculated
	 * @param iNode index of the node for who the sufficient statistics refere
	 */
	public<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>> String toString(TM mdl, int iNode) {
		
		NodeType node = mdl.getNode(iNode);
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(node.getName() + "|{");
		for( int iPa = 0; iPa < node.getParentsNumber(); ++iPa) {
			strBuilder.append(node.getParent(iPa).getName());
			if( iPa != node.getParentsNumber() - 1)
				strBuilder.append(",");
		}
		strBuilder.append("}:\n");
		
		if( this.Mxx == null) {			
			for(int pE = 0; pE < this.Px.length; ++pE) {
				strBuilder.append("pE=" + pE + "\n");
				
				strBuilder.append("count=" + this.counts[pE] + "\n");
				
				strBuilder.append("Px=" + "\n");
				for(int fS = 0; fS < this.Px[0].length; ++fS) {
					for(int sS = 0; sS < this.Px[0][0].length; ++sS)
						strBuilder.append("[" + this.Px[pE][fS][sS] + "]");
					strBuilder.append("\n");
				}
			}			
		}else {
			for(int pE = 0; pE < this.Mxx.length; ++pE) {
				strBuilder.append("pE=" + pE + "\n");
				
				strBuilder.append("Tx=" + "\n");
				for(int fS = 0; fS < this.Mxx[0].length; ++fS) {
					strBuilder.append("[" + this.Tx[pE][fS] + "]");
				}
				strBuilder.append("\n");
				
				strBuilder.append("Mxx=" + "\n");
				for(int fS = 0; fS < this.Mxx[0].length; ++fS) {
					for(int sS = 0; sS < this.Mxx[0][0].length; ++sS)
						strBuilder.append("[" + this.Mxx[pE][fS][sS] + "]");
					strBuilder.append("\n");
				}
				
				strBuilder.append("Mx=" + "\n");
				for(int fS = 0; fS < this.Mxx[0].length; ++fS) {
					strBuilder.append("[" + this.Mx[pE][fS] + "]");
				}
				strBuilder.append("\n");
			}
		}
		
		return strBuilder.toString();
	}
}

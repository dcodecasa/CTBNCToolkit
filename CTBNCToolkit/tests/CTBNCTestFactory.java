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
package CTBNCToolkit.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;

import CTBNCToolkit.*;
import CTBNCToolkit.performances.*;
import CTBNCToolkit.tests.validators.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Standard factory.
 * 
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <NodeType> type of node in the model
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public class CTBNCTestFactory<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,PType extends IPerformances> implements ITestFactory<TimeType,NodeType,TM,PType> {
	
	private IModelFactory<TM,TimeType,NodeType> modelFactory;
	private IValidationMethod<TimeType,NodeType,TM,PType> validationMethod;
	private ILearningAlgorithm<TimeType, NodeType> learnAlgo;
	private IClassifyAlgorithm<TimeType, NodeType> infAlgo;
	
	/**
	 * Constructor without the model factory.
	 * The dataset can not be generated automatically.
	 * 
	 * @param validationMethod validation method to use during the tests
	 * @param learnAlgo learning algorithm
	 * @param infAlgo inference algorithm
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public CTBNCTestFactory( IValidationMethod<TimeType,NodeType,TM,PType>  validationMethod,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			IClassifyAlgorithm<TimeType, NodeType> infAlgo) throws IllegalArgumentException {
		
		this(null, validationMethod, learnAlgo, infAlgo);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param modelFactory factory used to generate a model
	 * @param validationMethod validation method to use during the tests
	 * @param learnAlgo learning algorithm
	 * @param infAlgo inference algorithm
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public CTBNCTestFactory(IModelFactory<TM,TimeType,NodeType> modelFactory,
			IValidationMethod<TimeType,NodeType,TM,PType> validationMethod,
			ILearningAlgorithm<TimeType, NodeType> learnAlgo,
			IClassifyAlgorithm<TimeType, NodeType> infAlgo) throws IllegalArgumentException {
	
		if( validationMethod == null)
			throw new IllegalArgumentException("Error: null validatorFactory argument");
		if( learnAlgo == null)
			throw new IllegalArgumentException("Error: null learnAlgo argument");
		if( infAlgo == null)
			throw new IllegalArgumentException("Error: null infAlgo argument");
		
		this.modelFactory = modelFactory;
		this.validationMethod = validationMethod;
		this.learnAlgo = learnAlgo;
		this.infAlgo = infAlgo;
	}

	/* (non-Javadoc)
	 * @see test.ITestFactoty#newTest()
	 */
	@Override
	public GenericTestResults<TimeType,NodeType,TM,PType> newTest(String testName, int datasetDim) {
		
		if( this.modelFactory == null)
			throw new RuntimeException("Error: null modelFactory argument impossible generate a dataset");
		
		TM trueModel = modelFactory.newInstance();
		
		List<ITrajectory<TimeType>> dataset = new Vector<ITrajectory<TimeType>>(datasetDim);
		for(int i = 0; i < datasetDim; ++i)
			dataset.add( trueModel.generateTrajectory(this.modelFactory.getTrajectoryLength()));
		
		@SuppressWarnings("unchecked")
		PType performances = this.validationMethod.validate((TM) trueModel.clone(), this.learnAlgo, this.infAlgo, dataset);
		
		return new GenericTestResults<TimeType,NodeType,TM,PType>(testName, trueModel, dataset, performances);
	}

	/* (non-Javadoc)
	 * @see test.ITestFactoty#newTest(java.util.List)
	 */
	@Override
	public GenericTestResults<TimeType,NodeType,TM,PType> newTest(String testName, TM model, List<ITrajectory<TimeType>> dataset) {
		
		@SuppressWarnings("unchecked")
		PType performances = this.validationMethod.validate((TM) model.clone(), this.learnAlgo, this.infAlgo, dataset);
		
		return new GenericTestResults<TimeType,NodeType,TM,PType>(testName, null, dataset, performances);
	}
	
	
	/**
	 * Load a partition file and partitions
	 * the dataset in input.
	 * Partition files can be results files
	 * or text files where each test is divided
	 * by a line starting with the keyword
	 * "Test" and containing in the other
	 * lines the trajectories files for the
	 * tests.
	 * 
	 * @param filePath path of the partition file
	 * @param dataset dataset to divide
	 * @param filePrefixToRemove prefix to remove in the partition file (can be null if there is not)
	 * @param ext extension of the trajectories files
	 * @return partitioned input dataset
	 * @throws Exception in case of errors with the partitioning or the file loading
	 */
	public static List<List<IClassificationResult<Double>>> partitionResultDataset(String filePath, List<IClassificationResult<Double>> dataset, String filePrefixToRemove, String ext) throws Exception {
		
		List<List<IClassificationResult<Double>>> partitioning = new ArrayList<List<IClassificationResult<Double>>>();
		
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(new FileInputStream(filePath));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		//Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			
			// If line defines a new test
			if( strLine.startsWith("Test")) {
				partitioning.add(new ArrayList<IClassificationResult<Double>>());
				continue;
			}
			
			// Generate the string to seach in the loaded dataset
			int endIndex;
			if( (endIndex = strLine.indexOf(":")) != -1)	// cut the line to the ":" if presents
				strLine = strLine.substring( 0, endIndex);
			if( filePrefixToRemove != null && (!filePrefixToRemove.isEmpty()) && strLine.startsWith(filePrefixToRemove))	// remove the prefix if presents
				strLine = strLine.substring(filePrefixToRemove.length());
			if( !strLine.endsWith(ext))						// add the extension if not presents
				strLine += ext;
			
			// Partitions the dataset
			boolean added = false;
			for( int i = 0; i < dataset.size(); ++i) {
				if( dataset.get(i).getName().contains(strLine)) {
					partitioning.get(partitioning.size() - 1).add(dataset.get(i));
					added = true;
					break;
				}
			}
			if( !added) {
				br.close();
				throw new RuntimeException( "Error: " + strLine + " didn't found in the dataset's trajectories");
			}
		}
		in.close();
		br.close();
		
		return partitioning;
	}
	
	
	/**
	 * Load a partition file and partitions
	 * the dataset in input.
	 * Partition files can be results files
	 * or text files where each test is divided
	 * by a line starting with the keyword
	 * "Test" and containing in the other
	 * lines the trajectories files for the
	 * tests.
	 * 
	 * @param filePath path of the partition file
	 * @param dataset dataset to divide
	 * @param filePrefixToRemove prefix to remove in the partition file (can be null if there is not)
	 * @param ext extension of the trajectories files
	 * @return partitioned input dataset
	 * @throws Exception in case of errors with the partitioning or the file loading
	 */
	public static List<List<ITrajectory<Double>>> partitionDataset(String filePath, List<ITrajectory<Double>> dataset, String filePrefixToRemove, String ext) throws Exception {
		
		List<List<ITrajectory<Double>>> partitioning = new ArrayList<List<ITrajectory<Double>>>();
		
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(new FileInputStream(filePath));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		//Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			
			// If line defines a new test
			if( strLine.startsWith("Test")) {
				partitioning.add(new ArrayList<ITrajectory<Double>>());
				continue;
			}
			
			// Generate the string to seach in the loaded dataset
			int endIndex;
			if( (endIndex = strLine.indexOf(":")) != -1)	// cut the line to the ":" if presents
				strLine = strLine.substring( 0, endIndex);
			if( filePrefixToRemove != null && (!filePrefixToRemove.isEmpty()) && strLine.startsWith(filePrefixToRemove))	// remove the prefix if presents
				strLine = strLine.substring(filePrefixToRemove.length());
			if( !strLine.endsWith(ext))						// add the extension if not presents
				strLine += ext;
			
			// Partitions the dataset
			boolean added = false;
			for( int i = 0; i < dataset.size(); ++i) {
				if( dataset.get(i).getName().contains(strLine)) {
					partitioning.get(partitioning.size() - 1).add(dataset.get(i));
					added = true;
					break;
				}
			}
			if( !added) {
				br.close();
				throw new RuntimeException( "Error: " + strLine + " didn't found in the dataset's trajectories");
			}
		}
		in.close();
		br.close();
		
		return partitioning;
	}
	
	/**
	 * Load all the trajectory files with results in
	 * the path.
	 * The first column in the files must be the time.
	 * 
	 * @param nameIndexing name that identify the global node indexing
	 * @param dataset where add the new files (it must be instanced)
	 * @param path path from where load the trajectories
	 * @param extention of the file to load (* means each file)
	 * @param separator fields separator in the file
	 * @param classNodeName of the class column
	 * @param timeNodeName name of the time column
	 * @param trajectoryCounterName name of the column to use to cat consecutive trajectories (it can be null)
	 * @param validColumns set of the names of the column of the dataset to load (the actual data columns not the time column or other columns)
	 * @param resultsDataset dataset where add the loaded trajectories with classification results
	 * @return continuous time naive bayes model generated over the loaded dataset
	 * @throws IllegalArgumentException if case of illegal arguments
	 * @throws IOException in case of loading errors
	 */
	public static CTBNClassifier loadResultsDataset(String nameIndexing, String path, String ext, char separator, String classNodeName, String timeNodeName, String trajectoryCounterName, Set<String> validColumns, List<IClassificationResult<Double>> resultsDataset) throws IllegalArgumentException,IOException {
		
		List<ITrajectory<Double>> dataset = new ArrayList<ITrajectory<Double>>();
		CTBNClassifier nBModel = loadDataset(nameIndexing,  dataset, path, ext, separator, classNodeName, timeNodeName, trajectoryCounterName, validColumns, 1, 0);
		loadClassification(path, ext, separator, nBModel.getClassNode(), dataset, resultsDataset);
		
		return nBModel;
	}
	
	/**
	 * Load the results of the classification
	 * from files using the corresponding
	 * dataset of trajectories.
	 * 
	 * @param path path of the folder that contain the result dataset
	 * @param ext extension of the trajectories files
	 * @param separator fields separator in the file
	 * @param classNode class node
	 * @param dataset dataset of loaded trajectories
	 * @param resultsDataset dataset where insert the trajectories with the classification results
	 * @throws IOException in case of IO errors
	 */
	private static void loadClassification(
			String path, String ext, char separator, CTDiscreteNode classNode, List<ITrajectory<Double>> dataset, List<IClassificationResult<Double>> resultsDataset) throws IOException {
		
		// Get all the file in the directory
		File[] listOfFiles = (new File(path)).listFiles();
		ext = ext.toLowerCase();
		
		// For each file
		for(int f = 0; f < listOfFiles.length; ++f) {
			
			// Jumps all the file that have not the right extension
			if( (!ext.equals("*")) && (!listOfFiles[f].getName().toLowerCase().endsWith(ext)))
				continue;
			
			// Find the corresponding trajectory in the dataset
			ITrajectory<Double> trj = findTrajectory(dataset, listOfFiles[f].getName());
			if( trj == null)
				throw new IllegalArgumentException("Error: input dataset does not contain a trajectory with name \"" + listOfFiles[f].getName() + "\"");
			ClassificationResults<Double> res = new ClassificationResults<Double>(trj);
			
			// Load the file
			CSVReader reader = new CSVReader(new FileReader(listOfFiles[f].getAbsolutePath()), separator);
			// Read the file
		    String [] firstLine,nextLine;
		    if ((firstLine = reader.readNext()) == null)
		    	continue;
		    
		    // Generate the indexes of results column
		    Map<String, Integer> stateToIndex = new TreeMap<String,Integer>();
		    List<Integer> resultsColumns = new Vector<Integer>( classNode.getStatesNumber());
		    for( int s = 0; s < classNode.getStatesNumber(); ++s) {
		    	boolean found = false;
		    	for( int i = 0; i < firstLine.length && (!found); ++i)
		    		if( firstLine[i].equals(classNode.getStateName(s))) {
		    			if( stateToIndex.containsKey(firstLine[i])) {
		    				reader.close();
		    				throw new RuntimeException("Error: file " + listOfFiles[f].getAbsolutePath() + " contains two columns with " + firstLine[i] + " state name");
		    			}
		    			stateToIndex.put(firstLine[i], s);
		    			resultsColumns.add(i);
		    			found = true;
		    		}
		    	if( !found) {
		    		reader.close();
		    		throw new RuntimeException("Error: file " + listOfFiles[f].getAbsolutePath() + " does not contain the result for class " + classNode.getStateName(s));
		    	}
		    }
		    	
		    // For each line
		    int iTransition = 0;
	    	while ((nextLine = reader.readNext()) != null) {
	    		if( nextLine.length < 1)
	    			continue;
	    		
	    		// Get the probability distribution
	    		int iMax = 0;
	    		double[] p = new double[classNode.getStatesNumber()];
	    		for( int rC = 0; rC < resultsColumns.size(); ++rC) {
	    			p[rC] = Double.parseDouble(nextLine[resultsColumns.get(rC)]);
	    			if( p[rC] > p[iMax])
	    				iMax = rC;
	    		}	    			
	    		res.setProbability(iTransition, p, stateToIndex);
	    		
	    		// Set the classification with the class with the maximum probability
	    		res.setClassification(firstLine[resultsColumns.get(iMax)]);
	    		
	    		++iTransition;	// increase the line counter
			}
	    	reader.close();
	    	
	    	// Add the new trajectories results
	    	resultsDataset.add(res);
		}		
		
	}
	
	/**
	 * Find the trajectory with the target name
	 * in the input dataset.
	 * 
	 * @param dataset dataset where search the trajectory
	 * @param name target name
	 * @return trajectory
	 */
	private static ITrajectory<Double> findTrajectory(
			List<ITrajectory<Double>> dataset, String name) {

		for( int i = 0; i < dataset.size(); ++i)
			if( dataset.get(i).getName().equals( name))
				return dataset.get(i);
		
		return null;
	}

	
	/**
	 * Load all the trajectory files in the path.
	 * The first column in the files must be the time.
	 * 
	 * @param nameIndexing name that identify the global node indexing
	 * @param dataset where add the new files (it must be instanced)
	 * @param path path from where load the trajectories (it can be just a file)
	 * @param extention of the file to load (* means each file)
	 * @param separator fields separator in the file
	 * @param classNodeName name of the class column
	 * @param timeNodeName name of the time column
	 * @param trajectoryCounterName name of the column to use to cat consecutive trajectories (it can be null)
	 * @param validColumns set of the names of the column of the dataset to load (the actual data columns not the time column or other columns); if empty load all the columns
	 * @param timeFactor multiplication factor to the time in the dataset
	 * @param nClusters number of classes for the class node of the generated model (this parameter is used in case of clustering to specify the number of classes (clusters), if its value is less than 2 it is ignored)
	 * @return model generated from the dataset
	 * @throws RuntimeException in case of some error in the input files
	 * @throws IllegalArgumentException if case of illegal arguments
	 * @throws IOException in case of loading errors
	 */
	public static CTBNClassifier loadDataset(String nameIndexing, List<ITrajectory<Double>> dataset, String path, String ext, char separator, String classNodeName, String timeNodeName, String trajectoryCounterName, Set<String> validColumns, double timeFactor, int nClusters) throws IllegalArgumentException,IOException,RuntimeException {
		
		if( dataset == null)
			throw new IllegalArgumentException("Error: null dataset value. It must be instanced.");
		if( validColumns == null)
			throw new IllegalArgumentException("Error: null valid columns argument. It must be instanced.");
		
		Map<String,Set<String>> statesForNode = new TreeMap<String,Set<String>>();
		
		File[] listOfFiles;
		File input = new File(path);
		if( input.isDirectory()) {				// get all the file in the directory
			listOfFiles = (new File(path)).listFiles();
		} else if( input.isFile()) {
			listOfFiles = new File[1];			// select just the file
			listOfFiles[0] = input;
		} else {
			throw new IllegalArgumentException("Error: the input to load must be a file or a directory.");
		}
		ext = ext.toLowerCase();
		
		// For each file
		NodeIndexing nodeIndexing = null;
		for(int f = 0; f < listOfFiles.length; ++f) {
			
			// Jumps all the file that have not the right extension
			if( (!ext.equals("*")) && (!listOfFiles[f].getName().toLowerCase().endsWith(ext)))
				continue;
			
			// Load the file
			CSVReader reader = new CSVReader(new FileReader(listOfFiles[f].getAbsolutePath()), separator);
			// Read the file
		    String [] firstLine,nextLine;
		    List<Double> times = new Vector<Double>();
		    List<String[]> values = new Vector<String[]>();
		    // Index
		    int timeIdx = -1;
		    int trjCounterIdx = -1;
		    String counterState = null;
		    int trjCounter = 1;
		    
		    // Read the header and set the index of the state, time and, trajectories breaker column (if exist)
		    boolean setValidColumns = false;
		    if( validColumns.isEmpty()) {							// if empty, the valid columns are all the column except the class, the time and the trajectory separator columns
		    	setValidColumns = true;
		    }
		    if ((firstLine = reader.readNext()) == null)
		    	continue;
		    for(int i = 0; i < firstLine.length; ++i) {
		    	firstLine[i] = firstLine[i].trim();
		    	if( firstLine[i].equals(timeNodeName)) {													// time column
		    		if( timeIdx != -1) {
		    			reader.close();
		    			throw new RuntimeException("Error two or more time columns named " + timeNodeName);
		    		} else
		    			timeIdx = i;
		    	}else if( trajectoryCounterName != null && firstLine[i].equals(trajectoryCounterName)) {	// trajectory separator column
		    		if( trjCounterIdx != -1) {
		    			reader.close();
		    			throw new RuntimeException("Error two or more trajectory counter columns named " + trajectoryCounterName);
		    		} else
		    			trjCounterIdx = i;
		    	}else if( setValidColumns) {																// all the other columns
		    		validColumns.add(firstLine[i]);
		    	}
		    }
		    setValidColumns = false;
		    validColumns.add(classNodeName);																// the class must be alwasys in the valid columns
		    if( timeIdx == -1) {
		    	reader.close();
		    	throw new RuntimeException("Error time column named " + timeNodeName + " not found");
		    }
		    if( trajectoryCounterName != null && trjCounterIdx == -1) {
		    	reader.close();
		    	throw new RuntimeException("Error trajectory counter column named " + trajectoryCounterName + " not found");
		    }
		    if( nodeIndexing == null)
		    	nodeIndexing = NodeIndexing.getNodeIndexing(nameIndexing, firstLine, classNodeName, validColumns);
		    
		    // Read the file
	    	while ((nextLine = reader.readNext()) != null) {
	    		if( nextLine.length < 1)
	    			continue;
	    		if( nextLine.length != firstLine.length) {
	    			reader.close();
	    			throw new RuntimeException("Data row length does not correspond with header length");
	    		}

	    		// Trajectories separation
	    		if( trjCounterIdx != -1) {
	    			if( counterState == null) {
	    				//Start a new trajectory
	    				counterState = nextLine[trjCounterIdx];
	    			} else if( !nextLine[trjCounterIdx].equals( counterState)) {
	    				// Generate the finished trajectory
	    				CTTrajectory<Double> trj = new CTTrajectory<Double>(nodeIndexing, times, values);
	    		    	trj.setName(listOfFiles[f].getName() + "_" + trjCounter);
	    		    	dataset.add(trj);
	    		    	
	    				// Start the new trajectory
	    		    	times = new Vector<Double>();
	    				values = new Vector<String[]>();
	    				++trjCounter;
	    				counterState = nextLine[trjCounterIdx];
	    			}
	    		}
	    		
	    		// Read the row and generate its states
			    String[] rowValues = new String[nodeIndexing.getNodesNumber()];
			    for(int i  = 0; i < rowValues.length; ++i)
			    	rowValues[i] = null;
			    
			    Set<String> states;
			    for(int i = 0; i < firstLine.length; ++i) {
			    	if( !validColumns.contains(firstLine[i]))	// if it is not a column, ignore it
			    		continue;
			    	
			    	nextLine[i] = nextLine[i].trim();
			    	rowValues[ nodeIndexing.getIndex( firstLine[i])] = nextLine[i];
			    	if( statesForNode.containsKey(firstLine[i]))
			    		states = statesForNode.get(firstLine[i]);
			    	else
			    		states = new TreeSet<String>();
		    		states.add(nextLine[i]);
		    		statesForNode.put(firstLine[i], states);
			    }
			    values.add(rowValues);
			    times.add(Double.parseDouble(nextLine[timeIdx])*timeFactor);
			}
	    	reader.close();
	    	
	    	CTTrajectory<Double> trj = new CTTrajectory<Double>(nodeIndexing, times, values);
	    	if( trjCounterIdx == -1)
	    		trj.setName(listOfFiles[f].getName());
	    	else
	    		trj.setName(listOfFiles[f].getName() + "_" + trjCounter);
	    	dataset.add(trj);
		}
		
		// Set the number of clusters for the class node
		if( nClusters >= 2) {
			Set<String> clusters = new TreeSet<String>();
			for(int i = 1; i <= nClusters; ++i)
				clusters.add("" + i);
			statesForNode.put(classNodeName, clusters);
		}			

		return CTBNClassifierFactory.getNaiveBayesFromDataset(nodeIndexing, statesForNode);
		
	}
	
	/**
	 * Generate a new dataset that is a random
	 * permutation of the dataset in input.
	 * 
	 * @param dataset dataset to mix
	 * @return a random permutation of the input dataset
	 */
	static public List<ITrajectory<Double>> permuteDataset(List<ITrajectory<Double>> dataset) {
		
		ITrajectory<Double> trjTemp = null;
		for( int i = 0; i < dataset.size(); ++i) {
			int rIndex = (int) (Math.random() * dataset.size());
			trjTemp = dataset.get(rIndex);
			dataset.set(rIndex, dataset.get(i));
			dataset.set(i, trjTemp);
		}
		
		return dataset;		
	}
	
	/**
	 * Choose only a percentage of the trajectories
	 * in the dataset. These trajectory are cut of
	 * the same percentage length.
	 * 
	 * Ex: applying the function to 1000 trajectories
	 * long 10 seconds with 0.5 of percentage, the
	 * method will choose randomly 500 trajectories
	 * cutting their length to 5 seconds.
	 * 
	 * 
	 * @param dataset dataset to cut
	 * @param cutPercentage percentage of cutting to apply
	 * @return new dataset generated
	 * @throws IllegalArgumentException in case of illegal parameters
	 */
	static public List<ITrajectory<Double>> cutDataset(List<ITrajectory<Double>> dataset, double cutPercentage) throws IllegalArgumentException {
		
		if( dataset == null)
			throw new IllegalArgumentException("Error: null dataset");
		if( dataset.size() == 0)
			throw new IllegalArgumentException("Error: empty dataset");
		if( cutPercentage <= 0 || cutPercentage > 1)
			throw new IllegalArgumentException("Error: the cutting percentage must be in (0,1]");
		
		// Reduce the number of trajectories
		int nTrj = (int) Math.round( dataset.size() * cutPercentage);
		for(int i = dataset.size() - nTrj; i > 0; --i)
			dataset.remove( (int) (dataset.size() * Math.random()));
		
		// Cut the length of the trajectories
		for(int i  = 0; i < dataset.size(); ++i)
			dataset.set(i, CTTrajectory.cutTrajectory( (CTTrajectory<Double>)dataset.get(i), cutPercentage));
			
		return dataset;
	}
	
}

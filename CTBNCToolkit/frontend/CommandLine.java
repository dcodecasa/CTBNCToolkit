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
package CTBNCToolkit.frontend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import CTBNCToolkit.BinaryDecider;
import CTBNCToolkit.CTBNCClassifyAlgorithm;
import CTBNCToolkit.CTBNCLocalStructuralLearning;
import CTBNCToolkit.CTBNCParameterLLAlgorithm;
import CTBNCToolkit.CTBNClassifier;
import CTBNCToolkit.CTDiscreteNode;
import CTBNCToolkit.ICTClassifier;
import CTBNCToolkit.IClassifyDecider;
import CTBNCToolkit.ILearningAlgorithm;
import CTBNCToolkit.ITrajectory;
import CTBNCToolkit.MultipleCTBNC;
import CTBNCToolkit.MultipleCTBNCLearningAlgorithm;
import CTBNCToolkit.clustering.CTBNClusteringParametersLLAlgorithm;
import CTBNCToolkit.clustering.StandardStopCriterion;
import CTBNCToolkit.optimization.CLLHillClimbingFactory;
import CTBNCToolkit.optimization.CTBNCHillClimbingIndividual;
import CTBNCToolkit.optimization.ICTBNCHillClimbingFactory;
import CTBNCToolkit.optimization.LLHillClimbingFactory;
import CTBNCToolkit.performances.ClassificationStandardPerformances;
import CTBNCToolkit.performances.ClassificationStandardPerformancesFactory;
import CTBNCToolkit.performances.ClusteringExternalPerformances;
import CTBNCToolkit.performances.ClusteringExternalPerformancesFactory;
import CTBNCToolkit.performances.IPerformances;
import CTBNCToolkit.performances.MicroMacroClassificationPerformances;
import CTBNCToolkit.performances.MicroMacroClassificationPerformancesFactory;
import CTBNCToolkit.tests.CTBNCTestFactory;
import CTBNCToolkit.tests.GenericTestResults;
import CTBNCToolkit.tests.ITestFactory;
import CTBNCToolkit.tests.ITestResults;
import CTBNCToolkit.tests.validators.ClusteringInSample;
import CTBNCToolkit.tests.validators.CrossValidation;
import CTBNCToolkit.tests.validators.HoldOut;
import CTBNCToolkit.tests.validators.IValidationMethod;


/**
 * 
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 *  Implement the front-end for command line processing.
 */
public class CommandLine {

	/*
	 * Command line modifier list.
	 * All the elements have the following structure:
	 *  - command line modifier
	 *  - method name to call to manage the modifier
	 *  - description of the modifier
	 *  - enable modifier ("enabled" to enable the modifier)
	 *
	 * To add a modifier is enough create a public method to manage the modifier.
	 * In the meanwhile it is necessary add an element in this structure.
	 * 
	 * In the command line you have to specify the arguments in the following
	 * form:
	 *  - "--modifier": for the modifiers without arguments
	 *  - "--modifier=arg1,arg2": for the modifiers with one or more arguments
	 *///?rewrite the help with the web sites links
	private String[][] modifiersList = {
		{"help",			"printHelp",			"print the CTBNCToolkit help",																				"enabled"},	// help
		{"CTBNC",			"setCTBNCModels",		"specify the list of models to use, each model must be one of the following:\n" +
													"\t. CTNB:\t\tContinuous Time Naive Bayes;\n" +
													"\t. ACTNBk-f:\tAugmented Continuous Time Naive Bayes;\n" +
													"\t\t\tk is the maximum number of parents (>=2), f is the scoring function (LL or CLL);\n" +
													"\t. CTBNCk-f:\tContinuous Time Bayesian Network Classifier;\n" +
													"\t\t\tk is the maximum number of parents (>=1), f is the scoring function (LL or CLL);\n" +
													"\tLL stands for log-likelihood scoring function\n" +
													"\tCLL stands for conditional log-likelihood scoring function\n" +
													"\tafter each model is possible add the following parameters related to the previous inserted\n" +
													"\tmodel:\n" +
													"\t. Mk: k are the imaginary counts related to the variables number of transitions\n" +
													"\t\t\t(default value: 1.0)\n" +
													"\t. Tk: k is the imaginary amount of time spent in a state of a variable\n" +
													"\t\t\t(default value: 0.005)\n" +
													"\t. Pk: are the imaginary counts related to the class occurrences\n" +
													"\t\t\t(default value: 1.0)\n" +
													"\t. penalty: indicate to add the dimension penalty during the structural learning process\n" +
													"\t\t\tif omitted the penalty is disabled, for CTNB the flag is ignored\n" +
													"\n" +
													"\texamples:\n" +
													"\t--CTBNC=CTNB,M0.1,ACTNB2-CLL,T0.00001,CTBNC2-LL,penalty\n" +
													"\t--CTBNC=CTBNC4-CLL,M0.5,T0.001,P0.5,penalty",															"enabled"},	// CTBNC
		//?{"model",			"setModels",			"specify the models file path to load and test (THIS MODIFIER IS CURRENTLY NOT IMPLEMENTED)",			"enabled"},	// model
		{"validation",		"setValidationMethod",	"specify the validation method to use between the following:\n" +
													"\t--validation=CV,k  \tk-folds cross validation is used (default value is 10)\n" +
													"\t--validation=HO,0.6\thold out is used (60% training set, 40% test set; default value is 0.7)",			"enabled"},	// validation
		{"clustering",		"setClustering",		"specify that the tests are clustering tests and not classification tests (only external performances\n" +
													"\tare calculated, so class column is required)\n" +
													"\tPossible parameters are:\n" +
													"\t. soft/hard:	\tto anable soft or hard clustering (default is soft clustering)\n" +
													"\t. k:		\tmaximum number of iteration (default is 10, range is {2,...,+oo}\n" +
													"\t. r:		\tpercentage of trajectories; if less trajectories changes class the clustering algorithm\n" +
													"\t		\tinterrupted (default is 0.01, range is [0,1]\n" +
													"\t. Cn:	\tn is the number of clusters (default is the number of classes in the labeled data set)\n" +
													"\texamples:\n" +
													"\t--clustering=soft,0.1,15,C2\n" +
													"\t--clustering=0.03\n" +
													"\t--clustering=6,0.05,hard,C4\n",																				"enabled"},	// clustering
		{"1vs1",			"setModelToClass",		"a model is learned to discriminate each class, the classification relies on experts votes",				"enabled"},	// 1vs1
		{"bThreshold",		"setBinaryDecider",		"specify the probability threshold to use in the classification of binary class problem (default value: 0.5)\n" +
													"\texample:\n" +
													"\t--bThreshold=0.4 indicates that a trajectory is classified in the first class only if its\n" +
													"\tprobability to be in that class is bigger or equal to 0.4.\n" +
													"\tClass order is given by alphabetical order (\"A\"<\"B\")",												"enabled"},	// bThreshold
		{"testName",		"setTestName",			"specify a name for the current test (default value \"yyMMddHHmm_Test\";",									"enabled"},	// testName
		{"ext",				"setFileExt",			"specify the extension of the files to load in the dataset directory (default value: \".csv\")",			"enabled"},	// ext
		{"sep",				"setFileSeparator",		"specify column separator of the files to load (default value: ',')",										"enabled"},	// sep
		{"className",		"setClassColumnName",	"specify the class column name (default value: \"class\")",													"enabled"},	// className
		{"timeName",		"setTimeColumnName",	"specify the time column name (default value: \"t\")",														"enabled"},	// timeName
		{"trjSeparator",	"setTrjSeparator",		"specify the name of the column to use as trajectories separator:\n" +
													"\twhen the column change value the previous trajectory is considered finished and a new\n" +
													"\tone is started. This modifier must be used if the files contain multiple trajectories",					"enabled"},	// trjSeparator
		{"validColumns",	"setValidColumns",		"specify all the column that represents a variable in the models\n" +
													"\t(default values are all the columns except the time column and the trajectory separation column\n" +
													"\tif enabled)",																							"enabled"},	// validColumns
		{"cvPartitions",	"setCVPartitions",		"specify the file path of the cross validation partitioning to use in the test",							"enabled"},	// cvPartitions
		{"cvPrefix",		"setCVPrefix",			"specify a prefix to remove from the names of the trajectories in the partitioning file",					"enabled"},	// cvPrefix
		{"cutPercentage",	"setCutPercentage",		"specify the percentage of the trajectories and trajectories length to use in the test (default value: 1.0)\n" +
													"\tthis modifier have to be used if you want to test the models over a smaller and randomly chosen part\n" +
													"\t of the dataset",																						"enabled"},	// cutPercentage
		{"timeFactor",		"setTimeFactor",		"specify the time factor to use to scale the time column (default value: 1.0)",								"enabled"},	// timeFactor
		{"training",		"setTrainingSet",		"specify training set to use to lean the models",															"enabled"},	// training
		//?{"testset",			"setTestSet",			"TODO",			"enabled"},	// testset
		{"rPath",			"setResultsPath",		"specify the directory where generate the results (default path is the DataPath)",							"enabled"},	// rPath
		{"confidence",		"setConfidence",		"specify the confidence level to use in the performances (default is 90%).\n" +
													"\tCurrently the admissible values are: 99.9%, 99.8%, 99%, 98%, 95%, 90%, 80%\n" +
													"\texamples:\n" +
													"\t--confidence=99%\n" +
													"\t--confidence=80%",																						"enabled"},	// confidence
		{"noprob",			"disableProbabilities",	"disable the class probabilities calculation along the trajectories, it is enabled only for the final result\n" +
													"\tif no specified probabilities calculation is enabled",													"enabled"},	// noprob
		{"v",				"setVerbose",			"enable the verbose comments",																				"enabled"}	// verbose
	};

	/*
 	 * Mapping to lunch the methods to manage the command line modifiers.
 	 * The first map is for the modifier without parameters. The second
 	 * for the modifier with parameters.
 	 */
	private Map<String,Method> validModifiers;
	private Map<String,Method> validModifiersWithArgs;
	
	/**
	 * Constructor.
	 * Manage the command line arguments to set the program execution.
	 *
	 * @param args list of modifiers
	 * @param filenames filenames that the program has to use
	 */
	public CommandLine( String[] args, String[] filenames) {
		
		// Initialization
		initModifiers();							// initialize the modifiers
		 
		// Build the map between each modifier and its arguments
		Map<String,LinkedList<String>> parameters = new HashMap<String,LinkedList<String>>();
		for (int i = 0; i < args.length; i++) {
			LinkedList<String> modParams = new LinkedList<String>();
			String param = breakString( args[i], modParams);			// break the command line argument in modifier and its arguments
			parameters.put( param, modParams);
		}
		
		// For each modifier (parameters map) lunch its managing method
		String key = "";
		for( Iterator<String> is = parameters.keySet().iterator(); is.hasNext(); ) {
			
			key = is.next();
			try {
				LinkedList<String> argumentList = parameters.get(key);					
				
				// Checking on the not parametric method
				if( argumentList.isEmpty()) {
					Method m = validModifiers.get(key);
					if (m != null) {
						m.invoke(this, new Object[0]);
						continue;
					}
				}
				// Checking on the parametric methods
				Method m = validModifiersWithArgs.get(key);
				if (m == null) {
					System.err.println("Invalid modifier: " + key);
					System.exit(1);
				}
				m.invoke(this, argumentList);
				
			} catch (Exception e) {
				System.err.println("Unable to invoke method for modifier " + key + ": " + e);
				System.err.println("StackTrace:\n");
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		
		
		// Execute the program
		if( filenames.length != 1) {
			if( filenames.length == 0) {
				System.err.println("Error: one and only one filename or path argument is required. No filename inserted. For more information type --help.");
			} else {
				StringBuilder strBuilder = new StringBuilder();
				for(int i = 0; i < filenames.length; ++i) {
					strBuilder.append( filenames[i]);
					if( i != filenames.length - 1)
						strBuilder.append( ", ");
				}
				System.err.println("Error: one and only one filename or path argument is required. Filenames added: " + strBuilder.toString() + ".For more information type --help.");
			}
			System.exit(1);
		}
		
		// Set the result path
		if( this.resultsPath == null) {
			File in = new File(filenames[0]);
			if( (in.isFile()))
				this.resultsPath = in.getParent() + this.S + testName + this.S;
			else
				this.resultsPath = filenames[0] + this.S + testName + this.S;
		}
		if( !checkSetModifiers())
			System.exit(1);
		
		// Print all the parameters
		printParameters( args, filenames);
		
		// Dataset loading and management 
		List<ITrajectory<Double>> trainingSet = new LinkedList<ITrajectory<Double>>();
		List<ITrajectory<Double>> testSet = new LinkedList<ITrajectory<Double>>();
		ICTClassifier<Double,CTDiscreteNode> baseNBModel;
		CTBNClassifier nbModel = loadDatasets(filenames[0], trainingSet, testSet);
		if( !this.m1vs1c)
			baseNBModel = nbModel;
		else
			baseNBModel = new MultipleCTBNC<Double,CTDiscreteNode> ( "mult_" + nbModel.getName(), (ICTClassifier<Double,CTDiscreteNode>) nbModel);
		
		// Load the learning algorithms to test
		List<ILearningAlgorithm<Double, CTDiscreteNode>> learningAlgorithms = loadLearningAlgorithms( baseNBModel);
		
		// Load the inference (classification) algorithm		
		CTBNCClassifyAlgorithm classificationAlg = loadInferenceAlgorithm();
		
		// Execute the tests
		if( this.cvValidation != null) {				// CROSS VALIDATION
			this.verbosePrint("Cross validation (" + this.cvValidation.getKFolds() + " folds)\n");	
			this.cvValidation.setVerbose(this.verbose);
			
			// Generate the factories to execute the tests
			List<ITestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,MicroMacroClassificationPerformances<Double,ClassificationStandardPerformances<Double>>>> testFactories =
					generateTestFactories(learningAlgorithms, classificationAlg, this.cvValidation);
	
			// Execute tests
			List<GenericTestResults<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,MicroMacroClassificationPerformances<Double,ClassificationStandardPerformances<Double>>>> resultsList =
					executeTests( testFactories, testSet, baseNBModel);
			
			// Comparison tests
			comparisonTest(this.resultsPath + S + "methodsComparison.csv", resultsList);

		}else if( this.hoValidation != null) {			// HOLD OUT
			this.verbosePrint("Hold out\n");
			this.hoValidation.setVerbose(this.verbose);
			
			// Generate the factories to execute the tests
			List<ITestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClassificationStandardPerformances<Double>>> testFactories =
					generateTestFactories(learningAlgorithms, classificationAlg, this.hoValidation);

			// Execute tests
			List<GenericTestResults<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClassificationStandardPerformances<Double>>> resultsList =
					executeTests( testFactories, testSet, baseNBModel);
			
			// Comparison tests
			comparisonTest(this.resultsPath + S + "methodsComparison.csv", resultsList);

		}else if( this.clusteringValidation != null) {	// CLUSTERING (in sample)
			this.verbosePrint("Clustering (in sample)\n");
			this.clusteringValidation.setVerbose(this.verbose);
			
			// Generate the factories to execute the tests
			List<ITestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClusteringExternalPerformances<Double>>> testFactories =
					generateTestFactories(learningAlgorithms, classificationAlg, this.clusteringValidation);
			
			// Execute tests
			executeTests( testFactories, testSet, baseNBModel);
			
		}else {
			System.err.println("Code bug: no validation method is enable");
			System.exit(1);
		}
		
	}
	private String S = File.separatorChar + "";			// file separator for the current file system
	
	
	/**
	 * @param testFactories
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private<PType extends IPerformances> List<GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>> executeTests(
			List<ITestFactory<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>> testFactories,
			List<ITrajectory<Double>> dataset,
			ICTClassifier<Double,CTDiscreteNode> baseNBModel) {

		this.verbosePrint("Execute tests:\n");
		
		// Inizialization
		Set<String> classStates = new TreeSet<String>();
		for( int i = 0; i < baseNBModel.getClassNode().getStatesNumber(); ++i)
			classStates.add( baseNBModel.getClassNode().getStateName(i));
		
		List<GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>> resultsList =
				new Vector<GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>>(this.modelToTest.size());
		
		// Tests
		for( int i = 0; i < this.modelToTest.size(); ++i) {
			
			String[] modelData = this.modelToTest.get(i);
			String modelName = "M" + i + "_" + modelData[0] + (modelData.length > 5 ? modelData[5] + "-" + modelData[6] : "");
			this.verbosePrint("\t. model " + modelName + "\n");
			
			// Test execution
			this.verbosePrint("\t. test running\n");
			ITestResults<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType> result =
				testFactories.get(i).newTest(modelName, baseNBModel, dataset);
			resultsList.add( (GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>) result);
			
			// Result printing
			this.verbosePrint("\t. result printing\n");
			try {
				if( this.cvValidation != null)
					printTestResults(this.resultsPath, (GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, MicroMacroClassificationPerformances<Double, ClassificationStandardPerformances<Double>>>) result,
							classStates, baseNBModel.getClassNode().getName(), this.confidenceLevel, dataset.size(), this.testName, this.cvValidation.getKFolds());
				else if( this.hoValidation != null)
					printTestResults(this.resultsPath, (GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, ClassificationStandardPerformances<Double>>) result,
							classStates, baseNBModel.getClassNode().getName(), this.confidenceLevel, dataset.size(), this.testName);
				else if( this.clusteringValidation != null)
					printTestResults(this.resultsPath, (GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, ClusteringExternalPerformances<Double>>) result,
							classStates, baseNBModel.getClassNode().getName(), dataset.size(), this.testName, this.testType.equals("hard"));
			}catch(Exception e) {
				System.err.println("Error during the result printing for model " + modelName + ": " + e);
				e.printStackTrace();
				System.exit(1);
			}
		}

		this.verbosePrint("Execute tests (END)\n");
		
		return resultsList;
	}
	
	
	/**
	 * Print the comparison matrix between
	 * accuracy values of all the tests.
	 * The matrix is a statistical test to
	 * proof when an algorithm worked better
	 * then another.
	 * 
	 * @param filePath path of the file where print the comparison matrix
	 * @param resultsList list of results to compare
	 */
	private<PType extends IPerformances> void comparisonTest(
			String filePath,
			List<GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>> resultsList) {

		this.verbosePrint("Comparison tests:\n");

		try {
			// Metrics file
		  	FileWriter outTable;
			File fileTable = new File(filePath);
			if( !fileTable.exists())
				fileTable.createNewFile();
			outTable  = new FileWriter(fileTable);
			outTable.write( GenericTestResults.<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType>compareMethods(resultsList));
			outTable.close();
			
		} catch (IOException e) {
			System.err.println("Error in comparison tests: " + e);
			System.exit(1);
		}
		
		this.verbosePrint("Comparison tests (END)\n");
	}
	
	
	/**
	 * Write on the File System the results
	 * of the cross validation test.
	 * 
	 * @param path base path where write the results
	 * @param result the results of the test to write
	 * @param classStates set of class states
	 * @param classNodeName name of the class node
	 * @param datasetDim dimension of the dataset
	 * @param testName name of the test
	 * @param hardClustering true to indicate a test made with hard clustering algorithm, false to indicate a soft clustering algorithm
	 * @throws Exception in case of error
	 */
	protected void printTestResults(
			String path, 
			GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, ClusteringExternalPerformances<Double>> result, 
			Set<String> classStates, String classNodeName, int datasetDim, String testName, boolean hardClustering) throws Exception {
		
		System.out.println( result.getModelName() + " results printing");
		
		// Directory creation
		String basePath;
		File folder = new File(path);
		if( !folder.exists())
			folder.mkdir();
		if( hardClustering)
			basePath = path + "HardClustering" + S;
		else
			basePath = path + "SoftClustering" + S;
		folder = new File(basePath);
		if( !folder.exists())
			folder.mkdir();
		basePath += result.getModelName() + S;
		folder = new File(basePath);
		if( !folder.exists())
			folder.mkdir();
		
		// Readme
	  	FileWriter outMetrics;
		File fileMetrics = new File(basePath + "readme.csv");
		if( !fileMetrics.exists())
			fileMetrics.createNewFile();
	    outMetrics  = new FileWriter(fileMetrics);
	    if( hardClustering)
	    	outMetrics.write( "Dataset dim;" + datasetDim + ";\nTest name;" + testName + ";\n#folds;" + 1 + ";\nClustering;hard;\n");
	    else
	    	outMetrics.write( "Dataset dim;" + datasetDim + ";\nTest name;" + testName + ";\n#folds;" + 1 + ";\nClustering;soft;\n");
		outMetrics.close();
		
		
		// In sample performances
		if( this.nClusters == 0) {
			fileMetrics = new File(basePath + "performances.csv");
			if( !fileMetrics.exists())
				fileMetrics.createNewFile();
		    outMetrics  = new FileWriter(fileMetrics);
			outMetrics.write( result.getPerformances().toString());
			outMetrics.close();
		}
		
		
	  	// Dataset classification printing
		File file;
		FileWriter out;
		file = new File(basePath + result.getModelName() + "-results.txt");
		if( !file.exists())
			file.createNewFile();
	  	out = new FileWriter(file);
	  	out.write(result.resultsToString(classNodeName));
	  	out.close();
			
	  	
		// Model printing
		if(result.getPerformances().getLearnedModel() != null) {
		  	File fileModel = new File(basePath + "model.ctbn");
			if( !fileModel.exists())
				fileModel.createNewFile();	
			FileWriter outModel  = new FileWriter(fileModel);
			outModel.write(result.getPerformances().getLearnedModel().toString());
			outModel.close();
		}
		
	}
	
	
	/**
	 * Write on the File System the results
	 * of the Hold Out test.
	 * 
	 * @param path base path where write the results
	 * @param result the results of the test to write
	 * @param set of class states
	 * @param classNodeName name of the class node
	 * @param confidenceLevel to use in aggregate metrics
	 * @param datasetDim dimension of the dataset
	 * @param testName name of the test
	 * @param kFolds number of folds in the cross validation
	 * @throws Exception in case of error
	 */
	protected void printTestResults(
			String path, 
			GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, ClassificationStandardPerformances<Double>> result, 
			Set<String> classStates, String classNodeName, String confidenceLevel, int datasetDim, String testName) throws Exception {
		
		System.out.println( result.getModelName() + " results printing");
		
		// Metrics file
		File folder = new File(path + S );
		if( !folder.exists())
			folder.mkdir();
	  	FileWriter outMetrics;
		File fileMetrics = new File(path + S + "metrics.csv");
		if( !fileMetrics.exists()) {
			fileMetrics.createNewFile();
			outMetrics  = new FileWriter(fileMetrics, true);
			outMetrics.write( GenericTestResults.metricsHeader(classStates) + "\n");
		} else
			outMetrics  = new FileWriter(fileMetrics, true);
		String metricsCSV = result.metricsToString(classStates, confidenceLevel, datasetDim, testName, 1) + "\n";
		outMetrics.write( metricsCSV);
		outMetrics.close();
		
		
	  	// Dataset classification printing
		File file;
		FileWriter out;
		file = new File(path + S + result.getModelName() + "-results.txt");
		if( !file.exists())
			file.createNewFile();
	  	out = new FileWriter(file);
	  	out.write(result.resultsToString(classNodeName));
	  	out.close();
	  	
	  	
	  	// Model printing
		if( result.getPerformances().getLearnedModel() != null) {
		  	File fileModel = new File(path + S + result.getModelName() + ".ctbn");
			if( !fileModel.exists())
				fileModel.createNewFile();	
			FileWriter outModel  = new FileWriter(fileModel);
			outModel.write(result.getPerformances().getLearnedModel().toString());
			outModel.close();
		}
		
		
		// ROC and Precision-Recall curve
		File fileCurve;
		FileWriter outCurve;
		(new File(path + S + result.getModelName() + S + "ROCs" + S)).mkdirs();
		(new File(path + S + result.getModelName() + S + "Precision-Recall" + S)).mkdirs();
	  	(new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S)).mkdirs();
	  	Iterator<String> iterClasses = classStates.iterator();
	  	for(int i = 1; iterClasses.hasNext(); ++i) {
	  		String classState = iterClasses.next();
	  		
			// ROC
			fileCurve = new File(path + S + result.getModelName() + S + "ROCs" + S + "ROC-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printROC(result.getPerformances(), classState));
			outCurve.close();
			
			// PR curve
			fileCurve = new File(path + S + result.getModelName() + S + "Precision-Recall" + S + "PRC-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printPRCurve(result.getPerformances(), classState));
			outCurve.close();
			
			// Cumulative Response
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "CumulativeResp-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printCumulativeResponse(result.getPerformances(), classState));
			outCurve.close();
			
			// Lift Chart
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "LiftChart-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printLiftChart(result.getPerformances(), classState));
			outCurve.close();
		}
		
	}
	
	
	/**
	 * Write on the File System the results
	 * of the clustering test.
	 * 
	 * @param path base path where write the results
	 * @param result the results of the test to write
	 * @param set of class states
	 * @param classNodeName name of the class node
	 * @param confidenceLevel to use in aggregate metrics
	 * @param datasetDim dimension of the dataset
	 * @param testName name of the test
	 * @param kFolds number of folds in the cross validation
	 * @throws Exception in case of error
	 */
	protected void printTestResults(
			String path, 
			GenericTestResults<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, MicroMacroClassificationPerformances<Double,ClassificationStandardPerformances<Double>>> result, 
			Set<String> classStates, String classNodeName, String confidenceLevel, int datasetDim, String testName, int kFolds) throws Exception {
		
		System.out.println( result.getModelName() + " results printing");
		
		// Metrics file
		File folder = new File(path + S );
		if( !folder.exists())
			folder.mkdir();
	  	FileWriter outMetrics;
		File fileMetrics = new File(path + S + "metrics.csv");
		if( !fileMetrics.exists()) {
			fileMetrics.createNewFile();
			outMetrics  = new FileWriter(fileMetrics, true);
			outMetrics.write( GenericTestResults.metricsHeader(classStates) + "\n");
		} else
			outMetrics  = new FileWriter(fileMetrics, true);
		String metricsCSV = result.metricsToString(classStates, confidenceLevel, datasetDim, testName, kFolds) + "\n";
		outMetrics.write( metricsCSV);
		outMetrics.close();
		
		
	  	// Dataset classification printing
		File file;
		FileWriter out;
		file = new File(path + S + result.getModelName() + "-results.txt");
		if( !file.exists())
			file.createNewFile();
	  	out = new FileWriter(file);
	  	out.write(result.resultsToString(classNodeName));
	  	out.close();
	  	
	  	
	  	// Single tests printing
	  	(new File(path + S + result.getModelName() + S + "runs")).mkdirs();
	  	// Metrics
		fileMetrics = new File(path + S + result.getModelName() + S + "runs" + S + "metrics.csv");
		if( !fileMetrics.exists())
			fileMetrics.createNewFile();	
		outMetrics  = new FileWriter(fileMetrics);
		outMetrics.write( GenericTestResults.metricsHeader(classStates) + "\n");
		
	  	Iterator<ClassificationStandardPerformances<Double>> iterSingPerf = result.getPerformances().getPerformances().iterator();
		for( int i = 1;iterSingPerf.hasNext(); ++i) {
			ClassificationStandardPerformances<Double> singPerf = iterSingPerf.next();
			// metrics
			outMetrics.write( GenericTestResults.metricsToString(singPerf, "test" + i, classStates, confidenceLevel, datasetDim, "", testName, kFolds) + "\n");
			// model
			if(singPerf.getLearnedModel() != null) {
			  	File fileModel = new File(path + S + result.getModelName() + S + "runs" + S + "test" + i +"-model.ctbn");
				if( !fileModel.exists())
					fileModel.createNewFile();	
				FileWriter outModel  = new FileWriter(fileModel);
				outModel.write(singPerf.getLearnedModel().toString());
				outModel.close();
			}
		}
		outMetrics.close();
		
		
		// ROC and Precision-Recall curve
		File fileCurve;
		FileWriter outCurve;
		(new File(path + S + result.getModelName() + S + "ROCs" + S + "MicroAvg" + S)).mkdirs();
		(new File(path + S + result.getModelName() + S + "ROCs" + S + "MacroAvg" + S)).mkdirs();
		(new File(path + S + result.getModelName() + S + "Precision-Recall" + S + "MicroAvg" + S)).mkdirs();
	  	(new File(path + S + result.getModelName() + S + "Precision-Recall" + S + "MacroAvg" + S)).mkdirs();
	  	(new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MicroAvg" + S)).mkdirs();
	  	(new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MacroAvg" + S)).mkdirs();
	  	Iterator<String> iterClasses = classStates.iterator();
	  	for(int i = 1; iterClasses.hasNext(); ++i) {
	  		String classState = iterClasses.next();
	  		
			// ROC micro
			fileCurve = new File(path + S + result.getModelName() + S + "ROCs" + S + "MicroAvg" + S + "ROC-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printROC(result.getPerformances().getMicroAveraging(), classState));
			outCurve.close();
			
			// ROC macro
			fileCurve = new File(path + S + result.getModelName() + S + "ROCs" + S + "MacroAvg" + S + "ROC-" + confidenceLevel + "-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printROC(result.getPerformances().getMacroAveraging(), classState, confidenceLevel));
			outCurve.close();
			
			// PR curve micro
			fileCurve = new File(path + S + result.getModelName() + S + "Precision-Recall" + S + "MicroAvg" + S + "PRC-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printPRCurve(result.getPerformances().getMicroAveraging(), classState));
			outCurve.close();
			
			// PR curve  macro
			fileCurve = new File(path + S + result.getModelName() + S + "Precision-Recall" + S + "MacroAvg" + S + "PRC-" + confidenceLevel + "-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printPRCurve(result.getPerformances().getMacroAveraging(), classState, confidenceLevel));
			outCurve.close();
			
			// Cumulative Response micro
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MicroAvg" + S + "CumulativeResp-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printCumulativeResponse(result.getPerformances().getMicroAveraging(), classState));
			outCurve.close();
			
			// Cumulative Response macro
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MacroAvg" + S + "CumulativeResp-" + confidenceLevel + "-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printCumulativeResponse(result.getPerformances().getMacroAveraging(), classState, confidenceLevel));
			outCurve.close();
			
			// Lift Chart micro
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MicroAvg" + S + "LiftChart-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printLiftChart(result.getPerformances().getMicroAveraging(), classState));
			outCurve.close();
			
			// Lift Chart macro
			fileCurve = new File(path + S + result.getModelName() + S + "CumulativeResp&LiftChart" + S + "MacroAvg" + S + "LiftChart-" + confidenceLevel + "-" + i +".csv");
			if( !fileCurve.exists())
				fileCurve.createNewFile();	
			outCurve  = new FileWriter(fileCurve);
			outCurve.write(GenericTestResults.printLiftChart(result.getPerformances().getMacroAveraging(), classState, confidenceLevel));
			outCurve.close();
		}
		
	}
	
	
	/**
	 * Generate the test factory to execute the tests.
	 * 
	 * @param learningAlgorithms list of learning algorithm to use
	 * @param classificationAlg classification algorithm to use
	 * @param validationMethod tests validation method
	 * @return list of test factories one for each test to do
	 */
	private<PType extends IPerformances> List<ITestFactory<Double, CTDiscreteNode, ICTClassifier<Double,CTDiscreteNode>, PType>> generateTestFactories(
			List<ILearningAlgorithm<Double, CTDiscreteNode>> learningAlgorithms,
			CTBNCClassifyAlgorithm classificationAlg,
			IValidationMethod<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType> validationMethod) {
		
		this.verbosePrint("Test factories generation:\n");
		// List instantiation
		List<ITestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType>> testFactories =
				new Vector<ITestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType>>(this.modelToTest.size());
		
		// Test factories generation
		for( int i = 0; i < this.modelToTest.size(); ++i) {
			this.verbosePrint("\t. model " + this.modelToTest.get(i)[0] + "\n");
			testFactories.add( new CTBNCTestFactory<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,PType>(
										validationMethod, learningAlgorithms.get(i), classificationAlg)
							 );
		}
		
		this.verbosePrint("Test factories generation (END)\n");
		
		return testFactories;
	}
	
	
	/**
	 * Load inference algorithm (classification)
	 * to use in the tests.
	 * 
	 * @return inference algorithm to use in the tests
	 */
	private CTBNCClassifyAlgorithm loadInferenceAlgorithm() {
		
		this.verbosePrint("Inference algorithm generation:\n");
		
		// Set the parameters
		Map<String, Object> paramsClassifyAlg = new TreeMap<String,Object>();
		this.verbosePrint("\t. probabilities = " + this.probabilitiesFlag + "\n");
		paramsClassifyAlg.put("probabilities", this.probabilitiesFlag);
		this.verbosePrint("\t. binary class decider = " + (this.classDecider != null ? "enabled" : "disabled") + "\n");
		paramsClassifyAlg.put("classifyDecider", this.classDecider);
		
		// Generate the classification algorithm
		this.verbosePrint("\t. classification algorithm\n");
		CTBNCClassifyAlgorithm classificationAlg = new CTBNCClassifyAlgorithm();
		classificationAlg.setParameters(paramsClassifyAlg);
		
		this.verbosePrint("Inference algorithm generation (END)\n");
		
		return classificationAlg;
	}
	
	
	/**
	 * Return the algorithms to test.
	 * 
	 * @param baseNBModel base NB model
	 * @return list of the algorithms to test
	 */
	private List<ILearningAlgorithm<Double, CTDiscreteNode>> loadLearningAlgorithms(
			ICTClassifier<Double,CTDiscreteNode> baseNBModel) {
		
		this.verbosePrint("Learning algorithms generation:\n");
		List<ILearningAlgorithm<Double, CTDiscreteNode>> learningAlgorithms = new Vector<ILearningAlgorithm<Double, CTDiscreteNode>>(this.modelToTest.size());
		try {
			for( int i = 0; i < this.modelToTest.size(); ++i) {
				
				String[] modelData = this.modelToTest.get(i);
				this.verbosePrint("\t. model " + i + ": " +  modelData[0] + "\n");
				
				// Set the parameter learning parameters
				Map<String, Object> paramsParamsLAlg = new TreeMap<String,Object>();
				this.verbosePrint("\t\t. Mxx_prior = " + modelData[1] + "\n");
				paramsParamsLAlg.put("Mxx_prior", Double.parseDouble( modelData[1]));
				this.verbosePrint("\t\t. Tx_prior = " + modelData[2] + "\n");
				paramsParamsLAlg.put("Tx_prior", Double.parseDouble( modelData[2]));
				this.verbosePrint("\t\t. Px_prior = " + modelData[3] + "\n");
				paramsParamsLAlg.put("Px_prior", Double.parseDouble( modelData[3]));
				
				// Set the parameter learning algorithm
				ILearningAlgorithm<Double, CTDiscreteNode> paramLearningAlg = null;
				if( this.cvValidation != null || this.hoValidation != null) {	// classification
					// Parameter learning algorithm
					this.verbosePrint("\t\t. generation of classification parameter learning algorithm\n");
					CTBNCParameterLLAlgorithm classificationLearningAlg = new CTBNCParameterLLAlgorithm();
					
					paramLearningAlg = classificationLearningAlg;
					
				}else if( this.clusteringValidation != null) {					// clustering
					this.verbosePrint("\t\t. cluster type = " + this.testType + " clustering\n");
					paramsParamsLAlg.put("hardClustering", this.testType.equals("hard"));
					this.verbosePrint("\t\t. max iteration = " + this.stopCriterion.getMaxIteration() + "\n");
					this.verbosePrint("\t\t. % bound = " + this.stopCriterion.getChangedBound() + "\n");
	
					// Parameter learning algorithm
					this.verbosePrint("\t\t. generation of clustering parameter learning algorithm\n");
					CTBNClusteringParametersLLAlgorithm clusteringLearningAlg = new CTBNClusteringParametersLLAlgorithm();
					clusteringLearningAlg.setStopCriterion( this.stopCriterion);
					
					paramLearningAlg = clusteringLearningAlg;
				}else {
					System.err.println("Code bug: no validation method is enable");
					System.exit(1);
				}
				// Set the parameters of parameters learning algorithm
				paramLearningAlg.setParameters(paramsParamsLAlg);
				
				// Set the learning algorithms
				if( modelData[0].equals("CTNB")) {					// CTNB
					paramLearningAlg.setStructure( baseNBModel.getAdjMatrix());		// fix the structure of the model
					learningAlgorithms.add( paramLearningAlg);
					
				} else if( modelData[0].equals("CTBNC") || modelData[0].equals("ACTNB")){
					
					// Parameters of the structural learning algorithm
					boolean featuresSelection = modelData[0].equals("CTBNC");
					this.verbosePrint("\t\t. dimension penalty = " + modelData[4] + "\n");
					boolean dimensionPenalty = Boolean.parseBoolean( modelData[4]);
					this.verbosePrint("\t\t. parents bound = " + modelData[5] + "\n");
					int parentsBound = Integer.parseInt( modelData[5]);
					
					// Hill climbing factory selection (LL vs CLL)
					ICTBNCHillClimbingFactory hillClimbingElemFactory;
					if( modelData[6].equals("LL")) {
						this.verbosePrint("\t\t. log-likelihood scoring\n");
						hillClimbingElemFactory = new LLHillClimbingFactory(paramLearningAlg, parentsBound, dimensionPenalty, featuresSelection);	//dimension penalty, feature selection mode
					} else if( modelData[6].equals("CLL")) {
						this.verbosePrint("\t\t. conditional log-likelihood scoring\n");
						hillClimbingElemFactory = new CLLHillClimbingFactory(paramLearningAlg, parentsBound, dimensionPenalty, featuresSelection);	//dimension penalty, feature selection mode
					} else
						throw new RuntimeException("Code bug: scoring function not recognized (possible values {LL,CLL})");
					
					// Define the structural learning algorithm
					this.verbosePrint("\t\t. hill climbing optimization\n");
					CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual> structLearningAlgorithm = 
							new CTBNCLocalStructuralLearning<String,CTBNCHillClimbingIndividual>(hillClimbingElemFactory);
					structLearningAlgorithm.setStructure(baseNBModel.getAdjMatrix());
					learningAlgorithms.add( structLearningAlgorithm);
				} else
					throw new RuntimeException("Code bug: learning algorithm not recognized (possible values {CTNB,ACTNB,CTBNC})");
			}
		} catch(Exception e) {
			System.err.println("Error during the algorithms generation process: " + e);
			System.exit(1);
		}
		
		// Generate the one expert for one class learning algorithms
		if( this.m1vs1c) {
			this.verbosePrint("\t. 1vs1 algorithm generation");
			for(int i = 0; i < learningAlgorithms.size(); ++i) {
				this.verbosePrint("\t\t. " + this.modelToTest.get(i)[0] + " model");
				learningAlgorithms.set(i, new MultipleCTBNCLearningAlgorithm<Double, CTDiscreteNode>( learningAlgorithms.get( i)));
			}
		}
		
		this.verbosePrint("Learning algorithms generation (END)\n");		
		
		return learningAlgorithms;
	}

	
	/**
	 * Load the datasets.
	 * 
	 * @param dataPath path of a file or a directory that represent the input data (see --help)
	 * @param trainingSet training set to load
	 * @param testSet test set to load
	 * @return base Naive Bayes model instantiation
	 */
	private CTBNClassifier loadDatasets(String dataPath, List<ITrajectory<Double>> trainingSet, List<ITrajectory<Double>> testSet) {
		
		this.verbosePrint("Datasets generation:\n");
		
		if( dataPath == null || dataPath.isEmpty()) {
			System.err.println("Error: empty input data");
			System.exit(1);
		}
		if( testSet == null || (! testSet.isEmpty())) {
			throw new IllegalArgumentException("Code bug: the test set list must be instantiated before to call the function");
		}
		
		CTBNClassifier nbModel = null;
		if( this.trainingPath != null) {	// Load training and test set

			// Parameter checking
			if( trainingSet == null || (! trainingSet.isEmpty())) {
				throw new IllegalArgumentException("Code bug: the training set list must be instantiated before to call the function");
			}
			
			// Training set loading
			this.verbosePrint("\t. training set loading\n");
			try {
				nbModel = CTBNCTestFactory.loadDataset(this.testName, trainingSet, this.trainingPath, this.ext, this.fileSeparator, this.classColumnName, this.timeColumnName, this.trjSeparatorColumnName, this.validColumns, this.timeFactor, this.nClusters);
			} catch (Exception e) {
				System.err.println("Traing set load error: " + e);
				System.exit(1);
			}

			// Test set loading
			this.verbosePrint("\t. test set loading\n");
			try {
				CTBNCTestFactory.loadDataset(this.testName, testSet, dataPath, this.ext, this.fileSeparator, this.classColumnName, this.timeColumnName, this.trjSeparatorColumnName, this.validColumns, this.timeFactor, this.nClusters);
			} catch (Exception e) {
				System.err.println("Traing set load error: " + e);
				System.exit(1);
			}
			
			// Hold out dataset instantiation
			this.verbosePrint("\t. dataset loading in hold out validation method\n");
			if( this.hoValidation == null) {
				System.err.println("Validation method error. Hold out disabled when training set and test set are specified. For more information type --help.");
				System.exit(1);
			}
			this.hoValidation.setTrainingSet( trainingSet);
			this.hoValidation.setTestSet( testSet);
			
		} else {							// Load a single dataset in the test set
			
			this.verbosePrint("\t. single dataset loading\n");
			try {
				nbModel = CTBNCTestFactory.loadDataset(this.testName, testSet, dataPath, this.ext, this.fileSeparator, this.classColumnName, this.timeColumnName, this.trjSeparatorColumnName, this.validColumns, this.timeFactor, this.nClusters);
			} catch (Exception e) {
				System.err.println("Dataset load error: " + e);
				System.exit(1);
			}
			
			// Dataset permutation or partitioning using partitioning file
			if( this.clusteringValidation == null) {
				if( this.cVPartitionFile == null || this.cVPartitionFile.isEmpty()) {	// dataset permutation and cutting
					
					this.verbosePrint("\t. dataset permutation\n");
					testSet = CTBNCTestFactory.permuteDataset(testSet);
					this.verbosePrint("\t. dataset dimension cutting (% = " + this.cutPercentage + ")\n");
					testSet = CTBNCTestFactory.cutDataset(testSet, this.cutPercentage);
					
				}else {																	// dataset partitioning
					try {
						this.verbosePrint("\t. dataset paritioning using the referenced folding division\n");
						List<List<ITrajectory<Double>>> partitionedDataSet
							= CTBNCTestFactory.partitionDataset(this.cVPartitionFile, testSet, this.cVFilePrefixToRemove, this.ext);
						
						this.cvValidation.setMemorizeFolding(true);
						this.cvValidation.setFoldedDataset(partitionedDataSet);
					} catch (Exception e) {
						System.err.println("Dataset partitioning error: " + e);
						System.exit(1);
					}
					
				}
			}
		}
		
		if( this.classDecider != null && nbModel.getClassNode().getStatesNumber() != 2) {
			System.err.println("Error: binary class decider can not be used on dataset where the class node has more than 2 states. For more information type --help.");
			System.exit(1);
		}
		
		this.verbosePrint("Datasets generation (END)\n");
		
		return nbModel;
	}
	
	
	/**
	 * Check the compatibility of the modifiers
	 * and return true if everything is right.
	 * Otherwise print the errors and return
	 * false.
	 * 
	 * @return true if all the modifiers are compatible, false otherwise.
	 */
	private boolean checkSetModifiers() {

		boolean corr = true;
		
		if( this.modelToTest == null || this.modelToTest.size() < 1) {
			System.err.println("Modifiers error: no model to test is set. For more information type --help.");
			corr = false;
		}
		
		if( this.clusteringValidation == null && this.hoValidation == null && this.cvValidation == null) {
			System.err.println("Modifiers error: no validation method is enabled. For more information type --help.");
			corr = false; 
		}
		
		// Training set VS Hold Out missing
		if( this.trainingPath != null && this.hoValidation == null) {
			System.err.println("Modifiers error: if you set a training set you have to enable the Hold Out validation method. For more information type --help.");
			corr = false;
		}
		
		// Clustering VS Other validation methods
		if( this.clusteringValidation != null && (this.hoValidation != null || this.cvValidation != null)) {
			System.err.println("Modifiers error: when the clustering is enabled it will use a special validator, so you can not enable Cross Validation and Hold Out. For more information type --help.");
			corr = false; 
		}
		
		// Clustering VS Binary classification decider
		if( this.clusteringValidation != null && this.classDecider != null) {
			System.err.println("Modifiers error: when the clustering is enabled the binary class decider can not be used. For more information type --help.");
			corr = false; 
		}
		
		// Clustering VS 1 model 1 class
		if( this.clusteringValidation != null && this.m1vs1c) {
			System.err.println("Modifiers error: --1vs1 modifier can not be enabled with clustering. For more information type --help.");
			corr = false; 
		}
		
		// Class column name VS Time column name
		if( this.classColumnName.equals(this.timeColumnName)) {
			System.err.println("Modifiers error: class column can not be the same of time column. For more information type --help.");
			corr = false; 
		}
		
		// Trajectory separator column name VS Time column name
		if( this.trjSeparatorColumnName != null && this.trjSeparatorColumnName.equals(this.timeColumnName)) {
			System.err.println("Modifiers error: trajectory separator column can not be the same of time column. For more information type --help.");
			corr = false; 
		}
		
		// Valid columns VS Time column
		if( this.validColumns.contains( this.timeColumnName)) {
			System.err.println("Modifiers error: the valid columns must not contain the time column. For more information type --help.");
			corr = false;
		}
				
		// CV partition file VS CV validation method
		if( this.cVPartitionFile != null && this.cvValidation == null) {
			System.err.println("Modifiers error: Cross Validation file partition is specified while Cross Validation method is not enabled. For more information type --help.");
			corr = false; 
		}
		
		// CV partition file VS CV prefix to remove
		if( this.cVFilePrefixToRemove != null && this.cVPartitionFile == null) {
			System.err.println("Modifiers error: the prefix to remove in Cross Validation partitions is specified while the Cross Validation partition file is not specified. For more information type --help.");
			corr = false; 
		}
		
		// CV prefix to remove VS CV validation method
		if( this.cVFilePrefixToRemove != null && this.cvValidation == null) {
			System.err.println("Modifiers error: the prefix to remove in Cross Validation partitions is specified while Cross Validation method is not enabled. For more information type --help.");
			corr = false; 
		}
		
		if( this.testName == null || this.testName.isEmpty()) {
			System.err.println("Modifiers error: test name must not be empty. For more information type --help.");
			corr = false;
		}
		
		return corr;
	}
	
	
	/**
	 * Print all the parameters used to execute the test.
	 * 
	 * @param args modifiers
	 * @param filenames files
	 */
	private void printParameters( String[] args, String[] filenames) {

		try{
			// Create result directory
			(new File(this.resultsPath + S)).mkdir();
			
			// Create file
			FileWriter fstream = new FileWriter( this.resultsPath + S + "modifiers.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			  
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append( "Modifiers:\n");
			for(int i = 0; i < args.length; ++i)
				strBuilder.append( args[i] + "\n");
			strBuilder.append( "\nData path:\n");
			for(int i = 0; i < filenames.length; ++i)
				strBuilder.append( filenames[i] + "\n");
			  
			out.write( strBuilder.toString());
			out.close();
			  
		  }catch (Exception e){
			  System.err.println("Impossible to write the parameters file: " + e);
		  }
		
	}
	
	
	/**
	 *  Initialize the maps (modifiers, method to call).
	 * This method check the CommandLine class methods and associate
	 * the class methods with the modifierList methods.
	 */
	private void initModifiers() {
		
		validModifiers = new HashMap<String,Method>();
		validModifiersWithArgs = new HashMap<String,Method>();
		@SuppressWarnings("rawtypes")
		Class thisClass = this.getClass();
		
		// Get the CommandLine (this) class methods
		Method[] mList = null;
		try {
			mList = thisClass.getMethods();
		} catch (Exception e) {
			System.err.println("Unable to load modifiers.");
			System.exit(1);
		}
		
		// Load the method for each modifier
		for ( int i = 0; i < this.modifiersList.length; i++) {
			// Jump the disabled modifiers
			if( !this.modifiersList[i][3].equals("enabled"))
				continue;

			// Check the enabled modifiers
			boolean found = false;
			for ( int j = 0; j < mList.length; j++) {		
				
				if ( this.modifiersList[i][1].equals( mList[j].getName())) {
					if ( mList[j].getParameterTypes().length == 0) { 		// if the method has not parameters
						validModifiers.put( "--" + this.modifiersList[i][0], mList[j]);
						found = true;
						
					} else if ( mList[j].getParameterTypes().length == 1) { 	// if it is a method with parameters
						validModifiersWithArgs.put( "--" + this.modifiersList[i][0], mList[j]);
						found = true;
					}
				}
			}
			if (!found) {
				System.err.println("Unable to load modifier: " + this.modifiersList[i][0]);
			}
		}
	}

	/**
	 * This method break the string --modifier=arg1,arg2 in modifier name,
	 * and its arguments.
	 * 
	 * @param s string containing the modifier name and its arguments
	 * @param list list of arguments that will be pupolated in the method
	 * @return modifier name
	 */
	private String breakString(String s, LinkedList<String> list) {
		
		int i = s.indexOf("=");
		String mods = "";
		
		if (i < 0) {
			i = s.length();
		} else {
			mods = s.substring(i+1);
		}
		StringTokenizer st = new StringTokenizer(mods,",");
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		
		return s.substring(0,i);
	}
	
	
	/**
	 * Print the string in input only if the
	 * verbose modality is on.
	 * 
	 * @param s string to print.
	 */
	private void verbosePrint(String s) {
		
		if( this.verbose)
			System.out.println(s);
	}
	
	

	/***************************************
	 * Parameters and methods for software
	 * managing.
	 ***************************************/
	
	/**
	 * Print the help.
	 * Calling modifier: "--help".
	 * 
	 * Print the description list of the modifiers.
	 */
	public void printHelp() {
		
		System.out.println("CTBNCToolkit for classification");
		System.out.println("\nThis software is a prototype developed by Daniele Codecasa <codecasa@disco.unimib.it>\nworking in Model and Algorithm for Data & Text Mining lab (http://www.mad.disco.unimib.it/doku.php)\nUniversity of Milano-Bicocca, Italy.");
		System.out.println("\n\nUsage of CTBNToolkit:\n\tCTBNToolkit [modifiers] DataPath");
		System.out.println("\nThe modifiers that accept arguments have the following form --modifier=arg1,arg2,..,argN");
		System.out.println("\nModifiers:");
		for (int i = 0; i < this.modifiersList.length; i++) {
			// Jump the disabled modifiers
			if( !this.modifiersList[i][3].equals("enabled"))
				continue;
			
			System.out.println("--" + modifiersList[i][0] + "\n\t" + modifiersList[i][2]);
		}
		System.out.println("\nDataPath can be:");
		System.out.println("- a file path to test if only learned models are loaded or a different training set is specified");
		System.out.println("- the test set directory if only learned models are loaded or a different training set is specified");
		System.out.println("- a dataset directory to use ad training set and test set, if no learned models are loaded and not\ntraining set is specified");
		System.exit(0);
	}

	
	/**
	 * Set the models to learn and test.
	 * Calling modifier: "--CTBNC".
	 * 
	 * Parse the input and set the models to test.
	 *
	 * @param models list of model to parse in accord to the input definition of the modifier
	 */
	public void setCTBNCModels( LinkedList<String> models) {
	
		this.modelToTest = new Vector<String[]>(models.size());
		for( int i = 0; i < models.size(); ++i) {
			
			String[] modelDefinition = null;
			if( models.get(i).equals("CTNB")) {				// CTNB
				
				modelDefinition = new String[4];
				modelDefinition[0] = "CTNB";									// model
			}
			else if( models.get(i).startsWith("ACTNB")) {	// ACTNB
				
				Integer minusIdx = models.get(i).indexOf("-");
				if( minusIdx == -1) {
					System.err.println("CTBNC parsing error. Error in " + models.get(i) + " argument. For more information type --help.");
					System.exit(1);
				}
				
				modelDefinition = new String[7];
				modelDefinition[0] = "ACTNB";									// model
				modelDefinition[5] = models.get(i).substring(5, minusIdx);		// parent bound
				modelDefinition[6] = models.get(i).substring( minusIdx + 1);	// scoring function
			}
			else if( models.get(i).startsWith("CTBNC")) {	// CTBNC
				
				Integer minusIdx = models.get(i).indexOf("-");
				if( minusIdx == -1) {
					System.err.println("CTBNC parsing error. Error in " + models.get(i) + " argument. For more information type --help.");
					System.exit(1);
				}
				
				modelDefinition = new String[7];
				modelDefinition[0] = "CTBNC";									// model
				modelDefinition[5] = models.get(i).substring(5, minusIdx);		// parent bound
				modelDefinition[6] = models.get(i).substring( minusIdx + 1);	// scoring function
			}	
			else {
				System.err.println("CTBNC parsing error. Argument " + models.get(i) + " did not recognize. For more information type --help.");
				System.exit(1);
			}
			
			// If specified add the imaginary counts to the last model
			String m = "1.0", t = "0.005", p = "1.0", penalty = "false";
			
			while( i+1 < models.size() && (models.get(i+1).startsWith("M") ||  models.get(i+1).startsWith("T") || models.get(i+1).startsWith("P") || models.get(i+1).equals("penalty"))) {
				
				try {
					// Dimension penalty flag
					if( models.get(i + 1).equals("penalty")) {
						penalty = "true";
						continue;
					}
					
					// Parameters learning priors
					String prior = models.get(i+1).substring(1);
					Double.parseDouble(prior);
					if( models.get(i+1).startsWith("M"))
						m = prior;
					else if( models.get(i+1).startsWith("T"))
						t = prior;
					else if( models.get(i+1).startsWith("P"))
						p = prior;
					
				}catch(Exception e) {
					System.err.println("CTBNC parsing error. Prior counts " + models.get(i+1) + " did not recognize. For more information type --help.");
					System.exit(1);
				}
				
				++i;
			}
			modelDefinition[1] = m;					// imaginary counts for the variables transition counts
			modelDefinition[2] = t;					// imaginary time counts
			modelDefinition[3] = p;					// imaginary counts for the class variable
			if( modelDefinition.length > 4)
				modelDefinition[4] = penalty;			// imaginary counts for the class variable
			
			this.modelToTest.add(modelDefinition);
		}
	}
	List<String[]> modelToTest = null;
	

	/**
	 * Set the path of the models files to load.
	 * Calling modifier: "--model".
	 *
	 * @param modelsPath path of the models to load
	 */
	public void setModels( LinkedList<String> modelsPath) {
	
		System.err.println("--model modifier is not implemented yet you can not load precalculated models. For more information type --help.");
		System.exit(1);
	}

	
	/**
	 * Set the path of the training set.
	 * Calling modifier: "--training".
	 *
	 * @param path path of the training set directory
	 */
	public void setTrainingSet( LinkedList<String> path) {
	
		if( path.size() != 1) {
			System.err.println("Training set parsing error. One and only one argument is required for --training modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.trainingPath = path.get(0);
	}
	String trainingPath = null;
	
	
	/**
	 * Set the validation method.
	 * Calling modifier: "--validation".
	 *
	 * @param vMethod validation method to use
	 */
	public void setValidationMethod( LinkedList<String> vMethod) {
	
		if( vMethod.get(0).equals("CV")) {			// Cross validation
			try {
				int kFolds = 10;
				if( vMethod.size() > 1)
					kFolds = Integer.parseInt( vMethod.get(1));
				
				MicroMacroClassificationPerformancesFactory<Double,ClassificationStandardPerformances<Double>> microMacroFactory = 
						new MicroMacroClassificationPerformancesFactory<Double,ClassificationStandardPerformances<Double>>(
								new ClassificationStandardPerformancesFactory<Double>());
				this.cvValidation =
						new CrossValidation<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,MicroMacroClassificationPerformances<Double,ClassificationStandardPerformances<Double>>,ClassificationStandardPerformances<Double>>(
								microMacroFactory, kFolds, true);
			}catch(Exception e) {
				System.err.println("Validation method parsing error. Error in the number of CV folds parsing. For more information type --help.");
				System.exit(1);
			}
		}
		else if( vMethod.get(0).equals("HO")) {		// Hold out
			try {
				double cut = 0.7;
				if( vMethod.size() > 1)
					cut = Double.parseDouble( vMethod.get(1));
				
				ClassificationStandardPerformancesFactory<Double> performanceFactory = 
						new ClassificationStandardPerformancesFactory<Double>();
				this.hoValidation =
						new HoldOut<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>, ClassificationStandardPerformances<Double>>(
								performanceFactory, cut);
				
			}catch(Exception e) {
				System.err.println("Validation method parsing error. Error in the hold out cut percentage parsing. For more information type --help.");
				System.exit(1);
			}			
		}
		else {
			System.err.println("Validation method parsing error. No validation method recognized. For more information type --help.");
			System.exit(1);
		}
		
	}
	CrossValidation<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,MicroMacroClassificationPerformances<Double,ClassificationStandardPerformances<Double>>,ClassificationStandardPerformances<Double>> cvValidation = null;
	HoldOut<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClassificationStandardPerformances<Double>> hoValidation = null;
	
	
	/**
	 * Set clustering type of the test (soft/hard).
	 * Calling modifier: "--clustering".
	 *
	 * @param testType type of the test to activate
	 */
	public void setClustering( LinkedList<String> testType) {
		
		if( testType.size() > 3) {
			System.err.println("Clustering parsing error. No more than 3 parameters are allowed for --clustering modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.testType = "soft";
		int maxIteration = 10;
		double changedBound = 0.01;
		boolean type = false, bound = false, iter = false;
		try {
			for( int i = 0; i < testType.size(); ++i) {
				if( testType.get(i).equals("soft") || testType.get(i).equals("hard")) {
					if( type) {
						System.err.println("Clustering parsing error. Type of clustering specified more than one time in --clustering modifier. For more information type --help.");
						System.exit(1);
					}
					
					this.testType = testType.get(0);
					type = true;
					
				} else if( testType.get(i).charAt(0) == 'C' || testType.get(i).charAt(0) == 'c'){
					try {
						this.nClusters = Integer.parseInt( testType.get(i).substring(1));
						if( this.nClusters < 2) {
							System.err.println("Clustering parsing error. The number of clusters specified in --clustering modifier must be bigger than 1. For more information type --help.");
							System.exit(1);	
						}
					}catch(Exception e) {
						System.err.println("Clustering parsing error. The number of clusters specified in --clustering modifier must be an integer. For more information type --help.");
						System.exit(1);
					}
					
			    } else if( testType.get(i).contains(".")){
					if( bound) {
						System.err.println("Clustering parsing error. Percentage bound stop criterion specified more than one time in --clustering modifier. For more information type --help.");
						System.exit(1);
					}
					
					changedBound = Double.parseDouble( testType.get(i));
					bound = true;
						
				} else {
					if( iter) {
						System.err.println("Clustering parsing error. Maximum iteration bound stop criterion specified more than one time in --clustering modifier. For more information type --help.");
						System.exit(1);
					}
					
					maxIteration = Integer.parseInt( testType.get(i));
					iter = true;				
				}
			}
		
			this.stopCriterion = new StandardStopCriterion(maxIteration, changedBound);
		}catch(Exception e) {
			System.err.println("Clustering parsing error. Uncorrect argument in --clustering modifier. For more information type --help.");
			System.exit(1);
		}
		
		ClusteringExternalPerformancesFactory<Double> performanceFactory = new ClusteringExternalPerformancesFactory<Double>();
		this.clusteringValidation =
				new ClusteringInSample<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClusteringExternalPerformances<Double>>(performanceFactory);
	}
	String testType = "classification";
	ClusteringInSample<Double,CTDiscreteNode,ICTClassifier<Double,CTDiscreteNode>,ClusteringExternalPerformances<Double>> clusteringValidation = null;
	StandardStopCriterion stopCriterion = null;
	int nClusters = 0; 
	
	
	/**
	 * Set the directory where generate the results.
	 * Calling modifier: "--rPath".
	 *
	 * @param path path where generate the results
	 */
	public void setResultsPath( LinkedList<String> path) {
	
		if( path.size() != 1) {
			System.err.println("Results path parsing error. One and only one argument is required for --rPath modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.resultsPath = path.get(0) + this.S;
	}
	String resultsPath = null;
	
	
	/**
	 * Set the confidence level to use in the
	 * performances evaluation.
	 * Calling modifier: "--confidence".
	 *
	 * @param path path where generate the results
	 */
	public void setConfidence( LinkedList<String> confidence) {
	
		if( confidence.size() != 1) {
			System.err.println("Results confidence parsing error. One and only one argument is required for --confidence modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.confidenceLevel = confidence.get(0);
	}
	String confidenceLevel = "90%";
	
	
	/**
	 * Set the extension of the dataset files to load.
	 * Calling modifier: "--ext".
	 *
	 * @param extStr extension of the file to load
	 */
	public void setFileExt( LinkedList<String> extStr) {
	
		if( extStr.size() != 1) {
			System.err.println("Extension parsing error. One and only one argument is required for --ext modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.ext = extStr.get(0);
	}
	String ext = ".csv";
	
	
	/**
	 * Set the column separator of the files to load.
	 * Calling modifier: "--sep".
	 *
	 * @param sp separator of the files
	 */
	public void setFileSeparator( LinkedList<String> sp) {
	
		if( sp.size() != 1) {
			System.err.println("Files separator parsing error. One and only one argument is required for --sep modifier. For more information type --help.");
			System.exit(1);
		}
		if( sp.get(0).length() != 1) {
			System.err.println("Files separator parsing error. The argument of --sep modifier must be a character. For more information type --help.");
			System.exit(1);
		}
		
		this.fileSeparator = sp.get(0).charAt(0);
	}
	char fileSeparator = ',';
	
	
	
	/**
	 * Set the column name of the class.
	 * Calling modifier: "--className".
	 *
	 * @param clmName name of the column
	 */
	public void setClassColumnName( LinkedList<String> clmName) {
	
		if( clmName.size() != 1) {
			System.err.println("Class column name parsing error. One and only one argument is required for --className modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.classColumnName = clmName.get(0);
	}
	String classColumnName = "class"; 
										
	
	/**
	 * Set the column name of the time.
	 * Calling modifier: "--timeName".
	 *
	 * @param clmName name of the column
	 */
	public void setTimeColumnName( LinkedList<String> clmName) {
	
		if( clmName.size() != 1) {
			System.err.println("Time column name parsing error. One and only one argument is required for --timeName modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.timeColumnName = clmName.get(0);
	}
	String timeColumnName = "t";
	
	
	/**
	 * Set the column name of the trajectory separator
	 * column.
	 * Calling modifier: "--trjSeparator".
	 *
	 * @param clmName name of the column
	 */
	public void setTrjSeparator( LinkedList<String> clmName) {
	
		if( clmName.size() != 1) {
			System.err.println("Trajectories separator column name parsing error. One and only one argument is required for --trjSeparator modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.trjSeparatorColumnName = clmName.get(0);
	}
	String trjSeparatorColumnName = null;
	
	
	/**
	 * Set the names of the columns that represent
	 * a variable in the models.
	 * Calling modifier: "--validColumns".
	 *
	 * @param clmsNames names of the columns
	 */
	public void setValidColumns( LinkedList<String> clmsNames) {
	
		if( clmsNames.size() < 1) {
			System.err.println("Valid columns names parsing error. At least one argument is required for --validColumns modifier. For more information type --help.");
			System.exit(1);
		}
		
		for( int i = 0; i < clmsNames.size(); ++i) {
			
			this.validColumns.add( clmsNames.get(i));
		}
	}	
	Set<String> validColumns = new TreeSet<String>();;


	/**
	 * Set the time factor to use to scale the time
	 * of the trajectories.
	 * Calling modifier: "--timeFactor".
	 *
	 * @param tFactor time factor
	 */
	public void setTimeFactor( LinkedList<String> tFactor) {
	
		if( tFactor.size() != 1) {
			System.err.println("Time factor parsing error. One and only one argument is required for --timeFactor modifier. For more information type --help.");
			System.exit(1);
		}
		
		try {
			this.timeFactor = Double.parseDouble( tFactor.get(0));
		} catch( Exception e) {
			System.err.println("Time factor parsing error. Error in parsing double argument. For more information type --help.");
			System.exit(1);
		}
	}
	Double timeFactor = 1.0;
	
	
	/**
	 * Set the time threshold of the binary class decider.
	 * Calling modifier: "--bThreshold".
	 *
	 * @param threshold time factor
	 */
	public void setBinaryDecider( LinkedList<String> threshold) {
	
		if( threshold.size() != 1) {
			System.err.println("Binary class decision threshold parsing error. One and only one argument is required for --bThreshold modifier. For more information type --help.");
			System.exit(1);
		}
		
		try {
			this.classDecider = new BinaryDecider( Double.parseDouble( threshold.get(0)));
		} catch( Exception e) {
			System.err.println("Binary class decision threshold parsing error. Error in parsing double argument. For more information type --help.");
			System.exit(1);
		}
	}
	IClassifyDecider classDecider = null;
	
	
	/**
	 * Disable the probabilities calculation
	 * along the trajectories.
	 * It should make the test a bit faster.
	 */
	public void disableProbabilities() {
		
		this.probabilitiesFlag = false;
	}
	boolean probabilitiesFlag = true;
	
	
	/**
	 * Set the path of the cross validation
	 * partitioning file.
	 * Calling modifier: "--cvPartitions".
	 *
	 * @param filePath path of the file containing the  CV partitioning
	 */
	public void setCVPartitions( LinkedList<String> filePath) {
	
		if( filePath.size() != 1) {
			System.err.println("Cross validation partition parsing error. One and only one argument is required for --cvPartitions modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.cVPartitionFile = filePath.get(0);
	}
	String cVPartitionFile = null; 
									

	/**
	 * Set the prefix to remove from the names of
	 * the trajectories in the partitioning file.
	 * Calling modifier: "--cvPrefix".
	 *
	 * @param prefix prefix to remove from the names of the trajectories in the partitioning file
	 */
	public void setCVPrefix( LinkedList<String> prefix) {
	
		if( prefix.size() != 1) {
			System.err.println("Cross validation prefix parsing error. One and only one argument is required for --cvPrefix modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.cVFilePrefixToRemove = prefix.get(0);
	}
	String cVFilePrefixToRemove = null; 
	
	
	/**
	 * Set the percentage of the trajectories and
	 * trajectories length to use in the test.
	 * Calling modifier: "--cutPercentage".
	 *
	 * @param perc percentage to use
	 */
	public void setCutPercentage( LinkedList<String> perc) {
	
		if( perc.size() != 1) {
			System.err.println("Cut percentage parsing error. One and only one argument is required for --cutPercentage modifier. For more information type --help.");
			System.exit(1);
		}
		
		try {
			this.cutPercentage = Double.parseDouble( perc.get(0));
		} catch( Exception e) {
			System.err.println("Cut percentage parsing error. Error in parsing double argument. For more information type --help.");
			System.exit(1);
		}
	}
	Double cutPercentage = 1.0;
	
	
	/**
	 * Set name of the test.
	 *
	 * @param name name of the test
	 */
	public void setTestName( LinkedList<String> name) {
	
		if( name.size() != 1) {
			System.err.println("Test name parsing error. One and only one argument is required for --testName modifier. For more information type --help.");
			System.exit(1);
		}
		
		this.testName = name.get(0);
	}
	String testName = new SimpleDateFormat("yyMMdd_HHmm").format(Calendar.getInstance().getTime()) + "_Test"; 

	
	/**
	 * Set the verbose modality.
	 * Calling modifier: "--v".
	 */
	public void setVerbose() {
	
		this.verbose = true;
	}
	boolean verbose = false;

	
	/**
	 * Set the modality in which a model is learned
	 * to discriminate each class. The classification
	 * relies on experts votes.
	 * Calling modifier: "--1vs1".
	 */
	public void setModelToClass() {
	
		this.m1vs1c = true;
	}
	boolean m1vs1c = false;
	
}
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import CTBNCToolkit.*;
import CTBNCToolkit.performances.*;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that represent the result of a generic
 * test.
 *
 * @param <TimeType> type of the time interval in the trajectories (Integer = discrete time, Double = continuous time)
 * @param <TM> type of the model used in the test
 * @param <PType> type of returned performances
 */
public class GenericTestResults<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,PType extends IPerformances> implements ITestResults<TimeType,NodeType,TM,PType> {
	
	private String modelName;
	private TM trueModel;
	private List<ITrajectory<TimeType>> dataset;
	private PType performances;

	
	/**
	 * Base constructor.
	 * 
	 * @param modelName name of the tested model
	 * @param dataset dataset used for the test
	 * @param results result of the test
	 * @throws IllegalArgumentException if there is some error with the parameters
	 */
	public GenericTestResults(String modelName, List<ITrajectory<TimeType>> dataset, PType results) throws IllegalArgumentException{
		
		this( modelName, null, dataset, results);
	}
	

	/**
	 * Base constructor.
	 * 
	 * @param modelName name of the tested model
	 * @param trueModel model used to generate the dataset
	 * @param dataset dataset used for the test
	 * @param results result of the test
	 * @throws IllegalArgumentException if there is some error with the parameters
	 */
	public GenericTestResults(String modelName, TM trueModel, List<ITrajectory<TimeType>> dataset, PType results) throws IllegalArgumentException{
		
		if( modelName == null || modelName.equals(""))
			throw new IllegalArgumentException("Error: invilid model name");
		if( dataset == null)
			throw new IllegalArgumentException("Error: dataset can not be null");
		if( dataset.size() <= 0)
			throw new IllegalArgumentException("Error: empty dataset it is not allowed");
		if( results == null)
			throw new IllegalArgumentException("Erorr: null results are not allowed");
		
		this.modelName = modelName;
		this.trueModel = trueModel;
		this.dataset = dataset;
		this.performances = results;
		
	}
	

	/* (non-Javadoc)
	 * @see test.ITest#getModelName()
	 */
	@Override
	public String getModelName() {
		
		return this.modelName;
	}
	
	/* (non-Javadoc)
	 * @see test.ITest#getTrueModel()
	 */
	@Override
	public TM getTrueModel() {

		return this.trueModel;
	}
	
	/* (non-Javadoc)
	 * @see test.ITest#getDataset()
	 */
	@Override
	public List<ITrajectory<TimeType>> getDataset() {

		return this.dataset;
	}

	/* (non-Javadoc)
	 * @see test.ITest#getResults()
	 */
	@Override
	public PType getPerformances() {

		return this.performances;
	}


	/* (non-Javadoc)
	 * @see test.ITest#setTrueModel(CTBNToolkit.IModel)
	 */
	@Override
	public void setTrueModel(TM trueModel) {

		this.trueModel = trueModel;
	}
	
	
	/**
	 * Print the results of the test
	 * in a string.
	 * 
	 * @param classNodeName name of the class node
	 * @return string of test results
	 */
	@SuppressWarnings("unchecked")
	public String resultsToString(String classNodeName) {
		
		String str = "";
		
		if( IAggregatePerformances.class.isAssignableFrom(this.performances.getClass())) {
			IAggregatePerformances<TimeType,ISingleRunPerformances<TimeType>> aggPerformances = (IAggregatePerformances<TimeType,ISingleRunPerformances<TimeType>>) this.performances;
			Iterator<ISingleRunPerformances<TimeType>> singlePerformances = aggPerformances.getPerformances().iterator();
			for( int i = 1;singlePerformances.hasNext(); ++i) {
				str += "Test" + i + "\n";
				str += resultsToString(singlePerformances.next());
			}
		} else if( ISingleRunPerformances.class.isAssignableFrom(this.performances.getClass())) {
			str = resultsToString((ISingleRunPerformances<TimeType>) this.performances);
		} else
			str = "";
		
		return str;
	}
	
	/**
	 * Print the results of the test
	 * in a string.
	 * 
	 * @param performances to use to print results
	 * @param classNodeName name of the class node
	 * @return string of test results
	 */
	public static<TimeType extends Number & Comparable<TimeType>, NodeValueType> String resultsToString( ISingleRunPerformances<TimeType> performances) {

		String str = "";
		
		Iterator<IClassificationResult<TimeType>> results = performances.sortResultsTrajectoriesByNames().iterator();
		while( results.hasNext())
			str += results.next().resultToString() + "\n";
		
		return str;
	}


	/**
	 * Return the header for the metrics
	 * CSV file.
	 * 
	 * @param classStates set of classes states 
	 * @return header for the metrics CSV file
	 */
	public static String metricsHeader(Set<String> classStates) {
		
		int fS = 0; //(int)(classStates.size() / 2.0) - 1;	// separator first of the parameter name
		int sS = classStates.size() - fS - 1;			// separator after the parameter name
		
		String str = "Test;Model;Aggregation;ConfidenceLevel;DatasetDim;KFolds;" +
				";Accuracy;;" +
				"Error;" +
				addSeparator(fS) + "Precision;" + addSeparator(sS) + 
				addSeparator(fS) + "Recall;" + addSeparator(sS) +
				addSeparator(fS) + "F-Measure;" + addSeparator(sS) +
				addSeparator(fS) + "PR AUC;" + addSeparator(sS) +
				addSeparator(fS) + "Sensitivity;" + addSeparator(sS) +
				addSeparator(fS) + "Specificity;" + addSeparator(sS) +
				addSeparator(fS) + "TP-Rate;" + addSeparator(sS) +
				addSeparator(fS) + "FP-Rate;" + addSeparator(sS) +
				addSeparator(fS) + "ROC AUC;" + addSeparator(sS) +
				"Brier;Avg learning time;Var learning time;Avg inference time; Var inference time;\n" + 
				";;;;;;lower bound;;upper bound;;" +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addClasses(classStates) +
				addSeparator(5);
		
		return str;		
	}
	
	/**
	 * Write the metrics in a ";"-separated
	 * string. 
	 * 
	 * @param classStates set of classes states
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param testName name of the test run
	 * @param kFolds number of fold in the cross validation
	 * @return metric in a CSV format
	 */
	@SuppressWarnings("unchecked")
	public String metricsToString( Set<String> classStates, String confidenceLevel, int datasetDim, String testName, int kFolds) {
		
		if(MicroMacroClassificationPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.metricsToString((MicroMacroClassificationPerformances<TimeType,IClassificationSingleRunPerformances<TimeType>>) performances, this.getModelName(), classStates, confidenceLevel, datasetDim, testName, kFolds);
		else if(MicroAvgAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.metricsToString((IClassificationPerformances<TimeType>) this.performances, this.getModelName(), classStates, confidenceLevel, datasetDim, "MicroAvg", testName, kFolds);
		else if(MacroAvgAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.metricsToString((IClassificationPerformances<TimeType>) this.performances, this.getModelName(), classStates, confidenceLevel, datasetDim, "MacroAvg", testName, kFolds);
		else if(IClassificationPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.metricsToString((IClassificationPerformances<TimeType>) this.performances, this.getModelName(), classStates, confidenceLevel, datasetDim, "", testName, kFolds);
		else
			throw new RuntimeException("Error: metricsToString method in " + this.getClass().getName() + " class is not defined for " + this.performances.getClass().getName() + " class");

	}
		
	/**
	 * Write the metrics in a ";"-separated
	 * string. 
	 * 
	 * @param performances performances where take metrics
	 * @param modelName model name
	 * @param classStates set of classes states
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param testName name of the test run
	 * @param kFolds number of fold in the cross validation
	 * @return metric in a CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String metricsToString(MicroMacroClassificationPerformances<TimeType,?> performances, 
			String modelName, Set<String> classStates, String confidenceLevel, int datasetDim, String testName, int kFolds) {
		
		String str = metricsToString(performances.getMicroAveraging(), modelName, classStates, confidenceLevel, datasetDim, "MicroAvg",testName, kFolds) + "\n";

		performances.getMacroAveraging().setROCConfidenceInterval(confidenceLevel);
		performances.getMacroAveraging().setPrecisionRecallConfidenceInterval(confidenceLevel);
		str += metricsToString(performances.getMacroAveraging(), modelName, classStates, confidenceLevel, datasetDim, "MacroAvg",testName, kFolds);
		
		return str;
	}
	
	/**
	 * Write the metrics in a ";"-separated
	 * string.
	 * 
	 * @param performances performances where take metrics
	 * @param modelName model name
	 * @param classStates set of classes states
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param aggMode aggregation modality
	 * @param testName name of the test run
	 * @param kFolds number of fold in the cross validation
	 * @return return metric in a CSV format
	 */
	@SuppressWarnings("unchecked")
	public static<TimeType extends Number> String metricsToString(IClassificationPerformances<TimeType> performances, String modelName, Set<String> classStates, String confidenceLevel, int datasetDim, String aggMode, String testName, int kFolds) {
	
		Iterator<String> classesIter;
		String metricsStr;
		
		// Model name
		metricsStr = testName + ";" + modelName + ";" + aggMode + ";" + confidenceLevel + ";" + datasetDim + ";" + kFolds + ";";
		
		// Accuracy
		try {
			double[] acc = performances.getAccuracy(confidenceLevel);
			metricsStr += acc[0] + ";" + acc[1] + ";" + acc[2] + ";";
		}catch(Exception e) {
			metricsStr += "ERR;ERR;ERR;";
		}
		 
		// Error
		try {
			metricsStr += performances.getError() + ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		// Precision
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getPrecision(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// Recall
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getRecall(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// FMeasure
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getFMeasure(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// PR AUC
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getPrecisionRecallAUC(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// Sensitivity
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getSensitivity(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// Specificity
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getSpecificity(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// TP Rate
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getTruePositiveRate(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// FP Rate
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getFalsePositiveRate(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// ROC AUC
		classesIter = classStates.iterator();
		while( classesIter.hasNext()) {
			try {
				metricsStr += performances.getROCAUC(classesIter.next()) + ";";
			}catch(Exception e) {
				metricsStr += "ERR;";
			}
		}
		
		// Brier
		try {
			metricsStr += performances.getBrier() + ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		// Avg learning time
		try {
			metricsStr += performances.getLearningTime() + ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		// Var learning time
		try {
			if(IAggregatePerformances.class.isAssignableFrom( performances.getClass()))
				metricsStr += ((IAggregatePerformances<TimeType,?>) performances).getVarianceLearningTime() + ";";
			else
				metricsStr += ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		// Avg inference time
		try {
			metricsStr += performances.getAvgInferenceTime() + ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		// Var inference time
		try {
			metricsStr += performances.getVarianceInferenceTime() + ";";
		}catch(Exception e) {
			metricsStr += "ERR;";
		}
		
		return metricsStr;
	}

	/**
	 * Write the metrics in a ";"-separated
	 * string. 
	 * 
	 * @param testName name of the test
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param kFolds number of fold in the cross validation
	 * @return metric in a CSV format
	 */
	@SuppressWarnings("unchecked")
	public String performancesSummary( String testName, String confidenceLevel, int datasetDim, int kFolds) {
		
		// Classification
		if(MicroMacroClassificationPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((MicroMacroClassificationPerformances<TimeType,IClassificationSingleRunPerformances<TimeType>>) performances, testName, this.getModelName(), confidenceLevel, datasetDim, kFolds);
		else if(MicroAvgAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IClassificationPerformances<TimeType>) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "MicroAvg", kFolds);
		else if(MacroAvgAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IClassificationPerformances<TimeType>) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "MacroAvg", kFolds);
		else if(IClassificationPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IClassificationPerformances<TimeType>) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "", kFolds);
		// Clustering
		else if(MicroMacroClusteringPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((MicroMacroClusteringPerformances<TimeType, IExternalClusteringSingleRunPerformances<TimeType>>) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, kFolds);
		else if(MicroAvgExternalClusteringAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IExternalClusteringPerformances) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "MicroAvg", kFolds);
		else if(MacroAvgExternalClusteringAggregatePerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IExternalClusteringPerformances) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "MacroAvg", kFolds);
		else if(IExternalClusteringPerformances.class.isAssignableFrom(this.performances.getClass()))
			return GenericTestResults.performancesSummary((IExternalClusteringPerformances) this.performances, testName, this.getModelName(), confidenceLevel, datasetDim, "", kFolds);
		else
			throw new RuntimeException("Error: metricsToString method in " + this.getClass().getName() + " class is not defined for " + this.performances.getClass().getName() + " class");

	}

	/**
	 * Performances summary in CSV format.
	 * 
	 * @param performances performances where take metrics
	 * @param testName name of the test
	 * @param modelName name of the model tested
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param kFolds number of fold in the cross validation
	 * @return summary of performances in CSV format
	 */
	private static<TimeType extends Number & Comparable<TimeType>> String performancesSummary(
			MicroMacroClusteringPerformances<TimeType, ?> performances,
			String testName, String modelName, String confidenceLevel,
			int datasetDim, int kFolds) {
		
		String str = performancesSummary(performances.getMicroAveraging(), testName, modelName, confidenceLevel, datasetDim, "MicroAvg", kFolds) + "\n";
		str += performancesSummary(performances.getMacroAveraging(), testName, modelName, confidenceLevel, datasetDim, "MacroAvg", kFolds);
		
		return str;
	}
	
	/**
	 * Performances summary in CSV format.
	 * 
	 * @param performances performances where take metrics
	 * @param testName name of the test
	 * @param modelName name of the model tested
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param aggMode aggregation modality (micro averaging = "MicroAvg", macro averaging = "MacroAvg", no aggregation (single run performances) = "")
	 * @param kFolds number of fold in the cross validation
	 * @return summary of performances in CSV format
	 */
	private static String performancesSummary(
			IExternalClusteringPerformances performances, String testName,
			String modelName, String confidenceLevel, int datasetDim,
			String aggMode, int kFolds) {
		
		StringBuilder strBuilder = new StringBuilder();
		
		if( kFolds > 1) {
			strBuilder.append( performancesSummary(performances, testName, modelName, confidenceLevel, datasetDim, aggMode, kFolds, "out of sample"));
			if( performances.getInSamplePerformances() != null)
				strBuilder.append( performancesSummary(performances.getInSamplePerformances(), testName, modelName, confidenceLevel, datasetDim, aggMode, kFolds, "in of sample"));
		} else
			strBuilder.append( performancesSummary(performances.getInSamplePerformances(), testName, modelName, confidenceLevel, datasetDim, aggMode, kFolds, "in of sample"));
		
		return strBuilder.toString();
	}
	
	/**
	 * Performances summary in CSV format.
	 * 
	 * @param performances performances where take metrics
	 * @param testName name of the test
	 * @param modelName name of the model tested
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param aggMode aggregation modality (micro averaging = "MicroAvg", macro averaging = "MacroAvg", no aggregation (single run performances) = "")
	 * @param kFolds number of fold in the cross validation
	 * @param sampleMode sampling modality ("in sample", "out of sample")
	 * @return summary of performances in CSV format
	 */
	private static String performancesSummary(
			IExternalClusteringPerformances performances, String testName,
			String modelName, String confidenceLevel, int datasetDim,
			String aggMode, int kFolds, String sampleMode) {
		
		StringBuilder strBuilder = new StringBuilder();
		
		// Model name
		strBuilder.append( testName + ";" + 
					modelName + ";" + 
					sampleMode + ";" +
					aggMode + ";" + 
					//confidenceLevel + ";" +
					datasetDim + ";" +
					kFolds + ";");
		
		// #classes
		try {
			strBuilder.append( performances.classesNumber() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// #clusters
		try {
			strBuilder.append( performances.clusterNumber() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Rand statistic
		try {
			strBuilder.append( performances.getRandStatistic() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Jaccard coefficient
		try {
			strBuilder.append( performances.getJaccardCoefficient() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Folkes & Mallows index
		try {
			strBuilder.append( performances.getFolkesMallowsIndex() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Avg learning time
		try {
			strBuilder.append( performances.getLearningTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Var learning time
		try {
			if(IAggregatePerformances.class.isAssignableFrom( performances.getClass()))
				strBuilder.append( ((IAggregatePerformances<?,?>) performances).getVarianceLearningTime() + ";");
			else
				strBuilder.append( ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Avg inference time
		try {
			strBuilder.append( performances.getAvgInferenceTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Var inference time
		try {
			strBuilder.append( performances.getVarianceInferenceTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		return strBuilder.toString();
	}


	/**
	 * Performances summary in CSV format.
	 * 
	 * @param performances performances where take metrics
	 * @param testName name of the test
	 * @param modelName name of the model tested
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param kFolds number of fold in the cross validation
	 * @return summary of performances in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String performancesSummary(
			MicroMacroClassificationPerformances<TimeType,?> performances, 
			String testName, String modelName, String confidenceLevel, int datasetDim, int kFolds) {
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append( performancesSummary(performances.getMicroAveraging(), testName, modelName, confidenceLevel, datasetDim, "MicroAvg", kFolds) + "\n");
		strBuilder.append( performancesSummary(performances.getMacroAveraging(), testName, modelName, confidenceLevel, datasetDim, "MacroAvg", kFolds));
		
		return strBuilder.toString();
	}
	
	/**
	 * Performances summary in CSV format.
	 * 
	 * @param performances performances where take metrics
	 * @param testName name of the test
	 * @param modelName name of the model tested
	 * @param confidenceLevel level of confidence to use
	 * @param datasetDim dataset dimension
	 * @param aggMode aggregation modality
	 * @param kFolds number of fold in the cross validation
	 * @return summary of performances in CSV format
	 */
	@SuppressWarnings("unchecked")
	public static<TimeType extends Number> String performancesSummary(IClassificationPerformances<TimeType> performances, 
			String testName,  String modelName, String confidenceLevel, int datasetDim, String aggMode, int kFolds) {

		StringBuilder strBuilder = new StringBuilder();
		
		// Model name
		strBuilder.append( testName + ";" + 
					modelName + ";" + 
					aggMode + ";" + 
					confidenceLevel + ";" +
					datasetDim + ";" +
					kFolds + ";");
		
		// Accuracy
		try {
			double[] acc = performances.getAccuracy(confidenceLevel);
			strBuilder.append( acc[0] + ";" + acc[1] + ";" + acc[2] + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;ERR;ERR;");
		}
		
		// Error
		try {
			strBuilder.append( performances.getError() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Brier
		try {
			strBuilder.append( performances.getBrier() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Avg learning time
		try {
			strBuilder.append( performances.getLearningTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Var learning time
		try {
			if(IAggregatePerformances.class.isAssignableFrom( performances.getClass()))
				strBuilder.append( ((IAggregatePerformances<TimeType,?>) performances).getVarianceLearningTime() + ";");
			else
				strBuilder.append( ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Avg inference time
		try {
			strBuilder.append( performances.getAvgInferenceTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		// Var inference time
		try {
			strBuilder.append( performances.getVarianceInferenceTime() + ";");
		}catch(Exception e) {
			strBuilder.append( "ERR;");
		}
		
		return strBuilder.toString();
	}
	
	/**
	 * Return the header for the performance
	 * summary CSV.
	 * 
	 * @param clustering false to print the classification header, true to print clustering header
	 * @return header of performance summary CSV
	 */
	public static String performacesSummaryHeader(boolean clustering) {
		
		if( !clustering) {
			return "Test;" +
					"Model;" +
					"Aggregation;" +
					"ConfidenceLevel;" +
					"DatasetDim;" +
					"KFolds;" +
					";Accuracy;;" +
					"Error;" + 
					"Brier;" +
					"Avg learning time;" +
					"Var learning time;" +
					"Avg inference time; " +
					"Var inference time;\n" + 
					";;;;;;lower bound;;upper bound;;;;;;;";
		} else {
			return "Test;" +
					"Model;" +
					"Sampling;" +
					"Aggregation;" +
					//"ConfidenceLevel;" +
					"DatasetDim;" +
					"KFolds;" +
					"#classes;" +
					"#clusters;" +
					"Rand Stat;" +
					"Jaccard Coeff;" +
					"Folkes Mallows Idx;" +
					"Avg learning time;" +
					"Var learning time;" +
					"Avg inference time; " +
					"Var inference time;";
		}
		
	}
	
	
	/**
	 * Return a string with a sequence
	 * of N ";".
	 * 
	 * @param N number of separator in the sequence
	 * @return a string of N separators in sequence
	 */
	public static String addSeparator( int N) {
		
		String str = "";
		for( int i = 0; i < N; ++i)
			str += ";";
		
		return str;
	}
	
	/**
	 * Return a string with all the classes
	 * states written and separated by ";".
	 * 
	 * @param classStates set of classes states names
	 * @return string of the classes names separated by ";"
	 */
	public static String addClasses( Set<String> classStates) {
		
		String str = "";
		Iterator<String> iter = classStates.iterator();
		while( iter.hasNext())
			str += iter.next() + ";";
		
		return str;
	}
	
	/**
	 * Print the cumulative response curve.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the curve to print
	 * @param confidenceInterval confidence interval for the vertical averaging curve calculation
	 * @return cumulative response curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printCumulativeResponse(
			MacroAvgAggregatePerformances<TimeType,?> performances, 
			String classState, String confidenceInterval) {
		
		performances.setLiftConfidenceInterval(confidenceInterval);
		try {
			return GenericTestResults.cumulativeRespHeader(true) + "\n" + GenericTestResults.curveToString(performances.getCumulativeResponse(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the cumulative response curve.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the curve to print
	 * @return cumulative response curve in CSV format
	 */
	public static<TimeType extends Number> String printCumulativeResponse(
			IClassificationPerformances<TimeType> performances, 
			String classState) {
		
		try {
			return GenericTestResults.cumulativeRespHeader(false) + "\n" + GenericTestResults.curveToString(performances.getCumulativeResponse(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the lift chart.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the curve to print
	 * @param confidenceInterval confidence interval for the vertical averaging curve calculation
	 * @return lift chart curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printLiftChart(
			MacroAvgAggregatePerformances<TimeType,?>  performances, 
			String classState, String confidenceInterval) {
		
		performances.setLiftConfidenceInterval(confidenceInterval);
		try {
			return GenericTestResults.liftChartHeader(true) + "\n" + GenericTestResults.curveToString(performances.getLiftChart(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the lift chart.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the curve to print
	 * @return lift chart in CSV format
	 */
	public static<TimeType extends Number> String printLiftChart(
			IClassificationPerformances<TimeType> performances, 
			String classState) {
		
		try {
			return GenericTestResults.liftChartHeader(false) + "\n" + GenericTestResults.curveToString(performances.getLiftChart(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}

	/**
	 * Print the ROC curve.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the ROC curve to print
	 * @param confidenceInterval confidence interval for the vertical averaging curve calculation
	 * @return ROC curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printROC(
			MacroAvgAggregatePerformances<TimeType,?> performances, 
			String classState, String confidenceInterval) {
		
		performances.setROCConfidenceInterval(confidenceInterval);
		try {
			return GenericTestResults.ROCHeader(true) + "\n" + GenericTestResults.curveToString(performances.getROC(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the ROC curve.
	 * 
	 * @param performances micro-average performances
	 * @param classState state of the ROC curve to print
	 * @return ROC curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printROC(
			MicroAvgAggregatePerformances<TimeType,?> performances, 
			String classState) {
		
		try {
			return GenericTestResults.ROCHeader(false) + "\n" + GenericTestResults.curveToString(performances.getROC(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the ROC curve.
	 * 
	 * @param performances single run performances
	 * @param classState state of the ROC curve to print
	 * @return ROC curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printROC(
			IClassificationSingleRunPerformances<TimeType> performances, 
			String classState) {
		
		try {
			return GenericTestResults.ROCHeader(false) + "\n" + GenericTestResults.curveToString(performances.getROC(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/** 
	 * Print the header of the cumulative
	 * response curve.
	 * 
	 * @param intervalFlag if true print also the vertical interval header
	 * @return header of the cumulative response curve
	 */
	private static String cumulativeRespHeader(boolean intervalFlag) {
		
		if( intervalFlag)
			return "%-Population;%-Positive;%Pos-low;%Pos-up;";
		else
			return "%-Population;%-Positive;";
	}

	/** 
	 * Print the header of the lift chart.
	 * 
	 * @param intervalFlag if true print also the vertical interval header
	 * @return header of the lift chart
	 */
	private static String liftChartHeader(boolean intervalFlag) {
		
		if( intervalFlag)
			return "%-Population;Lift;Lift-low;Lift-up;";
		else
			return "%-Population;Lift;";
	}
	
	/**
	 * Print the header of the ROC
	 * curve.
	 * 
	 * @param intervalFalg if true print also the vertical interval header
	 * @return header of the ROC curve
	 */
	public static String ROCHeader(boolean intervalFlag) {
		
		if( intervalFlag)
			return "FP-Rate;TP-Rate;TPR-low;TPR-up;";
		else
			return "FP-Rate;TP-Rate;";
	}
	
	
	/**
	 * Print the precision-recall curve.
	 * 
	 * @param performances macro-average performances
	 * @param classState state of the ROC curve to print
	 * @param confidenceInterval confidence interval for the vertical averaging curve calculation
	 * @return precision-recall curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printPRCurve(
			MacroAvgAggregatePerformances<TimeType,?> performances, 
			String classState, String confidenceInterval) {
		
		performances.setROCConfidenceInterval(confidenceInterval);
		try {
			return GenericTestResults.PRHeader(true) + "\n" + GenericTestResults.curveToString(performances.getPrecisionRecallCurve(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the precision-recall curve.
	 * 
	 * @param performances micro-average performances
	 * @param classState state of the ROC curve to print
	 * @return precision-recall curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printPRCurve(
			MicroAvgAggregatePerformances<TimeType,?> performances, 
			String classState) {
		
		try{
			return GenericTestResults.PRHeader(false) + "\n" + GenericTestResults.curveToString(performances.getPrecisionRecallCurve(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the precision-recall curve.
	 * 
	 * @param performances single run performances
	 * @param classState state of the ROC curve to print
	 * @return precision-recall curve in CSV format
	 */
	public static<TimeType extends Number & Comparable<TimeType>> String printPRCurve(
			IClassificationSingleRunPerformances<TimeType> performances, 
			String classState) {
		
		try {
			return GenericTestResults.PRHeader(false) + "\n" + GenericTestResults.curveToString(performances.getPrecisionRecallCurve(classState));
		}catch(Exception e) {
			return e.toString();
		}
	}
	
	/**
	 * Print the header of the Precision-
	 * Recall curve.
	 * 
	 * @param intervalFalg if true print also the vertical interval header
	 * @return header of the Precision-Recall curve
	 */
	public static String PRHeader(boolean intervalFlag) {
		
		if( intervalFlag)
			return "Recall;Precision;Pr-low;Pr-up;";
		else
			return "Recall;Precision;";
	}
	
	
	/**
	 * Print the curve data in a
	 * CSV format.
	 * 
	 * @param curve curve to print
	 * @return string of the curve in CSV format.
	 */
	public static String curveToString(List<double[]> curve) {
		
		StringBuilder strBuilder = new StringBuilder(10000);
		
		for(int i = 0; i < curve.size(); ++i) {
			double[] data = curve.get(i);
			for( int j = 0; j < data.length; ++j)
				strBuilder.append( data[j] + ";");
			
			if( i != curve.size() - 1)
				strBuilder.append( "\n");
		}
		
		return strBuilder.toString();
	}
	
	
	/**
	 * Return a CSV ";" separated table that
	 * inform about the statistical differences
	 * in different methods accuracy.
	 * It is require the same number of tests
	 * over the same dataset partitions.
	 * 
	 * @param resultsList list of results
	 * @return table of comparisons between accuracy of different tests
	 */
	public static<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,PType extends IPerformances> String compareMethods( 
			List<GenericTestResults<TimeType,NodeType,TM,PType>> resultsList) {
	
		List<String> confidenceLevels = new Vector<String>(5);
		confidenceLevels.add("99%");
		confidenceLevels.add("95%");
		confidenceLevels.add("90%");
		confidenceLevels.add("80%");
		confidenceLevels.add("70%");
		
		return compareMethods( resultsList, confidenceLevels);
	}
	
	
	/**
	 * Return a CSV ";" separated table that
	 * inform about the statistical differences
	 * in different methods accuracy.
	 * It is require the same number of tests
	 * over the same dataset partitions.
	 * 
	 * @param resultsList list of results
	 * @param confidenceLevels list of confidence level to use in the table comparisons
	 * @return table of comparisons between accuracy of different tests
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static<TimeType extends Number & Comparable<TimeType>,NodeType extends INode,TM extends ICTClassifier<TimeType,NodeType>,PType extends IPerformances> String compareMethods( 
  			List<GenericTestResults<TimeType,NodeType,TM,PType>> resultsList, 
  			List<String> confidenceLevels) {
		
		if( resultsList == null)
			throw new IllegalArgumentException("Null result list argument");  		
  		if( resultsList.size() <= 1)
  			return "\n";
 
  		
  		StringBuilder strBuilder = new StringBuilder(1000);
  		
  		for( int iCL = 0; iCL < confidenceLevels.size(); ++iCL) {
  			
  			strBuilder.append( "Comparison test - ;" + confidenceLevels.get(iCL) + addSeparator(resultsList.size()) + "\n;");
  			
	  		for(int i = 0; i < resultsList.size(); ++i)
	  			strBuilder.append( resultsList.get(i).getModelName() + ";");
	  		
	  		for(int i = 0; i < resultsList.size(); ++i) {
	  			strBuilder.append( "\n" + resultsList.get(i).getModelName() + ";");
	  			for(int j = 0; j < resultsList.size(); ++j) {
	  				
	  				if( i == j)
	  					strBuilder.append( ";");
	  				else {
	  					if( MicroMacroClassificationPerformances.class.isAssignableFrom( resultsList.get(i).getPerformances().getClass()))
	  						strBuilder.append( 
	  								tTest( ((MicroMacroClassificationPerformances) resultsList.get(i).getPerformances()).getMacroAveraging(), 
	  									   ((MicroMacroClassificationPerformances) resultsList.get(j).getPerformances()).getMacroAveraging(), confidenceLevels.get(iCL)) + ";");
	  					else if(ClassificationStandardPerformances.class.isAssignableFrom( resultsList.get(i).getPerformances().getClass()))
	  						strBuilder.append( 
	  								tTest( (ClassificationStandardPerformances) resultsList.get(i).getPerformances(), 
	  									   (ClassificationStandardPerformances) resultsList.get(j).getPerformances(), confidenceLevels.get(iCL)) + ";");
	  					else
	  						strBuilder.append("ERR;");
	  				}
	  			}
	  		}
	  		
	  		strBuilder.append( "\n");
  		}
  		
  		return strBuilder.toString();
  	}

	
	/**
	 * Compare the accuracy of the performances
	 * of two tests.
	 * Return "LF" if the first is better then the
	 * second, "UP" if the second is better then
	 * the first, "0" if they are comparable.
	 * 
	 * @param performancesRow
	 * @param performancesColumn
	 * @param confidenceLevel
	 * @return LF if the row is statistically better then the column, UP in the opposite case, and 0 if they are comparable
	 */
	private static<TimeType extends Number & Comparable<TimeType>,NodeValueType,PType extends IClassificationSingleRunPerformances<TimeType>> String tTest(ClassificationStandardPerformances<TimeType> performancesRow,
			ClassificationStandardPerformances<TimeType> performancesColumn, String confidenceLevel) {

		try {
			double[] accRow = performancesRow.getAccuracy( confidenceLevel);
			double[] accCol = performancesColumn.getAccuracy( confidenceLevel);
			
			if( accRow[0] > accCol[2])
				return "LF";
			else if( accRow[2] < accCol[0] )
				return "UP";
			else
				return "0";
	
		}catch(Exception e) {
			return "ERR";
		}
	}
	

	/**
	 * Compare the accuracy of the performances
	 * of two tests.
	 * Return "LF" if the first is better then the
	 * second, "UP" if the second is better then
	 * the first, "0" if they are comparable.
	 * 
	 * @param performancesRow
	 * @param performancesColumn
	 * @param confidenceLevel
	 * @return LF if the row is statistically better then the column, UP in the opposite case, and 0 if they are comparable
	 */
	private static<TimeType extends Number & Comparable<TimeType>,NodeValueType,PType extends IClassificationSingleRunPerformances<TimeType>> String tTest(MacroAvgAggregatePerformances<TimeType,PType> performancesRow,
			MacroAvgAggregatePerformances<TimeType,PType> performancesColumn, String confidenceLevel) {

		try {
			double diff = performancesRow.getAccuracy() - performancesColumn.getAccuracy();
			
			int nTests = 0;
			double denominator = 0.0;
			Iterator<PType> iterRow = performancesRow.getPerformances().iterator();
			Iterator<PType> iterColumn = performancesColumn.getPerformances().iterator();
			while(iterRow.hasNext() && iterColumn.hasNext()) {
				double testDiff = iterRow.next().getAccuracy() - iterColumn.next().getAccuracy();
				
				denominator += Math.pow(diff - testDiff, 2);
				++nTests;
			}
			if(iterRow.hasNext() || iterColumn.hasNext())
				throw new IllegalArgumentException("Error: the two performances has a different number of tests");
			
			denominator = Math.sqrt( denominator / nTests);
			
			double res = diff/denominator;
			double z = StatisticalTables.tStudentTable(nTests - 1, confidenceLevel);
			if( res > z)
				return "LF";
			else if( res < -z )
				return "UP";
			else
				return "0";
	
		}catch(Exception e) {
			return "ERR";
		}
	}
  	
}

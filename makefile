JC=javac
JC_FLAGS=
JAR=jar
JAR_FLAGS=cvfm
LIBS=./lib/commons-math3-3.0.jar:./lib/opencsv-2.3.jar
SOURCES=\
	./CTBNCToolkit/BinaryDecider.java \
	./CTBNCToolkit/ClassificationResults.java \
	./CTBNCToolkit/ClassificationTransition.java \
	./CTBNCToolkit/ClassifyAlgorithm.java \
	./CTBNCToolkit/CTBNCClassifyAlgorithm.java \
	./CTBNCToolkit/CTBNClassifier.java \
	./CTBNCToolkit/CTBNCLocalStructuralLearning.java \
	./CTBNCToolkit/CTBNCParameterLLAlgorithm.java \
	./CTBNCToolkit/CTDiscreteNode.java \
	./CTBNCToolkit/CTTrajectory.java \
	./CTBNCToolkit/CTTransition.java \
	./CTBNCToolkit/DiscreteModel.java \
	./CTBNCToolkit/DiscreteNode.java \
	./CTBNCToolkit/GenericLearningResults.java \
	./CTBNCToolkit/IClassificationResult.java \
	./CTBNCToolkit/IClassificationTransition.java \
	./CTBNCToolkit/IClassifyAlgorithm.java \
	./CTBNCToolkit/IClassifyDecider.java \
	./CTBNCToolkit/ICTClassifier.java \
	./CTBNCToolkit/IDiscreteNode.java \
	./CTBNCToolkit/ILearningAlgorithm.java \
	./CTBNCToolkit/ILearningResults.java \
	./CTBNCToolkit/IModel.java \
	./CTBNCToolkit/INode.java \
	./CTBNCToolkit/ITrajectory.java \
	./CTBNCToolkit/ITransition.java \
	./CTBNCToolkit/LearningAlgorithm.java \
	./CTBNCToolkit/MultipleCTBNC.java \
	./CTBNCToolkit/MultipleCTBNCLearningAlgorithm.java \
	./CTBNCToolkit/MultipleCTBNCLearningResults.java \
	./CTBNCToolkit/Node.java \
	./CTBNCToolkit/NodeIndexing.java \
	./CTBNCToolkit/StatisticalTables.java \
	./CTBNCToolkit/StructuralLearningScoringFormulae.java \
	./CTBNCToolkit/SufficientStatistics.java \
	./CTBNCToolkit/clustering/ClusteringAlgorithm.java \
	./CTBNCToolkit/clustering/ClusteringResults.java \
	./CTBNCToolkit/clustering/CTBNClusteringParametersLLAlgorithm.java \
	./CTBNCToolkit/clustering/IClusteringAlgorithm.java \
	./CTBNCToolkit/clustering/IStopCriterion.java \
	./CTBNCToolkit/clustering/StandardStopCriterion.java \
	./CTBNCToolkit/frontend/CommandLine.java \
	./CTBNCToolkit/frontend/Main.java \
	./CTBNCToolkit/optimization/CLLHillClimbingFactory.java \
	./CTBNCToolkit/optimization/CTBNCHillClimbingIndividual.java \
	./CTBNCToolkit/optimization/ICache.java \
	./CTBNCToolkit/optimization/ICacheElement.java \
	./CTBNCToolkit/optimization/ICTBNCHillClimbingFactory.java \
	./CTBNCToolkit/optimization/IElementFactory.java \
	./CTBNCToolkit/optimization/ILocalSearchIndividual.java \
	./CTBNCToolkit/optimization/IOptimizationAlgorithm.java \
	./CTBNCToolkit/optimization/IOptimizationElement.java \
	./CTBNCToolkit/optimization/LLHillClimbingFactory.java \
	./CTBNCToolkit/optimization/LRUCache.java \
	./CTBNCToolkit/performances/ClassificationStandardPerformances.java \
	./CTBNCToolkit/performances/ClassificationStandardPerformancesFactory.java \
	./CTBNCToolkit/performances/ClusteringExternalPerformances.java \
	./CTBNCToolkit/performances/ClusteringExternalPerformancesFactory.java \
	./CTBNCToolkit/performances/IAggregatePerformances.java \
	./CTBNCToolkit/performances/IAggregatePerformancesFactory.java \
	./CTBNCToolkit/performances/IClassificationAggregatePerformances.java \
	./CTBNCToolkit/performances/IClassificationPerformances.java \
	./CTBNCToolkit/performances/IClassificationSingleRunPerformances.java \
	./CTBNCToolkit/performances/IExternalClusteringAggregatePerformances.java \
	./CTBNCToolkit/performances/IExternalClusteringPerformances.java \
	./CTBNCToolkit/performances/IExternalClusteringSingleRunPerformances.java \
	./CTBNCToolkit/performances/IPerformances.java \
	./CTBNCToolkit/performances/ISingleRunPerformances.java \
	./CTBNCToolkit/performances/ISingleRunPerformancesFactory.java \
	./CTBNCToolkit/performances/MacroAvgAggregatePerformances.java \
	./CTBNCToolkit/performances/MacroAvgExternalClusteringAggregatePerformances.java \
	./CTBNCToolkit/performances/MicroAvgAggregatePerformances.java \
	./CTBNCToolkit/performances/MicroAvgExternalClusteringAggregatePerformances.java \
	./CTBNCToolkit/performances/MicroMacroClassificationPerformances.java \
	./CTBNCToolkit/performances/MicroMacroClassificationPerformancesFactory.java \
	./CTBNCToolkit/performances/MicroMacroClusteringPerformances.java \
	./CTBNCToolkit/performances/MicroMacroClusteringPerformancesFactory.java \
	./CTBNCToolkit/tests/CTBNClassifierFactory.java \
	./CTBNCToolkit/tests/CTBNCTestFactory.java \
	./CTBNCToolkit/tests/GenericTestResults.java \
	./CTBNCToolkit/tests/IModelFactory.java \
	./CTBNCToolkit/tests/ITestFactory.java \
	./CTBNCToolkit/tests/ITestResults.java \
	./CTBNCToolkit/tests/validators/BaseValidationMethod.java \
	./CTBNCToolkit/tests/validators/ClusteringInSample.java \
	./CTBNCToolkit/tests/validators/CrossValidation.java \
	./CTBNCToolkit/tests/validators/HoldOut.java \
	./CTBNCToolkit/tests/validators/IValidationMethod.java
OBJECTS=$(SOURCES:.java=.class)
MANIFEST=MANIFEST.MF

all:
	$(JC) $(JC_FLAGS) -classpath $(LIBS) $(SOURCES)

jar:
	$(JAR) $(JAR_FLAGS) CTBNCToolkit.jar $(MANIFEST) $(OBJECTS)

clean:
	$(RM) ./CTBNCToolkit/*.class
	$(RM) ./CTBNCToolkit/clustering/*.class
	$(RM) ./CTBNCToolkit/frontend/*.class
	$(RM) ./CTBNCToolkit/optimization/*.class
	$(RM) ./CTBNCToolkit/performances/*.class
	$(RM) ./CTBNCToolkit/tests/*.class
	$(RM) ./CTBNCToolkit/tests/validators/*.class


CTBNCToolkit
============

Continuous time Bayesian network classifiers are designed for temporal classification of multivariate streaming data when time duration of events matters and the class does not change over time. CTBNCToolkit is an open source Java toolkit which provides a stand-alone application for temporal classification and a library for continuous time Bayesian network classifiers.

CTBNCToolkit can be used for scientific purposes, such as model comparison and temporal classification of interesting scientific problems, but it can be used as well as a prototype to address real world problems.

CTBNCToolkit was built at [MAD laboratory](http://www.mad.disco.unimib.it/), mainly by [Daniele Codecasa](http://www.linkedin.com/in/codecasa/en) as part of his Ph.D. thesis.


Usage
--------------
CTBNCToolkit requires opencsv-2.3 library to read the csv files and commons-math3-3.0 library for the Gamma function calculation.
To compile and try the toolkit follow these steps:
- download [opencsv 2.3](http://sourceforge.net/projects/opencsv/) library (direct link [here](http://sourceforge.net/projects/opencsv/files/opencsv/2.3/opencsv-2.3-src-with-libs.tar.gz/download));
- download [commons math 3.0](http://commons.apache.org/proper/commons-math/download_math.cgi) library (direct link [here](http://archive.apache.org/dist/commons/math/binaries/commons-math3-3.0-bin.zip));
- create a 'lib' folder in the same directory were the 'CTBNCToolkit' folder is contained;
- copy the downloaded libraries (i.e. commons-math3-3.0.jar and opencsv-2.3.jar) in the lib folder;
- run the command 'make' to compile the library;
- run the command 'make jar' to create an executable .jar file;
- try it out with the command

    java -jar CTBNCToolkit.jar --help


External links
--------------

* [Website](http://dcodecasa.wordpress.com/ctbnc/) (all what you need)
* [Documentation](http://arxiv.org/abs/1404.4893) (paper / tutorial)
* [Related papers](http://dcodecasa.wordpress.com/ctbnc/papers/) (related papers)
* [Source code](https://github.com/dcodecasa/CTBNCToolkit) (GitHub)

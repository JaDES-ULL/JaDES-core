package es.ull.simulation.experiment;

import com.beust.jcommander.Parameter;

/** 
 * Commonly used arguments for the simulation experiments.
 */
public class CommonArguments {
	@Parameter(names = { "--output", "-o" }, description = "Name of the output file name", order = 1)
	public String outputFileName = null;
	@Parameter(names = { "--runs", "-r" }, description = "Number of simulation experiments to launch", order = 2)
	public int nRuns = 1;
	@Parameter(names = { "--horizon", "-h" }, description = "Time horizon for the simulation (years)", order = 3)
	public int timeHorizon = -1;
	@Parameter(names = { "--nthreads", "-th" }, description = "Sets a specific number of threads to run the experiments in parallel (by default, the number of available processors)", order = 5)
	public int nThreads = Runtime.getRuntime().availableProcessors();
	@Parameter(names = { "--parallel", "-p" }, description = "Enables parallel execution", order = 4)
	public boolean parallel = false;
	@Parameter(names = { "--quiet", "-q" }, description = "Quiet execution (does not print progress info)", order = 6)
	public boolean quiet = false;
    
}

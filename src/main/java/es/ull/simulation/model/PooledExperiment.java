/**
 * 
 */
package es.ull.simulation.model;

import es.ull.simulation.utils.concurrent.StandardThreadPool;

/**
 * A class to execute several simulations in parallel. It uses a pool of threads to execute the 
 * simulations
 * @author Iván Castilla Rodríguez
 *
 */
public abstract class PooledExperiment extends Experiment {
	/** Internal structure to execute simulations in parallel */
	final private StandardThreadPool<Simulation> pool;

	/**
	 * Constructs a PooledExperiment with the given description.
	 * This constructor initializes a PooledExperiment with the specified description and defaults the number of experiments
	 * to the number of available processors, running them in parallel.
	 *
	 * @param description   A description of the experiment.
	 */
	public PooledExperiment(String description) {
		this(description, Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Constructs a PooledExperiment with the given description and number of threads.
	 * This constructor initializes a PooledExperiment with the specified description and number of threads.
	 * It sets up the experiment to run with a pool of threads to execute experiments in parallel.
	 *
	 * @param description   A description of the experiment.
	 * @param nThreads      The number of threads to be used for running experiments in parallel.
	 */
	public PooledExperiment(String description, int nThreads) {
		super(description, 1, true, nThreads);
		pool = StandardThreadPool.getPool(nThreads);
	}

	/** 
	 * Launches a simulation to be executed in the thread pool
	 * @param sim New simulation to execute
	 */
	public void execSimulation(Simulation sim) {
		pool.execute(sim);
	}
	
	/**
	 * Preparation for the parallel execution
	 */
	public abstract void preExecution();
	/**
	 * Parallel execution of simulations
	 */
	public abstract void parallelExecution();
	/**
	 * Steps to be performed after the parallel simulation is finished
	 */
	public abstract void postExecution();
	
	@Override
	public void start() {
		preExecution();
		parallelExecution();
		postExecution();
	}
}

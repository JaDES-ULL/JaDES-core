package es.ull.simulation.experiment;

/**
 * An interface to provide experiment configuration
 */
public interface IExperimentConfigurationProvider {
    /**
     * Gets the number of runs for the experiment
     * @return The number of runs for the experiment
     */
    public int getNRuns();
    /**
     * Gets the seed for the random number generator
     * @return The seed for the random number generator
     */
    public long getSeed();
    /**
     * Gets the time horizon for the simulation (years)
     * @return The time horizon for the simulation (years)
     */
    public int getTimeHorizon();
    /**
     * Gets the number of threads to run the experiments in parallel
     * @return The number of threads to run the experiments in parallel
     */
    public int getNThreads();
    /**
     * Indicates if the experiments should be run in parallel
     * @return true if the experiments should be run in parallel, false otherwise
     */
    public boolean isParallel();

}

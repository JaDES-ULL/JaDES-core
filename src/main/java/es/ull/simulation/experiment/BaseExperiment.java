package es.ull.simulation.experiment;

/**
 * A class to execute several simulation experiments sequentially. It uses a single thread to execute the simuation experiments.
 */
public abstract class BaseExperiment implements IExperiment {
	/** A short text describing this experiment */
	private final String description;
	/** The arguments for the experiment */
	private final CommonArguments arguments;


	/**
	 * Creates a new experiment.
	 * @param description A short text describing this experiment
	 */
    public BaseExperiment(String description, CommonArguments arguments) {
        super();
        this.description = description;
		this.arguments = arguments;
    }

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the number of experiments to be carried out.
	 * @return The number of experiments to be carried out
	 */
	public int getNExperiments() {
		return arguments.nRuns;
	}

	/**
	 * Returns the arguments for the experiment.
	 * @return The arguments for the experiment
	 */
	public CommonArguments getArguments() {
		return arguments;
	}

    @Override
    public void run() {
        beforeStart();
		final int nExperiments = getNExperiments();
		if (arguments.parallel) {
			final int nThreads = arguments.nThreads;
			try {
				final Thread[] workers = new Thread[nThreads];
				int nExperimentsPerThread = nExperiments / nThreads;
				for (int nTh = 0; nTh < nThreads; nTh++) {
					workers[nTh] = new Thread(new ParallelExperimentsLauncher(nExperimentsPerThread * nTh,
							Math.min(nExperiments, nExperimentsPerThread * (nTh + 1)) - 1));
					workers[nTh].start();
				}
				for (int nTh = 0; nTh < nThreads; nTh++) {
					workers[nTh].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		else {
			for (int i = 0; i < nExperiments; i++) {
				runExperiment(i);
			}
		}
        afterFinalize();
    }
	
	protected class ParallelExperimentsLauncher implements Runnable {
        /** The index of the first experiment executed in this launcher */
		private final int firstIndex;
        /** The index of the last experiment executed in this launcher */
		private final int lastIndex;

		
		public ParallelExperimentsLauncher(int firstIndex, int lastIndex) {
			this.firstIndex = firstIndex;
			this.lastIndex = lastIndex;
		}

		@Override
		public void run() {
			for (int sim = firstIndex; sim <= lastIndex; sim++) {
				runExperiment(sim);
			}
		}
		
	}
    
}

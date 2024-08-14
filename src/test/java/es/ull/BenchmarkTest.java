/**
 * 
 */
package es.ull;

import java.io.PrintStream;

import com.beust.jcommander.JCommander;

import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.model.Simulation;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class BenchmarkTest {
	private static final int MINARGS = 8;
	static int nThreads = 1;
	static int nElem = 512;
	static int nAct = 512;
	static int nIter = 100;
	static int nExp = 1;
	static int rtXact = 4;
	static int rtXres = 1;
	static int mixFactor = 2;
	static long workLoad = 0;
	static BenchmarkModel.OverlappingType ovType = BenchmarkModel.OverlappingType.SAMETIME;
	static BenchmarkModel.ModelType modType = BenchmarkModel.ModelType.CONFLICT;
	static boolean debug = true;
	static PrintStream out = System.out;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final BenchmarkArguments arguments = new BenchmarkArguments();
		final JCommander jc = JCommander.newBuilder().addObject(arguments).build();
		jc.parse(args);

		int argCounter = 0;
		if (args.length >= MINARGS) {
			modType = BenchmarkModel.ModelType.valueOf(args[argCounter++]);
			ovType = BenchmarkModel.OverlappingType.valueOf(args[argCounter++]);
			nAct = Integer.parseInt(args[argCounter++]);
			nElem = Integer.parseInt(args[argCounter++]);
			nIter = Integer.parseInt(args[argCounter++]);
			nThreads = Integer.parseInt(args[argCounter++]);
			nExp = Integer.parseInt(args[argCounter++]);
			workLoad = Long.parseLong(args[argCounter++]);
			if (args.length > argCounter) {
				if (ovType == BenchmarkModel.OverlappingType.MIXED) {
					mixFactor = Integer.parseInt(args[argCounter++]);
				}
				// Debug is always the last parameter
				if (args.length > argCounter)
					debug = "D".equals(args[args.length - 1]);
			}
		} else if (args.length > 0) { 
			System.err.println("Wrong number of arguments.\n Arguments expected: " + MINARGS);
			System.exit(0);
		} 
		
		BaseExperiment exp = new BaseExperiment("Same Time", arguments) {
			long t1;

			@Override
			public void beforeStart() {
				t1 = System.nanoTime();
				super.beforeStart();
			}
			
			@Override
			public void afterFinalize() {
				super.afterFinalize();
				System.out.println("TOTAL EXPERIMENT: " + ((System.nanoTime() - t1) / 1000000) + " miliseconds");
			}
			
			@Override
			public void runExperiment(int ind) {
				BenchmarkModel config = new BenchmarkModel(ind, modType, ovType, nThreads, nIter, nElem,
						nAct, mixFactor, workLoad);
				config.setRtXact(rtXact);
				config.setRtXres(rtXres);
				System.out.println(config);
				Simulation sim = config.getTestModel(); 
				
				if (debug)
					sim.addInfoReceiver(new BenchmarkListener(System.out));
				sim.run();;
			}
			
		};
		
		exp.run();		
	}
	
	public static class BenchmarkArguments extends CommonArguments {

	}
}

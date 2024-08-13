/**
 * 
 */
package es.ull;

import es.ull.simulation.factory.SimulationFactory;
import es.ull.simulation.model.Experiment;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.TimeStamp;
import es.ull.simulation.model.TimeUnit;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class TestPerformance {
	final static TimeUnit unit = TimeUnit.MINUTE;
	final static TimeStamp STARTTS = TimeStamp.getZero();
	final static TimeStamp ENDTS = TimeStamp.getZero();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Experiment("EXP", 1) {

			@Override
			public Simulation getSimulation(int ind) {
				SimulationFactory factory = new SimulationFactory(ind, "SimTest", unit, STARTTS, ENDTS);
				Simulation sim = factory.getSimulation();
				int i = 0;
				try {
					for (; ; i++)
						factory.getResourceTypeInstance("RT" + i);
				} catch(OutOfMemoryError e) {
					System.out.println("Not enough memory with " + i + " res. types");
				} 
//				try {
//					for (; ; i++)
//						factory.getTimeDrivenActivityInstance(i, "ACT" + i);
//				} catch(OutOfMemoryError e) {
//					System.out.println("Not enough memory with " + i + " activities");
//				}
				
				return sim;
			}			
		}.start();

	}

}

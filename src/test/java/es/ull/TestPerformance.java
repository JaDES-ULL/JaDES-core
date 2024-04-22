/**
 * 
 */
package com.ull;

import com.ull.simulation.factory.SimulationFactory;
import com.ull.simulation.factory.SimulationType;
import com.ull.simulation.model.Experiment;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.TimeStamp;
import com.ull.simulation.model.TimeUnit;

/**
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class TestPerformance {
	final static SimulationType simType = SimulationType.PARALLEL;
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

/**
 * 
 */
package es.ull.simulation.info;

import es.ull.simulation.model.Simulation;

public class TimeChangeInfo extends AsynchronousInfo {

	public TimeChangeInfo(Simulation model, long ts) {
		super(model, ts);
	}
	
	public String toString() {
		return simul.long2SimulationTime(getTs()) + "\t[SIM]\tCLOCK AVANCED";
	}
}
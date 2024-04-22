package es.ull.simulation.info;

import es.ull.simulation.model.Simulation;

public abstract class TimeStampedInfo extends SimulationInfo {

	final protected long ts;
	
	TimeStampedInfo(final Simulation model, final long ts) {
		super(model);
		this.ts = ts;
	}

	public long getTs() {
		return ts;
	}

}

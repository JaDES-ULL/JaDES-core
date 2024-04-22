package es.ull.simulation.info;

import es.ull.simulation.model.Simulation;

public abstract class AsynchronousInfo extends TimeStampedInfo {

	public AsynchronousInfo(final Simulation model, final long ts) {
		super(model, ts);
	}

}

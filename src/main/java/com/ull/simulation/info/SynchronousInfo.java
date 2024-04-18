package com.ull.simulation.info;

import com.ull.simulation.model.Simulation;

public abstract class SynchronousInfo extends TimeStampedInfo {

	public SynchronousInfo(final Simulation model, final long ts) {
		super(model, ts);
	}

}

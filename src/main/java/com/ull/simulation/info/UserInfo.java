package com.ull.simulation.info;

import com.ull.simulation.model.Simulation;

public abstract class UserInfo extends AsynchronousInfo {

	public boolean finalInfo = false;

	public UserInfo(final Simulation model, final long ts) {
		super(model, ts);
	}

	public boolean isFinalInfo() {
		return finalInfo;
	}

	public void setFinalInfo(final boolean finalInfo) {
		this.finalInfo = finalInfo;
	}
		
}
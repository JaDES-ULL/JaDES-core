package es.ull.simulation.info;

import es.ull.simulation.model.Describable;
import es.ull.simulation.model.Simulation;

public abstract class SimulationInfo {
	/**
	 * A common interface for all the Type enums that appear in each {@link SimulationInfo} class 
	 * @author Iv�n Castilla
	 *
	 */
	public interface InfoType extends Describable {	}

	final protected Simulation simul;
	
	public SimulationInfo(final Simulation simul) {
		this.simul = simul;
	}

	public Simulation getSimul() {
		return simul;
	}
}
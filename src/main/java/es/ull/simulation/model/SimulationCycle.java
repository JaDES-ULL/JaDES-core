/**
 * 
 */
package com.ull.simulation.model;

import com.ull.utils.cycle.Cycle;

/**
 * A wrapper class for {@link com.ull.util.cycle.Cycle Cycle} to be used inside a simulation. 
 * Thus {@link TimeStamp} can be used to define the cycle parameters.
 * @author Iv�n Castilla Rodr�guez
 *
 */
public interface SimulationCycle {
	/**
	 * Returns the inner {@link com.ull.util.cycle.Cycle Cycle}.
	 * @return the inner {@link com.ull.util.cycle.Cycle Cycle}
	 */
	Cycle getCycle();
}

/**
 * 
 */
package com.ull.simulation.model.flow;

import com.ull.simulation.model.Simulation;

/**
 * Meets the Multiple Instances with a Priori Design-Time Knowledge pattern (WFP13)
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class SynchronizedMultipleInstanceFlow extends StaticPartialJoinMultipleInstancesFlow {

	/**
	 * Creates a Synchronized Multiple Instances flow
	 * @param nInstances The number of thread instances this flow creates and which must 
	 * finish to pass the control
	 */
	public SynchronizedMultipleInstanceFlow(Simulation model, int nInstances) {
		super(model, nInstances, nInstances);
	}


}

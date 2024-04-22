/**
 * 
 */
package com.ull.simulation.inforeceiver;

import com.ull.simulation.info.AsynchronousInfo;
import com.ull.simulation.info.SimulationInfo;
import com.ull.simulation.info.SynchronousInfo;

/**
 * @author ycallero
 *
 */
public interface InfoHandler {

	public Number notifyInfo (SimulationInfo info);
	public void asynchronousInfoProcessing(AsynchronousInfo info);
	public Number synchronousInfoProcessing(SynchronousInfo info);
}

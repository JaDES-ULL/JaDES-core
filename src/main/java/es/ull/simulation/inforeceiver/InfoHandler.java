/**
 * 
 */
package es.ull.simulation.inforeceiver;

import es.ull.simulation.info.AsynchronousInfo;
import es.ull.simulation.info.SimulationInfo;
import es.ull.simulation.info.SynchronousInfo;

/**
 * @author ycallero
 *
 */
public interface InfoHandler {

	public Number notifyInfo (SimulationInfo info);
	public void asynchronousInfoProcessing(AsynchronousInfo info);
	public Number synchronousInfoProcessing(SynchronousInfo info);
}

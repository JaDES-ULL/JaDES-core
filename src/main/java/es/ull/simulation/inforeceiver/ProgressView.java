/**
 * 
 */
package es.ull.simulation.inforeceiver;

import es.ull.simulation.info.SimulationInfo;
import es.ull.simulation.info.SimulationStartStopInfo;
import es.ull.simulation.info.TimeChangeInfo;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class ProgressView extends Listener {
	long nextMsg = 0;
	final long gap;
	int percentage = 0;
	public ProgressView(long endTs) {
		super("Progress");
		addTargetInformation(TimeChangeInfo.class);
		addTargetInformation(SimulationStartStopInfo.class);
		gap = endTs / 100;
		nextMsg = gap;
	}

	@Override
	public void infoEmited(SimulationInfo info) {
		if (info instanceof SimulationStartStopInfo) {
			final SimulationStartStopInfo tInfo = (SimulationStartStopInfo)info;
		
			if (SimulationStartStopInfo.Type.START.equals(tInfo.getType())) {
				System.out.println("Starting!!");
			}
		}
		else if (info instanceof TimeChangeInfo) {
			if (((TimeChangeInfo) info).getTs() >= nextMsg) {
				System.out.println("" + (++percentage) + "%");
				nextMsg += gap;
			}
		}	
	}
}

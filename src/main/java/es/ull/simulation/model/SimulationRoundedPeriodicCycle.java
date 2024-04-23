/**
 * 
 */
package es.ull.simulation.model;

import es.ull.simulation.functions.AbstractTimeFunction;
import es.ull.simulation.utils.cycle.Cycle;
import es.ull.simulation.utils.cycle.RoundedPeriodicCycle;
import es.ull.simulation.utils.cycle.RoundedPeriodicCycle.Type;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class SimulationRoundedPeriodicCycle implements ISimulationCycle {
	private RoundedPeriodicCycle cycle;
	
	/**
	 * Creates a new cycle which "rounds" the values that it returns and 
	 * finishes at the specified timestamp.
	 * @param startTs Relative time when this cycle is expected to start.
	 * @param period Time interval between two successive events.
	 * @param endTs Relative time when this cycle is expected to finish.
	 * @param type The way the events are going to be treated.
	 * @param scale The factor to which the results are fitted. 
	 */
	public SimulationRoundedPeriodicCycle(TimeUnit unit, long startTs, AbstractTimeFunction period, long endTs,
										  Type type, long scale, long shift) {
		this(unit, new TimeStamp(unit, startTs), period, new TimeStamp(unit, endTs), type, new TimeStamp(unit, scale),
				new TimeStamp(unit, shift));
	}

	/**
	 * Creates a new cycle which "rounds" the values that it returns and 
	 * finishes at the specified timestamp.
	 * @param startTs Relative time when this cycle is expected to start.
	 * @param period Time interval between two successive events.
	 * @param endTs Relative time when this cycle is expected to finish.
	 * @param type The way the events are going to be treated.
	 * @param scale The factor to which the results are fitted. 
	 */
	public SimulationRoundedPeriodicCycle(TimeUnit unit, TimeStamp startTs, AbstractTimeFunction period,
										  TimeStamp endTs, Type type, TimeStamp scale, TimeStamp shift) {
		cycle = new RoundedPeriodicCycle(unit.convert(startTs), period, unit.convert(endTs), type, unit.convert(scale),
				unit.convert(shift));
	}

	/**
	 * Creates a cycle which "rounds" the values that it returns and is 
	 * executed the specified iterations.
	 * @param startTs Relative time when this cycle is expected to start.
	 * @param period Time interval between two successive events.
	 * @param iterations How many times this cycle is executed. A value of 0 indicates 
     * infinite iterations.
	 * @param type The way the events are going to be treated.
	 * @param scale The factor to which the results are fitted. 
	 */
	public SimulationRoundedPeriodicCycle(TimeUnit unit, long startTs, AbstractTimeFunction period, int iterations,
										  Type type, long scale, long shift) {
		this(unit, new TimeStamp(unit, startTs), period, iterations, type, new TimeStamp(unit, scale),
				new TimeStamp(unit, shift));
	}

	/**
	 * Creates a cycle which "rounds" the values that it returns and is 
	 * executed the specified iterations.
	 * @param startTs Relative time when this cycle is expected to start.
	 * @param period Time interval between two successive events.
	 * @param iterations How many times this cycle is executed. A value of 0 indicates 
     * infinite iterations.
	 * @param type The way the events are going to be treated.
	 * @param scale The factor to which the results are fitted. 
	 */
	public SimulationRoundedPeriodicCycle(TimeUnit unit, TimeStamp startTs, AbstractTimeFunction period,
										  int iterations, Type type, TimeStamp scale, TimeStamp shift) {
		cycle = new RoundedPeriodicCycle(unit.convert(startTs), period, iterations, type, unit.convert(scale),
				unit.convert(shift));
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.ISimulationCycle#getCycle()
	 */
	@Override
	public Cycle getCycle() {
		return cycle;
	}

}

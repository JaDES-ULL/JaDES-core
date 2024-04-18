/*
 * Simulation.java
 *
 * Created on 8 de noviembre de 2005, 18:47
 */

package com.ull.simulation.model.engine;

import com.ull.simulation.inforeceiver.InfoReceiver;
import com.ull.simulation.model.ActivityManager;
import com.ull.simulation.model.Debuggable;
import com.ull.simulation.model.DiscreteEvent;
import com.ull.simulation.model.Element;
import com.ull.simulation.model.ElementType;
import com.ull.simulation.model.Identifiable;
import com.ull.simulation.model.Resource;
import com.ull.simulation.model.ResourceList;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.TimeDrivenElementGenerator;
import com.ull.simulation.model.TimeUnit;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.Flow;
import com.ull.simulation.model.flow.MergeFlow;
import com.ull.simulation.model.flow.RequestResourcesFlow;
import com.ull.util.Output;

/**
 * Main simulation class, identified by means of an identifier and a description.
 * A simulation executes a model defined by means of different structures: 
 * <ul>
 * <li>{@link ResourceTypeEngine}</li>
 * <li>{@link ResourceEngine}</li>
 * <li>{@link WorkGroup}</li>
 * <li>{@link ActivityFlow}</li>
 * <li>{@link ElementType}</li>
 * <li>{@link Flow}</li>
 * <li>{@link TimeDrivenElementGenerator}</li>
 * </ul>
 * A simulation has an associated clock which starts in <tt>startTs</tt> and advances according 
 * to the events produced by the {@link Element}s, {@link ResourceEngine}s and {@link TimeDrivenElementGenerator}s. 
 * A "next-event" technique is used to determine the next timestamp to advance. A minimum 
 * {@link TimeUnit} determines the accuracy of the simulation's clock. The simulation ends when the 
 * simulation clock reaches the <tt>endTs</tt> timestamp or no more events are available.<br>
 * Depending on the specific implementation, a simulation can use one or more "worker" threads to 
 * execute the event's actions.
 * <p>
 * A user can interact with this Simulation by filling in some user methods that are activated in different
 * instants:
 * <ul>
 * <li>Just before the simulation starts {@link #init()}</li>
 * <li>Just after the simulation ends {@link #end()}</li>
 * <li>Just before the simulation clock advances {@link #beforeClockTick()}</li> 
 * <li>Just After the simulation clock advances {@link #afterClockTick()}</li> 
 * </ul> 
 * <p>
 * A simulation uses {@link InfoReceiver}s to show results. Those "listeners" can
 * be added by invoking the {@link #addInfoReceiver(InfoReceiver)} method. 
 * <p>
 * For debugging purposes, an {@link Output} can be associated to this simulation, thus
 * defining the destination for error and debug messages.
 * @author Iv�n Castilla Rodr�guez
 */
public abstract class SimulationEngine implements Identifiable, Debuggable {
	/** Simulation's identifier */
	protected final int id;
	/** The associated {@link Simulation} */
	protected final Simulation simul;
	
	/**
	 * Creates a new instance of Simulation
	 *
	 * @param id This simulation's identifier
	 * @param description A short text describing this simulation.
	 * @param unit This simulation's time unit
	 * @param startTs Timestamp of simulation's start expressed in Simulation Time Units
	 * @param endTs Timestamp of simulation's end expressed in Simulation Time Units
	 */
	public SimulationEngine(int id, Simulation simul) {
		this.id = id;
		this.simul = simul;
	}

	/**
	 * @return the simul
	 */
	public Simulation getSimulation() {
		return simul;
	}

	@Override
	public int getIdentifier() {
		return id;
	}

	@Override
	public void debug(String description) {
		Simulation.debug(description);
	}

	@Override
	public void error(String description) {
		Simulation.error(description);
	}

	@Override
	public boolean isDebugEnabled() {
		return Simulation.isDebugEnabled();
	}
	
	public abstract void initializeEngine();
	public abstract void simulationLoop();
	public abstract ResourceEngine getResourceEngineInstance(Resource modelRes);
	public abstract ElementEngine getElementEngineInstance(Element modelElem);
	public abstract ResourceList getResourceListInstance();
	public abstract ActivityManagerEngine getActivityManagerEngineInstance(ActivityManager modelAM);
	public abstract RequestResourcesEngine getRequestResourcesEngineInstance(RequestResourcesFlow reqFlow);
	public abstract MergeFlowEngine getMergeFlowEngineInstance(MergeFlow modelFlow);
	public abstract void addEvent(DiscreteEvent ev); 
	public abstract void addWait(DiscreteEvent ev); 
	/**
	 * Prints the current state of the simulation for debug purposes. Prints the current local 
	 * time, the contents of the future event list and the execution queue. 
	 */
	public abstract void printState();
    /**
     * Returns the current simulation time
     * @return The current simulation time
     */
	public abstract long getTs();
}

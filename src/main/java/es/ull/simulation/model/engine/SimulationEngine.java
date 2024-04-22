/*
 * Simulation.java
 *
 * Created on 8 de noviembre de 2005, 18:47
 */

package es.ull.simulation.model.engine;

import es.ull.simulation.inforeceiver.InfoReceiver;
import es.ull.simulation.model.ActivityManager;
import es.ull.simulation.model.IDebuggable;
import es.ull.simulation.model.DiscreteEvent;
import es.ull.simulation.model.Element;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.IIdentifiable;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.AbstractResourceList;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.IFlow;
import es.ull.simulation.model.flow.AbstractMergeFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow;
import es.ull.simulation.utils.Output;

/**
 * Main simulation class, identified by means of an identifier and a description.
 * A simulation executes a model defined by means of different structures: 
 * <ul>
 * <li>{@link ResourceTypeEngine}</li>
 * <li>{@link IResourceEngine}</li>
 * <li>{@link WorkGroup}</li>
 * <li>{@link ActivityFlow}</li>
 * <li>{@link ElementType}</li>
 * <li>{@link IFlow}</li>
 * <li>{@link TimeDrivenElementGenerator}</li>
 * </ul>
 * A simulation has an associated clock which starts in <tt>startTs</tt> and advances according 
 * to the events produced by the {@link Element}s, {@link IResourceEngine}s and {@link TimeDrivenElementGenerator}s. 
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
 * @author Iván Castilla Rodríguez
 */
public abstract class SimulationEngine implements IIdentifiable, IDebuggable {
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
	public abstract IResourceEngine getResourceEngineInstance(Resource modelRes);
	public abstract IElementEngine getElementEngineInstance(Element modelElem);
	public abstract AbstractResourceList getResourceListInstance();
	public abstract IActivityManagerEngine getActivityManagerEngineInstance(ActivityManager modelAM);
	public abstract IRequestResourcesEngine getRequestResourcesEngineInstance(RequestResourcesFlow reqFlow);
	public abstract IMergeFlowEngine getMergeFlowEngineInstance(AbstractMergeFlow modelFlow);
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

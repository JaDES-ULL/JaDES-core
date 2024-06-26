/**
 * 
 */
package es.ull.simulation.model.flow;

import java.util.ArrayList;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;

/**
 * An element that requests this IFlow has to wait until a special class (implementing the {@link Listener} interface) sends a signal. Useful for implementing
 * conditional waitings.  
 * @author Iván Castilla Rodríguez
 *
 */
public class WaitForSignalFlow extends AbstractSingleSuccessorFlow implements IInitializerFlow, IActionFlow {
	private final ArrayList<ElementInstance> lockedElements;
	private final Listener listener;
	private final String description;

	/**
	 * Creates the IFlow
	 * @param simul Simulation this IFlow belongs to
	 * @param listener The object that will be listening to this IFlow and that will send the signals to continue
	 */
	public WaitForSignalFlow(Simulation simul, String description, Listener listener) {
		super(simul);
		lockedElements = new ArrayList<ElementInstance>();
		this.listener = listener;
		this.description = description;
		listener.register(this);
	}

	@Override
	public void addPredecessor(IFlow predecessor) {
	}

	@Override
	public void request(ElementInstance ei) {
		if (!ei.wasVisited(this)) {
			if (ei.isExecutable()) {
				if (beforeRequest(ei)) {
					lockedElements.add(ei);
					listener.notifyArrival(this, ei);
				}
				else {
					ei.cancel(this);
					next(ei);
				}
			}
			else {
				ei.updatePath(this);
				next(ei);
			}
		} else
			ei.notifyEnd();
	}

	/**
	 * Notifies the IFlow that the specified {@link ElementInstance} can continue to the next step.
	 * @param ei Element instance that must continue
	 * @return True if the IFlow contained the specified element instance; false otherwise
	 */
	public boolean signal(ElementInstance ei) {
		if (lockedElements.remove(ei)) {
			next(ei);
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * A class capable to listen to this IFlow and send a signal to element instances within this IFlow
	 * @author Iván Castilla Rodríguez
	 *
	 */
	public static interface Listener {
		/**
		 * This method is invoked from the IFlow constructor. It can be used to let the listener know which {@link WaitForSignalFlow} is
		 * listening to 
		 * @param IFlow The IFlow this Listener is going to listen to
		 */
		void register(WaitForSignalFlow IFlow);
		/**
		 * This method is invoked from the {@link WaitForSignalFlow#request(ElementInstance)} method, and let the listener know that a new
		 * element instance has arrived at the IFlow
		 * @param IFlow The IFlow this element instance has arrived
		 * @param ei Element instance requesting the IFlow
		 */
		void notifyArrival(WaitForSignalFlow IFlow, ElementInstance ei);
	}
}

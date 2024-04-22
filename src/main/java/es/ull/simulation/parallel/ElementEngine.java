package es.ull.simulation.parallel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import es.ull.simulation.model.Element;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.flow.IFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow;
import es.ull.simulation.model.engine.IElementEngine;

/**
 * Represents case instances that make use of activity flows in order to carry out
 * their events.
 * TODO Comment
 * @author Iván Castilla Rodríguez
 */
public class ElementEngine extends AbstractEngineObject implements IElementEngine {
	/** Activity queues in which this element is. This list is used to notify the activities
	 * when the element becomes available. */
	protected final ArrayList<ElementInstance> inQueue;
	/** A structure to protect access to shared flows */
	protected final Map<IFlow, AtomicBoolean> protectedFlows;
    /** Access control */
    final private AtomicBoolean sem;
	/** The associated {@link Element} */
	private final Element modelElem;
    /** Flag that indicates if the element has finished its execution */
    final private AtomicBoolean endFlag;
	
	/**
	 * Creates a new element.
	 * @param id Element's identifier
	 * @param simul ParallelSimulationEngine object
	 * @param et Element type this element belongs to
	 * @param IFlow First step of this element's IFlow
	 */
	public ElementEngine(ParallelSimulationEngine simul, Element modelElem) {
		super(modelElem.getIdentifier(), simul, "E");
		this.modelElem = modelElem;
		protectedFlows = new HashMap<IFlow, AtomicBoolean>();
        sem = new AtomicBoolean(false);
        inQueue = new ArrayList<ElementInstance>();
        endFlag = new AtomicBoolean(false);
	}

	/**
	 * Returns the associated {@link Element}
	 * @return the associated {@link Element}
	 */
	public Element getModelElem() {
		return modelElem;
	}

    /**
     * Sends a "wait" signal to the semaphore.
     */    
    protected void waitSemaphore() {
    	while (!sem.compareAndSet(false, true));
    }
    
    /**
     * Sends a "continue" signal to the semaphore.
     */    
    protected void signalSemaphore() {
        sem.set(false);
    }
    
	@Override
    public void notifyEnd() {
    	if (!endFlag.getAndSet(true)) {
            simul.addEvent(modelElem.onDestroy(simul.getTs()));
        }
    }
    
	@Override
	public void incInQueue(ElementInstance fe) {
		synchronized(inQueue) {
			inQueue.add(fe);
		}
	}

	@Override
	public void decInQueue(ElementInstance fe) {
		synchronized(inQueue) {
			inQueue.remove(fe);
		}
	}

	@Override
	public void notifyAvailableElement() {
		synchronized(inQueue) {
			for (final ElementInstance fe : inQueue) {
	            final RequestResourcesFlow act = (RequestResourcesFlow) fe.getCurrentFlow();
				if (act.isInExclusiveActivity()) {
		            act.getManager().notifyAvailableElement(fe);
				}
			}
		}		
	}
	
	/**
	 * Acquires a semaphore associated to a specific IFlow
	 * @param IFlow The IFlow to be requested
	 */
	public void waitProtectedFlow(IFlow IFlow) {
		waitSemaphore();
		if (!protectedFlows.containsKey(IFlow)) {
			protectedFlows.put(IFlow, new AtomicBoolean(true));
			signalSemaphore();
		}
		else {
			signalSemaphore();
			final AtomicBoolean localBool = protectedFlows.get(IFlow);
			while (!localBool.compareAndSet(false, true));
		}
	}
	
	/**
	 * Releases a semaphore associated to a specific IFlow
	 * @param IFlow The IFlow to be requested
	 */
	public void signalProtectedFlow(IFlow IFlow) {
		protectedFlows.get(IFlow).set(false);
	}

	@Override
	public ElementInstanceEngine getElementInstance(ElementInstance ei) {
		return new ElementInstanceEngine((ParallelSimulationEngine) simul, ei);
	}
	
}

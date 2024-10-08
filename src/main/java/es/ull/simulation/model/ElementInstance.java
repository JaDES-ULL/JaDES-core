/**
 * 
 */
package es.ull.simulation.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.TreeSet;

import es.ull.simulation.info.ElementActionInfo;
import es.ull.simulation.info.ResourceInfo;
import es.ull.simulation.model.engine.ElementInstanceEngine;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.IFlow;
import es.ull.simulation.model.flow.IInitializerFlow;
import es.ull.simulation.model.flow.ReleaseResourcesFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow.ActivityWorkGroup;
import es.ull.simulation.model.flow.ITaskFlow;
import es.ull.simulation.utils.Prioritizable;

/**
 * Represents an instance of an element, so multiple instances of the same element can be active at
 * the same time.
 * There are three types of element instances, each one requiring a different method to be created:
 * <ol>
 * <li>Main instance. The element's main instance. Must be created by invoking the static method
 * {@link #getMainElementInstance(Element)}</li>
 * <li>Descendant thread</li>A thread created to carry out the inner flows of a structured IFlow.
 * To invoke, use: {@link #getDescendantElementInstance(IInitializerFlow)}</li>
 * <li>Subsequent thread</li>A thread created to carry out a new IFlow after a split.
 * To invoke, use: {@link #getSubsequentElementInstance(boolean, IFlow, WorkToken)}</li>
 * </ol><p>
 *  An instance has an associated token, which can be true or false. A false token is used
 *  only for synchronization purposes and doesn't execute task flows. 
 * @author Ivan Castilla Rodriguez
 *
 */
public class ElementInstance implements Prioritizable, Comparable<ElementInstance>, IIdentifiable {
	/** A string that identifies the instance */
	private final String description; 
    /** Element which carries out this IFlow. */    
    private final Element elem; 
    /** The parent element thread */
    protected final ElementInstance parent;
    /** The descendant element instances */
	protected final ArrayList<ElementInstance> descendants;
    /** Thread's initial IFlow */
    protected final IFlow initialFlow;
	/** A flag to indicate if the thread executes the IFlow or not */
	protected WorkToken token;
	/** The current IFlow the thread is in */
	protected IFlow currentFlow = null;
	/** The last IFlow the thread was in */
	protected IFlow lastFlow = null;
    /** The workgroup which is used to carry out this IFlow. If <code>null</code>, 
     * the IFlow has not been carried out. */
    protected ActivityWorkGroup executionWG = null;
	/** The arrival order of this element instance relatively to the rest of element instances 
	 * in the same activity manager. */
	protected int arrivalOrder;
	/** The simulation timestamp when this element instance was requested. */
	protected long arrivalTs = -1;
	/** The proportion of time left to finish the activity. Used in interruptible activities. */
	protected double remainingTask = 0.0;
	/** The engine with the specific functioning of the element instance */
	final private ElementInstanceEngine engine;
	
    /** 
     * Creates a new element instance. The constructor is private since it must be invoked from the 
     * <code>getInstance...</code> methods.
     * @param token An object containing the state of the thread  
     * @param elem Element owner of this thread
     * @param initialFlow The first IFlow to be executed by this thread
     * @param parent The parent thread, if this thread is included within a structured IFlow
     */
    private ElementInstance(final WorkToken token, final Element elem, 
							final IFlow initialFlow, final ElementInstance parent) {
    	this.token = token;
        this.elem = elem;
        this.parent = parent;
        descendants = new ArrayList<ElementInstance>();
        if (parent != null)
        	parent.addDescendant(this);
        this.initialFlow = initialFlow;
        this.engine = elem.getEngine().getElementInstance(this);
        this.description = elem.toString() + "-" + engine.getIdentifier();
    }

    /**
	 * @return the engine
	 */
	public ElementInstanceEngine getEngine() {
		return engine;
	}

	@Override
	public int getIdentifier() {
		return engine.getIdentifier();
	}
	
	/**
     * Returns the priority of the element owner of this IFlow
     * @return The priority of the associated element.
     */
    @Override
    public int getPriority() {
    	return elem.getPriority();
    }

	/**
	 * Sets the IFlow currently executed by this FlowExecutor 
	 * @param f The IFlow to be performed
	 */
	public void setCurrentFlow(final IFlow f) {
    	currentFlow = f;
		executionWG = null;
		arrivalTs = -1;
		if (f instanceof RequestResourcesFlow) {
			remainingTask = 1.0;
			if (parent.currentFlow instanceof ActivityFlow) {
				if (parent.remainingTask > 0.0)
					remainingTask = parent.remainingTask;
			}
		}
		else {
			remainingTask = 0.0;  			
		}
	}

    /**
     * Returns the IFlow being performed.
	 * @return The IFlow being performed.
	 */
	public IFlow getCurrentFlow() {
		return currentFlow;
	}

	/**
	 * Returns the workgroup that the element instance is using to execute a resource handler IFlow
	 * @return the workgroup that the element instance is using to execute a resource handler IFlow
	 */
	public ActivityWorkGroup getExecutionWG() {
		return executionWG;
	}

	/**
	 * When the single IFlow can be carried out, sets the workgroup used to
	 * carry out the activity.
	 * @param executionWG the workgroup which is used to carry out this IFlow.
	 */
	public void setExecutionWG(final ActivityWorkGroup executionWG) {
		this.executionWG = executionWG;
	}

    /**
     * Notifies the parent this thread has finished.
     */
    public void notifyEnd() {
    	if (parent != null) {
    		parent.removeDescendant(this);
    		if ((parent.descendants.size() == 0) && (parent.currentFlow != null))
    			((ITaskFlow)parent.currentFlow).finish(parent);
    	}
    }
    
    /**
     * Adds a thread to the list of descendants.
     * @param wThread Descendant thread
     */
	private void addDescendant(final ElementInstance wThread) {
		descendants.add(wThread);
	}

	/**
	 * Removes a thread from the list of descendants. If it's the last thread of an element,
	 * the element has to be notified and finished.
	 * @param wThread Descendant thread
	 */
	private void removeDescendant(final ElementInstance wThread) {
		descendants.remove(wThread);
		if (parent == null && descendants.size() == 0)
			elem.notifyEnd();
	}

	/**
	 * Returns <code>true</code> if this thread is valid.
	 * @return <code>True</code> if this thread is valid; false otherwise.
	 */
	public boolean isExecutable() {
		return token.isExecutable();
	}

	/**
	 * Changes the state of this thread to not valid and restarts the path of visited flows.
	 * @param startPoint The initial IFlow to control infinite loops with not valid threads. 
	 */
	public void cancel(final IFlow startPoint) {
		token.reset();
		token.addFlow(startPoint);
	}

	/**
	 * Returns the last IFlow visited by this FlowExecutor
	 * @return the last IFlow visited by this FlowExecutor
	 */
	public IFlow getLastFlow() {
		return lastFlow;
	}

	/**
	 * Sets the last IFlow visited by this thread.
	 * @param lastFlow The lastFlow visited by this thread
	 */
	public void setLastFlow(final IFlow lastFlow) {
		this.lastFlow = lastFlow;
	}

	/**
     * Returns the element performing this single IFlow.
     * @return The element performing this single IFlow
     */
    public Element getElement() {
        return elem;
    }
    
    /**
     * Gets the parent element thread.
     * @return The parent element thread.
     */
	public ElementInstance getParent() {
		return parent;
	}

	/**
	 * Returns the main instance of the element. This is a "valid" instance and has no parent.
	 * @param elem Element owner of this instance
	 * @return the main instance of the element
	 */
	public static ElementInstance getMainElementInstance(final Element elem) {
		return new ElementInstance(new WorkToken(true), elem, elem.getFlow(), null);
	}

	/**
	 * Returns a new instance of an element which carries out the inner subflow of a structured IFlow. 
	 * The current instance is the parent of the newly created child instance. 
	 * @return A new instance of an element created to carry out the inner subflow of a structured IFlow
	 */
	public ElementInstance getDescendantElementInstance(final IInitializerFlow newFlow) {
		assert isExecutable() : "Invalid parent to create descendant element instance"; 
		return new ElementInstance(new WorkToken(true), elem, newFlow, this);
	}

	/**
	 * Returns a new instance of an element which carries out a new IFlow after a split IFlow
	 * @param executable Indicates if the instance to be created has to be valid or not
	 * @param newFlow The IFlow associated to the new instance 
	 * @param token The token to be cloned in case the current instance is not valid and the token is also not valid. 
	 * @return A new instance of an element created to carry out a new IFlow after a split IFlow
	 */
	public ElementInstance getSubsequentElementInstance(final boolean executable, final IFlow newFlow,
														final WorkToken token) {
		final WorkToken newToken;
		if (!executable)
			if (!token.isExecutable())
				newToken = new WorkToken(token);
			else
				newToken = new WorkToken(false, newFlow);
		else
			newToken = new WorkToken(true);
		return new ElementInstance(newToken, elem, newFlow, parent);
	}

	/**
	 * Adds a new visited IFlow to the list.
	 * @param IFlow New visited IFlow
	 */
	public void updatePath(final IFlow IFlow) {
		token.addFlow(IFlow);
	}

	/**
	 * Returns this thread's current token.
	 * @return This thread's current token
	 */
	public WorkToken getToken() {
		return token;
	}
	
	/**
	 * Returns true if the specified IFlow was already visited from this thread.
	 * @param IFlow IFlow to be checked.
	 * @return True if the specified IFlow was already visited from this thread; false otherwise.
	 */
	public boolean wasVisited (final IFlow IFlow) {
		return token.wasVisited(IFlow);
	}

	/**
	 * Returns the order this thread occupies among the rest of element instances.
	 * @return the order of arrival of this element instance to request the activity
	 */
	public int getArrivalOrder() {
		return arrivalOrder;
	}

	/**
	 * Sets the order this thread occupies among the rest of element instances.
	 * @param arrivalOrder the order of arrival of this element instance to request the activity
	 */
	public void setArrivalOrder(final int arrivalOrder) {
		this.arrivalOrder = arrivalOrder;
	}

	/**
	 * Returns the timestamp when this element instance arrives to request the current single IFlow.
	 * @return the timestamp when this element instance arrives to request the current single IFlow
	 */
	public long getArrivalTs() {
		return arrivalTs;
	}

	/**
	 * Sets the timestamp when this element instance arrives to request the current single IFlow.
	 * @param arrivalTs the timestamp when this element instance arrives to request the current single IFlow
	 */
	public void setArrivalTs(final long arrivalTs) {
		this.arrivalTs = arrivalTs;
	}

    /**
     * Catch the resources needed for each resource type to carry out an activity.
	 * @param solution Tentative solution with booked resources
     * @return The minimum availability timestamp of the taken resources 
     */
	public long catchResources(final ArrayDeque<Resource> solution) {
		final RequestResourcesFlow reqFlow = (RequestResourcesFlow)currentFlow;
		// Add booked resources to the element
		elem.seizeResources(reqFlow, this, solution);
    	long auxTs = Long.MAX_VALUE;
    	for (Resource res : solution) {
    		auxTs = Math.min(auxTs, res.catchResource(this));;
            res.getCurrentResourceType().trace("Resource taken\t" + res + "\t" + getElement());
    	}
    	engine.notifyResourcesAcquired();
    	final long ts = elem.getTs();
		elem.getSimulation().notifyInfo(new ElementActionInfo(elem.getSimulation(), this, elem, reqFlow,
				executionWG, solution, ElementActionInfo.Type.ACQ, ts));
		elem.trace("Resources acquired\t" + this + "\t" + reqFlow.getDescription());			
		reqFlow.afterAcquire(this);
		long delay = Math.round(executionWG.getDurationSample(elem) * remainingTask);
		auxTs -= ts;
		if (delay > 0) {
			if (remainingTask == 1.0) {
				elem.getSimulation().notifyInfo(new ElementActionInfo(elem.getSimulation(), this, elem, reqFlow,
						executionWG, null, ElementActionInfo.Type.START, ts));
				elem.trace("Start delay\t" + this + "\t" + reqFlow.getDescription());
			}
			else {
				elem.getSimulation().notifyInfo(new ElementActionInfo(elem.getSimulation(), this, elem, reqFlow,
						executionWG, null, ElementActionInfo.Type.RESACT, ts));
				elem.trace("Continues\t" + this + "\t" + reqFlow.getDescription());			
			}
			// The required time for finishing the activity is reduced (useful only for interruptible activities)
			if (reqFlow.partOfInterruptible() && (delay - auxTs > 0.0)) {
				remainingTask = (delay - auxTs) * remainingTask / (double)delay;
				delay = auxTs;
			}
			else {
				remainingTask = 0.0;
			}
		}
		else {
			remainingTask = 0.0;
		}
		return delay;
	}
	
	public void startDelay(final long delay) {
		elem.addFinishEvent(delay + elem.getTs(), (ITaskFlow)currentFlow, this);
	}
	
	/**
	 * Releases the previously seized resources
	 * @return The released resources
	 */
	public ArrayDeque<Resource> releaseCaughtResources() {
        final TreeSet<ActivityManager> amList = new TreeSet<ActivityManager>();
		final ReleaseResourcesFlow relFlow = (ReleaseResourcesFlow)currentFlow;
		
		final ArrayDeque<Resource> resources = elem.releaseResources(relFlow, this);
        // Generate unavailability periods.
        for (Resource res : resources) {
        	final long cancellationDuration = relFlow.getResourceCancellation(res.getCurrentResourceType(), this);
        	if (cancellationDuration > 0) {
				final long actualTs = elem.getTs();
				res.setNotCanceled(false);
				elem.getSimulation().notifyInfo(new ResourceInfo(elem.getSimulation(), res,
						res.getCurrentResourceType(), ResourceInfo.Type.CANCELON, actualTs));
				res.generateCancelPeriodOffEvent(actualTs, cancellationDuration);
			}
			elem.trace("Returned " + res);
        	// The resource is freed
        	if (res.releaseResource(this)) {
        		// The activity managers involved are included in the list
        		for (ActivityManager am : res.getCurrentManagers()) {
        			amList.add(am);
        		}
        	}
        }
		for (ActivityManager am : amList) {
			am.notifyResource();
		}
        return resources;
	}
   
	/**
	 * Sends the information and executes the tasks required when the delay of a request flow finishes.
	 * @param f The request flow that has been delayed
	 */
    public void endDelay(final RequestResourcesFlow f) {
		// Checks time and not percentage of time to avoid rounding errors
		if (Math.round(executionWG.getDurationSample(elem) * remainingTask) == 0) {
			remainingTask = 0.0;
			elem.getSimulation().notifyInfo(new ElementActionInfo(elem.getSimulation(), this, elem, f,
					executionWG, null, ElementActionInfo.Type.END, elem.getTs()));
			elem.trace("Finishes\t" + this + "\t" + f.getDescription());
			f.afterFinalize(this);
		}
		else {
			elem.getSimulation().notifyInfo(new ElementActionInfo(elem.getSimulation(), this, elem, f,
					executionWG, null, ElementActionInfo.Type.INTACT, elem.getTs()));
			elem.trace("Finishes part of \t" + this + "\t" + f.getDescription() + "\t" +
						remainingTask * 100 + "% Left");
			// Notifies the parent workthread that the activity was interrupted
		}
		parent.remainingTask = remainingTask;
    }

    public boolean wasInterrupted(final ActivityFlow f) {
		// It was an interruptible activity and it was interrupted
		return (remainingTask > 0.0);    	
    }

	@Override
	public int compareTo(final ElementInstance o) {
		final int id1 = engine.getIdentifier();
		final int id2 = o.engine.getIdentifier();
		if (id1 > id2)
			return -1;
		if (id1 < id2)
			return 1;
		return 0;
	}
    
	@Override
	public String toString() {
		return description;
	}
}

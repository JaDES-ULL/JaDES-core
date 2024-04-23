package es.ull.simulation.parallel;

import java.util.ArrayDeque;

import es.ull.simulation.model.ActivityManager;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.flow.RequestResourcesFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow.ActivityWorkGroup;
import es.ull.simulation.model.engine.IRequestResourcesEngine;

/**
 * A task which could be carried out by a WorkItem and requires certain amount and
 * type of {@link Resource resources} to be performed.  An activity is characterized by its 
 * priority and a set of {@link WorkGroup Workgroups} (WGs). Each WG represents a combination 
 * of {@link ResourceType resource types} required to carry out the activity.<p>
 * Each activity is attached to an {@link ActivityManager}, which manages the access to the activity.<p>
 * An activity is potentially feasible if there is no proof that there are not enough resources
 * to perform it. An activity is feasible if it's potentially feasible and there is at least one
 * WG with enough available resources to perform the activity. The WGs are checked in 
 * order according to some priorities, and can also have an associated condition which must be 
 * accomplished to be selected.<p>
 * An activity can be requested (that is, check if the activity is feasible) by a valid 
 * WorkItem.
 * If the activity is not feasible, the work item is added to a queue until new resources are 
 * available. If the activity is feasible, the work item "carries out" the activity, that is, 
 * catches the resources needed to perform the activity. Whenever it is determined that the 
 * activity has finished, the work item releases the resources previously caught.<p>
 * An activity can also define cancellation periods for each one of the resource types it uses. 
 * If a work item takes a resource belonging to one of the cancellation periods of the activity, this
 * resource can't be used during a period of time after the activity finishes.
 * @author Carlos Martín Galán
 */
public class RequestResourcesEngine extends AbstractEngineObject implements IRequestResourcesEngine {
    /** Total amount of {@link ElementInstance} waiting for carrying out this activity */
    protected int queueSize = 0;
    /** The associated {@link RequestResourcesFlow} */
    final protected RequestResourcesFlow modelReq;

	/**
	 * Constructs a new RequestResourcesEngine object.
	 * RequestResourcesEngine represents an activity that can be carried out by a WorkItem and requires a certain
	 * amount and type of resources to be performed. It manages the queue of ElementInstances waiting to carry out
	 * this activity and is associated with a specific RequestResourcesFlow.
	 *
	 * @param simul        The ParallelSimulationEngine where this activity is used.
	 * @param modelReq     The associated RequestResourcesFlow object representing this activity.
	 */
    public RequestResourcesEngine(ParallelSimulationEngine simul, RequestResourcesFlow modelReq) {
    	super(modelReq.getIdentifier(), simul,"REQ");
        this.modelReq = modelReq;
    }

	public RequestResourcesFlow getModelReqFlow() {
		return modelReq;
	}

	@Override
	public synchronized void queueAdd(ElementInstance fe) {
        modelReq.getManager().queueAdd(fe);
    	queueSize++;
		fe.getElement().incInQueue(fe);
		modelReq.inqueue(fe);
    }
    
	@Override
	// TODO: Check why it's not synchronized
    public void queueRemove(ElementInstance fe) {
		modelReq.getManager().queueRemove(fe);
    	queueSize--;
		fe.getElement().decInQueue(fe);
    }

    /**
     * Returns how many element instances are waiting to carry out this activity. 
     * @return The size of this activity's queue
     */
    public int getQueueSize() {
    	return queueSize;    	
    }

	@Override
	public boolean checkWorkGroup(ArrayDeque<Resource> solution, ActivityWorkGroup wg, ElementInstance ei) {
		final ElementInstanceEngine engine = (ElementInstanceEngine)ei.getEngine();
    	engine.resetConflictZone();
    	if (!wg.getCondition().check(ei))
    		return false;
    	
    	int ned[] = wg.getNeeded().clone();
    	if (ned.length == 0) { // Infinite resources
    		engine.waitConflictSemaphore();	// FIXME: unneeded, but fails if removed
    		return true; 
    	}
        final int []pos = {0, -1}; // "Start" position
        
        // B&B algorithm for finding a solution
        while (wg.findSolution(solution, pos, ned, ei)) {
    		engine.waitConflictSemaphore();
    		// All the resources taken for the solution only appears in this AM 
        	if (!engine.isConflictive()) 
	            return true;
        	// Any one of the resources taken for the solution also appears in a different AM 
        	else {
	        	modelReq.debug("Possible conflict. Recheck is needed " + ei.getElement());
        		// A recheck is needed
	        	boolean checked = true; 
	    		for (Resource res : solution)
	    			if (!((ResourceEngine)res.getEngine()).checkSolution(engine)) {
	    				checked = false;
	    			}
        		if (checked) {
        			return true;
        		}
        		else {
        			// Resets the solution
        			engine.signalConflictSemaphore();
        			while (!solution.isEmpty()) {
        				final Resource res = solution.peek();
        				res.removeFromSolution(solution, ei);
        			}
        			ned = wg.getNeeded().clone();
        			pos[0] = 0;
        			pos[1] = -1;
        		}
        	}
        }
        return false;
	}

}

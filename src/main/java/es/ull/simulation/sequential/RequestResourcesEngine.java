/**
 * 
 */
package es.ull.simulation.sequential;

import java.util.ArrayDeque;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.flow.RequestResourcesFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow.ActivityWorkGroup;
import es.ull.simulation.model.engine.IRequestResourcesEngine;
/**
 * @author Iván Castilla Rodríguez
 *
 */
public class RequestResourcesEngine extends AbstractEngineObject implements IRequestResourcesEngine {
    /** Total of work items waiting for carrying out this activity */
    protected int queueSize = 0;
    /** The associated {@link RequestResourcesFlow} */
    final protected RequestResourcesFlow modelReq;

	/**
	 * Constructs a new RequestResourcesEngine object.
	 * RequestResourcesEngine represents an engine responsible for managing activities that require specific
	 * resources in a sequential simulation. It tracks the total number of work items waiting to carry out this
	 * activity and is associated with a specific RequestResourcesFlow.
	 *
	 * @param simul       The SequentialSimulationEngine to which this request resources engine belongs.
	 * @param modelReq    The associated RequestResourcesFlow object representing this request resources engine.
	 */
	public RequestResourcesEngine(SequentialSimulationEngine simul, RequestResourcesFlow modelReq) {
		super(modelReq.getIdentifier(), simul, "REQ");
		this.modelReq = modelReq;
	}

	public RequestResourcesFlow getModelReqFlow() {
		return modelReq;
	}
	
	@Override
	public void queueAdd(ElementInstance fe) {
        modelReq.getManager().queueAdd(fe);
    	queueSize++;
		fe.getElement().incInQueue(fe);
		modelReq.inqueue(fe);
    }
    
	@Override
    public void queueRemove(ElementInstance fe) {
		modelReq.getManager().queueRemove(fe);
    	queueSize--;
		fe.getElement().decInQueue(fe);
    }

    /**
     * Returns how many element instances are waiting to carry out this activity. 
     * @return the size of this activity's queue
     */
    public int getQueueSize() {
    	return queueSize;    	
    }

	@Override
	public boolean checkWorkGroup(ArrayDeque<Resource> solution, ActivityWorkGroup wg, ElementInstance ei) {
    	if (!wg.getCondition().check(ei))
    		return false;
    	int ned[] = wg.getNeeded().clone();
    	if (ned.length == 0) // Infinite resources
    		return true; 
        int []pos = {0, -1}; // "Start" position
        
        // B&B algorithm for finding a solution
        return wg.findSolution(solution, pos, ned, ei);
	}

}

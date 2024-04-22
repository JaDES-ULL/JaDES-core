package es.ull.simulation.model.flow;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;


/**
 * A structured loop flow which resembles a while-do loop. A precondition is
 * checked before executing the internal flow. If the postcondition is false,
 * this flow finishes. 
 * @author ycallero
 */
public class WhileDoFlow extends StructuredLoopFlow {
	/** Condition which controls the loop operation. */
	protected final AbstractCondition<ElementInstance> cond;
	
	/**
	 * Create a new WhileDoFlow.
	 * @param initialSubFlow First step of the internal subflow
	 * @param finalSubFlow Last step of the internal subflow
	 * @param postCondition Break loop condition.
 	 */
	public WhileDoFlow(Simulation model, InitializerFlow initialSubFlow, FinalizerFlow finalSubFlow,
					   AbstractCondition<ElementInstance> postCondition) {
		super(model, initialSubFlow, finalSubFlow);
		cond = postCondition;
	}

	/**
	 * Create a new WhileDoFlow.
	 * @param subFlow A unique flow defining an internal subflow
	 * @param postCondition Break loop condition.
 	 */
	public WhileDoFlow(Simulation model, TaskFlow subFlow, AbstractCondition<ElementInstance> postCondition) {
		this(model, subFlow, subFlow, postCondition);
	}

	/** 
	 * Returns the condition which controls the loop operation.
	 * @return The condition which controls the loop operation
	 */
	public AbstractCondition<ElementInstance> getCondition() {
		return cond;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ull.simulation.Flow#request(com.ull.simulation.FlowExecutor)
	 */
	public void request(ElementInstance wThread) {
		if (!wThread.wasVisited(this)) {
			if (wThread.isExecutable()) {
				if (beforeRequest(wThread)) {
					finish(wThread);
				}
				else {
					wThread.cancel(this);
					next(wThread);				
				}
			}
			else {
				wThread.updatePath(this);
				next(wThread);
			}
		} else
			wThread.notifyEnd();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ull.simulation.TaskFlow#finish(com.ull.simulation.FlowExecutor)
	 */
	public void finish(ElementInstance wThread) {
		// The loop condition is checked
		if (cond.check(wThread)) {
			wThread.getElement().addRequestEvent(initialFlow, wThread.getDescendantElementInstance(initialFlow));
		} else {
			super.finish(wThread);
		}
	}

}


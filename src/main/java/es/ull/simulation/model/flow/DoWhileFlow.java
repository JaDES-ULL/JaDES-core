package es.ull.simulation.model.flow;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;


/**
 * A structured loop flow which resembles a do-while loop. The internal flow
 * is executed the first time and then the postcondition is checked. If the
 * postcondition is true, the internal flow is executed again; if not, this
 * flow finishes. 
 * @author ycallero
 */
public class DoWhileFlow extends StructuredLoopFlow {
	/** Condition which controls the loop operation. */
	protected final AbstractCondition<ElementInstance> cond;
	
	/**
	 * Create a new DoWhileFlow.
	 * @param initialSubFlow First step of the internal subflow
	 * @param finalSubFlow Last step of the internal subflow
	 * @param postCondition Break loop condition.
 	 */
	public DoWhileFlow(Simulation model, InitializerFlow initialSubFlow, FinalizerFlow finalSubFlow,
					   AbstractCondition<ElementInstance> postCondition) {
		super(model, initialSubFlow, finalSubFlow);
		cond = postCondition;
	}

	/**
	 * Create a new DoWhileFlow.
	 * @param subFlow A unique flow defining an internal subflow
	 * @param postCondition Break loop condition.
 	 */
	public DoWhileFlow(Simulation model, TaskFlow subFlow, AbstractCondition<ElementInstance> postCondition) {
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
	 * @see com.ull.simulation.TaskFlow#finish(com.ull.simulation.FlowExecutor)
	 */
	public void finish(ElementInstance wThread) {
		if (cond.check(wThread)) {
			wThread.getElement().addRequestEvent(initialFlow, wThread.getDescendantElementInstance(initialFlow));
		} else {
			super.finish(wThread);
		}
	}

}
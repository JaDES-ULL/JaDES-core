/**
 * 
 */
package com.ull.simulation.model.flow;

import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.Simulation;


/**
 * Creates an OR flow which allows all the true incoming branches to pass. 
 * Meets the Multi-Merge pattern (WFP8).
 * @author Iv�n Castilla Rodr�guez
 */
public class MultiMergeFlow extends ORJoinFlow {

	/**
	 * Creates a new MultiMergeFlow.
	 */
	public MultiMergeFlow(Simulation model) {
		super(model);
	}
	
	/* (non-Javadoc)
	 * @see com.ull.simulation.MergeFlow#canPass(com.ull.simulation.FlowExecutor)
	 */
	@Override
	protected boolean canPass(ElementInstance wThread) {
		return wThread.isExecutable();
	}
}

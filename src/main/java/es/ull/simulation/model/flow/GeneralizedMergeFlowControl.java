/**
 * 
 */
package com.ull.simulation.model.flow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.WorkToken;

/**
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class GeneralizedMergeFlowControl extends MergeFlowControl {
	protected Map<Flow, LinkedList<WorkToken>> incBranches;

	/**
	 * @param flow
	 */
	public GeneralizedMergeFlowControl(MergeFlow flow, Map<Flow, LinkedList<WorkToken>> control) {
		super(flow);
		incBranches = control;
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.MergeFlowControl#arrive(com.ull.simulation.FlowExecutor)
	 */
	@Override
	public void arrive(ElementInstance wThread) {
		// New incoming branch
		if (!incBranches.containsKey(wThread.getLastFlow())) {
			incBranches.put(wThread.getLastFlow(), new LinkedList<WorkToken>());
			if (wThread.isExecutable())
				trueChecked++;
			else
				outgoingFalseToken.addFlow(wThread.getToken().getPath());
		}
		// The new incoming branch is added
		incBranches.get(wThread.getLastFlow()).add(wThread.getToken());
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.MergeFlowControl#canReset(int)
	 */
	@Override
	public boolean canReset(int checkValue) {
		return (incBranches.size() == checkValue);
	}

	@Override
	public boolean reset() {
		super.reset();
		Iterator<Map.Entry<Flow, LinkedList<WorkToken>>> iter = incBranches.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Flow, LinkedList<WorkToken>> entry = iter.next();
			entry.getValue().removeFirst();
			if (!entry.getValue().isEmpty()) {
				WorkToken token = entry.getValue().peek();
				if (token.isExecutable())
					trueChecked++;
				else
					outgoingFalseToken.addFlow(token.getPath());
			}
			else
				iter.remove();
		}
		return incBranches.isEmpty();
	}
}

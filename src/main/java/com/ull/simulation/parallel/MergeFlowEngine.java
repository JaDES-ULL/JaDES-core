/**
 * 
 */
package com.ull.simulation.parallel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import com.ull.simulation.model.Element;
import com.ull.simulation.model.WorkToken;
import com.ull.simulation.model.engine.EngineObject;
import com.ull.simulation.model.engine.SimulationEngine;
import com.ull.simulation.model.flow.Flow;
import com.ull.simulation.model.flow.MergeFlow;
import com.ull.simulation.model.flow.MergeFlowControl;

/**
 * @author Iv�n Castilla
 *
 */
public class MergeFlowEngine extends EngineObject implements com.ull.simulation.model.engine.MergeFlowEngine {
	final private MergeFlow modelFlow;
	
	/**
	 * @param simul
	 * @param objTypeId
	 */
	public MergeFlowEngine(SimulationEngine simul, MergeFlow modelFlow) {
		super(modelFlow.getIdentifier(), simul, modelFlow.getObjectTypeIdentifier());
		this.modelFlow = modelFlow;
	}

	/**
	 * @return the modelFlow
	 */
	public MergeFlow getModelFlow() {
		return modelFlow;
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.model.engine.MergeFlowEngine#getControlStructureInstance()
	 */
	@Override
	public Map<Element, MergeFlowControl> getControlStructureInstance() {
		return Collections.synchronizedSortedMap(new TreeMap<Element, MergeFlowControl>());
	}

	@Override
	public Map<Flow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance() {
		return Collections.synchronizedSortedMap(new TreeMap<Flow, LinkedList<WorkToken>>());
	}

}

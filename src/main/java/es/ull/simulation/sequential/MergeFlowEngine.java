/**
 * 
 */
package es.ull.simulation.sequential;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import es.ull.simulation.model.Element;
import es.ull.simulation.model.WorkToken;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.engine.SimulationEngine;
import es.ull.simulation.model.flow.Flow;
import es.ull.simulation.model.flow.MergeFlow;
import es.ull.simulation.model.flow.MergeFlowControl;
import es.ull.simulation.model.engine.MergeFlowEngine;

/**
 * @author Iv�n Castilla
 *
 */
public class MergeFlowEngine extends AbstractEngineObject implements MergeFlowEngine {
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
		return new TreeMap<Element, MergeFlowControl>();
	}

	@Override
	public Map<Flow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance() {
		return new TreeMap<Flow, LinkedList<WorkToken>>();
	}

}

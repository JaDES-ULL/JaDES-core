/**
 * 
 */
package es.ull.simulation.parallel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import es.ull.simulation.model.Element;
import es.ull.simulation.model.WorkToken;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.engine.SimulationEngine;
import es.ull.simulation.model.flow.IFlow;
import es.ull.simulation.model.flow.AbstractMergeFlow;
import es.ull.simulation.model.flow.MergeFlowControl;
import es.ull.simulation.model.engine.IMergeFlowEngine;
/**
 * @author Iv�n Castilla
 *
 */
public class MergeFlowEngine extends AbstractEngineObject implements IMergeFlowEngine {
	final private AbstractMergeFlow modelFlow;
	
	/**
	 * @param simul
	 * @param objTypeId
	 */
	public MergeFlowEngine(SimulationEngine simul, AbstractMergeFlow modelFlow) {
		super(modelFlow.getIdentifier(), simul, modelFlow.getObjectTypeIdentifier());
		this.modelFlow = modelFlow;
	}

	/**
	 * @return the modelFlow
	 */
	public AbstractMergeFlow getModelFlow() {
		return modelFlow;
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.model.engine.IMergeFlowEngine#getControlStructureInstance()
	 */
	@Override
	public Map<Element, MergeFlowControl> getControlStructureInstance() {
		return Collections.synchronizedSortedMap(new TreeMap<Element, MergeFlowControl>());
	}

	@Override
	public Map<IFlow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance() {
		return Collections.synchronizedSortedMap(new TreeMap<IFlow, LinkedList<WorkToken>>());
	}

}

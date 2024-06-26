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
 * @author Iván Castilla Rodríguez
 *
 */
public class MergeFlowEngine extends AbstractEngineObject implements IMergeFlowEngine {
	final private AbstractMergeFlow modelFlow;

	/**
	 * Constructs a new MergeFlowEngine object.
	 * MergeFlowEngine represents the engine responsible for managing the merging of flow instances in a simulation.
	 * It is associated with a specific AbstractMergeFlow object.
	 *
	 * @param simul      The SimulationEngine object to which this merge flow engine belongs.
	 * @param modelFlow  The associated AbstractMergeFlow object.
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

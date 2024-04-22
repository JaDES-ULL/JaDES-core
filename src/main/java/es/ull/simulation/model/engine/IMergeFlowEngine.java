/**
 * 
 */
package es.ull.simulation.model.engine;

import java.util.LinkedList;
import java.util.Map;

import es.ull.simulation.model.Element;
import es.ull.simulation.model.WorkToken;
import es.ull.simulation.model.flow.IFlow;
import es.ull.simulation.model.flow.MergeFlowControl;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public interface IMergeFlowEngine {
	Map<Element, MergeFlowControl> getControlStructureInstance();
	Map<IFlow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance();
	
}

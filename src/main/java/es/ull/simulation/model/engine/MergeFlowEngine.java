/**
 * 
 */
package es.ull.simulation.model.engine;

import java.util.LinkedList;
import java.util.Map;

import es.ull.simulation.model.Element;
import es.ull.simulation.model.WorkToken;
import es.ull.simulation.model.flow.Flow;
import es.ull.simulation.model.flow.MergeFlowControl;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public interface MergeFlowEngine {
	Map<Element, MergeFlowControl> getControlStructureInstance();
	Map<Flow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance();
	
}

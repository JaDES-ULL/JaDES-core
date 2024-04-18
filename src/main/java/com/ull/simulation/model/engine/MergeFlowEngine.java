/**
 * 
 */
package com.ull.simulation.model.engine;

import java.util.LinkedList;
import java.util.Map;

import com.ull.simulation.model.Element;
import com.ull.simulation.model.WorkToken;
import com.ull.simulation.model.flow.Flow;
import com.ull.simulation.model.flow.MergeFlowControl;

/**
 * @author Iv�n Castilla Rodr�guez
 *
 */
public interface MergeFlowEngine {
	Map<Element, MergeFlowControl> getControlStructureInstance();
	Map<Flow, LinkedList<WorkToken>> getGeneralizedBranchesControlInstance();
	
}

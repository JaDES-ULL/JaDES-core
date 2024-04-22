package es.ull.simulation.model.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.condition.TrueCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;

/**
/**
 * A {@link MultipleSuccessorFlow} whose successors are conditioned, that is, the successor
 * can only be activated if certain condition is met. When adding successors, if no condition 
 * is indicated, it is supposed to be <tt>true</tt>.
 * @author Yeray Callero
 *
 */
public abstract class ConditionalFlow extends MultipleSuccessorFlow {
	/** Condition list associated to the successor list. */
	protected final ArrayList<AbstractCondition<ElementInstance>> conditionList;
	
	/**
	 * Creates a new ConditionalFlow.
	 */
	public ConditionalFlow(Simulation model) {
		super(model);
		conditionList = new ArrayList<>();
	}

	/**
	 * @return the conditionList
	 */
	public ArrayList<AbstractCondition<ElementInstance>> getConditionList() {
		return conditionList;
	}

	/**
	 * Adds a conditioned flow's successor. The associated condition is set to true by default.
	 * This method must invoke <code>successor.addPredecessor</code> to build the graph properly. 
	 * @param successor This flow's successor.
	 */
	@Override
	public Flow link(Flow successor) {
		return link(successor, new TrueCondition<ElementInstance>());
	}
	
	/**
	 * Adds a conditioned flow's successor. 
	 * @param successor This flow's successor
	 * @param cond The condition that has to be met to invoke the successor
	 */
	public Flow link(Flow successor, AbstractCondition<ElementInstance> cond) {
		super.link(successor);
		conditionList.add(cond);
		return successor;
	}

	/**
	 * Adds a collection of conditioned flow's successors. The associated condition is set to 
	 * true by default.
	 * @param succList This flow's successors
	 */
	@Override
	public void link(Collection<Flow> succList) {
		link(succList, Collections.nCopies(succList.size(), new TrueCondition<ElementInstance>()));
	}

	/**
	 * Adds a collection of conditioned flow's successors. 
	 * Size of <code>succList</code> and <code>condList</code> must agree. 
	 * @param succList This flow's successors
	 * @param condList The conditions attached to each successor
	 */
	public void link(Collection<Flow> succList, Collection<AbstractCondition<ElementInstance>> condList) {
		super.link(succList);
		conditionList.addAll(condList);
	}
	
}

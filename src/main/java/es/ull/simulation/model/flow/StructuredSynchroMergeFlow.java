package es.ull.simulation.model.flow;

import java.util.TreeSet;

import es.ull.simulation.condition.Condition;
import es.ull.simulation.condition.TrueCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;

/**
 * A {@link StructuredFlow} whose initial step is a {@link MultiChoiceFlow} and whose final step
 * is a {@link SynchronizationFlow}. Meets the Structured Synchronization pattern (WFP7). 
 * @author Yeray Callero
 */
public class StructuredSynchroMergeFlow extends PredefinedStructuredFlow {
	
	/**
	 * Create a new StructuredSynchroMergeMetaFlow.
	 */
	public StructuredSynchroMergeFlow(Simulation model) {
		super(model);
		initialFlow = new MultiChoiceFlow(model);
		initialFlow.setParent(this);
		finalFlow = new SynchronizationFlow(model);
		finalFlow.setParent(this);
	}

	/**
	 * Variation of <code>addBranch</code> which allows to indicate a condition
	 * @param branch A unique flow defining an internal branch
	 * @param cond This branch's condition.
	 */
	
	public void addBranch(TaskFlow branch, Condition<ElementInstance> cond) {
		addBranch(branch, branch, cond);
	}
	
	/**
	 * Variation of <code>addBranch</code> which allows to indicate a condition
	 * @param initialBranch First step of the internal branch
	 * @param finalBranch Last step of the internal branch
	 * @param cond This branch's condition.
	 */
	public void addBranch(InitializerFlow initialBranch, FinalizerFlow finalBranch, Condition<ElementInstance> cond) {
		final TreeSet<Flow> visited = new TreeSet<Flow>(); 
		initialBranch.setRecursiveStructureLink(this, visited);
		((MultiChoiceFlow)initialFlow).link(initialBranch, cond);
		finalBranch.link(finalFlow);
	}
	
	@Override
	public void addBranch(InitializerFlow initialBranch, FinalizerFlow finalBranch) {
		addBranch(initialBranch, finalBranch, new TrueCondition<ElementInstance>());		
	}
	
	@Override
	public void addBranch(TaskFlow initialBranch) {
		addBranch(initialBranch, new TrueCondition<ElementInstance>());
	}
}

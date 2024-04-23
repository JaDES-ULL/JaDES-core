/**
 * 
 */
package es.ull.simulation.parallel;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.engine.AbstractEngineObject;
import es.ull.simulation.model.engine.IElementInstanceEngine;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class ElementInstanceEngine extends AbstractEngineObject implements IElementInstanceEngine {
	/** Element instance's counter. Useful for identifying each instance */
	// Must start in 1 to avoid problems with internal control of request flows
	private static final AtomicInteger counter = new AtomicInteger(1);
    // Avoiding deadlocks (time-overlapped resources)
    /** List of conflictive elements */
    private ConflictZone conflicts;
    /** Amount of possible conflictive resources in the solution */
    private int conflictiveResources = 0;
    /** Stack of nested semaphores */
	private ArrayList<Semaphore> semStack;
	/** Associated {@link ElementInstance} */
	private final ElementInstance modelInstance;

	/**
	 * Constructs a new ElementInstanceEngine object.
	 * ElementInstanceEngine represents individual instances of elements within a simulation, each with its own counter
	 * for identification.
	 * It manages conflict zones to avoid deadlocks, maintains a stack of semaphores for nested synchronization, and is
	 * associated with a specific ElementInstance.
	 *
	 * @param simul          The ParallelSimulationEngine object to which this element instance belongs.
	 * @param modelInstance The associated ElementInstance object.
	 */
	public ElementInstanceEngine(ParallelSimulationEngine simul, ElementInstance modelInstance) {
		super(counter.getAndIncrement(), simul, "EI");
		this.modelInstance = modelInstance;
	}

	/**
	 * @return the modelInstance
	 */
	public ElementInstance getModelInstance() {
		return modelInstance;
	}
	
	/**
	 * Adds a new conflict to this element instance, that is, increments the
	 * counter for conflictive resources while booking.
	 */
	public void addConflict() {
		conflictiveResources++;
	}
	
	/**
	 * Removes a conflict from this element instance, that is, decrements the
	 * counter for conflictive resources while booking.
	 */
	public void removeConflict() {
		conflictiveResources--;
	}
	
	/**
	 * Returns true if any one of the resources taken to carry out an activity is active in more 
	 * than one AM
	 * @return True if any one of the resources taken to carry out an activity is active in more 
	 * than one AM; false in other case.
	 */
	protected boolean isConflictive() {
		return (conflictiveResources > 0);
	}
	
    /**
     * Creates a new conflict zone. This method should be invoked previously to
     * any activity request.
     */
	protected void resetConflictZone() {
        conflicts = new ConflictZone(this);
	}
	
	/**
	 * Establish a different conflict zone for this work item.
	 * @param zone The new conflict zone for this work item.
	 */
	protected void setConflictZone(ConflictZone zone) {
		conflicts = zone;
	}
	
	/**
	 * Removes this single IFlow from its conflict list. This method is invoked in case
	 * the work item detects that it can not carry out an activity.
	 */
	protected void removeFromConflictZone() {
		conflicts.remove(this);
	}
	
	/**
	 * Returns the conflict zone of this work item.
	 * @return The conflict zone of this work item.
	 */
	protected ConflictZone getConflictZone() {
		return conflicts;
	}

	/**
	 * Merges the conflict zone of this work item with another one.
	 * Since one conflict zone must be merged into the other, the decision of which work item "receives" the merging
	 * operation depends on their identifiers: the work item with the lower identifier "receives" the merging,
	 * and the other one "produces" the operation.
	 *
	 * @param ei The ElementInstanceEngine whose conflict zone must be merged with this one.
	 */
	protected void mergeConflictList(ElementInstanceEngine ei) {
		final int result = conflicts.compareTo(ei.getConflictZone());
		if (result != 0) {
			if (result < 0)
				conflicts.safeMerge(ei.getConflictZone());
			else if (result > 0) 
				ei.getConflictZone().safeMerge(conflicts);
		}
	}
	
	/**
	 * Obtains the stack of semaphores from the conflict zone and goes through
	 * this stack performing a wait operation on each semaphore.
	 */
	protected void waitConflictSemaphore() {
		semStack = conflicts.getSemaphores(this);
		try {
			for (Semaphore sem : semStack)
				sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Goes through the stack of semaphores performing a signal operation on each semaphore.
	 */
	protected void signalConflictSemaphore() {
		for (Semaphore sem : semStack)
			sem.release();
	}

	@Override
	public void notifyResourcesAcquired() {
    	// When this point is reached, that means that the resources have been completely taken
    	signalConflictSemaphore();
	}

	
}

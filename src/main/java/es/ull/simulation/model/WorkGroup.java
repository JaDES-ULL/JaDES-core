/**
 * 
 */
package es.ull.simulation.model;

import java.util.ArrayDeque;

import es.ull.simulation.model.engine.SimulationEngine;
import es.ull.simulation.model.flow.ActivityFlow;

/**
 * A set of pairs &lt{@link ResourceType}, {@link Integer}&gt which defines how many resources 
 * from each type are required to do something (typically an {@link ActivityFlow}).
 * Contains several methods to help the simulation find a suitable solution with the currently available
 * resources.
 * @author Iván Castilla Rodríguez
 */
public class WorkGroup extends SimulationObject implements IDescribable {
	/** List of resource types required to do something */
	protected final ResourceType[] resourceTypes;
	/** Amount of resource types required to do something */
	protected final int[] needed;
		
	/**
	 * Creates an empty work group
	 * @param simul The simulation this work group belongs to
	 */
	public WorkGroup(final Simulation simul) {
		this(simul, new ResourceType[0], new int[0]);
	}

    /**
     * Constructs a new instance of a work group.
     * This constructor initializes a new work group with one pair in the list of resource type and needed resources.
     *
     * @param model   The simulation to which this work group belongs.
     * @param rt      The resource type required by the work group.
     * @param needed  The number of resources needed.
     */
    public WorkGroup(final Simulation model, final ResourceType rt, final int needed) {
        this(model, new ResourceType[] {rt}, new int[] {needed});
    }

    /**
     * Constructs a new instance of a work group.
     * This constructor initializes a new work group with the specified resource types and their corresponding
     * required amounts.
     *
     * @param model   The simulation to which this work group belongs.
     * @param rts     An array containing the resource types that comprise this work group.
     * @param needs   An array containing the amounts of each resource type required by this work group.
     */
    public WorkGroup(final Simulation model, final ResourceType[] rts, final int []needs) {
    	super(model, model.getWorkGroupList().size(), "WG");
    	this.resourceTypes = rts;
    	this.needed = needs;
		model.add(this);
    }

	/**
     * Returns the amount of entries of the resource type table.
     * @return Amount of entries.
     */
    public int size() {
        return resourceTypes.length;
    }
    
    /**
     * Returns the {@link ResourceType resource type} from the position ind of the table.
     * @param ind Index of the entry
     * @return The resource type from the position ind. 
     */
    public ResourceType getResourceType(final int ind) {
        return resourceTypes[ind];
    }

    /**
     * Returns the needed amount of resources from the position ind of the table.
     * @param ind Index of the entry
     * @return The needed amount of resources from the position ind. 
     */
    protected int getNeeded(final int ind) {
        return needed[ind];
    }

    /**
     * Returns an array with the needs of resources for each {@link ResourceType resource type}
     * @return an array with the needs of resources for each {@link ResourceType resource type}
     */
    public int[] getNeeded() {
    	return needed;    	
    }
    
    /**
     * Returns an array with the {@link ResourceType resource types} required by this work group
     * @return an array with the {@link ResourceType resource types} required by this work group
     */
    public ResourceType[] getResourceTypes() {
    	return resourceTypes;
    }
    
    /**
     * Checks if a valid solution can be reached from the current situation. This method 
     * is used to bound the search tree.
     * @param pos Initial position.
     * @param nec Resources needed.
     * @return True if there is a reachable solution. False in other case.
     */
    protected boolean hasSolution(final int []pos, final int []nec) {
    	// Checks the current RT
        if (!resourceTypes[pos[0]].checkNeeded(pos[1], nec[pos[0]]))
        	return false;
        // For the next resource types, the first index must be 0
        for (int i = pos[0] + 1; i < resourceTypes.length; i++) {
            if (!resourceTypes[i].checkNeeded(0, nec[i]))
            	return false;
        }
        return true;
    }
    
    /**
     * Returns the position [{@link ResourceType}, {@link Resource}] of the next valid 
     * solution. The initial position <code>pos</code> is supposed to be correct.
	 * @param solution Tentative solution with booked resources
     * @param pos Initial position [ResourceType, Resource].
     * @param nec Resources needed.
     * @param ei The element instance trying to seize the resources
     * @return [ResourceType, Resource] where the next valid solution can be found; or
     * <code>null</code> if no solution was found. 
     */
    private int []searchNext(final ArrayDeque<Resource> solution, final int[] pos, final int []nec,
                             final ElementInstance ei) {
        final int []aux = new int[2];
        aux[0] = pos[0];
        aux[1] = pos[1];
        // Searches a resource type that requires resources
        while (nec[aux[0]] == 0) {
            aux[0]++;
            // The second index is reset
            aux[1] = -1;
            // No more resources needed ==> SOLUTION
            if (aux[0] == resourceTypes.length) {
                return aux;
            }
        }
        // Takes the first resource type and searches the NEXT available resource
        aux[1] = resourceTypes[aux[0]].getNextAvailableResource(solution, aux[1] + 1, ei);
        // This resource type don't have enough available resources
        if (aux[1] == -1)
        	return null;

        return aux;
    }

    /**
     * Makes a depth first search looking for a solution.
	 * @param solution Tentative solution with booked resources
     * @param pos Position to look for a solution [ResourceType, Resource] 
     * @param ned Resources needed
     * @param ei The element instance trying to seize the resources
     * @return True if a valid solution exists. False in other case.
     */
    public boolean findSolution(final ArrayDeque<Resource> solution, int []pos, final int []ned,
                                final ElementInstance ei) {
        pos = searchNext(solution, pos, ned, ei);
        // No solution
        if (pos == null)
            return false;
        // No more elements needed => SOLUTION
        if (pos[0] == resourceTypes.length)
            return true;
        ned[pos[0]]--;
        // Bound
        if (hasSolution(pos, ned))
        // ... the search continues
            if (findSolution(solution, pos, ned, ei))
                return true;
        // There's no solution with this resource. Try without it
        final Resource res = resourceTypes[pos[0]].getResource(pos[1]);
        res.removeFromSolution(solution, ei);
        ned[pos[0]]++;
        // ... and the search continues
        return findSolution(solution, pos, ned, ei);        
    }

    @Override
	public String getDescription() {
		StringBuilder str = new StringBuilder("WG" + id);
		for (int i = 0; i < resourceTypes.length; i++)
			str.append(" [" + resourceTypes[i] + "," + needed[i] + "]");
		return str.toString();
	}
    
	@Override
	protected void assignSimulation(final SimulationEngine engine) {
		// Nothing to do
	}
}

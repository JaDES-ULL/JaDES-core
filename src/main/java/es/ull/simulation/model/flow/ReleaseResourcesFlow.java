/**
 * 
 */
package es.ull.simulation.model.flow;

import java.util.ArrayDeque;
import java.util.TreeMap;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.info.ElementActionInfo;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.ResourceType;

/**
 * A IFlow to release a set of previously seized resources. The resources to release can be identified by means of a unique identifier (resourcesId)
 * or a workgroup.<p>
 * 
 * The IFlow can also define resource-type-related cancellation periods. If an element releases a resource belonging to one of the resource types, this 
 * resource can't be used during a period of time after the release.
 * @author Iván Castilla Rodríguez
 *
 */
public class ReleaseResourcesFlow extends AbstractSingleSuccessorFlow implements IResourceHandlerFlow, IFinalizerFlow {
    /** A brief description of the activity */
    protected final String description;
    /** A workgroup of resources to release */
    protected final WorkGroup wg;
    /** A unique identifier that sets which resources to release */
	protected final int resourcesId;
    /** Resources cancellation table */
    protected final TreeMap<ResourceType, Long> cancellationList;
    /** Conditions associated to resource cancellations */
    protected final TreeMap<ResourceType, AbstractCondition<ElementInstance>> cancellationConditionList;
	
	/**
	 * Creates a release resources IFlow
	 * @param model The simulation model this IFlow belongs to
	 * @param description A brief description of the IFlow
	 */
	public ReleaseResourcesFlow(final Simulation model, final String description) {

		this(model, description, 0, null);
	}
	
	/**
	 * Creates a release resources IFlow that will release the resources within the default group of resources and belonging to the 
	 * resource types defined in the specified workgroups
	 * @param model The simulation model this IFlow belongs to
	 * @param description A brief description of the IFlow
	 * @param wg Workgroup that identifies the resources to release
	 */
	public ReleaseResourcesFlow(final Simulation model, final String description, final WorkGroup wg) {
		this(model, description, 0, wg);
	}
	
	/**
	 * Creates a release resources IFlow that will release all the resources within the group identified by resourcesId 
	 * @param model The simulation model this IFlow belongs to
	 * @param description A brief description of the IFlow
	 * @param resourcesId Identifier of the group of resources
	 */
	public ReleaseResourcesFlow(final Simulation model, final String description, final int resourcesId) {
		this(model, description, resourcesId, null);
	}
	
	/**
	 * Creates a release resources IFlow that will release the resources within the group identified by resourcesId and belonging to the 
	 * resource types defined in the specified workgroups
	 * @param model The simulation model this IFlow belongs to
	 * @param description A brief description of the IFlow
	 * @param wg Workgroup that identifies the resources to release
	 * @param resourcesId Identifier of the group of resources
	 */
	public ReleaseResourcesFlow(final Simulation model, final String description,
								final int resourcesId, final WorkGroup wg) {
		super(model);
        this.description = description;
		this.resourcesId = resourcesId;
		cancellationList = new TreeMap<ResourceType, Long>();
		cancellationConditionList = new TreeMap<ResourceType, AbstractCondition<ElementInstance>>();
		this.wg = wg;
	}
	
	/**
	 * Returns the identifier of the group of resources
	 * @return the identifier of the group of resources
	 */
	public int getResourcesId() {
		return resourcesId;
	}

	@Override
	public String getDescription() {
		return description;
	}
    
	/**
	 * Returns the workgroup that identifies the resources to release
	 * @return the workgroup that identifies the resources to release
	 */
	public WorkGroup getWorkGroup() {
		return wg;
	}

	/**
	 * Adds a new ResourceType to the cancellation list.
	 * @param rt Resource type
	 * @param duration Duration of the cancellation.
	 */
	public void addResourceCancellation(final ResourceType rt, final long duration) {
		cancellationList.put(rt, duration);		
	}

	/**
	 * Adds a new ResourceType to the cancellation list.
	 * @param rt Resource type
	 * @param duration Duration of the cancellation.
	 * @param cond Condition that must be fulfilled to apply the cancellation 
	 */
	public void addResourceCancellation(final ResourceType rt, final long duration,
										final AbstractCondition<ElementInstance> cond) {
		cancellationList.put(rt, duration);	
		cancellationConditionList.put(rt, cond);
	}

	/**
	 * Returns the duration of the cancellation of a resource with the specified resource type.
	 * @param rt Resource Type
	 * @param ei Element instance that releases the resources
	 * @return The duration of the cancellation
	 */
	public long getResourceCancellation(final ResourceType rt, final ElementInstance ei) {
		final Long duration = cancellationList.get(rt);
		if (duration != null) {
			final AbstractCondition<ElementInstance> cond = cancellationConditionList.get(rt);
			if (cond == null) {
				return duration;
			}
			else if (cond.check(ei)) {
				return duration;
			}
		}
		return 0;
	}
	
	@Override
	public String getObjectTypeIdentifier() {
		return "REL";
	}
	
	@Override
	public void addPredecessor(final IFlow newFlow) {}

	@Override
	public void afterFinalize(final ElementInstance ei) {}

    /**
     * Releases the resources caught by this item to perform the activity.
     * @param ei Element instance that releases the resources
     */
    public void releaseResources(final ElementInstance ei) {
    	final ArrayDeque<Resource> resources = ei.releaseCaughtResources();
		simul.notifyInfo(new ElementActionInfo(simul, ei, ei.getElement(),
				this, ei.getExecutionWG(), resources, ElementActionInfo.Type.REL, simul.getTs()));
		ei.getElement().trace("Finishes\t" + this + "\t" + getDescription());
		afterFinalize(ei);
    }

    @Override
	public void request(final ElementInstance ei) {
		if (!ei.wasVisited(this)) {
			if (ei.isExecutable()) {
				if (beforeRequest(ei)) {
					releaseResources(ei);
					next(ei);
				}
				else {
					ei.cancel(this);
					next(ei);
				}
			}
			else {
				ei.updatePath(this);
				next(ei);
			}
		} else
			ei.notifyEnd();
	}

}

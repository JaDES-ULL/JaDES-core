package com.ull.simulation.condition;

import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.ResourceType;

/**
 * @author Iv�n Castilla
 *
 */
public class ResourceTypeAcquiredCondition extends Condition<ElementInstance> {
	final private ResourceType rt;
	
	/**
	 * 
	 */
	public ResourceTypeAcquiredCondition(ResourceType rt) {
		this.rt = rt;
	}

	@Override
	public boolean check(ElementInstance fe) {
		return (fe.getElement().isAcquiredResourceType(rt));
	}
	
	public ResourceType getResourceType() {
		return rt;
	}
}

package com.ull.simulation.condition;

/**
 * Default {@link Condition} used to define not conditional branches and situations 
 * without uncertainty. This {@link Condition} always returns true. 
 * @author Yeray Callero
 *
 */
public final class TrueCondition<E> extends Condition<E> {
	
	/**
	 * Creates a new TrueCondition
	 */
	public TrueCondition(){
	}

	@Override
	public boolean check(E fe) {
		return true;
	}
}

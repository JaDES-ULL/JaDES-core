package com.ull.simulation.condition;
/**
 * Default {@link Condition} used to define not conditional branches and situations 
 * without uncertainty. This {@link Condition} always returns false. 
 * @author Iv�n Castilla Rodr�guez
 *
 */
public final class FalseCondition<E> extends Condition<E> {
	
	/**
	 * Creates a new TrueCondition
	 */
	public FalseCondition(){
	}

	@Override
	public boolean check(E fe) {
		return false;
	}
}

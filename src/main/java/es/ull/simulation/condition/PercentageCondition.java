package es.ull.simulation.condition;

/**
 * Defines a {@link AbstractCondition} which is satisfied according to a specified percentage
 * of success.
 * @author Yeray Callero
 *
 */
public final class PercentageCondition<E> extends AbstractCondition<E> {
	/** Probability of success */
	final private double percentage;
	
	/**
	 * Creates a new PercentageCondition.
	 * @param percentage Percentage of success
	 */
	public PercentageCondition (double percentage) {
		this.percentage = percentage / 100;
	}
	
	/**
	 * Calculates a random (0, 100) number. If that number is
	 * lower than the percentage of success, returns <tt>true</tt>.
	 * @param fe Element used to check the condition (useless in this case).
	 * @return <tt>True</tt> if success, <tt>false</tt> otherwise 
	 */
	public boolean check(E fe) {
		double randomProb = Math.random();
		if (randomProb < percentage)
			return true;
		return false;
	}

}

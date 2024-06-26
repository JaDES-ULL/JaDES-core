package es.ull.simulation.variable;


/**
 * Simulation's variable which house a boolean type.
 * @author ycallero
 *
 */
public class BooleanVariable implements IUserVariable {
	
	/** Boolean value */
	Boolean value;

	/**
	 * Create a Boolean.valueOfVariable.
	 * @param value Init value.
	 */
	public BooleanVariable(Boolean value) {
		this.value = value;
	}
	
	/**
	 * Create a BooleanVariable.
	 * @param value Init value.
	 */
	public BooleanVariable(double value) {
		if (value == 0.0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}
	
	/**
	 * Obtain de current Variable's value.
	 * @return Variable's value.
	 */
	public Number getValue(Object... params) {
		if (value.booleanValue())
			return (Integer.valueOf(1));
		return (Integer.valueOf(0));
	}

	/**
	 * Compare two Variables. 
	 * @param arg0 The Variable which you want compare.
	 * @return True if both are equal.
	 */
	public boolean equals(IVariable arg0) {
		return (getValue().equals(arg0.getValue()));
	}
	
	/**
	 * Convert Variable's value to a string.
	 * @return String which represents Variable's value.
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * Set a new Variable's value from an Object.
	 * @param value New value.
	 */
	public void setValue(Object value) {
		this.value = (Boolean) value;
	}
	
	/**
	 * Set a new Variable's value from an integer.
	 * @param value New value.
	 */
	public void setValue(int value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}

	/**
	 * Set a new Variable's value from a boolean.
	 * @param value New value.
	 */
	public void setValue(boolean value) {
		this.value = Boolean.valueOf(value);
	}

	/**
	 * Set a new Variable's value from a char.
	 * @param value New value.
	 */
	public void setValue(char value) {
		if (value == '0')
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);			
	}

	/**
	 * Set a new Variable's value from a byte.
	 * @param value New value.
	 */
	public void setValue(byte value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}

	/**
	 * Set a new Variable's value from a double.
	 * @param value New value.
	 */
	public void setValue(double value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}

	/**
	 * Set a new Variable's value from a float.
	 * @param value New value.
	 */
	public void setValue(float value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}

	/**
	 * Set a new Variable's value from a long.
	 * @param value New value.
	 */
	public void setValue(long value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}

	/**
	 * Set a new Variable's value from a short.
	 * @param value New value.
	 */
	public void setValue(short value) {
		if (value == 0)
			this.value = Boolean.valueOf(true);
		else
			this.value = Boolean.valueOf(false);
	}
}

package es.ull.simulation.variable;


/**
 * Simulation's variable which house a short type.
 * @author ycallero
 *
 */
public class ShortVariable extends NumberVariable{
	
	/**
	 * Create a new ShortVariable.
	 * @param value Init value.
	 */
	public ShortVariable(short value) {
		this.value = Short.valueOf(value);
	}
	
	/**
	 * Create a new ShortVariable.
	 * @param value Init value.
	 */
	public ShortVariable(double value) {
		this.value = Short.valueOf((short)value);
	}
	
	/**
	 * Compare two Variables. 
	 * @param obj The Variable which you want compare.
	 * @return True if both are equal.
	 */	
	public boolean equals(IVariable obj) {
		return (value.equals(obj.getValue()));
	}

	/**
	 * Set a new Variable's value from an integer.
	 * @param value New value.
	 */
	public void setValue(int value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a boolean.
	 * @param value New value.
	 */
	public void setValue(boolean value) {
		if (value)
			this.value = Short.valueOf((short) 0);
		else
			this.value = Short.valueOf((short) 1);
	}

	/**
	 * Set a new Variable's value from a char.
	 * @param value New value.
	 */
	public void setValue(char value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a byte.
	 * @param value New value.
	 */
	public void setValue(byte value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a double.
	 * @param value New value.
	 */
	public void setValue(double value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a float.
	 * @param value New value.
	 */
	public void setValue(float value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a long.
	 * @param value New value.
	 */
	public void setValue(long value) {
		this.value = Short.valueOf((short) value);	
	}

	/**
	 * Set a new Variable's value from a short.
	 * @param value New value.
	 */
	public void setValue(short value) {
		this.value = Short.valueOf((short) value);
	}

}

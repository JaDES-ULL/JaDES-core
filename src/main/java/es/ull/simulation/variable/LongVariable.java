package es.ull.simulation.variable;


/**
 * Simulation's variable which house a long type.
 * @author ycallero
 *
 */
public class LongVariable extends NumberVariable{
	
	/**
	 * Create a new LongVariable.
	 * @param value Init value.
	 */
	public LongVariable(long value) {
		this.value = Long.valueOf(value);
	}
	
	/**
	 * Create a new LongVariable.
	 * @param value Init value.
	 */
	public LongVariable(double value) {
		this.value = Long.valueOf((long)value);
	}
	
	/**
	 * Compare two Variables. 
	 * @param obj The Variable which you want compare.
	 * @return True if both are equal.
	 */	
	public boolean equas(IVariable obj) {
		return value.equals(obj.getValue());
	}

	/**
	 * Set a new Variable's value from an integer.
	 * @param value New value.
	 */
	public void setValue(int value) {
		this.value = Long.valueOf((long) value);
	}

	/**
	 * Set a new Variable's value from a boolean.
	 * @param value New value.
	 */
	public void setValue(boolean value) {
		if (value)
			this.value = Long.valueOf(0);
		else
			this.value = Long.valueOf(1);
	}

	/**
	 * Set a new Variable's value from a char.
	 * @param value New value.
	 */
	public void setValue(char value) {
		this.value = Long.valueOf((long) value);
	}

	/**
	 * Set a new Variable's value from a byte.
	 * @param value New value.
	 */
	public void setValue(byte value) {
		this.value = Long.valueOf((long) value);
	}

	/**
	 * Set a new Variable's value from a double.
	 * @param value New value.
	 */
	public void setValue(double value) {
		this.value = Long.valueOf((long) value);
	}

	/**
	 * Set a new Variable's value from a float.
	 * @param value New value.
	 */
	public void setValue(float value) {
		this.value = Long.valueOf((long)value);
	}

	/**
	 * Set a new Variable's value from a long.
	 * @param value New value.
	 */
	public void setValue(long value) {
		this.value = Long.valueOf((long)value);
	}

	/**
	 * Set a new Variable's value from a short.
	 * @param value New value.
	 */
	public void setValue(short value) {
		this.value = Long.valueOf((long)value);
	}
}

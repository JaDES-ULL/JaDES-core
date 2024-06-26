package es.ull.simulation.variable;


/**
 * Simulation's variable which house a character type.
 * @author ycallero
 *
 */
public class CharacterVariable implements IUserVariable {

	/** Character value */
	Character value;

	/**
	 * Create a new CharacterVariable.
	 * @param value Init value.
	 */
	public CharacterVariable(Character value) {
		this.value = value;
	}
	
	/**
	 * Obtain de current Variable's value.
	 * @return Variable's value.
	 */
	public Number getValue(Object... params) {
		return (Integer.valueOf(Character.charCount(value.charValue())));
	}

	/**
	 * Compare two Variables. 
	 * @param arg0 The Variable which you want compare.
	 * @return True if both are equal.
	 */
	public boolean equals(IVariable arg0) {
		return (value.equals(arg0.getValue()));
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
		this.value = (Character) value;
	}
	
	/**
	 * Set a new Variable's value from an integer.
	 * @param value New value.
	 */
	public void setValue(int value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a boolean.
	 * @param value New value.
	 */
	public void setValue(boolean value) {
		if (value)
			this.value = Character.valueOf('0');
		else
			this.value = Character.valueOf('1');
	}

	/**
	 * Set a new Variable's value from a char.
	 * @param value New value.
	 */
	public void setValue(char value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a byte.
	 * @param value New value.
	 */
	public void setValue(byte value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a double.
	 * @param value New value.
	 */
	public void setValue(double value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a float.
	 * @param value New value.
	 */
	public void setValue(float value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a long.
	 * @param value New value.
	 */
	public void setValue(long value) {
		this.value = Character.valueOf((char) value);
	}

	/**
	 * Set a new Variable's value from a short.
	 * @param value New value.
	 */
	public void setValue(short value) {
		this.value = Character.valueOf((char) value);
	}
}

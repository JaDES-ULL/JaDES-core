/**
 * 
 */
package com.ull.simulation.model;

import java.util.TreeMap;

import com.ull.simulation.variable.BooleanVariable;
import com.ull.simulation.variable.ByteVariable;
import com.ull.simulation.variable.CharacterVariable;
import com.ull.simulation.variable.DoubleVariable;
import com.ull.simulation.variable.FloatVariable;
import com.ull.simulation.variable.IntVariable;
import com.ull.simulation.variable.LongVariable;
import com.ull.simulation.variable.ShortVariable;
import com.ull.simulation.variable.UserVariable;
import com.ull.simulation.variable.Variable;

/**
 * A simulation object that can handle {@link Variable variables}. 
 * @author Iv�n Castilla Rodr�guez
 *
 */
public abstract class VariableStoreSimulationObject extends SimulationObject implements VariableStore {
    /** Variable warehouse */
	protected final TreeMap<String, Variable> varCollection = new TreeMap<String, Variable>();

	/**
	 * Creates a simulation object capable of using {@link Variable variables}
	 * @param simul Simulation this object belongs to
	 * @param id Object identifier
     * @param objectTypeId a String that identifies the type of simulation object
	 */
	public VariableStoreSimulationObject(final Simulation simul, final int id, final String objectTypeId) {
		super(simul, id, objectTypeId);
	}

	@Override
	public Variable getVar(final String varName) {
		return varCollection.get(varName);
	}
	
	@Override
	public void putVar(final String varName, final Variable value) {
		varCollection.put(varName, value);
	}
	
	@Override
	public void putVar(final String varName, final double value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new DoubleVariable(value));
	}
	
	@Override
	public void putVar(final String varName, final int value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new IntVariable(value));
	}

	@Override
	public void putVar(final String varName, final boolean value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new BooleanVariable(value));
	}

	@Override
	public void putVar(final String varName, final char value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new CharacterVariable(value));
	}
	
	@Override
	public void putVar(final String varName, final byte value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new ByteVariable(value));
	}

	@Override
	public void putVar(final String varName, final float value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new FloatVariable(value));
	}
	
	@Override
	public void putVar(final String varName, final long value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new LongVariable(value));
	}
	
	@Override
	public void putVar(final String varName, final short value) {
		UserVariable v = (UserVariable) varCollection.get(varName);
		if (v != null) {
			v.setValue(value);
			varCollection.put(varName, v);
		} else
			varCollection.put(varName, new ShortVariable(value));
	}
	
}

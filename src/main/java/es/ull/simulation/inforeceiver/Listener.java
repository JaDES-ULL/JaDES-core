package es.ull.simulation.inforeceiver;

import java.util.ArrayList;

import es.ull.simulation.info.SimulationInfo;
import es.ull.simulation.model.IDescribable;

public abstract class Listener implements IDescribable {

	private final ArrayList<Class<?>> entrance = new ArrayList<Class<?>>();
	private final ArrayList<Class<?>> generatedInfos = new ArrayList<Class<?>>();
	private final String description;
	
	public Listener (String description) {
		this.description = description;
	}
	
	public abstract void infoEmited(SimulationInfo info);
	
	public void addEntrance(Class<?> cl) {
		entrance.add(cl);
	}
	
	public String toString() {
		return description;
	}

	public ArrayList<Class<?>> getEntrance() {
		return entrance;
	}
    
    public void debug (String message) {
    	System.out.println(description + " DEBUG MESSAGE:\n" + message + description
    			+ " END DEBUG MESSAGE.");
    }
	
	public void addGenerated(Class<?> cl) {
		generatedInfos.add(cl);
	}
	
	public ArrayList<Class<?>> getGeneratedInfos() {
		return generatedInfos;
	}

	@Override
	public String getDescription() {
		return description;
	}
}

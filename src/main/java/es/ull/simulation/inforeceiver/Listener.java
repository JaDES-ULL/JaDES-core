package es.ull.simulation.inforeceiver;

import java.util.ArrayList;

import es.ull.simulation.info.IPieceOfInformation;
import es.ull.simulation.model.IDescribable;

/**
 * This class represents a listener that is interested in receiving information from the simulation. Once received, the information
 * is processed by the listener by invoking the method {@link #infoEmited(IPieceOfInformation)}.
 */
public abstract class Listener implements IDescribable {
	/** The list of pieces of information this listener is interested in */
	private final ArrayList<Class<? extends IPieceOfInformation>> targetInformation;
	/** The description of the listener */
	private final String description;
	
	/**
	 * Creates a listener with a description.
	 * @param description The description of the listener.
	 */
	public Listener (String description) {
		this.description = description;
		targetInformation = new ArrayList<>();
	}
	
	/**
	 * The method that works with the received information.
	 * @param info The piece of information that has been received.
	 */
	public abstract void infoEmited(IPieceOfInformation info);
	
	/**
	 * Adds a class to the list of classes that the listener is interested in.
	 * @param cl The class to be added to the list.
	 */
	public void addTargetInformation(Class<? extends IPieceOfInformation> cl) {
		targetInformation.add(cl);
	}
	
	public String toString() {
		return description;
	}

	/**
	 * Returns the list of classes that the listener is interested in.
	 * @return The list of classes that the listener is interested in.
	 */
	public ArrayList<Class<? extends IPieceOfInformation>> getTargetInformation() {
		return targetInformation;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
}

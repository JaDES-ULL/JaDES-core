package es.ull.simulation.inforeceiver;

import java.util.ArrayList;
import java.util.HashMap;

import es.ull.simulation.info.IPieceOfInformation;

/**
 * This class is responsible for managing the information that is generated by the simulation and
 * the listeners that are interested in receiving it.
 */
public class InfoHandler implements IHandlesInformation {
	/**
	 * A map between the type of information and the listeners that are interested in receiving it.
	 */
	private final HashMap<Class<? extends IPieceOfInformation>, ArrayList<BasicListener> > registeredListeners;
	
	/**
	 * Creates a handler for the simulation information.
	 */
	public InfoHandler() {
		registeredListeners = new HashMap<Class<? extends IPieceOfInformation>, ArrayList<BasicListener>>();
	}
	
	/**
	 * Registers a listener to receive information of a certain type.
	 * @param listener The listener that wants to receive the information.
	 */
	public void registerListener(BasicListener listener) {	
		for (Class<? extends IPieceOfInformation> cl: listener.getTargetInformation()) {
			ArrayList<BasicListener> list;
			if (!registeredListeners.containsKey(cl)) {
				list = new ArrayList<BasicListener>();
				registeredListeners.put(cl, list);
			}
			else {
				list = registeredListeners.get(cl);
			}
			list.add(listener);
		}
	}

	/**
	 * Returns the listeners that are interested in receiving information of a certain type.
	 * @param infoTypeClass The type of information.
	 * @return The listeners that are interested in receiving information of the given type.
	 */
	public ArrayList<BasicListener> getListeners(Class<? extends IPieceOfInformation> infoTypeClass) {
		return registeredListeners.get(infoTypeClass);
	}

	/**
	 * Returns all the listeners that are interested in receiving information.
	 * @return All the listeners that are interested in receiving information.
	 */
	public ArrayList<BasicListener> getListeners() {
		ArrayList<BasicListener> listeners = new ArrayList<BasicListener>();
		for (ArrayList<BasicListener> list: registeredListeners.values()) {
			listeners.addAll(list);
		}
		return listeners;
	}

	/**
	 * Notifies the listeners that a piece of information has been generated.
	 * @param info The piece of information that has been generated.
	 */
	public void notifyInfo(IPieceOfInformation info) {
		if (registeredListeners.containsKey(info.getClass())) {
			final ArrayList<BasicListener> list = registeredListeners.get(info.getClass());
			for(BasicListener listener: list)
				listener.infoEmited(info);
		}
	}
	
}

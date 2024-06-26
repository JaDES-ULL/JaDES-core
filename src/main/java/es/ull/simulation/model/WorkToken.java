package es.ull.simulation.model;

import java.util.TreeSet;

import es.ull.simulation.model.flow.IFlow;

/**
 * The information of the current state of an {@link ElementInstance element instance}. In short, it indicates whether 
 * the current element instance is valid or not, and records the visited flows to avoid infinite loops. 
 * @author Yeray Callero
 */
public class WorkToken {
	/** The list of flows already visited during the current timestamp */ 
	private final TreeSet<IFlow> path = new TreeSet<IFlow>();
	/** Validity of the element instance containing this token */
	private boolean state;
	
	/**
	 * Creates a work token.
	 * @param state The initial state of the work token
	 */
	public WorkToken (final boolean state) {
		this.state = state;
	}

	/**
	 * Creates a work token.
	 * @param state The initial state of the work token
	 * @param startPoint The first IFlow that this token passes by
	 */
	public WorkToken(final boolean state, final IFlow startPoint) {
		this(state);
		path.add(startPoint);
	}
	
	/**
	 * Creates a work token copy of a previously created one.
	 * @param token Previously created token
	 */
	public WorkToken(final WorkToken token) {
		this(token.isExecutable());
		path.addAll(token.getPath());
	}
	
	/**
	 * Resets this token by setting its state to <code>false</code> and clearing the list of 
	 * visited flows.
	 */
	public void reset() {
		state = false;
		path.clear();
	}
	
	/**
	 * Adds a IFlow to the list of visited ones.
	 * @param visited New IFlow visited by the element instance containing this token
	 */
	public void addFlow(final IFlow visited) {
		path.add(visited);
	}
	
	/**
	 * Adds a collection of flows to the list of visited ones.
	 * @param path Collection of new IFlow visited by the element instance containing this token
	 */
	public void addFlow(final TreeSet<IFlow> path) {
		this.path.addAll(path);
	}
	
	/**
	 * Returns true if the specified IFlow was already visited by the element instance containing this token.
	 * @param IFlow IFlow that has to be checked
	 * @return True of the specified IFlow was already visited by the element instance containing this token;
	 * false in other case.
	 */
	public boolean wasVisited(final IFlow IFlow) {
		return path.contains(IFlow);
	}
	
	/**
	 * Returns true if the element instance containing this token is valid; false in other case.
	 * @return True if the element instance containing this token is valid; false in other case.
	 */
	public boolean isExecutable() {
		return state;
	}

	/**
	 * Sets the state of this work token.
	 * @param state New state of this work token
	 */
	public void setState(final boolean state) {
		this.state = state;
	}

	/**
	 * Returns the list of flows already visited by the element instance containing this token.
	 * @return The list of flows already visited by the element instance containing this token
	 */
	public TreeSet<IFlow> getPath() {
		return path;
	}
}

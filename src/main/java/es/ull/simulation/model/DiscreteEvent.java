/**
 * 
 */
package es.ull.simulation.model;

/**
 * A generic class for discrete events. Discrete events are instantaneous occurrences that execute one or more actions.
 * @author Ivan Castilla Rodriguez
 *
 */
public abstract class DiscreteEvent implements Runnable, Comparable<DiscreteEvent> {
    /** Timestamp when this event will be executed */
    final protected long ts;
    /** If true, the event will not be executed */
    protected boolean cancelled = false;

    /**
     * Creates a new basic event.
     * @param ts Timestamp when the event will be executed.
     */
    public DiscreteEvent(final long ts) {
        this.ts = ts;
    }
    
    /**
     * Performs a task and then it removes this event from the execution 
     * queue. It also updates the element's timestamp.
     */        
    public void run() {
    	if (!cancelled)
    		event();
    }
    
    /**
     * The action/task that this event carries out.
     */
    public abstract void event();
    
    /**
     * Cancels this event. This function only works when the event is currently scheduled in the 
     * simulation.
     * @return True if the event can be cancelled; false otherwise. 
     */
    public boolean cancel() {
    	cancelled = true;
    	return true;
    }
    
    /**
     * Checks if this event has been cancelled.
     * @return True if the event has been cancelled; false otherwise.
     */
    public boolean isCancelled() {
    	return cancelled;
    }

    /**
     * String representation of the event
     * @return A character string "Ev()[#]".
     */
    public String toString() {
        return "Ev(" + getClass().getName() + ")[" + ts + "]";
    }
    
    /**
     * Getter for property ts.
     * @return Value of property ts.
     */
    public long getTs() {
        return ts;
    }
    
    /**
     * Checks if this event is greater than, less than, or equal to other.
     * @param e Compared event.
     * @return 1, -1 or 0 if the current timestamp is greater than, less than, or equal to
     * the timestamp of the event passed by parameters.
     */    
	public int compareTo(final DiscreteEvent e) {
		final long evTs = e.getTs();
		if (ts > evTs)
			return 1;
		else if (ts < evTs)
			return -1;
		return 0;
	}

    /**
     * The last event this element executes. It decrements the total amount of elements of the
     * simulation.
     * @author Iván Castilla Rodríguez
     */
    public static class DefaultFinalizeEvent extends DiscreteEvent {
    	final protected IEventSource source;
        
        public DefaultFinalizeEvent(final IEventSource source, final long ts) {
            super(ts);
            this.source = source;
        }
        
        public void event() {
        	ILoggable.logger.trace(this + "\tEnds execution");
        }
    }

    /**
     * The first event this element executes.
     * @author Iván Castilla Rodríguez
     *
     */
    public static class DefaultStartEvent extends DiscreteEvent {
    	final protected IEventSource source;

    	public DefaultStartEvent(final IEventSource source, final long ts) {
            super(ts);
            this.source = source;
        }

        public void event() {
        	ILoggable.logger.trace(this + "\tStarts Execution");
        }
    }
}

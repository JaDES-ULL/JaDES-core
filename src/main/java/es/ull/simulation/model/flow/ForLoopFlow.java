/**
 * 
 */
package es.ull.simulation.model.flow;

import java.util.SortedMap;
import java.util.TreeMap;

import es.ull.simulation.functions.ConstantFunction;
import es.ull.simulation.functions.AbstractTimeFunction;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;


/**
 * A structured loop IFlow which resembles a for loop. The internal IFlow is
 * executed n times. n is defined by using the <code>iterations</code> attribute.
 * Be careful when using a continuous random distribution for defining the number
 * of iterations, since decimal values are rounded to the closest integer.  
 * @author Iván Castilla Rodríguez
 */
public class ForLoopFlow extends StructuredLoopFlow {
	/** Loop iterations */
	protected final AbstractTimeFunction iterations;
	/** List used by the control system. */
	protected final SortedMap<ElementInstance, Integer> checkList = new TreeMap<ElementInstance, Integer>();

	/**
	 * Create a new ForLoopFlow.
	 * @param initialSubFlow First step of the internal subflow
	 * @param finalSubFlow Last step of the internal subflow
	 * @param iterations Loop iterations.
 	 */
	public ForLoopFlow(final Simulation model, final IInitializerFlow initialSubFlow, final IFinalizerFlow finalSubFlow, final AbstractTimeFunction iterations) {
		super(model, initialSubFlow, finalSubFlow);
		this.iterations = iterations;
	}
	
	/**
	 * Create a new ForLoopFlow.
	 * @param subFlow A unique IFlow defining an internal subflow
	 * @param iterations Loop iterations.
 	 */
	public ForLoopFlow(final Simulation model, final ITaskFlow subFlow, final AbstractTimeFunction iterations) {
		this(model, subFlow, subFlow, iterations);
	}
	
	/**
	 * Create a new ForLoopFlow.
	 * @param initialSubFlow First step of the internal subflow
	 * @param finalSubFlow Last step of the internal subflow
	 * @param iterations Loop iterations.
 	 */
	public ForLoopFlow(final Simulation model, final IInitializerFlow initialSubFlow, final IFinalizerFlow finalSubFlow, final int iterations) {
		this(model, initialSubFlow, finalSubFlow, new ConstantFunction(iterations));
	}
	
	/**
	 * Create a new ForLoopFlow.
	 * @param subFlow A unique IFlow defining an internal subflow
	 * @param iterations Loop iterations.
 	 */
	public ForLoopFlow(final Simulation model, final ITaskFlow subFlow, final int iterations) {
		this(model, subFlow, subFlow, new ConstantFunction(iterations));
	}
	
	/**
	 * Returns the function which characterizes the iterations performed in the loop.
	 * @return The function which characterizes the iterations performed in the loop
	 */
	public AbstractTimeFunction getIterations() {
		return iterations;
	}

	@Override
	public void request(final ElementInstance ei) {
		if (!ei.wasVisited(this)) {
			if (ei.isExecutable()) {
				int iter = Math.round((float)iterations.getValue(ei.getElement()));
				if (beforeRequest(ei) && (iter > 0)) {
					checkList.put(ei, iter);
					ei.getElement().addRequestEvent(initialFlow, ei.getDescendantElementInstance(initialFlow));
				}
				else {
					ei.cancel(this);
					next(ei);				
				}
			}
			else {
				ei.updatePath(this);
				next(ei);
			}
		} else
			ei.notifyEnd();
	}

	@Override
	public void finish(final ElementInstance ei) {
		int iter = checkList.get(ei);
		if (--iter > 0) {
			ei.getElement().addRequestEvent(initialFlow, ei.getDescendantElementInstance(initialFlow));
			checkList.put(ei, iter);
		}
		else {
			checkList.remove(ei);
			super.finish(ei);
		}
	}

}

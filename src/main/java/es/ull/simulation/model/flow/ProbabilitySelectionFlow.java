/**
 * 
 */
package es.ull.simulation.model.flow;

import java.util.ArrayList;
import java.util.Collection;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;


/**
 * A {@link MultipleSuccessorFlow} which selects one outgoing branch among a set of them by 
 * using a probability value. Each outgoing branch has a value (0.0 - 1.0] expressing the 
 * probability to be chosen of such branch. 
 * @author Iván Castilla Rodríguez
 *
 */
public class ProbabilitySelectionFlow extends MultipleSuccessorFlow {
	/** List of probabilities associated to each outgoing branch */
	protected final ArrayList<Double> probabilities;

	/**
	 * Creates a new Probability Selection IFlow associated to the specified simulation
	 */
	public ProbabilitySelectionFlow(Simulation model) {
		super(model);
		probabilities = new ArrayList<Double>();
	}

	/**
	 * @return the probabilities
	 */
	public ArrayList<Double> getProbabilities() {
		return probabilities;
	}

	/**
	 * Adds a probabilistic IFlow's successor.
	 * @param successor This IFlow's successor
	 * @param prob The probability of this successor of being chosen
	 */
	public IFlow link(IFlow successor, double prob) {
		super.link(successor);
		probabilities.add(prob);
		return successor;
	}

	/**
	 * Adds a probabilistic IFlow's successor. A probability of 1.0 is associated to this successor
	 * @param successor This IFlow's successor
	 */
	public IFlow link(IFlow successor) {
		return link(successor, 1.0);
	}
	
	/**
	 * Adds a collection of probabilistic IFlow's successor. 
	 * Size of <code>succList</code> and <code>probList</code> must agree.
	 * @param succList This IFlow's successors
	 * @param probList The probability of these successors of being chosen
	 */
	public void link(Collection<IFlow> succList, Collection<Double> probList) {
		super.link(succList);
		probabilities.addAll(probList);	
	}
	
	/**
	 * Adds a collection of probabilistic IFlow's successor. The same probability
	 * is assigned to each successor. 
	 * @param succList This IFlow's successors
	 */
	public void link(Collection<IFlow> succList) {
		ArrayList<Double> probList = new ArrayList<Double>();
		for (int i = 0; i < succList.size(); i++)
			probList.add(1.0 / (double)succList.size());
		link(succList, probList);
	}

	@Override
	public void next(ElementInstance wThread) {
		super.next(wThread);
		if (wThread.isExecutable()) {
			double ref = Math.random();
			double aux = 0.0;
			for (int i = 0; i < successorList.size(); i++) {
				boolean res = (ref >= aux) && (ref < (aux + probabilities.get(i)));
				aux += probabilities.get(i);
				wThread.getElement().addRequestEvent(successorList.get(i), wThread.getSubsequentElementInstance(res, this, wThread.getToken()));					
			}			
		}
		else
			for (int i = 0; i < successorList.size(); i++)
				wThread.getElement().addRequestEvent(successorList.get(i), wThread.getSubsequentElementInstance(false, this, wThread.getToken()));
		wThread.notifyEnd();
	}
}

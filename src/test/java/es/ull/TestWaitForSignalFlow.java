/**
 * 
 */
package es.ull;

import java.util.ArrayList;
import java.util.Random;

import es.ull.simulation.model.DiscreteEvent;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.IEventSource;
import es.ull.simulation.model.Experiment;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationObject;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.engine.SimulationEngine;
import es.ull.simulation.model.flow.TimeFunctionDelayFlow;
import es.ull.simulation.model.flow.WaitForSignalFlow;

/**
 * A dummy example of using the WaitForSignalFlow class. We define a simple IFlow with a WaitForSignalFlow and a DelayFlow. Elements get to the wait... IFlow
 * and waits until a special object ({@link SimListener}) tells them to continue. The {@link SimListener} is a class that checks the waiting list every 5 minutes
 * and let pass a random number of elements.  
 * @author Iv�n Castilla
 *
 */
public class TestWaitForSignalFlow extends Experiment {
	final static private long ENDTS = 8 * 60;
	final static private int NELEM = 10;
	final static private long DELAY = 10;
	final static private long CHECK_DELAY = 5;
	/**
	 * @param description
	 * @param nExperiments
	 */
	public TestWaitForSignalFlow() {
		super("Test wait for signal", 1);
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.model.Experiment#getSimulation(int)
	 */
	@Override
	public Simulation getSimulation(int ind) {
		final Simulation simul = new TestSimulation();
		return simul;
	}

	class TestSimulation extends Simulation {
		private final SimListener listener;
		public TestSimulation() {
			super(0, "Test wait for signal simulation", 0L, ENDTS);
			final ElementType et = new ElementType(this, "Message");
			final TimeFunctionDelayFlow delayFlow = new TimeFunctionDelayFlow(this, "Little delay", DELAY) {
				@Override
				public void afterFinalize(ElementInstance ei) {
					super.afterFinalize(ei);
					System.out.println(getTs() + "\t" + ei.getElement() + "\tFinished");
				}
				
				@Override
				public boolean beforeRequest(ElementInstance ei) {
					System.out.println(getTs() + "\t" + ei.getElement() + "\tPassed");
					return super.beforeRequest(ei);
				}
			};
			listener = new SimListener(this);
			final WaitForSignalFlow waitFlow = new WaitForSignalFlow(this, "Wait", listener) {
				@Override
				public boolean beforeRequest(ElementInstance ei) {
					System.out.println(getTs() + "\t" + ei.getElement() + "\tTrying to pass");
					return super.beforeRequest(ei);
				}
			};
			waitFlow.link(delayFlow);
			new TimeDrivenElementGenerator(this, NELEM, et, waitFlow, SimulationPeriodicCycle.newHourlyCycle(getTimeUnit()));
		}
		
		@Override
		public void init() {
			super.init();
			addEvent(listener.onCreate(getTs()));
		}
	}
	
	class SimListener extends SimulationObject implements IEventSource, WaitForSignalFlow.Listener {
		private WaitForSignalFlow IFlow = null;
		final private TestSimulation simul;
		final private ArrayList<ElementInstance> waiting;
		final private Random rnd;
		
		public SimListener(TestSimulation simul) {
			super(simul, 0, "LIS");
			this.simul = simul;
			this.waiting = new ArrayList<ElementInstance>();
			this.rnd = new Random();
		}

		@Override
		public DiscreteEvent onCreate(long ts) {
			return new CheckEvent(ts + CHECK_DELAY);
		}
		
		@Override
		public DiscreteEvent onDestroy(long ts) {
			return new DiscreteEvent.DefaultFinalizeEvent(this, ts);
		}

		@Override
		public void notifyEnd() {
	        simul.addEvent(onDestroy(simul.getTs()));		
		}

		@Override
		public void register(WaitForSignalFlow IFlow) {
			this.IFlow = IFlow;
			
		}

		@Override
		public void notifyArrival(WaitForSignalFlow IFlow, ElementInstance ei) {
			waiting.add(ei);
		}
		
		class CheckEvent extends DiscreteEvent {

			public CheckEvent(long ts) {
				super(ts);
			}

			@Override
			public void event() {
				final int n = rnd.nextInt(waiting.size());
				System.out.println(getTs() + "\t" + SimListener.this + "\tAllowing " + n + " elements to pass");
				for (int i = 0; i < n; i++) {
					IFlow.signal(waiting.remove(0));
				}
				simul.addEvent(new CheckEvent(ts + CHECK_DELAY));
			}
			
		}

		@Override
		protected void assignSimulation(SimulationEngine engine) {
			// Nothing to do
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestWaitForSignalFlow().start();

	}

}

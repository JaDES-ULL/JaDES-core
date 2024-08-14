/**
 * 
 */
package es.ull;

import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.inforeceiver.StdInfoView;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.ParallelFlow;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class TestConditionalResourceGenerator extends BaseExperiment {
	private final static int NRT = 5;
	private final static long []DURATIONS = new long[] {5,6,7,8,9};

	class SpecialActivityFlow extends ActivityFlow {
		final private ResourceType[] rts;
		final private int specialId;
		
		public SpecialActivityFlow(Simulation model, String description, int specialId, ResourceType[] rts) {
			super(model, description, 0);
			this.specialId = specialId;
			this.rts = rts;
		}
		
		@Override
		public void afterFinalize(ElementInstance fe) {
			if (specialId < NRT - 1) {
				final Resource res = new Resource(simul, "Container " + (specialId + 1));
				res.newTimeTableOrCancelEntriesAdder(rts[specialId + 1]).addTimeTableEntry();
				simul.addEvent(res.onCreate(simul.getTs()));
			}
		}
	}
	class TestModel extends Simulation {
		final ResourceType[] rts;
		
		public TestModel(int id) {
			super(id, "Testing conditional generation of resources " + id, TimeUnit.MINUTE, 0L,
					24 * 60);
			final ElementType et = new ElementType(this, "Crane");
			rts = new ResourceType[NRT];
			final WorkGroup[] wgs = new WorkGroup[NRT];
			final ActivityFlow[] reqs = new ActivityFlow[NRT];
			final ParallelFlow pf = new ParallelFlow(this);
			for (int i = 0; i < NRT; i++) {
				rts[i] = new ResourceType(this, "Container type " + i);
				wgs[i] = new WorkGroup(this, rts[i], 1);
				reqs[i] = new SpecialActivityFlow(this, "Req " + i, i, rts);
				reqs[i].newWorkGroupAdder(wgs[i]).withDelay(DURATIONS[i]).add();
				pf.link(reqs[i]);
			}
			// Only the first resource is available from the beginning
			final Resource res0 = new Resource(this, "Container " + 0);
			res0.newTimeTableOrCancelEntriesAdder(rts[0]).addTimeTableEntry();
			
			new TimeDrivenElementGenerator(this, 1, et, pf,
					SimulationPeriodicCycle.newDailyCycle(getTimeUnit()));
		}
		
	}
	/**
	 * @param description
	 * @param nExperiments
	 */
	public TestConditionalResourceGenerator(CommonArguments arguments) {
		super("Testing conditional generation of resources", arguments);
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.model.Experiment#getModel(int)
	 */
	@Override
	public void runExperiment(int ind) {
		Simulation simul = new TestModel(ind);
		simul.addInfoReceiver(new StdInfoView());
		simul.run();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final CommonArguments arguments = new CommonArguments();

		new TestConditionalResourceGenerator(arguments).run();

	}

}

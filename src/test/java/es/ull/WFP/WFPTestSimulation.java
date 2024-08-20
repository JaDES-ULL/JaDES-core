/**
 * 
 */
package es.ull.WFP;

import java.util.ArrayList;
import java.util.TreeMap;

import es.ull.CheckActivitiesListener;
import es.ull.CheckElementsListener;
import es.ull.CheckResourcesListener;
import es.ull.CheckResourcesListener.ResourceUsageTimeStamps;
import es.ull.simulation.inforeceiver.StdInfoView;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.SimulationTimeFunction;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeStamp;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.IInitializerFlow;

/**
 * The base class to create tests for Workflow patterns.
 * Since the checking needs to be automated, the models defined in the tests must adhere to the following restrictions:
 * - The simulation must be restricted to 1 day
 * - Simulation Time Unit must be MINUTE 
 * - No random number generators should be used. 
 * - Resources must use a simple periodic cycle
 * - The preferred cycle is the TableCycle   
 * - All the elements must finish their tasks within the simulated time
 * @author Iván Castilla Rodríguez
 *
 */
public abstract class WFPTestSimulation extends Simulation {
	public final static int DEFNELEMENTS = 3;
	public final static TimeUnit SIMUNIT = TimeUnit.MINUTE; 
	public final static long RESSTART = 8 * 60;
	public final static long RESPERIOD = 24 * 60;
	public final static long RESAVAILABLE = 7 * 60;
	public final static TimeStamp GENSTART = TimeStamp.getZero();
	public final static TimeStamp GENPERIOD = TimeStamp.getDay();
	public final static long []DEFACTDURATION = new long [] {5, 10, 15, 20, 25, 30, 120};
	public final static long SIMSTART = 0L;
	public final static long SIMEND = 1440L;
	private final ArrayList<Integer> nElems;
	private final ArrayList<Long> actDuration;
	private final TreeMap<ActivityFlow, Integer> actIndex; 
	private final ArrayList<ResourceUsageTimeStamps> roleOns;
	private final ArrayList<ResourceUsageTimeStamps> roleOffs;
	private final TestWFP.TestWFPArguments args;
	
	public WFPTestSimulation(int id, String description, TestWFP.TestWFPArguments args) {
		super(id, description, SIMSTART, SIMEND);
		nElems = new ArrayList<Integer>();
		actDuration = new ArrayList<Long>();
		actIndex = new TreeMap<ActivityFlow, Integer>();
		roleOffs = new ArrayList<>();
		roleOns = new ArrayList<>();
		this.args = args;
		createModel();
		addCheckers();
	}
	
	protected abstract void createModel();

	private void addCheckers() {
		if (!args.quiet)
			addInfoReceiver(new StdInfoView());
		addInfoReceiver(new CheckResourcesListener(this.getResourceList().size(), roleOns, roleOffs));
		addInfoReceiver(new CheckElementsListener(nElems));
		int n = 0;
		for (int count : nElems) 
			n += count;
		addInfoReceiver(new CheckActivitiesListener(n, actIndex, actDuration));
	}
	
	public Resource getDefResource(String description, ResourceType rt) {
		final Resource res = new Resource(this, description);
		final SimulationPeriodicCycle cycle = new SimulationPeriodicCycle(SIMUNIT, RESSTART, new SimulationTimeFunction(SIMUNIT, "ConstantVariate", RESPERIOD), 0);
		res.newTimeTableOrCancelEntriesAdder(rt).withDuration(cycle, RESAVAILABLE).addTimeTableEntry();
		
		roleOns.add(new ResourceUsageTimeStamps(res.getIdentifier(), rt.getIdentifier(), RESSTART));
		roleOffs.add(new ResourceUsageTimeStamps(res.getIdentifier(), rt.getIdentifier(), RESSTART + RESAVAILABLE));
		return res;
	}
	
	public ResourceType getDefResourceType(String description) {
		return new ResourceType(this, description);
	}
	
	public ActivityFlow getDefActivity(String description, WorkGroup wg) {
		return getDefActivity(description, 0, wg, true);
	}
	
	public ActivityFlow getDefActivity(String description, WorkGroup wg, boolean presential) {
		return getDefActivity(description, 0, wg, presential);
	}
	
	public ActivityFlow getDefActivity(String description, int dur, WorkGroup wg) {
		return getDefActivity(description, dur, wg, true);
	}
	
	public ActivityFlow getDefActivity(String description, int dur, WorkGroup wg, boolean exclusive) {
		return new TestActivityFlow(description, dur, wg, exclusive);
	}
	
	public ElementType getDefElementType(String description) {
		// Adds a new element type but, until not used within a generator, it will create 0 elements
		nElems.add(0);
		return new ElementType(this, description);
	}
	
	public SimulationPeriodicCycle getGeneratorCycle() {
		return new SimulationPeriodicCycle(SIMUNIT, GENSTART, new SimulationTimeFunction(SIMUNIT, "ConstantVariate", GENPERIOD), 0);
	}
	
	public TimeDrivenElementGenerator getDefGenerator(ElementType et, IInitializerFlow IFlow) {
		return getDefGenerator(DEFNELEMENTS, et, IFlow);
	}
	
	public TimeDrivenElementGenerator getDefGenerator(int elems, ElementType et, IInitializerFlow IFlow) {
		nElems.set(et.getIdentifier(), nElems.get(et.getIdentifier()) + elems);
        return new TimeDrivenElementGenerator(this, elems, et, IFlow, getGeneratorCycle());
	}
	
	public class TestActivityFlow extends ActivityFlow {

		public TestActivityFlow(String description, int dur, WorkGroup wg, boolean exclusive) {
			super(WFPTestSimulation.this, description, exclusive, false);
	    	newWorkGroupAdder(wg).withDelay(DEFACTDURATION[dur]).add();
	    	actIndex.put(this, actDuration.size());
	    	actDuration.add(DEFACTDURATION[dur]);
		}
		
	}
}

/**
 * 
 */
package es.ull;

import es.ull.simulation.condition.ResourceTypeAcquiredCondition;
import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.inforeceiver.StdInfoListener;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.ExclusiveChoiceFlow;
import es.ull.simulation.model.flow.ReleaseResourcesFlow;
import es.ull.simulation.model.flow.RequestResourcesFlow;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class TestResourcesManagement extends BaseExperiment {
	final static TimeUnit UNIT = Simulation.DEF_TIME_UNIT;
	final static long END_TIME = 100;

	class ModelResourceManagement extends Simulation {
		public ModelResourceManagement(int ind) {
			super(ind, "Testing resource management " + ind);
			
			// The only element type
			final ElementType et = new ElementType(this, "Package");
			
			// The three resource types involved in the simulation
			final ResourceType rtOperatorA = new ResourceType(this, "OperatorA");
			final ResourceType rtOperatorB = new ResourceType(this, "OperatorB");
			final ResourceType rtMachine = new ResourceType(this, "Machine");
			final ResourceType rtTransport = new ResourceType(this, "Transport");
			final ResourceType rtLocationA = new ResourceType(this, "LocationA");
			final ResourceType rtLocationB = new ResourceType(this, "LocationB");

			// Create the specific resources
			rtOperatorA.addGenericResources(1);
			rtOperatorB.addGenericResources(1);
			rtMachine.addGenericResources(1);
			rtTransport.addGenericResources(1);
			rtLocationA.addGenericResources(2);
			rtLocationB.addGenericResources(1);
			
			// Define the workgroups
			final WorkGroup wgLocationA = new WorkGroup(this, rtLocationA, 1);
			final WorkGroup wgLocationB = new WorkGroup(this, rtLocationB, 1);
			final WorkGroup wgOperatorA = new WorkGroup(this, rtOperatorA, 1);
			final WorkGroup wgOperatorB = new WorkGroup(this, rtOperatorB, 1);
			final WorkGroup wgTransport = new WorkGroup(this, rtTransport, 1);
			final WorkGroup wgMachine = new WorkGroup(this, rtMachine, 1);
			final WorkGroup wgEmpty = new WorkGroup(this);
			
			// Create basic steps of the IFlow
			final RequestResourcesFlow reqLocationA = new RequestResourcesFlow(this,
					"Request location A", 0);
			final RequestResourcesFlow reqLocationB = new RequestResourcesFlow(this,
					"Request location B", 1);
			final RequestResourcesFlow reqOperatorA = new RequestResourcesFlow(this,
					"Request operator A", 2);
			final RequestResourcesFlow reqTransport = new RequestResourcesFlow(this,
					"Request transport", 3);
			final ReleaseResourcesFlow relLocationA = new ReleaseResourcesFlow(this,
					"Request location A", 0);
			final ReleaseResourcesFlow relLocationB = new ReleaseResourcesFlow(this,
					"Release location B", 1);
			final ReleaseResourcesFlow relOperatorA = new ReleaseResourcesFlow(this,
					"Release operator A", 2);
			final ReleaseResourcesFlow relTransport = new ReleaseResourcesFlow(this,
					"Release transport", 3);
			
			final ActivityFlow actWorkAtLocationA = new ActivityFlow(this, "Work at location A");
			final ActivityFlow actWorkAtLocationB = new ActivityFlow(this, "Work at location B");
			final ActivityFlow actMoveFromAToB = new ActivityFlow(this, "Move from A to B");
			
			// Assign duration and workgroups to activities
			reqLocationA.newWorkGroupAdder(wgLocationA).add();
			reqLocationB.newWorkGroupAdder(wgLocationB).add();
			reqOperatorA.newWorkGroupAdder(wgOperatorA).add();
			reqTransport.newWorkGroupAdder(wgTransport).add();
			actWorkAtLocationA.newWorkGroupAdder(wgMachine).withDelay(10L).add();
			actWorkAtLocationB.newWorkGroupAdder(wgOperatorB).withDelay(10L).add();
			actMoveFromAToB.newWorkGroupAdder(wgEmpty).withDelay(5L).add();

			// Create IFlow
			reqLocationA.link(reqOperatorA).link(actWorkAtLocationA).link(relOperatorA)
					.link(reqTransport).link(relLocationA);
			relLocationA.link(actMoveFromAToB).link(reqLocationB).link(relTransport)
					.link(actWorkAtLocationB).link(relLocationB);
			SimulationPeriodicCycle cycle = SimulationPeriodicCycle.newDailyCycle(UNIT, 0);
			new TimeDrivenElementGenerator(this, 2, et, reqLocationA, cycle);
		}
	}
	
	class ModelResourceManagementDefaultGroup extends Simulation {
		public ModelResourceManagementDefaultGroup(int ind) {
			super(ind, "Testing resource management " + ind);
			
			// The only element type
			final ElementType et = new ElementType(this, "Package");
			
			// The three resource types involved in the simulation
			final ResourceType rtOperatorA = new ResourceType(this, "OperatorA");
			final ResourceType rtOperatorB = new ResourceType(this, "OperatorB");
			final ResourceType rtMachine = new ResourceType(this, "Machine");
			final ResourceType rtTransport = new ResourceType(this, "Transport");
			final ResourceType rtLocationA = new ResourceType(this, "LocationA");
			final ResourceType rtLocationB = new ResourceType(this, "LocationB");

			// Create the specific resources
			rtOperatorA.addGenericResources(1);
			rtOperatorB.addGenericResources(1);
			rtMachine.addGenericResources(1);
			rtTransport.addGenericResources(1);
			rtLocationA.addGenericResources(2);
			rtLocationB.addGenericResources(1);
			
			// Define the workgroups
			final WorkGroup wgLocationA = new WorkGroup(this, rtLocationA, 1);
			final WorkGroup wgLocationB = new WorkGroup(this, rtLocationB, 1);
			final WorkGroup wgOperatorA = new WorkGroup(this, rtOperatorA, 1);
			final WorkGroup wgOperatorB = new WorkGroup(this, rtOperatorB, 1);
			final WorkGroup wgTransport = new WorkGroup(this, rtTransport, 1);
			final WorkGroup wgMachine = new WorkGroup(this, rtMachine, 1);
			final WorkGroup wgEmpty = new WorkGroup(this);
			
			// Create basic steps of the IFlow
			final RequestResourcesFlow reqLocationA = new RequestResourcesFlow(this,
					"Request location A");
			final RequestResourcesFlow reqLocationB = new RequestResourcesFlow(this,
					"Request location B");
			final RequestResourcesFlow reqOperatorA = new RequestResourcesFlow(this,
					"Request operator A");
			final RequestResourcesFlow reqTransport = new RequestResourcesFlow(this,
					"Request transport");
			final ReleaseResourcesFlow relLocationA = new ReleaseResourcesFlow(this,
					"Request location A", wgLocationA);
			final ReleaseResourcesFlow relLocationB = new ReleaseResourcesFlow(this,
					"Release location B", wgLocationB);
			final ReleaseResourcesFlow relOperatorA = new ReleaseResourcesFlow(this,
					"Release operator A", wgOperatorA);
			final ReleaseResourcesFlow relTransport = new ReleaseResourcesFlow(this,
					"Release transport", wgTransport);
			
			final ActivityFlow actWorkAtLocationA = new ActivityFlow(this, "Work at location A");
			final ActivityFlow actWorkAtLocationB = new ActivityFlow(this, "Work at location B");
			final ActivityFlow actMoveFromAToB = new ActivityFlow(this, "Move from A to B");
			
			// Assign duration and workgroups to activities
			reqLocationA.newWorkGroupAdder(wgLocationA).add();
			reqLocationB.newWorkGroupAdder(wgLocationB).add();
			reqOperatorA.newWorkGroupAdder(wgOperatorA).add();
			reqTransport.newWorkGroupAdder(wgTransport).add();
			actWorkAtLocationA.newWorkGroupAdder(wgMachine).withDelay(10L).add();
			actWorkAtLocationB.newWorkGroupAdder(wgOperatorB).withDelay(10L).add();
			actMoveFromAToB.newWorkGroupAdder(wgEmpty).withDelay(5L).add();

			// Create IFlow
			reqLocationA.link(reqOperatorA).link(actWorkAtLocationA).link(relOperatorA)
					.link(reqTransport).link(relLocationA);
			relLocationA.link(actMoveFromAToB).link(reqLocationB).link(relTransport)
					.link(actWorkAtLocationB).link(relLocationB);
			SimulationPeriodicCycle cycle = SimulationPeriodicCycle.newDailyCycle(UNIT, 0);
			new TimeDrivenElementGenerator(this, 2, et, reqLocationA, cycle);
		}
	}
	
	class ModelResourceManagementSimple extends Simulation {
		public ModelResourceManagementSimple(int ind) {
			super(ind, "Testing simple resource management " + ind);
			
			// The only element type
			final ElementType et = new ElementType(this, "Package");
			
			// The three resource types involved in the simulation
			final ResourceType rtOperatorA = new ResourceType(this, "OperatorA");
			final ResourceType rtTransport = new ResourceType(this, "Transport");

			// Create the specific resources
			rtOperatorA.addGenericResources(1);
			rtTransport.addGenericResources(1);
			
			// Define the workgroups
			final WorkGroup wgOperatorA = new WorkGroup(this, rtOperatorA, 1);
			final WorkGroup wgTransport = new WorkGroup(this, rtTransport, 1);
			
			// Create basic steps of the IFlow
			final RequestResourcesFlow reqTransport = new RequestResourcesFlow(this,
					"Request transport", 1);
			final ReleaseResourcesFlow relTransport = new ReleaseResourcesFlow(this,
					"Release transport", 1);
			
			final ActivityFlow actWorkAtLocationA = new ActivityFlow(this, "Work at location A");
			
			// Assign duration and workgroups to activities
			reqTransport.newWorkGroupAdder(wgTransport).add();
			actWorkAtLocationA.newWorkGroupAdder(wgOperatorA).withDelay(10L).add();

			// Create IFlow
			reqTransport.link(actWorkAtLocationA).link(relTransport);
			SimulationPeriodicCycle cycle = SimulationPeriodicCycle.newDailyCycle(UNIT, 0);
			new TimeDrivenElementGenerator(this, 2, et, reqTransport, cycle);
		}
	}
	
	class ModelResourceManagementCheckingRTs extends Simulation {
		public ModelResourceManagementCheckingRTs(int ind) {
			super(ind, "Testing simple resource management with several different resource types" + ind);
			
			// The only element type
			final ElementType et = new ElementType(this, "Package");
			
			// The three resource types involved in the simulation
			final ResourceType rtOperatorA = new ResourceType(this, "OperatorA");
			final ResourceType rtOperatorB = new ResourceType(this, "OperatorB");
			final ResourceType rtTransportA = new ResourceType(this, "TransportA");
			final ResourceType rtTransportB = new ResourceType(this, "TransportB");

			// Create the specific resources
			rtOperatorA.addGenericResources(1);
			rtOperatorB.addGenericResources(1);
			rtTransportA.addGenericResources(1);
			rtTransportB.addGenericResources(1);
			
			// Define the workgroups
			final WorkGroup wgOperatorA = new WorkGroup(this, rtOperatorA, 1);
			final WorkGroup wgOperatorB = new WorkGroup(this, rtOperatorB, 1);
			final WorkGroup wgTransportA = new WorkGroup(this, rtTransportA, 1);
			final WorkGroup wgTransportB = new WorkGroup(this, rtTransportB, 1);
			
			// Create basic steps of the IFlow
			final RequestResourcesFlow reqTransport = new RequestResourcesFlow(this,
					"Request transport", 1);
			final ReleaseResourcesFlow relTransport = new ReleaseResourcesFlow(this,
					"Release transport", 1);
			
			final ActivityFlow actWorkAtLocationA = new ActivityFlow(this, "Work at location A");
			final ActivityFlow actWorkAtLocationB = new ActivityFlow(this, "Work at location B");
			
			// Assign duration and workgroups to activities
			reqTransport.newWorkGroupAdder(wgTransportA).add();
			reqTransport.newWorkGroupAdder(wgTransportB).add();
			actWorkAtLocationA.newWorkGroupAdder(wgOperatorA).withDelay(10L).add();
			actWorkAtLocationB.newWorkGroupAdder(wgOperatorB).withDelay(10L).add();

			ExclusiveChoiceFlow condFlow = new ExclusiveChoiceFlow(this);
			condFlow.link(actWorkAtLocationB, new ResourceTypeAcquiredCondition(rtTransportB));
			condFlow.link(relTransport);
			// Create IFlow
			reqTransport.link(actWorkAtLocationA).link(condFlow);
			actWorkAtLocationB.link(relTransport);
			SimulationPeriodicCycle cycle = SimulationPeriodicCycle.newDailyCycle(UNIT, 0);
			new TimeDrivenElementGenerator(this, 2, et, reqTransport, cycle);
		}
	}
	
	/**
	 * 
	 */
	public TestResourcesManagement(CommonArguments args) {
		super("Testing resource management", args);
	}

	@Override
	public void runExperiment(int ind) {
		final Simulation simul = new ModelResourceManagementDefaultGroup(ind);
		simul.registerListener(new StdInfoListener());
		simul.run(END_TIME);;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final CommonArguments arguments = new CommonArguments();
		new TestResourcesManagement(arguments).run();

	}

}

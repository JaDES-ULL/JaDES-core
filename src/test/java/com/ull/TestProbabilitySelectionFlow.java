package com.ull;
import com.ull.functions.TimeFunctionFactory;
import com.ull.simulation.factory.SimulationFactory;
import com.ull.simulation.inforeceiver.StdInfoView;
import com.ull.simulation.model.ElementType;
import com.ull.simulation.model.Experiment;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.SimulationPeriodicCycle;
import com.ull.simulation.model.SimulationTimeFunction;
import com.ull.simulation.model.TimeStamp;
import com.ull.simulation.model.TimeUnit;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.ProbabilitySelectionFlow;

/**
 * 
 */
class ExperimentProbSel extends Experiment {
	static final int NEXP = 1;
    static final int NDAYS = 1;
	
	public ExperimentProbSel() {
		super("Banco", NEXP);
	}

	public Simulation getSimulation(int ind) {
		Simulation sim = null;
		TimeUnit unit = TimeUnit.MINUTE;
		SimulationFactory factory = new SimulationFactory(ind, "EjProbabilidades", TimeUnit.MINUTE, TimeStamp.getZero(), new TimeStamp(TimeUnit.DAY, NDAYS));
		sim = factory.getSimulation();

    	ActivityFlow act0 = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "10 %", 0, false, false);
    	ActivityFlow act1 = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "30 %", 0, false, false);
    	ActivityFlow act2 = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "60 %", 0, false, false);
        ResourceType rt = factory.getResourceTypeInstance("Empleado");
        
        WorkGroup wg = factory.getWorkGroupInstance(new ResourceType[] {rt}, new int[] {1});

        act0.newWorkGroupAdder(wg).withDelay(new SimulationTimeFunction(unit, "NormalVariate", 15, 2)).add();
        act1.newWorkGroupAdder(wg).withDelay(new SimulationTimeFunction(unit, "NormalVariate", 15, 2)).add();
        act2.newWorkGroupAdder(wg).withDelay(new SimulationTimeFunction(unit, "NormalVariate", 15, 2)).add();
   
        SimulationPeriodicCycle subc2 = new SimulationPeriodicCycle(unit, 480, new SimulationTimeFunction(unit, "ConstantVariate", 1040), 5);
        SimulationPeriodicCycle c2 = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(unit, "ConstantVariate", 1040 * 7), 0, subc2);

        factory.getResourceInstance("Empleado1").newTimeTableOrCancelEntriesAdder(rt).withDuration(c2, 420).addTimeTableEntry();
        factory.getResourceInstance("Empleado2").newTimeTableOrCancelEntriesAdder(rt).withDuration(c2, 420).addTimeTableEntry();
        factory.getResourceInstance("Empleado3").newTimeTableOrCancelEntriesAdder(rt).withDuration(c2, 420).addTimeTableEntry();
        

        ProbabilitySelectionFlow root = (ProbabilitySelectionFlow)factory.getFlowInstance("ProbabilitySelectionFlow");
        
        root.link(act0, 0.1);
        root.link(act1, 0.3);
        root.link(act2, 0.6);
        
        ElementType et = factory.getElementTypeInstance("Cliente");
        SimulationPeriodicCycle cGen = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(unit, "ConstantVariate", 1040), NDAYS);
        factory.getTimeDrivenElementGeneratorInstance(TimeFunctionFactory.getInstance("ConstantVariate", 100), et, root, cGen);        
		
		StdInfoView debugView = new StdInfoView();
		sim.addInfoReceiver(debugView);
		return sim;
	}
	

}


public class TestProbabilitySelectionFlow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ExperimentProbSel().start();
	}

}

package com.ull;

import com.ull.simulation.factory.SimulationFactory;
import com.ull.simulation.inforeceiver.StdInfoView;
import com.ull.simulation.model.Experiment;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.TimeStamp;
import com.ull.simulation.model.TimeUnit;
import com.ull.simulation.parallel.ParallelSimulationEngine;
import com.ull.simulation.variable.EnumType;
import com.ull.simulation.variable.EnumVariable;
import com.ull.simulation.variable.IntVariable;

/**
 * 
 */
class ExperimentTest1 extends Experiment {
	final static int NTHREADS = 2;
	final static TimeUnit unit = TimeUnit.MINUTE;
	static final int NEXP = 1;
    static final int NDAYS = 1;
	static final double PERIOD = 1040.0;
	
	public ExperimentTest1() {
		super("Banco", NEXP);
	}

	public Simulation getSimulation(int ind) {
		SimulationFactory factory = new SimulationFactory(ind, "Ej", unit, TimeStamp.getZero(), new TimeStamp(TimeUnit.DAY, NDAYS));
		Simulation sim = factory.getSimulation();

		factory.getFlowInstance("ActivityFlow", "Verificar cuenta", 0, false, false);
        
    	sim.putVar("Coste total", new IntVariable(0));
    	sim.putVar("Coste", new IntVariable(200));
    	
    	IntVariable temp = (IntVariable) sim.getVar("Coste");
    	temp.setValue(temp.getValue().intValue() * 10);  	
    	System.out.println("Coste de la actividad = " + sim.getVar("Coste").toString());
    	
    	((IntVariable)sim.getVar("Coste total")).setValue(sim.getVar("Coste").getValue());
    	System.out.println("Coste total= " + sim.getVar("Coste total").toString());
    	
    	EnumType type = new EnumType("Deportivo", "Familiar", "Gasoil");
    	sim.putVar("tipoCoche", new EnumVariable(type, Integer.valueOf(0)));
    	((EnumVariable)sim.getVar("tipoCoche")).setValue(2);
    	System.out.println("Valor del enumerado: " + sim.getVar("tipoCoche").toString());
    	((EnumVariable)sim.getVar("tipoCoche")).setValue("Deportivo");
    	System.out.println("Valor del enumerado: " + sim.getVar("tipoCoche").toString());

		sim.addInfoReceiver(new StdInfoView());
		if (NTHREADS > 1) {
			sim.setSimulationEngine(new ParallelSimulationEngine(ind, sim, NTHREADS));
		}
		return sim;
	}
	

}

public class VariableManagerTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ExperimentTest1().start();
	}

}

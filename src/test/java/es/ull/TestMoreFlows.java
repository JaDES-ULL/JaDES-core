package es.ull;

import org.junit.jupiter.api.Test;

import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.model.Simulation;

public class TestMoreFlows {
    public static class TestMoreFlowsExperiment extends BaseExperiment {
        public TestMoreFlowsExperiment(CommonArguments args) {
            super("Testing More Flows", args);
        }

        @Override
        public void runExperiment(int ind) {
            Simulation simul = null;
            switch (ind) {
                case 0:
                    simul = new WaitForSignalFlowTestSimulation();
                    break;            
                default:
                    break;
            }
            simul.run();
        }
    }

    @Test
    public void tests() {
        final CommonArguments arguments = new CommonArguments();
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(arguments);
        experiment.run();
    }
}

package es.ull;

import org.junit.jupiter.api.Test;

import es.ull.StandardTestSimulation.TestArguments;
import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.model.Simulation;

public class TestMoreFlows {
    public static class TestMoreFlowsExperiment extends BaseExperiment {
        final private int expId;
        final private TestArguments args;
        public TestMoreFlowsExperiment(int expId, TestArguments args) {
            super("Testing More Flows", args);
            this.expId = expId;
            this.args = args;
        }

        @Override
        public void runExperiment(int ind) {
            Simulation simul = null;
            switch (expId) {
                case 0:
                    simul = new WaitForSignalFlowTestSimulation(args);
                    break;            
                case 1:
                    simul = new ConditionalResourceGeneratorTestSimulation(args);
                    break;
                case 2:
                    simul = new InterruptibleActivitiesTestSimulation();
                    break;
                default:
                    break;
            }
            simul.run();
        }
    }

    @Test
    public void test1() {
        final TestArguments arguments = new TestArguments();
        arguments.simEnd = 35;
        arguments.nElements = 10;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(0, arguments);
        experiment.run();
    }

    @Test
    public void test2() {
        final TestArguments arguments = new TestArguments();
        arguments.simEnd = 60;
        arguments.nElements = 1;
        arguments.resStart = 0;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(1, arguments);
        experiment.run();
    }

    @Test
    public void test3() {
        final TestArguments arguments = new TestArguments();
        arguments.debug = true;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(2, arguments);
        experiment.run();
    }
}

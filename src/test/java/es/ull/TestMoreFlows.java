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
                    args.simEnd = 35;
                    args.nElements = 10;
                    simul = new TestWaitForSignalFlowSimulation(args);
                    break;            
                case 1:
                    args.simEnd = 60;
                    args.nElements = 1;
                    args.resStart = 0;
                    simul = new TestConditionalResourceGeneratorSimulation(args);
                    break;
                case 2:
                    args.simEnd = 400L;
                    args.nElements = 1;
                    args.resStart = 20L;
                    args.resPeriod	= 100L;
                    args.resAvailability = 40L;
                    simul = new TestInterruptibleActivitiesSimulation(args);
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
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(0, arguments);
        experiment.run();
    }

    @Test
    public void test2() {
        final TestArguments arguments = new TestArguments();
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(1, arguments);
        experiment.run();
    }

    @Test
    public void test3() {
        final TestArguments arguments = new TestArguments();
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(2, arguments);
        experiment.run();
    }
}

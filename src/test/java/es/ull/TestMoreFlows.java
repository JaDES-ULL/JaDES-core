package es.ull;

import org.junit.jupiter.api.Test;

import com.beust.jcommander.Parameter;

import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.model.Simulation;

public class TestMoreFlows {
    public static class TestMoreFlowsExperiment extends BaseExperiment {
        final private int expId;
        public TestMoreFlowsExperiment(MoreArguments args) {
            super("Testing More Flows", args);
            expId = args.expId;
        }

        @Override
        public void runExperiment(int ind) {
            Simulation simul = null;
            switch (expId) {
                case 0:
                    simul = new WaitForSignalFlowTestSimulation();
                    break;            
                case 1:
                    simul = new ConditionalResourceGeneratorTestSimulation();
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
        final MoreArguments arguments = new MoreArguments();
        arguments.expId = 0;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(arguments);
        experiment.run();
    }

    @Test
    public void test2() {
        final MoreArguments arguments = new MoreArguments();
        arguments.expId = 1;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(arguments);
        experiment.run();
    }

    @Test
    public void test3() {
        final MoreArguments arguments = new MoreArguments();
        arguments.expId = 2;
        arguments.debug = true;
        final TestMoreFlowsExperiment experiment = new TestMoreFlowsExperiment(arguments);
        experiment.run();
    }

    public class MoreArguments extends CommonArguments {
        @Parameter (names = "-exp", description = "Identifier of the experiment to run")
        public int expId = 0;
    }
}

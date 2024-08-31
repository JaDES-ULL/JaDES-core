package es.ull.simulation.experiment;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.Simulation;

/** 
 * Commonly used arguments for the simulation experiments.
 */
public class CommonArguments {
	@Parameter(names = { "--runs", "-r" }, description = "Number of simulation experiments to launch", order = 1)
	public int nRuns = 1;
 	@Parameter(names = { "--seed", "-s" }, description = "Seed for the random number generator", order = 3)
	public long seed = IExperiment.getSeed();
	@Parameter(names = { "--horizon", "-h" }, description = "Time horizon for the simulation (years)", order = 2)
	public int timeHorizon = -1;
	@Parameter(names = { "--nthreads", "-th" }, description = "Sets a specific number of threads to run the experiments in parallel (by default, the number of available processors)", order = 5)
	public int nThreads = Runtime.getRuntime().availableProcessors();
	@Parameter(names = { "--parallel", "-p" }, description = "Enables parallel execution", order = 4)
	public boolean parallel = false;
	@Parameter(names = { "--quiet", "-q" }, description = "Quiet execution (does not print progress info)", order = 3)
	public boolean quiet = false;
	@Parameter(names = { "--timeunit", "-tu" }, description = "Time unit for the simulation. Must be one of 'Y' (YEAR), 'M' (MONTH), 'W' (WEEK), 'D' (DAY), 'h' (HOUR), 'm' (MINUTE), 's' (SECOND), 'l' (MILLISECOND)", validateWith = TimeUnitValidator.class, converter = TimeUnitConverter.class, order = 6)
	public TimeUnit timeUnit = Simulation.DEF_TIME_UNIT;    

	public static class TimeUnitValidator implements IParameterValidator {
		@Override
		public void validate(String name, String value) throws ParameterException {
			if (value.length() > 1) {
				throw new ParameterException("Found " + value + "as time unit. Must be a single character: 'Y' (YEAR), 'M' (MONTH), 'W' (WEEK), 'D' (DAY), 'h' (HOUR), 'm' (MINUTE), 's' (SECOND), 'l' (MILLISECOND)");
			}
			switch(value.charAt(0)) {
				case 'Y':
				case 'M':
				case 'W':
				case 'D':
				case 'h':
				case 'm':
				case 's':
				case 'l':
					break;
				default:
					throw new ParameterException("Found " + value + "as time unit. Must be a single character: 'Y' (YEAR), 'M' (MONTH), 'W' (WEEK), 'D' (DAY), 'h' (HOUR), 'm' (MINUTE), 's' (SECOND), 'l' (MILLISECOND)");
			}
		}
	}
	public static class TimeUnitConverter implements IStringConverter<TimeUnit> {
		@Override
		public TimeUnit convert(String value) {
			switch(value.charAt(0)) {
				case 'Y': return TimeUnit.YEAR;
				case 'M': return TimeUnit.MONTH;
				case 'W': return TimeUnit.WEEK;
				case 'D': return TimeUnit.DAY;
				case 'h': return TimeUnit.HOUR;
				case 'm': return TimeUnit.MINUTE;
				case 's': return TimeUnit.SECOND;
				case 'l': return TimeUnit.MILLISECOND;
				default:
					throw new ParameterException("Found " + value + "as time unit. Must be a single character: 'Y' (YEAR), 'M' (MONTH), 'W' (WEEK), 'D' (DAY), 'h' (HOUR), 'm' (MINUTE), 's' (SECOND), 'l' (MILLISECOND)");
			}
		}
	}
}

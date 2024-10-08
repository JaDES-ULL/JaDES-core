package es.ull.simulation.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.flow.IFlow;

/**
 * Generate IFlow's instances.
 * @author ycallero
 *
 */
public class FlowFactory {
	private final static String workingPkg = IFlow.class.getPackage().getName();
	static int id = 0;
	/**
     * A list of packages to search for RandomVariates if the
     * class name given is not fully qualified.
     **/
    protected static Set<String> searchPackages;
    /**
     * Holds a cache of the RandomVariate Classes that have already been
     * found indexed by their name.
     **/
    protected static Map<String, Class<?>> cache;
	
    /**
     * If true, print out information while searching for RandomVariate
     * Classes.
     **/
    protected static boolean verbose;
    
	/**
	 * Create de IFlow's classes index.
	 */
	static {
		searchPackages = new LinkedHashSet<String>();
        searchPackages.add(workingPkg);
        cache = new WeakHashMap<String, Class<?>>();
	}
	
	/**
	 * Finds the IFlow Class corresponding to the given name. First
	 * attempts to find the IFlow assuming the the name is fully qualified.
	 * Then searches the "search packages." The search path defaults to "com.ull.model.IFlow"
	 * but additional search packages can be added.
	 **/
	public static Class<?> findFullyQualifiedNameFor(String className) {
		Class<?> theClass = null;
		
        // First check cache
        theClass = cache.get(className);
        if (theClass != null)
        	return theClass;
        
        // If not, see if name passed is "fully qualified"
		try {
			theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			cache.put(className, theClass);
			return theClass;
		}
		//        If not, then try the search path
		catch (ClassNotFoundException e) {}
		for (String searchPackage : searchPackages) {
			if (verbose) {
				System.out.println("Checking " + searchPackage + "." + className);
			}
			try {
				theClass = Thread.currentThread().getContextClassLoader().loadClass(
						searchPackage + "." + className );
				if (!IFlow.class.isAssignableFrom(theClass)) {
					continue;
				}
			} catch (ClassNotFoundException e) { continue; }
		}
		if (verbose) {System.out.println("returning " + theClass);}
		if (theClass != null)
			cache.put(className, theClass);
		return theClass;
	}
	
	static private String createConstructor(String flowType, Integer id, Class<?>... params) {
		String code = "(Simulation simul";
		String strParam = ") {super(simul";
		for (int i = 0; i < params.length; i++) {
			code += ", " + params[i].getSimpleName() + " arg" + i;
			strParam += ", arg" + i;
		}
		return code + strParam + ");}";
	}
	
	/**
	 * Get a IFlow's instance.
	 * @param id Identifier.
	 * @param flowType IFlow's type.
	 * @param simul Actual simulation.
	 * @param initargs Rest of the params.
	 * @return A IFlow's instance.
	 */
	static public IFlow getInstance(int id, String flowType, Simulation simul, Object... initargs) {
		Class<?> cl = findFullyQualifiedNameFor(flowType);
		Constructor<?>[] consList = cl.getConstructors();
		Object [] newParams = new Object[initargs.length + 1];
		newParams[0] = simul;
		for (int i = 1; i < newParams.length; i++)
			newParams[i] = initargs[i - 1];
		for (int i = 0; i < consList.length; i++) {
			try {
				return (IFlow)consList[i].newInstance(newParams);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if (i == consList.length - 1)
					e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;	
	}

	static public IFlow getInstance(int id, String flowType, SimulationUserCode userMethods, Simulation simul, Object... initargs) {
		userMethods.addImports("import " + Simulation.class.getPackage().getName() + ".*;");
		Class<?>[] parameterTypes = StandardCompilator.param2Classes(initargs);
		String cons = createConstructor(flowType, id, parameterTypes);
		Object [] newParams = new Object[initargs.length + 1];
		newParams[0] = simul;
		for (int i = 1; i < newParams.length; i++)
			newParams[i] = initargs[i - 1];
		return (IFlow)StandardCompilator.getInstance(workingPkg, flowType, id, cons, userMethods, newParams);
	}
}
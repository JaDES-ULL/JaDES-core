package es.ull.simulation.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.variable.IntVariable;
import es.ull.simulation.variable.IVariable;

/**
 * Generate Condition's instances. We can obtain a predefined Condition
 * like PercentageCondition, NotCondition,... Moreover, we can define a 
 * condition through a logic expresion which can use simulation variables.
 * @author ycallero
 *
 */
public class ConditionFactory {
	private final static String workingPkg = AbstractCondition.class.getPackage().getName();
	
	/**
	 * Generate a new condition through a logic expression. Parse the
	 * expression and generate the string with the new Condition's code.  
	 * @param id Identifier.
	 * @param imports Imports code indexed by "imports".
	 * @param condition Logic expression indexed by "logicExp".
	 * @return A string with the new class code.
	 */
	static private String generateClass(Integer id, String imports, String condition) {
		
		String finalCode = new String();

		// Package
		finalCode += "package " + workingPkg + ";";

		// Include imports
		finalCode += "import " + AbstractCondition.class.getName() + ";";
		finalCode += "import " + ElementInstance.class.getName() + ";";
		finalCode += "import " + IntVariable.class.getName() + ";";
		finalCode += "import " + IVariable.class.getName() + ";";
		
		if (imports != null)
			finalCode += imports;
		
		// Class denifition
		finalCode += "public class CompiledCondition" + id + " extends Condition {";
		
		// Constructor
		finalCode += "public CompiledCondition" + id + 
					"() {super();}";
		
		finalCode += "public boolean check(ElementInstance ei){" + "return("
				+ StandardCompilator.getCode(condition, "logicExp") + ");" + "}";
		
		// Class close
		finalCode += "}";
		
		return finalCode;
	}
	
	/**
	 * Get the constructors of the class, compiled or not.
	 * @param src Identifier of Java File Object used to store the java code
	 * @return An array of constructors.
	 */
	static private Constructor<?> getConstructor(StringJFO src) {
		try {
			
			Class<?> cl;
			ByteClassLoader loader = new ByteClassLoader(StandardCompilator.getBytecodeCache());
			if (loader.isCompiledClass(workingPkg + "." + src.getClassName()))
				StandardCompilator.getBytecodeCache().remove(workingPkg + "." + src.getClassName());
			StandardCompilator.compileCode(src);
			cl = loader.loadClass(workingPkg + "." + src.getClassName());
			Constructor<?> cons = cl.getConstructor();
			return cons;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}		
	
	/**
	 * Get a Condition's instance.
	 * @param id Identifier.
	 * @param imports Imports of the Condition code.
	 * @param condition Condition code.
	 * @return A Condition's instance.
	 */
	static public AbstractCondition<ElementInstance> getInstance(int id, String imports, String condition){
		String classCode = generateClass(id, imports, condition);
		StringJFO src = null;
		try {
			src = new StringJFO("CompiledCondition" + id, classCode);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		// Obtain the Class's constructors.
		Constructor<?> cons = getConstructor(src);
		try {
			return (AbstractCondition<ElementInstance>) cons.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}

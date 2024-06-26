package es.ull.simulation.info;

import es.ull.simulation.model.Simulation;

public class VarViewValueRequestInfo extends SynchronousInfo {

	private final String varName;
	private final Object requestObject;
	private Object[] params;
	
	public VarViewValueRequestInfo(final Simulation model, final String varName,
								   final Object requestObject, final Object[] params, final long ts) {
		super(model, ts);
		this.varName = varName;
		this.requestObject = requestObject;
		this.params = params;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(final Object[] params) {
		this.params = params;
	}

	public String getVarName() {
		return varName;
	}

	public Object getRequestObject() {
		return requestObject;
	}

	@Override
	public String toString() {
		String message = new String();
		message += simul.long2SimulationTime(ts) + "\tVARVIEWVALUEREQUEST:\t" + varName +
				"\tREQOBJ: " + requestObject.toString() + "\t" + simul.toString() + "\n";
		for (int i = 0; i < params.length; i++)
			message += "\tPARAM" + (i+1) + ": " + params[i].toString();
		return message;
	}

}

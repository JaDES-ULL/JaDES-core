package es.ull.simulation.inforeceiver;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import es.ull.simulation.info.AsynchronousInfo;
import es.ull.simulation.info.ElementActionInfo;
import es.ull.simulation.info.ElementInfo;
import es.ull.simulation.info.EntityLocationInfo;
import es.ull.simulation.info.ResourceInfo;
import es.ull.simulation.info.ResourceUsageInfo;
import es.ull.simulation.info.SimulationInfo;
import es.ull.simulation.info.SimulationStartStopInfo;
import es.ull.simulation.info.SynchronousInfo;
import es.ull.simulation.info.TimeChangeInfo;
import es.ull.simulation.info.UserInfo;
import es.ull.simulation.info.VarViewValueRequestInfo;

public class SimulationInfoHandler implements IInfoHandler {

	private HashSet<Class<?>> definedInfos;
	private HashMap<Class<?>, ArrayList<InfoReceiver> > entranceList;
	private HashSet<Class<?>> notFinalicedUserInfos;
	
	public SimulationInfoHandler() {
		definedInfos = new HashSet<Class<?>>();
		notFinalicedUserInfos = new HashSet<Class<?>>();
		entranceList = new HashMap<Class<?>, ArrayList<InfoReceiver>>();
		initEntranceList();
	}
	
	private void initEntranceList() {
		definedInfos.add(SimulationStartStopInfo.class);
		definedInfos.add(ElementActionInfo.class);
		definedInfos.add(ElementInfo.class);
		definedInfos.add(ResourceInfo.class);
		definedInfos.add(ResourceUsageInfo.class);
		definedInfos.add(EntityLocationInfo.class);
		definedInfos.add(TimeChangeInfo.class);
	}
		
	private boolean isUserInfo(Type type) {
		if (type == null)
			return false;
		if (type.equals(UserInfo.class))
			return true;
		return false;
	}
	
	private void addDefinedInfos(ArrayList<Class<?>> infos) {
		definedInfos.addAll(infos);
		for(Class<?> info: infos) {
			if (isUserInfo(info.getGenericSuperclass()))
				notFinalicedUserInfos.add(info);
		}
	}
	
	private void indexReceiver(InfoReceiver receiver) {
		for (Class<?> cl: receiver.getEntrance()) {
			if (definedInfos.contains(cl)){
				ArrayList<InfoReceiver> list = entranceList.get(cl);
				if (list != null)
					list.add(receiver);
				else {
					list = new ArrayList<InfoReceiver>();
					list.add(receiver);
					entranceList.put(cl, list);
				}
			} else {
				Error err = new Error("Unable to index " + receiver.getClass().toString() + " to info " + cl.toString());
				err.printStackTrace();
			}				
		}
	}
	
	public void registerReceivers(InfoReceiver receiver) {	
		if (receiver instanceof Listener)
			addDefinedInfos(((Listener) receiver).getGeneratedInfos());
		indexReceiver(receiver);
	}

	public void asynchronousInfoProcessing(AsynchronousInfo info) {
		if ((info instanceof UserInfo) && ((UserInfo)info).isFinalInfo()) 
			notFinalicedUserInfos.remove(info);
		ArrayList<InfoReceiver> list = entranceList.get(info.getClass());
		if (list != null)
			for(InfoReceiver reciever: list)
				reciever.infoEmited(info);
	}

	public Number notifyInfo(SimulationInfo info) {
		if (info instanceof AsynchronousInfo)
			asynchronousInfoProcessing((AsynchronousInfo)info);
		else
			if (info instanceof SynchronousInfo)
				return synchronousInfoProcessing((SynchronousInfo)info);
		return null;
	}

	public Number synchronousInfoProcessing(SynchronousInfo info) {
		if (info instanceof VarViewValueRequestInfo) {
			VarViewValueRequestInfo reqInfo = (VarViewValueRequestInfo) info;
			Number value = reqInfo.getSimul().getVar(reqInfo.getVarName()).getValue(reqInfo.getParams());
			if (value != null)
				return value.doubleValue();
		}
		return -1;
	}
	
}

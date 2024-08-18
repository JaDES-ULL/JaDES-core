/**
 * 
 */
package es.ull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import es.ull.simulation.info.ResourceInfo;
import es.ull.simulation.info.ResourceUsageInfo;
import es.ull.simulation.info.SimulationInfo;
import es.ull.simulation.info.SimulationStartStopInfo;
import es.ull.simulation.inforeceiver.Listener;
import es.ull.simulation.model.ResourceType;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class CheckResourcesListener extends Listener {
	private final static String ERROR_ROLON1 = "Wrong activation time of resource role";
	private final static String ERROR_ROLON2 = "Resource activating for unexpected role";
	private final static String ERROR_ROLON3 = "Resource never activated for role";
	private final static String ERROR_ROLOFF1 = "Wrong deactivation time of resource role";
	private final static String ERROR_ROLOFF2 = "Resource deactivating for unexpected role";
	private final static String ERROR_ROLOFF3 = "Resource never deactivated for role";
	private final static String ERROR_RESCREATED = "Not all the resources were created";
	private final static String ERROR_RESFINISHED = "Not all the resources were finished";
	private final static String ERROR_SEIZE = "Resource already seized";
	private final static String ERROR_RELEASE = "Resource not previously seized";
	private final int resources;
	private int resCreated;
	private int resFinished;
	private boolean []inUse;
	private final ArrayList<TreeMap<Integer, Long>> roleOns;
	private final ArrayList<TreeMap<Integer, Long>> roleOffs;
	private final ArrayList<TreeMap<Integer, Boolean>> checkedroleOns;
	private final ArrayList<TreeMap<Integer, Boolean>> checkedroleOffs;

	public CheckResourcesListener(final int resources, final ArrayList<TreeMap<Integer, Long>> roleOns, final ArrayList<TreeMap<Integer, Long>> roleOffs) {
		super("Resource checker");
		this.resources = resources;
		this.resCreated = 0;
		this.resFinished = 0;
		this.inUse = new boolean[resources];
		Arrays.fill(inUse, false);
		this.roleOffs = roleOffs;
		this.roleOns = roleOns;
		this.checkedroleOns = new ArrayList<TreeMap<Integer, Boolean>>();
		this.checkedroleOffs = new ArrayList<TreeMap<Integer, Boolean>>();
		for (int i = 0; i < this.roleOns.size(); i++) {
			checkedroleOns.add(new TreeMap<Integer, Boolean>());
			for (final Integer key : this.roleOns.get(i).keySet()) {
				checkedroleOns.get(i).put(key, false);
			}
		}
		for (int i = 0; i < this.roleOffs.size(); i++) {
			checkedroleOffs.add(new TreeMap<Integer, Boolean>());
			for (final Integer key : this.roleOffs.get(i).keySet()) {
				checkedroleOffs.get(i).put(key, false);
			}
		}
		addEntrance(ResourceInfo.class);
		addEntrance(ResourceUsageInfo.class);
	}

	@Override
	public void infoEmited(final SimulationInfo info) {
		if (info instanceof ResourceInfo) {
			final ResourceInfo rInfo = (ResourceInfo)info;
			final int resId = rInfo.getResource().getIdentifier();
			final ResourceType rt = rInfo.getResourceType();
			final int roleId = (rt != null) ? rt.getIdentifier() : -1;
			switch(rInfo.getType()) {
			case ROLON:
				assertTrue(roleOns.get(resId).containsKey(roleId), rInfo.getResource().toString() + "\t" + ERROR_ROLON2 + "\t" + rInfo.getResourceType().toString());
				if (roleOns.get(resId).containsKey(roleId))
					assertEquals(rInfo.getTs(),  roleOns.get(resId).get(roleId), rInfo.getResource().toString() + "\t" + ERROR_ROLON1);
				checkedroleOns.get(resId).put(roleId, true);
				resCreated++;
				break;
			case ROLOFF:
				assertTrue(roleOffs.get(resId).containsKey(roleId), rInfo.getResource().toString() + "\t" + ERROR_ROLOFF2 + "\t" + rInfo.getResourceType().toString());
				if (roleOffs.get(resId).containsKey(roleId))
					assertEquals(rInfo.getTs(),  roleOffs.get(resId).get(roleId), rInfo.getResource().toString() + "\t" + ERROR_ROLOFF1);
				checkedroleOffs.get(resId).put(roleId, true);
				resFinished++;
				break;
			default:
				break;
			}
		}
		else if (info instanceof ResourceUsageInfo) {
			final ResourceUsageInfo rInfo = (ResourceUsageInfo)info;
			final int resId = rInfo.getResource().getIdentifier();
			switch(rInfo.getType()) {
				case CAUGHT:
					assertFalse(inUse[resId], rInfo.getResource().toString() + "\t" + ERROR_SEIZE);
					inUse[resId] = true;
					break;
				case RELEASED:
					assertTrue(inUse[resId], rInfo.getResource().toString() + "\t" + ERROR_RELEASE);
					inUse[resId] = false;
					break;
				default:
					break;			
			}
		}
		else if (info instanceof SimulationStartStopInfo) {
			final SimulationStartStopInfo tInfo = (SimulationStartStopInfo) info;
			if (SimulationStartStopInfo.Type.END.equals(tInfo.getType()))  {
				assertEquals(resCreated, resources, ERROR_RESCREATED);
				assertEquals(resFinished, resources, ERROR_RESFINISHED);
				for (int i = 0; i < roleOns.size(); i++) {
					for (final Integer key : roleOns.get(i).keySet()) {
						assertTrue(checkedroleOns.get(i).get(key), ERROR_ROLON3 + "\t" + roleOns.get(i).get(key));
					}
				}
				for (int i = 0; i < roleOffs.size(); i++) {
					for (final Integer key : roleOffs.get(i).keySet()) {
						assertTrue(checkedroleOffs.get(i).get(key), ERROR_ROLOFF3 + "\t" + roleOns.get(i).get(key));
					}
				}
			}
		}
	}
}

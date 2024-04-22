/**
 * 
 */
package es.ull.simulation.model.engine;

import es.ull.simulation.model.Identifiable;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public interface ElementInstanceEngine extends Identifiable {
	void notifyResourcesAcquired();

}

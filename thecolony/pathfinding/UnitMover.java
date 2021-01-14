/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.pathfinding;

import java.io.Serializable;

/**
 *
 * @author pdogmuncher
 */
public class UnitMover implements Serializable{
	/** The unit ID moving */
	private int type;
	
	/**
	 * Create a new mover to be used while path finder
	 * 
	 * @param type The ID of the unit moving
	 */
	public UnitMover(int type) {
		this.type = type;
	}
	
	/**
	 * Get the ID of the unit moving
	 * 
	 * @return The ID of the unit moving
	 */
	public int getType() {
		return type;
	}
}

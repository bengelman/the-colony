/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.pathfinding;

import java.io.Serializable;
import thecolony.World;

/**
 *
 * @author pdogmuncher
 */
public class AStarHeuristic implements Serializable{
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(World map, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		
		return result;
	}

}

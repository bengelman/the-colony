/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import thecolony.entity.behavior.AI;
import thecolony.entity.Entity;
import thecolony.entity.EntityVehicle;

/**
 *
 * @author pdogmuncher
 */
public class AIVehicle extends AI{
    public EntityVehicle v;
    @Override
    public void onUpdate() {
        if (v.rider != null){
            v.x = v.rider.x;
            v.y = v.rider.y;
            v.xvel = v.rider.xvel;
            v.yvel = v.rider.yvel;
            
        }
        
    }

    @Override
    public void onCollide(Entity with) {
        
    }
    
}

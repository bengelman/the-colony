/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.criminal;

import thecolony.entity.behavior.AINPC;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.EntityLiving;

/**
 *
 * @author pdogmuncher
 */
public class EntityChahTenn extends EntityLiving{
    public EntityChahTenn(World world, double x, double y, int w, int h, int height){
        super(world, x, y, w, h, height, new String[][]{{"chahtenn.png"}}, 50, 0, new AINPC(), 0);
        world.chahtenn = this;
    }
    @Override
    public void takeDamage(int amount, EntityLiving attacker){
        
    }
}

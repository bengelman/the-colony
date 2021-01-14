/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import thecolony.entity.criminal.EntityHostileInmate;
import thecolony.entity.triad.EntityPrisonGuard;
import thecolony.entity.triad.EntityTriadSoldier;
import thecolony.entity.triad.EntityThug;
import thecolony.entity.criminal.EntityThief;
import thecolony.entity.criminal.EntityChahMinion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import thecolony.entity.behavior.AIFighter;
import thecolony.World;
import thecolony.entity.behavior.AINPC;
import thecolony.entity.criminal.EntityChahBoss;
import thecolony.entity.criminal.EntityMichaelChuck;

/**
 *
 * @author pdogmuncher
 */
public class EntityExplodable extends EntityLiving{
    //public ArrayList<Class> targets;
    
    
    public EntityExplodable(World world, double x, double y, int w, int h, int height, String[][] texture){
        super(world, x, y, w, h, height, texture, 1, 0, new AINPC(), 0);
        
    }
    @Override
    public void takeDamage(int amount, EntityLiving attacker){
        world.entities.add(new EntityExplosion(world, x, y, 50, 0, 50, this, 50, "tnt"));
        this.world.entities.remove(this);
    }
}

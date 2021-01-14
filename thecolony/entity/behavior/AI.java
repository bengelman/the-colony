/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;
import thecolony.entity.criminal.EntityThief;
import thecolony.entity.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author pdogmuncher
 */
public abstract class AI implements Serializable{
    public EntityLiving e;
    /*
    public static final ArrayList<Class> ENEMIES = new ArrayList(Arrays.asList(new Class[]{
        EntityThief.class
    }));
    public static final ArrayList<Class> GOODGUYS = new ArrayList(Arrays.asList(new Class[]{
        EntityPlayer.class
    }));*/
    public AI(){
        
    }
    public abstract void onUpdate();
    public abstract void onCollide(Entity with);
}

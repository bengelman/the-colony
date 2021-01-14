/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import java.io.Serializable;
import thecolony.entity.Entity;
import thecolony.entity.EntityLiving;

/**
 *
 * @author brend
 */
public abstract class MoveScript implements Serializable{
    protected int time = 0;
    public abstract void step(Entity entity);
}

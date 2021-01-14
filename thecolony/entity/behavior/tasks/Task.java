/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import java.io.Serializable;
import thecolony.entity.Entity;
import thecolony.entity.EntityLiving;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public abstract class Task implements Serializable{
    /**
     * 0: compatible with all
     * 1: targeting
     * 2: movement
     * 3: shooting
     */
    public AISmart ai;
    public Task(AISmart ai){
        this.ai = ai;
    }
    public abstract boolean shouldExecute(EntityLiving entity);
    public abstract void startExecuting(EntityLiving entity);
    public abstract boolean shouldContinue(EntityLiving entity);
    public abstract void execute(EntityLiving entity);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import thecolony.entity.behavior.tasks.Task;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityLiving;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public class TaskMelee extends Task{

    public TaskMelee(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entity) {
        return EntityFighter.groups.get(((EntityFighter)entity).targets).contains(ai.colliding.getClass()) && entity.meleetimer == 0;
    }

    @Override
    public void startExecuting(EntityLiving entity) {
        
    }

    @Override
    public boolean shouldContinue(EntityLiving entity) {
        return false;
    }

    @Override
    public void execute(EntityLiving entity) {
        entity.meleetimer = entity.weapon.meleetimer;
        ((EntityLiving)ai.colliding).takeDamage(entity.strength + entity.weapon.melee, entity);
        ai.colliding.xvel = (ai.colliding.x - entity.x) / 2;
        ai.colliding.yvel = (ai.colliding.y - entity.y) / 2;
    }
    
}

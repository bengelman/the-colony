/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import thecolony.entity.Entity;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityLiving;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public class TaskTargetNextEnemy extends Task{

    public TaskTargetNextEnemy(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entitye) {
        if (ai.enemyTarget != null){
            return true;
        }
        EntityFighter entity = (EntityFighter) entitye;
        EntityLiving targeted = null;
        int closeness = 0;
        for (Entity target : entity.world.entities){
            if (target instanceof EntityLiving){
                if (EntityFighter.groups.get(entity.targets).contains(target.getClass())){
                    if (targeted == null){
                        targeted = (EntityLiving) target;
                        closeness = (int)Math.sqrt(Math.pow(Math.abs(entity.x - targeted.x), 2) + Math.pow(Math.abs(entity.y - targeted.y), 2));
                    }
                    else{
                        if ((int)Math.sqrt(Math.pow(Math.abs(entity.x - targeted.x), 2) + Math.pow(Math.abs(entity.y - targeted.y), 2)) < closeness){
                            closeness = (int)Math.sqrt(Math.pow(Math.abs(entity.x - targeted.x), 2) + Math.pow(Math.abs(entity.y - targeted.y), 2));
                            targeted = (EntityLiving)target;
                            
                        }
                    }
                }
            }
        }
        if (targeted != null){
            ai.enemyTarget = targeted;
            return true;
        }
        return false;
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
        
    }
    
}

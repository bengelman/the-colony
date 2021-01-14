/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import thecolony.entity.EntityLiving;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.Pathfinder;

/**
 *
 * @author pdogmuncher
 */
public class TaskMeleeCharge extends Task{

    public TaskMeleeCharge(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entity) {
        if (ai.enemyTarget == null) {
            return false;
        }
        if (!entity.weapon.isMelee){
            return false;
        }
        return !(Math.abs(entity.x - ai.enemyTarget.x) > AISmart.TRACKING_RANGE || Math.abs(entity.y - ai.enemyTarget.y) > AISmart.TRACKING_RANGE);
    }

    @Override
    public void startExecuting(EntityLiving entity) {
        ai.pathfinder = new Pathfinder(ai.enemyTarget, AISmart.TRACKING_RANGE, entity);
        
    }

    @Override
    public boolean shouldContinue(EntityLiving entity) {
        return shouldExecute(entity);
    }

    @Override
    public void execute(EntityLiving entity) {
        
    }
    
}

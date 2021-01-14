/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import java.awt.geom.Line2D;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityVehicle;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.Pathfinder;

/**
 *
 * @author pdogmuncher
 */
public class TaskMoveToShoot extends Task{

    public TaskMoveToShoot(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entity) {
        if (ai.enemyTarget == null || entity.weapon.isMelee){
            return false;
        }
        if (entity.weapon.isMelee){
            return false;
        }
        if (Math.abs(entity.x - ai.enemyTarget.x) > AISmart.TRACKING_RANGE || Math.abs(entity.y - ai.enemyTarget.y) > AISmart.TRACKING_RANGE){
            return false;
        }
        if (Math.abs(entity.x - ai.enemyTarget.x) < (entity.weapon.range * entity.weapon.bulletVelocity) && Math.abs(entity.y - ai.enemyTarget.y) < (entity.weapon.range * entity.weapon.bulletVelocity)){

            Line2D line = new Line2D.Float((float)entity.x + entity.w / 2, (float)entity.y + entity.h / 2, (float)ai.enemyTarget.x + ai.enemyTarget.w / 2, (float)ai.enemyTarget.y + ai.enemyTarget.h / 2);
            boolean valid = true;
            for (int j = 0; j < entity.world.entities.size() && valid; j++){
                if (entity.world.entities.get(j).getBounds().intersectsLine(line) && entity.world.entities.get(j) != entity && entity.world.entities.get(j) != ai.enemyTarget && !(entity.world.entities.get(j) instanceof EntityVehicle)){

                    return true;
                }
            }
            if (valid) {
                return false;
            }

        }
        return true;
    }

    @Override
    public void startExecuting(EntityLiving entity) {
        ai.pathfinder = new Pathfinder(ai.enemyTarget, AISmart.TRACKING_RANGE, entity);
        
    }

    @Override
    public boolean shouldContinue(EntityLiving entity) {
        if (ai.pathfinder == null){
            return false;
        }
        return shouldExecute(entity);
    }

    @Override
    public void execute(EntityLiving entity) {
        
    }
    
}

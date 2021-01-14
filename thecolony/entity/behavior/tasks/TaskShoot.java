/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior.tasks;

import java.awt.Point;
import java.awt.geom.Line2D;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityVehicle;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public class TaskShoot extends Task{

    public TaskShoot(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entity) {
        if (ai.enemyTarget == null){
            return false;
        }
        if (entity.weapon.isMelee){
            return false;
        }
        if (Math.abs(entity.x - ai.enemyTarget.x) < (entity.weapon.range * entity.weapon.bulletVelocity) && Math.abs(entity.y - ai.enemyTarget.y) < (entity.weapon.range * entity.weapon.bulletVelocity)){

            Line2D line = new Line2D.Float((float)entity.x + entity.w / 2, (float)entity.y + entity.h / 2, (float)ai.enemyTarget.x + ai.enemyTarget.w / 2, (float)ai.enemyTarget.y + ai.enemyTarget.h / 2);
            boolean valid = true;
            for (int j = 0; j < entity.world.entities.size() && valid; j++){
                if (entity.world.entities.get(j).getBounds().intersectsLine(line) && entity.world.entities.get(j) != entity && entity.world.entities.get(j) != ai.enemyTarget && !(entity.world.entities.get(j) instanceof EntityVehicle)){

                    valid = false;
                }
            }
            if (valid) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void startExecuting(EntityLiving entity) {
        
    }

    @Override
    public boolean shouldContinue(EntityLiving entity) {
        return shouldExecute(entity);
    }

    @Override
    public void execute(EntityLiving entity) {
        entity.facingRight = entity.x < ai.enemyTarget.x;
        entity.facingBack = entity.y > ai.enemyTarget.y + 100;
        entity.weapon.fireBullet(new Point((int)ai.enemyTarget.x, (int)ai.enemyTarget.y), entity);
    }
    
}

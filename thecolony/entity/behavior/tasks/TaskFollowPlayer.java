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
public class TaskFollowPlayer extends Task{

    public TaskFollowPlayer(AISmart ai) {
        super(ai);
    }

    @Override
    public boolean shouldExecute(EntityLiving entity) {
        
        
        return !(Math.abs(entity.x - entity.world.player.x) > AISmart.TRACKING_RANGE || Math.abs(entity.y - entity.world.player.y) > AISmart.TRACKING_RANGE);
    }

    @Override
    public void startExecuting(EntityLiving entity) {
        ai.pathfinder = new Pathfinder(entity.world.player, AISmart.TRACKING_RANGE, entity);
        
    }

    @Override
    public boolean shouldContinue(EntityLiving entity) {
        return shouldExecute(entity);
    }

    @Override
    public void execute(EntityLiving entity) {
        
    }
    
}

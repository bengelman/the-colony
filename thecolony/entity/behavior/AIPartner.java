/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import thecolony.entity.behavior.AI;
import thecolony.items.ItemWeapon;
import thecolony.entity.EntityLiving;
import thecolony.entity.Entity;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import thecolony.WorldPanel;
import thecolony.entity.EntityFighter;
import thecolony.pathfinding.AStarPathFinder;
import thecolony.pathfinding.Path;

/**
 *
 * @author pdogmuncher
 */
public class AIPartner extends AI{
    public ItemWeapon firearm, melee;
    double lastpx, lastpy;
    Path toFollow = null;
    int nextIndex = 1;
    public AIPartner() {
        
    }

    @Override
    public void onUpdate() {
        if (e.weapon.currentAmmo == 0){
                e.weapon = melee;
                firearm = null;
            }
        boolean changed = false;
        for (int i = 0; i < e.world.entities.size(); i++){
            
            if (!(e.world.entities.get(i) instanceof EntityLiving)){
                continue;
            }
            EntityFighter ent = (EntityFighter) e;
            EntityLiving en = (EntityLiving) e.world.entities.get(i);
            if (EntityFighter.groups.get(ent.targets).contains(en.getClass())){
                if (firearm != null){
                    
                    if (Math.abs(e.x - en.x) < 600 && Math.abs(e.y - en.y) < 400){
                        Line2D line = new Line2D.Float((float)e.x + e.w / 2, (float)e.y + e.h / 2, (float)en.x + en.w / 2, (float)en.y + en.h / 2);
                        boolean valid = true;
                        for (int j = 0; j < e.world.entities.size() && valid; j++){
                            if (e.world.entities.get(j).getBounds().intersectsLine(line) && e.world.entities.get(j) != e && e.world.entities.get(j) != en){
                                valid = false;
                            }
                        }
                        if (valid) {
                            e.weapon.fireBullet(new Point((int)en.x, (int)en.y), e);
                        }
                        if (e.xvel > 0){
                            e.xvel--;
                        }
                        if (e.xvel < 0){
                            e.xvel++;
                        }
                        if (e.yvel > 0){
                            e.yvel--;
                        }
                        if (e.yvel < 0){
                            e.yvel++;
                        }
                        
                        
                    }
                }
                /*else{
                    if (Math.abs(e.x - en.x) < 600 && Math.abs(e.y - en.y) < 400){
                        changed = true;
                        if (e.x > (en.x + (e.maxVelocity + 1) / 2)){
                            e.xvel = -e.maxVelocity;
                        }
                        else if (e.x < (en.x - (e.maxVelocity + 1) / 2)){
                            e.xvel = e.maxVelocity;
                        }
                        else{
                            e.xvel = 0;
                        }
                        if (e.y > (en.y + (e.maxVelocity + 1) / 2)){
                            e.yvel = -e.maxVelocity;
                        }
                        else if (e.y < (en.y - (e.maxVelocity + 1) / 2)){
                            e.yvel = e.maxVelocity;
                        }
                        else{
                            e.yvel = 0;
                        }
                    }
                    
                }*/
            }
        }
        if (Math.abs(e.world.player.x - e.x) > 500 || Math.abs(e.world.player.y - e.y) > 500){
            return;
        }
        //if (lastpx != e.world.player.x || lastpy != e.world.player.y){
        if ((toFollow == null && e.age % WorldPanel.TICKS_PER_SECOND == 0) || (toFollow != null && nextIndex >= toFollow.getLength())){
            toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y + 25) / 50, (int)e.world.player.x / 50, ((int)e.world.player.y + 25) / 50);
            lastpx = e.world.player.x;
            lastpy = e.world.player.y;
            nextIndex = 1;
        }
        
        if (toFollow != null){
            boolean canShift = false;
            e.xvel = toFollow.getX(nextIndex) * 50 - e.x;
            if (Math.abs(e.xvel) > 5){
                e.xvel = (e.xvel / Math.abs(e.xvel)) * 5;
            }
            else{
                canShift = true;
            }
            e.yvel = ((toFollow.getY(nextIndex) * 50) - 25) - e.y;
            if (Math.abs(e.yvel) > 5){
                e.yvel = (e.yvel / Math.abs(e.yvel)) * 5;
            }
            else{
                if (canShift)nextIndex++;
            }
            
        }
        /*
        else{
            if (e.x > (e.world.player.x + (e.maxVelocity + 1) / 2)){
                e.xvel = -e.maxVelocity;
            }
            else if (e.x < (e.world.player.x - (e.maxVelocity + 1) / 2)){
                e.xvel = e.maxVelocity;
            }
            else{
                e.xvel = 0;
            }
            if (e.y > (e.world.player.y + (e.maxVelocity + 1) / 2)){
                e.yvel = -e.maxVelocity;
            }
            else if (e.y < (e.world.player.y - (e.maxVelocity + 1) / 2)){
                e.yvel = e.maxVelocity;
            }
            else{
                e.yvel = 0;
            }
        }
        * */
    }

    @Override
    public void onCollide(Entity with) {
        if (EntityFighter.groups.get(((EntityFighter)e).targets).contains(with.getClass())){
            EntityLiving ent = (EntityLiving) with;
            ent.takeDamage(e.strength + e.weapon.melee, e);
            ent.xvel = (ent.x - e.x) / 2;
            ent.yvel = (ent.y - e.y) / 2;
        }
    }
    
}

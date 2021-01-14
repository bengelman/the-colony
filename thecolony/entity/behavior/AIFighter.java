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
import thecolony.entity.EntityVehicle;
import thecolony.pathfinding.AStarHeuristic;
import thecolony.pathfinding.AStarPathFinder;
import thecolony.pathfinding.Path;

/**
 *
 * @author pdogmuncher
 */
public class AIFighter extends AI{
    //public ItemWeapon firearm, melee;
    public ItemWeapon melee;
    double lastpx, lastpy;
    Path toFollow = null;
    int nextIndex = 1;
    int idle = 0;
    int nextMove = (int) (Math.random() * 10 + 5) * WorldPanel.TICKS_PER_SECOND;
    //public Path toFollow = null;
    //public EntityLiving targeted = null;
    //public final int MAX_SEARCH = 400;
    //public int stepIndex = 0;
    public AIFighter() {
        
    }

    
    @Override
    public void onUpdate() {
        
        if (!EntityFighter.groups.containsKey(((EntityFighter)e).targets)){
            System.out.println("Error with " + ((EntityFighter)e).targets);
            int i = 0/0;
            
        }
        idle++;
        if (toFollow != null){
            if (nextIndex >= toFollow.getLength()){
                e.xvel = 0;
                e.yvel = 0;
            }
        }
        if (e.weapon.currentAmmo == 0 && e.weapon != melee){
            e.weapon = melee;
            toFollow = null;
            //firearm = null;
        }
        if (e.weapon != melee){
            boolean located = false;
            
            
            for (int i = 0; i < e.world.entities.size(); i++){
            
                if (!(e.world.entities.get(i) instanceof EntityLiving)){
                    continue;
                }
                EntityFighter ent = (EntityFighter) e;
                EntityLiving en = (EntityLiving) e.world.entities.get(i);

                if (EntityFighter.groups.get(ent.targets).contains(en.getClass())){

                    if (e.weapon != melee){

                        if (Math.abs(e.x - en.x) < (e.weapon.range * e.weapon.bulletVelocity) && Math.abs(e.y - en.y) < (e.weapon.range * e.weapon.bulletVelocity)){

                            Line2D line = new Line2D.Float((float)e.x + e.w / 2, (float)e.y + e.h / 2, (float)en.x + en.w / 2, (float)en.y + en.h / 2);
                            boolean valid = true;
                            for (int j = 0; j < e.world.entities.size() && valid; j++){
                                if (e.world.entities.get(j).getBounds().intersectsLine(line) && e.world.entities.get(j) != e && e.world.entities.get(j) != en && !(e.world.entities.get(j) instanceof EntityVehicle)){

                                    valid = false;
                                }
                            }
                            if (valid) {
                                e.facingRight = e.x < en.x;
                                e.facingBack = e.y > en.y + 100;
                                e.weapon.fireBullet(new Point((int)en.x, (int)en.y), e);
                                located = true;
                                idle = 0;
                                toFollow = null;
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
                                break;
                            }

                        }
                    }
                }
            }
            if (!located){
                if (toFollow == null && e.age % WorldPanel.TICKS_PER_SECOND == 0 || (toFollow != null && nextIndex >= toFollow.getLength())){
                    EntityLiving target = null;
                    toFollow = null;
                    for (int i = 0; i < e.world.entities.size(); i++){

                        if (!(e.world.entities.get(i) instanceof EntityLiving)){
                            continue;
                        }
                        EntityLiving en = (EntityLiving) e.world.entities.get(i);

                        if (EntityFighter.groups.get(((EntityFighter)e).targets).contains(en.getClass())){
                            if (Math.abs(en.x - e.x) > 700 || Math.abs(en.y - e.y) > 700){
                                continue;
                            }
                            target = en;
                            break;
                        }
                    }
                    if (target != null){
                        e.facingRight = e.x < target.x;
                        e.facingBack = e.y > target.y + 100;
                        toFollow = new AStarPathFinder(e.world, 700, false, e).findPath((int)e.x / 50, ((int)e.y + 25) / 50, (int)target.x / 50, ((int)target.y + 25) / 50);
                        lastpx = target.x;
                        lastpy = target.y;
                        nextIndex = 1;
                        idle = 0;
                        
                    }
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
                    idle = 0;
                }
            }
        }
        else{
            
            //if (lastpx != e.world.player.x || lastpy != e.world.player.y){
            if ((toFollow == null && e.age % WorldPanel.TICKS_PER_SECOND == 0) || (toFollow != null && nextIndex >= toFollow.getLength())){
                
                EntityLiving target = null;
                toFollow = null;
                for (int i = 0; i < e.world.entities.size(); i++){
            
                    if (!(e.world.entities.get(i) instanceof EntityLiving)){
                        continue;
                    }
                    EntityLiving en = (EntityLiving) e.world.entities.get(i);

                    if (EntityFighter.groups.get(((EntityFighter)e).targets).contains(en.getClass())){
                        if (Math.abs(en.x - e.x) > 500 || Math.abs(en.y - e.y) > 500){
                            continue;
                        }
                        target = en;
                        break;
                    }
                }
                if (target != null){
                    toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y + 25) / 50, (int)target.x / 50, ((int)target.y + 25) / 50);
                    lastpx = target.x;
                    lastpy = target.y;
                    e.facingRight = e.x < target.x;
                    e.facingBack = e.y > target.y + 100;
                    nextIndex = 1;
                    idle = 0;
                }
                
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
                    idle = 0;
            }
        }
        if (idle > nextMove){
            nextMove = (int) (Math.random() * 10 + 5) * WorldPanel.TICKS_PER_SECOND;
            idle = 0;
            int dir = (int) (Math.random() * 4);
            switch(dir){
                case 0:
                    int newx = (int) (e.x + 200);
                    while (e.world.blocked(newx, (int)e.y, e) && newx > e.x){
                        newx--;
                    }
                    if (newx > e.x  && newx > 0 && newx < e.world.width){
                        toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y +25) / 50, (int)newx / 50, ((int)e.y + 25) / 50);
                        lastpx = newx;
                        lastpy = e.y;
                        nextIndex = 1;
                        break;
                    }
                case 1:
                    int newxf = (int) (e.x - 200);
                    while (e.world.blocked(newxf, (int)e.y, e) && newxf > e.x){
                        newxf++;
                    }
                    if (newxf < e.x  && newxf > 0 && newxf < e.world.width){
                        toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y +25) / 50, (int)newxf / 50, ((int)e.y + 25) / 50);
                        lastpx = newxf;
                        lastpy = e.y;
                        nextIndex = 1;
                        break;
                    }
                case 2:
                    int newy = (int) (e.y + 200);
                    while (e.world.blocked((int) e.x, newy, e) && newy > e.y){
                        newy--;
                    }
                    if (newy > e.y && newy > 0 && newy < e.world.height - 50){
                        toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y +25) / 50, (int)e.x / 50, ((int)newy + 25) / 50);
                        lastpx = e.x;
                        lastpy = newy;
                        nextIndex = 1;
                        break;
                    }
                case 3:
                    int newyf = (int) (e.y - 200);
                    while (e.world.blocked((int) e.x, newyf, e) && newyf > e.y){
                        newyf++;
                    }
                    if (newyf < e.y && newyf > 0 && newyf < e.world.height){
                        toFollow = new AStarPathFinder(e.world, 500, false, e).findPath((int)e.x / 50, ((int)e.y +25) / 50, (int)e.x / 50, ((int)newyf + 25) / 50);
                        lastpx = e.x;
                        lastpy = newyf;
                        nextIndex = 1;
                        break;
                    }
            }
        }
    }
    
    @Override
    public void onCollide(Entity with) {
        if (EntityFighter.groups.get(((EntityFighter)e).targets).contains(with.getClass()) && e.meleetimer == 0){
            EntityLiving ent = (EntityLiving) with;
            e.meleetimer = e.weapon.meleetimer;
            ent.takeDamage(e.strength + e.weapon.melee, e);
            ent.xvel = (ent.x - e.x) / 2;
            ent.yvel = (ent.y - e.y) / 2;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import thecolony.entity.behavior.tasks.TaskMoveToShoot;
import thecolony.entity.behavior.tasks.TaskMelee;
import thecolony.entity.behavior.tasks.Task;
import thecolony.entity.behavior.tasks.TaskTargetClosestEnemy;
import thecolony.entity.behavior.tasks.TaskShoot;
import thecolony.entity.behavior.tasks.TaskMeleeCharge;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import thecolony.entity.Entity;
import thecolony.entity.behavior.tasks.TaskFollowPlayer;
import thecolony.entity.behavior.tasks.TaskGunCharge;
import thecolony.entity.behavior.tasks.TaskMoveToEngage;
import thecolony.items.ItemWeapon;
import thecolony.pathfinding.AStarPathFinder;
import thecolony.pathfinding.Path;

/**
 *
 * @author pdogmuncher
 */
public abstract class AISmart extends AI{
    public static final int TRACKING_RANGE = 700;
    public ArrayList <Task> genericTasks = new ArrayList <>();
    public int genericTask = -1;
    public ArrayList <Task> movementTasks = new ArrayList <>();
    public int movementTask = -1;
    public ArrayList <Task> targetingTasks = new ArrayList <>();
    public int targetingTask = -1;
    public ArrayList <Task> shootingTasks = new ArrayList <>();
    public int shootingTask = -1;
    public ArrayList <Task> collideTasks = new ArrayList<>();
    public Entity allyTarget = null;
    public Entity enemyTarget = null;
    public Entity colliding = null;
    public Pathfinder pathfinder = null;
    public ItemWeapon melee = null;
    public enum CombatStyle{
        CLASSIC, SENTRY, BLITZER, CHASER, ASSAULT, PARTNER
    };
    public CombatStyle style;
    public AISmart(){
        addTasks();
    }
    public abstract void addTasks();
    public static AISmart generateAI(final CombatStyle combatStyle){
        
        return new AISmart(){
            @Override
            public void addTasks() {
                
                this.style = combatStyle;
                
                switch(combatStyle){
                    case CLASSIC:
                        movementTasks.add(new TaskMoveToShoot(this));
                        movementTasks.add(new TaskMeleeCharge(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                    case SENTRY:
                        movementTasks.add(new TaskMeleeCharge(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                    case BLITZER:
                        movementTasks.add(new TaskGunCharge(this));
                        movementTasks.add(new TaskMeleeCharge(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                    case CHASER:
                        movementTasks.add(new TaskGunCharge(this));
                        movementTasks.add(new TaskMoveToEngage(this));
                        movementTasks.add(new TaskMeleeCharge(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                    case ASSAULT:
                        movementTasks.add(new TaskMoveToShoot(this));
                        movementTasks.add(new TaskMoveToEngage(this));
                        movementTasks.add(new TaskMeleeCharge(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                    case PARTNER:
                        movementTasks.add(new TaskMeleeCharge(this));
                        movementTasks.add(new TaskFollowPlayer(this));
                        targetingTasks.add(new TaskTargetClosestEnemy(this));
                        shootingTasks.add(new TaskShoot(this));
                        collideTasks.add(new TaskMelee(this));
                        break;
                }
                //movementTasks.add(new TaskMoveToShoot(this));
                //movementTasks.add(new TaskMeleeCharge(this));
                //targetingTasks.add(new TaskTargetClosestEnemy(this));
                //shootingTasks.add(new TaskShoot(this));
                //collideTasks.add(new TaskMelee(this));
            }
        };
    }
    @Override
    public void onUpdate() {
        if (e.weapon.currentAmmo == 0 && !e.weapon.isMelee){
            e.weapon = melee;
            //firearm = null;
        }
        if (genericTask != -1){
            if (genericTasks.get(genericTask).shouldContinue(e)) {
                genericTasks.get(genericTask).execute(e);
            }
            else{
                genericTask = -1;
            }
        }
        for (int i = 0; i < genericTasks.size() && (genericTask == -1 || i < genericTask); i++){
            if (genericTasks.get(i).shouldExecute(e)){
                genericTask = i;
                genericTasks.get(i).startExecuting(e);
                genericTasks.get(i).execute(e);
                break;
            }
        }
        
        if (movementTask != -1){
            if (movementTasks.get(movementTask).shouldContinue(e)) {
                movementTasks.get(movementTask).execute(e);
            }
            else{
                movementTask = -1;
            }
        }
        for (int i = 0; i < movementTasks.size() && (movementTask == -1 || i < movementTask); i++){
            if (movementTasks.get(i).shouldExecute(e)){
                movementTask = i;
                movementTasks.get(i).startExecuting(e);
                movementTasks.get(i).execute(e);
                break;
            }
        }
        
        if (targetingTask != -1){
            if (targetingTasks.get(targetingTask).shouldContinue(e)) {
                targetingTasks.get(targetingTask).execute(e);
            }
            else{
                targetingTask = -1;
            }
        }
        for (int i = 0; i < targetingTasks.size() && (targetingTask == -1 || i < targetingTask); i++){
            if (targetingTasks.get(i).shouldExecute(e)){
                targetingTask = i;
                targetingTasks.get(i).startExecuting(e);
                targetingTasks.get(i).execute(e);
                break;
            }
        }
        
        if (shootingTask != -1){
            if (shootingTasks.get(shootingTask).shouldContinue(e)) {
                shootingTasks.get(shootingTask).execute(e);
            }
            else{
                shootingTask = -1;
            }
        }
        for (int i = 0; i < shootingTasks.size() && (shootingTask == -1 || i < shootingTask); i++){
            if (shootingTasks.get(i).shouldExecute(e)){
                shootingTask = i;
                shootingTasks.get(i).startExecuting(e);
                shootingTasks.get(i).execute(e);
                break;
            }
        }
        if (pathfinder != null) {
            pathfinder.update();
            if (pathfinder.path == null || e.collidedLastTick){
                pathfinder = null;
                movementTask = -1;
            }
            
        }
        if (pathfinder != null && (pathfinder.index >= pathfinder.path.getLength())){
            pathfinder = null;
            movementTask = -1;
            e.xvel = 0;
            e.yvel = 0;
        }
    }
    @Override
    public void onCollide(Entity with) {
        for (Task task : collideTasks){
            colliding = with;
            if (task.shouldExecute(e)){
                task.execute(e);
            }
        }
    }
    public static class Pathfinder implements Serializable{
        Point destination = null;
        Entity target = null;
        int index = 0;
        int nextRecalc = 0;
        Path path = null;
        Entity e;
        int range;
        public Pathfinder(Point destination, int range, Entity e){
            this.destination = destination;
            this.e = e;
            this.range = range;
            path = new AStarPathFinder(e.world, range, false, e).findPath((int)e.x / 50, ((int)e.y + 25) / 50, (int)destination.x / 50, ((int)destination.y + 25) / 50);
        }
        public Pathfinder(Entity target, int range, Entity e){
            this(new Point((int)target.x, (int)target.y), range, e);
            this.target = target;
            
        }
        public void update(){
            
            if (nextRecalc > 0){
                nextRecalc--;
            }
            if ((nextRecalc == 0)&& Math.abs(target.x - e.x) + Math.abs(target.y - e.y) < range && target != null){
                if (destination.x / 50 == (int)target.x / 50 && destination.y / 50 == (int)target.y / 50){
                    destination = new Point((int)target.x, (int)target.y);
                    nextRecalc = 40;
                    index = 0;
                    path = new AStarPathFinder(e.world, range, false, e).findPath((int)e.x / 50, ((int)e.y + 25) / 50, (int)destination.x / 50, ((int)destination.y + 25) / 50);
                }
            }
            if (path != null){
                boolean canShift = false;
                e.xvel = path.getX(index) * 50 - e.x;
                if (Math.abs(e.xvel) > 5){
                    e.xvel = (e.xvel / Math.abs(e.xvel)) * 5;
                }
                else{
                    canShift = true;
                }
                e.yvel = ((path.getY(index) * 50) - 25) - e.y;
                if (Math.abs(e.yvel) > 5){
                    e.yvel = (e.yvel / Math.abs(e.yvel)) * 5;
                }
                else{
                    if (canShift) {
                        index++;
                    }
                }
            }
        }
    }
}


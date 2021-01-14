/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import thecolony.ImageRegistry;
import thecolony.entity.behavior.AI;
import thecolony.items.Item;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.World;

/**
 *
 * @author pdogmuncher
 */
public class EntityLiving extends Entity{
    public int health;
    public int maxHealth;
    public int maxVelocity;
    public AI ai;
    public ItemArmor armor;
    public ItemHelmet helmet;
    public ItemWeapon weapon;
    public int strength;
    public int credits = 0;
    public ArrayList<Item> inventory = new ArrayList<>();
    public EntityVehicle riding = null;
    public int baseSpeed;
    public Point defaultSize;
    public int meleetimer = 0;
    public boolean freeze = false;
    public EntityLiving(World world, double x, double y, int w, int h, int height, String[][] texture, int health, int maxVelocity, AI ai, int strength){
        super(world, x, y, w, h, height, texture);
        this.health = health;
        this.maxHealth = health;
        this.maxVelocity = maxVelocity;
        this.ai = ai;
        this.strength = strength;
        this.ai.e = this;
        baseSpeed = maxVelocity;
        defaultSize = new Point(w, h);
    }
    @Override
    public void update(){
        super.update();
        if (yvel < 0){
            //facingBack = true;
        }
        else if (yvel > 0){
            //facingBack = false;
        }
        if (moveScript != null || noai){
            
            return;
        }
        if (meleetimer > 0){
            meleetimer--;
        }
        if (this.health <= 0){
            if (texture.length > 3){
                world.entities.add(new EntityLoot(world, x, y + (h - 1), w, 0, height, texture[3], this.inventory, credits, armor, helmet));
            }
            else{
                world.entities.add(new EntityLoot(world, x, y + (h - 1), w, 0, height, texture[0], this.inventory, credits, armor, helmet));
            }
            world.entities.remove(this);
            this.dismount();
            world.clear();
            this.onDeath();
        }
        if (this.xvel > maxVelocity){
            this.xvel--;
        }
        else if (this.xvel < -maxVelocity){
            this.xvel++;
        }
        if (this.yvel > maxVelocity){
            this.yvel--;
        }
        else if (this.yvel < -maxVelocity){
            this.yvel++;
        }
        if (!freeze) {
            ai.onUpdate();
        }
        if (riding != null) {
            riding.ai.onUpdate();
        }
        if (weapon != null) {
            weapon.update(this);
        }
        if (helmet != null) {
            helmet.equippedUpdate(this);
        }
            
    }
    @Override
    public void collide(Entity e){
        super.collide(e);
        ai.onCollide(e);
    }
    public boolean facingBack = false;
    @Override
    public int getTextureIndex(){
        if (this instanceof EntityFighter || this instanceof EntityPlayer){
            return facingBack ? 1 : 0;
        }
        return super.getTextureIndex();
    }
    public void drawLinear(Graphics g, int playerx, int playery, int screenw, int screenh){
        //if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh))){
            g.drawImage(getTexture(), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
        //}
        //if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && (this instanceof EntityFighter || this instanceof EntityPlayer)){
            int textureIndex;
            if (this.xvel != 0 || this.yvel != 0){
                textureIndex = (age / 8) % 2 + 1;
            }
            else{
                textureIndex = 0;
            }
            if (facingRight){
                g.drawImage(ImageRegistry.getFlippedImage("legs" + textureIndex + ".png"), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            }
            else{
                g.drawImage(ImageRegistry.getImage("legs" + textureIndex + ".png", false), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            }
            
            
        //}
        //if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && armor != null){
        if (armor != null) {
            g.drawImage(armor.getImage(this, false), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            }
        //}
        //if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && helmet != null){
        if (helmet != null) {
            g.drawImage(helmet.getImage(this, false), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            }
            
        //}
        //if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && weapon != null){
        if (weapon != null) {
            g.drawImage(weapon.getEquippedImage(this), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            //}
            }
    }
    @Override
    public void drawEntity(Graphics g, int playerx, int playery, int screenw, int screenh){
        
        if (riding != null){
            if (facingRight){
                x = riding.x + riding.w - riding.offset.x - defaultSize.x;
            }
            else{
                x += riding.offset.x;
            }
            y += riding.offset.y;
            w = this.defaultSize.x;
            h = defaultSize.y;
            //System.out.println("This x: " + x + " This y: " + y + " Riding x: " + riding.x + " Riding y: " + riding.y);
        }
        
        if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh))){
            g.drawImage(getTexture(), getPosOnScreen(playerx, playery).x,getPosOnScreen(playerx, playery).y, w, height, null);
        }
        if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && (this instanceof EntityFighter || this instanceof EntityPlayer)){
            int textureIndex;
            if (this.xvel != 0 || this.yvel != 0){
                textureIndex = (age / 8) % 2 + 1;
            }
            else{
                textureIndex = 0;
            }
            if (facingRight){
                g.drawImage(ImageRegistry.getFlippedImage("legs" + textureIndex + ".png"), getPosOnScreen(playerx, playery).x, getPosOnScreen(playerx, playery).y, w, height, null);
            }
            else{
                g.drawImage(ImageRegistry.getImage("legs" + textureIndex + ".png", false), getPosOnScreen(playerx, playery).x, getPosOnScreen(playerx, playery).y, w, height, null);
            }
            
            
        }
        if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && armor != null){
            g.drawImage(armor.getImage(this, false), getPosOnScreen(playerx, playery).x, getPosOnScreen(playerx, playery).y, w, height, null);
        }
        if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && helmet != null){
            g.drawImage(helmet.getImage(this, false), getPosOnScreen(playerx, playery).x, getPosOnScreen(playerx, playery).y, w, height, null);
            
        }
        if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && weapon != null){
             g.drawImage(weapon.getEquippedImage(this), getPosOnScreen(playerx, playery).x, getPosOnScreen(playerx, playery).y, w, height, null);
        }
        if (riding != null){
            
            riding.drawEntity(g, playerx, playery, screenw, screenh);
            x = riding.x;
            y = riding.y;
            w = riding.w;
            h = riding.h;
        }
    }
    public void takeDamage(int amount, EntityLiving attacker){
        if (riding != null){
            return;
        }
        if (armor != null) {
            amount -= armor.protection;
        }
        if (helmet != null) {
            amount -= helmet.protection;
        }
        if (amount <= 0){
            amount = 1;
        }
        health -= amount;
    }
    public boolean chargeCredit(int amount){
        if (credits >= amount){
            credits -= amount;
            return true;
        }
        return false;
    }
    public void dismount(){
        if (riding != null){
            if (riding.prevWeapon != null) {
                for (ItemWeapon weapony : riding.newWeapons){
                    inventory.remove(weapony);
                }
                
                weapon = riding.prevWeapon;
            }
            riding.xvel = 0;
            riding.yvel = 0;
            riding.prevWeapon = null;
            riding.rider = null;
            riding = null;
            
            this.w = defaultSize.x;
            this.h = defaultSize.y;
            this.maxVelocity = this.baseSpeed;
        }
    }
    public int getQuantityOf(String item){
        int num = 0;
        for (Item i : inventory){
            if (i.name.equalsIgnoreCase(item)){
                num++;
            }
        }
        return num;
    }
    public void onDeath(){}
}

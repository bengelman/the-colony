/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

import java.awt.Color;
import java.awt.Image;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public class ItemWeapon extends Item{
    public int damage = 0;
    public int delay = 0;
    public int currentDelay = 0;
    public double recoil = 0;
    public double currentRecoil = 0;
    public int meleetimer;
    public int inaccuracy = 0;
    public int range = 0;
    public String bulletTexture;
    public double bulletVelocity = 0;
    public static final ItemBullet AMMOCELL = new ItemBullet("Plasma Cell", "plasmacell.png");
    public static final ItemBullet ROCKET = new ItemBullet("Launchable Grenade", "rocket.png");
    public static final ItemBullet NINEMM = new ItemBullet("9x19mm Round", "pistolammo.png");
    public static final ItemBullet FIFTYAA = new ItemBullet(".50 Action Express Round", "pistolammo.png");
    public static final ItemBullet THIRTY = new ItemBullet("7.62x51mm Round", "ammo.png");
    public static final ItemBullet FIVECM = new ItemBullet("5.56x45mm Round", "ammo.png");
    public static final ItemBullet TWELVEGAUGE = new ItemBullet("12 Gauge Shotgun Shell", "shell.png");
    public static final ItemBullet BATTERY = new ItemBullet("Laser Battery", "battery.png");
    public static final ItemBullet TNT = new ItemBullet("Dynamite", "tnt.png");
    public static final ItemBullet CANNONBALL = new ItemBullet("Cannonball", "cannonball.png");
    public Item ammo;
    public int capacity = 0;
    public int currentAmmo = 0;
    public int melee;
    public int burst;
    public FireType fireType;
    public String description;
    public boolean ricochet = false;
    public int bulletsize = 3;
    public Point lastPoint = new Point();
    public EntityLiving lastFirer = null;
    public boolean explodey = false;
    public boolean clusterBomb = false;
    public boolean autofill = false;
    String equip;

    public Image getEquippedImage(EntityLiving aThis) {
        if (aThis.facingBack && !aThis.facingRight){
            return ImageRegistry.getImage("null.png", false);
        }
        String equipImage = equip + "equip.png";
        if (aThis.facingRight && aThis.facingBack){
            return ImageRegistry.getFlippedImage(equipImage);
        }
        if (aThis.facingRight){
            return ImageRegistry.getImage(equip + "equipr.png", false);
        }
        return ImageRegistry.getImage(equipImage, false);
    }
    public enum FireType{
        BOLT, SEMI, AUTO, THREE
    };
    public enum Scope{
        NONE, MAG, DIGITAL, LONGRANGE
    };
    public boolean reloading = false;
    public Scope scope;
    public boolean isMelee;
    public int spread;
    Color color;
    /* *
     * @param name The name of the weapon
     * @param texture The weapon's image
     */
    
    public ItemWeapon(String name, String texture, Color color, String description, int damage, int delay, double recoil, int inaccuracy, 
            int range, String bulletTexture, String equip, double bulletVelocity, Item ammo, int amount, int burst, int spread, 
            FireType fireType, Scope scope, boolean autofill){
        super(name, "weapons/" + texture);
        isMelee = false;
        this.damage = damage;
        this.color = color;
        this.delay = delay;
        this.recoil = recoil;
        this.range = range;
        this.bulletTexture = bulletTexture;
        this.bulletVelocity = bulletVelocity;
        this.ammo = ammo;
        this.melee = 5;
        this.capacity = amount;
        this.currentAmmo = amount;
        this.spread = spread;
        this.burst = burst;
        this.inaccuracy = inaccuracy;
        currentRecoil = inaccuracy;
        this.fireType = fireType;
        this.scope = scope;
        this.description = description;
        this.meleetimer = 50;
        if (bulletTexture.equals("dynamite")){
            bulletsize = 16;
        }
        if (bulletTexture.equals("rocket")){
            bulletsize = 16;
        }
        this.autofill = autofill;
        this.equip = equip;
    }
    public ItemWeapon(String name, String texture, String description, int melee, int meleetimer){
        super(name, "weapons/" + texture);
        this.melee = melee;
        this.description = description;
        this.meleetimer = meleetimer;
        isMelee = true;
        equip = "knife";
    }
    public void update(EntityLiving user){
        
        if (currentDelay > 0){
            if (currentDelay == 8 && !reloading && user == user.world.player && fireType == FireType.BOLT){
                new AudioPlayer("pump.wav").play(0);
            }
            currentDelay--;
            
        }
        else{
            reloading = false;
        }
        if (currentRecoil > inaccuracy && currentDelay <= 0){
            currentRecoil-= 1.5;
            if (currentRecoil < inaccuracy){
                currentRecoil = inaccuracy;
            }
        }
         if (currentRecoil > 30){
             currentRecoil -= 2.5;
         }   
    }
    public void reload(EntityLiving entity){
        if ((entity.inventory.contains(ammo) || autofill) && currentAmmo < capacity){
            reloading = true;
            if (entity == entity.world.player) {
                new AudioPlayer("reload.wav").play(0);
            }
            currentDelay = 40;
            if (autofill){
                currentAmmo = capacity;
            }
            else{
                entity.inventory.remove(ammo);
                currentAmmo++;
                while (currentAmmo < capacity && entity.inventory.contains(ammo)){
                    entity.inventory.remove(ammo);
                    currentAmmo++;
                }
            }
            
                
        }
    }
        
    public void fireBullet(Point target, EntityLiving firer){
        if (damage == 0){
            return;
        }
        if (currentDelay > 0){
            return;
        }
        if (currentAmmo <= 0){
            reload(firer);
            return;
        }
        lastFirer = firer;
        lastPoint = target;
       reloading = false;
        currentAmmo--;
        
        
        double x = target.x - (firer.x + firer.w / 2.0D);
        double y = target.y - firer.y;
        double angle = Math.atan((double)y / (double)x);
        double increase = Math.toRadians((Math.random() * currentRecoil * 2D) - currentRecoil);
        boolean shouldIncrease = true;
        if (firer instanceof EntityPlayer){
            if (((EntityPlayer)firer).cigaretteTimer > 0){
                shouldIncrease = false;
            }
        }
        if (shouldIncrease) {
            angle += increase;
        }
        else {
            angle += (increase / 4.0f);
        }
        if (x < 0){
            angle += Math.PI;
        }
        
        currentRecoil += recoil;
        for (int i = 0; i < burst; i++){
            double indAngle = angle + Math.toRadians(((spread / burst) * i) - (spread / 2));
            EntityBullet bullet = new EntityBullet(firer.world, firer.x + firer.w / 2, firer.y, bulletsize, damage, range, bulletVelocity, ricochet, firer, color != null ? null : bulletTexture, color, explodey, clusterBomb);
            bullet.xvel = Math.cos(indAngle) * bulletVelocity;
            bullet.yvel = Math.sin(indAngle) * bulletVelocity;
            //bullet.x += Math.cos(indAngle) * (firer.w / 2);
            //bullet.y += Math.sin(indAngle) * (firer.h / 2);
            firer.world.entities.add(bullet);
            //bullet.x += 
            //bullet.update();
            /*
            if (bulletVelocity < 25) {
                bullet.update();
            }*/
            
        }
        try {
            new AudioPlayer(bulletTexture + ".wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(EntityBullet.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentDelay = delay;
        if (currentAmmo <= 0){
            reload(firer);
        }
        
    } 
    @Override
    public String[] getMouseover(){
        if (damage > 0) {
            return new String[]{name, "Damage: " + damage, "Rounds per Second: " + (WorldPanel.TICKS_PER_SECOND / delay) * burst, "Recoil: " + recoil, "Accuracy: " + inaccuracy, description};
        }
        else {
            return new String[]{name, "Melee Damage: " + melee, "Speed: " + this.getSpeed(), description};
        }
    }
    @Override
    public boolean use(EntityPlayer p){
        super.use(p);
        p.weapon = this;
        return false;
    }
    public String getSpeed(){
        switch (meleetimer){
            case 25:
                return "fast";
            case 100:
                return "slow";
            default:
                return "medium";
        }
    }
    public class Upgrade{
        
    }
}

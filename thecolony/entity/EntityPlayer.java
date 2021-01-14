/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.items.drugs.ItemAdrenaline;
import thecolony.items.ItemArmor;
import thecolony.items.drugs.ItemCigarette;
import thecolony.items.drugs.ItemExenol;
import thecolony.items.ItemHelmet;
import thecolony.items.drugs.ItemNicotineGum;
import thecolony.items.drugs.ItemNoctaine;
import thecolony.items.drugs.ItemTransneuract;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.Main;
import thecolony.WorldPanel;
import thecolony.entity.behavior.AIFighter;
import thecolony.entity.behavior.AIPlayer;
import thecolony.items.Item;
import thecolony.items.chips.ItemChip;
import thecolony.items.chips.ItemDetectiveChip;
import thecolony.items.chips.ItemQuickdrawChip;
import thecolony.items.chips.ItemScopeChip;

/**
 *
 * @author pdogmuncher
 */
public class EntityPlayer extends EntityLiving{
    public boolean zoomed = false;
    public int zoomLevel = 1;
    public boolean frozen = false;
    public int exenolLevel = 0;
    public int timeWithoutExenol = 0;
    public int adrenalineTimer = 0;
    public int timeWithoutCigarette = 0;
    public int totalTimeWithoutCigarette = 0;
    public boolean nicoteneAddict = false;
    public int cigaretteTimer = 0;
    public int nodrugsHp = 100;
    public AudioPlayer heartbeat;
    public String lastWorld = "house";
    public String enemyGroup = "resistanceEnemies";
    public double lastx = 0, lasty = 0;
    public int noctaineLevel = 0;
    public int noctaineTimer = 0;
    public int timeWithoutNoctaine = 0;
    public boolean takingGif = false;
    private int reputation = 0;
    public ArrayList<Item> stolenInventory = new ArrayList<>();
    public ItemChip chip = null;
    
    public EntityPlayer(int x, int y, int height){
        super(null, x, y, 36, 25, 48, new String[][]{{"null.png", "hero0.png"}/*,{"heroine.png", "heroine0.png", "heroine1.png"}*/}, 110, 200 / WorldPanel.TICKS_PER_SECOND, new AIFighter(), 15);
    }
    public EntityPlayer(int x, int y, int w, int h, int height){
        super(null, x, y, w, h, height, new String[][]{{"hero.png", "hero0.png"}/*,{"heroine.png", "heroine0.png", "heroine1.png"}*/}, 100, 200 / WorldPanel.TICKS_PER_SECOND, new AIPlayer(), 15);
        ItemArmor armors = new ItemArmor("Trenchcoat", "Zayve Brock's reinforced detective trenchcoat", "trenchcoat.png", "trenchflip.png", 5);
        ItemHelmet helmets = new ItemHelmet("CyberMask", "Can expand over the wearer's face at will", "mask.png", "maskflip.png", 4);
        ItemChip chipy = new ItemDetectiveChip("Detective Chip", "chip.png");
        this.armor = armors;
        this.helmet = helmets;
        //this.chip = chipy;
        this.weapon = new ItemWeapon("Customized Plasma Cell Revolver", "revolver.png", EntityBullet.PLASMA, "Zayve Brock's signature revolver, modified for better damage and capacity", 20, 10, 8, 2, 500, "plasma", "pistol", 25, ItemWeapon.AMMOCELL, 6, 1, 0, FireType.BOLT, Scope.NONE, false);
        
        
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(chipy);
        inventory.add(weapon);
        
        inventory.add(new ItemScopeChip("Targeting Chip", "chip.png"));
        inventory.add(new ItemQuickdrawChip("Quick Draw Chip", "chip.png"));
        
        ItemWeapon laser = new ItemWeapon("Laser Rifle", "lasergun.png", EntityBullet.LASER, "Shoots a deadly laser beam and has a powerful targeting scope", 100, 5, 0, 5, 1200, "laser", "pistol", 50, ItemWeapon.BATTERY, 100, 1, 0, FireType.AUTO, Scope.DIGITAL, false);
        //laser.explodey = true;
        //ItemWeapon gun = new ItemWeapon("Five-Barelled Shotgun", "fiveshotgun.png", "Cinquen Cinquetel's signature five barelled shotgun", 10, 15, 2, 3, 10, "bullet", 20, ItemWeapon.TWELVEGAUGE, 5, 6, false, Scope.NONE);
        inventory.add(laser);
        //inventory.add(gun);
        //inventory.add(new ItemWeapon("Plasma Cell AR", "bullpup.png", "A full-auto bullpup plasma assault rifle", 20, 5, 3, 1, 25, "plasma", 25, ItemWeapon.AMMOCELL, 20, 1, true, Scope.MAG));
        //inventory.add(new ItemWeapon("Plasma Cell LMG", "plasmacannon.png", "Lays down suppressive fire with a huge fire rate", 10, 2, 0, 20, 10, "plasma", 1500 / WorldPanel.TICKS_PER_SECOND, ItemWeapon.AMMOCELL, 100, 1, FireType.AUTO, Scope.NONE, false));
        //inventory.add(new ItemWeapon("Minigun", "minigun.png", "Lays down suppressive fire with a huge fire rate", 10, 1, 1, 15, 25, "bullet", 30, ItemWeapon.THIRTY, 100, 3, 1, true, Scope.NONE));
        //inventory.add(new ItemWeapon("Plasma Cell Revolving Rifle", "plasmasubgun.png", "Slow, inaccurate, and deadly", 70, 50, 0, 10, 20, "plasma", 20, ItemWeapon.AMMOCELL, 10, 1, false, Scope.NONE));
        
        //inventory.add(new ItemWeapon("Rocket machinegun", "missileturret.png", "Fires rockets rapidly, dealing massive damage", 7, 5, 8, 2, 10, "rocket", 1000 / WorldPanel.TICKS_PER_SECOND, ItemWeapon.ROCKET, 20, 1, FireType.AUTO, Scope.DIGITAL, false));
        //((ItemWeapon)inventory.get(inventory.size() - 1)).explodey = true;
        //inventory.add(new ItemWeapon("Dynamite launcher", "dynamitelauncher.png", "An improvised weapon that fires sticks of TNT", 12, 20, 8, 2, 12, "dynamite", 750 / WorldPanel.TICKS_PER_SECOND, ItemWeapon.TNT, 5, 1, FireType.BOLT, Scope.NONE, false));
        //((ItemWeapon)inventory.get(inventory.size() - 1)).explodey = true;
        inventory.add(new ItemWeapon("L200 SAW", "saw.png", EntityBullet.BULLET, "The SAW is a fully automatic firearm that can fire many rounds quickly", 15, 3, 2, 3, 1000, "bullet", "rifle", 30, ItemWeapon.FIVECM, 20, 1, 0, FireType.AUTO, Scope.MAG, false));
        
        inventory.add(new ItemWeapon("Grenade slingshot", "slingshot.png", null, "An improvised weapon that launches grenades", 7, 20, 8, 3, 400, "rocket", "pistol", 15, ItemWeapon.ROCKET, 1, 1, 0, FireType.BOLT, Scope.NONE, false));
        ((ItemWeapon)inventory.get(inventory.size() - 1)).explodey = true;
        
        
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        
        
        
        //inventory.add(new ItemCigarette());
        //inventory.add(new ItemCigarette());
        //inventory.add(new ItemCigarette());
        this.credits = 50;
    }
    public int getReputation(){
        return reputation;
    }
    public void addReputation(int rep){
        reputation += rep;
        if (WorldPanel.panel.currentTextQueue.isEmpty()){
            WorldPanel.panel.addMessage(new String[]{"Reputation " + (rep < 0 ? rep : "+" + rep)});
        }
        else{
            WorldPanel.panel.currentTextQueue.add("Reputation " + (rep < 0 ? rep : "+" + rep));
        }
    }
    @Override
    public void drawEntity(Graphics g, int playerx, int playery, int screenw, int screenh){
        super.drawEntity(g, playerx, playery, screenw, screenh);
        if (cigaretteTimer > 0){
            if (this.xvel > 0){
                g.drawImage(ImageRegistry.getFlippedImage("smoking.png"), this.getPosOnScreen(playerx, playery).x,this.getPosOnScreen(playerx, playery).y, w, height, null);
            }
            else {
                g.drawImage(ImageRegistry.getImage("smoking.png", false), this.getPosOnScreen(playerx, playery).x,this.getPosOnScreen(playerx, playery).y, w, height, null);
            }
        }
        
    }
    
    @Override
    public void update(){
        
        if (frozen){
            age++;
            return;
        }
        super.update();
        
        if (world.getXInWorld(WorldPanel.pointerLocation.x) > x){
            facingRight = true;
        }
        else{
            facingRight = false;
        }
        if (world.getYInWorld(WorldPanel.pointerLocation.y) > y - 100){
            facingBack = false;
        }
        else{
            facingBack = true;
        }
        
        
        if (chip != null){
            chip.onChipUpdate(this);
        }
        //System.out.println("Updating, time is " + WorldPanel.panel.totalTime);
        if (nicoteneAddict && cigaretteTimer == 0){
            timeWithoutCigarette++;
            totalTimeWithoutCigarette++;
            if (timeWithoutCigarette == 120 * WorldPanel.TICKS_PER_SECOND){
                WorldPanel.panel.showDialogue(new String[]{"Experiencing Nicotine Withdrawl Effects"}, true);
            }
            if (timeWithoutCigarette > 120 * WorldPanel.TICKS_PER_SECOND){
                
                if (timeWithoutCigarette % WorldPanel.TICKS_PER_SECOND == 0){
                    health--;
                }
            }
            if (totalTimeWithoutCigarette > 180 * WorldPanel.TICKS_PER_SECOND){
                nicoteneAddict = false;
                maxHealth += 10;
                WorldPanel.panel.showDialogue(new String[]{"Nicotene Addiction Cured"}, true);
            }
        }
        if (noctaineLevel > 0){
            timeWithoutNoctaine++;
            if (timeWithoutNoctaine == (10 - noctaineLevel) * (WorldPanel.TICKS_PER_SECOND * 12)){
                WorldPanel.panel.showDialogue(new String[]{"Experiencing Noctaine Withdrawl Effects"}, true);
            }
            if (timeWithoutNoctaine > (10 - noctaineLevel) * (WorldPanel.TICKS_PER_SECOND * 12)){
                noctaineTimer = 1;
            }
            if (timeWithoutNoctaine > WorldPanel.TICKS_PER_SECOND * 120){
                WorldPanel.panel.showDialogue(new String[]{"Noctaine addiction cured"}, true);
                noctaineLevel = 0;
            }
        }
        if (noctaineTimer > 0){
            noctaineTimer--;
        }
        if (exenolLevel > 0 && WorldPanel.panel.exenolTimer > 0){
            timeWithoutExenol++;
            if (timeWithoutExenol == (10 - exenolLevel) * (WorldPanel.TICKS_PER_SECOND * 12)){
                WorldPanel.panel.showDialogue(new String[]{"Experiencing Exenol Withdrawl Effects"}, true);
            }
            if (timeWithoutExenol >= (10 - exenolLevel) * (WorldPanel.TICKS_PER_SECOND * 12)){
                if (timeWithoutExenol % (WorldPanel.TICKS_PER_SECOND / 10) == 0) {
                    this.health--;
                }
            }
        }
        
        if (this.health < maxHealth / 4 && age % WorldPanel.TICKS_PER_SECOND == 0){
            this.health++;
        }
        if (this.health <= 0){
            //WorldPanel.panel.showMenu();
            WorldPanel.panel.audio.stop();
            Runnable[] runnables = new Runnable[]{
                new Runnable(){
                    @Override
                    public void run() {
                        Main.currentFrame.reInit();
                    }
                    
                },
                new Runnable(){
                    @Override
                    public void run() {
                        System.exit(1);
                    }
                    
                }
            };
            WorldPanel.panel.showOptionPane(new String[]{"Menu", "Quit"}, "You have died", runnables);
            
            
        }
        if (adrenalineTimer > 0){
            adrenalineTimer--;
            if (adrenalineTimer == 0){
                this.maxVelocity = baseSpeed;
                heartbeat.stop();
            }
        }
        if (cigaretteTimer > 0){
            cigaretteTimer--;
        }
        /*
        if (reputation < -20){
            EntityFighter.groups.get("enemies").add(EntityPlayer.class);
        }
        else if (EntityFighter.groups.get("enemies").add(EntityPlayer.class)){
            EntityFighter.groups.get("enemies").remove(EntityPlayer.class);
        }*/
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import thecolony.items.drugs.ItemExenol;
import java.awt.Graphics;
import java.awt.Rectangle;
import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import java.util.ArrayList;
import thecolony.ImageRegistry;
import thecolony.items.*;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.items.ItemArmor;

/**
 *
 * @author pdogmuncher
 */
public class EntityLoot extends Entity{
    public ArrayList<Item> loot;
    public int credits = 0;
    ItemArmor armor;
    ItemHelmet helmet;
    public enum Loot{
        BUNKER, TRIAD
    };
    public EntityLoot(World world, double x, double y, int w, int h, int height, String texture, ArrayList<Item> loot, int credit, ItemArmor armor, ItemHelmet helmet){
        super(world, x, y, w, h, height, new String[]{texture, texture, texture});
        this.loot = loot;
        this.credits = credit;
        this.armor = armor;
        this.helmet = helmet;
    }
    @Override
    public void drawEntity(Graphics g, int playerx, int playery, int screenw, int screenh){
        super.drawEntity(g, playerx, playery, screenw, screenh);
        if (this.getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && armor != null){
            g.drawImage(armor.getImage(this, false), this.getPosOnScreen(playerx, playery).x, this.getPosOnScreen(playerx, playery).y, w, height, null);
        }
        if (this.getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && helmet != null){
            g.drawImage(helmet.getImage(this, false), this.getPosOnScreen(playerx, playery).x, this.getPosOnScreen(playerx, playery).y, w, height, null);
            
        }
        if (this.getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh)) && this.w < 50){
            g.drawImage(ImageRegistry.getImage("legs0.png", false), this.getPosOnScreen(playerx, playery).x,this.getPosOnScreen(playerx, playery).y, w, height, null);
            g.drawImage(ImageRegistry.getImage("blood.png", false), this.getPosOnScreen(playerx, playery).x,this.getPosOnScreen(playerx, playery).y, w, height, null);
        }
        
    }
    @Override
    public void onInteract(final EntityPlayer player){
        if (this.texture[0].equals("partner.png")){
            final EntityLoot loot = this;
            WorldPanel.panel.addMessage(new String[]{"normachead.png It's just a flesh wound, I'll be fine. Please don't take my stuff."});
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.openInventory(loot);
                    player.credits += credits;
                    credits = 0;
                }
                
            };
            return;
        }
        WorldPanel.panel.openInventory(this);
        player.credits += credits;
        credits = 0;
    }
    public static ArrayList<Item> createLoot(Loot type){
        ArrayList<Item> loots = new ArrayList<>();
        Item ammo = null;
        switch (type){
            case BUNKER:
                ammo = ItemWeapon.AMMOCELL;
                loots.add(new ItemAid("Nachos", "nachos.png", 50));
                int amount = (int)Math.floor(Math.random() * 3);
                for (int i = 0; i < amount; i++) {
                    loots.add(new ItemExenol());
                }
                break;
            case TRIAD:
                
                ammo = ItemWeapon.NINEMM;
                if (Math.random() < 0.5){
                    ammo = ItemWeapon.THIRTY;
                }
                loots.add(new ItemAid("SnacPac", "snacpac.png", 25));
                int amount2 = (int)Math.floor(Math.random() * 3);
                for (int i = 0; i < amount2; i++) {
                    loots.add(ItemWeapon.ROCKET);
                }
                break;
        }
        
        int amount = (int)Math.ceil(Math.random() * 20);
        for (int i = 0; i < amount; i++){
            loots.add(ammo);
        }
        //for (int i = 0; i < amount; i++, loots.add(ammo));
        return loots;
    }
}

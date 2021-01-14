/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import thecolony.entity.behavior.AINPC;
import thecolony.items.ItemNote;
import thecolony.StoreItem;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.items.*;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.items.drugs.*;

/**
 *
 * @author pdogmuncher
 */
public class EntityVendor extends EntityLiving{
    public ArrayList<StoreItem> store = new ArrayList<>();
    ArrayList<String> greeting = new ArrayList<>();
    public EntityVendor(World world, double x, double y, int w, int h, int height, String[] texture, String[] store){
        super(world, x, y, w, h, height, new String[][]{texture, texture, texture}, 50, 0, new AINPC(), 0);
        int j = 0;
        while (!store[j].equals("xxx")){
            greeting.add(store[j]);
            j++;
        }
        j++;
        for (int i = 0; i < (store.length - j) / 3; i++){
            Item item = getItemFromString(store[i * 3 + j]);
            int cost = Integer.parseInt(store[i * 3 + j + 1]);
            int quantity = Integer.parseInt(store[i * 3 + j + 2]);
            this.store.add(new StoreItem(item, cost, quantity));
        }
        //List<StoreItem> list = Arrays.asList(store);
        
        this.credits = 50;
        
    }
    public static Item getItemFromString(String name){
        switch(name){
            case "exenol":
                return new ItemExenol();
            case "noctaine":
                return new ItemNoctaine();
            case "adrenaline":
                return new ItemAdrenaline();
            case "snacpac":
                return new ItemAid("SnacPac", "snac.png", 25);
            case "nachos":
                return new ItemAid("Nachos", "nachos.png", 50);
            case "plasmacannon":
                return new ItemWeapon("Plasma Cell LMG", "plasmacannon.png", EntityBullet.PLASMA, "Lays down suppressive fire with a huge fire rate", 10, 2, 0, 20, 700, "plasma", "rifle", 35, ItemWeapon.AMMOCELL, 100, 1, 0, FireType.AUTO, Scope.NONE, false);
            case "plasmacell50":
                return new ItemAmmoStash(ItemWeapon.AMMOCELL, 50);
            default:
                return null;
        }
    }
    public EntityVendor(World world, double x, double y, int w, int h, int height, String[] texture, StoreItem[] store){
        super(world, x, y, w, h, height, new String[][]{texture, texture, texture}, 50, 0, new AINPC(), 0);
        List<StoreItem> list = Arrays.asList(store);
        for (StoreItem item : list){
            this.store.add(item);
        }
        this.credits = 50;
        
    }
    @Override
    public void takeDamage(int amount, EntityLiving attacker){
        super.takeDamage(amount, attacker);
        if (health <= 0 && inventory.isEmpty()){
            for (StoreItem item : store){
                inventory.add(item.item);
            }
            if (texture[0].contains("drugs") && world.name.equals("haven market")){
                inventory.add(new ItemNote("Chah Tenn's Note", 
                        new String[]{"Nice work this week, Mitt. You almost beat your personal record. You're up for a raise.",
                            "There has been an incident involving a private investigator. Be on the lookout.", 
                            "I've moved our shipping headquarters to the back of the Ubermart as a precaution."}));
            }
            else if (attacker == world.player){
                world.player.addReputation(-10);
            }
        }
    }
    @Override
    public void onInteract(final EntityPlayer p){
        super.onInteract(p);
        if (texture[0].contains("drugs") && world.name.equals("haven market")){
            WorldPanel.panel.addMessage(new String[]{"zayvehead.png I'm looking for someone named Chah Tenn.", "dealerhead.png Buzz off unless you're buying."});
            final EntityVendor store = this;
            WorldPanel.panel.runOnText = new Runnable(){

                @Override
                public void run() {
                    WorldPanel.panel.showStore(store);
                }
                
            };
                      
        }
        else if (!greeting.isEmpty()){
            WorldPanel.panel.addMessage(greeting.toArray(new String[0]));
            final EntityVendor thestore = this;
            WorldPanel.panel.runOnText = new Runnable(){

                @Override
                public void run() {
                    WorldPanel.panel.showStore(thestore);
                }
                
            };
        }
        else{
            WorldPanel.panel.showStore(this);
        }
    }
}

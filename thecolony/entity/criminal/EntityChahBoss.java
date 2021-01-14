/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.criminal;

import thecolony.entity.behavior.AIFighter;
import thecolony.items.ItemArmor;
import thecolony.items.drugs.ItemExenol;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.EntityFighter;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public class EntityChahBoss extends EntityFighter{
    boolean intro = true;
    public EntityChahBoss(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"chah0.png", "chah1.png", "chahtenn.png"}}, 150, 5, 15, "chahEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.SENTRY});
        freeze = true;
        ItemArmor thiefArmor = new ItemArmor("Tenn Cartel Leader's Uniform", "The uniform of Chah Tenn, leader of the Tenn Cartel", "tennclothes.png", "thiefclothes.png", 5);
        ItemHelmet thiefHelmet = new ItemHelmet("Dark Glasses", "Instantly become the coolest kid on the block", "glasses.png", "null.png", 1);
        //ItemWeapon plasmaRevolver = new ItemWeapon("Plasma Cell Revolving Rifle", "plasmasubgun.png", "Slow, inaccurate, and deadly", 60, 50, 0, 10, 20, "plasma", 15, ItemWeapon.AMMOCELL, 10, 1, FireType.BOLT, Scope.NONE);
        ItemWeapon plasmaRevolver = new ItemWeapon("Cannonballer", "cannonballer.png", null, "Fires cannonballs that fracture on impact", 20, 20, 8, 1, 500, "cannonball", "rifle", 20, ItemWeapon.CANNONBALL, 1, 1, 0, FireType.BOLT, Scope.NONE, false);
        plasmaRevolver.bulletsize = 16;
        plasmaRevolver.clusterBomb = true;
        ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
        //((AIFighter)ai).firearm = plasmaRevolver;
        ((AISmart)ai).melee = switchblade;
        armor = thiefArmor;
        helmet = thiefHelmet;
        weapon = plasmaRevolver;
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(weapon);
        inventory.add(switchblade);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(ItemWeapon.CANNONBALL);
        inventory.add(new ItemExenol());
        credits = 10;
    }
    @Override
    public void update(){
        
        super.update();
        if (intro){
            freeze = true;
            world.player.frozen = true;
            intro = false;
            WorldPanel.panel.addMessage(new String[]{"tennhead.png Zayve? How did you get in here?", "zayvehead.png Through a combination of detective work and gun fu.", "tennhead.png GUARDS!", "zayvehead.png Don't bother, they're mostly dead.", "tennhead.png I guess I'll have to settle this myself."});
            WorldPanel.panel.runOnText = new Runnable(){

                @Override
                public void run() {
                    WorldPanel.panel.addAnim("chahtenn", 60, 20, null);
                    freeze = false;
                    world.player.frozen = false;
                }
                
            };
        }
    }
    @Override
    public void onDeath(){
        super.onDeath();
        world.unlock();
        WorldPanel.panel.nextObjective = "";
    }
}

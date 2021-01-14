/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.criminal;

import thecolony.entity.behavior.AIFighter;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityFighter;
import thecolony.entity.behavior.AISmart;

/**
 *
 * @author pdogmuncher
 */
public class EntityThief extends EntityFighter{
    
    public EntityThief(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"thief.png", "thief0.png", "thief1.png"}}, 50, 5, 15, "criminalEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC});
        ItemArmor thiefArmor = new ItemArmor("Dark Clothing", "Perfect for sneaking around in", "thiefclothes.png", "thiefclothes.png", 2);
        ItemHelmet thiefHelmet = new ItemHelmet("Mask", "Instantly become the sketchiest kid on the block!", "thiefmask.png", "null.png", 1);
        ItemWeapon plasmaRevolver = new ItemWeapon("Plasma Cell Revolver", "snubnose.png", EntityBullet.PLASMA, "A popular personal defense firearm", 15, 10, 8, 2, 500, "plasma", "pistol", 25, ItemWeapon.AMMOCELL, 4, 1, 0, FireType.BOLT, Scope.MAG, false);
        if (Math.random() >= 0.5){
            plasmaRevolver = new ItemWeapon("Plasma Cell Scattergun", "plasmashotgun.png", EntityBullet.PLASMA, "Shoots a scattered burst of plasma", 7, 14, 15, 2, 400, "plasma", "pistol", 25, ItemWeapon.AMMOCELL, 3, 20, 42, FireType.BOLT, Scope.NONE, false);
        }
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
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        inventory.add(ItemWeapon.AMMOCELL);
        credits = 10;
    }
    @Override
    public void onDeath(){
        if (world.name.equals("thief house")){
            WorldPanel.panel.addMessage(new String[]{"Nice job! You can loot the body by right-clicking on it. This tutorial is finished. Head out that door to start your adventure."});
            WorldPanel.panel.nextObjective = "Exit the house";
            world.unlock();
        }
    }
}

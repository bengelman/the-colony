/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.cdf;

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
import thecolony.entity.behavior.AISmart.CombatStyle;

/**
 *
 * @author pdogmuncher
 */
public class EntityPolice extends EntityFighter{
    
    public EntityPolice(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"police.png", "police0.png", "police1.png"},{"fempolice.png", "fempolice0.png", "fempolice1.png"}}, 50, 250 / WorldPanel.TICKS_PER_SECOND, 15, "cdfEnemies", new CombatStyle[]{CombatStyle.ASSAULT, CombatStyle.CHASER});
        ItemArmor thiefArmor = new ItemArmor("Police Kevlar Vest", "A bulletproof vest worn by police", "swatvest.png", "swatvest.png", 8);
        ItemHelmet thiefHelmet = new ItemHelmet("Gas Mask", "Filters out poisonous gas", "gasmask.png", "gasmaskflip.png", 6);
        if (style == CombatStyle.CHASER){
            ItemWeapon firearm = new ItemWeapon("Hydra Shotgun", "advancedshotgun.png", EntityBullet.BULLET, "The shotgun shoots a burst of projectiles", 15, 10, 10, 0, 400, "bullet", "rifle", 25, ItemWeapon.TWELVEGAUGE, 7, 8, 36, FireType.BOLT, Scope.NONE, false);
        
            ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
            //((AIFighter)ai).firearm = firearm;
            ((AISmart)ai).melee = switchblade;
            armor = thiefArmor;
            helmet = thiefHelmet;
            weapon = firearm;
            inventory.add(armor);
            inventory.add(helmet);
            inventory.add(weapon);
            inventory.add(switchblade);
            for (int i = 0; i < 21; i++) {
                inventory.add(ItemWeapon.TWELVEGAUGE);
            }
        }
        else{
            ItemWeapon firearm = new ItemWeapon("Hailfire SMG", "mp5.png", EntityBullet.BULLET, "The Hailfire SMG is used by CDF assault teams", 15, 2, 2, 1, 500, "bullet", "pistol", 25, ItemWeapon.NINEMM, 50, 1, 0, FireType.AUTO, Scope.NONE, false);
        
            ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
            //((AIFighter)ai).firearm = firearm;
            ((AISmart)ai).melee = switchblade;
            armor = thiefArmor;
            helmet = thiefHelmet;
            weapon = firearm;
            inventory.add(armor);
            inventory.add(helmet);
            inventory.add(weapon);
            inventory.add(switchblade);
            for (int i = 0; i < 50; i++) inventory.add(ItemWeapon.NINEMM);
        }
        
        credits = 10;
    }
}

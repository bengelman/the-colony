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
public class EntitySoldier extends EntityFighter{
    public EntitySoldier(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"soldier.png", "soldier0.png", "soldier1.png"}, {"femsoldier.png", "femsoldier0.png", "femsoldier1.png"}}, 50, 250 / WorldPanel.TICKS_PER_SECOND, 15, "cdfEnemies", new CombatStyle[]{CombatStyle.SENTRY, CombatStyle.CLASSIC});
        ItemArmor thiefArmor = new ItemArmor("Military Fatigues", "The uniform of the Colonies military", "armyuniform.png", "armyuniform.png", 9);
        ItemHelmet thiefHelmet = new ItemHelmet("Gas Mask", "Filters out poisonous gas", "gasmask.png", "gasmaskflip.png", 6);
        if (style == CombatStyle.ASSAULT){
            ItemWeapon firearm = new ItemWeapon("Helix AR", "tavor.png", EntityBullet.BULLET, "The Helix AR was the standard-issue rifle of the Colonies during the war", 20, 3, 4, 0, 1200, "bullet", "rifle", 30, ItemWeapon.FIVECM, 20, 1, 0, FireType.AUTO, Scope.DIGITAL, false);
        
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
            for (int i = 0; i < 60; i++) {
                inventory.add(ItemWeapon.FIVECM);
            }
        }
        else{
            ItemWeapon firearm = new ItemWeapon("Deadshot Sniper Rifle", "sniper.png", EntityBullet.BULLET, "This sniper rifle deals high damage at long range", 40, 18, 8, 0, 1500, "snipershot", "rifle", 130, ItemWeapon.THIRTY, 5, 1, 0, FireType.BOLT, Scope.LONGRANGE, false);
        
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
            for (int i = 0; i < 20; i++) inventory.add(ItemWeapon.THIRTY);
        }
        
        credits = 10;
    }
}

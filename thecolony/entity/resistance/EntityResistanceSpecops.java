/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.resistance;

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
public class EntityResistanceSpecops extends EntityFighter{
    public EntityResistanceSpecops(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"soldier.png", "soldier0.png", "soldier1.png"}, {"femsoldier.png", "femsoldier0.png", "femsoldier1.png"}}, 50, 250 / WorldPanel.TICKS_PER_SECOND, 15, "resistanceEnemies", new AISmart.CombatStyle[]{CombatStyle.CHASER, CombatStyle.ASSAULT});
        ItemArmor thiefArmor = new ItemArmor("Resistance Fatigues", "A protective vest that optimizes stealth", "stealthuniform.png", "stealthuniform.png", 8);
        ItemHelmet thiefHelmet = new ItemHelmet("Special Ops Helmet", "A helmet worn by resistance special ops", "spechelmet.png", "spechelmetflip.png", 6);
        if (style == CombatStyle.ASSAULT){
            ItemWeapon firearm = new ItemWeapon("AKS-52U", "aks.png", EntityBullet.BULLET, "An assault carbine with a built-in silencer", 21, 5, 4, 3, 1000, "silencer", "rifle", 30, ItemWeapon.FIVECM, 20, 1, 0, FireType.AUTO, Scope.MAG, false);
        
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
            for (int i = 0; i < 60; i++) inventory.add(ItemWeapon.FIVECM);
        }
        else{
            ItemWeapon firearm = new ItemWeapon("Petrov 12", "autoshotgun.png", EntityBullet.BULLET, "An extremely durable automatic shotgun", 10, 5, 7, 10, 600, "bullet", "rifle", 25, ItemWeapon.TWELVEGAUGE, 12, 8, 18, FireType.AUTO, Scope.NONE, false);
        
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
            for (int i = 0; i < 24; i++) inventory.add(ItemWeapon.TWELVEGAUGE);
        }
        
        credits = 10;
    }
}

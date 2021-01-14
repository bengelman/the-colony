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
public class EntityResistanceSoldier extends EntityFighter{
    public EntityResistanceSoldier(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"soldier.png", "soldier0.png", "soldier1.png"}, {"femsoldier.png", "femsoldier0.png", "femsoldier1.png"}}, 50, 250 / WorldPanel.TICKS_PER_SECOND, 15, "resistanceEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.ASSAULT, CombatStyle.CHASER});
        ItemArmor thiefArmor = new ItemArmor("Resistance Fatigues", "A protective vest that optimizes stealth", "stealthuniform.png", "stealthuniform.png", 8);
        ItemHelmet thiefHelmet = new ItemHelmet("Soldier Helmet", "A protective helmet", "cyberhelmet.png", "cyberflip.png", 6);
        if (style == CombatStyle.CHASER){
            ItemWeapon firearm = new ItemWeapon("AK-52", "ak.png", EntityBullet.BULLET, "An extremely reliable assault rifle", 25, 5, 6, 3, 800, "bullet", "rifle", 30, ItemWeapon.FIVECM, 20, 1, 0, FireType.AUTO, Scope.NONE, false);
        
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
            ItemWeapon firearm = new ItemWeapon("Scofield .50", "handgun.png", EntityBullet.BULLET, "An extremely powerful sidearm chambered in .50 AE", 25, 6, 10, 2, 500, "bullet", "pistol", 25, ItemWeapon.FIFTYAA, 7, 1, 0, FireType.SEMI, Scope.NONE, false);
        
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
            for (int i = 0; i < 21; i++) inventory.add(ItemWeapon.FIFTYAA);
        }
        
        credits = 10;
    }
}

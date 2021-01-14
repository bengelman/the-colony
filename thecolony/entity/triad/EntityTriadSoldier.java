/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.triad;

import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.World;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityFighter;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.CombatStyle;

/**
 *
 * @author pdogmuncher
 */
public class EntityTriadSoldier extends EntityFighter{
    
    public EntityTriadSoldier(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"thug.png", "thug0.png", "thug1.png"}, {"thugalt.png", "thugalt0.png", "thugalt1.png"}}, 50, 5, 15, "triadEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC, AISmart.CombatStyle.CLASSIC, AISmart.CombatStyle.CLASSIC, AISmart.CombatStyle.SENTRY});
        ItemArmor thiefArmor = new ItemArmor("Triad Armor", "Bullet resistant armor", "cyberarmor.png", "cyberarmor.png", 7);
        ItemHelmet thiefHelmet = new ItemHelmet("Soldier Helmet", "A protective helmet", "cyberhelmet.png", "cyberflip.png", 3);
        ItemWeapon firearm = new ItemWeapon("L23 Assault Rifle", "ar.png", EntityBullet.BULLET, "The L23 is a powerful and precise semi-automatic firearm", 20, 5, 4, 1, 700, "bullet", "rifle", 30, ItemWeapon.FIVECM, 20, 1, 0, FireType.SEMI, Scope.NONE, false);
        ItemWeapon firearm2 = new ItemWeapon("L200 SAW", "saw.png", EntityBullet.BULLET, "The SAW is a fully automatic firearm that can fire many rounds quickly", 15, 3, 2, 4, 1000, "bullet", "rifle", 30, ItemWeapon.FIVECM, 80, 1, 0, FireType.AUTO, Scope.MAG, false);
        ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
        //((AIFighter)ai).firearm = firearm;
        ((AISmart)ai).melee = switchblade;
        armor = thiefArmor;
        helmet = thiefHelmet;
        weapon = style == CombatStyle.CLASSIC ? firearm : firearm2;
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(weapon);
        inventory.add(switchblade);
        for (int i = 0; i < 40; i++) {
            inventory.add(ItemWeapon.FIVECM);
        }
        inventory.add(ItemWeapon.ROCKET);
        inventory.add(ItemWeapon.ROCKET);
        credits = 10;
    }
}

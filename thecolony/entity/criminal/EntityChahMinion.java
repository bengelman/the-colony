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
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityFighter;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.CombatStyle;

/**
 *
 * @author pdogmuncher
 */
public class EntityChahMinion extends EntityFighter{
    
    public EntityChahMinion(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"chahminion.png", "chahminion0.png", "chahminion1.png"}}, 50, 5, 15, "chahEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC, AISmart.CombatStyle.BLITZER});
        ItemArmor thiefArmor = new ItemArmor("Tenn Cartel Uniform", "The uniform of the Tenn Cartel", "tennclothes.png", "thiefclothes.png", 2);
        ItemHelmet thiefHelmet = new ItemHelmet("Dark Glasses", "Instantly become the coolest kid on the block", "glasses.png", "null.png", 1);
        //ItemWeapon plasmaRevolver = new ItemWeapon("Plasma Cell Revolving Rifle", "plasmasubgun.png", "Slow, inaccurate, and deadly", 60, 50, 0, 10, 20, "plasma", 15, ItemWeapon.AMMOCELL, 10, 1, FireType.BOLT, Scope.NONE);
        ItemWeapon plasmaRevolver = new ItemWeapon("Plasma Cell Revolving Rifle", "bullpup.png", null, "Fires powerful bursts of plasma", 30, 20, 4, 1, 500, "plasma", "rifle", 25, ItemWeapon.AMMOCELL, 10, 1, 0, FireType.AUTO, Scope.MAG, false);
        ItemWeapon plasmaSMG = new ItemWeapon("Plasma Cell SMG", "plasmasmg.png", EntityBullet.PLASMA, "Fires plasma at a high rate", 10, 1, 4, 1, 400, "plasma", "rifle", 25, ItemWeapon.AMMOCELL, 20, 1, 0, FireType.AUTO, Scope.MAG, false);
        plasmaRevolver.bulletsize = 16;
        ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
        //((AIFighter)ai).firearm = plasmaRevolver;
        ((AISmart)ai).melee = switchblade;
        armor = thiefArmor;
        helmet = thiefHelmet;
        weapon = style == CombatStyle.CLASSIC ? plasmaRevolver : plasmaSMG;
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
        inventory.add(new ItemExenol());
        credits = 10;
    }
    @Override
    public void update(){
        super.update();
            
    }
}

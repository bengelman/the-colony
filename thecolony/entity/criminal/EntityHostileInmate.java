/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.criminal;

import thecolony.entity.triad.EntityPrisonGuard;
import thecolony.entity.behavior.AIFighter;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.World;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;
import static thecolony.entity.EntityFighter.prisonEnemies;
import static thecolony.entity.EntityFighter.prisonHeroes;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.resistance.EntityPartner;

/**
 *
 * @author pdogmuncher
 */
public class EntityHostileInmate extends EntityFighter{
    
    public EntityHostileInmate(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"inmate.png", "inmate0.png", "inmate1.png"}, {"feminmate.png", "feminmate0.png", "feminmate1.png"}}, 50, 3, 15, "prisonHeroes", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC});
        ItemArmor thiefArmor = new ItemArmor("Inmate Uniform", "A uniform for prison inmates", "inmateoutfit.png", "inmateflipped.png", 2);
        ItemHelmet thiefHelmet = new ItemHelmet("Cap", "A comfy hat", "thugcap.png", "thugcap.png", 1);
        
        ItemWeapon switchblade = new ItemWeapon("Prison Shank", "switchblade.png", "An improvised blade", 10, 25);
        //((AIFighter)ai).firearm = null;
        ((AISmart)ai).melee = switchblade;
        armor = thiefArmor;
        helmet = thiefHelmet;
        weapon = switchblade;
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(weapon);
        credits = 10;
    }
    public void update(){
        super.update();
        if (this.targets.equals("prisonHeroes")){
            this.targets = "prisonHeroes";
        }
    }
    public void takeDamage(int amount, EntityLiving attacker){
        super.takeDamage(amount, attacker);
        
        if (EntityFighter.groups.get("prisonHeroes").isEmpty()){
            //System.out.println(texture[0] + " attacked by " + attacker.texture[0]);
            EntityFighter.groups.get("prisonHeroes").add(EntityPlayer.class);
            EntityFighter.groups.get("prisonHeroes").add(EntityHelperInmate.class);
            EntityFighter.groups.get("prisonHeroes").add(EntityPartner.class);
            EntityFighter.groups.get("prisonEnemies").add(EntityPrisonGuard.class);
            EntityFighter.groups.get("prisonEnemies").add(EntityHostileInmate.class);
        }
    }
}

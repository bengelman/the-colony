/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.triad;

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
import thecolony.entity.criminal.EntityHelperInmate;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;
import static thecolony.entity.EntityFighter.prisonEnemies;
import static thecolony.entity.EntityFighter.prisonHeroes;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.criminal.EntityHostileInmate;
import thecolony.entity.resistance.EntityPartner;

/**
 *
 * @author pdogmuncher
 */
public class EntityPrisonGuard extends EntityFighter{
    
    public EntityPrisonGuard(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"thug.png", "thug0.png", "thug1.png"}, {"thugalt.png", "thugalt0.png", "thugalt1.png"}}, 50, 3, 15, "prisonHeroes", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC});
        ItemArmor thiefArmor = new ItemArmor("Triad Uniform", "The uniform of triad thugs", "thuguniform.png", "thuguniform.png", 5);
        ItemHelmet thiefHelmet = new ItemHelmet("Cap", "A comfy hat", "thugcap.png", "thugcap.png", 1);
        ItemWeapon plasmaRevolver;
        //plasmaRevolver= new ItemWeapon("Sawed-Off Shotgun", "sawedoff.png", "The shotgun's spread makes it weak at long range and against armor", 10, 25, 2, 3, 20, "bullet", 10, ItemWeapon.TWELVEGAUGE, 7, 0, 6, false, false);
        plasmaRevolver = new ItemWeapon("L9 Compact Handgun", "compact.png", EntityBullet.BULLET, "A light, effective sidearm", 15, 4, 4, 2, 500, "bullet", "pistol", 25, ItemWeapon.NINEMM, 13, 1, 0, FireType.SEMI, Scope.MAG, false);
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
        for (int i = 0; i < 26; i++) {
            inventory.add(ItemWeapon.NINEMM);
        }
        credits = 10;
        
    }
    public void update(){
        super.update();
        if (!this.targets.equals("prisonHeroes")){
            this.targets = "prisonHeroes";
        }
    }
    @Override
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
        if (this.health <= 0){
            this.world.unlock();
        }
    }
}

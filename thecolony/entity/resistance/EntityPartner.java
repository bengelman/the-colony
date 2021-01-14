/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.resistance;

import thecolony.entity.criminal.EntityHostileInmate;
import thecolony.entity.triad.EntityPrisonGuard;
import thecolony.entity.behavior.AIFighter;
import thecolony.entity.behavior.AIPartner;
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
import thecolony.entity.behavior.AISmart.CombatStyle;

/**
 *
 * @author pdogmuncher
 */
public class EntityPartner extends EntityFighter{
    String[] conversation;
    boolean frozen = false;
    ItemWeapon knife = new ItemWeapon("Laser Knife", "laserknife.png", "A superheated plasma knife", 20, 25);
    public EntityPartner(World world, int x, int y, String[] conversation, boolean inPrison){
        super(world, x, y, 36, 25, 48, new String[][]{{"partner.png", "partner0.png", "partner1.png"}}, 500, 8, 15, "resistanceEnemies", new CombatStyle[]{CombatStyle.PARTNER});
        if (inPrison){
            this.targets = "prisonEnemies";
        }
        this.ai = new AIPartner();
        ai.e = this;
        
        ItemArmor thiefArmor = new ItemArmor("Inmate Uniform", "A uniform for prison inmates", "inmateoutfit.png", "inmateflipped.png", 2);
        ItemHelmet thiefHelmet = new ItemHelmet("Cap", "A comfy hat", "thugcap.png", "thugcap.png", 1);
        
        ItemArmor swagArmor = new ItemArmor("Leather Jacket", "I told you not to take my stuff -Normac", "leatherjacket.png", "thiefclothes.png", 5);
        ItemHelmet swagHelmet = new ItemHelmet("Aviator Sunglasses", "Cool retro sunglasses you stole from your partner", "glasses.png", "null.png", 1);
        
        ItemWeapon gun = new ItemWeapon("Sawed-Off Shotgun", "sawedoff.png", EntityBullet.BULLET, "The sawed-off is powerful at close range against unarmored targets", 10, 10, 20, 0, 300, "bullet", "pistol", 15, ItemWeapon.TWELVEGAUGE, 7, 6, 30, FireType.BOLT, Scope.NONE, false);
        gun.ricochet = true;
        ItemWeapon switchblade = new ItemWeapon("Prison Shank", "switchblade.png", "An improvised blade", 10, 25);
        if (inPrison){
            armor = thiefArmor;
            helmet = thiefHelmet;
        }
        else{
            armor = swagArmor;
            helmet = swagHelmet;
        }
        ((AIPartner)ai).firearm = gun;
        ((AIPartner)ai).melee = switchblade;
        
        weapon = gun;
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(weapon);
        inventory.add(switchblade);
        for (int i = 0; i < 14; i++) {
            inventory.add(ItemWeapon.TWELVEGAUGE);
        }
        this.conversation = conversation;
    }
    @Override
    public void update(){
        if (!frozen) {
            super.update();
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
    }
    @Override
    public void onInteract(final EntityPlayer p){
        super.onInteract(p);
        if (conversation != null){
            WorldPanel.panel.addMessage(conversation);
            
        }
    }
}

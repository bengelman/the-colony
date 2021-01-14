/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import thecolony.entity.criminal.EntityHelperInmate;
import thecolony.entity.cdf.EntitySoldier;
import thecolony.entity.cdf.EntityPolice;
import thecolony.entity.resistance.EntityPartner;
import thecolony.entity.criminal.EntityHostileInmate;
import thecolony.entity.triad.EntityPrisonGuard;
import thecolony.entity.triad.EntityTriadSoldier;
import thecolony.entity.triad.EntityThug;
import thecolony.entity.criminal.EntityThief;
import thecolony.entity.criminal.EntityChahMinion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import thecolony.entity.behavior.AIFighter;
import thecolony.World;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.CombatStyle;
import thecolony.entity.criminal.EntityChahBoss;
import thecolony.entity.criminal.EntityMichaelChuck;

/**
 *
 * @author pdogmuncher
 */
public abstract class EntityFighter extends EntityLiving{
    //public ArrayList<Class> targets;
    
    public String targets;
    //public static ArrayList<Class> heroes = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntityHelperInmate.class, EntityPolice.class, EntitySoldier.class}));
    public static ArrayList<Class> triadEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntityHelperInmate.class, EntityPolice.class, EntitySoldier.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
    public static ArrayList<Class> resistanceEnemies = new ArrayList(Arrays.asList(new Class[]{EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class, EntitySoldier.class, EntityPolice.class}));
    public static ArrayList<Class> cdfEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPartner.class, EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
    public static ArrayList<Class> criminalEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntitySoldier.class, EntityPolice.class}));
    
    public static ArrayList<Class> prisonHeroes = new ArrayList(Arrays.asList(new Class[]{}));
    public static ArrayList<Class> prisonEnemies = new ArrayList(Arrays.asList(new Class[]{}));
    public CombatStyle style;
    //public static ArrayList<Class> enemies = new ArrayList(Arrays.asList(new Class[]{EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
    public static ArrayList<Class> chahEnemies = new ArrayList(Arrays.asList(new Class[]{EntityThug.class, EntityTriadSoldier.class, EntityPrisonGuard.class, EntityPolice.class, EntitySoldier.class}));
    
    public static TreeMap<String, ArrayList<Class>> groups = new TreeMap<String, ArrayList<Class>>(){{put("cdfEnemies", cdfEnemies);put("criminalEnemies",criminalEnemies);put("resistanceEnemies", resistanceEnemies);put("triadEnemies", triadEnemies); put("prisonHeroes", prisonHeroes); put("prisonEnemies", prisonEnemies); put("chahEnemies", chahEnemies);}};
    
    public static boolean prisonBreak = false;
    public EntityFighter(World world, double x, double y, int w, int h, int height, String[][] texture, int health, int maxVelocity, int strength, String targets, CombatStyle[] styles){
        super(world, x, y, w, h, height, texture, health, maxVelocity, AISmart.generateAI(styles[(int)(Math.random() * styles.length)]), strength);
        this.style = ((AISmart)ai).style;
        this.targets = targets;
    }
    @Override
    public void takeDamage(int amount, EntityLiving attacker){
        super.takeDamage(amount, attacker);
        if (attacker == attacker.world.player && !groups.get(targets).contains(EntityPlayer.class) && health <= 0){
            attacker.world.player.addReputation(-5);
        }
    }
    public static void reinitClasses(){
        //heroes = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntityHelperInmate.class, EntityPolice.class, EntitySoldier.class}));
        triadEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntityHelperInmate.class, EntityPolice.class, EntitySoldier.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
        prisonHeroes = new ArrayList(Arrays.asList(new Class[]{}));
        prisonEnemies = new ArrayList(Arrays.asList(new Class[]{}));
        resistanceEnemies = new ArrayList(Arrays.asList(new Class[]{EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class, EntitySoldier.class, EntityPolice.class}));
        //enemies = new ArrayList(Arrays.asList(new Class[]{EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
        chahEnemies = new ArrayList(Arrays.asList(new Class[]{EntityThug.class, EntityTriadSoldier.class, EntityPrisonGuard.class}));
        cdfEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPartner.class, EntityThief.class, EntityThug.class, EntityTriadSoldier.class, EntityHostileInmate.class, EntityPrisonGuard.class, EntityChahMinion.class, EntityChahBoss.class, EntityMichaelChuck.class}));
        criminalEnemies = new ArrayList(Arrays.asList(new Class[]{EntityPlayer.class, EntityPartner.class, EntitySoldier.class, EntityPolice.class}));
        if (groups != null) {
            groups.clear();
        }
        groups = new TreeMap<String, ArrayList<Class>>(){{put("cdfEnemies", cdfEnemies);put("criminalEnemies",criminalEnemies);put("resistanceEnemies", resistanceEnemies);put("triadEnemies", triadEnemies); put("prisonHeroes", prisonHeroes); put("prisonEnemies", prisonEnemies); put("chahEnemies", chahEnemies);}};
    }
}

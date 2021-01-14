
package thecolony;

import thecolony.items.ItemAid;
import thecolony.items.drugs.ItemAdrenaline;
import thecolony.items.drugs.ItemExenol;
import thecolony.items.drugs.ItemTransneuract;
import thecolony.items.ItemWeapon;
import thecolony.items.drugs.ItemNicotineGum;
import thecolony.items.drugs.ItemNoctaine;
import thecolony.entity.object.EntityCutscene;
import thecolony.entity.object.EntityBed;
import thecolony.entity.object.EntityFalseWall;
import thecolony.entity.object.EntityGate;
import thecolony.entity.object.EntityJournal;
import thecolony.entity.EntityNPC;
import thecolony.entity.EntityPlayer;
import thecolony.entity.object.EntityPortal;
import thecolony.entity.criminal.EntityThief;
import thecolony.entity.triad.EntityThug;
import thecolony.entity.triad.EntityTriadSoldier;
import thecolony.entity.Entity;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityExplodable;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.entity.criminal.EntityChahMinion;
import thecolony.entity.criminal.EntityChahTenn;
import thecolony.entity.EntityFighter;
import thecolony.entity.criminal.EntityHelperInmate;
import thecolony.entity.criminal.EntityHostileInmate;
import thecolony.entity.EntityLoot;
import thecolony.entity.EntityLoot.Loot;
import thecolony.entity.criminal.EntityMichaelChuck;
import thecolony.entity.resistance.EntityPartner;
import thecolony.entity.cdf.EntityPolice;
import thecolony.entity.EntityReadable;
import thecolony.entity.resistance.EntityResistanceSoldier;
import thecolony.entity.resistance.EntityResistanceSpecops;
import thecolony.entity.cdf.EntitySoldier;
import thecolony.entity.triad.EntityPrisonGuard;
import thecolony.entity.EntityVehicle;
import thecolony.entity.EntityVendor;
import thecolony.entity.criminal.EntityChahBoss;
import thecolony.entity.object.EntityStolenItems;
import thecolony.items.ItemAmmoStash;

/**
 *
 * @author pdogmuncher
 */
public class World implements Serializable{
    public ArrayList<Entity> entities = new ArrayList<>();
    public EntityPlayer player;
    public String background;
    public String music;
    public boolean linear = false;
    public String midPath = "";
    public TreeMap<String, String[]> screens = new TreeMap<String, String[]>(){
        {
            put("house", new String[]{"The screen is displaying a list of your active cases."});
            put("base", new String[]{"It's showing some propoganda film about the mistreatment of aliens."});
            put("bunker", new String[]{"The recipe for exenol is displayed... Apparently it's main ingredient is something called \"Dank Memes.\""});
            put("glitch", new String[]{"There's computer code on the screen."});
            put("househaven2", new String[]{"(A movie is playing)", "holoscreen.png Voice 1: If I pull this off, will you die?", "holoscreen.png Voice 2: It would be extremely painful.", "holoscreen.png Voice 1: You're a big guy.", "holoscreen.png Voice 2: For you."});
            put("jmishtin house", new String[]{"What is this... The screen is displaying... Triad fanfiction?"});
            put("machos nachos", new String[]{"It's the menu for Macho's Nachos. There are 12 different types of nachos displayed."});
        }
    };
    
    
    public String name;
    public int width, height;
    boolean playedCartelConcern = false;
    public String tileset;
    public boolean cleared = false;
    public boolean altedHint = false;
    public EntityChahTenn chahtenn = null;
    boolean playedHouse = false;
    //public static ItemWeapon sniper = new ItemWeapon("Deadshot Sniper Rifle", "sniper.png", EntityBullet.BULLET, "The sniper rifle deals high damage at long range", 40, 18, 8, 0, 10, "snipershot", "rifle", 138, ItemWeapon.THIRTY, 5, 1, 0, FireType.BOLT, Scope.LONGRANGE, false);
    
    public boolean playedThugsStart = false;
    public String tips;
    public boolean playPhtrip = false;
    String path;
    public static int[][] parseStream(InputStream stream){
        int[][] data = new int[100][100];
        int[] empty = new int[100];
        Arrays.fill(empty, 0);
        Arrays.fill(data, empty);
        if (stream == null){
            return data;
        }
        Scanner scan = new Scanner(stream);
        int lineNum = 0;
        while(scan.hasNextLine()){
            boolean floating = false;
            String line = scan.nextLine();
            char[] chars = line.toCharArray();
            
            for (int i = 0; i < chars.length; i++){
                if (Character.isDigit(chars[i])){
                    data[lineNum][i] = Integer.parseInt(chars[i] + "");
                }
                lineNum++;
            }
        }
        return data;    
    }
    public World(InputStream stream, int[][] data, String name){
        this.name = name.substring(name.lastIndexOf("/") + 1); 
        if (name.equals("outsideprison") || name.equals("havenpath"))linear=true;
        this.path = name;
        //midPath = name;
        background = name + ".png";
        Scanner scan = new Scanner(stream);
        tileset = scan.nextLine();
        music = scan.nextLine();
        tips = scan.nextLine();
        ArrayList<String> portals = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        portals.add(scan.nextLine());
        for (int index = 0; !(portals.get(portals.size() - 1).equals("xxx")); portals.add(scan.nextLine())){
            String x = scan.nextLine();
            String y = scan.nextLine();
            points.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
        }
            
        int lineNum = 0;
        int portalIndex = 0;
        int npcIndex = 0;
        int vendorIndex = 0;
        while(scan.hasNextLine()){
            boolean floating = false;
            String line = scan.nextLine();
            char[] chars = line.toCharArray();
            if (chars.length * 50 > width){
                width = chars.length * 50;
            }
            for (int i = 0; i < chars.length; i++){
                switch(chars[i]){
                    case 'w':
                        Entity wall = new Entity(this, i * 50, lineNum * 50, 50, 25, 50, new String[]{tileset + "/wall.png"});
                        if (floating){
                            wall.y += wall.h;
                            wall.topLayer = true;
                            wall.h = 0;
                        }
                        entities.add(wall);
                        break;
                    case 'e':
                        Entity upwallh = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/wall.png"});
                        if (floating){
                            upwallh.y += upwallh.h;
                            upwallh.topLayer = true;
                            upwallh.h = 0;
                        }
                        entities.add(upwallh);
                        break;
                    case 'E':
                        Entity wallbase = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/wallbase.png"});
                        if (floating){
                            wallbase.y += wallbase.h;
                            wallbase.topLayer = true;
                            wallbase.h = 0;
                        }
                        entities.add(wallbase);
                        break;
                    case 's':
                        Entity road = new Entity(this, i * 50, lineNum * 50 + 25, 50, 0, 50, new String[]{tileset + "/path.png"});
                        if (floating){
                            road.topLayer = true;
                            road.h = 0;
                        }
                        entities.add(road);
                        break;
                    case 'G':
                        EntityGate gate = new EntityGate(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{"locked.png"}, portals.get(portalIndex), points.get(portalIndex));
                        gate.drawLinear = false;
                        
                        entities.add(gate);
                        portalIndex++;
                        break;
                    case 'Z':
                        System.out.println("OUTDATED LETTER CALL: Z @ " + this.name);
                        break;
                    case 'z':
                        switch(data[lineNum][i]){
                            case 0:
                                Entity frigate = new Entity(this, i * 50, lineNum * 50, 400, 75, 200, new String[]{"frigate.png"});
                                entities.add(frigate);
                                break;
                            case 1:
                                Entity fighter = new Entity(this, i * 50, lineNum * 50, 100, 25, 100, new String[]{"fighter.png"});
                                entities.add(fighter);
                                break;
                        }
                        
                        break;
                    case 'L':
                        Entity falsewall = new EntityFalseWall(this, i * 50, lineNum * 50, 50, 25, 50, new String[]{tileset + "/falsewall.png"});
                        entities.add(falsewall);
                        break;
                    case 'W':
                        Entity upwall = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/wallup.png"});
                        if (floating){
                            upwall.y += upwall.h;
                            upwall.topLayer = true;
                            upwall.h = 0;
                        }
                        entities.add(upwall);
                        break;
                    case 'c':
                        Entity ldwall = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/cornerld.png"});
                        if (floating){
                            ldwall.y += ldwall.h;
                            ldwall.topLayer = true;
                            ldwall.h = 0;
                        }
                        entities.add(ldwall);
                        break;
                    case 'C':
                        Entity rdwall = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/cornerrd.png"});
                        if (floating){
                            rdwall.y += rdwall.h;
                            rdwall.topLayer = true;
                            rdwall.h = 0;
                        }
                        entities.add(rdwall);
                        break;
                    case 'x':
                        Entity luwall = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/cornerlu.png"});
                        if (floating){
                            luwall.y += luwall.h;
                            luwall.topLayer = true;
                            luwall.h = 0;
                        }
                        entities.add(luwall);
                        break;
                    case 'X':
                        Entity ruwall = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/cornerru.png"});
                        if (floating){
                            ruwall.y += ruwall.h;
                            ruwall.topLayer = true;
                            ruwall.h = 0;
                        }
                        entities.add(ruwall);
                        break;
                    case 'P':
                        Entity portal = new EntityPortal(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{"portal.png"}, portals.get(portalIndex), points.get(portalIndex));
                        entities.add(portal);
                        portalIndex++;
                        break;
                    case 'p':
                        Entity cutportal = new EntityCutscene(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{"cutportal.png"}, portals.get(portalIndex), points.get(portalIndex), name + "/cutscene.png");
                        entities.add(cutportal);
                        portalIndex++;
                        break;
                    case 'D':
                        Entity vertdoor = new Entity(this, i * 50, lineNum * 50 + 25, 50, 1, 75, new String[]{"vertdoor.png"});
                        entities.add(vertdoor);
                        break;
                    case 'B':
                        Entity bed = new EntityBed(this, i * 50, lineNum * 50 - 25, 50, 100, 100, new String[]{"bed.png"});
                        entities.add(bed);
                        break;
                    case 'b':
                        Entity box = new Entity(this, i * 50, lineNum * 50, 50, 25, 50, new String[]{tileset + "/box.png"});
                        entities.add(box);
                        break;
                    case 'a':
                        Entity boxtnt = new EntityExplodable(this, i * 50, lineNum * 50, 50, 25, 50, new String[][]{{"tntbox.png"}});
                        entities.add(boxtnt);
                        break;
                    case 'M':
                        Entity upbox = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/box.png"});
                        entities.add(upbox);
                        break;
                    case 'J':
                        Entity journal = new EntityJournal(this, i * 50, lineNum * 50 - 25, 50, 40, 50, new String[]{"journal.png"}, WorldPanel.journals.get(name));
                        entities.add(journal);
                        break;
                    case 'r':
                        Entity roof = new Entity(this, i * 50, lineNum * 50 - 25, 50, 50, 50, new String[]{tileset + "/roof.png"});
                        if (floating && i > 1){
                            roof.y += roof.h;
                            roof.topLayer = true;
                            roof.h = 0;
                        }
                        entities.add(roof);
                        break;
                    case 'S':
                        Entity sign = new EntityJournal(this, i * 50, lineNum * 50 - 25, 50, 30, 50, new String[]{"sign.png"}, WorldPanel.signs.get(name));
                        entities.add(sign);
                        break;
                    case 'f':
                        Entity fence = new Entity(this, i * 50, lineNum * 50 + 20, 50, 5, 50, new String[]{tileset + "/fence.png"});
                        entities.add(fence);
                        break;
                    case 'g':
                        Entity screen = new EntityReadable(this, i * 50, lineNum * 50 + 20, 50, 5, 50, new String[]{"holoscreen.png"}, screens.get(name));
                        entities.add(screen);
                        break;
                    case 'F':
                        Entity fenceup = new Entity(this, i * 50 + 20, lineNum * 50 - 25, 5, 50, 50, new String[]{tileset + "/fenceup.png"});
                        entities.add(fenceup);
                        break;
                    case 'T':
                        EntityThief thief = new EntityThief(this, i * 50, lineNum * 50);
                        entities.add(thief);
                        break;
                    case 'H':
                        EntityChahMinion thiefsy = new EntityChahMinion(this, i * 50, lineNum * 50);
                        entities.add(thiefsy);
                        break;
                    case 'm':
                        EntityMichaelChuck thiefsy2 = new EntityMichaelChuck(this, i * 50, lineNum * 50);
                        entities.add(thiefsy2);
                        break;
                    case 'I':
                        System.out.println("OUTDATED LETTER CALL: I @ " + this.name);
                        break;
                    case 'i':
                        switch(data[lineNum][i]){
                            case 0:
                                EntityHelperInmate help = new EntityHelperInmate(this, i * 50, lineNum * 50);
                                entities.add(help);
                                break;
                            case 1:
                                EntityHostileInmate thiefs = new EntityHostileInmate(this, i * 50, lineNum * 50);
                                entities.add(thiefs);
                                break;
                        }
                        
                        break;
                    case 'K':
                        System.out.println("OUTDATED LETTER CALL: K @ " + this.name);
                        break;
                    case 'k':
                        EntityPartner dudeman = new EntityPartner(this, i * 50, lineNum * 50, WorldPanel.panel.npcs.get(name), name.equals("prison"));
                        entities.add(dudeman);
                        break;
                    case 'U':
                        System.out.println("OUTDATED LETTER CALL: U @ " + this.name);
                        break;
                    case 'R':
                        System.out.println("OUTDATED LETTER CALL: R @ " + this.name);
                        break;
                    case 'h':
                        if(data[lineNum][i] == 1){
                            EntitySoldier soldier = new EntitySoldier(this, i * 50, lineNum * 50);
                            entities.add(soldier);
                        }
                        else if(data[lineNum][i] == 0){
                            EntityPolice ct = new EntityPolice(this, i * 50, lineNum * 50);
                            entities.add(ct);
                        }
                        break;
                    case 'q':
                        if(data[lineNum][i] == 0){
                            EntityResistanceSoldier ressoldier = new EntityResistanceSoldier(this, i * 50, lineNum * 50);
                            entities.add(ressoldier);
                        }
                        else if (data[lineNum][i] == 1){
                            EntityResistanceSpecops specsoldier = new EntityResistanceSpecops(this, i * 50, lineNum * 50);
                            entities.add(specsoldier);
                        }
                        
                        break;
                    case 'Q':
                        System.out.println("OUTDATED LETTER CALL: Q @ " + this.name);
                        break;
                    case 'u':
                        switch (data[lineNum][i]) {
                            case 0:
                                Entity triad = new EntityTriadSoldier(this, i * 50, lineNum * 50);
                                entities.add(triad);
                                break;
                            case 1:
                                EntityThug thug = new EntityThug(this, i * 50, lineNum * 50);
                                entities.add(thug);
                                break;
                            case 2:
                                EntityPrisonGuard dude = new EntityPrisonGuard(this, i * 50, lineNum * 50);
                                entities.add(dude);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 'N':
                        EntityNPC npc = new EntityNPC(this, i * 50, lineNum * 50, ImageRegistry.getImage(name + "/NPC.png", false).getWidth() * 3, 25, ImageRegistry.getImage(name + "/NPC.png", false).getHeight() * 3, name + "/NPC.png", WorldPanel.panel.npcs.get(name));
                        entities.add(npc);
                        break;
                    case 'n':
                        EntityNPC npc2 = new EntityNPC(this, i * 50, lineNum * 50, 36, 25, 48, name + "/NPC" + npcIndex + ".png", World.getText(name + "/" + npcIndex + ".txt"));
                        entities.add(npc2);
                        npcIndex++;
                        break;
                    case 'o':
                        EntityChahTenn tenn = new EntityChahTenn(this, i * 50, lineNum * 50, 36, 25, 48);
                        entities.add(tenn);
                        break;
                    case 'O':
                        EntityChahBoss ctq = new EntityChahBoss(this, i * 50, lineNum * 50);
                        entities.add(ctq);
                        break;
                    case 't':
                        Entity desk = new Entity(this, i * 50, lineNum * 50 - 25, 50, 40, 50, new String[]{"desk.png"});
                        entities.add(desk);
                        break;
                    case 'l':
                        Entity comp = new Entity(this, i * 50, lineNum * 50 - 25, 50, 40, 50, new String[]{"computer.png"});
                        entities.add(comp);
                        break;
                    case ':':
                        Entity weights = new Entity(this, i * 50, lineNum * 50 - 25, 50, 20, 50, new String[]{"weights.png"});
                        entities.add(weights);
                        break;
                    case 'v':
                        EntityVendor vend = null;
                        vend = new EntityVendor(this, i * 50, lineNum * 50, 36, 25, 48, new String[]{name + "/vendor" + vendorIndex + ".png"}, World.getText(name + "/vendor" + vendorIndex + ".txt"));
                        /*
                        vend = new EntityVendor(this, i * 50, lineNum * 50, 36, 25, 48, new String[]{name + "/vendor" + vendorIndex + ".png"}, new StoreItem[]{
                            new StoreItem(new ItemWeapon("Plasma Cell LMG", "plasmacannon.png", EntityBullet.PLASMA, "Lays down suppressive fire with a huge fire rate", 10, 2, 0, 20, 10, "plasma", "rifle", 75, ItemWeapon.AMMOCELL, 100, 1, 0, FireType.AUTO, Scope.NONE, false), 100),
                            new StoreItem(new ItemAmmoStash(ItemWeapon.AMMOCELL, 60), 50),
                            new StoreItem(new ItemAid("SnacPac", "snac.png", 25), 5),
                            new StoreItem(new ItemAid("SnacPac", "snac.png", 25), 5)
                        });*/
                        entities.add(vend);
                        
                        break;
                    case 'V':
                        EntityVendor vende = new EntityVendor(this, i * 50, lineNum * 50, 36, 25, 48, new String[]{"vendordrugs.png", "vendordrugs.png", "vendordrugs.png"}, new StoreItem[]{
                            new StoreItem(new ItemExenol(), 20, 5),
                            new StoreItem(new ItemAdrenaline(), 30, 2), 
                            new StoreItem(new ItemNoctaine(), 20, 1)
                        });
                        entities.add(vende);
                        
                        break;
                    case 'j':
                        
                        EntityVehicle jeep = new EntityVehicle(this, i * 50, lineNum * 50, 128, 64, 128, new Point(45, -18), new String[][]{{"jeep.png", "jeep0.png", "jeep1.png", "brokenjeep.png"}}, 500, 20, new ItemWeapon[]{
                            new ItemWeapon("Vortex Minigun", "minigun.png", EntityBullet.BULLET, "Lays down suppressive fire with a huge fire rate", 10, 1, 1, 5, 700, "bullet", "rifle", 25, ItemWeapon.THIRTY, 100, 1, 0, FireType.AUTO, Scope.NONE, true)
                            //new ItemWeapon("Five-Barelled Shotgun", "fiveshotgun.png", "Cinquen Cinquetel's signature five barelled shotgun", 10, 15, 2, 3, 10, "bullet", 20, ItemWeapon.TWELVEGAUGE, 5, 6, FireType.BOLT, Scope.NONE)
                        });
                        entities.add(jeep);
                        
                        break;
                    case ';':
                        EntityVehicle jeepy = new EntityVehicle(this, i * 50, lineNum * 50, 128, 64, 128, new Point(45, -18), new String[][]{{"jeep.png", "jeep0.png", "jeep1.png", "brokenjeep.png"}}, 50, 20, new ItemWeapon[]{
                            new ItemWeapon("Vortex Minigun", "minigun.png", EntityBullet.BULLET, "Lays down suppressive fire with a huge fire rate", 10, 1, 1, 5, 700, "bullet", "rifle", 25, ItemWeapon.THIRTY, 100, 1, 0, FireType.AUTO, Scope.NONE, true)
                            //new ItemWeapon("Five-Barelled Shotgun", "fiveshotgun.png", "Cinquen Cinquetel's signature five barelled shotgun", 10, 15, 2, 3, 10, "bullet", 20, ItemWeapon.TWELVEGAUGE, 5, 6, FireType.BOLT, Scope.NONE)
                        });
                        entities.add(jeepy);
                        EntityTriadSoldier triady = new EntityTriadSoldier(this, i * 50, lineNum * 50);
                        entities.add(triady);
                        jeepy.mount(triady);
                        break;
                    case 'A':
                        EntityVehicle powerarmor = new EntityVehicle(this, i * 50, lineNum * 50, 54, 25, 63, new Point(9, -12), new String[][]{{"powerarmor.png", "powerarmor0.png", "powerarmor1.png", "brokenpa.png"}}, 2000, 8, new ItemWeapon[]{
                            new ItemWeapon("Dual-barrelled grenade launcher", "pacannon.png", null, "Launches devastating explosives", 12, 20, 8, 1, 500, "rocket", "rifle", 25, ItemWeapon.ROCKET, 2, 1, 0, FireType.BOLT, Scope.NONE, true),
                            new ItemWeapon("Zenith Machinegun", "missileturret.png", EntityBullet.BULLET, "Has a high damage and fire rate", 20, 3, 5, 3, 700, "bullet", "rifle", 35, ItemWeapon.THIRTY, 50, 1, 0, FireType.AUTO, Scope.NONE, true)
                            //new ItemWeapon("Five-Barelled Shotgun", "fiveshotgun.png", "Cinquen Cinquetel's signature five barelled shotgun", 10, 15, 2, 3, 10, "bullet", 20, ItemWeapon.TWELVEGAUGE, 5, 6, FireType.BOLT, Scope.NONE)
                        });
                        for (int index = 0; index < 20; index++) {
                            powerarmor.inventory.add(ItemWeapon.ROCKET);
                        }
                        powerarmor.newWeapons[0].explodey = true;
                        entities.add(powerarmor);
                        break;
                    case '1':
                        Entity bill1 = new Entity(this, i * 50, lineNum * 50, 200, 25, 100, new String[]{name + "/billboard1.png"});
                        if (floating){
                            bill1.y += bill1.h;
                            bill1.topLayer = true;
                            bill1.h = 0;
                        }
                        entities.add(bill1);
                        break;
                    case '2':
                        Entity bill2 = new Entity(this, i * 50, lineNum * 50, 200, 25, 100, new String[]{name + "/billboard2.png"});
                        if (floating){
                            bill2.y += bill2.h;
                            bill2.topLayer = true;
                            bill2.h = 0;
                        }
                        entities.add(bill2);
                        break;
                    case '3':
                        Entity bill3 = new Entity(this, i * 50, lineNum * 50, 200, 25, 100, new String[]{name + "/billboard3.png"});
                        if (floating){
                            bill3.y += bill3.h;
                            bill3.topLayer = true;
                            bill3.h = 0;
                        }
                        entities.add(bill3);
                        break;
                    case '$':
                        EntityLoot loot = new EntityLoot(this, i * 50, lineNum * 50, 50, 25, 50, "drawers.png", EntityLoot.createLoot(Loot.BUNKER), (int)(Math.random() * 10), null, null);
                        entities.add(loot);
                        break;
                    case 'd':
                        EntityStolenItems stolen = new EntityStolenItems(this, i * 50, lineNum * 50, 50, 25, 50, new String[]{"drawers.png"});
                        entities.add(stolen);
                        break;
                    case '¢':
                        EntityLoot loot2 = new EntityLoot(this, i * 50, lineNum * 50, 50, 25, 50, "drawers.png", EntityLoot.createLoot(Loot.TRIAD), (int)(Math.random() * 20), null, null);
                        entities.add(loot2);
                        break;
                    case '-':
                        floating = !floating;
                        break;
                }
            }
            lineNum++;
            
        }
        height = lineNum * 50;
    }
    public static String[] getText(String file){
        ArrayList<String> messages = new ArrayList<>();
        Scanner scan = null;
        if (file.startsWith(Constants.SAVE_FILE)){
            try {
                scan = new Scanner(new File(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            scan = new Scanner(World.class.getResourceAsStream("/thecolony/resources/" + file));
        while (scan.hasNext()){
            messages.add(scan.nextLine());
        }
        return messages.toArray(new String[messages.size()]);
    }
    public void loadWorld(EntityPlayer player){
        this.player = player;
        player.dismount();
        player.world = this;
        if (!entities.contains(player)) {
            this.entities.add(player);
        }
        WorldPanel.panel.currentWorld = this.name;
        player.frozen = false;
        if (name.equals("haven market") && !playedThugsStart){
            WorldPanel.panel.addMessage(new String[]{
                "tennhead.png Good morning, Zayve.",
                "zayvehead.png Hey, Chah. What are you doing here? The Stosthor Triad controls this area, and they aren't exactly your best pals.",
                "tennhead.png I'm just expanding my cartel. But that's beside the point. Do you know what's special about today?", 
                "zayvehead.png Not really...", 
                "tennhead.png Well it's exactly A WEEK past when you were supposed to have that hacker tracked down for us.", 
                "zayvehead.png I'm sorry. I got held up. I just need a few more days.", 
                "tennhead.png I've already given you an extension. And I payed you $1000 in advance for this job. I want my money back.", 
                "zayvehead.png I... already spent it. Come on, just two more days.", 
                "tennhead.png I've given you long enough. Henchmen, take out Brock!"
            });
            
            player.frozen = true;
            final EntityPlayer play = player;
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run(){
                    EntityFighter.groups.get("chahEnemies").add(EntityPlayer.class);
                    entities.remove(chahtenn);
                    play.frozen = false;
                    playedThugsStart = true;
                }
            };
            
            
        }
        else if (name.equals("thief house")){
            WorldPanel.panel.addMessage(new String[]{"This is it! There's the thief. Be careful - it looks like he has an actual gun now.", "Use the arrow keys to switch between weapons. You can shoot with [MOUSE1] and reload with [R].", "You can use a melee attack by holding [Space] and colliding with an enemy.", "With certain guns, you can also aim with [Q]"});
        }
        else if (name.equals("house") && !playedHouse){
            playedHouse = true;
            WorldPanel.panel.nextObjective = "";
            WorldPanel.panel.addMessage(new String[]{"*Ring Ring Ring*", "*Click*", "zayvehead.png You've reached Zayve Brock QUALITY DETECTIVE SERVICES, how may I help you?", "curatorhead.png Hello. I'm Maise Curry, from the Haven Museum of History. We have a case for you.", "zayvehead.png I'll be right over.", "*Click*"});
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Meet the curator at the museum";
                }
                
            };
        }
        else if (name.equals("haven") && playPhtrip){
            playPhtrip = false;
            altedHint = true;
            WorldPanel.panel.addMessage(new String[]{
                "*Ring Ring Ring*", "*Click*", 
                "zayvehead.png You've reached Zayve Brock QUALITY DETECTIVE SERVICES, how may I help you?", 
                "normachead.png Hey, it's Normac. So I was messing with the data storage on the ship we stole, and I found something interesting. So, I was able to decrypt the manifest using a key I stole a while back...", 
                "zayvehead.png Can you just get to the point?", 
                "normachead.png The Triad has a base in Haven. And they're looking for you. They know you have information about the antimatter bomb.",
                "zayvehead.png Thanks for the heads up. I'll be ready for them.",
                "normachead.png No, you won't. They saw what you did at the prison. They'll have air support, so they can bomb you.",
                "zayvehead.png So what do I do?",
                "normachead.png The Resistance would be willing to provide you with protection. I can take you to our base, but I'll need you to help me with something first.",
                "zayvehead.png Well, I guess I don't have another option. What do you need me to do?",
                "normachead.png I need you to help me infiltrate the base. It's right outside Haven, but you'll have to find a way to get to it."
            });
            
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Find a way into the base";
                    
                }
                
            };
        }
        else if (name.equals("haven") && !playedCartelConcern){
            playedCartelConcern = true;
            WorldPanel.panel.addMessage(new String[]{"zayvehead.png Shoot... I forgot to solve that case the Tenn Cartel hired me for.", "zayvehead.png Oh well, I'm sure it's fine."});
        }
        else{
            //WorldPanel.panel.addMessage(new String[]{this.name});
        }
        
        try {
            if (WorldPanel.panel.audio != null) {
                if (!WorldPanel.panel.audio.sound.equals(music + ".wav")) {
                    WorldPanel.panel.audio.stop();
                    WorldPanel.panel.audio = new AudioPlayer(music + ".wav");
                    
                    
                    WorldPanel.panel.audio.play(-1);
                    //FloatControl gainControl = 
                        //(FloatControl) WorldPanel.panel.audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
                    //gainControl.setValue(-10.0f);
                }
            }
            else{
                WorldPanel.panel.audio = new AudioPlayer(tileset + ".wav");
                //FloatControl gainControl = 
                    //(FloatControl) WorldPanel.panel.audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
                //gainControl.setValue(-10.0f);
                WorldPanel.panel.audio.play(-1);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(WorldPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        TexturePaint paint = new TexturePaint(ImageRegistry.getImage(background, false), new Rectangle(ImageRegistry.getImage(background, false).getWidth(), ImageRegistry.getImage(background, false).getHeight()));
        BufferedImage off_Image = new BufferedImage(width / 4, height / 4,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = off_Image.createGraphics();
        g2.setPaint(paint);
        g2.fillRect(0, 0, off_Image.getWidth(), off_Image.getHeight());
        ImageRegistry.images.put(name, off_Image);
        
    }
        
    
    public void updateWorld(){
        if (name.equals("bunker") && ! EntityFighter.groups.get("chahEnemies").contains(EntityPlayer.class)){
            EntityFighter.groups.get("chahEnemies").add(EntityPlayer.class);
            
        }
        for (int i = 0; i < entities.size(); i++){
            entities.get(i).update();
            
        }
    }
    public double getXOnScreen(double absX){
        double playerX;// = player.y > WorldPanel.GAMEHEIGHT / 2 ? player.y : WorldPanel.GAMEHEIGHT / 2;
        if (width < WorldPanel.GAMEWIDTH){
            playerX = width / 2 - 18;
        }
        else if (player.x < WorldPanel.GAMEWIDTH / 2){
            playerX = WorldPanel.GAMEWIDTH / 2;
        }
        else if (player.x > width - WorldPanel.GAMEWIDTH / 2){
            playerX = width - WorldPanel.GAMEWIDTH / 2;
        }
        else{
            playerX = player.x;
        }
        double res = (absX - playerX + WorldPanel.GAMEWIDTH / 2);
        if (player.zoomed && !linear){
            double widthRatio = (double)WorldPanel.panel.getWidth() / (double)WorldPanel.GAMEWIDTH;
            double heightRatio = (double)WorldPanel.panel.getHeight() / (double)WorldPanel.GAMEHEIGHT;
            int xMouse = (int)(WorldPanel.pointerLocation.x / Math.min(widthRatio, heightRatio));
            int yMouse = (int)(WorldPanel.pointerLocation.y / Math.min(widthRatio, heightRatio));
            int xp = (WorldPanel.pointerLocation.x - WorldPanel.panel.getWidth() / 2) * player.zoomLevel;
            int yp = (WorldPanel.pointerLocation.y - WorldPanel.panel.getHeight() / 2) * player.zoomLevel;
            res -= xp;
            //tangle.y -= yp;
        }
        return res;
    }
    public double getXInWorld(double screenX){
        double widthRatio = (double)WorldPanel.panel.getWidth() / (double)WorldPanel.GAMEWIDTH;
        double heightRatio = (double)WorldPanel.panel.getHeight() / (double)WorldPanel.GAMEHEIGHT;
        screenX /= Math.min(widthRatio, heightRatio);
        
        double playerX;// = player.y > WorldPanel.GAMEHEIGHT / 2 ? player.y : WorldPanel.GAMEHEIGHT / 2;
        if (width < WorldPanel.GAMEWIDTH){
            playerX = width / 2 - 18;
        }
        else if (player.x < WorldPanel.GAMEWIDTH / 2){
            playerX = WorldPanel.GAMEWIDTH / 2;
        }
        else if (player.x > width - WorldPanel.GAMEWIDTH / 2){
            playerX = width - WorldPanel.GAMEWIDTH / 2;
        }
        else{
            playerX = player.x;
        }
        double res = playerX - WorldPanel.GAMEWIDTH / 2 + screenX; 
        if (player.zoomed && !linear){
            int xp = (WorldPanel.pointerLocation.x - WorldPanel.panel.getWidth() / 2)  * player.zoomLevel;
            int yp = (WorldPanel.pointerLocation.y - WorldPanel.panel.getHeight() / 2)  * player.zoomLevel;
            res += xp;
            //tangle.y -= yp;
        }
        return res;
    }
    
    public double getYOnScreen(double absY){
        
        double playerY;// = player.y > WorldPanel.GAMEHEIGHT / 2 ? player.y : WorldPanel.GAMEHEIGHT / 2;
        if (height < WorldPanel.GAMEHEIGHT){
            playerY = height / 2;
        }
        else if (player.y < WorldPanel.GAMEHEIGHT / 2){
            playerY = WorldPanel.GAMEHEIGHT / 2;
        }
        else if (player.y > height - WorldPanel.GAMEHEIGHT / 2){
            playerY = height - WorldPanel.GAMEHEIGHT / 2;
        }
        else{
            playerY = player.y;
        }
        if (linear){
            playerY -= (WorldPanel.GAMEHEIGHT / 2) - 100;
        }
        double res = absY - playerY + WorldPanel.GAMEHEIGHT / 2;
        if (player.zoomed && !linear){
            int xp = (WorldPanel.pointerLocation.x - WorldPanel.panel.getWidth() / 2)  * player.zoomLevel;
            int yp = (WorldPanel.pointerLocation.y - WorldPanel.panel.getHeight() / 2)  * player.zoomLevel;
            //tangle.x -= xp;
            res -= yp;
        }
        return res;
    }
    public double getYInWorld(double screenY){
        double widthRatio = (double)WorldPanel.panel.getWidth() / (double)WorldPanel.GAMEWIDTH;
        double heightRatio = (double)WorldPanel.panel.getHeight() / (double)WorldPanel.GAMEHEIGHT;
        screenY /= Math.min(widthRatio, heightRatio);
        
        double playerY;// = player.y > WorldPanel.GAMEHEIGHT / 2 ? player.y : WorldPanel.GAMEHEIGHT / 2;
        if (height < WorldPanel.GAMEHEIGHT){
            playerY = height / 2;
        }
        else if (player.y < WorldPanel.GAMEHEIGHT / 2){
            playerY = WorldPanel.GAMEHEIGHT / 2;
        }
        else if (player.y > height - WorldPanel.GAMEHEIGHT / 2){
            playerY = height - WorldPanel.GAMEHEIGHT / 2;
        }
        else{
            playerY = (player.y + player.h) - (player.height / 2);
        }
        if (linear){
            playerY -= (WorldPanel.GAMEHEIGHT / 2) - 100;
        }
        double res = playerY - WorldPanel.GAMEHEIGHT / 2 + screenY; 
        if (player.zoomed && !linear){
            int xp = (WorldPanel.pointerLocation.x - WorldPanel.panel.getWidth() / 2)  * player.zoomLevel;
            int yp = (WorldPanel.pointerLocation.y - WorldPanel.panel.getHeight() / 2)  * player.zoomLevel;
            res += yp;
            //tangle.y -= yp;
        }
        return res;
    }
    public void paintAll(Graphics g, int w, int h){
        if (linear){
            g.drawImage(ImageRegistry.getImage(path + "/background.png", false), (int)getXOnScreen(0) / 4, 0, w + ((width - w) / 4), h, null);
            String midgroundImg = "midground";
            if (WorldPanel.panel.boomTimer >= 20){
                midgroundImg += (int)((((WorldPanel.panel.boomTimer - 20) % 10) / 2)) + "";
            }
            g.drawImage(ImageRegistry.getImage(path + "/" + midgroundImg + ".png", false), (int)getXOnScreen(0) / 2, 0, w + ((width - w) / 2), h, null);
        
        }
        
       int xtrans = (int) player.x;
       /*
       if (xtrans < w / 2){
           xtrans = w / 2;
       }*/
       int ytrans = (int) player.y;
       /*
       if (ytrans < h / 2){
           ytrans = h / 2;
       }*/
       BufferedImage image = null;
       if (ImageRegistry.images.containsKey(name))
           image = ImageRegistry.getImage(name, false);
       if (width != 0 && height != 0){
           if (image == null){
               TexturePaint paint = new TexturePaint(ImageRegistry.getImage(background, false), new Rectangle(ImageRegistry.getImage(background, false).getWidth(), ImageRegistry.getImage(background, false).getHeight()));
                BufferedImage off_Image = new BufferedImage(width / 4, height / 4,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = off_Image.createGraphics();
                g2.setPaint(paint);
                g2.fillRect(0, 0, off_Image.getWidth(), off_Image.getHeight());
                ImageRegistry.images.put(name, off_Image);
                image = ImageRegistry.getImage(name, false);
           }
           g.drawImage(image, (int)getXOnScreen(0), (int)getYOnScreen(25), width, height - 50, null);
       }
       
       /*
       if (player.zoomed){
           g.drawImage(image, -xtrans + w / 2 - (WorldPanel.pointerLocation.x - w / 2), -ytrans + h / 2 -(WorldPanel.pointerLocation.y - h / 2), width, height, null);
       }
       else{
           g.drawImage(image, -xtrans + w / 2, -ytrans + h / 2, width, height, null);
       }*/
        
        for (int j = 0; j <= height; j++){
            for (int i = 0; i < entities.size(); i++){
                if ((entities.get(i).y == j && entities.get(i).h != 0) || (j == 0 && entities.get(i).h == 0 && !entities.get(i).topLayer) || (j == height  && entities.get(i).h == 0 && entities.get(i).topLayer)){
                    if (entities.get(i) instanceof EntityVehicle){
                        EntityVehicle v = (EntityVehicle) entities.get(i);
                        if (v.rider != null){
                            continue;
                        }
                    }
                    entities.get(i).drawEntity(g, xtrans, ytrans, w, h);
                }
            }
        }
    }
    public void unload(){
        
        this.entities.remove(player);
        ImageRegistry.images.remove(name);
        
    }
    public void unlock(){
        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i) instanceof EntityGate){
                ((EntityGate)entities.get(i)).locked = false;
                entities.get(i).texture = new String[]{"portal.png"};
            }
        
        }
    }
    public void unlock(int index){
        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i) instanceof EntityGate){
                if (index == 0)
                {
                    ((EntityGate)entities.get(i)).locked = false;
                    entities.get(i).texture = new String[]{"portal.png"};
                    return;
                }
                
                index--;
            }
        
        }
    }
    public void lock(){
        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i) instanceof EntityGate){
                ((EntityGate)entities.get(i)).locked = true;
                entities.get(i).texture = new String[]{"locked.png"};
            }
        }
    }
    public void clear(){
        if (cleared){
            return;
        }
        boolean clear = true;
        for (int i = 0; i < this.entities.size(); i++){
            if (EntityFighter.groups.get(player.enemyGroup).contains(entities.get(i).getClass()) && !(entities.get(i) instanceof EntityMichaelChuck)){
                clear = false;
                break;
            }
        }
        if (clear){
            cleared = true;
            if (name.equals("haven market")){
                WorldPanel.panel.addMessage(new String[]{"zayvehead.png Chah Tenn got away... No doubt he'll keep being a problem.", "zayvehead.png Looks like I'm going to have to deal with him."});
                WorldPanel.panel.runOnText = new Runnable(){
                    @Override
                    public void run() {
                        WorldPanel.panel.nextObjective = "Search for clues to Chah Tenn's whereabouts";
                    }
                    
                };
                //WorldPanel.panel.saveGame();
            }
            if (name.equals("basehangar")){
                //1200;
                Entity frigate = new Entity(this, 1200, 50, 100, 25, 100, new String[]{"fighter.png"});
                entities.add(frigate);
                EntityNPC npc = new EntityNPC(this, 1200, 250, ImageRegistry.getImage(name + "/NPC.png", false).getWidth() * 3, 25, ImageRegistry.getImage(name + "/NPC.png", false).getHeight() * 3, name + "/NPC.png", WorldPanel.npcs.get(name));
                entities.add(npc);
                WorldPanel.panel.nextObjective = "Board the ship";
                
                //unlock();
            }
            if (name.equals("hangar")){
                WorldPanel.panel.nextObjective = "Board Normac's gunship";
                unlock();
            }
        }
    }
    public boolean blocked(int tx, int ty, Entity e){
        boolean valid = false;
        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i).getBounds().intersects(new Rectangle(tx * 50, (ty * 50) - 25, 50, 50)) && entities.get(i) != e && !entities.get(i).getBounds().intersects(e.getBounds()) && entities.get(i) != player){
                
                valid = true;
                break;
            }
        }
        return valid;
    }
}

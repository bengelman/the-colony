/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony;

import java.awt.Color;
import thecolony.entity.EntityLoot;
import thecolony.entity.EntityPlayer;
import thecolony.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import thecolony.items.ItemWeapon.Scope;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityVendor;
import thecolony.entity.behavior.AIPlayer;
import thecolony.gui.OptionMenu;
import thecolony.items.chips.ItemQuickdrawChip;
import thecolony.items.chips.ItemScopeChip;

/**
 *
 * @author pdogmuncher
 */
public class WorldPanel extends JPanel implements ActionListener{
    public static final Color DARK = new Color(0.11f, 0.11f, 0.11f, 1.0f);
    int startx = 150, starty = 150;//starty = 150;
    public static final int TICKS_PER_SECOND = 20;
    public TreeMap<String, World> currentWorlds = new TreeMap();
    public String currentWorld;
    public Timer gameloop;
    public ArrayList<String> currentTextQueue = new ArrayList<>();
    char lastDisplayChar = 'a';
    private int animTimer = 0;
    private int animSwitchRate = 0;
    private String animName = null;
    private Runnable runOnAnim = null;
    public EntityPlayer player;
    public int textTimer = 0;
    public int wordTimer = 0;
    public World haven;
    int boomTimer = 1;
    public ArrayList<BufferedImage> gifImages = new ArrayList<>();
    public String nextObjective = "Talk to the store manager";
    public static WorldPanel panel;
    //final String FIRSTWORLD = "secretbaseoffice";//"convenience store";
    //public boolean paused = false;
    private boolean needToShow = false;
    public static int GAMEWIDTH = 1400, GAMEHEIGHT = 700;
    public String result = null;
    public Runnable runOnText = null;
    public Runnable runNextTick = null;
    public GUI currentGUI = null;
    public static Point pointerLocation = new Point(0, 0);
    public static final Font bitFont = new Font("Atari Font Full Version", 0, 20);
    public static final Font fallbackFont = new Font("Impact", 0, 20);
    public int exenolTimer = 0;
    public int totalTime = 0;
    public String[] worlds;
    String missionName;
    public static String[] storyWorlds = new String[]{
        
        
        //"havenpath",
        "convenience store",
        "secretbaseoffice",
        //"outsideprison",
        //"hangar",
        //"bunker",
        //"city",
        //"museum",
        //"landingzone",
        "house",
        "triadoffices",
        "haven",
        "havenpath",
        "museum",
        "museum2",
        "landingzone",
        "baseoutside",
        "skyscraper1",
        "skyscraper2",
        "vents",
        "cell",
        "prisonvents",
        "prisoncontrol",
        "base",
        "prison",
        "hangar",
        "haven market",
        "cannon1",
        "prison2",
        "outsideprison",
        "ubermart",
        "machos nachos",
        "bunker",
        "haven suburbs",
        "jmishtin house",
        "tennfight",
        "secretbaseoutside",
        "secretbase",
        "toys r ok",
        "thief house",
        //"secretbaseoffice",
        "basehangar",
        "househaven2",
        "city"
    };
    public static TreeMap <String, String[]> journals = new TreeMap<>();
    public static TreeMap <String, String[]> signs = new TreeMap<>();
    public static TreeMap <String, String[]> npcs = new TreeMap<>();
    public AudioPlayer audio;
    public WorldPanel(String[] worlds, String missionName){
        WorldPanel.panel = this;
        this.worlds = worlds;
        this.missionName = missionName;
        loadJournals();
        loadSigns();
        loadNpcs();
        //loadWorld();
        
        
        gameloop = new Timer(0, this);
        gameloop.setDelay(1000 / TICKS_PER_SECOND);
        gameloop.setRepeats(true);
        gameloop.start();
        repaint();
        this.setLayout(null);
        this.setFocusable(true);
        //this.requestFocusInWindow();
    }
    public void loadJournals(){
        Scanner scan = new Scanner(this.getClass().getResourceAsStream("/thecolony/resources/journals.txt"));
        while (scan.hasNext()){
            String key = scan.nextLine();
            ArrayList<String> text = new ArrayList<>();
            String texts = scan.nextLine();
            while(!texts.equals("xxx")){
                text.add(texts);
                texts = scan.nextLine();
            }
            journals.put(key, text.toArray(new String[text.size()]));
        }
    }
    public void loadSigns(){
        Scanner scan = new Scanner(this.getClass().getResourceAsStream("/thecolony/resources/signs.txt"));
        while (scan.hasNext()){
            String key = scan.nextLine();
            ArrayList<String> text = new ArrayList<>();
            String texts = scan.nextLine();
            while(!texts.equals("xxx")){
                text.add(texts);
                texts = scan.nextLine();
            }
            signs.put(key, text.toArray(new String[text.size()]));
        }
    }
    public void loadNpcs(){
        Scanner scan = new Scanner(this.getClass().getResourceAsStream("/thecolony/resources/npcs.txt"));
        while (scan.hasNext()){
            String key = scan.nextLine();
            ArrayList<String> text = new ArrayList<>();
            String texts = scan.nextLine();
            while(!texts.equals("xxx")){
                text.add(texts);
                texts = scan.nextLine();
            }
            npcs.put(key, text.toArray(new String[text.size()]));
        }
    }
    int pauseTicks = 0;
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
        if (e.getSource() == gameloop){
            if (!Main.fullscreen){
                if (Main.device.getFullScreenWindow() != null){
                    JFrame w = (JFrame) Main.device.getFullScreenWindow();
                    Main.device.setFullScreenWindow(null);
                    w.setSize(Main.fullX, Main.fullY);
                }
            }
            if (needToShow && totalTime > 1){
                needToShow = false;
                //WorldPanel.panel.showImage(new String[]{"picut1.png", "picut2.png", "picut3.png", "picut4.png", "picut5.png", "picut6.png", "picut7.png", "tips.png"});
                audio = new AudioPlayer("haven.wav");
                //FloatControl gainControl = 
                    //(FloatControl) audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
                //gainControl.setValue(-10.0f);
                audio.play(-1);
            }
            //this.requestFocusInWindow();
            pointerLocation = new Point(MouseInfo.getPointerInfo().getLocation());
            if (this.isShowing()){
                pointerLocation.translate(- this.getLocationOnScreen().x, - this.getLocationOnScreen().y);
            }
            double widthRatio = (double)getWidth() / (double)GAMEWIDTH;
            double heightRatio = (double)getHeight() / (double)GAMEHEIGHT;
            if (widthRatio > heightRatio){
                //pointerLocation = new Point((int)(pointerLocation.x / heightRatio), (int)(pointerLocation.y / heightRatio));
                pointerLocation.translate((int)((-((getWidth() - (GAMEWIDTH * heightRatio)) / 2))/* / heightRatio*/), 0);
            }
            else{
                //pointerLocation = new Point((int)(pointerLocation.x / widthRatio), (int)(pointerLocation.y / widthRatio));
                //System.out.println("Shifting " + (-((getHeight() - (int)(GAMEHEIGHT * widthRatio)) / 2)));
                pointerLocation.translate(0, (int)((-((getHeight() - (GAMEHEIGHT * widthRatio)) / 2))/* / widthRatio*/));
                
            }
            
            
            if (animTimer > 0){
                
                animTimer--;
                if (animTimer == 0 && runOnAnim != null){
                    runOnAnim.run();
                    runOnAnim = null;
                    this.currentGUI = null;
                }
                else if (animTimer == 0){
                    this.currentGUI = null;
                }
                else{
                    int index = (animTimer) / animSwitchRate;
                    GUIImage image = new GUIImage(new String[]{"animations/" + animName + index + ".png"}, false, false);
                    this.currentGUI = image;
                }
                
            }
            if (currentGUI != null){
                currentGUI.onUpdate();
                repaint();
            }
            else{
                if (runNextTick != null){
                    runNextTick.run();
                    runNextTick = null;
                }
                
                totalTime++;
                if (boomTimer > 1){
                    boomTimer--;
                }
                if (exenolTimer > 0){
                    exenolTimer--;
                    if (exenolTimer == 0){
                        gameloop.setDelay(1000 / TICKS_PER_SECOND);
                    }
                }
                if (textTimer > 0){
                    textTimer--;
                }
                pauseTicks++;
                switch(lastDisplayChar){
                    case ',':
                    case '!':
                    case '?':
                    case '.':
                        if (wordTimer > 0 && pauseTicks > 3){
                            pauseTicks = 0;
                            wordTimer--;
                        }
                        break;
                    default:
                        if (wordTimer > 0){
                            pauseTicks = 0;
                            wordTimer--;
                        }
                }
                
                if (textTimer == 0 && !currentTextQueue.isEmpty()){
                    currentTextQueue.remove(0);
                    textTimer = TICKS_PER_SECOND * 40;
                    if (currentTextQueue.size() > 0){
                        wordTimer = currentTextQueue.get(0).length();
                        if (currentTextQueue.get(0).equals("*BOOM*")){
                            //player.world.midPath = "explode";
                            boomTimer = 30;
                        }
                        
                    }
                    if (runOnText != null && currentTextQueue.isEmpty()){
                        runOnText.run();
                        runOnText = null;
                    }
                }
                currentWorlds.get(currentWorld).updateWorld();
                repaint();
            }
            
            
        }
        }catch (OutOfMemoryError er){
            ImageRegistry.images.clear();
            Main.currentFrame.setVisible(false);
            
            
            JOptionPane.showMessageDialog(this, "Out of Memory Error - Consider Allocating Java More Memory");
            System.exit(0);
        }
    }
    @Override
    public void paintComponent(Graphics ga){
        super.paintComponent(ga);
        ga.setColor(Color.BLACK);
        ga.fillRect(0, 0, getWidth(), getHeight());
        BufferedImage buf = new BufferedImage(GAMEWIDTH, GAMEHEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, buf.getWidth(), buf.getHeight());
        
        BufferedImage worldImg = new BufferedImage(GAMEWIDTH, GAMEHEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = worldImg.createGraphics();
        super.paintComponent(g2);
        g2.setColor(Color.BLACK);
        int xalt = ((boomTimer % 3) - 1) * 10;
        g2.fillRect(0, 0, GAMEWIDTH, GAMEHEIGHT);
        currentWorlds.get(currentWorld).paintAll(g2, GAMEWIDTH, GAMEHEIGHT);
        g.drawImage(worldImg, xalt, 0, this);
        g.setFont(bitFont);
        
        if ((player.weapon.scope == Scope.DIGITAL && player.zoomed) || player.chip instanceof ItemScopeChip){
            Point ev = new Point((int)player.world.getXOnScreen(player.world.getXInWorld(WorldPanel.pointerLocation.x)), (int)player.world.getYOnScreen(player.world.getYInWorld(WorldPanel.pointerLocation.y)));
            int width = (int)this.currentWorlds.get(currentWorld).getXOnScreen(player.x) + 18;//getWidth() / 2 + 18;
            int height = (int)this.currentWorlds.get(currentWorld).getYOnScreen(player.y);//getHeight() / 2;
            
            double angle = Math.atan((double)(ev.y - height) / (double)(ev.x - width));
            
            if ((ev.x - width) < 0){
                angle += Math.PI;
            }
            double angle2 = angle;
            angle += Math.toRadians(player.weapon.currentRecoil );
            angle2 -= Math.toRadians(player.weapon.currentRecoil);
            int x1 = (int)(Math.cos(angle) * (double)((double)player.weapon.range));
            int x2 = (int)(Math.cos(angle2) * (double)((double)player.weapon.range));
            int y1 = (int)(Math.sin(angle) * (double)((double)player.weapon.range));
            int y2 = (int)(Math.sin(angle2) * (double)((double)player.weapon.range));
            g.setColor(new Color(0, 1, 1, 0.5f));
            g.fillPolygon(new int[]{width, width + x1, width + x2}, new int[]{height, height + y1, height + y2}, 3);
            if (player.weapon.currentRecoil == 0){
                g.drawLine(width, height, width + x1, height + y1);
            }
            
        }
        
        g.setColor(Color.GREEN);
        if (player.weapon != null){
            //g.setColor(new Color(1.0f, 128.0f / 255.0f, 0.0f, 1.0f));
            g.setColor(Color.WHITE);
            g.fillRoundRect(GAMEWIDTH - 100, GAMEHEIGHT - 100, 100, 100, 10, 10);
            g.drawImage(player.weapon.getImage(player, false), GAMEWIDTH - 100, GAMEHEIGHT - 100, 100, 100, this);
            g.setColor(Color.GREEN);
            if (player.weapon.bulletTexture != null && player.weapon.bulletTexture.equals("laser")){
                g.drawString(Main.createFallbackString((int)(((double)player.weapon.currentAmmo / (double)player.weapon.capacity) * 100D) + "%", bitFont, fallbackFont).getIterator(), GAMEWIDTH - 75, GAMEHEIGHT - 50);
            }
            else if (!player.weapon.isMelee){
                g.drawString(Main.createFallbackString(player.weapon.currentAmmo + "", bitFont, fallbackFont).getIterator(), GAMEWIDTH - 50, GAMEHEIGHT - 50);
                if (player.weapon.ammo != null) {
                    g.drawString(Main.createFallbackString(player.getQuantityOf(player.weapon.ammo.name) + "", bitFont, fallbackFont).getIterator(), GAMEWIDTH - 50, GAMEHEIGHT - 25);
                }
            }
            
        }
        
        
        if (!currentTextQueue.isEmpty()){
            g.setColor(Color.BLACK);
            String[] words = currentTextQueue.get(0).split(" ");
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> displayLines = new ArrayList<>();
            int xoffset = words[0].endsWith(".png") ? 110 : 0;
            
            for (int i = words[0].endsWith(".png") ? 1 : 0; i < words.length; i++){
                if (lines.isEmpty()){
                    lines.add(words[i]);
                    continue;
                }
                if (getStringWidth(g, lines.get(lines.size() - 1) + " " + words[i]) > GAMEWIDTH - xoffset){
                    lines.add(words[i]);
                }
                else{
                    lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " " + words[i]);
                }
            }
            int added = 0;
            int max = currentTextQueue.get(0).length() - wordTimer;
            for (int i = 0; i < lines.size() && added < max; i++){
                if (lines.get(i).length() + added > max){
                    displayLines.add(lines.get(i).substring(0, max - added));
                    added = max;
                    
                    //System.out.println("Adding " + lines.get(i).substring(0, max - added));
                }
                else{
                    added = lines.get(i).length();
                    displayLines.add(lines.get(i));
                }
            }
            int width = lines.size() <= 1 ? getStringWidth(g, lines.get(0)) + xoffset : GAMEWIDTH - 20;
            int heightamt = lines.size() * (getStringHeight(g, "Test") + 10);
            g.setColor(DARK);
            g.fillRoundRect((GAMEWIDTH / 2) - (width / 2) - 10, GAMEHEIGHT - 210, width + 20, heightamt > xoffset ? heightamt : xoffset, 10, 10);
            g.setColor(Color.WHITE);
            g.drawRoundRect((GAMEWIDTH / 2) - (width / 2) - 10, GAMEHEIGHT - 210, width + 20, heightamt > xoffset ? heightamt : xoffset, 10, 10);
            
            
            if (currentTextQueue.get(0).startsWith("zayvehead.png")){
                g.setColor(Color.WHITE);
                //g.drawRoundRect((getWidth() / 2) - (width / 2) - 7, getHeight() - 207, 104, 104, 10, 10);
                g.drawImage(ImageRegistry.getImage(player.texture[0].equals("hero.png") ? "portraits/zayvehead.png" : "zayveheadfem.png", false), (GAMEWIDTH / 2) - (width / 2) - 5, GAMEHEIGHT - 205, 100, 100, this);
            }
            else if (words[0].endsWith(".png")){
                g.setColor(Color.WHITE);
                //g.drawRoundRect((getWidth() / 2) - (width / 2) - 7, getHeight() - 207, 104, 104, 10, 10);
                g.drawImage(ImageRegistry.getImage((words[0].contains("/") ? "" : "portraits/") + words[0], false), (GAMEWIDTH / 2) - (width / 2) - 5, GAMEHEIGHT - 205, 100, 100, this);
            }
            
            g.setColor(Color.WHITE);
            for (int j = 0; j < displayLines.size(); j++){
                int yh = GAMEHEIGHT - 190 + (j * (getStringHeight(g, "Test") + 10));
                g.drawString(Main.createFallbackString(displayLines.get(j), bitFont, fallbackFont).getIterator(), xoffset + (lines.size() > 1 ? 10 : (GAMEWIDTH / 2) - ((xoffset + getStringWidth(g, lines.get(j))) / 2)), yh);
            }
            if (!displayLines.isEmpty())
                if (displayLines.get(displayLines.size() - 1).length() > 0)
                    lastDisplayChar = displayLines.get(displayLines.size() - 1).charAt(displayLines.get(displayLines.size() - 1).length() - 1);
        }
        
        g.setColor(new Color(1.0f, 128.0f / 255.0f, 0.0f, 1.0f));
        g.fillRoundRect(100, 0, GAMEWIDTH - 200, 30, 20, 20);
        g.setColor(Color.RED);
        g.fillRoundRect(100, 0, (int)(((float)player.maxHealth / (float)player.nodrugsHp) * (GAMEWIDTH - 200)), 30, 20, 20);
        g.setColor(Color.GREEN);
        g.fillRoundRect(100, 0, (int)(((float)player.health / (float)player.nodrugsHp) * (GAMEWIDTH - 200)), 30, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(100, 0, GAMEWIDTH - 200, 30, 20, 20);
        g.setColor(new Color(0, 0, 0, 0.25f));
        if (player.riding != null){
            g.setColor(Color.RED);
            g.fillRoundRect(100, 30, (int)((GAMEWIDTH - 200)), 30, 20, 20);
            g.setColor(Color.GREEN);
            g.fillRoundRect(100, 30, (int)(((float)player.riding.health / player.riding.maxHealth) * (GAMEWIDTH - 200)), 30, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRoundRect(100, 30, GAMEWIDTH - 200, 30, 20, 20);
            g.setColor(new Color(0, 0, 0, 0.25f));
        }
        
        if (nextObjective != null){
            if (getStringWidth(g, "Current Objective: " + nextObjective) + 20 > GAMEWIDTH){
                
                String[] words = nextObjective.split(" ");
                ArrayList<String> lines = new ArrayList<>();
                for (int i = 0; i < words.length; i++){
                    if (lines.isEmpty()){
                        lines.add(words[i]);
                        continue;
                    }
                    if (getStringWidth(g, lines.get(lines.size() - 1) + " " + words[i]) + 20 > GAMEWIDTH){
                        lines.add(words[i]);
                    }
                    else{
                        lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " " + words[i]);
                    }
                }
                int yha = 30 + ((lines.size() - 1) * (getStringHeight(g, "Test") + 10)) + 20;
                g.fillRoundRect(0, 30, GAMEWIDTH, yha, 10, 10);
                g.setColor(Color.WHITE);
                for (int j = 0; j < lines.size(); j++){
                    int yh = (40 + getStringHeight(g, "Test") / 2) + (j * (getStringHeight(g, "Test") + 10));
                    g.drawString(Main.createFallbackString(lines.get(j), bitFont, fallbackFont).getIterator(), 10 + (lines.size() > 1 ? 10 : (GAMEWIDTH / 2) - ((10 + getStringWidth(g, lines.get(j))) / 2)), yh);
                }
                
            }
            else{
                g.fillRoundRect(0, 30, getStringWidth(g, "Current Objective: " + nextObjective) + 20, getStringHeight(g, "Test") + 20, 10, 10);
                g.setColor(Color.WHITE);
                g.drawString(Main.createFallbackString("Current Objective: " + nextObjective, bitFont, fallbackFont).getIterator(), 10, 40 + getStringHeight(g, "Test") / 2);
                
            }
            
        }
            
        
        
        if (gameloop.getDelay() > 1000 / WorldPanel.TICKS_PER_SECOND){
            g.drawImage(ImageRegistry.getImage("drugtrip.png", false), 0, 0, GAMEWIDTH, GAMEHEIGHT, this);
        }
        if (player.noctaineTimer > 0){
            g.drawImage(ImageRegistry.getImage("blackedout.png", false), 0, 0, GAMEWIDTH, GAMEHEIGHT, this);
        }
        /*if (animTimer > 0){
            int index = (animTimer) / animSwitchRate;
            g.drawImage(ImageRegistry.getImage("animations/" + animName + index + ".png", false), 380, 30, 640, 640, this);
        }*/
        if (currentGUI != null){
            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(0, 0, GAMEWIDTH, GAMEHEIGHT);
            currentGUI.draw(g);
        }
        double widthRatio = (double)getWidth() / (double)buf.getWidth();
        double heightRatio = (double)getHeight() / (double)buf.getHeight();
        if (widthRatio > heightRatio){
            ga.drawImage(buf, (getWidth() - (int)(buf.getWidth() * heightRatio)) / 2, 0, (int)(buf.getWidth() * heightRatio), (int)(buf.getHeight() * heightRatio), this);
        }
        else{
            ga.drawImage(buf, 0, (getHeight() - (int)(buf.getHeight() * widthRatio)) / 2, (int)(buf.getWidth() * widthRatio), (int)(buf.getHeight() * widthRatio), this);
        }
        //ga.drawImage(buf, 0, 0, this);
        if (player.takingGif){
            /*
            Image tmp = buf.getScaledInstance(buf.getWidth() / 2, buf.getHeight() / 2, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(buf.getWidth() / 2, buf.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();*/
            gifImages.add(buf);
        }
        else if (!gifImages.isEmpty() && !player.takingGif){
            g.setFont(new Font("Atari Font Full Version", 1, 60));
            g.setColor(Color.WHITE);
            //g.drawString("Creating GIF...", getWidth() / 2 - (getStringWidth(g, "Creating GIF...") / 2), getHeight() / 2);
            final ArrayList<BufferedImage> gifImages2 = new ArrayList<>(gifImages);
            gifImages.clear();
            new Thread(new Runnable() {
                public void run() {
                    int i = 0;
                    while (new File(Constants.SAVE_FILE + "gif" + i + ".gif").exists()){
                        i++;
                    }
                    ImageOutputStream output = null;
                    GifSequenceWriter writer = null;
                    try{
                        output = new FileImageOutputStream(new File(Constants.SAVE_FILE + "gif" + i + ".gif"));

                        // create a gif sequence with the type of the first image, 1 second
                        // between frames, which loops continuously

                        writer = new GifSequenceWriter(output, gifImages2.get(0).getType(), (1000 / WorldPanel.TICKS_PER_SECOND), true);

                        // write out the first image to our sequence...
                        for(int iff=0; iff<gifImages2.size(); iff++) {

                          writer.writeToSequence(gifImages2.get(iff));
                        }
                        writer.close();
                        output.close();

                    }
                    catch (Exception exe){exe.printStackTrace();}
                    finally{


                    }			
                }
            }).start();
            // create a new BufferedOutputStream with the last argument
        }
    }
    public static int getStringWidth(Graphics g, String s){
        return (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
    }
    public static int getStringHeight(Graphics g, String s){
        return (int)g.getFontMetrics().getStringBounds(s, g).getHeight();
    }
    public void loadWorld(){
        
        if (!new File(Constants.SAVE_FILE + missionName + ".save").exists()){
            loadNewGame();
            return;
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(Constants.SAVE_FILE + missionName + ".save"))));
            
            currentWorlds = (TreeMap)in.readObject();
            currentWorld = (String)in.readObject();
            nextObjective = in.readUTF();
            for (Map.Entry<String,ArrayList<Class>> entry : EntityFighter.groups.entrySet()){
                String key = entry.getKey();
                EntityFighter.groups.put(key,(ArrayList) in.readObject());
            }
            player = currentWorlds.get(currentWorld).player;
            ((AIPlayer)player.ai).initKeys();
            player.frozen = false;
            if (currentWorld.equals("haven market")){
                if (!currentWorlds.get(currentWorld).playedThugsStart){
                    currentWorlds.get(currentWorld).loadWorld(player);
                    audio.stop();
                }
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(WorldPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            audio = new AudioPlayer(currentWorlds.get(currentWorld).music + ".wav");
            
            audio.play(-1);
            //FloatControl gainControl = 
                //(FloatControl) audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
            //gainControl.setValue(-10.0f);
        } catch (NullPointerException ex) {
            Logger.getLogger(WorldPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadNewGame(){
        EntityFighter.reinitClasses();
        for (int i = 0; i < worlds.length; i++){
            InputStream in = null;
            InputStream data = null;
            if (worlds[i].startsWith(Constants.SAVE_FILE)){
                try {
                    in = new FileInputStream(new File(worlds[i] + ".map"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(WorldPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    in = new FileInputStream(new File(worlds[i] + "_data.map"));
                } catch (FileNotFoundException ex) {
                    in = null;
                }
            }
            else{
                in = this.getClass().getResourceAsStream("/thecolony/resources/worlds/" + worlds[i] + ".map");
            }
            World w = new World(in, World.parseStream(data), worlds[i]);
            currentWorlds.put(worlds[i], w);
        }
        player = new EntityPlayer(startx, starty, 36, 25, 48);
        if (Constants.SKIP)
            currentWorlds.get(worlds[1]).loadWorld(player);
        else
            currentWorlds.get(worlds[0]).loadWorld(player);
        
        audio.stop();
        needToShow = true;
        
        this.addMessage(new String[]{"Welcome! This tutorial will run you through the game's basics and controls. Press [ENTER] to close this box when you finish reading.", "The door to your right is locked. Locked doors stay locked until necessary events in the story are triggered.", "Use [WASD] to move around.", "You can right click on objects in the environment to interact with them. Try interacting with the NPC over there."});
        
    }
    
    public void saveGame(){
        ObjectOutputStream out = null;
        try {
            if (!new File(Constants.SAVE_FILE).exists())
                new File(Constants.SAVE_FILE).mkdirs();
            File file = new File(Constants.SAVE_FILE + missionName + ".save");
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(Constants.SAVE_FILE + missionName + ".save"), false)));
            out.writeObject(currentWorlds);
            out.writeObject(currentWorld);
            out.writeUTF(nextObjective);
            for (Map.Entry<String,ArrayList<Class>> entry : EntityFighter.groups.entrySet()){
                out.writeObject(entry.getValue());
            }
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showDialogue(String[] info, boolean unPauses){
        GUIText text = new GUIText(info);
        this.currentGUI = text;
        repaint();
    }
    public void showImage(String[] info){
        GUIImage image = new GUIImage(info, info[0].equals("cutportal.png"), true);
        this.currentGUI = image;
    }
    public void showMenu(){
        GUIPause pause = new GUIPause();
        this.currentGUI = pause;
    }
    public static String showStaticOptionPane(final String[] options, final String question){
        Main.device.setFullScreenWindow(null);
        JDialog frame = new JDialog(Main.currentFrame, null, true);
        
        OptionMenu menu = new OptionMenu(frame, null, question, options);
        frame.add(menu);
        frame.setUndecorated(Main.fullscreen);
        frame.setSize(Main.currentFrame.getWidth(), Main.currentFrame.getHeight());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        frame.setVisible(true);
        if (Main.fullscreen)
            Main.device.setFullScreenWindow(Main.currentFrame);
        return menu.result;
    }
    public void showOptionPane(final String[] options, final String question, Runnable[] runnables){
        GUIOptionMenu menu = new GUIOptionMenu(question, options, runnables);
        this.currentGUI = menu;
        
    }
    public void openInventory(){
        GUIInventory gui = new GUIInventory(player.inventory);
        this.currentGUI = gui;
    }
    public void openInventory(EntityLoot looter){
        GUILootInventory gui = new GUILootInventory(looter);
        this.currentGUI = gui;
    }
    public void showStore(EntityVendor looter){
        GUIStoreInventory gui = new GUIStoreInventory(looter);
        currentGUI = gui;
    }
    public void addMessage(String[] message){
        this.textTimer = TICKS_PER_SECOND * 40;
        this.wordTimer = message[0].length();
        this.currentTextQueue.clear();
         for (int i = 0; i < message.length; i++) {
            this.currentTextQueue.add(message[i]);
        }
    }  
    static public String exportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = WorldPanel.class.getResourceAsStream("/thecolony/resources/" + resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            
            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(new File(resourceName));
            
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }
    public void addAnim(String texture, int time, int switchRate, Runnable action){
        this.animName = texture;
        this.animTimer = time - 1;
        this.animSwitchRate = switchRate;
        this.runOnAnim = action;
    }
    public boolean click(Point p){
        
        double widthRatio = (double)getWidth() / (double)GAMEWIDTH;
        double heightRatio = (double)getHeight() / (double)GAMEHEIGHT;
        int x = p.x, y = p.y;
        
        if (widthRatio > heightRatio){
            x /= heightRatio;
            y /= heightRatio;
        }
        else{
            x /= widthRatio;
            y /= widthRatio;
        }
        if (currentGUI != null){
            currentGUI.click(new Point(x, y));
            return true;
        }
        return false;
    }
    public boolean scroll(int amount){
        if (currentGUI != null){
            currentGUI.scroll(amount);
            return true;
        }
        return false;
    }
}


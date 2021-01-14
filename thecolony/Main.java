
package thecolony;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import thecolony.entity.EntityFighter;
import thecolony.gui.ImageDialog;
import thecolony.gui.OptionMenu;

/**
 *
 * @author pdogmuncher
 */
public class Main extends JFrame implements ActionListener{
    public static BufferedImage backgroundImg = null;
    public static BufferedImage midgroundImg = null;
    public static BufferedImage enviroImg = null;
    public static Settings settings;
    public JPanel menu1;
    public JButton start;
    public JButton reset;
    public JButton quit;
    public JButton mute;
    public static boolean fullscreen = true;
    public static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public JLabel title, subtitle;
    public Timer scroll;
    public WorldPanel panel;
    public AudioPlayer backgroundMusic;
    public int scrollNum = 0;
    public static Main currentFrame;
    public static int fullX = 0, fullY = 0;
    public final Color TEXT = Color.LIGHT_GRAY;//new Color(102f / 255, 51f / 255f, 0f / 255f, 1f);
    public Main (){reInit();}
    public void reInit(){
        
        this.setIconImage(ImageRegistry.getImage("icon_large.png", false));
        //device.setFullScreenWindow(null);
        
        this.getContentPane().removeAll();
        
        this.setTitle("The Colony");
        this.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.requestFocus();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        if (this.getWidth() < width){
            this.setSize(width, height);
            //this.setExtendedState(MAXIMIZED_BOTH);
        }
        
        if (!this.isDisplayable()){
            this.setUndecorated(fullscreen);
            
        }
        menu1 = new JPanel(){  
            @Override
            protected void paintComponent(Graphics g) {  
                super.paintComponent(g);
                g.drawImage(enviroImg, (scrollNum / 2) % this.getWidth(), 0, getWidth(), getHeight(), this);
                g.drawImage(enviroImg, (scrollNum / 2) % this.getWidth() - this.getWidth(), 0, getWidth(), getHeight(), this);
                
                g.drawImage(backgroundImg, scrollNum % this.getWidth(), 0, getWidth(), getHeight(), this);
                g.drawImage(backgroundImg, scrollNum % this.getWidth() - this.getWidth(), 0, getWidth(), getHeight(), this);
                
                g.drawImage(midgroundImg, scrollNum * 2 % this.getWidth(), 0, getWidth(), getHeight(), this);
                g.drawImage(midgroundImg, scrollNum * 2 % this.getWidth() - this.getWidth(), 0, getWidth(), getHeight(), this);
            }  
        };
        title = new JLabel("The Colony"){
            @Override
            protected void paintComponent(Graphics g){
                g.setFont(this.getFont());
                //g.setColor(new Color(0, 128f / 255f, 128f / 255f, 1f));
                //g.setColor(new Color(128f / 255, 0f / 255f, 0f / 255f, 1f));
                g.setColor(Color.LIGHT_GRAY);
                int stringLen = (int)g.getFontMetrics().getStringBounds("The Last Colony", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString("The Last Colony", start, getHeight() / 2);
            }
        };
        subtitle = new JLabel("The Colony"){
            @Override
            protected void paintComponent(Graphics g){
                g.setFont(this.getFont());
                //g.setColor(new Color(0, 128f / 255f, 128f / 255f, 1f));
                //g.setColor(new Color(128f / 255, 0f / 255f, 0f / 255f, 1f));
                g.setColor(Color.LIGHT_GRAY);
                int stringLen = (int)g.getFontMetrics().getStringBounds("An Intergalactic Space RPG", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString("An Intergalactic Space RPG", start, getHeight() / 2);
            }
        };
        start = new JButton("Start"){  
            @Override
            protected void paintComponent(Graphics g) {  
                //super.paintComponent(g);  
                g.setFont(this.getFont());
                g.setColor(TEXT);
                int stringLen = (int)g.getFontMetrics().getStringBounds("Start", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString("Start", start, getHeight() / 2);
            }  
        };
        reset = new JButton("Reset"){  
            @Override
            protected void paintComponent(Graphics g) {  
                //super.paintComponent(g);  
                g.setFont(this.getFont());
                g.setColor(TEXT);
                int stringLen = (int)g.getFontMetrics().getStringBounds("Reset", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString("Reset", start, getHeight() / 2);
            }  
        };
        quit = new JButton("Exit"){  
            @Override
            protected void paintComponent(Graphics g) {  
                //super.paintComponent(g);  
                g.setFont(this.getFont());
                g.setColor(TEXT);
                int stringLen = (int)g.getFontMetrics().getStringBounds("Exit", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString("Exit", start, getHeight() / 2);
            }  
        };
        mute = new JButton("Mute"){  
            @Override
            protected void paintComponent(Graphics g) {  
                //super.paintComponent(g);  
                g.setFont(this.getFont());
                g.setColor(TEXT);
                int stringLen = (int)g.getFontMetrics().getStringBounds(!Main.settings.getBoolean("muted", false) ? "Mute" : "Unmute", g).getWidth();
                int start = getBounds().width/2 - stringLen/2;
                g.drawString(!Main.settings.getBoolean("muted", false) ? "Mute" : "Unmute", start, getHeight() / 2);
            }  
        };
        
        menu1.setLayout(null);
        
        Font f = new Font("Atari Font Full Version", 0, 20);
        Font titleFont = new Font("Atari Font Full Version", 0, 64);
        start.setFont(f);
        title.setFont(titleFont);
        subtitle.setFont(f);
        reset.setFont(f);
        quit.setFont(f);
        mute.setFont(f);
        
        start.setOpaque(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        title.setOpaque(false);
        subtitle.setOpaque(false);
        reset.setOpaque(false);
        reset.setContentAreaFilled(false);
        reset.setBorderPainted(false);
        quit.setOpaque(false);
        quit.setContentAreaFilled(false);
        quit.setBorderPainted(false);
        mute.setOpaque(false);
        mute.setContentAreaFilled(false);
        mute.setBorderPainted(false);
        
        menu1.add(start);
        menu1.add(title);
        menu1.add(subtitle);
        menu1.add(reset);
        menu1.add(quit);
        menu1.add(mute);
        
        menu1.grabFocus();
        
        start.setBounds(getWidth() / 2 - 100, 350, 200, 50);
        start.addActionListener(this);
        reset.setBounds(getWidth() / 2 - 100, 400, 200, 50);
        reset.addActionListener(this);
        quit.setBounds(getWidth() / 2 - 100, 450, 200, 50);
        quit.addActionListener(this);
        mute.setBounds(getWidth() / 2 - 100, 500, 200, 50);
        mute.addActionListener(this);
        title.setBounds(getWidth() / 2 - 600, 150, 1200, 150);
        subtitle.setBounds(getWidth() / 2 - 600, 200, 1200, 150);
        
        this.getContentPane().add(menu1);
        menu1.setSize(getWidth(), getHeight());
        
        menu1.repaint();
        
        scroll = new Timer(0,this);
        scroll.setInitialDelay(0);
        scroll.setDelay(50);
        scroll.setRepeats(true);
        scroll.addActionListener(this);
        scroll.start();
        if (backgroundMusic != null) {
            if (!backgroundMusic.sound.equals("menu.wav")){
                backgroundMusic.stop();
                backgroundMusic = new AudioPlayer("menu.wav");
                backgroundMusic.play(-1);
            }
            if (!backgroundMusic.clip.isRunning()){
                backgroundMusic = new AudioPlayer("menu.wav");
                backgroundMusic.play(-1);
            }
        }
        repaint();
        
        
    }
    public static void countLines() throws FileNotFoundException{
        final String folderPath = "/Users/pdogmuncher/The Colony/src";

        long totalLineCount = 0;
        final List<File> folderList = new LinkedList<>();
        folderList.add(new File(folderPath));
        while (!folderList.isEmpty()) {
            final File folder = folderList.remove(0);
            if (folder.isDirectory() && folder.exists()) {
                System.out.println("Scanning " + folder.getName());
                final File[] fileList = folder.listFiles();
                for (final File file : fileList) {
                    if (file.isDirectory()) {
                        folderList.add(file);
                    } else if (file.getName().endsWith(".java")
                            || file.getName().endsWith(".sql")) {
                        long lineCount = 0;
                        final Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                            scanner.nextLine();
                            lineCount++;
                        }
                        totalLineCount += lineCount;
                        final String lineCountString;
                        if (lineCount > 99999) {
                            lineCountString = "" + lineCount;
                        } else {
                            final String temp = ("     " + lineCount);
                            lineCountString = temp.substring(temp.length() - 5);
                        }
                        System.out.println(lineCountString + " lines in " + file.getName());
                    }
                }
            }
        }
        System.out.println("Scan Complete: " + totalLineCount + " lines total");
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        //countLines();
        if (System.getProperty("os.name").startsWith("Mac")){
            com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
            Image image = null;
            try {
                image = ImageIO.read(Main.class.getResource("/thecolony/resources/icon_large.png"));
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                application.setDockIconImage(image);
            }
            catch(Exception e){
            
            }  
            Runtime.getRuntime().exec("/bin/bash -c defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
        }
        if (Constants.SKIP){
            JOptionPane.showMessageDialog(null, "Skip mode is currently enabled. This means you are either the developer of the game, or you've been given the wrong version and you should contact the dev team immediately.");
        }
        new File(Constants.SAVE_FILE + "missions").mkdirs();
        if (!Constants.SKIP && JOptionPane.showInputDialog(null, "Run game or world creator?", "", JOptionPane.QUESTION_MESSAGE, new ImageIcon(ImageRegistry.getImage("Icon.png", false)), new String[]{"Game", "World Creator"}, "Game").equals("World Creator")){
            WorldPainter.run(args);
            return;
        }
        settings = new Settings(System.getProperty("user.home") + "/settings.save");
        fullscreen = settings.getBoolean("fullscreen", Boolean.TRUE);
        
        
        Main main = new Main();
        main.backgroundMusic = new AudioPlayer("tinte.wav");
        main.backgroundMusic.play(-1);
        if (!Constants.SKIP){
            final BufferedImage buff = ImageRegistry.getImage("studios.png", true);
            JFrame splash = new JFrame();

            splash.setSize(buff.getWidth(), buff.getHeight());
            splash.setIconImage(ImageRegistry.getImage("Icon.png", false));
            splash.setUndecorated(true);
            splash.getContentPane().add(new JPanel(){
                @Override
                public void paintComponent(Graphics g){
                    super.paintComponent(g);
                    g.drawImage(buff, 0, 0, this);
                }
            });
            splash.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (buff.getWidth() / 2), (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (buff.getHeight() / 2));
            splash.setVisible(true);

            Thread.sleep(2500);
            splash.setVisible(false);
            final BufferedImage buff2 = ImageRegistry.getImage("credit.png", true);
            JFrame splash2 = new JFrame();
            splash2.setSize(buff.getWidth(), buff.getHeight());
            splash2.setUndecorated(true);
            splash2.setIconImage(ImageRegistry.getImage("Icon.png", false));
            splash2.getContentPane().add(new JPanel(){
                public void paintComponent(Graphics g){
                    super.paintComponent(g);
                    g.drawImage(buff2, 0, 0, getWidth(), getHeight(), this);
                }
            });
            splash2.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (buff.getWidth() / 2), (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (buff.getHeight() / 2));
            splash2.setVisible(true);
            Thread.sleep(2500);
            splash2.setVisible(false);
            showImageDialog(
                    new BufferedImage[]{
                        ImageRegistry.getImage("scenehero.png", true),
                        ImageRegistry.getImage("sceneearth.png", true),
                        ImageRegistry.getImage("scenegalaxy.png", true), 
                        ImageRegistry.getImage("scenegalaxy.png", true), 
                        ImageRegistry.getImage("scenegalaxy.png", true), 
                        ImageRegistry.getImage("scenegalaxy.png", true), 
                        ImageRegistry.getImage("scenebomb.png", true), 
                        ImageRegistry.getImage("scenebomb.png", true), 
                        ImageRegistry.getImage("scenebomb.png", true),
                        ImageRegistry.getImage("scenehero.png", true),
                        ImageRegistry.getImage("scenehero.png", true)
                    }, 
                    new String[]{
                        "My name is Zayve Brock. I'm a private investigator in Haven, a town on Bellona, one of the few colony worlds to survive the war. \"What war?\" I'm sure you're asking yourself. Let me start at the beginning.", 
                        "In the year 2075, technology was developed that allowed faster-than-light travel by warping spacetime around an object.", 
                        "This invention, called an Alcubierre Drive, allowed humans to finally explore and colonize the outer reaches of the galaxy and beyond.", 
                        "In the year 2203, many human colonies started fighting for independance from the Earth nations that governed them. A lengthy war broke out.", 
                        "Eventually, the colonies united their military forces to form the Colonies Defense Force, or CDF. The Earth nations did the same, forming the Galactic Treaty Organization, or GTO.", 
                        "In 2213, both the CDF and the GTO were able to develop antimatter bombs capable of destroying entire planets. Due to the threat of mutual destruction, they signed a peace treaty.",
                        "Later that year, the GTO launched antimatter warheads at every colony with a CDF military presence. CDF forces had no time to react. The CDF government, as well as every planet storing CDF antimatter warheads, was destroyed. The CDF was completely crippled.",//"Later that year, every antimatter weapon the CDF had in storage detonated, annihilating the planets they were stored on. The CDF was immensely crippled, considering every planet with a substantial CDF military presence had just been destroyed. The CDF suspected that GTO sabotage was behind the attack.",
                        "However, the CDF had one last contingency plan. Unbeknownst to the GTO, a secret CDF armada had been set up in a remote solar system. The armada used the small supply of antimatter warheads in its possession to destroy Earth, as well as the GTO's colonies.",
                        "Unable to save itself, the GTO launched the rest of its antimatter arsenal to destroy the armada and the remaining CDF worlds. The armada was able to protect its own solar system from the counterattack, but the remaining unprotected CDF planets were destroyed.",
                        "The only solar system to survive the war was the system where the armada was stationed.",
                        "Bellona, my home planet, is in that system. Now, in the year 2223, the system is being fought over by the Colonies Defense Force; the Stosthor Triad, an alien crime ring; and now the resistance, a small insurgent faction."
                    }
            );
        }
        
        
        loadFont("atari full.ttf");
        backgroundImg = loadImage("background.png");
        midgroundImg = loadImage("midground.png");
        enviroImg = loadImage("enviro.png");
        
        currentFrame = main;
        main.setVisible(true);
        main.backgroundMusic.stop();
        main.backgroundMusic = new AudioPlayer("menu.wav");
        main.backgroundMusic.play(-1);
        
        if (fullscreen) {
            Main.device.setFullScreenWindow(Main.currentFrame);
        }
                    
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scroll){
            scrollNum++;
            menu1.repaint();
        }
        if (e.getSource() == mute){
            settings.updateBoolean("muted", !settings.getBoolean("muted", Boolean.FALSE));
            backgroundMusic.stop();
            backgroundMusic = new AudioPlayer("menu.wav");
            
            backgroundMusic.play(-1);
            repaint();
        }
        if (e.getSource() == start){
            fullX = getWidth();
            fullY = getHeight();
            backgroundMusic.stop();
            try {
                new AudioPlayer("click.wav").play(0);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            getContentPane().removeAll();
            menu1.removeAll();
            String[] missions = new File(Constants.SAVE_FILE + "missions").list();
            int length;
            if (missions == null) {
                length = 0;
            }
            else {
                length = missions.length;
            }
            String[] options = new String[length + 1];
            for (int i = 0; i < length; i++){
                options[i + 1] = missions[i];
            }
            options[0] = "Story Mode";
            String choice = "Story Mode";//WorldPanel.showStaticOptionPane(options, "Select mission to load");
            String[] worlds;
            if (choice.equals("Story Mode")){
                worlds = WorldPanel.storyWorlds;
            }
            else{
                File[] subWorlds = new File(Constants.SAVE_FILE + "missions/" + choice).listFiles();
                worlds = new String[subWorlds.length];
                for (int i = 0; i < subWorlds.length; i++){
                    worlds[i] = subWorlds[i].getAbsolutePath().substring(0, subWorlds[i].getAbsolutePath().length() - 4);
                    
                }
            }
            panel = new WorldPanel(worlds, choice);
            getContentPane().add(panel);
            Dimension d = this.getSize();
            pack();
            this.setSize(d);
            if (fullscreen){
                device.setFullScreenWindow(this);
                if (System.getProperty("os.name").startsWith("Mac")){
                    Main.currentFrame.setVisible(false);
                    Main.currentFrame.setVisible(true);
                }
            }
            //this.setSize(1400, 700);
            //this.setExtendedState(MAXIMIZED_BOTH);
            scroll.stop();
            scroll = null;
            panel.loadWorld();
        }
        if (e.getSource() == quit){
            System.exit(0);
        }
        if (e.getSource() == reset){
            try {
                new AudioPlayer("click.wav").play(0);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            File fi = new File(Constants.SAVE_FILE);
            
            if (fi.exists() && WorldPanel.showStaticOptionPane(new String[]{"Yes", "No"}, "Are you sure you want to reset? This cannot be undone").equals("Yes")){
                for (File f : fi.listFiles()){
                    if (f.getName().endsWith(".save")){
                        f.delete();
                    }
                }
                
            }
            
        }
            
    }
    
    public static void loadFont(String fontName){
        URL fontUrl = Main.class.getResource("/thecolony/resources/" + fontName);
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FontFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }
    public static BufferedImage loadImage (String path){
        BufferedImage img = null;
        try {
            img = ImageIO.read(Main.class.getResource("/thecolony/resources/" + path));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return img;
    }
    public static void showImageDialog(BufferedImage[] image, String[] text){
        //JDialog frame = new JDialog(Main.currentFrame, null, true);
        JDialog frame = new JDialog((Dialog)null, true);
        ImageDialog menu = new ImageDialog(frame, image, text);
        frame.add(menu);
        frame.setUndecorated(fullscreen);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskBarSize = settings.getBoolean("fullscreen", true) ? 0 : scnMax.bottom;
        double width = screenSize.getWidth();
        double height = screenSize.getHeight() - taskBarSize;
        frame.setSize((int)width, (int)height);
        //frame.setSize(1400, 700);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        frame.setVisible(true);
        
    }
    public static AttributedString createFallbackString(String text2, Font mainFont, Font fallbackFont) {
        
        String text = text2 + "";
        /*
        for (int i = 0; i < text.length(); i++){
            if (text.charAt(i) == '0'){
                text = text.substring(0, i) + 'O' + text.substring(i + 1);
            }
            
        }
        text = text.replace('q', 'Q');*/
        AttributedString result = new AttributedString(text);

        int textLength = text.length(); 
        if (textLength==0)return new AttributedString("");
        result.addAttribute(TextAttribute.FONT, mainFont, 0, textLength);

        boolean fallback = false;
        int fallbackBegin = 0;
        for (int i = 0; i < text.length(); i++) {
            
            boolean curFallback = !mainFont.canDisplay(text.charAt(i)) || text.charAt(i) == '$';
            if (curFallback != fallback) {
                fallback = curFallback;
                if (fallback) {
                    fallbackBegin = i;
                } else {
                    result.addAttribute(TextAttribute.FONT, fallbackFont, fallbackBegin, i);
                }
            }
        }
        if (fallback){
            result.addAttribute(TextAttribute.FONT, fallbackFont, fallbackBegin, text.length());
            
        }
        return result;
    }
}


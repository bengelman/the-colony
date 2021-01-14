/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import thecolony.entity.EntityPlayer;
import thecolony.items.ItemWeapon;

/**
 *
 * @author brend
 */
public class WorldPainter extends JPanel implements ActionListener{
    String tileset = null;
    String hint = null;
    //int portals = 0;
    ArrayList<Portal> portals = new ArrayList<>();
    ArrayList<ArrayList<Character>> tiles = new ArrayList<>();
    World world;
    String name;
    Timer t;
    File map;
    public WorldPainter(){
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(3000, 3000));
        JFileChooser chooser = new JFileChooser();
        if (!new File(Constants.SAVE_FILE + "/missions").exists())new File(Constants.SAVE_FILE + "/missions").mkdirs();
        chooser.setCurrentDirectory(new File(Constants.SAVE_FILE + "/missions"));
        
        chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/The Colony/src/thecolony/resources/worlds"));
        
        chooser.setFileFilter(new FileNameExtensionFilter(
        "MAP files", "map"));
        if (JOptionPane.showOptionDialog(null, "Open or create file?", "File creation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(ImageRegistry.getImage("icon.png", false)), new String[]{"Create New", "Open Existing"}, "Create New") == 0){
            int i = chooser.showSaveDialog(null);
            if (i != 0)System.exit(0);
            map = chooser.getSelectedFile();
            if (!map.getAbsolutePath().endsWith(".map"));
                map = new File(map.getAbsolutePath().concat(".map"));
        }
        else{
            int i = chooser.showOpenDialog(null);
            
            if (i != 0)System.exit(0);
            map = chooser.getSelectedFile();
        }
        name = map.getName().substring(0, map.getName().indexOf("."));
        //name = JOptionPane.showInputDialog("Enter world name");
        try {
            ImageIO.read(ImageRegistry.class.getResource("/thecolony/resources/" + name + ".png"));
        }catch(Exception e){
            ImageRegistry.images.put(name + ".png", ImageRegistry.getImage("haven.png", true));
        }
        
        if (!map.exists()){
            PrintWriter write = null;
            try {
                write = new PrintWriter(new BufferedWriter(new FileWriter(map)));
                tileset = (String) JOptionPane.showInputDialog(null, "Select tileset", "Tileset", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ImageRegistry.getImage("icon.png", false)), new String[]{"haven", "base", "prison", "city"}, "haven");
                write.println(tileset);
                hint = JOptionPane.showInputDialog("Enter hint for world (displayed when using detective chip)");
                write.println(hint);
                write.println("INSERT LOCATION");
                write.println(0);
                write.println(0);
                write.println("xxx");
                write.println(" ");
            } catch (IOException ex) {
                Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                write.close();
            }
        }
        
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(map));
            Scanner in = new Scanner(stream);
            tileset = in.nextLine();
            hint = in.nextLine();
            String destination = in.nextLine();
            while (!destination.equals("xxx")){
                int x = Integer.parseInt(in.nextLine());
                int y = Integer.parseInt(in.nextLine());
                portals.add(new Portal(destination, x, y));
                destination = in.nextLine();
            }
            int linenum = 0;
            while(in.hasNextLine()){
                String line = in.nextLine();
                char[] chars = line.toCharArray();
                Character[] cobj = new Character[chars.length];
                for (int i = 0; i < chars.length; i++){
                    cobj[i] = chars[i];
                }
                tiles.add(new ArrayList(Arrays.asList(cobj)));
            }
            stream.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream newstream = null;
        try {
            newstream = new BufferedInputStream(new FileInputStream(map));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
        WorldPanel.journals = new TreeMap<>();
        WorldPanel.npcs = new TreeMap<>();
        WorldPanel.signs = new TreeMap<>();
        WorldPanel.journals.put(name, new String[]{});
        WorldPanel.npcs.put(name, new String[]{});
        WorldPanel.signs.put(name, new String[]{});
        world = new World(newstream, World.parseStream(null), name);
        world.player = new EntityPlayer(getWidth(), getHeight(), 48);
        WorldPanel.GAMEWIDTH = getWidth() * 2;
        WorldPanel.GAMEHEIGHT = getHeight() * 2;
        t = new Timer(0, this);
        t.addActionListener(this);
        t.setInitialDelay(100);
        t.setRepeats(true);
        t.setDelay(100);
        t.start();
        repaint();
        this.setLayout(null);
        this.initMouse();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //System.out.println("Calling action");
        BufferedImage worldImg = new BufferedImage(getWidth() * 2, getHeight() * 2, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2 = worldImg.createGraphics();
        super.paintComponent(g2);
        //g2.setColor(Color.YELLOW);
        g2.fillRect(0, 0, worldImg.getWidth(), worldImg.getHeight());
        
        g2.setColor(Color.DARK_GRAY);
        //System.out.println("Size is " + this.getSize());
        g2.setStroke(new BasicStroke(5));
        for (int i = 0; i < (worldImg.getHeight() / 50); i++){
            g2.drawLine(0, (i * 50) + 25, worldImg.getWidth(), (i * 50) + 25);
        }
        
        for (int j = 0; j < (worldImg.getWidth() / 50); j++){
            //g2.fillRect(i * 50, 0, i * 50, worldImg.getHeight());
            g2.drawLine(j * 50, 0, j * 50, worldImg.getHeight());
            
        }
        //g2.drawLine(50, 0, 70, worldImg.getHeight());
        world.paintAll(g2, getWidth() * 2, getHeight() * 2);
        Point pointerLocation = new Point(MouseInfo.getPointerInfo().getLocation());
        if (this.isShowing())
            pointerLocation.translate(- this.getLocationOnScreen().x, - this.getLocationOnScreen().y);
        
        //System.out.println("Drawing at " + -this.getLocationOnScreen().x + ", " + ((getHeight() * 2) - this.getLocationOnScreen().y - WorldPanel.getStringHeight(g, "Test") / 2));
        
        g.drawImage(worldImg, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Impact", 0, 30));
        int height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        //System.out.println("Drawing at " + (height - this.getLocationOnScreen().y - WorldPanel.getStringHeight(g, "Test") / 2));
        g.drawString(pointerLocation.x * 2 + ", " + pointerLocation.y * 2, 1 - this.getLocationOnScreen().x, height - this.getLocationOnScreen().y - WorldPanel.getStringHeight(g, "Test") - 50);
    }
    
    public static void run(String[] args){
        final JFrame frame = new JFrame();
        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        WorldPainter paint = new WorldPainter();
        JScrollPane scroll = new JScrollPane(paint);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.requestFocusInWindow();
        repaint();
    }
    public void initMouse(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev){}
            @Override
            public void mouseReleased(MouseEvent ev){}
            @Override
            public void mouseClicked(MouseEvent ev) {
                Point click = ev.getPoint();
                int x = (click.x / 25);
                int y = (click.y * 2 + 25) / 50;
                if (ev.getButton() == MouseEvent.BUTTON3){
                    if (tiles.size() > y){
                        if (tiles.get(y).size() > x){
                            if (tiles.get(y).get(x) == 'P'){
                                int portalsEncountered = 0;
                                for (int i = 0; i < y; i++){
                                    for (int j = 0; j < tiles.get(i).size(); j++){
                                        if (tiles.get(i).get(j).equals('P')){
                                            portalsEncountered++;
                                        }
                                    }
                                }
                                for (int i = 0; i < x; i++){
                                    if (tiles.get(y).get(i).equals('P')){
                                        portalsEncountered++;
                                    }
                                }
                                portals.remove(portalsEncountered);
                                
                            }
                            tiles.get(y).set(x, ' ');
                            for (int j = tiles.get(y).size() - 1; j >= 0; j--){
                                if (tiles.get(y).get(j).equals(' ')){
                                    tiles.get(y).remove(j);
                                }
                                else{
                                    break;
                                }
                            }
                            if (tiles.get(y).isEmpty()){
                                tiles.remove(y);
                                outer:
                                for (int i = tiles.size() - 1; i >= 0; i--){
                                    for (int j = tiles.get(i).size() - 1; j >= 0; j--){
                                        if (tiles.get(i).get(j).equals(' ')){
                                            tiles.get(i).remove(j);
                                        }
                                        else{
                                            break outer;
                                        }
                                    }
                                    if (tiles.get(i).isEmpty()){
                                        tiles.remove(i);
                                    }
                                }
                            }
                            
                        }
                    }
                    reset();
                    return;
                }
                char c = getObject();
                
                while (tiles.size() <= y){
                    tiles.add(new ArrayList<Character>());
                    
                }
                while (tiles.get(y).size() <= x){
                    tiles.get(y).add(' ');
                }
                if (c == 'P' || (tiles.get(y).size() > x && tiles.get(y).get(x) == 'P')){
                    int portalsEncountered = 0;
                    for (int i = 0; i < y; i++){
                        for (int j = 0; j < tiles.get(i).size(); j++){
                            if (tiles.get(i).get(j).equals('P')){
                                portalsEncountered++;
                            }
                        }
                    }
                    for (int i = 0; i < x; i++){
                        if (tiles.get(y).get(i).equals('P')){
                            portalsEncountered++;
                        }
                    }
                    if (c == 'P'){
                        String destination = JOptionPane.showInputDialog("Enter portal destination name");
                        int xp = Integer.parseInt(JOptionPane.showInputDialog("Enter portal destination x"));
                        int yp = Integer.parseInt(JOptionPane.showInputDialog("Enter portal destination y"));
                        portals.add(portalsEncountered, new Portal(destination, xp, yp));
                    }
                    else{
                        portals.remove(portalsEncountered);
                    }
                    
                }
                tiles.get(y).set(x, c);
                /*
                for (int i = 0; i < tiles.size(); i++){
                    for (int j = 0; j < tiles.get(i).size(); j++){
                        System.out.print(tiles.get(i).get(j));
                    }
                    System.out.println();
                }*/
                reset();
            }
        });
    }
    public void reset(){
        //File map = new File(name + ".map");
        PrintWriter write = null;
        try {
            write = new PrintWriter(new BufferedWriter(new FileWriter(map, false)));
            
            write.println(tileset);
            write.println(hint);
            if (portals.isEmpty()){
                write.println("INSERT LOCATION");
                write.println(0);
                write.println(0);
            }
            for (int i = 0; i < portals.size(); i++){
                write.println(portals.get(i).destination);
                write.println(portals.get(i).x);
                write.println(portals.get(i).y);
            }
            write.println("xxx");
            for (int i = 0; i < tiles.size(); i++){
                for (int j = 0; j < tiles.get(i).size(); j++){
                    write.print(tiles.get(i).get(j));
                }
                write.println();
            }
        } catch (IOException ex) {
            Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            write.close();
        }
        InputStream stream = null;
        InputStream data = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(map));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorldPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int i = world.player.getBounds().x;
        int j = world.player.getBounds().y;
        ImageRegistry.images.remove(name);
        world = new World(stream, World.parseStream(null), name);
        world.player = new EntityPlayer(i, j, 48);
        repaint();
    }
    
    public Pair[] tileTypes = new Pair[]{
        new Pair("Horizontal wall", 'w'), 
        new Pair("Vertical wall", 'W'), 
        new Pair("Down-right corner", 'C'), 
        new Pair("Down-left corner", 'c'), 
        new Pair("Up-right corner", 'X'), 
        new Pair("Up-left corner", 'x'), 
        new Pair("Horizontal fence", 'f'), 
        new Pair("Vertical fence", 'F'), 
        new Pair("Portal", 'P'),
        new Pair("Wall base", 'E'),
        new Pair("Wall top", 'e'),
        new Pair("Roof", 'r'),
        new Pair("NPC", 'n'),
        new Pair("Vendor", 'v'),
        new Pair("Journal", 'J'),
        new Pair("Bed", 'B'),
        new Pair("Sign", 'S'),
        new Pair("Horizontal box", 'b'),
        new Pair("Vertical box", 'M'),
        new Pair("Path", 's')
    };
    public Pair lastSelection = tileTypes[0];
    public char getObject(){
        Pair result = (Pair) JOptionPane.showInputDialog(null, "Select object to add", "Object Selection", JOptionPane.INFORMATION_MESSAGE, null, tileTypes, lastSelection);
        lastSelection = result;
        return result.c;
    }
    public class Pair {
        String s;
        char c;
        public Pair(String s, char c){
            this.s = s;
            this.c = c;
        }
        @Override
        public String toString(){
            return s;
        }
    }
    public class Portal{
        String destination;
        int x, y;
        public Portal(String destination, int x, int y){
            this.destination = destination;
            this.x = x;
            this.y = y;
        }
    }
}

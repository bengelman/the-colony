/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.entity.EntityLoot;
import thecolony.entity.EntityPlayer;
import thecolony.Main;
import thecolony.WorldPanel;
import static thecolony.WorldPanel.bitFont;
import static thecolony.WorldPanel.fallbackFont;

/**
 *
 * @author pdogmuncher
 */
public class ImageDialog extends JPanel implements MouseListener, ActionListener{
    JDialog parent;
    BufferedImage[] images;
    String[] text;
    int display = 0;
    Timer timer;
    int pauseTicks = 0;
    int wordTimer = 0;
    char lastDisplayChar = 'a';
    public ImageDialog(final JDialog parent, BufferedImage[] images, String[] text){
        parent.setIconImage(ImageRegistry.getImage("icon_large.png", false));
        this.parent = parent;
        this.requestFocusInWindow();
        this.setLayout(null);
        this.removeAll();
        this.setLayout(null);
        this.text = text;
        wordTimer = text[0].length();
        timer = new Timer(0, this);
        timer.setRepeats(true);
        timer.setDelay(1000 / WorldPanel.TICKS_PER_SECOND);
        timer.start();
        parent.setIconImage(ImageRegistry.getImage("icon.png", true));
        
        //this.setSize(1400, 700);
        
        parent.setResizable(false);
        this.images = images;
        /*
        JLabel title = new JLabel(question){
            public void paintComponent(Graphics g){
                g.setFont(new Font("Atari Font Full Version", 0, 20));
                g.setColor(Color.BLACK);
                g.drawString(question, 0, getHeight() / 2);
            }
            public void paint(Graphics g){
                paintComponent(g);
            }
        };*/
        this.addMouseListener(this);
        repaint();
    }
        
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(bitFont);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(images[display], 0, 0, getWidth(), getHeight(), this);
        int xoffset = text[display].endsWith(".png") ? 110 : 0;

        g.setColor(Color.BLACK);
        String[] words = text[display].split(" ");
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> displayLines = new ArrayList<>();

        for (int i = text[display].endsWith(".png") ? 1 : 0; i < words.length; i++){
            if (lines.isEmpty()){
                lines.add(words[i]);
                continue;
            }
            if (WorldPanel.getStringWidth(g, lines.get(lines.size() - 1) + " " + words[i]) > getWidth() - xoffset){
                
                lines.add(words[i]);
            }
            else{
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " " + words[i]);
            }
        }
        int added = 0;
        int max = text[display].length() - wordTimer;
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
        int width = lines.size() <= 1 ? WorldPanel.getStringWidth(g, lines.get(0)) + xoffset : getWidth() - 20;
        int heightamt = lines.size() * (WorldPanel.getStringHeight(g, "Test") + 10);
        g.fillRoundRect((getWidth() / 2) - (width / 2) - 10, getHeight() - 210, width + 20, heightamt > xoffset ? heightamt : xoffset, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect((getWidth() / 2) - (width / 2) - 10, getHeight() - 210, width + 20, heightamt > xoffset ? heightamt : xoffset, 10, 10);
        if (text[display].startsWith("zayvehead.png")){
            g.drawImage(ImageRegistry.getImage("zayvehead.png", false), (getWidth() / 2) - (width / 2) - 5, getHeight() - 205, 100, 100, this);
        }
        else if (text[display].endsWith(".png")){
            g.drawImage(ImageRegistry.getImage(words[0].substring(1), false), (getWidth() / 2) - (width / 2) - 5, getHeight() - 205, 100, 100, this);
        }

        g.setColor(Color.WHITE);
        for (int j = 0; j < displayLines.size(); j++){
            int yh = getHeight() - 190 + (j * (WorldPanel.getStringHeight(g, "Test") + 10));
            g.drawString(Main.createFallbackString(displayLines.get(j), bitFont, fallbackFont).getIterator(), xoffset + (lines.size() > 1 ? 10 : (getWidth() / 2) - ((xoffset + WorldPanel.getStringWidth(g, lines.get(j))) / 2)), yh);
        }
        if (!displayLines.isEmpty())
            if (displayLines.get(displayLines.size() - 1).length() > 0)
                lastDisplayChar = displayLines.get(displayLines.size() - 1).charAt(displayLines.get(displayLines.size() - 1).length() - 1);
        
    }

    

    @Override
    public void mouseClicked(MouseEvent e) {
        if (wordTimer > 0){
            wordTimer = 0;
            return;
        }
        try {
            new AudioPlayer("click.wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        display ++;
        
        repaint();
        
        if (display >= images.length)
            parent.setVisible(false);
        else
            wordTimer = text[display].length();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (wordTimer > 0){
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
        }
        repaint();
    }
        
}

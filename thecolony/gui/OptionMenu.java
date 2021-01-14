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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.text.AttributedCharacterIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.entity.EntityLoot;
import thecolony.entity.EntityPlayer;
import thecolony.Main;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public class OptionMenu extends JPanel implements ActionListener{
    EntityPlayer player;
    JDialog parent;
    WorldPanel panel;
    EntityLoot loot;
    String question;
    public String result;
    String[] options;
    
    public OptionMenu(final JDialog parent, final WorldPanel panel, final String question, String[] options){
        parent.setIconImage(ImageRegistry.getImage("icon_large.png", false));
        this.parent = parent;
        this.panel = panel;
        this.requestFocusInWindow();
        this.setLayout(null);
        this.removeAll();
        this.options = options;
        this.question = question;
        int currentPos = 25;
        this.setLayout(null);
        this.setSize(Main.currentFrame.getWidth(), Main.currentFrame.getHeight());
        JLabel title = new JLabel(question){
            public void paintComponent(Graphics g){
                g.setColor(Color.LIGHT_GRAY);
                    
                g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 5, 5);
                g.setFont(new Font("Atari Font Full Version", 0, 20));
                g.setColor(Color.BLACK);
                Font bit = new Font("Atari Font Full Version", 0, 20);
                Font back = new Font("Impact", 0, 20);
                g.drawString(Main.createFallbackString(question, bit, back).getIterator(), 0, getHeight() / 2);
            }
            public void paint(Graphics g){
                paintComponent(g);
            }
        };
        title.setBounds(0, currentPos, getWidth(), 50);
        this.add(title);
        currentPos += 60;
        for (int i = 0; i < options.length; i++){
            final int j = i;
            final String[] options2 = options;
            JButton q = new JButton(options[i]){
                public void paintComponent(Graphics g){
                    g.setColor(Color.LIGHT_GRAY);
                    
                    g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 5, 5);
                    g.setFont(new Font("Atari Font Full Version", 0, 20));
                    g.setColor(Color.BLACK);
                    Font bit = new Font("Atari Font Full Version", 0, 20);
                    Font back = new Font("Impact", 0, 20);
                    g.drawString(Main.createFallbackString(options2[j], bit, back).getIterator(), 0, getHeight() / 2);
                }
                public void paint(Graphics g){
                    paintComponent(g);
                }
            };
            
            q.setBounds(0, currentPos, getWidth(), 50);
            this.add(q);
            q.addActionListener(this);
            currentPos += 60;
        }
        repaint();
    }
        
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        this.result = b.getText();
        try {
            new AudioPlayer("click.wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        parent.setVisible(false);
        if (panel != null){
            //panel.paused = false;
            panel.requestFocus();
        }
        
    }
        
}

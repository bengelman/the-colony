/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public abstract class GUI {
    public void onUpdate(){}
    public abstract void draw(Graphics2D g);
    public abstract void click(Point p);
    public abstract void scroll(int amount);
    public void close(){
        WorldPanel.panel.currentGUI = null;
    }
}

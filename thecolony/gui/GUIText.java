/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import thecolony.Main;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public class GUIText extends GUI{
    String[] text;
    public GUIText(String[] text){
        this.text = text;
    }
    @Override
    public void click(Point p){
        close();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRoundRect(100, 100, WorldPanel.GAMEWIDTH - 200, WorldPanel.GAMEHEIGHT - 200, 10, 10);
        g.setStroke(new BasicStroke(4));
        g.setColor(Color.BLACK);
        g.drawRoundRect(100, 100, WorldPanel.GAMEWIDTH - 200, WorldPanel.GAMEHEIGHT - 200, 10, 10);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Atari Font Full Version", 0, 20));
        Font bit = new Font("Atari Font Full Version", 0, 20);
        Font back = new Font("Impact", 0, 20);
        ArrayList<String> lines = new ArrayList<>();
        for (int i = 0; i < text.length; i++){
            String[] words = text[i].split(" ");
            lines.add(words[0]);
            for (int j = 1; j < words.length; j++){
                int length = WorldPanel.getStringWidth(g, lines.get(lines.size() - 1) + " " + words[j]);
                if (length > (WorldPanel.GAMEWIDTH - 220)){
                    lines.add(words[j]);
                }
                else{
                    lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " " + words[j]);
                }
            }
        }
        for (int i = 0; i < lines.size(); i++){
            g.drawString(Main.createFallbackString(lines.get(i), bit, back).getIterator(), 110, i * 25 + 25 + 100);
        }
    }

    @Override
    public void scroll(int amount) {
        
    }
}

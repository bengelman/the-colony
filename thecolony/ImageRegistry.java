/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;

/**
 *
 * @author pdogmuncher
 */
public class ImageRegistry {
    public static TreeMap <String, BufferedImage> images = new TreeMap<>();
    public static BufferedImage getImage(String path, boolean temp){
        
        if (images.containsKey(path)){
            return images.get(path);
        }
        else{
            BufferedImage img = null;
            try {
                if (path.startsWith(Constants.SAVE_FILE)){
                    img = ImageIO.read(new File(path));
                }
                else
                    img = ImageIO.read(ImageRegistry.class.getResource("/thecolony/resources/" + path));
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Error with image: " + path);
                e.printStackTrace();
                return null;
            }
            if (!temp) {
                images.put(path, img);
            }
            return img;
        }
    }
    public static BufferedImage getFlippedImage(String path){
        if (images.containsKey(path + "flip")){
            return images.get(path + "flip");
        }
        else{
            BufferedImage img = null;
            try {
                if (path.startsWith(Constants.SAVE_FILE)){
                    img = ImageIO.read(new File(path));
                }
                else {
                    img = ImageIO.read(ImageRegistry.class.getResource("/thecolony/resources/" + path));
                }
                img = ImageRegistry.flip(img);
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println("Error with image: " + path);
                return null;
                ///Logger.getLogger(ImageRegistry.class.getName()).log(Level.SEVERE, null, e);
            }
            images.put(path + "flip", img);
            return img;
        }
        

    }
    public static void printRegistry(){
        for(Map.Entry<String,BufferedImage> entry : images.entrySet()) {
            String key = entry.getKey();

            System.out.println(key);
        }
    }/*
    public static void loadBipedAssets(String path){
        try {
            BufferedImage image = ImageIO.read(ImageRegistry.class.getResource("/thecolony/resources/" + path));
            BufferedImage left = image.getSubimage(0, 0, 16, 32);
            BufferedImage left1 = image.getSubimage(16, 0, 16, 32);
            BufferedImage left2 = image.getSubimage(32, 0, 16, 32);
            BufferedImage back = image.getSubimage(48, 0, 12, 32);
            BufferedImage back1 = image.getSubimage(60, 0, 12, 32);
            BufferedImage back2 = image.getSubimage(72, 0, 12, 32);
            BufferedImage right = copyFlip(left);
            BufferedImage right1 = copyFlip(left1);
            BufferedImage right2 = copyFlip(left2);
            images.put(path + "left", left);
            images.put(path + "left1", left1);
            images.put(path + "left2", left2);
            images.put(path + "back", back);
            images.put(path + "back1", back1);
            images.put(path + "back2", back2);
            images.put(path + "right", right);
            images.put(path + "right1", right1);
            images.put(path + "right2", right2);
        } catch (IOException ex) {
            Logger.getLogger(ImageRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
     }*/
     public static BufferedImage copyFlip (BufferedImage src){
         BufferedImage img = src.getSubimage(0, 0, src.getWidth(), src.getHeight());
         AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); 
         tx.translate(-img.getWidth(null), 0); 
         AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
         img = op.filter(img, null);
         return img;
     }
     public static BufferedImage flip (BufferedImage img) {
         AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); 
         tx.translate(-img.getWidth(null), 0); 
         AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
         img = op.filter(img, null);
         return img;
     }
}

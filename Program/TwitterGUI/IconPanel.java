package TwitterGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IconPanel extends JPanel{

  public static final int Width = Tweet.HEIGHT;
  public BufferedImage image;
  
  public IconPanel(String URL) throws URISyntaxException, IOException{
    this.setPreferredSize(new Dimension(80,80));
    this.setBounds(0,0,80,80);
    URL url = new URL(URL);
    this.image  =ImageIO.read(url);
    ImageIcon icon = new ImageIcon(this.image);
    JLabel label = new JLabel(icon);
    this.add(label);
    //this.setOpaque(false);
    label.setVisible(true);
    this.setVisible(true);
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}

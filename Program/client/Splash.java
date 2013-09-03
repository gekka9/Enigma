package client;

import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Splash extends JFrame{
    private static final String MESSAGE = "Now connecting to Twitter...";
    public Splash(){
      this.setVisible(false);
      JPanel info = new JPanel();
      BoxLayout layout=new BoxLayout(info,BoxLayout.Y_AXIS);
      info.setLayout(layout);
      this.getContentPane().add(info);
      info.add(new JLabel(new ImageIcon("gui"+File.separator+"logo.png")));
      info.add(new JLabel(MESSAGE, JLabel.CENTER));
      this.pack();
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
      Splash splash = new Splash();
      splash.setVisible(true);
    }
}

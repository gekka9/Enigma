package client;


import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VersionWindow extends JFrame{
  private final static String BASETEXT = "Version: ";
  private final static String VERSION = "0.20";
  private final static String CREDIT = "© @gekka9, 2013";
  
  public VersionWindow(){
    this.setVisible(false);
    JPanel info = new JPanel();
    BoxLayout layout=new BoxLayout(info,BoxLayout.Y_AXIS);
    info.setLayout(layout);
    this.getContentPane().add(info);
    info.add(new JLabel(new ImageIcon("./gui/logo.png")));
    info.add(new JLabel(BASETEXT+VERSION, JLabel.CENTER));
    info.add(new JLabel(CREDIT, JLabel.CENTER));
    this.pack();
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    VersionWindow window = new VersionWindow();
    window.setVisible(true);
  }

}

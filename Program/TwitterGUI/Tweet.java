package TwitterGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import twitter4j.Status;

public class Tweet extends JPanel{

  public static final int HEIGHT = 80;
  
  private Status status;
  private IconPanel icon;
  private Contents contents;
  private Console console;
  private ClientModel model;
  
  public Tweet(Status status, ClientModel model){
    this.model=model;
    this.setPreferredSize(new Dimension(400,80));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
    GridBagLayout layout=new GridBagLayout();
    this.setLayout(layout);
    GridBagConstraints c=new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;  // 制約の記述
    c.anchor=GridBagConstraints.WEST;
    this.setBackground(Color.red);
    IconPanel icon=null;
    Console console = new Console(status,this);
    Contents contents = new Contents(status);
    try {
      icon = new IconPanel(status.getUser().getProfileImageURL());
      icon.setBounds(0, 0, 80, 80);
      c.gridx=0;
      c.gridy=0;
      layout.setConstraints(icon, c);
      this.add(icon);
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    c.gridx=1;
    c.gridy=0;
    c.weightx = 1.0d;
    layout.setConstraints(contents,c);
    this.add(contents);
    
    c.gridx=2;
    c.gridy=0;
    c.weightx = 0.0d;
    layout.setConstraints(console, c);
    this.add(console);
    this.setVisible(true);
  }
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  public ClientModel getClientModel() {
    return this.model;
  }

}

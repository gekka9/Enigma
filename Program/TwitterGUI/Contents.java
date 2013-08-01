package TwitterGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;

import twitter4j.Status;

public class Contents extends Box{
  public Contents(Status status){
    super(BoxLayout.Y_AXIS);
    GridBagConstraints c=new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;  // 制約の記述
    c.anchor=GridBagConstraints.WEST;
    
    this.setMinimumSize(new Dimension(200,80));
    this.setPreferredSize(new Dimension(200,80));
    this.setBackground(Color.blue);
    JTextArea name = new JTextArea(status.getUser().getScreenName()+" " + status.getUser().getName());
    name.setPreferredSize(new Dimension(100,name.getFont().getSize()));
    name.setMaximumSize(new Dimension(Short.MAX_VALUE,name.getFont().getSize()*2));
    name.setEditable(false);
    name.setLineWrap(true);
    name.setForeground(Color.black);
    name.setBackground(Color.green);
    
    JTextArea text = new JTextArea(status.getText());
    text.setEditable(false);
    text.setLineWrap(true);
    text.setForeground(Color.white);
    text.setBackground(Color.DARK_GRAY);
    
    c.gridx=1;
    c.gridy=0;
    c.weightx = 0.0d;
    this.add(name);
    
    c.gridx=0;
    c.gridy=1;
    c.weightx = 1.0d;
    this.add(text);
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}

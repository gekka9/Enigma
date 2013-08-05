package TwitterGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import twitter4j.Status;

public class Tweet extends JPanel{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private ClientModel model;
  private JLabel iconLabel;
  private ImageIcon icon;
  private JTextArea name;
  private JTextArea text;
  private JButton rtButton;
  private JButton reButton;
  private JButton favButton;
  private JButton otherButton;
  public static final int BUTTON_SIZE=32;
  public static final int ICON_SIZE=80;
  
  public Tweet(){
    super();
    GridBagLayout layout=new GridBagLayout();
    this.setLayout(layout);
    GridBagConstraints c=new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;  // 制約の記述
    c.anchor=GridBagConstraints.NORTHWEST;
    c.insets = new Insets(3, 0, 0, 3);
    this.setBackground(Color.red);
    
    //アイコン
    c.gridx=0;
    c.gridy=0;
    c.gridwidth=4;
    c.gridheight=4;
    iconLabel = new JLabel();
    icon=new ImageIcon();
    iconLabel.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
    iconLabel.setPreferredSize(new Dimension(ICON_SIZE,ICON_SIZE));
    layout.setConstraints(iconLabel, c);
    this.add(iconLabel);
    
    //名前
    c.anchor=GridBagConstraints.NORTHWEST;
    c.gridx=4;
    c.gridy=0;
    c.gridwidth=12;
    c.gridheight=1;
    c.weightx=1;
    name = new JTextArea();
    name.setEditable(false);
    name.setLineWrap(false);
    name.setForeground(Color.black);
    name.setBackground(Color.green);
    layout.setConstraints(name, c);
    this.add(name);
    
    //本文
    c.gridx=4;
    c.gridy=1;
    c.gridwidth=12;
    c.gridheight=3;
    c.weightx=1;
    //c.weighty=1;
    text = new JTextArea();
    text.setEditable(false);
    text.setLineWrap(true);
    text.setForeground(Color.white);
    text.setBackground(Color.DARK_GRAY);
    layout.setConstraints(text, c);
    this.add(text);
    
    
    c.gridx=16;
    c.gridy=0;
    c.gridwidth=4;
    c.gridheight=4;
    c.weightx=0;
    c.weighty=0;
    c.fill=GridBagConstraints.NONE;
    JPanel console = new JPanel();
    console.setLayout(new GridLayout(2,2));
    layout.setConstraints(console, c);
    this.add(console);

    rtButton = new JButton(new ImageIcon("./gui/rt.png") );
    rtButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    rtButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    console.add(rtButton);

    reButton = new JButton(new ImageIcon("./gui/re.png") );
    reButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    reButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    console.add(reButton);
    

    favButton = new JButton(new ImageIcon("./gui/fav.png") );
    favButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    favButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    console.add(favButton);
    

    otherButton = new JButton(new ImageIcon("./gui/other.png") );
    otherButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    otherButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    console.add(otherButton);
    
    
    /*
    //RT
    c.gridx=16;
    c.gridy=0;
    c.gridwidth=2;
    c.gridheight=2;
    c.weightx=0;
    c.weighty=0;
    c.fill=GridBagConstraints.NONE;
    rtButton = new JButton(new ImageIcon("./gui/rt.png") );
    rtButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    rtButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    layout.setConstraints(rtButton, c);
    this.add(rtButton);
    
    //Re
    c.gridx=18;
    c.gridy=0;
    c.gridwidth=2;
    c.gridheight=2;
    reButton = new JButton(new ImageIcon("./gui/re.png") );
    reButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    reButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    layout.setConstraints(reButton, c);
    this.add(reButton);
    
    //fav
    c.gridx=16;
    c.gridy=2;
    c.gridwidth=2;
    c.gridheight=2;
    favButton = new JButton(new ImageIcon("./gui/fav.png") );
    favButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    favButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    layout.setConstraints(favButton, c);
    this.add(favButton);
    
    //other
    c.gridx=18;
    c.gridy=2;
    c.gridwidth=2;
    c.gridheight=2;
    othreButton = new JButton(new ImageIcon("./gui/other.png") );
    othreButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    othreButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    layout.setConstraints(othreButton, c);
    this.add(othreButton);
    */
    
  }
  
  public void setValues(Status status, ClientModel model,ImageIcon image,boolean isSelect){
    this.icon=image;
    this.iconLabel.setIcon(this.icon);
    this.name.setText(status.getUser().getName());
    this.text.setText(status.getText());
    if(isSelect){
      this.setBackground(Color.blue);
    }else{
      this.setBackground(Color.red);
    }
  }

  public ClientModel getClientModel() {
    return this.model;
  }

}

package TwitterGUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.DefaultListModel;

class EnigmaMenu extends MenuBar implements ActionListener, ItemListener {
  private DefaultListModel listModel;
  public EnigmaMenu(DefaultListModel listModel){
    this.listModel=listModel;
        // [File]
        Menu menuFile = new Menu("Utility");
        menuFile.addActionListener(this);
        this.add(menuFile);
        // [File]-[Open]
        MenuItem crearTweets = new MenuItem("Clear Tweets", new MenuShortcut('O'));
        menuFile.add(crearTweets);
        // [File]-[----]
        //menuFile.addSeparator();
        // [File]-[Exit]
        MenuItem clearAuthentication = new MenuItem("Clear Authentication");
        menuFile.add(clearAuthentication);
    }
    public void actionPerformed(ActionEvent e) {
      if(e.getActionCommand().equals("Clear Tweets")){
        this.listModel.clear();
      }else if(e.getActionCommand().equals("Clear Authentication")){
        
      }
    }
    public void itemStateChanged(ItemEvent e) {
        CheckboxMenuItem menu = (CheckboxMenuItem)e.getSource();
        if (menu.getState()) {
            System.out.println(menu.getLabel() + " SELECTED");
        } else {
            System.out.println(menu.getLabel() + " DESELECTED");
        }
    }
}
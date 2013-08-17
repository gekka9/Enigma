package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.DefaultListModel;

import twitter4j.Twitter;

class EnigmaMenu extends MenuBar implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private DefaultListModel listModel;
  private Twitter twitter;
  private ClientModel model;
  public EnigmaMenu(DefaultListModel listModel,ClientModel model){
    this.listModel=listModel;
    this.model = model;
        // [File]
        Menu menuFile = new Menu("Utility");
        menuFile.addActionListener(this);
        this.add(menuFile);
        // [File]-[Open]
        MenuItem crearTweets = new MenuItem("Clear Tweets", new MenuShortcut('O'));
        menuFile.add(crearTweets);
        MenuItem clearAuthentication = new MenuItem("Clear Authentication");
        menuFile.add(clearAuthentication);

        MenuItem decode = new MenuItem("Decode tweet");
        menuFile.add(decode);
    }
    public void actionPerformed(ActionEvent e) {
      if(e.getActionCommand().equals("Clear Tweets")){
        this.listModel.clear();
      }else if(e.getActionCommand().equals("Clear Authentication")){
        
      }else if(e.getActionCommand().equals("Decode tweet")){
        new DecodeWindow(this.model);
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
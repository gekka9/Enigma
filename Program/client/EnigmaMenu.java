package client;


import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;


class EnigmaMenu extends MenuBar implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private DefaultListModel listModel;
  private ClientModel model;
  public EnigmaMenu(DefaultListModel listModel,ClientModel model){
    this.listModel=listModel;
    this.model = model;
        // [File]
        Menu menuFile = new Menu("Utility");
        menuFile.addActionListener(this);
        this.add(menuFile);
        // [File]-[Open]
        MenuItem crearTweets = new MenuItem("Clear Tweets");
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
    @Override
    public void itemStateChanged(ItemEvent e) {
      // TODO Auto-generated method stub
      
    }
}
package client;


import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;

/**
 * メニューバー
 * @author gekka9
 *
 */
class EnigmaMenu extends MenuBar implements ActionListener, ItemListener {
  /**
   * 生成されたシリアルバージョンUID
   */
  private static final long serialVersionUID = 8492477380317870610L;
  
  /**
   * タイムラインのListModel
   */
  private DefaultListModel listModel;
  
  /**
   * モデル
   */
  private ClientModel model;
  
  /**
   * コンストラクタ
   * @param listModel タイムラインのlistModel
   * @param model モデル
   */
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
  
  /**
   * メニュー選択時の処理
   */
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
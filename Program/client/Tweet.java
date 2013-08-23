package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * 1つのツイートを描画するJPanel
 * @author gekka9
 *
 */
public class Tweet extends JPanel{
  /**
   * 生成されたシリアルバージョンUID
   */
  private static final long serialVersionUID = 3505393704256155762L;
  /**
   * モデル
   */
  private ClientModel model;
  
  /**
   * アイコン描画のためのラベル
   */
  private JLabel iconLabel;
  /**
   * アイコン
   */
  private ImageIcon icon;
  /**
   * ツイートした人の名前
   */
  private JTextArea name;
  /**
   * ツイート本文
   */
  private JTextArea text; 
  /**
   * フッター
   */
  private JTextArea footer;
  /**
   * RTのためのボタン
   */
  private JButton rtButton;
  /**
   * リプライのためのボタン
   */
  private JButton reButton;
  /**
   * お気に入りのためのボタン
   */
  private JButton favButton;
  /**
   * 将来の拡張用のボタン
   */
  private JButton otherButton;
  /**
   * ボタンの幅と高さ
   */
  public static final int BUTTON_SIZE=32;
  /**
   * アイコンの幅と高さ
   */
  public static final int ICON_SIZE=60; 
  /**
   * コンソール(ボタンの集合)の幅と高さ
   */
  public static final int CONSOLE_SIZE=80;
  
  /**
   * コンストラクタ。まず空の入れ物だけを用意する
   */
  public Tweet(){
    super();
    GridBagLayout layout=new GridBagLayout();
    this.setLayout(layout);
    GridBagConstraints c=new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;  // 制約の記述
    c.anchor=GridBagConstraints.NORTHWEST;
    this.setBackground(Color.LIGHT_GRAY);
    
    //アイコン
    c.gridx=0;
    c.gridy=0;
    c.gridwidth=4;
    c.gridheight=4;
    icon=new ImageIcon();
    iconLabel = new JLabel(icon);
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
    c.insets = new Insets(3, 0, 0, 3);
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
    c.insets = new Insets(0, 0, 3, 3);
    text = new JTextArea();
    text.setEditable(false);
    text.setLineWrap(true);
    text.setForeground(Color.white);
    text.setBackground(Color.DARK_GRAY);
    layout.setConstraints(text, c);
    this.add(text);
    
    //フッタ
    c.gridx=4;
    c.gridy=5;
    c.gridwidth=12;
    c.gridheight=1;
    c.weightx=1;
    //c.weighty=1;
    c.insets = new Insets(0, 0, 3, 3);
    footer = new JTextArea();
    footer.setEditable(false);
    footer.setLineWrap(true);
    footer.setForeground(Color.black);
    footer.setBackground(Color.green);
    layout.setConstraints(footer, c);
    this.add(footer);
    
    
    c.gridx=16;
    c.gridy=0;
    c.gridwidth=4;
    c.gridheight=4;
    c.weightx=0;
    c.weighty=0;
    c.insets = new Insets(3, 0, 0, 0);
    c.fill=GridBagConstraints.NONE;
    JPanel console = new JPanel();
    console.setMaximumSize(new Dimension(CONSOLE_SIZE,CONSOLE_SIZE));
    console.setLayout(new GridLayout(2,2));
    layout.setConstraints(console, c);
    this.add(console);

    rtButton = new JButton(new ImageIcon("./gui/rt.png") );
    rtButton.setEnabled(true);
    rtButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    rtButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    rtButton.addActionListener(new ReTweet());
    console.add(rtButton);

    reButton = new JButton(new ImageIcon("./gui/re.png") );
    reButton.setEnabled(true);
    reButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    reButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    reButton.addActionListener(new Reply());
    console.add(reButton);
    

    favButton = new JButton(new ImageIcon("./gui/fav.png") );
    favButton.setEnabled(true);
    favButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    favButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    favButton.addActionListener(new Favorite());
    console.add(favButton);
    
    otherButton = new JButton(new ImageIcon("./gui/other.png") );
    otherButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    otherButton.setMaximumSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
    console.add(otherButton);  
  }
  
  /**
   * 空の領域に値を実際に入れていく
   * @param status つぶやきのStatusインスタンス
   * @param text つぶやきの本文
   * @param model モデル
   * @param image アイコン画像
   * @param isSelect 現在選択されているかどうか
   */
  public void setValues(Status status,String text, ClientModel model,ImageIcon image,boolean isSelect){
    this.icon=image;
    this.iconLabel.setIcon(this.icon);
    this.name.setText(status.getUser().getName());
    this.text.setText(text);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.JAPAN);
    this.footer.setText(" at:"+sdf.format(status.getCreatedAt()));
    ActionListener[] listeners= this.reButton.getActionListeners();
    Reply reply = (Reply) listeners[0];
    reply.setStatus(status, model.getField());
    
    listeners= this.rtButton.getActionListeners();
    ReTweet reTweet = (ReTweet) listeners[0];
    reTweet.setStatus(status, model.getTwitter());
    
    listeners= this.favButton.getActionListeners();
    Favorite favorite = (Favorite) listeners[0];
    favorite.setStatus(status, model.getTwitter());
    if(isSelect){
      this.setBackground(Color.blue);
    }else{
      this.setBackground(Color.lightGray);
    }
  }

  /**
   * ゲッター
   * @return
   */
  public ClientModel getClientModel() {
    return this.model;
  }
  /**
   * リプライのためのリスナー
   * @author gekka9
   *
   */
  class Reply implements ActionListener{
    private Status status;
    private PostField field;
    public Reply(){
      super();
    }
    public void setStatus(Status status,PostField field){
      this.field=field;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      this.field.setText("@"+status.getUser().getScreenName()+" "+this.field.getText());
      this.field.setDestination(this.status);
    }
  }
  
  /**
   *RTのためのリスナー
   * @author gekka9
   *
   */
  class ReTweet implements ActionListener{
    private Status status;
    private Twitter twitter;
    public ReTweet(){
      super();
    }
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isRetweetedByMe()){
        try {
          this.twitter.retweetStatus(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      };
    }
  }
  
  /**
   * お気に入りのためのリスナー
   * @author gekka9
   *
   */
  class Favorite implements ActionListener{
    private Status status;
    private Twitter twitter;
    
    public Favorite(){
      super();
    }
    
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isFavorited()){
        try {
          this.twitter.createFavorite(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          try {
            this.twitter.destroyFavorite(this.status.getId());
          } catch (TwitterException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          e.printStackTrace();
        }
      }
    }
  }

}

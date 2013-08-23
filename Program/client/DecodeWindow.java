package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import enigma.CharCheck;
import enigma.Enigma;
import enigma.Enigma.CharacterType;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * 任意のツイートを復号化するためのJFrame
 * @author gekka9
 *
 */
public class DecodeWindow extends JFrame{
  /**
   * 生成されたシリアルバージョンUID
   */
  private static final long serialVersionUID = -5083336928366012763L;
  
  /**
   * Modelを束縛する
   */
  private ClientModel model;
  
  /**
   * 復号化されたツイートを表示するための領域を束縛する
   */
  private Tweet tweet;
  
  /**
   * ひらがなを復号化するための装置のインスタンス
   */
  private Enigma hiraganaEnigma;
  
  /**
   * カタカナを復号化するための装置のインスタンス
   */
  private Enigma katakanaEnigma;
  
  /**
   * 漢字を復号化するための装置のインスタンス
   */
  private Enigma kanjiEnigma;
  
  /**
   * アルファベットを復号化するための装置のインスタンス
   */
  private Enigma alphabetEnigma;
  
  /**
   * 数字を復号化するための装置のインスタンス
   */
  private Enigma numberEnigma;
  
  /**
   * 復号化するツイートのIDを入力するためのフィールド
   */
  private JTextField field;

  /**
   * コンストラクタ
   * @param model モデル
   */
  public DecodeWindow(ClientModel model){
    this.model = model;
    this.hiraganaEnigma = new Enigma(CharacterType.HIRAGANA);
    this.katakanaEnigma = new Enigma(CharacterType.KATAKANA);
    this.kanjiEnigma = new Enigma(CharacterType.KANJI);
    this.alphabetEnigma = new Enigma(CharacterType.ALPHABET);
    this.numberEnigma = new Enigma(CharacterType.NUMBER);
    
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    JTextArea notice = new JTextArea();
    notice.setText("任意のツイートのIDを入力してください。"+System.getProperty("line.separator")+"https://twitter.com/gekka9/status/368681471487143936"+System.getProperty("line.separator")+"なら368681471487143936です。");
    notice.setEditable(false);
    notice.setLineWrap(true);
    this.add(notice);
    this.field = new JTextField();
    this.field.setColumns(50);
    this.add(this.field);
    this.tweet = new Tweet();
    tweet.setVisible(false);
    this.add(tweet);
    this.pack();
    this.field.addKeyListener(new DecodeListener(this));
    this.setVisible(true);
  }

  /**
   * 投稿フィールドのインスタンスを返す
   * @return 投稿フィールドのインスタンス
   */
  public JTextField getField(){
    return this.field;
  }
  
  /**
   * 復号化して表示する
   * @param targetID 復号化対象のツイートのID
   */
  public void showDecode(long targetID){
    Status status;
    Twitter twitter = this.model.getTwitter();
    try {
      status =  twitter.showStatus(targetID);
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
      String[] date = sdf.format(status.getCreatedAt()).split("/");
      this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
      this.tweet.setValues(status, decode1(status), model, TweetRenderer.createIcon(status.getUser().getProfileImageURL()), false);
      this.tweet.setVisible(true);
      this.pack();
      System.out.println(status.getText());
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TwitterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * 復号器の初期化
   * @param seed シード値
   * @param offset ロータの初期位置
   */
  private void initialize(long seed,int offset){
    Random random = new Random(seed);
    this.alphabetEnigma.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
    this.hiraganaEnigma.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
    this.kanjiEnigma.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
    this.katakanaEnigma.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
    this.numberEnigma.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
  }
  
  /**
   * つぶやきのStatusインスタンスを受け取り、復号化後の本文を返す
   * @param status 復号化対象のStatusインスタンス
   * @return 復号化後の本文
   */
  private String decode1(Status status){
    //初期化
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
    String[] date = sdf.format(status.getCreatedAt()).split("/");
    this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
    
    //スペースで分割
    String[] targetList = status.getText().split(" ");
    ArrayList<String> resultList=new ArrayList<String>();
    //もし@が入ってなければ復号化
    int count=0;
    for(String aString:targetList){
      if(count != 0){
        resultList.add(" ");
      }
      if(!aString.contains("@") && !aString.contains("#")&& !aString.contains("http")){
        resultList.add(decode(aString));
      }else{
        resultList.add(aString);
      }
    }
    String resultText = "";
    for(String aString : resultList){
      if(count != 0){
        resultText+=" ";
      }
      count++;
      resultText += aString;
    }
    return resultText;
  }
  
  /**
   * 文字列を受け取り、復号化して返す
   * @param target 対象の文字列
   * @return 復号化後の文字列
   */
  private String decode(String target){
    char[] targetArray = target.toCharArray();
    StringBuilder builder = new StringBuilder();
    String result="";
    for(char aChar :targetArray){
      CharacterType mode = CharCheck.check(aChar);
      switch(mode){
      case ALPHABET : 
        result = this.alphabetEnigma.decrypt(String.valueOf(aChar));
        break;
      case HIRAGANA :
        result = this.hiraganaEnigma.decrypt(String.valueOf(aChar));
        break;
      case KATAKANA :
        result = this.katakanaEnigma.decrypt(String.valueOf(aChar));
        break;
      case KANJI :
        result = this.kanjiEnigma.decrypt(String.valueOf(aChar));
        break;
      case NUMBER :
        result = this.numberEnigma.decrypt(String.valueOf(aChar));
        break;
      default:
        result = String.valueOf(aChar);
      }
      builder.append(result);
    }
    return builder.toString();
  }
}

/**
 * 復号化対象のIDを入力するためのフォームのコントローラ
 * @author otaki
 *
 */
class DecodeListener implements KeyListener{

  /**
   * 入力フォームの親フレームを束縛する
   */
  private DecodeWindow window;
  
  /**
   * コンストラクタ
   * @param window 入力フォームの親フレーム
   */
  public DecodeListener(DecodeWindow window){
    this.window=window;
  } 

  /**
   * エンターキーが押されたらフォームに入力されたIDを対象に復号化を依頼する
   */
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_ENTER){
        this.window.showDecode(Long.parseLong(this.window.getField().getText()));
        this.window.getField().setText("");
    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

}


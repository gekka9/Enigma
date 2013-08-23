package client;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import client.ClientModel.Mode;

import enigma.CharCheck;
import enigma.Enigma;
import enigma.Enigma.CharacterType;

import twitter4j.User;

/**
 * つぶやきを描画するレンダラー
 * @author gekka9
 *
 */
public class TweetRenderer extends MouseAdapter implements ListCellRenderer {
  /**
   * Tweetインスタンスを束縛する
   */
  private Tweet tweet;
  /**
   * 取得済みアイコンを束縛するMap
   */
  private HashMap<User,ImageIcon> imageMap;
  /**
   * アイコンのサイズ
   */
  private static int SIZE=48;
  
  /**
   * ひらがなのための復号器
   */
  private Enigma hiraganaEnigma;
  /**
   *カタカナのための復号器
   */
  private Enigma katakanaEnigma;
  /**
   * 漢字のための復号器
   */
  private Enigma kanjiEnigma;
  /**
   * アルファベットのための復号器
   */
  private Enigma alphabetEnigma;
  /**
   * 数字のための復号器
   */
  private Enigma numberEnigma;

  /**
   * クリックされたつぶやきのインデックス
   */
  public int pressedIndex;
  public JButton button;
  /**
   * 現在のモード
   */
  public ClientModel.Mode mode;
  
  /**
   * コンストラクタ
   * @param mode 実行モード
   */
  public TweetRenderer(Mode mode){
    this.mode = mode;
    this.tweet = new Tweet();
    this.imageMap=new HashMap<User,ImageIcon>();
    this.hiraganaEnigma = new Enigma(CharacterType.HIRAGANA);
    this.katakanaEnigma = new Enigma(CharacterType.KATAKANA);
    this.kanjiEnigma = new Enigma(CharacterType.KANJI);
    this.alphabetEnigma = new Enigma(CharacterType.ALPHABET);
    this.numberEnigma = new Enigma(CharacterType.NUMBER);
  }
  
  /**
   * 描画のための処理
   */
  @Override
  public Component getListCellRendererComponent(JList list, Object data,int index, boolean isSelected, boolean cellHasFocus) {
    TweetData tweetData = (TweetData) data;
      ImageIcon icon = this.imageMap.get(tweetData.status.getUser());
      if(icon == null){
        icon = TweetRenderer.createIcon(tweetData.status.getUser().getProfileImageURL());
        this.imageMap.put(tweetData.status.getUser(), icon);
      }
      String resultText;
      //もしこのクライアントなら
      if(TweetRenderer.source2clientName(tweetData.status.getSource()).equals(ClientModel.CLIENT_NAME) && this.mode!=Mode.NO_ENCRYPT ){
        //初期化
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String[] date = sdf.format(tweetData.status.getCreatedAt()).split("/");
        this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
        
        //スペースで分割
        String[] targetList = tweetData.status.getText().split(" ");
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
        resultText = "";
        for(String aString : resultList){
          if(count != 0){
            resultText+=" ";
          }
          count++;
          resultText += aString;
        }
      }else{
        resultText = tweetData.status.getText();
      }
      this.tweet.setValues(tweetData.status,resultText,tweetData.model,icon,isSelected);
     
    return this.tweet;
  }
  
  /**
   * 画像を取得してアイコンを生成
   * @param urlString アイコン画像のURL
   * @return 生成したアイコン
   */
  public static ImageIcon createIcon(String urlString){
    Image image;
    try {
      image = ImageIO.read(new URL(urlString));
      if(image.getWidth(null)!=SIZE && image.getHeight(null) !=SIZE){
        //System.out.println("scaling");
        image = image.getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT);
      }
      ImageIcon icon = new ImageIcon(image);
      return icon;
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * XML形式の文字列からクライアント名を抜き出す
   * @param target 対象の文字列
   * @return 抜き出されたクライアント名
   */
  public static String source2clientName(String target){
    StringBuilder builder = new StringBuilder();
    char[] targetArray = target.toCharArray();
    boolean isInClientName=false;
    for(char aChar:targetArray){
      if(isInClientName){
        if(isInClientName && aChar == '<'){
          break;
        }else{
          builder.append(aChar);
        }
      }
      if(aChar == '>'){
        isInClientName=true;
      }
    }
    return builder.toString();
  }
  /**
   * 復号器の初期化
   * @param seed シード値
   * @param offset
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
   * 文字列を受け取り、復号化して返す
   * @param target 復号化対象の文字列
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

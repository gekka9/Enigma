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

public class TweetRenderer extends MouseAdapter implements ListCellRenderer {
  private Tweet tweet;
  private HashMap<User,ImageIcon> imageMap;
  private static int SIZE=48;
  private Enigma hiraganaEnigma;
  private Enigma katakanaEnigma;
  private Enigma kanjiEnigma;
  private Enigma alphabetEnigma;
  private Enigma numberEnigma;
  public int pressedIndex;
  public JButton button;
  public ClientModel.Mode mode;
  
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
  
  @Override
  public Component getListCellRendererComponent(JList list, Object data,int index, boolean isSelected, boolean cellHasFocus) {
    TweetData tweetData = (TweetData) data;
      ImageIcon icon = this.imageMap.get(tweetData.status.getUser());
      if(icon == null){
        icon = this.createIcon(tweetData.status.getUser().getProfileImageURL());
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
  
  private ImageIcon createIcon(String urlString){
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
  private void initialize(long seed,int offset){
    Random random = new Random(seed);
    this.alphabetEnigma.initialize(random.nextLong(), offset);
    this.hiraganaEnigma.initialize(random.nextLong(), offset);
    this.kanjiEnigma.initialize(random.nextLong(), offset);
    this.katakanaEnigma.initialize(random.nextLong(), offset);
    this.numberEnigma.initialize(random.nextLong(), offset);
  }
  
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

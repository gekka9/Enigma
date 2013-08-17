package client;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextField;

import enigma.CharCheck;
import enigma.Enigma;
import enigma.Enigma.CharacterType;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class DecodeWindow extends JFrame{
  private ClientModel model;
  private Tweet tweet;
  private Enigma hiraganaEnigma;
  private Enigma katakanaEnigma;
  private Enigma kanjiEnigma;
  private Enigma alphabetEnigma;
  private Enigma numberEnigma;
  private JTextField field;

  
  public DecodeWindow(ClientModel model){
    this.model = model;
    this.hiraganaEnigma = new Enigma(CharacterType.HIRAGANA);
    this.katakanaEnigma = new Enigma(CharacterType.KATAKANA);
    this.kanjiEnigma = new Enigma(CharacterType.KANJI);
    this.alphabetEnigma = new Enigma(CharacterType.ALPHABET);
    this.numberEnigma = new Enigma(CharacterType.NUMBER);
    
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
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

  public JTextField getField(){
    return this.field;
  }
  
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
  
  private void initialize(long seed,int offset){
    Random random = new Random(seed);
    this.alphabetEnigma.initialize(random.nextLong(), offset);
    this.hiraganaEnigma.initialize(random.nextLong(), offset);
    this.kanjiEnigma.initialize(random.nextLong(), offset);
    this.katakanaEnigma.initialize(random.nextLong(), offset);
    this.numberEnigma.initialize(random.nextLong(), offset);
  }
  
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

class DecodeListener implements KeyListener{

  private DecodeWindow window;
  
  public DecodeListener(DecodeWindow window){
    this.window=window;
  } 

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    int mod = e.getModifiersEx();
    if ((mod & InputEvent.META_DOWN_MASK) != 0 && key == KeyEvent.VK_ENTER){
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


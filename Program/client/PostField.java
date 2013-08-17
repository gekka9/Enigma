package client;

import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JTextArea;

import client.ClientModel.Mode;

import enigma.Enigma;
import enigma.Enigma.CharacterType;
import enigma.CharCheck;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostField extends JTextArea{

  private static final long serialVersionUID = 1L;
  private Twitter twitter;
  private static final int WIDTH=400;
  private static final int HEIGHT=80;
  private static final int POST_LENGTH=80;
  private Enigma hiraganaEnigma;
  private Enigma katakanaEnigma;
  private Enigma kanjiEnigma;
  private Enigma alphabetEnigma;
  private Enigma numberEnigma;
  private ClientModel model;
  private long destTweet;
  private String destUser;
  
  public PostField(ClientModel model){
    this.model=model;
    this.hiraganaEnigma = new Enigma(CharacterType.HIRAGANA);
    this.katakanaEnigma = new Enigma(CharacterType.KATAKANA);
    this.kanjiEnigma = new Enigma(CharacterType.KANJI);
    this.alphabetEnigma = new Enigma(CharacterType.ALPHABET);
    this.numberEnigma = new Enigma(CharacterType.NUMBER);
    this.twitter=model.getTwitter();
    this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
    this.setLineWrap(true);
    this.addKeyListener(new Poster(this));
  }

  public void post() throws IOException{
    if(this.getText().length() <POST_LENGTH && 0<this.getText().length() ){
      String postText = this.getText();
      boolean isReply=false;
      if (postText.indexOf("@"+this.destUser) != -1) {
          isReply = true;
      }
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
      String[] date = sdf.format(new Date()).split("/");
      this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
      String[] targetList = postText.split(" ");
      String resultText="";
      if(!(this.model.getMode() == Mode.NO_ENCRYPT)){
        ArrayList<String> resultList=new ArrayList<String>();
        //もし@が入ってなければ暗号化
        int count=0;
        for(String aString:targetList){
          if(!aString.contains("@") && !aString.contains("#")&& !aString.contains("http")){ 
            resultList.add(encode(aString));
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
        resultText=postText;
      }
      try {
        if(!isReply){
          this.twitter.updateStatus(resultText);
        }else{
          this.twitter.updateStatus(new StatusUpdate(resultText).inReplyToStatusId(this.destTweet));
        }
      } catch (TwitterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      this.destTweet=0;
      this.destUser=null;
      this.setText("");
    }
  }
  
  private void initialize(long seed,int offset) throws IOException{
    Random random = new Random(seed);
    this.alphabetEnigma.initialize(random.nextLong(), offset);
    this.hiraganaEnigma.initialize(random.nextLong(), offset);
    this.kanjiEnigma.initialize(random.nextLong(), offset);
    this.katakanaEnigma.initialize(random.nextLong(), offset);
    this.numberEnigma.initialize(random.nextLong(), offset);
  }
  
  public void setDestination(Status status){
    this.destTweet=status.getId();
    this.destUser = status.getUser().getScreenName();
  }
  
  private String encode(String target){
    char[] targetArray = target.toCharArray();
    String result="";
    StringBuilder builder = new StringBuilder();
    for(char aChar :targetArray){
      CharacterType mode = CharCheck.check(aChar);
      switch(mode){
      case ALPHABET : 
        result = this.alphabetEnigma.encrypt(String.valueOf(aChar));
        break;
      case HIRAGANA :
        result = this.hiraganaEnigma.encrypt(String.valueOf(aChar));
        break;
      case KATAKANA :
        result = this.katakanaEnigma.encrypt(String.valueOf(aChar));
        break;
      case KANJI :
        result = this.kanjiEnigma.encrypt(String.valueOf(aChar));
        break;
      case NUMBER :
        result = this.numberEnigma.encrypt(String.valueOf(aChar));
        break;
      default:
        result = String.valueOf(aChar);
      }
      builder.append(result);
    }
    return builder.toString();
  }
}

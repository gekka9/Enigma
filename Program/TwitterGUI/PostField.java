package TwitterGUI;

import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextArea;

import enigma.Enigma;
import enigma.Enigma.Mode;
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
  private long destTweet;
  private String destUser;
  
  public PostField(Twitter twitter){
    this.hiraganaEnigma = new Enigma(Mode.HIRAGANA);
    this.katakanaEnigma = new Enigma(Mode.KATAKANA);
    this.kanjiEnigma = new Enigma(Mode.KANJI);
    this.alphabetEnigma = new Enigma(Mode.ALPHABET);
    this.numberEnigma = new Enigma(Mode.NUMBER);
    this.twitter=twitter;
    this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
    this.setLineWrap(true);
    this.addKeyListener(new Poster(this));
  }

  public void post() throws IOException{
    if(this.getText().length() <POST_LENGTH && 0<this.getText().length() ){
      String postText = this.getText();
      System.out.println("post: " +postText);
      boolean isReply=false;
      if (postText.indexOf("@"+this.destUser) != -1) {
          isReply = true;
      }
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String[] date = sdf.format(new Date()).split("/");
        System.out.println(date[0]+" : "+date[1]);
        this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
        String[] targetList = postText.split(" ");
        ArrayList<String> resultList=new ArrayList<String>();
        String resultText;
        //もし@が入ってなければ暗号化
        int count=0;
        for(String aString:targetList){
          StringBuilder builder = new StringBuilder();
          if(!aString.contains("@")){
            char[] targetArray = aString.toCharArray();
            String result="";
            for(char aChar :targetArray){
              Mode mode = CharCheck.check(aChar);
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
            resultList.add(builder.toString());
            builder = new StringBuilder();
          }else{
            resultList.add(aString);
          }
        }
        resultText = "";
        for(String aString : resultList){
          System.out.println("fnaoiegneql/k ntaeithbarlgna/egfhea:oh"+aString);
          if(count != 0){
            resultText+=" ";
          }
          count++;
          resultText += aString;
        }
        if(!isReply){
          this.twitter.updateStatus(resultText);
        }else{
          this.twitter.updateStatus(new StatusUpdate(resultText).inReplyToStatusId(this.destTweet));
        }
        this.destTweet=0;
        this.destUser=null;
      } catch (TwitterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      this.setText("");
    }
  }
  
  private void initialize(long seed,int offset) throws IOException{
    this.alphabetEnigma.initialize(seed, offset);
    this.hiraganaEnigma.initialize(seed, offset);
    this.kanjiEnigma.initialize(seed, offset);
    this.katakanaEnigma.initialize(seed, offset);
    this.numberEnigma.initialize(seed, offset);
  }
  
  public void setDestination(Status status){
    this.destTweet=status.getId();
    this.destUser = status.getUser().getScreenName();
  }
  
}

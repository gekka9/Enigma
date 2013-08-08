package TwitterGUI;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import enigma.Enigma;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostField extends JTextArea{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Twitter twiter;
  private static final int WIDTH=400;
  private static final int HEIGHT=80;
  private static final int POST_LENGTH=80;
  private Enigma enigma;
  
  
  public PostField(Twitter twitter){
    this.enigma = new Enigma();
    this.twiter=twitter;
    this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
    this.setLineWrap(true);
    this.addKeyListener(new Poster(this));
  }

  public void post() throws IOException{
    if(this.getText().length() <POST_LENGTH && 0<this.getText().length() ){
      String postText = this.getText();
      System.out.println("post: " +postText);
      try {
        this.enigma.initialize(0, 5);
        String[] targetList = postText.split(" ");
        ArrayList<String> resultList=new ArrayList<String>();
        String resultText;
        //もし@が入ってなければ復号化
        int count=0;
        for(String aString:targetList){
          if(!aString.contains("@")){
            String result = enigma.encrypt(aString);
            resultList.add(result);
          }else{
            resultList.add(aString);
          }
        }
        resultText = "";
        for(String aString : resultList){
          if(count != 0){
            resultText+=" ";
          }
          resultText += aString;
        }
        this.twiter.updateStatus(resultText);
      } catch (TwitterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      this.setText("");
    }
  }
  
}

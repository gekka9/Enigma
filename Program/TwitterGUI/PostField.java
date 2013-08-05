package TwitterGUI;

import java.awt.Dimension;

import javax.swing.JTextArea;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostField extends JTextArea{

  private Twitter twiter;
  
  public PostField(Twitter twitter){
    this.twiter=twitter;
    this.setPreferredSize(new Dimension(400,80));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
    this.setLineWrap(true);
    this.addKeyListener(new Poster(this));
  }

  public void post(){
    if(this.getText().length() <140 && 0<this.getText().length() ){
      System.out.println("post: " +this.getText());
      try {
        this.twiter.updateStatus(this.getText());
      } catch (TwitterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      this.setText("");
    }
  }
  
}

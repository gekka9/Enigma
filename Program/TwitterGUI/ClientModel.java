package TwitterGUI;

import twitter4j.Status;
import twitter4j.Twitter;

public class ClientModel {
  private Twitter twitter;
  private MainFrame frame;

  public ClientModel(Twitter twitter){
    this.setTwitter(twitter);
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public void setTwitter(Twitter twitter) {
    this.twitter = twitter;
  }
  
  public void addPost(Status status){
    this.frame.getPostBox().add(new Tweet(status,this),0);
    if(this.frame.getPostBox().getComponentCount()>75){
      this.frame.getPostBox().remove(75);
    };
    this.frame.setVisible(true);
  }
  
  public void clear(){
    this.frame.getPostBox().removeAll();
  }

  public MainFrame getFrame() {
    return frame;
  }

  public void setFrame(MainFrame frame) {
    this.frame = frame;
  }

}

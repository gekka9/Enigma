package client;

import twitter4j.Twitter;

public class ClientModel {
  private Twitter twitter;
  private MainFrame frame;
  private PostField field;

  public ClientModel(Twitter twitter){
    this.setTwitter(twitter);
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public void setTwitter(Twitter twitter) {
    this.twitter = twitter;
  }
  public MainFrame getFrame() {
    return frame;
  }

  public void setFrame(MainFrame frame) {
    this.frame = frame;
  }

  public void setField(PostField field) {
    this.field=field;
  }
  public PostField getField(){
    return this.field;
  }

}

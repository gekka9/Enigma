package TwitterGUI;

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
  public MainFrame getFrame() {
    return frame;
  }

  public void setFrame(MainFrame frame) {
    this.frame = frame;
  }

}

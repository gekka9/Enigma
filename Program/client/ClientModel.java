package client;

import twitter4j.Status;
import twitter4j.Twitter;

public class ClientModel {
  private Twitter twitter;
  private MainFrame frame;
  private PostField field;
  private Mode mode;
  public final static String CLIENT_NAME="enigma9";
  public enum Mode {NORMAL,NO_ENCRYPT,VIEW_LIMIT};

  public ClientModel(Twitter twitter,Mode mode){
    this.twitter=twitter;
    this.mode=mode;
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public void setTwitter(Twitter twitter) {
    this.twitter = twitter;
  }

  public void setField(PostField field) {
    this.field=field;
  }
  public PostField getField(){
    return this.field;
  }
  
  public void setFrame(MainFrame frame) {
    this.frame=frame;
  }
  
  public void post(Status status){
    if(this.mode==Mode.VIEW_LIMIT){
      if(TweetRenderer.source2clientName(status.getSource()).equals(ClientModel.CLIENT_NAME)){
        this.frame.addPost(status);
      }else{
        return;
      }
    }else{
      this.frame.addPost(status);
    }
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

}

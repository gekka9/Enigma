package client;

import twitter4j.Status;

public class TweetData {
  public Status status;
  public ClientModel model;
  
  public TweetData(Status status , ClientModel model){
    this.status=status;
    this.model=model;
  }

}

package TwitterGUI;

import twitter4j.Status;
import twitter4j.Twitter;

public class TweetData {
  public Status status;
  public ClientModel model;
  
  public TweetData(Status status , ClientModel model){
    this.status=status;
    this.model=model;
  }

}

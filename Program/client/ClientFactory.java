package client;

import client.ClientModel.Mode;
import twitter4j.Twitter;

public class ClientFactory {

  private ClientModel model;
  private MainFrame frame;
  
  public ClientFactory(Twitter twitter, Mode mode){
    this.model=new ClientModel(twitter,mode);
    this.frame = new MainFrame(this.model);
    this.model.setFrame(this.frame);
  }
  
  public ClientModel getClientModel(){
    return this.model;
  }

}

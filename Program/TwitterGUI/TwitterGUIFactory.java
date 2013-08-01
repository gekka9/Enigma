package TwitterGUI;

import twitter4j.Twitter;

public class TwitterGUIFactory {

  private ClientModel model;
  private MainFrame frame;
  
  public TwitterGUIFactory(Twitter twitter){
    this.model=new ClientModel(twitter);
    this.frame = new MainFrame(this.model);
    this.model.setFrame(frame);
  }
  
  public ClientModel getClientModel(){
    return this.model;
  }

}

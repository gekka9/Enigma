package TwitterGUI;

import java.awt.Component;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class TweetRenderer implements ListCellRenderer {
  private Tweet tweet;
  
  public TweetRenderer(){
    tweet = new Tweet();
  }
  
  @Override
  public Component getListCellRendererComponent(JList list, Object data,
      int index, boolean isSelected, boolean cellHasFocus) {
    TweetData tweetData = (TweetData) data;
    try {
      this.tweet.setValues(tweetData.status,tweetData.model,new ImageIcon(new URL(tweetData.status.getUser().getProfileImageURL())));
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this.tweet;
  }

}

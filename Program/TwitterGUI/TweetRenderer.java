package TwitterGUI;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import twitter4j.User;

public class TweetRenderer implements ListCellRenderer {
  private Tweet tweet;
  private HashMap<User,ImageIcon> imageMap;
  
  public TweetRenderer(){
    tweet = new Tweet();
    this.imageMap=new HashMap<User,ImageIcon>();
  }
  
  @Override
  public Component getListCellRendererComponent(JList list, Object data,
      int index, boolean isSelected, boolean cellHasFocus) {
    TweetData tweetData = (TweetData) data;
    try {
      ImageIcon icon = this.imageMap.get(tweetData.status.getUser());
      if(icon == null){
        System.out.println("new");
        Image image = ImageIO.read(new URL(tweetData.status.getUser().getProfileImageURL()));
        if(image.getWidth(null)!=48 && image.getHeight(null) !=48){
          System.out.println("scaling");
          image = image.getScaledInstance(48, 48, Image.SCALE_DEFAULT);
        }
        icon = new ImageIcon(image);
        this.imageMap.put(tweetData.status.getUser(), icon);
      }
      this.tweet.setValues(tweetData.status,tweetData.model,icon,isSelected);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this.tweet;
  }

}

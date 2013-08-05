package TwitterGUI;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
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
  private static int SIZE=48;
  
  public TweetRenderer(){
    this.tweet = new Tweet();
    this.imageMap=new HashMap<User,ImageIcon>();
  }
  
  @Override
  public Component getListCellRendererComponent(JList list, Object data,
      int index, boolean isSelected, boolean cellHasFocus) {
    TweetData tweetData = (TweetData) data;
      ImageIcon icon = this.imageMap.get(tweetData.status.getUser());
      if(icon == null){
        try {
          icon = this.createIcon(tweetData.status.getUser().getProfileImageURL());
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        this.imageMap.put(tweetData.status.getUser(), icon);
      }
      this.tweet.setValues(tweetData.status,tweetData.model,icon,isSelected);
    return this.tweet;
  }
  
  private ImageIcon createIcon(String urlString) throws IOException{
    Image image = ImageIO.read(new URL(urlString));
    if(image.getWidth(null)!=SIZE && image.getHeight(null) !=SIZE){
      //System.out.println("scaling");
      image = image.getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT);
    }
    ImageIcon icon = new ImageIcon(image);
    return icon;
  }

}

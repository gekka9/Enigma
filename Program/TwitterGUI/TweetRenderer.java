package TwitterGUI;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import enigma.Enigma;

import twitter4j.User;

public class TweetRenderer implements ListCellRenderer {
  private Tweet tweet;
  private HashMap<User,ImageIcon> imageMap;
  private static int SIZE=48;
  private Enigma enigma;
  private final String CLIENT_NAME="gekkaTestClient";
  
  public TweetRenderer(){
    this.tweet = new Tweet();
    this.imageMap=new HashMap<User,ImageIcon>();
    this.enigma=new Enigma();
  }
  
  @Override
  public Component getListCellRendererComponent(JList list, Object data,int index, boolean isSelected, boolean cellHasFocus) {
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
      String resultText;
      //もしこのクライアントなら
      if(this.source2clientName(tweetData.status.getSource()).equals(CLIENT_NAME) ){
        System.out.println("Clienttest");
        try {
          //初期化
          enigma.initialize(0, 5);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        String[] targetList = tweetData.status.getText().split(" ");
        ArrayList<String> resultList=new ArrayList<String>();
        //もし@が入ってなければ復号化
        int count=0;
        for(String aString:targetList){
          if(count != 0){
            resultList.add(" ");
          }
          if(!aString.contains("@")){
            String result = enigma.decrypt(aString);
            resultList.add(result);
          }else{
            resultList.add(aString);
          }
        }
        resultText = "";
        for(String aString : resultList){
          resultText += aString;
        }
      }else{
        resultText = tweetData.status.getText();
      }
      this.tweet.setValues(tweetData.status,resultText,tweetData.model,icon,isSelected);
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
  private String source2clientName(String target){
    StringBuilder builder = new StringBuilder();
    char[] targetArray = target.toCharArray();
    boolean isInClientName=false;
    for(char aChar:targetArray){
      if(isInClientName){
        if(isInClientName && aChar == '<'){
          break;
        }else{
          builder.append(aChar);
        }
      }
      if(aChar == '>'){
        isInClientName=true;
      }
    }
    return builder.toString();
  }

}

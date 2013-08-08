package TwitterGUI;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import enigma.CharCheck;
import enigma.Enigma;
import enigma.Enigma.Mode;

import twitter4j.User;

public class TweetRenderer extends MouseAdapter implements ListCellRenderer {
  private Tweet tweet;
  private HashMap<User,ImageIcon> imageMap;
  private static int SIZE=48;
  private final String CLIENT_NAME="gekkaTestClient";
  private Enigma hiraganaEnigma;
  private Enigma katakanaEnigma;
  private Enigma kanjiEnigma;
  private Enigma alphabetEnigma;
  private Enigma numberEnigma;
  public int pressedIndex;
  public JButton button;
  
  public TweetRenderer(){
    this.tweet = new Tweet();
    this.imageMap=new HashMap<User,ImageIcon>();
    this.hiraganaEnigma = new Enigma(Mode.HIRAGANA);
    this.katakanaEnigma = new Enigma(Mode.KATAKANA);
    this.kanjiEnigma = new Enigma(Mode.KANJI);
    this.alphabetEnigma = new Enigma(Mode.ALPHABET);
    this.numberEnigma = new Enigma(Mode.NUMBER);
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
      if(TweetRenderer.source2clientName(tweetData.status.getSource()).equals(CLIENT_NAME) ){
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
          String[] date = sdf.format(tweetData.status.getCreatedAt()).split("/");
          System.out.println(date[0]+" : "+date[1]);
          this.initialize(Long.parseLong(date[0]), Integer.parseInt(date[1]));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        String[] targetList = tweetData.status.getText().split(" ");
        ArrayList<String> resultList=new ArrayList<String>();
        //もし@が入ってなければ復号化
        int count=0;
        for(String aString:targetList){
          StringBuilder builder = new StringBuilder();
          if(count != 0){
            resultList.add(" ");
          }
          if(!aString.contains("@")){
            char[] targetArray = aString.toCharArray();
            String result="";
            for(char aChar :targetArray){
              Mode mode = CharCheck.check(aChar);
              switch(mode){
              case ALPHABET : 
                result = this.alphabetEnigma.decrypt(String.valueOf(aChar));
                break;
              case HIRAGANA :
                result = this.hiraganaEnigma.decrypt(String.valueOf(aChar));
                break;
              case KATAKANA :
                result = this.katakanaEnigma.decrypt(String.valueOf(aChar));
                break;
              case KANJI :
                result = this.kanjiEnigma.decrypt(String.valueOf(aChar));
                break;
              case NUMBER :
                result = this.numberEnigma.decrypt(String.valueOf(aChar));
                break;
              default:
                result = String.valueOf(aChar);
              }
              builder.append(result);
            }
            resultList.add(builder.toString());
            builder = new StringBuilder();
          }else{
            resultList.add(aString);
          }
        }
        resultText = "";
        for(String aString : resultList){
          if(count != 0){
            resultText+=" ";
          }
          count++;
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
  public static String source2clientName(String target){
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
  private void initialize(long seed,int offset) throws IOException{
    this.alphabetEnigma.initialize(seed, offset);
    this.hiraganaEnigma.initialize(seed, offset);
    this.kanjiEnigma.initialize(seed, offset);
    this.katakanaEnigma.initialize(seed, offset);
    this.numberEnigma.initialize(seed, offset);
  }

}

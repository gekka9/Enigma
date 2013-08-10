package client;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.EnigmaToken;
import client.MainFrame;
import client.ClientFactory;


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Example
{
  public static final String KEY = "5EHIcwWrFiKDJ19l2ceoDQ";
  public static final String SECRET = "yCm7uy1dJMUHG2HRYvozy5iuQ9WEuegssmlBpv6I4";
  public static final String ACCESS_FILENAME="resource/access.obj"; 
  public static void main(String[] args) throws Exception{
    File newdir = new File("./resource");
    newdir.mkdir();
    Example stream = new Example();
    stream.startUserStream();
  }
  
  /*
   * UserStreamの開始
   */
  public String startUserStream()throws Exception
  {
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.setOAuthConsumerKey(KEY);
    builder.setOAuthConsumerSecret(SECRET);
    AccessToken accessToken=null;
    Configuration conf = builder.build();
    TwitterFactory factory = new TwitterFactory(conf);
    Twitter twitter= factory.getInstance(); 
    try{
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCESS_FILENAME));
      EnigmaToken enigmaToken = (EnigmaToken)ois.readObject();
      ois.close();
      accessToken = new AccessToken(enigmaToken.getToken(),enigmaToken.getSecret());
      //System.out.println(accessToken.getTokenSecret());
    }catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      RequestToken requestToken = twitter.getOAuthRequestToken();
      /*
      System.out.println("Open the following URL and grant access to your account:");
      System.out.println(requestToken.getAuthorizationURL());
      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
      String pin = br.readLine();
      */
      JFrame frame = new JFrame();
      JOptionPane.showMessageDialog(frame, "OKボタンを押すと、ブラウザで認証ページを開きます。\n認証後に表示されたPINコードを入力してください。");
      Desktop desktop = Desktop.getDesktop();
      String uriString = requestToken.getAuthorizationURL();
      try {
        URI uri = new URI(uriString);
        desktop.browse(uri);
      } catch (URISyntaxException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      String pin = JOptionPane.showInputDialog("PINコードを入力してください");
      if (pin == null){
        System.exit(1);
      }
      try{
         if(pin.length() > 0){
           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
         }else{
           accessToken = twitter.getOAuthAccessToken();
         }
         //将来の参照用に accessToken を永続化する
         storeAccessToken(accessToken);
      } catch (TwitterException te) {
        if(401 == te.getStatusCode()){
          System.out.println("Unable to get the access token.");
        }else{
          te.printStackTrace();
        }
      }
    }
    //twitter.setOAuthConsumer(KEY, SECRET);
    twitter.setOAuthAccessToken(accessToken);
    ClientFactory clientFactory= new ClientFactory(twitter,ClientModel.Mode.NORMAL);
    ClientModel model = clientFactory.getClientModel();
    List<Status> statuses = twitter.getHomeTimeline();
    for (Status status : statuses) {
      model.post(status);
    }
    TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
    TwitterStream twitterStream = twitterStreamFactory.getInstance();
    twitterStream.addListener(new MyUserStreamAdapter(model));
    twitterStream.setOAuthAccessToken(accessToken);
    twitterStream.user();
    
    return null;
  }
  
  private static void storeAccessToken(AccessToken accessToken){
    //accessToken.getToken() を保存
    //accessToken.getTokenSecret() を保存
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(ACCESS_FILENAME));
      EnigmaToken enigmaToken = new EnigmaToken(accessToken.getToken(),accessToken.getTokenSecret());
      oos.writeObject(enigmaToken);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

/**
 * UserStreamAdapter
 */
class MyUserStreamAdapter extends UserStreamAdapter
{

  private ClientModel model;
  private int count=0; 
  
  public MyUserStreamAdapter(ClientModel model){
    this.model=model;
  }
  
  /*
   * ツイートに対する反応
   */
  @Override
  public void onStatus(Status status)
  {
    super.onStatus(status);
    //logger.info(status.getText() + " : " + status.getUser().getScreenName());
    this.model.post(status);
    System.out.println(status.getText() + " : " + status.getUser().getScreenName()+" "+status.getUser().getName());
    //System.out.println(count);
    //count++;
  }
}
package enigma;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import TwitterGUI.ClientModel;
import TwitterGUI.MainFrame;
import TwitterGUI.TwitterGUIFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.logging.Logger;

public class Example
{
  public static final String KEY = "5EHIcwWrFiKDJ19l2ceoDQ";
  public static final String SECRET = "yCm7uy1dJMUHG2HRYvozy5iuQ9WEuegssmlBpv6I4";
  public static final String ACCESS_FILENAME="access.obj"; 
  private AccessToken accessToken;
  
  public static void main(String[] args) throws Exception{
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
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Configuration conf = builder.build();
    TwitterFactory factory = new TwitterFactory(conf);
    Twitter twitter= factory.getInstance(); 
    try{
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCESS_FILENAME));
      accessToken = (AccessToken)ois.readObject();
      //System.out.println(accessToken.getTokenSecret());
    }catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      RequestToken requestToken = twitter.getOAuthRequestToken();
      System.out.println("Open the following URL and grant access to your account:");
      System.out.println(requestToken.getAuthorizationURL());
      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
      String pin = br.readLine();
      try{
         if(pin.length() > 0){
           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
         }else{
           accessToken = twitter.getOAuthAccessToken();
         }
         //将来の参照用に accessToken を永続化する
         storeAccessToken(twitter.verifyCredentials().getId() ,accessToken);
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
    TwitterGUIFactory GUIfactory= new TwitterGUIFactory(twitter);
    MainFrame frame = GUIfactory.getMainFrame();
    List<Status> statuses = twitter.getHomeTimeline();
    for (Status status : statuses) {
        frame.addPost(status);
    }
    TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
    TwitterStream twitterStream = twitterStreamFactory.getInstance();
    twitterStream.addListener(new MyUserStreamAdapter(frame));
    twitterStream.setOAuthAccessToken(accessToken);
    twitterStream.user();
    
    return null;
  }
  
  private static void storeAccessToken(long userID,AccessToken accessToken){
    //accessToken.getToken() を保存
    //accessToken.getTokenSecret() を保存
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(ACCESS_FILENAME));
      oos.writeObject(accessToken);
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
  private static final Logger logger = Logger.getLogger(MyUserStreamAdapter.class);

  private MainFrame frame;
  private int count=0; 
  
  public MyUserStreamAdapter(MainFrame frame){
    this.frame=frame;
  }
  
  /*
   * ツイートに対する反応
   */
  @Override
  public void onStatus(Status status)
  {
    super.onStatus(status);
    
    //logger.info(status.getText() + " : " + status.getUser().getScreenName());
    this.frame.addPost(status);
    System.out.println(status.getText() + " : " + status.getUser().getScreenName()+" "+status.getUser().getName());
    System.out.println(count);
    count++;
  }
}
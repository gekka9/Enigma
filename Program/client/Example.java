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

import client.ClientModel.Mode;
import client.EnigmaToken;
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

/**
 * 起動用クラス。認証とユーザーストリームの接続を試みる
 * @author gekka9
 *
 */
public class Example
{
  public static final String KEY = "5EHIcwWrFiKDJ19l2ceoDQ";
  public static final String SECRET = "yCm7uy1dJMUHG2HRYvozy5iuQ9WEuegssmlBpv6I4";
  private static final Splash splash = new Splash();
  private Mode mode; 
  
  public static void main(String[] args) throws Exception{
    if(ClientModel.isMac()){
      System.setProperty("com.apple.mrj.application.apple.menu.about.name","Enigma");
      System.setProperty("apple.laf.useScreenMenuBar", "false");
    }

    splash.setVisible(true);
    
    Mode mode=Mode.NORMAL;
    if(args.length>0){
      if(args[0].equals("-h") || args[0].equals("-help")){
        showHelp();
        return;
      }else if(args[0].equals("-n") || args[0].equals("-noenceypt")){
        mode=Mode.NO_ENCRYPT;
        System.out.println("start-up in no encrypt mode");
      }else if(args[0].equals("-l") || args[0].equals("-limit")){
        mode=Mode.VIEW_LIMIT;
        System.out.println("start-up in view limited mode");
      }
    }else{
      System.out.println("start-up in normal mode");
    }
    
    File newdir = new File("resource");
    newdir.mkdir();
    Example stream = new Example(mode);
    stream.startUserStream();
  }
  
  public Example(Mode mode){
    this.mode=mode;
  }
  
  /**
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
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ClientModel.ACCESS_FILENAME));
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
    ClientFactory clientFactory= new ClientFactory(twitter,this.mode);
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
    model.show();
    splash.setVisible(false);
    return null;
  }
  
  /**
   * アクセストークンを永続化
   * @param accessToken 対象のアクセストークン
   */
  private static void storeAccessToken(AccessToken accessToken){
    //accessToken.getToken() を保存
    //accessToken.getTokenSecret() を保存
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(ClientModel.ACCESS_FILENAME));
      EnigmaToken enigmaToken = new EnigmaToken(accessToken.getToken(),accessToken.getTokenSecret());
      oos.writeObject(enigmaToken);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * ヘルプの表示
   */
  private static void showHelp(){
    String ln = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("java -jar enigma.jar [OPTIONS] <TARGETS...>").append(ln);
    sb.append(ln);
    sb.append("OPTIONS").append(ln);
    sb.append("  -n, -noenceypt:        NO encrypt and NO deceypt.").append(ln);
    sb.append("  -l, -l:                view ONLY posts tweeted from this client.").append(ln);
    sb.append("  -h, --help:            show this message.").append(ln);
    sb.append("  no input:              ececute in normal mode.").append(ln);
    System.out.println(sb.toString());
  }
}

/**
 * UserStreamAdapter
 */
class MyUserStreamAdapter extends UserStreamAdapter
{

  private ClientModel model;
  
  /**
   * コンストラクタ
   * @param model Modelインスタンス
   */
  public MyUserStreamAdapter(ClientModel model){
    this.model=model;
  }
  
  /**
   * 新着ツイートがあれば追加を依頼
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
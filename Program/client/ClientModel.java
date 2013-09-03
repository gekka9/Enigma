package client;

import twitter4j.Status;
import twitter4j.Twitter;

/**
 * モデル。1アカウントに1つModelのインスタンスを作る
 * @author gekka9
 *
 */
public class ClientModel {
  
  /**
   * Twitterインスタンスを束縛する
   */
  private Twitter twitter;
  
  /**
   * メインフレームを束縛する
   */
  private MainFrame frame;
  
  /**
   * 投稿フィールドを束縛する
   */
  private PostField field;
  
  /**
   * 現在の実行モードを束縛する
   */
  private Mode mode;
  
  /**
   * 自分のクライアント名の定数
   */
  public final static String CLIENT_NAME="enigma9";
  
  /**
   * 実行モード
   * @author gekka9
   */
  public enum Mode {NORMAL,NO_ENCRYPT,VIEW_LIMIT};

  /**
   * コンストラクタ
   * @param twitter 認証済みのTwitterインスタンス
   * @param mode 実行モード
   */
  public ClientModel(Twitter twitter,Mode mode){
    this.twitter=twitter;
    this.mode=mode;
  }

  /**
   * Twitterインスタンスを返す
   * @return Twitterインスタンス
   */
  public Twitter getTwitter() {
    return twitter;
  }

  /**
   * 投稿フィールドのセッター
   * @param field 投稿フィールド
   */
  public void setField(PostField field) {
    this.field=field;
  }
  
  /**
   * 投稿フィールドのゲッター
   * @return 投稿フィールド
   */
  public PostField getField(){
    return this.field;
  }
  
  /**
   * メインフレームのセッター
   * @param frame メインフレーム
   */
  public void setFrame(MainFrame frame) {
    this.frame=frame;
  }
  
  /**
   * つぶやきの追加をメインフレームが持つJListのモデルに依頼する
   * @param status 依頼するStatusインスタンス
   */
  public void post(Status status){
    //モードによって追加するかしないかを決める
    if(this.mode==Mode.VIEW_LIMIT){
      if(TweetRenderer.source2clientName(status.getSource()).equals(ClientModel.CLIENT_NAME)){
        this.frame.addPost(status);
      }else{
        return;
      }
    }else{
      this.frame.addPost(status);
    }
  }

  /**
   * Modeのゲッター
   * @return 現在の実行モードを返す
   */
  public Mode getMode() {
    return mode;
  }
  
  /**
   * MacOSXで動作しているかを返す
   * @return MacOSXならtrue
   */
  protected static boolean isMac() {
    String OSName = System.getProperty("os.name").toLowerCase();
    return OSName.startsWith("mac os x");
  }
}

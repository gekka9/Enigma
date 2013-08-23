package client;

import twitter4j.Status;

/**
 * Statusインスタンスとモデルインスタンスを保持する
 * @author gekka9
 *
 */
public class TweetData {
  public Status status;
  public ClientModel model;
  
  /**
   * コンストラクタ
   * @param status ステータス
   * @param model モデル
   */
  public TweetData(Status status , ClientModel model){
    this.status=status;
    this.model=model;
  }

}

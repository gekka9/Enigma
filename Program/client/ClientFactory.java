package client;

import client.ClientModel.Mode;
import twitter4j.Twitter;

/**
 * 与えられた認証済みTwitterインスタンスとモードからモデルを生成する
 * @author gekka9
 *
 */
public class ClientFactory {

  /**
   * Modelを束縛する
   */
  private ClientModel model;
  
  /**
   * メインフレームを束縛する
   */
  private MainFrame frame;
  
  /**
   * コンストラクタ
   * @param twitter 認証済みのTwitterインスタンス
   * @param mode 実行モード
   */
  public ClientFactory(Twitter twitter, Mode mode){
    this.model=new ClientModel(twitter,mode);
    this.frame = new MainFrame(this.model);
    this.model.setFrame(this.frame);
  }
  
  /**
   * Modelインスタンスを生成して返す
   * @return 生成したModelインスタンス
   */
  public ClientModel getClientModel(){
    return this.model;
  }

}

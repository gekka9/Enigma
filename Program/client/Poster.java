package client;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * 投稿を行うためのキーリスナー
 * @author otaki
 *
 */
public class Poster implements KeyListener{

  /**
   * 親テキストフィールド
   */
  private PostField field;
  
  /**
   * コンストラクタ
   * @param field 親テキストフィールド
   */
  public Poster(PostField field){
    this.field = field;
  }

  /**
   * シフト＋エンターでつぶやきを依頼し、フィールドを空にする
   */
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    int mod = e.getModifiersEx();
    if ((mod & InputEvent.META_DOWN_MASK) != 0 && key == KeyEvent.VK_ENTER){
      this.field.post();
    }else if ((mod & InputEvent.META_DOWN_MASK) != 0 && (mod & InputEvent.SHIFT_DOWN_MASK) != 0 && key == KeyEvent.VK_ENTER){

    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

}

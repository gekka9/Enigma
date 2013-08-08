package TwitterGUI;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import enigma.Enigma;

public class Poster implements KeyListener{

  private PostField field;
  
  public Poster(PostField field){
    this.field = field;
  } 
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    int mod = e.getModifiersEx();
    if ((mod & InputEvent.META_DOWN_MASK) != 0 && key == KeyEvent.VK_ENTER){
      try {
        this.field.post();
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
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

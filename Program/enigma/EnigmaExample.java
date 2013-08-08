package enigma;

import java.io.IOException;

public class EnigmaExample {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    Rotor rotor = new Rotor();
    Rotor reflecter = new Rotor();
    rotor.initialize(0, 5);
    reflecter.initialize(0, 5);
    //暗号化
    char[] target = "福田収真".toCharArray();
    for (char aChar: target){
      int result =rotor.inAside(rotor.getIndex(aChar));
      char resultChar = rotor.getChar(rotor.inBside(result));
      System.out.println(resultChar);
      //復号化
      int result1 =rotor.inAside(rotor.getIndex(resultChar));
      char resultChar1 = rotor.getChar(rotor.inBside(result1));
      System.out.println(resultChar1);
    } 
  }

}

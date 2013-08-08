package enigma;

import java.io.IOException;

public class EnigmaExample {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    Rotor rotor = new Rotor(false);
    Rotor reflector = new Rotor(true);
    rotor.initialize(0, 5);
    reflector.initialize(0, 5);
    //暗号化
    char[] target = "福田収真".toCharArray();
    for (char aChar: target){
      int result =rotor.inAside(rotor.getIndex(aChar));
      result = reflector.inAside(result);
      char resultChar = rotor.getChar(rotor.inBside(result));
      System.out.println(resultChar);
      //復号化
      int result1 =rotor.inAside(rotor.getIndex(resultChar));
      result1 = reflector.inBside(result1);
      char resultChar1 = rotor.getChar(rotor.inBside(result1));
      System.out.println(resultChar1);
    } 
  }

}

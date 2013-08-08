package enigma;

import java.io.IOException;
import java.util.ArrayList;

public class Enigma {

  Rotor rotor;
  Rotor reflector;
  public Enigma(){
    try {
      this.rotor = new Rotor(false);
      this.reflector = new Rotor(true);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void initialize(long seed, int offset) throws IOException{
    rotor.initialize(seed, offset);
    reflector.initialize(seed, offset);
  }
  
  public String encrypt (String target){
    char[] targetArray = target.toCharArray();
    StringBuilder resultBuilder = new StringBuilder();
    for (char aChar: targetArray){
      System.out.println(aChar);
      if(rotor.isContains(aChar)){
        int result =rotor.inAside(rotor.getIndex(aChar)); 
        result = reflector.inAside(result);
        char resultChar = rotor.getChar(rotor.inBside(result));
        System.out.println(resultChar);
        resultBuilder.append(resultChar);
      }else{
        resultBuilder.append(aChar);
      }
    } 
    return resultBuilder.toString();
  }
  public String decrypt(String target){
    char[] targetArray = target.toCharArray();
    StringBuilder resultBuilder = new StringBuilder();
    for (char aChar: targetArray){
      if(rotor.isContains(aChar)){
        //復号化
        int result =rotor.inAside(rotor.getIndex(aChar));
        result = reflector.inBside(result);
        char resultChar = rotor.getChar(rotor.inBside(result));
        System.out.println(resultChar);
        resultBuilder.append(resultChar);
      }else{
        resultBuilder.append(aChar);
      }
    } 
    return resultBuilder.toString();
  }
  
  public static void main(String[] args) throws IOException{
    Enigma enigma = new Enigma();
    enigma.initialize(0, 5);
    String target = "abc bkhoi npigrwnp";
    String[] dividedTarget = target.split(" ");
    ArrayList<String> encryptedList=new ArrayList<String>();
    for(String aString:dividedTarget){
      String result = enigma.encrypt(aString);
      System.out.println(result);
      encryptedList.add(result);
    }
    enigma.initialize(0, 5);
    ArrayList<String> resultList=new ArrayList<String>();
    for(String aString:encryptedList){
      String result = enigma.decrypt(aString);
      System.out.println(result);
      resultList.add(result);
    }
    for(String aString:resultList){
      System.out.print(aString+" ");
    }
  }
  
}

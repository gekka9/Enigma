package enigma;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;

/**
 * なんちゃってエニグマ暗号器
 * @author gekka9
 *
 */
public class Enigma {
  /**
   * ローター
   */
  private Rotor rotor;
  /**
   * リフレクター
   */
  private Rotor reflector;
  /**
   * 扱う文字種
   * @author gekka9
   *
   */
  public enum CharacterType{HIRAGANA, KATAKANA, KANJI, ALPHABET,NUMBER,OTHER}
  
  /**
   * モードを受け取ってそのモードのインスタンスを生成する
   * @param mode
   */
  public Enigma(CharacterType mode){
    this.rotor = new Rotor(false,mode);
    this.reflector = new Rotor(true,mode);
  }
  /**
   * 初期化する
   * @param seed シード値
   * @param offset
   */
  public void initialize(long seed, int offset){
    Random random = new Random(seed);
    rotor.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
    reflector.initialize(random.nextLong(), Math.round(random.nextFloat()*10));
  }
  
  /**
   * 暗号化
   * @param target 対象の文字列
   * @return 暗号化後の文字列
   */
  public String encrypt (String target){
    char[] targetArray = target.toCharArray();
    //結果を収めるビルダー
    StringBuilder resultBuilder = new StringBuilder();
    //一文字ずつループ
    for (char aChar: targetArray){
      //ローターに文字がなければはじく
      if(rotor.isContains(aChar)){
        //ローターに入力
        int result =rotor.inAside(rotor.getIndex(aChar)); 
        //ローターからの出力をリフレクターに入力
        result = reflector.inAside(result);
        //リフレクターからの応答をローターに入力
        char resultChar = rotor.getChar(rotor.inBside(result));
        //ローターからの応答を暗号化後の文字としてappend
        resultBuilder.append(resultChar);
      }else{
        //対応していない場合はそのまま収める
        resultBuilder.append(aChar);
      }
    } 
    //ローターを1目盛り回転する
    rotor.rotate(1);
    return resultBuilder.toString();
  }
  
  /**
   * 復号化。処理は暗号化と同様
   * @param target 対象の文字列
   * @return 復号化後の文字列
   */
  public String decrypt(String target){
    char[] targetArray = target.toCharArray();
    StringBuilder resultBuilder = new StringBuilder();
    for (char aChar: targetArray){
      if(rotor.isContains(aChar)){
        int result =rotor.inAside(rotor.getIndex(aChar));
        result = reflector.inBside(result);
        char resultChar = rotor.getChar(rotor.inBside(result));
        resultBuilder.append(resultChar);
      }else{
        resultBuilder.append(aChar);
      }
    }
    rotor.rotate(1);
    return resultBuilder.toString();
  }
  
  /**
   * テスト用クラス
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    Enigma enigma = new Enigma(CharacterType.ALPHABET);
    enigma.initialize(0, 5);
    String target = "abc bkhoi npigrwnp";
    String[] dividedTarget = target.split(" ");
    ArrayList<String> encryptedList=new ArrayList<String>();
    for(String aString:dividedTarget){
      String result = enigma.encrypt(aString);
      encryptedList.add(result);
    }
    enigma.initialize(0, 5);
    ArrayList<String> resultList=new ArrayList<String>();
    for(String aString:encryptedList){
      String result = enigma.decrypt(aString);
      resultList.add(result);
    }
  }
  
}

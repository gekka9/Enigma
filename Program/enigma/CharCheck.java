package enigma;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enigma.Enigma.CharacterType;


public class CharCheck {

  //文字種のチェックを行うクラス
  public static Enigma.CharacterType check(final char target){
      int targetChar = getSJISByte(target);
      
      //半角数字
      Pattern p = Pattern.compile("[0-9]");
      Matcher m = p.matcher(String.valueOf(target));
      if(m.matches()){
        return CharacterType.NUMBER;
      }
      
      //半角アルファベット
      p = Pattern.compile("[a-z|A-Z]");
      m = p.matcher(String.valueOf(target));
      if(m.matches()){
        return CharacterType.ALPHABET;
      }
      //つついてはいけないユニコードの闇
      if(targetChar == 0x3f){
        return CharacterType.OTHER;
      }
      //半角カナ
      else if((0xA6 <= targetChar) && (targetChar <= 0xDD)) {
        return CharacterType.KATAKANA;
      }
      //全角の0~9
      else if((0x824F <= targetChar) && (targetChar <= 0x8258)) {
        return CharacterType.NUMBER;
      }
      //全角のA~Z
      else if((0x8260 <= targetChar) && (targetChar <= 0x8279)) {
        return CharacterType.ALPHABET;
      }
      //全角のa~z
      else if((0x8281 <= targetChar) && (targetChar <= 0x829a)) {
        return CharacterType.ALPHABET;
      }
      //ぁ〜ん
      else if((0x829f <= targetChar) && (targetChar <= 0x82f1)) {
        return CharacterType.HIRAGANA;
      }
      //ァ〜ヶ
      else if((0x8340 <= targetChar) && (targetChar <= 0x8396)) {
        return CharacterType.KATAKANA;
      }
      //第一水準
      else if((0x889f <= targetChar) && (targetChar <= 0x9872)) {
        return CharacterType.KANJI;
      }else{
        return CharacterType.OTHER;
      }
  }
   
   
  /**
   * Shift-JISでバイトに変換する.
   * @param c 変換対象文字
   * @return 変換後文字
   */
  private static int getSJISByte(final char c) {   
      try {
          //Windows-31Jでbyteに変換
          byte[] bArray = String.valueOf(c).getBytes("Windows-31J");
          int targetChar;
          if (bArray.length == 1) {
              //1バイト文字
              targetChar = bArray[0] & 0xFF;
          } else {
              //2バイト文字
              targetChar = ((bArray[0] & 0xFF) << 8) | (bArray[1] & 0xFF);
          }
          return targetChar;
      } catch (UnsupportedEncodingException e) {
          return 0;
      }
  }
  
  //テスト用クラス
  public static void main(String[] args) throws IOException {
    File file = new File("first.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));
    String str = br.readLine();
    StringBuilder builder = new StringBuilder();
    while(str != null){
      //System.out.println(str);
      builder.append(str);
      str = br.readLine();
    }
    br.close();
    char[] target = builder.toString().toCharArray();
    for(char aChar:target){
      System.out.println(aChar+":"+check(aChar));
    }
  }
}

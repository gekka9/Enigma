package TwitterGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class CharCheck {

  /**
   * @param args
   * @throws IOException 
   */
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
      System.out.println(aChar+":"+checkChar(aChar));
    }

    //System.out.println(checkChar("𠮟"));
  }
  /**
   * 文字種チェック.
   * @param target チェック対象文字列
   * @return 文字種不正 false
   */
  public static boolean checkChar(final char target){
   
      char c = target;
      int targetChar = getSJISByte(c);
      //つついてはいけないユニコードの闇
      if(targetChar == 0x3f){
        System.out.println("????????????????????");
        return false;
      }
      //全角の0~9
      else if((0x824F <= targetChar) && (targetChar <= 0x8258)) {
        return true;
      }
      //全角のA~Z
      else if((0x8260 <= targetChar) && (targetChar <= 0x8279)) {
        return true;
      }
      //全角のa~z
      else if((0x8281 <= targetChar) && (targetChar <= 0x829a)) {
        return true;
      }
      //ぁ〜ん
      else if((0x829f <= targetChar) && (targetChar <= 0x82f1)) {
        return true;
      }
      //ァ〜ヶ
      else if((0x8340 <= targetChar) && (targetChar <= 0x8396)) {
        return true;
      }
      //第一水準
      else if((0x889f <= targetChar) && (targetChar <= 0x9872)) {
        return true;
      }else{
        return false;
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
      } catch (Exception e) {
          return 0;
      }
  }

}

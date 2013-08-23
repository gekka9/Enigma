package client;


import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * AccessTokenを暗号化して直列化するためのクラス
 * @author gekka9
 *
 */
public class EnigmaToken implements Serializable{

  /**
   * 生成されたシリアルバージョンUID
   */
  private static final long serialVersionUID = 7278452565960358860L;
  
  /**
   * アクセストークン
   */
  private String token;
  
  /**
   * アクセスシークレット
   */
  private String secret;
  
  /**
   * コンストラクタ
   * @param token アクセストークン
   * @param secret アクセスシークレット
   */
  public EnigmaToken(String token, String secret) {
    this.token=token;
    this.secret=secret;
  }
  
  /**
   * ファイルへの書き出し（永続化）
   * @param stream ObjectOutputStream
   */
  private void writeObject(java.io.ObjectOutputStream stream){
    /*
    String key = "aaaaaaaaaabbbbbb";
    byte[] keyBytes = key.getBytes();
    
    //暗号化
    byte[] encryptedToken = encryptECB(this.token.getBytes(),keyBytes);
    byte[] encryptedSecret = encryptECB(this.secret.getBytes(),keyBytes);
    
    //暗号化した文字列をフィールドへ
    this.token=new String(encryptedToken);
    this.secret=new String(encryptedSecret);
    */
    //書き込み
    try {
      stream.defaultWriteObject();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 読み込み
   * @param stream ObjectOutputStream
   */
  private void readObject(java.io.ObjectInputStream stream)
  {
    //読み込み
    try {
      stream.defaultReadObject();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    /*
    String key = "aaaaaaaaaabbbbbb"; // <-128bitのキー
    byte[] keyBytes = key.getBytes(); // バイト列に変換
    
    //復号化
    byte[] decryptedToken = decryptECB(this.token.getBytes(), keyBytes);
    byte[] decryptedSecret = decryptECB(this.secret.getBytes(), keyBytes);
    
    System.out.println(new String(decryptedToken, "UTF-8"));
    //復号化したものをフィールドへ
    this.token=new String(decryptedToken, "UTF-8");
    this.secret=new String(decryptedSecret, "UTF-8");
    */
  }
  
  /**
   * 暗号化
   * @param data 
   * @param secret_key
   * @return
   */
  private static byte[] encryptECB(byte[] data, byte[] secret_key) {

    SecretKeySpec sKey = new SecretKeySpec(secret_key, "ARCFOUR");
    try {
     Cipher cipher = Cipher.getInstance("ARCFOUR");
     cipher.init(Cipher.ENCRYPT_MODE, sKey);
     return cipher.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
     e.printStackTrace();
    } catch (NoSuchPaddingException e) {
     e.printStackTrace();
    } catch (InvalidKeyException e) {
     e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
     e.printStackTrace();
    } catch (BadPaddingException e) {
     e.printStackTrace();
    }
    return new byte[0];
}

  /**
   * 復号化
   * @param encData
   * @param secret_key
   * @return
   */
private static byte[] decryptECB(byte[] encData, byte[] secret_key) {
    SecretKeySpec sKey = new SecretKeySpec(secret_key, "ARCFOUR");
    try {
      Cipher cipher = Cipher.getInstance("ARCFOUR");
     cipher.init(Cipher.DECRYPT_MODE, sKey);
     return cipher.doFinal(encData);
    } catch (NoSuchAlgorithmException e) {
     e.printStackTrace();
    } catch (NoSuchPaddingException e) {
     e.printStackTrace();
    } catch (InvalidKeyException e) {
     e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
     e.printStackTrace();
    } catch (BadPaddingException e) {
     e.printStackTrace();
    }
    return new byte[0];
  }
  public String getToken(){
    return this.token;
  }
  public String getSecret(){
    return this.secret;
  }
}

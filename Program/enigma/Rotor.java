package enigma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

public class Rotor {
  ArrayList<Character> charList;
  HashMap<Character,Character> link;
  
  Rotor() throws IOException{
    this.charList=new ArrayList<Character>();
    this.link=new HashMap<Character,Character>();
    File file = new File("first.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));
    String str = br.readLine();
    while(str != null){
      char[] strChar=str.toCharArray();
      this.charList.add(strChar[0]);
      str = br.readLine();
    }
    br.close();
  }
  
  public void initialize(long seed, int offset){
    ArrayList<Character> hashedList=(ArrayList<Character>) this.charList.clone();
    Collections.shuffle(hashedList, new Random(seed));
    Collections.rotate(this.charList, offset);
    int i=0;
    for(char aChar:this.charList){
      this.link.put(aChar, hashedList.get(i));
      i++;
    }
  }
  public int inAside(int index){
    char aChar=this.charList.get(index);
    aChar = this.link.get(aChar);
    int result = this.charList.indexOf(aChar);
    return result;
  }
  
  public int inBside(int index){
    char aChar=this.charList.get(index);
    int result=0;
    if(this.link.containsValue(aChar)){
      Set<Entry<Character,Character>> keys = this.link.entrySet();
      
      Iterator<Entry<Character, Character>> iterator = keys.iterator();
      while(iterator.hasNext()){
        //キーと値をセットを持つ、Map.Entry型のオブジェクトを取得する
        Entry<Character,Character> entry = iterator.next();
            
        //取得したMap.Entry型のオブジェクトからキーと値を取得する
        Character key = entry.getKey();
        Character value = entry.getValue();
        if(aChar ==value){
          result=this.charList.indexOf(key);
        }
      }
    }
    return result;
  }
  public char getChar(int index){
    return this.charList.get(index);
  }
  public int getIndex(char target){
    return this.charList.indexOf(target);
  }
}

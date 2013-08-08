package enigma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

public class Rotor {
  ArrayList<Character> charList;
  HashMap<Character,Character> link;
  
  Rotor(){
    this.charList=new ArrayList<Character>();
    this.link=new HashMap<Character,Character>();
  }
  
  public void initialize(long seed, int offset){
    ArrayList<Character> hashedList=(ArrayList<Character>) this.charList.clone();
    Collections.shuffle(hashedList, new Random(seed));
    Collections.rotate(this.charList, offset);
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
}

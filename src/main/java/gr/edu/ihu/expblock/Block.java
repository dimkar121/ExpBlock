/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.edu.ihu.expblock;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class Block {

    ArrayList<Record> arr = new ArrayList<Record>();
    String key;
    int recNo = 0;    
    int lastRoundUsed = 0;
    int degree = 0;     
    double decay = 0;
    
    public Block(String key) {
        this.key = key;
    }

    public Record get(int i) {
        return arr.get(i);
    }

    public void put(Record rec, int w, int round) {
        //Do the matching
        for (int i=0;i<arr.size();i++){
            Record rec1 = arr.get(i);
            //Use any matching rule            
            if ((rec.name.equals(rec1.name)  && (rec.surname.equals(rec1.surname)))){
                //Report a match
                System.out.println("Matching identified.");
            }
            
        }
        
        if (arr.size() == w) {
            ArrayList<Record> newArr = new ArrayList<Record>();
            int evicted = 0;
            Random r = new Random();
            for (int i = 0; i < arr.size(); i++) {
                int chance = r.nextInt(2);
                Record rec1 = arr.get(i);
                if (chance == 1) {
                    rec1.survivals++;
                    newArr.add(rec1);
                } else {
                    evicted++;
                }
            }
            this.arr = newArr;
        } else {
            arr.add(rec);
        }
        
       this.recNo++;       
       this.lastRoundUsed = round;
    }

    public void setDegree(int avg, int currentRound){
        decay = Math.pow(Math.E,  this.lastRoundUsed - currentRound) ;        
        //degree =( (this.recNo + (this.recNoPrev*decay)) / avg) - 1;      
        degree = (int) Math.floor((this.recNo*decay) / avg) ;      
    }
    
    
    public int getSize() {
        return arr.size();
    }

    public void set(ArrayList<Record> arr) {
        this.arr = arr;
    }

}

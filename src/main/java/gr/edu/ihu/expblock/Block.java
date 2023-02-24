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
    double activity = 0;
    int comparisonsNo = 0;

    public Block(String key) {
        this.key = key;
    }

    public Record get(int i) {
        return arr.get(i);
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public int editDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= str2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                distance[i][j] = min(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == (str2.charAt(j - 1)) ? 0 : 1))
                );
            }
        }

        return distance[str1.length()][str2.length()];
    }

    public int put(Record rec, int w, int round) {
        //Do the matching
        int matchingPairsNo = 0;
        for (int i = 0; i < arr.size(); i++) {
            Record rec1 = arr.get(i);
            //Use any matching rule 
            if (!rec1.origin.equals(rec.origin)) { //Compare only records which orginate from different data sources
                this.comparisonsNo++;
                if ((editDistance(rec1.surname, rec.surname) < 3) && (editDistance(rec1.name, rec.name) < 3)) {
                    //Report a match
                    //System.out.println("A matching pair identified.");
                    if (rec.getIdNo().equals(rec1.getIdNo())) {
                        //System.out.println("A truly matching pair identified.");
                        System.out.println(rec1.id+" "+rec1.surname + " "+rec1.name+" "+rec1.town+ " "+rec1.poBox + " matched with " +rec.id+" "+ rec.surname+ " "+rec.name+" "+rec.town+ " "+rec.poBox) ;                        
                        matchingPairsNo++;
                    }
                }
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
        return matchingPairsNo;
    }

    public void setDegree(int avg, int currentRound) {
        this.activity = this.lastRoundUsed / currentRound;
        this.degree = (int) Math.floor((this.recNo * this.activity) / avg);
    }

    public int getSize() {
        return arr.size();
    }

    public void set(ArrayList<Record> arr) {
        this.arr = arr;
    }

}

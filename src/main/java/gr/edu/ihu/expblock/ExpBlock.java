/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.edu.ihu.expblock;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class ExpBlock {

    /**
     * @param args the command line arguments
     */
    public HashMap<String, Block> map = new HashMap<String, Block>();
    public int w = 1000;
    public int b = 150;
    public int globalRecNo = 0;
    public int occupied = 0;
    public double xi = .1;
    public int currentRound = 0;

    public void put(String key, Record rec) {
        //System.out.println("occupied="+this.occupied);
        if (this.occupied == b) {
            Random r = new Random();

            System.out.println();
            Object[] values = map.values().toArray();
            //for (int vv = 0; vv < values.length; vv++) {
            //  Block block = (Block) values[vv];
            //double freq = block.recNo * 1.0 / globalRecNo;
            //System.out.println(block.key+" "+block.recNo+" freq="+freq);
            //}

            int avg = this.globalRecNo / b;
            if (avg == 0) {
                avg = 1;
            }
            System.out.println();
            //System.out.println("Initiate the eviction process of blocks. current round=" + currentRound + "  globalRecNo=" + globalRecNo + " avg=" + avg);
            int v = 0;
            while (v < (int) Math.floor((xi * b))) {
                Block block = (Block) values[r.nextInt(values.length)];
                String key1 = block.key;
                block.setDegree(avg, currentRound);
                if (block.degree <= 0) {
                    map.remove(key1);
                    //System.out.println("REMOVED Block " + key1 + " degree=" + block.degree + " recs=" + block.recNo + " decay=" + block.decay + " lastroundused=" + block.lastRoundUsed);
                    v++;
                } else {
                    block.recNo = block.recNo - avg;
                    System.out.println("NOT REMOVED Block " + key1 + " degree=" + block.degree + " recs=" + block.recNo + " decay=" + block.decay + " lastroundused=" + block.lastRoundUsed);
                }

            }

            this.occupied = this.occupied - ((int) Math.floor((xi * b)));
            if (currentRound % 40 == 0)
                this.globalRecNo = 0;
           
            currentRound++;
        }
        this.globalRecNo++;
        if (map.containsKey(key)) {
            Block block = map.get(key);
            block.put(rec, w, currentRound);
        } else {
            Block block = new Block(key);
            this.occupied++;
            block.put(rec, w, currentRound);
            map.put(key, block);
        }

    }

    public static void main(String[] args) {
        // TODO code application logic here
        ExpBlock e = new ExpBlock();
        System.out.println("b=" + e.b);
        int recNo = 0;

        try {
            CSVReader reader = new CSVReader(new FileReader("c:\\data\\ncvoters_A.txt"));
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                if (lineInArray.length == 5) {
                    String name = lineInArray[2];
                    String surname = lineInArray[1];
                    String region = lineInArray[4];
                    String id = lineInArray[0];
                    //System.out.println(id+" "+name+" "+surname+" "+region);
                    if ((surname.length() > 4)) {
                        recNo++;
                        String key = surname.substring(0, 3);
                        Record rec = new Record();
                        rec.id = id;
                        rec.name = name;
                        rec.surname = surname;
                        rec.region = region;
                        e.put(key, rec);
                    }
                }
            }
            System.out.println(" ==================== processed " + recNo + " in total.");
        } catch (Exception ex) {
            //ex.printStackTrace();
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.edu.ihu.expblock;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    public int b = 1000;
    public int globalRecNo = 0;
    public int occupied = 0;
    public double xi = .1;
    public int currentRound = 1;
    public int matchingPairsNo = 0;
    public int trulyMatchingPairsNo = 1000000;
    public MinHash minHash = new MinHash();
    public static FileWriter writer;
    
    public ExpBlock() {
        try {
            writer = new FileWriter("results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void put(Record rec) {
        //System.out.println("occupied="+this.occupied);
        if (this.occupied == b) {
            Random r = new Random();

            Object[] values = map.values().toArray();

            int avg = this.globalRecNo / b;
            if (avg == 0) {
                avg = 1;
            }

            //System.out.println("Initiate the eviction process of blocks. current round=" + currentRound + "  globalRecNo=" + globalRecNo + " avg=" + avg);
            int v = 0;
            while (v < (int) Math.floor((xi * b))) {
                Block block = (Block) values[r.nextInt(values.length)];
                String key1 = block.key;
                block.setDegree(avg, currentRound);
                if (block.degree <= 0) {
                    map.remove(key1);
                    v++;
                } else {
                    block.recNo = block.recNo - avg;
                }

            }

            this.occupied = this.occupied - ((int) Math.floor((xi * b)));           

            currentRound++;
        }
        this.globalRecNo++;
        String key = rec.getBlockingKey(minHash);
        if (map.containsKey(key)) {
            Block block = map.get(key);
            int mp = block.put(rec, w, currentRound, writer);
            this.matchingPairsNo = this.matchingPairsNo + mp;
        } else {
            Block block = new Block(key);
            this.occupied++;
            int mp = block.put(rec, w, currentRound, writer);
            map.put(key, block);
            this.matchingPairsNo = this.matchingPairsNo + mp;
        }

    }

    public static Record prepare(String[] lineInArray) {
        String name = lineInArray[2];
        String surname = lineInArray[1];
        String address = lineInArray[3];
        String town = lineInArray[4];
        String poBox = lineInArray[5];
        String id = lineInArray[0];
        Record rec = new Record();
        rec.id = id;
        rec.name = name;
        rec.surname = surname;
        rec.town = town;
        rec.poBox = poBox;
        rec.origin = id.charAt(0) + "";
        //System.out.println(id+" "+name+" "+surname+" "+town+" "+rec.origin);               
        return rec;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        ExpBlock e = new ExpBlock();
        System.out.println("Running ExpBlock using b=" + e.b + " w=" + e.w);
        int recNoA = 0;
        int recNoB = 0;
        long startTime = System.currentTimeMillis();
        long startTimeCycle = System.currentTimeMillis();
        try {
            CSVReader readerA = new CSVReader(new FileReader("test_voters_A.txt"));
            CSVReader readerB = new CSVReader(new FileReader("test_voters_B.txt"));

            String[] lineInArray1;
            String[] lineInArray2;
            int c = 0;
            while (true) {
                lineInArray1 = readerA.readNext();
                if (lineInArray1 != null) {
                    if (lineInArray1.length == 6) {
                        String surname = lineInArray1[1];
                        recNoA++;
                        //System.out.println("Working on "+recNoA+" record from A.");
                        Record rec1 = prepare(lineInArray1);
                         e.put(rec1);
                    }
                }
                lineInArray2 = readerB.readNext();                
                if (lineInArray2 != null) {
                    //String[] lineInArray2 = readerB.readNext();
                    if (lineInArray2.length == 6) {
                        String surname2 = lineInArray2[1];
                        recNoB++;
                        //System.out.println("Working on "+recNoB+" record from B.");                                                        
                        Record rec2 = prepare(lineInArray2);
                        e.put(rec2);
                    }
                }

                if ((recNoA + recNoB) % 100000 == 0) {
                    long stopTimeCycle = System.currentTimeMillis();
                    long elapsedTime = stopTimeCycle - startTimeCycle;
                    System.out.println("====== processed " + (recNoA + recNoB) + " records in " + (elapsedTime / 1000) + " seconds.");
                    System.out.println("====== identified " + e.matchingPairsNo+" matching pairs.");
                    startTimeCycle = System.currentTimeMillis();
                }
                if ((lineInArray1== null) && (lineInArray2==null))
                    break;
            }
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            e.writer.close();
            System.out.println(" ==================== elapsed time " + (elapsedTime / 1000) + " seconds.");
            System.out.println(" ==================== processed " + (recNoA + recNoB) + " records in total.");
            System.out.println(" ==================== processed " + (recNoA) + " records from A.");
            System.out.println(" ==================== processed " + (recNoB) + " records from B.");
            System.out.println(" ==================== identified " + e.matchingPairsNo + " in total. Recall = " + (e.matchingPairsNo * 1.0 / e.trulyMatchingPairsNo));
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                e.writer.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

        }
    }

}

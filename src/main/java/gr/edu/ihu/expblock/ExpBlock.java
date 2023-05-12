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
import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Administrator
 */
public class ExpBlock {

    /**
     * @param args the command line arguments
     */
    public double epsilon;
    public double delta = 0.1;
    public double q;
    public int w;
    public int b;
    public int globalRecNo = 0;
    public int occupied = 0;
    public double xi = .08;
    public int currentRound = 1;
    public int matchingPairsNo = 0;
    public int trulyMatchingPairsNo = 1000000;
    public MinHash minHash = new MinHash();
    public static FileWriter writer;
    public Block[] arr;
    IntStream rS;
    int[] r;
    int noRandoms = 5000;

    public ExpBlock(double epsilon, double q, int b) {
        try {
            this.epsilon = epsilon;
            this.b = b;
            this.q = q;
            this.arr = new Block[this.b];

            for (int i = 0; i < 10; i++) {
                IntStream rS = new SplittableRandom().ints(this.noRandoms / 10, 0, this.b);
                int[] r = rS.toArray();
                Arrays.sort(r);
                this.r = ArrayUtils.addAll(this.r, r);
            }
            this.w = (int) Math.ceil(3 * Math.log(2 / this.delta) / (this.q * (this.epsilon * this.epsilon)));
            writer = new FileWriter("results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void put(Record rec) {
        //System.out.println("occupied="+this.occupied);
        if (this.occupied == b) {
            int avg = this.globalRecNo / b;
            if (avg == 0) {
                avg = 1;
            }

            int v = 0;
            long startTime = System.nanoTime();

            int j = 0;
            int i = r[j];
            while (v < (int) Math.floor((xi * b))) {
                Block block = arr[i];
                if (block == null) {
                    j++;
                    if (j == this.noRandoms) {
                        j = 0;
                    }
                    i = r[j];
                    continue;
                }
                block.setDegree(avg, currentRound);
                if (block.degree <= 0) {
                    arr[i] = null;
                    v++;
                } else {
                    block.recNo = block.recNo - avg;
                }
                j++;
                if (j == this.noRandoms) {
                    j = 0;
                }
                i = r[j];
            }

            long stopTime = System.nanoTime();
            long elapsedTime = stopTime - startTime;
            this.occupied = this.occupied - ((int) Math.floor((xi * b)));

            currentRound++;
        }
        this.globalRecNo++;
        String key = rec.getBlockingKey(minHash);

        long startTime = System.nanoTime();
        boolean blockExists = false;
        int emptyPos = -1;
        for (int i = 0; i < arr.length; i++) {
            Block block = arr[i];
            if (block != null) {
                if (block.key.equals(key)) {
                    int mp = block.put(rec, w, currentRound, writer);
                    this.matchingPairsNo = this.matchingPairsNo + mp;
                    blockExists = true;
                    break;
                }
            } else {
                emptyPos = i;
            }
        }
        if (!blockExists) {
            Block newBlock = new Block(key, this.q);
            this.occupied++;
            int mp = newBlock.put(rec, w, currentRound, writer);
            if (emptyPos != -1) {
                arr[emptyPos] = newBlock;
            } else {
                for (int i = 0; i < this.b; i++) {
                    if (arr[i] == null) {
                        arr[i] = newBlock;
                        break;
                    }
                }
            }
            this.matchingPairsNo = this.matchingPairsNo + mp;
        }
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
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
        ExpBlock e = new ExpBlock(0.1, 2.0 / 3, 1000);
        System.out.println("Running ExpBlock using b=" + e.b + " w=" + e.w);
        int recNoA = 0;
        int recNoB = 0;
        long startTime = System.currentTimeMillis();
        long startTimeCycle = System.currentTimeMillis();
        try {
            CSVReader readerA = new CSVReader(new FileReader("c:\\data\\test_voters_A.txt"));
            CSVReader readerB = new CSVReader(new FileReader("c:\\data\\test_voters_B.txt"));

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
                    System.out.println("====== identified " + e.matchingPairsNo + " matching pairs.");
                    startTimeCycle = System.currentTimeMillis();
                }
                if ((lineInArray1 == null) && (lineInArray2 == null)) {
                    break;
                }
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
